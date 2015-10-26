package com.yeah.android.view.sticker;

import android.app.ActionBar;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.yeah.android.R;
import com.yeah.android.YeahApp;
import com.yeah.android.model.sticker.StickerExtraGroup;
import com.yeah.android.model.sticker.StickerInfo;
import com.yeah.android.model.sticker.StickerListItem;
import com.yeah.android.utils.AppUtils;
import com.yeah.android.utils.ImageLoaderUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by litingchang on 15-10-24.
 */
public class StickerGroupPopWindow  extends PopupWindow{

    private Context mContext;
    private View mRootView;
    private GridView mGridView;
    private ImageView mCoverImg;
    private TextView mTitleTV;
    private TextView mDescriptionTV;

//    private List<StickerInfo> mStickerList;
//    private StickerAdapter mStickerAdapter;

    private StickerGroupClickListener mClickListener;

    public StickerGroupPopWindow(Context context, StickerGroupClickListener clickListener) {
        super(context);
        mContext = context;
        mClickListener = clickListener;

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mRootView = inflater.inflate(R.layout.popwindow_sticker_group, null);
        mCoverImg = (ImageView) mRootView.findViewById(R.id.sticker_cover);
        mTitleTV = (TextView) mRootView.findViewById(R.id.sticker_group_title);
        mDescriptionTV = (TextView) mRootView.findViewById(R.id.sticker_group_description);
        mGridView = (GridView) mRootView.findViewById(R.id.sticker_list);

//        mStickerList = new ArrayList<>();
//        mStickerAdapter = new StickerAdapter(mContext, mStickerList);


        mGridView.setOnItemClickListener(new GridView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mClickListener != null) {
                    mClickListener.onStickerCroupItemClicke(position);
                    dismiss();
                }
            }
        });


        this.setContentView(mRootView);
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(YeahApp.getApp().dp2px(460));
        this.setFocusable(true);
        this.setAnimationStyle(R.style.Animation_AppCompat_DropDownUp);
//        this.setBackgroundDrawable(new ColorDrawable(0xCC000000));
        this.setBackgroundDrawable(new ColorDrawable(0xffffffff));

    }

    public void setStickerList(List<StickerInfo> stickerList) {
//        mStickerList = stickerList;

        if(stickerList == null || stickerList.isEmpty()) {
            dismiss();
            return;
        }

        StickerExtraGroup group = stickerList.get(0).getExtra().getGroup();
        mTitleTV.setText(group.getTitle());
        mDescriptionTV.setText(group.getDescription());

        ImageLoader.getInstance().displayImage(group.getIcon(), mCoverImg);

        mGridView.setAdapter(new StickerAdapter(mContext, stickerList));
    }

    public interface StickerGroupClickListener{
        public void onStickerCroupItemClicke(int position);
    }


    private static class ViewHolder {
        ImageView photoView;
    }

    class StickerAdapter extends BaseAdapter {

        private List<StickerInfo> data;
        private Context cxt;

        public StickerAdapter(Context cxt, List<StickerInfo> data) {
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
                holder.photoView = (ImageView) convertView.findViewById(R.id.sticker_photo);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            StickerInfo item = data.get(position);
            ImageLoader.getInstance().displayImage(item.getIcon(), holder.photoView);
            return convertView;
        }
    }


}
