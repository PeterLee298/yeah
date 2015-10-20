package com.yeah.android.activity.user;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.yeah.android.R;
import com.yeah.android.activity.BaseActivity;
import com.yeah.android.utils.ImageUtils;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by litingchang on 15-10-20.
 */
public class PhotoPostAvtivity extends BaseActivity {

    @InjectView(R.id.post_image)
    ImageView postImage;
    @InjectView(R.id.post_content)
    EditText postContent;
    @InjectView(R.id.post_btn)
    TextView postBtn;

    private Uri imgUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_post);
        ButterKnife.inject(this);

        imgUri = getIntent().getData();

        ImageUtils.asyncLoadImage(this, imgUri, new ImageUtils.LoadImageCallback() {
            @Override
            public void callback(Bitmap result) {
                postImage.setImageBitmap(result);
            }
        });
    }

//    File file = new File()

    public static void launch(Context context, Uri uri) {
        Intent intent = new Intent(context, PhotoPostAvtivity.class);
        intent.setData(uri);
        context.startActivity(intent);
    }
}


