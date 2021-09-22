package com.cxy.im4cxy.ui;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.transition.Explode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cxy.im4cxy.R;
import com.cxy.im4cxy.bean.User;
import com.cxy.im4cxy.model.UserModel;
import com.cxy.im4cxy.mvp.bean.Installation;
import com.cxy.im4cxy.util.BmobUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.orhanobut.logger.Logger;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityOptionsCompat;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobInstallationManager;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class LogActivity extends AppCompatActivity {


    @BindView(R.id.et_username)
    EditText mEtUsername;
    @BindView(R.id.et_password)
    EditText mEtPassword;
    @BindView(R.id.bt_go)
    Button mBtGo;
    @BindView(R.id.cv)
    CardView mCv;
    @BindView(R.id.fab)
    FloatingActionButton mFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);
        ButterKnife.bind(this);

    }

    @OnClick({R.id.bt_go, R.id.fab})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab:
                getWindow().setExitTransition(null);
                getWindow().setEnterTransition(null);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptions options =
                            ActivityOptions.makeSceneTransitionAnimation(this, mFab, mFab.getTransitionName());
                    startActivity(new Intent(this, RegActivity.class), options.toBundle());
                } else {
                    startActivity(new Intent(this, RegActivity.class));
                }
                break;
            case R.id.bt_go:

                UserModel.getInstance().login(mEtUsername.getText().toString(), mEtPassword.getText().toString(), new LogInListener() {

                    @Override
                    public void done(Object o, BmobException e) {
                        if (e == null) {
                            //登录成功
                            modifyInstallationUser((User) o);
                            Explode explode = new Explode();
                            explode.setDuration(500);

                            getWindow().setExitTransition(explode);
                            getWindow().setEnterTransition(explode);
                            ActivityOptionsCompat oc2 = ActivityOptionsCompat.makeSceneTransitionAnimation(LogActivity.this);
                            Intent i2 = new Intent(LogActivity.this, MainActivity.class);
                            startActivity(i2, oc2.toBundle());
                        } else {
                            Logger.e(e.getMessage() + "(" + e.getErrorCode() + ")");
                            Toast.makeText(LogActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;
            default:
                break;
        }
    }

    /**
     * 修改设备表的用户信息：先查询设备表中的数据，再修改数据中用户信息
     *
     * @param user
     */
    private void modifyInstallationUser(final User user) {
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
                            installation.setUser(user);
                            installation.updateObservable()
                                    .subscribe(new Observer<BmobException>() {
                                        @Override
                                        public void onSubscribe(Disposable d) {

                                        }

                                        @Override
                                        public void onNext(BmobException aVoid) {

                                            BmobUtils.toast(LogActivity.this, "更新设备用户信息成功！");
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
