package com.yeah.android.activity.camera.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yeah.android.R;
import com.yeah.android.YeahApp;
import com.yeah.android.activity.camera.CameraBaseActivity;
import com.yeah.android.activity.camera.CameraManager;
import com.yeah.android.activity.camera.EffectService;
import com.yeah.android.activity.camera.adapter.FilterAdapter;
import com.yeah.android.activity.camera.effect.FilterEffect;
import com.yeah.android.activity.camera.util.EffectUtil;
import com.yeah.android.activity.camera.util.GPUImageFilterTools;
import com.yeah.android.model.PhotoItem;
import com.yeah.android.model.TagItem;
import com.yeah.android.utils.Constants;
import com.yeah.android.utils.FileUtils;
import com.yeah.android.utils.ImageUtils;
import com.yeah.android.utils.StringUtils;
import com.yeah.android.utils.TimeUtils;
import com.yeah.android.view.LabelSelector;
import com.yeah.android.view.LabelView;
import com.yeah.android.view.MyHighlightView;
import com.yeah.android.view.MyImageViewDrawableOverlay;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import it.sephiroth.android.library.widget.HListView;
import jp.co.cyberagent.android.gpuimage.GPUImageFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageView;

/**
 * 图片滤镜
 */
public class PhotoFilterActivity extends CameraBaseActivity {

    //滤镜图片
    @InjectView(R.id.gpuimage)
    GPUImageView mGPUImageView;
    //绘图区域
    @InjectView(R.id.drawing_view_container)
    ViewGroup drawArea;
    //底部按钮
    @InjectView(R.id.filter_next)
    TextView filterNextBtn;
    //工具区
    @InjectView(R.id.list_tools)
    HListView bottomToolBar;

    //当前图片
    private Bitmap currentBitmap;
    //用于预览的小图片
    private Bitmap smallImageBackgroud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_filter);
        ButterKnife.inject(this);
        EffectUtil.clear();
        initView();
        initEvent();

        ImageUtils.asyncLoadImage(this, getIntent().getData(), new ImageUtils.LoadImageCallback() {
            @Override
            public void callback(Bitmap result) {
                currentBitmap = result;
                mGPUImageView.setImage(currentBitmap);
            }
        });

        ImageUtils.asyncLoadSmallImage(this, getIntent().getData(), new ImageUtils.LoadImageCallback() {
            @Override
            public void callback(Bitmap result) {
                smallImageBackgroud = result;
                initFilterToolBar();
            }
        });

    }
    private void initView() {
        //添加贴纸水印的画布
//        View overlay = LayoutInflater.from(PhotoFilterActivity.this).inflate(
//                R.layout.view_drawable_overlay, null);
//        mImageView = (MyImageViewDrawableOverlay) overlay.findViewById(R.id.drawable_overlay);
//        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(YeahApp.getApp().getScreenWidth(),
//                YeahApp.getApp().getScreenWidth());
//        mImageView.setLayoutParams(params);
//        overlay.setLayoutParams(params);
//        drawArea.addView(overlay);
        RelativeLayout.LayoutParams rparams = new RelativeLayout.LayoutParams(YeahApp.getApp().getScreenWidth(), YeahApp.getApp().getScreenWidth());

        //初始化滤镜图片
        mGPUImageView.setLayoutParams(rparams);


        //初始化空白标签
//        emptyLabelView = new LabelView(this);
//        emptyLabelView.setEmpty();
//        EffectUtil.addLabelEditable(mImageView, drawArea, emptyLabelView,
//                mImageView.getWidth() / 2, mImageView.getWidth() / 2);
//        emptyLabelView.setVisibility(View.INVISIBLE);

        //初始化推荐标签栏
//        commonLabelArea = LayoutInflater.from(PhotoFilterActivity.this).inflate(
//                R.layout.view_label_bottom,null);
//        commonLabelArea.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
//                ViewGroup.LayoutParams.MATCH_PARENT));
//        toolArea.addView(commonLabelArea);
//        commonLabelArea.setVisibility(View.GONE);
    }

    private void initEvent() {
        bottomToolBar.setVisibility(View.VISIBLE);

        filterNextBtn.setOnClickListener( v -> {
            savePicture();
        });
    }

    //保存图片
    private void savePicture(){
        //加滤镜
        final Bitmap newBitmap = Bitmap.createBitmap(mGPUImageView.getWidth(), mGPUImageView.getHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas cv = new Canvas(newBitmap);
        RectF dst = new RectF(0, 0, mGPUImageView.getWidth(), mGPUImageView.getHeight());
        try {
            cv.drawBitmap(mGPUImageView.capture(), null, dst, null);
        } catch (InterruptedException e) {
            e.printStackTrace();
            cv.drawBitmap(currentBitmap, null, dst, null);
        }

        new SavePicToFileTask().execute(newBitmap);
    }

    private class SavePicToFileTask extends AsyncTask<Bitmap,Void,String>{
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

                String picName = TimeUtils.dtFormat(new Date(), "yyyyMMddHHmmss");
                 fileName = ImageUtils.saveToFile(FileUtils.getInst().getPhotoSavedPath() + "/"+ picName, false, bitmap);

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

            CameraManager.getInst().processPhotoItem(PhotoFilterActivity.this,
                    new PhotoItem(fileName, System.currentTimeMillis()));
        }
    }


    //初始化滤镜
    private void initFilterToolBar(){
        final List<FilterEffect> filters = EffectService.getInst().getLocalFilters();
        final FilterAdapter adapter = new FilterAdapter(PhotoFilterActivity.this, filters,smallImageBackgroud);
        bottomToolBar.setAdapter(adapter);
        bottomToolBar.setOnItemClickListener(new it.sephiroth.android.library.widget.AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(it.sephiroth.android.library.widget.AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                if (adapter.getSelectFilter() != arg2) {
                    adapter.setSelectFilter(arg2);
                    GPUImageFilter filter = GPUImageFilterTools.createFilterForType(
                            PhotoFilterActivity.this, filters.get(arg2).getType());
                    mGPUImageView.setFilter(filter);
                    GPUImageFilterTools.FilterAdjuster mFilterAdjuster = new GPUImageFilterTools.FilterAdjuster(filter);
                    //可调节颜色的滤镜
                    if (mFilterAdjuster.canAdjust()) {
                        //mFilterAdjuster.adjust(100); 给可调节的滤镜选一个合适的值
                    }
                }
            }
        });
    }

}
