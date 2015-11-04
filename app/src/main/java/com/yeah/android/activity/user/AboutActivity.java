package com.yeah.android.activity.user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.yeah.android.R;
import com.yeah.android.YeahApp;
import com.yeah.android.activity.BaseActivity;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by litingchang on 15-11-4.
 */
public class AboutActivity extends BaseActivity {


    @InjectView(R.id.about_version_name)
    TextView aboutVersionName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ButterKnife.inject(this);

        aboutVersionName.setText(getString(R.string.about_version_name,
                YeahApp.getApp().getVersionName()));
    }

    public static void launch(Context context) {
        Intent intent = new Intent(context, AboutActivity.class);
        context.startActivity(intent);
    }
}
