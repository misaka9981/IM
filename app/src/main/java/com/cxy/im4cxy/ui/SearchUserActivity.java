package com.cxy.im4cxy.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.cxy.im4cxy.R;
import com.cxy.im4cxy.adapter.OnRecyclerViewListener;
import com.cxy.im4cxy.adapter.SearchUserAdapter;
import com.cxy.im4cxy.base.ParentWithNaviActivity;
import com.cxy.im4cxy.bean.User;
import com.cxy.im4cxy.model.BaseModel;
import com.cxy.im4cxy.model.UserModel;
import com.orhanobut.logger.Logger;

import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;


public class SearchUserActivity extends ParentWithNaviActivity {

    @BindView(R.id.et_find_name)
    EditText et_find_name;
    @BindView(R.id.sw_refresh)
    SwipeRefreshLayout sw_refresh;
    @BindView(R.id.btn_search)
    Button btn_search;
    @BindView(R.id.rc_view)
    RecyclerView rc_view;
    LinearLayoutManager layoutManager;
    SearchUserAdapter adapter;

    @Override
    protected String title() {
        return "搜索用户";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_user);
        initNaviView();
        adapter = new SearchUserAdapter();
        layoutManager = new LinearLayoutManager(this);
        rc_view.setLayoutManager(layoutManager);
        rc_view.setAdapter(adapter);
        sw_refresh.setEnabled(true);
        sw_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                query();
            }
        });
        adapter.setOnRecyclerViewListener(new OnRecyclerViewListener() {
            @Override
            public void onItemClick(int position) {
                Bundle bundle = new Bundle();
                User user = adapter.getItem(position);
                bundle.putSerializable("u", user);
                startActivity(UserInfoActivity.class, bundle, false);
            }

            @Override
            public boolean onItemLongClick(int position) {
                return true;
            }
        });
    }

    @OnClick(R.id.btn_search)
    public void onSearchClick(View view) {
        sw_refresh.setRefreshing(true);
        query();
    }

    public void query() {
        String name = et_find_name.getText().toString();
        if (TextUtils.isEmpty(name)) {
            toast("请填写用户名");
            sw_refresh.setRefreshing(false);
            return;
        }
        UserModel.getInstance().queryUsers(name, BaseModel.DEFAULT_LIMIT,
                new FindListener<User>() {
                    @Override
                    public void done(List<User> list, BmobException e) {
                        if (e == null) {
                            sw_refresh.setRefreshing(false);
                            adapter.setDatas(list);
                            adapter.notifyDataSetChanged();
                            if (list.size()==0){
                                toast("查无此人");
                            }
                        } else {
                            sw_refresh.setRefreshing(false);
                            adapter.setDatas(null);
                            adapter.notifyDataSetChanged();
                            Logger.e(e.getMessage());
                        }
                    }
                }


        );
    }

}
