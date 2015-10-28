package com.yeah.android.activity.user;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.TypeReference;
import com.facebook.drawee.view.SimpleDraweeView;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yeah.android.R;
import com.yeah.android.activity.BaseActivity;
import com.yeah.android.model.common.ResponseData;
import com.yeah.android.model.user.Photo;
import com.yeah.android.model.user.UserInfo;
import com.yeah.android.model.user.UserPhotoResponse;
import com.yeah.android.net.http.StickerHttpClient;
import com.yeah.android.net.http.StickerHttpResponseHandler;
import com.yeah.android.utils.Constants;
import com.yeah.android.utils.LogUtil;
import com.yeah.android.utils.StringUtils;
import com.yeah.android.utils.ToastUtil;
import com.yeah.android.utils.UserInfoManager;
import com.yeah.android.view.CommonTitleBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by litingchang on 15-10-8.
 * <p>
 * 个人主页
 */
public class UserHomeActivity extends BaseActivity {

    @InjectView(R.id.title_layout)
    CommonTitleBar titleLayout;
    @InjectView(R.id.userAvatar)
    SimpleDraweeView userAvatar;
    @InjectView(R.id.userNickName)
    TextView userNickName;
    @InjectView(R.id.user_photos)
    GridView userPhotosGridView;

    private static final int PAGE_SIZE = 10;

    private int currentCount = 0;
    private int total = 0;
    private int currentPage = 0;

    private List<Photo> photoList;
    private UserPhotoAdapter photoAdapter;

    private UserInfo mUserInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);
        ButterKnife.inject(this);


        titleLayout.setLeftBtnOnclickListener( v -> {
            finish();
        });

        titleLayout.setRightBtnOnclickListener( v -> {
            UserInfoActivity.launch(this);
        });

        photoList = new ArrayList<>();
        photoAdapter = new UserPhotoAdapter(this, photoList);
        userPhotosGridView.setAdapter(photoAdapter);


        loaduserPhotos(0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadUserInfo();
    }

    private void loadUserInfo() {

        mUserInfo = UserInfoManager.getUserInfo();

        if (!StringUtils.isEmpty(mUserInfo.getAvatar())) {
            userAvatar.setImageURI(Uri.parse(mUserInfo.getAvatar()));
        }

        userNickName.setText(mUserInfo.getNickname());
    }

    private void loaduserPhotos(int page) {

        RequestParams requestParams = new RequestParams();
        requestParams.put("appId", Constants.APP_ID);
        requestParams.put("appKey", Constants.APP_KEY);
        requestParams.put("loginId", UserInfoManager.getUserId());
        requestParams.put("loginToken", UserInfoManager.getToken());

        StickerHttpClient.post("/account/user/photo/list", requestParams,
                new TypeReference<ResponseData<UserPhotoResponse>>() {
                }.getType(),
                new StickerHttpResponseHandler<UserPhotoResponse>() {

                    @Override
                    public void onStart() {
                        showProgressDialog("获取照片列表...");
                    }

                    @Override
                    public void onSuccess(UserPhotoResponse response) {

                        if (response.getContent() != null) {
                            photoList.addAll(response.getContent());
                            photoAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onFailure(String message) {
                        LogUtil.e("onFailure", message);
                        ToastUtil.shortToast(UserHomeActivity.this, "获取失败：" + message
                                + "\n请稍候重试");
                    }

                    @Override
                    public void onFinish() {
                        dismissProgressDialog();
                    }
                });

    }

    private static class ViewHolder {
        ImageView photoView;
    }

    class UserPhotoAdapter extends BaseAdapter {

        private List<Photo> data;
        private Context cxt;

        public UserPhotoAdapter(Context cxt, List<Photo> data) {
            this.data = data;
            this.cxt = cxt;
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (null == convertView) {
                convertView = LayoutInflater.from(cxt).inflate(R.layout.user_photo_item, null);
                holder = new ViewHolder();
                holder.photoView = (ImageView) convertView.findViewById(R.id.user_photo);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            Photo item = data.get(position);
            ImageLoader.getInstance().displayImage(item.getUrl(), holder.photoView);
            return convertView;
        }
    }

    public static void launch(Context context) {
        Intent intent = new Intent(context, UserHomeActivity.class);
        context.startActivity(intent);
    }
}
