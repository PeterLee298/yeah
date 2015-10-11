package com.yeah.android.activity.user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.yeah.android.utils.LogUtil;
import com.yeah.android.activity.BaseActivity;
import com.yeah.android.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by litingchang on 15-10-7.
 */
public class BindActivity extends BaseActivity {

    @InjectView(R.id.bind_weibo)
    TextView bindWeibo;
    @InjectView(R.id.bind_weixin)
    TextView bindWeixin;
    @InjectView(R.id.bind_qq)
    TextView bindQq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind);
        ButterKnife.inject(this);



    }

    @OnClick(R.id.bind_weibo)
    public void bindWeibo() {
        LogUtil.d("bind", "weibo");
    }

    @OnClick(R.id.bind_weixin)
    public void bindWeixin() {
        LogUtil.d("bind", "weixin");
    }

    @OnClick(R.id.bind_qq)
    public void bindQQ() {
        LogUtil.d("bind", "qq");
    }

    public static void launch(Context context) {
        Intent intent = new Intent(context, BindActivity.class);
        context.startActivity(intent);
    }
}
