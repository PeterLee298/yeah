package com.stickercamera.app.ui.user;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.stickercamera.app.manager.UserInfoManager;
import com.stickercamera.app.ui.MainActivity;
import com.stickercamera.base.BaseActivity;
import com.yeah.stickercamera.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by litingchang on 15-10-6.
 */
public class SplashActivity extends BaseActivity {

    @InjectView(R.id.splash_login)
    LinearLayout btnLogin;
    @InjectView(R.id.splash_bind)
    LinearLayout btnBind;
    @InjectView(R.id.splash_regist)
    LinearLayout btnRegist;
    @InjectView(R.id.splash_action_layout)
    LinearLayout splashActionLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.inject(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (UserInfoManager.isLogin()) {
            MainActivity.launch(SplashActivity.this);
            SplashActivity.this.finish();
        } else {
            splashActionLayout.setVisibility(View.VISIBLE);
        }
    }

    @OnClick(R.id.splash_login)
    public void login() {

        LoginActivity.launch(SplashActivity.this);
    }

    @OnClick(R.id.splash_bind)
    public void bind() {
        BindActivity.launch(SplashActivity.this);
    }

    @OnClick(R.id.splash_regist)
    public void regist() {
        RegistActivity.launch(SplashActivity.this);
    }
}
