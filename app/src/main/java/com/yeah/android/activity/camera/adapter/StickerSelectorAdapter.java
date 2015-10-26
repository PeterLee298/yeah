package com.yeah.android.activity.camera.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.yeah.android.R;
import com.yeah.android.model.Addon;
import com.yeah.android.model.sticker.StickerInfo;
import com.yeah.android.utils.ImageLoaderUtils;

import java.util.List;

/**
 * 
 * 贴纸适配器
 * @author tongqian.ni
 */
public class StickerSelectorAdapter extends BaseAdapter {

    List<StickerInfo> mStickerInfoList;
    Context mContext;

    public StickerSelectorAdapter(Context context, List<StickerInfo> stickerInfoList) {
        mContext = context;
        mStickerInfoList = stickerInfoList;
    }

    @Override
    public int getCount() {
        return mStickerInfoList == null ? 0 : mStickerInfoList.size();
    }

    @Override
    public Object getItem(int position) {
        return mStickerInfoList == null ? null : mStickerInfoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.sticker_photo_item, null);

            holder = new ViewHolder();
            holder.imageView = (ImageView) convertView.findViewById(R.id.sticker_photo);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final StickerInfo stickerInfo = mStickerInfoList.get(position);

        ImageLoader.getInstance().displayImage(stickerInfo.getIcon(), holder.imageView);

        return convertView;
    }

    private static class ViewHolder {
        ImageView imageView;
    }
}
