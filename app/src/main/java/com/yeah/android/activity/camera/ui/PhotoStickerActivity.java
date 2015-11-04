package com.yeah.android.activity.camera.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.TypeReference;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yeah.android.R;
import com.yeah.android.YeahApp;
import com.yeah.android.activity.camera.CameraBaseActivity;
import com.yeah.android.activity.camera.adapter.StickerSelectorAdapter;
import com.yeah.android.activity.camera.adapter.StickerToolAdapter;
import com.yeah.android.activity.camera.util.EffectUtil;
import com.yeah.android.activity.user.PhotoPostAvtivity;
import com.yeah.android.activity.user.UserInfoActivity;
import com.yeah.android.model.Addon;
import com.yeah.android.model.common.ResponseData;
import com.yeah.android.model.sticker.StickerHot;
import com.yeah.android.model.sticker.StickerHotResponse;
import com.yeah.android.model.sticker.StickerInfo;
import com.yeah.android.model.sticker.StickerListItem;
import com.yeah.android.model.sticker.StickerListResponse;
import com.yeah.android.model.sticker.StickerResponse;
import com.yeah.android.net.http.StickerHttpClient;
import com.yeah.android.net.http.StickerHttpResponseHandler;
import com.yeah.android.utils.FileUtils;
import com.yeah.android.utils.ImageUtils;
import com.yeah.android.utils.LogUtil;
import com.yeah.android.utils.StringUtils;
import com.yeah.android.utils.TimeUtils;
import com.yeah.android.utils.ToastUtil;
import com.yeah.android.view.MyImageViewDrawableOverlay;
import com.yeah.android.view.sticker.StickerGroupPopWindow;
import com.yeah.android.view.sticker.StickerSampleDialog;

import java.util.ArrayList;
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

    private static final String TAG = PhotoStickerActivity.class.getSimpleName();

    private static final int REQUEST_CODE_STICKER = 101;
    private int mStickerGroupId;
//    private int
    //滤镜图片
    @InjectView(R.id.sticker_root)
    View stickerRootView;
    //滤镜图片
    @InjectView(R.id.gpuimage)
    GPUImageView mGPUImageView;
    //绘图区域
    @InjectView(R.id.drawing_view_container)
    ViewGroup drawArea;

    @InjectView(R.id.sticker_list_area)
    LinearLayout stickerListArea;
    @InjectView(R.id.sticker_list)
    HListView stickerListView;
    @InjectView(R.id.sticker_add_more)
    TextView stickerAddMore;

    @InjectView(R.id.sticker_group_area)
    LinearLayout stickerGroupArea;
    @InjectView(R.id.sticker_tab_radio_group)
    RadioGroup stickerTabRadioGroup;
    @InjectView(R.id.sticker_theme)
    GridView stickerTheme;
    @InjectView(R.id.sticker_hot)
    GridView stickerHot;

    private boolean mThemeLastItemVisible;
    private int themeNextPage = 0;
    private boolean isThemeReachEnd = false;
    private boolean isThemeLoading = false;

    private boolean mHotLastItemVisible;
    private int hotNextPage = 0;
    private boolean isHotReachEnd = false;
    private boolean isHotLoading = false;



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

//    private StickerSelectorAdapter stickerSelectorAdapter;
    private ThemeStickerAdapter themeStickerAdapter;
    private HotStickerAdapter hotStickerAdapter;

    private int stickerRequestCount = 0;

    private StickerGroupPopWindow stickerGroupPopWindow;



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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_STICKER && resultCode == RESULT_OK) {
        }
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


        stickerGroupPopWindow = new StickerGroupPopWindow(PhotoStickerActivity.this, new StickerGroupPopWindow.StickerGroupClickListener() {
            @Override
            public void onStickerCroupItemClicke(int position) {
                LogUtil.d(TAG, "onStickerCroupItemClicke:" + position);

                if(stickerInfoList != null && stickerInfoList.size() > position) {
                    showInfoDialog(stickerInfoList.get(position));
                }
            }
        });
    }

    private void initEvent() {

        stickerTabRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.sticker_group_tab_theme:
                        stickerTheme.setVisibility(View.VISIBLE);
                        stickerHot.setVisibility(View.GONE);

                        break;
                    case R.id.sticker_group_tab_hot:
                        stickerTheme.setVisibility(View.GONE);
                        stickerHot.setVisibility(View.VISIBLE);
                        if (hotStickerAdapter != null) {
                            hotStickerAdapter.notifyDataSetChanged();
                        }
                        break;
                }
            }
        });


    }

    @OnClick(R.id.sticker_add_more)
    public void stickerAddMore() {
        showStickerGroupArea();
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

        //添加Yeah水印

        try {
            Bitmap water = BitmapFactory.decodeResource(getResources(), R.drawable.skyblue_logo_evernote);
            cv.drawBitmap(water, 20, 20, null);
            cv.save(Canvas.ALL_SAVE_FLAG);
            cv.restore();
        } catch (Exception e) {
            e.printStackTrace();
        }

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

        stickerInfoList = new ArrayList<>();
//        stickerListView.setAdapter(new StickerSelectorAdapter(PhotoStickerActivity.this, stickerInfoList));
//        stickerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                EffectUtil.addStickerImage2(mImageView, PhotoStickerActivity.this,
//                        stickerInfoList.get(i).getIcon());
//            }
//        });


        stickerThemeList = new ArrayList<>();
        themeStickerAdapter = new ThemeStickerAdapter(PhotoStickerActivity.this, stickerThemeList);
        stickerTheme.setAdapter(themeStickerAdapter);
        stickerTheme.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(android.widget.AdapterView<?> parent, View view, int position, long id) {
                LogUtil.d(TAG, "onItemClick:" + position);
                getStickerList(0, stickerThemeList.get(position).getId());
            }
        });

        stickerTheme.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                mThemeLastItemVisible = (totalItemCount > 0) && (firstVisibleItem + visibleItemCount >= totalItemCount - 1);
            }

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                LogUtil.d("userPhotosGridView", "onScrollStateChanged");

                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE
                        && mThemeLastItemVisible && !isThemeReachEnd && !isThemeLoading) {
                    getStickerThemeList(themeNextPage);
                }
            }
        });


        stickerHotList = new ArrayList<>();
        hotStickerAdapter = new HotStickerAdapter(PhotoStickerActivity.this, stickerHotList);
        stickerHot.setAdapter(hotStickerAdapter);
        stickerHot.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(android.widget.AdapterView<?> parent, View view, int position, long id) {
                LogUtil.d(TAG, "onItemClick:" + position);
                getStickerList(0, stickerHotList.get(position).getGroupId());
            }
        });
        stickerHot.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                mHotLastItemVisible = (totalItemCount > 0) && (firstVisibleItem + visibleItemCount >= totalItemCount - 1);
            }

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                LogUtil.d("userPhotosGridView", "onScrollStateChanged");

                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE
                        && mHotLastItemVisible && !isHotReachEnd && !isHotLoading) {
                    getStickerHotList(hotNextPage);
                }
            }
        });



        getStickerHotList(0);
        getStickerThemeList(0);
    }

    private void showStickerGroupArea() {
        stickerGroupArea.setVisibility(View.VISIBLE);
        stickerListArea.setVisibility(View.GONE);

    }

    private void showStickerListArea() {
        stickerGroupArea.setVisibility(View.GONE);
        stickerListArea.setVisibility(View.VISIBLE);
//        stickerSelectorAdapter.notifyDataSetChanged();
    }

    private void getStickerHotList(int pageNumber) {
        RequestParams requestParams = new RequestParams();
        requestParams.put("pageNumber", pageNumber);
        requestParams.put("pageSize", PAGE_SIZE);
        requestParams.setUseJsonStreamer(true);

        StickerHttpClient.post("/group/hot", requestParams,
                new TypeReference<ResponseData<StickerHotResponse>>() {
                }.getType(),
                new StickerHttpResponseHandler<StickerHotResponse>() {

                    @Override
                    public void onStart() {
                        stickerRequestCount++;
                        if(stickerRequestCount == 1) {
                            showProgressDialog("贴纸标签加载中");
                        }
                        isHotLoading = true;
                    }

                    @Override
                    public void onSuccess(StickerHotResponse response) {

                        if(response.getContent() == null  || response.getContent().isEmpty()) {
                            isHotReachEnd = true;
                            return;
                        }

                        hotNextPage++;
                        if(stickerHotList.size() + response.getContent().size() >=
                                response.getTotal()) {
                            isHotReachEnd = true;
                        }

                        stickerHotList.addAll(response.getContent());
                        hotStickerAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure(String message) {
                        ToastUtil.shortToast(PhotoStickerActivity.this, "获取热门贴纸失败：" + message);
                    }

                    @Override
                    public void onFinish() {
                        stickerRequestCount--;
                        if(stickerRequestCount <= 0) {
                            dismissProgressDialog();
                        }
                        isHotLoading = false;
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
                        stickerRequestCount++;
                        if(stickerRequestCount == 1) {
                            showProgressDialog("贴纸标签加载中");
                        }
                        isThemeLoading = true;
                    }

                    @Override
                    public void onSuccess(StickerListResponse response) {
                        if(response.getContent() == null || response.getContent().isEmpty()) {
                            isThemeReachEnd = true;
                            return;
                        }

                        themeNextPage++;
                        if(stickerThemeList.size() + response.getContent().size() >=
                                response.getTotal()) {
                            isThemeReachEnd = true;
                        }

                        stickerThemeList.addAll(response.getContent());
                        themeStickerAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure(String message) {
                        ToastUtil.shortToast(PhotoStickerActivity.this, "获取主题贴纸失败：" + message);
                    }

                    @Override
                    public void onFinish() {
                        stickerRequestCount--;
                        if(stickerRequestCount <= 0) {
                            dismissProgressDialog();
                        }
                        isThemeLoading = false;
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
                        showProgressDialog("加载中。。。");
                    }

                    @Override
                    public void onSuccess(StickerResponse response) {

                        if(response.getContent() != null && !response.getContent().isEmpty()) {
                            stickerInfoList.clear();
                            stickerInfoList.addAll(response.getContent());

                            stickerListView.setAdapter(new StickerSelectorAdapter(PhotoStickerActivity.this, stickerInfoList));
                            stickerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    EffectUtil.addStickerImage2(mImageView, PhotoStickerActivity.this,
                                            stickerInfoList.get(i).getIcon());
                                }
                            });

                            stickerGroupPopWindow.showAtLocation(stickerRootView,
                                    Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);

                            stickerGroupPopWindow.setStickerList(stickerInfoList);
                        } else {
                            ToastUtil.shortToast(PhotoStickerActivity.this, "获取贴纸列表为空");
                        }
                    }

                    @Override
                    public void onFailure(String message) {
                        ToastUtil.shortToast(PhotoStickerActivity.this, "获取贴纸失败：" + message);
                    }

                    @Override
                    public void onFinish() {
                        dismissProgressDialog();
                    }
                });
    }


    private static class ViewHolder {
        ImageView photoView;
        TextView titleTV;
    }

    class HotStickerAdapter extends BaseAdapter {

        private List<StickerHot> data;
        private Context cxt;

        public HotStickerAdapter(Context cxt, List<StickerHot> data) {
            this.data = data;
            this.cxt = cxt;
        }

        @Override
        public int getCount() {
            return data == null ? 0 : data.size();
        }

        @Override
        public Object getItem(int position) {
            return data == null ? null : data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (null == convertView) {
                convertView = LayoutInflater.from(cxt).inflate(R.layout.sticker_photo_item, null);
                holder = new ViewHolder();
                holder.titleTV = (TextView) convertView.findViewById(R.id.sticker_title);
                holder.photoView = (ImageView) convertView.findViewById(R.id.sticker_photo);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            StickerHot item = data.get(position);
            holder.titleTV.setText(item.getExtra().getGroup().getTitle());
            ImageLoader.getInstance().displayImage(item.getExtra().getGroup().getIcon(), holder.photoView);
            return convertView;
        }
    }

    class ThemeStickerAdapter extends BaseAdapter {

        private List<StickerListItem> data;
        private Context cxt;

        public ThemeStickerAdapter(Context cxt, List<StickerListItem> data) {
            this.data = data;
            this.cxt = cxt;
        }

        @Override
        public int getCount() {
            return data == null ? 0 : data.size();
        }

        @Override
        public Object getItem(int position) {
            return data == null ? null : data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (null == convertView) {
                convertView = LayoutInflater.from(cxt).inflate(R.layout.sticker_photo_item, null);
                holder = new ViewHolder();
                holder.titleTV = (TextView) convertView.findViewById(R.id.sticker_title);
                holder.photoView = (ImageView) convertView.findViewById(R.id.sticker_photo);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            StickerListItem item = data.get(position);
            holder.titleTV.setText(item.getTitle());
            ImageLoader.getInstance().displayImage(item.getIcon(), holder.photoView);
            return convertView;
        }
    }

    private void showInfoDialog(StickerInfo info) {
        StickerSampleDialog dialog = new StickerSampleDialog(PhotoStickerActivity.this, info);
        dialog.setOnUseBtnClickeListener(new StickerSampleDialog.OnUseClickListener() {
            @Override
            public void onClick(StickerInfo info) {
                showStickerListArea();
                EffectUtil.addStickerImage2(mImageView, PhotoStickerActivity.this,
                        info.getIcon());
            }
        });
        dialog.show();
    }
}
