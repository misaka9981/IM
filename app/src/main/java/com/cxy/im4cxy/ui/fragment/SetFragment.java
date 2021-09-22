package com.cxy.im4cxy.ui.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cxy.im4cxy.R;
import com.cxy.im4cxy.base.ParentWithNaviFragment;
import com.cxy.im4cxy.bean.User;
import com.cxy.im4cxy.model.UserModel;
import com.cxy.im4cxy.mvp.bean.Installation;
import com.cxy.im4cxy.ui.LogActivity;
import com.cxy.im4cxy.util.BmobUtils;
import com.orhanobut.logger.Logger;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.newim.BmobIM;
import cn.bmob.v3.BmobInstallationManager;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;


public class SetFragment extends ParentWithNaviFragment {

    @BindView(R.id.v_top)
    View mVTop;
    @BindView(R.id.tv_left)
    ImageView mTvLeft;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.tv_right)
    TextView mTvRight;
    @BindView(R.id.btn_logout)
    Button mBtnLogout;
    @BindView(R.id.civ_avatar)
    CircleImageView mCivAvatar;
    @BindView(R.id.tv_username)
    TextView mTvUsername;
    @BindView(R.id.btn_diary)
    Button mBtnDiary;
    @BindView(R.id.btn_diary_record)
    Button mBtnDiaryRecord;
    @BindView(R.id.btn_cost)
    Button mBtnCost;
    @BindView(R.id.btn_cost_record)
    Button mBtnCostRecord;

    @Override
    protected String title() {
        return "我的";
    }

    public static SetFragment newInstance() {
        SetFragment fragment = new SetFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public SetFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_set, container, false);
        initNaviView();
        ButterKnife.bind(this, rootView);
        String username = UserModel.getInstance().getCurrentUser().getUsername();
        mTvUsername.setText(TextUtils.isEmpty(username) ? "" : username);
        if (UserModel.getInstance().getCurrentUser().getAvatar() != null) {
            Glide.with(getActivity()).load(UserModel.getInstance().getCurrentUser().getAvatar().getFileUrl()).into(mCivAvatar);
        } else {
            Glide.with(getActivity()).load(R.mipmap.icon_message_press).into(mCivAvatar);
        }

        return rootView;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @OnClick({R.id.civ_avatar, R.id.btn_logout, R.id.btn_diary, R.id.btn_diary_record, R.id.btn_cost, R.id.btn_cost_record,R.id.tv_username})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.btn_logout:

                modifyInstallationUser();

                break;

            case R.id.tv_username:
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                      builder.setTitle("修改用户名");
                       final EditText et = new EditText(getActivity());
                        et.setHint(mTvUsername.getText().toString());
                     et.setSingleLine(true);
                      builder.setView(et);
                        builder.setNegativeButton("取消",null);
                         builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
             @Override
             public void onClick(DialogInterface dialog, int which) {
                                  String name = et.getText().toString();
                                  if (!TextUtils.isEmpty(name)){
                                      User currentUser = BmobUser.getCurrentUser(User.class);
                                      currentUser.setUsername(name);
                                      currentUser.update(new UpdateListener() {
                                          @Override
                                          public void done(BmobException e) {
                                              if (e==null){
                                                  Toast.makeText(getActivity(),"修改成功！",Toast.LENGTH_SHORT).show();
                                                  mTvUsername.setText(name);
                                              }else {
                                                  Toast.makeText(getActivity(),e.toString(),Toast.LENGTH_SHORT).show();

                                              }
                                          }
                                      });
                                  }

                             }
       });
                       AlertDialog alertDialog = builder.create();
                      alertDialog.show();
                break;
            default:


                break;
        }
    }


    /**
     * 修改设备表的用户信息：先查询设备表中的数据，再修改数据中用户信息
     */
    private void modifyInstallationUser() {
        BmobQuery<Installation> bmobQuery = new BmobQuery<>();
        final String id = BmobInstallationManager.getInstallationId();
        bmobQuery.addWhereEqualTo("installationId", id);
        bmobQuery.findObjectsObservable(Installation.class)
                .subscribe(new Observer<List<Installation>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<Installation> installations) {
                        if (installations.size() > 0) {
                            Installation installation = installations.get(0);
                            User user = new User();
                            installation.setUser(user);
                            user.setObjectId("");
                            installation.updateObservable()
                                    .subscribe(new Observer<BmobException>() {
                                        @Override
                                        public void onSubscribe(Disposable d) {

                                        }

                                        @Override
                                        public void onNext(BmobException aVoid) {
                                            BmobUtils.toast(getActivity(), "更新设备用户信息成功！");
                                            /**
                                             * TODO 更新成功之后再退出
                                             */
                                            //TODO 连接：3.2、退出登录需要断开与IM服务器的连接
                                            BmobIM.getInstance().disConnect();
                                            BmobUser.logOut();
                                            startActivity(new Intent(getActivity(), LogActivity.class));
                                            getActivity().finish();
                                        }

                                        @Override
                                        public void onError(Throwable e) {

                                            Logger.e("更新设备用户信息失败：" + e.getMessage());
                                        }

                                        @Override
                                        public void onComplete() {

                                        }
                                    });

                        } else {
                            Logger.e("后台不存在此设备Id的数据，请确认此设备Id是否正确！\n" + id);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                        Logger.e("查询设备数据失败：" + e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }



}
