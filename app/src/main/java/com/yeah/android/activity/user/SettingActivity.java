package com.yeah.android.activity.user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.yeah.android.R;
import com.yeah.android.activity.BaseActivity;
import com.yeah.android.activity.camera.CameraManager;
import com.yeah.android.utils.UserInfoManager;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by litingchang on 15-10-15.
 */
public class SettingActivity extends BaseActivity {

    @InjectView(R.id.logout)
    TextView logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.inject(this);

    }

    @OnClick(R.id.logout)
    public void logout() {
        UserInfoManager.logout();
        CameraManager.getInst().close();
        SettingActivity.this.finish();
        LoginActivity.launch(SettingActivity.this);
    }

    public static void launch(Context context) {
        Intent intent = new Intent(context, SettingActivity.class);
        context.startActivity(intent);
    }
}
