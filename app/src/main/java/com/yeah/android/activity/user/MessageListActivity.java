package com.yeah.android.activity.user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.yeah.android.R;
import com.yeah.android.activity.BaseActivity;
import com.yeah.android.model.user.Photo;
import com.yeah.android.view.CommonTitleBar;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by litingchang on 15-11-10.
 */
public class MessageListActivity extends BaseActivity {

    @InjectView(R.id.title_layout)
    CommonTitleBar titleLayout;
    @InjectView(R.id.message_list_view)
    ListView messageListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_list);

        ButterKnife.inject(this);

    }

    private static class ViewHolder{
        TextView title;
        TextView content;
    }


    class MessageAdapter extends BaseAdapter {

        private List<String> data;
        private Context cxt;

        public MessageAdapter(Context cxt, List<String> data) {
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
                convertView = LayoutInflater.from(cxt).inflate(R.layout.message_item, null);
                holder = new ViewHolder();
                holder.title = (TextView) convertView.findViewById(R.id.message_title);
                holder.content = (TextView) convertView.findViewById(R.id.message_content);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }


            return convertView;
        }
    }


    public static void launch(Context context) {
        Intent intent = new Intent(context, MessageListActivity.class);
        context.startActivity(intent);
    }
}
