package com.yeah.android.activity.camera.ui;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.TypeReference;
import com.loopj.android.http.RequestParams;
import com.yeah.android.R;
import com.yeah.android.YeahApp;
import com.yeah.android.activity.camera.CameraBaseActivity;
import com.yeah.android.activity.camera.CameraManager;
import com.yeah.android.activity.camera.adapter.StickerToolAdapter;
import com.yeah.android.activity.camera.util.EffectUtil;
import com.yeah.android.activity.user.LoginActivity;
import com.yeah.android.activity.user.PhotoPostAvtivity;
import com.yeah.android.model.Addon;
import com.yeah.android.model.common.ResponseData;
import com.yeah.android.model.sticker.StickerHot;
import com.yeah.android.model.sticker.StickerHotResponse;
import com.yeah.android.model.sticker.StickerInfo;
import com.yeah.android.model.sticker.StickerListItem;
import com.yeah.android.model.sticker.StickerListResponse;
import com.yeah.android.model.sticker.StickerResponse;
import com.yeah.android.model.user.LoginResult;
import com.yeah.android.net.http.StickerHttpClient;
import com.yeah.android.net.http.StickerHttpResponseHandler;
import com.yeah.android.utils.Constants;
import com.yeah.android.utils.FileUtils;
import com.yeah.android.utils.ImageUtils;
import com.yeah.android.utils.LogUtil;
import com.yeah.android.utils.StringUtils;
import com.yeah.android.utils.TimeUtils;
import com.yeah.android.utils.ToastUtil;
import com.yeah.android.utils.UserInfoManager;
import com.yeah.android.view.MyImageViewDrawableOverlay;

import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import it.sephiroth.android.library.widget.AdapterView;
import it.sephiroth.android.library.widget.HListView;
import jp.co.cyberagent.android.gpuimage.GPUImageView;

/**
 * 图片贴图
 */
public class PhotoStickerActivity extends CameraBaseActivity {

    //滤镜图片
    @InjectView(R.id.gpuimage)
    GPUImageView mGPUImageView;
    //绘图区域
    @InjectView(R.id.drawing_view_container)
    ViewGroup drawArea;

    @InjectView(R.id.sticker_list_area)
    LinearLayout stickerListArea;
    @InjectView(R.id.sticker_list)
    HListView bottomToolBar;
    @InjectView(R.id.sticker_add_more)
    TextView stickerAddMore;

    @InjectView(R.id.sticker_group_area)
    LinearLayout stickerGroupArea;
    @InjectView(R.id.sticker_group_tab_theme)
    RadioButton stickerGroupTabTheme;
    @InjectView(R.id.sticker_group_tab_hot)
    RadioButton stickerGroupTabHot;
    @InjectView(R.id.sticker_group_tab_layout)
    RadioGroup stickerGroupTabLayout;
    @InjectView(R.id.sticker_theme)
    GridView stickerTheme;
    @InjectView(R.id.sticker_group)
    GridView stickerGroup;

    private MyImageViewDrawableOverlay mImageView;

    //当前图片
    private Bitmap currentBitmap;

    private static final int PAGE_SIZE = 10;
    // 热门
    private List<StickerHot> stickerHotList;
    // 主题
    private List<StickerListItem> stickerThemeList;
    // 单项列表
    private List<StickerInfo> stickerInfoList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_sticker);
        ButterKnife.inject(this);
        EffectUtil.clear();
        initView();
        initEvent();
        initStickerToolBar();

        ImageUtils.asyncLoadImage(this, getIntent().getData(), new ImageUtils.LoadImageCallback() {
            @Override
            public void callback(Bitmap result) {
                currentBitmap = result;
                mGPUImageView.setImage(currentBitmap);
            }
        });

        titleBar.setRightBtnOnclickListener(v -> {
            savePicture();
        });
    }

    private void initView() {
        //添加贴纸水印的画布
        View overlay = LayoutInflater.from(PhotoStickerActivity.this).inflate(
                R.layout.view_drawable_overlay, null);
        mImageView = (MyImageViewDrawableOverlay) overlay.findViewById(R.id.drawable_overlay);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(YeahApp.getApp().getScreenWidth(),
                YeahApp.getApp().getScreenWidth());
        mImageView.setLayoutParams(params);
        overlay.setLayoutParams(params);
        drawArea.addView(overlay);


        RelativeLayout.LayoutParams rparams = new RelativeLayout.LayoutParams(YeahApp.getApp().getScreenWidth(), YeahApp.getApp().getScreenWidth());
        //初始化滤镜图片
        mGPUImageView.setLayoutParams(rparams);
    }

    private void initEvent() {
        bottomToolBar.setVisibility(View.VISIBLE);
        initStickerToolBar();

        getStickerHotList(0);
        getStickerThemeList(0);
        getStickerList(0, 20150721);

    }

    @OnClick(R.id.sticker_add_more)
    public void stickerAddMore() {

    }

    //保存图片
    private void savePicture() {
        //加滤镜
        final Bitmap newBitmap = Bitmap.createBitmap(mImageView.getWidth(), mImageView.getHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas cv = new Canvas(newBitmap);
        RectF dst = new RectF(0, 0, mImageView.getWidth(), mImageView.getHeight());
        try {
            cv.drawBitmap(mGPUImageView.capture(), null, dst, null);
        } catch (InterruptedException e) {
            e.printStackTrace();
            cv.drawBitmap(currentBitmap, null, dst, null);
        }
        //加贴纸水印
        EffectUtil.applyOnSave(cv, mImageView);

        new SavePicToFileTask().execute(newBitmap);
    }

    private class SavePicToFileTask extends AsyncTask<Bitmap, Void, String> {
        Bitmap bitmap;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog("图片处理中...");
        }

        @Override
        protected String doInBackground(Bitmap... params) {
            String fileName = null;
            try {
                bitmap = params[0];

                String picName = TimeUtils.dtFormat(new Date(), "yyyyMMddHHmmss") + ".jpeg";
                fileName = ImageUtils.saveToFile(FileUtils.getInst().getPhotoSavedPath() + "/" + picName, false, bitmap);

            } catch (Exception e) {
                e.printStackTrace();
                toast("图片处理错误，请退出相机并重试", Toast.LENGTH_LONG);
            }
            return fileName;
        }

        @Override
        protected void onPostExecute(String fileName) {
            super.onPostExecute(fileName);
            dismissProgressDialog();
            if (StringUtils.isEmpty(fileName)) {
                return;
            }

            Uri uri = fileName.startsWith("file:") ? Uri.parse(fileName) : Uri.parse("file://" + fileName);
            PhotoPostAvtivity.launch(PhotoStickerActivity.this, uri);
        }
    }


    //初始化贴图
    private void initStickerToolBar() {

        bottomToolBar.setAdapter(new StickerToolAdapter(PhotoStickerActivity.this, EffectUtil.addonList));
        bottomToolBar.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0,
                                    View arg1, int arg2, long arg3) {
                Addon sticker = EffectUtil.addonList.get(arg2);
                EffectUtil.addStickerImage(mImageView, PhotoStickerActivity.this, sticker,
                        new EffectUtil.StickerCallback() {
                            @Override
                            public void onRemoveSticker(Addon sticker) {
                            }
                        });
            }
        });
    }

    private void getStickerHotList(int pageNumber) {
        RequestParams requestParams = new RequestParams();
        requestParams.put("pageNumber", pageNumber);
        requestParams.put("pageSize", PAGE_SIZE);
        StickerHttpClient.post("/group/hot", requestParams,
                new TypeReference<ResponseData<StickerHotResponse>>() {
                }.getType(),
                new StickerHttpResponseHandler<StickerHotResponse>() {

                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onSuccess(StickerHotResponse response) {
                        stickerHotList = response.getContent();


                    }

                    @Override
                    public void onFailure(String message) {
                        ToastUtil.shortToast(PhotoStickerActivity.this, "获取热门贴纸失败：" + message);
                    }

                    @Override
                    public void onFinish() {
                    }
                });
    }

    private void getStickerThemeList(int pageNumber) {
        RequestParams requestParams = new RequestParams();
        requestParams.put("pageNumber", pageNumber);
        requestParams.put("pageSize", PAGE_SIZE);
        StickerHttpClient.post("/group/list", requestParams,
                new TypeReference<ResponseData<StickerListResponse>>() {
                }.getType(),
                new StickerHttpResponseHandler<StickerListResponse>() {

                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onSuccess(StickerListResponse response) {
                        stickerThemeList = response.getContent();


                    }

                    @Override
                    public void onFailure(String message) {
                        ToastUtil.shortToast(PhotoStickerActivity.this, "获取热门贴纸失败：" + message);
                    }

                    @Override
                    public void onFinish() {
                    }
                });
    }

    private void getStickerList(int pageNumber, int groupId) {
        RequestParams requestParams = new RequestParams();
        requestParams.put("pageNumber", pageNumber);
        requestParams.put("pageSize", PAGE_SIZE);
        requestParams.put("groupId", groupId);

        StickerHttpClient.post("/group/sticker", requestParams,
                new TypeReference<ResponseData<StickerResponse>>() {
                }.getType(),
                new StickerHttpResponseHandler<StickerResponse>() {

                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onSuccess(StickerResponse response) {
                        stickerInfoList = response.getContent();


                    }

                    @Override
                    public void onFailure(String message) {
                        ToastUtil.shortToast(PhotoStickerActivity.this, "获取贴纸失败：" + message);
                    }

                    @Override
                    public void onFinish() {
                    }
                });
    }
}
