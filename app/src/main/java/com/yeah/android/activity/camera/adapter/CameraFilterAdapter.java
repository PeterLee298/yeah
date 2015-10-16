package com.yeah.android.activity.camera.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;

import com.yeah.android.activity.camera.EffectService;
import com.yeah.android.activity.camera.effect.FilterEffect;
import com.yeah.android.activity.camera.util.GPUImageFilterTools;
import com.yeah.android.impl.IFilterChange;

import java.util.ArrayList;
import java.util.List;

import jp.co.cyberagent.android.gpuimage.GPUImageFilter;

/**
 * Created by liuchao on 10/14/15.
 * 滤镜
 *
 */
public class CameraFilterAdapter extends PagerAdapter implements OnPageChangeListener{

    private List<FilterBundle> mFilterBundles;

    private List<View> mEmptyPageViews = new ArrayList<>();

    private IFilterChange mIFilterChange;

    private Context mContext;

    public CameraFilterAdapter(Context context, IFilterChange filterChange){
        mFilterBundles = new ArrayList<>();
//        mFilterBundles.add(new FilterBundle("Lomo", new GPUImageWeakPixelInclusionFilter()));
//        mFilterBundles.add(new FilterBundle("冬日", new GPUImageWhiteBalanceFilter()));
//        mFilterBundles.add(new FilterBundle("复古", new GPUImageLookupFilter()));
//        mFilterBundles.add(new FilterBundle("夏日", new GPUImageVignetteFilter()));
//        mFilterBundles.add(new FilterBundle("怀旧", new GPUImageToneCurveFilter()));
//        mFilterBundles.add(new FilterBundle("喜剧", new GPUImageSphereRefractionFilter()));
//        mFilterBundles.add(new FilterBundle("文艺", new GPUImageLookupFilter()));
//        mFilterBundles.add(new FilterBundle("日系", new GPUImageKuwaharaFilter()));
//        mFilterBundles.add(new FilterBundle("时尚", new GPUImageLookupFilter()));
//        mFilterBundles.add(new FilterBundle("温暖", new GPUImageKuwaharaFilter()));
//        mFilterBundles.add(new FilterBundle("纪实", new GPUImageLookupFilter()));
//        mFilterBundles.add(new FilterBundle("艺术", new GPUImageKuwaharaFilter()));
//        mFilterBundles.add(new FilterBundle("质感", new GPUImageLookupFilter()));
//        mFilterBundles.add(new FilterBundle("静物肖像", new GPUImageKuwaharaFilter()));
//        mFilterBundles.add(new FilterBundle("黑白电影", new GPUImageLookupFilter()));
//        mFilterBundles.add(new FilterBundle("黑白胶片", new GPUImageKuwaharaFilter()));

        List<FilterEffect> effects = EffectService.getInst().getLocalFilters();
        for(FilterEffect filterEffect : effects){
            mFilterBundles.add(new FilterBundle(filterEffect.getTitle(), GPUImageFilterTools.createFilterForType(context, filterEffect.getType())));
        }

//        mEmptyPageView = new View(context);
        mContext = context;
        for(FilterBundle filterBundle : mFilterBundles){
            View view = new View(mContext);
            mEmptyPageViews.add(view);
        }
        this.mIFilterChange = filterChange;
    }

    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels){

    }

    public void onPageSelected(int position){
        if(mIFilterChange != null){
            mIFilterChange.onFilterChange(mFilterBundles.get(position).getGpuImageFilter());
        }
    }

    public void onPageScrollStateChanged(int state){

    }

    @Override
    public int getCount() {
        return mFilterBundles.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position){
        container.addView(mEmptyPageViews.get(position));
        return mEmptyPageViews.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(mEmptyPageViews.get(position));
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFilterBundles.get(position).getFilterName();
    }

    public static class FilterBundle{
        private String mFilterName;
        private GPUImageFilter mGpuImageFilter;
        public FilterBundle(String filterName, GPUImageFilter gpuImageFilter){
            this.mFilterName = filterName;
            this.mGpuImageFilter = gpuImageFilter;
        }
        public String getFilterName() {
            return mFilterName;
        }
        public GPUImageFilter getGpuImageFilter() {
            return mGpuImageFilter;
        }
    }

}
