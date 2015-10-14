package com.yeah.android.activity.user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.yeah.android.R;
import com.yeah.android.activity.BaseActivity;
import com.yeah.android.activity.MainActivity;
import com.yeah.android.utils.UserInfoManager;
import com.yeah.android.view.LoopViewPager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by litingchang on 15-10-6.
 */
public class SplashActivity extends BaseActivity {


    @InjectView(R.id.splash_next)
    ImageView splashNext;
    @InjectView(R.id.splash_arrow_up)
    ImageView splashArrowUp;
    @InjectView(R.id.splash_pager)
    LoopViewPager splashPager;

    private int tickCount = 0;
    private static final int [] imgs = {R.drawable.splash_p1, R.drawable.splash_p2};
    private boolean isStartNext = false;

    CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.inject(this);


        splashPager.setAdapter(new RecommendPagerAdapter(SplashActivity.this, imgs));

        countDownTimer = new CountDownTimer(1500 * 6, 1500) {
            public void onTick(long millisUntilFinished) {
                tickCount++;
                splashPager.setCurrentItem(tickCount % imgs.length);
            }
            public void onFinish() {
                doNext();
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        countDownTimer.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        countDownTimer.cancel();
    }

    @OnClick(R.id.splash_next)
    public void login() {
        doNext();
    }

    private void doNext() {

        if(isStartNext) {
            return;
        }

        isStartNext = true;
        if (UserInfoManager.isLogin()) {
            MainActivity.launch(SplashActivity.this);
        } else {
            LoginActivity.launch(SplashActivity.this);
        }
        SplashActivity.this.finish();
    }

    class RecommendPagerAdapter extends PagerAdapter {

        private Context mContext;
        private int [] mImgs;


        public RecommendPagerAdapter(Context context, int [] imgs) {
            mContext = context;
            mImgs = imgs;
        }


        @Override
        public int getCount() {
            return mImgs.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object o) {
            return view == o;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            if(mImgs.length == 0 || position >= mImgs.length) {
                return null;
            }

            View view = LayoutInflater.from(mContext).inflate(R.layout.view_splash_item, null);

            ImageView imageView = (ImageView)view.findViewById(R.id.splash_pager_view);
            imageView.setImageResource(mImgs[position]);

            container.addView(view);
            return view;
        }
    }
}
