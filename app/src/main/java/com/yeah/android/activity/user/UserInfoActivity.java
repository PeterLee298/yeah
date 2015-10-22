package com.yeah.android.activity.user;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.TypeReference;
import com.facebook.drawee.view.SimpleDraweeView;
import com.loopj.android.http.RequestParams;
import com.yeah.android.R;
import com.yeah.android.activity.BaseActivity;
import com.yeah.android.model.common.ResponseData;
import com.yeah.android.model.user.UserInfo;
import com.yeah.android.net.http.StickerHttpClient;
import com.yeah.android.net.http.StickerHttpResponseHandler;
import com.yeah.android.utils.Constants;
import com.yeah.android.utils.DataUtils;
import com.yeah.android.utils.FileUtils;
import com.yeah.android.utils.LogUtil;
import com.yeah.android.utils.PhotoFileUtils;
import com.yeah.android.utils.StringUtils;
import com.yeah.android.utils.ToastUtil;
import com.yeah.android.utils.UserInfoManager;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by litingchang on 15-10-8.
 * <p>
 * 用户信息
 */
public class UserInfoActivity extends BaseActivity {

    private static final String TAG = BaseActivity.class.getSimpleName();

    @InjectView(R.id.userAvatar)
    SimpleDraweeView userAvatar;
    @InjectView(R.id.nickname)
    TextView nicknameTV;
    @InjectView(R.id.info_nickname_root)
    RelativeLayout infoNicknameRoot;
    @InjectView(R.id.sex)
    TextView sexTV;
    @InjectView(R.id.info_sex_root)
    RelativeLayout infoSexRoot;
    @InjectView(R.id.birthday)
    TextView birthdayTV;
    @InjectView(R.id.info_birthday_root)
    RelativeLayout infoBirthdayRoot;
    @InjectView(R.id.constellation)
    TextView constellationTV;
    @InjectView(R.id.info_constellation_root)
    RelativeLayout infoConstellationRoot;


    private static final int IMAGE_REQUEST_CODE = 0; // 请求码 本地图片
    private static final int CAMERA_REQUEST_CODE = 1; // 拍照
    private static final int RESULT_REQUEST_CODE = 2; // 裁剪
    private static final String SAVE_AVATORNAME = "head.jpeg";// 保存的图片名


    private final String[] sex = {"男", "女"};

    private final String[] constellations = {"白羊座", "金牛座", "双子座",
            "巨蟹座", "狮子座", "处女座", "天秤座", "天蝎座", "射手座", "摩羯座", "水瓶座", "双鱼座"};

    private UserInfo mUserInfo;
    private Map<String, String> updateInfo = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userinfo);
        ButterKnife.inject(this);


        if (UserInfoManager.isLogin()) {
            setUserInfo();
        } else {
            ToastUtil.shortToast(UserInfoActivity.this, "您尚未登录");
        }

        titleBar.setRightBtnOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateUserInfo();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case LOCAL_PICTURE:
                    LogUtil.e(TAG, "onActivityResult LOCAL_PICTURE");
                    try {
                        if (data != null) {
                            Uri uri = data.getData();
                            startPhotoRoom(uri);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        LogUtil.e(TAG, e.toString());
                    }
                    break;
                case CAMERA_PICTURE:
                    LogUtil.e(TAG, "onActivityResult CAMERA_PICTURE");
                    try {
                        File userPhotoFile = getUserPhotoFile();
                        if (userPhotoFile != null && userPhotoFile.isFile()) {
                            startPhotoRoom(Uri.fromFile(userPhotoFile));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        LogUtil.e(TAG, e.toString());
                    }
                    break;
                case CUT_PICTURE:
                    LogUtil.e(TAG, "onActivityResult CUT_PICTURE");
                    try {
                        if (data != null) {
                            Bundle extras = data.getExtras();
                            if (extras != null) {
                                Bitmap photo = extras.getParcelable("data");

                                // TODO
                                userAvatar.setImageURI(data.getData());
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        LogUtil.e(TAG, e.toString());
                    }
                    break;

                default:
                    break;
            }
        }


    }





    private void startPhotoRoom(Uri uri) {
        Log.i(TAG, "startPhotoZoom()");
        Log.d("Temp", "startPhotoZoom uri -->> " + uri.toString());

        Intent intent = new Intent("com.android.camera.action.CROP");

        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);

        if (PhotoFileUtils.isDocumentUri(this, uri)) {
            intent.setDataAndType(PhotoFileUtils.convertUri(this, uri), "image/*");
        } else {
            intent.setDataAndType(uri, "image/*");
        }
        intent.putExtra("crop", "true");
//        intent.putExtra("return-data", true);
        intent.putExtra("return-data", false);

        intent.putExtra(MediaStore.EXTRA_OUTPUT,
                Uri.fromFile(getUserPhotoFile()));

        intent.putExtra("scale", true);
        intent.putExtra("scaleUpIfNeeded", true);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        startActivityForResult(intent, CUT_PICTURE);
        Log.i(TAG, "---------------------------->goZoom");
    }


    private static final int LOCAL_PICTURE = 101;
    private static final int CAMERA_PICTURE = 102;
    private static final int CUT_PICTURE = 103;
    @OnClick(R.id.userAvatar)
    public void changeAvatar() {

        if(!FileUtils.isSDCardMounted()) {
            ToastUtil.shortToast(UserInfoActivity.this, "请插入SD卡后重试");
            return;
        }

        AlertDialog.Builder dialog = new AlertDialog.Builder(UserInfoActivity.this);
//        dialog.setTitle("上传头像");
        final String[] items = { "拍照上传", "相册选择"};

        dialog.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();

                switch (which) {
                    case 0:
                        LogUtil.d("changeAvatar", "camera");

                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(getUserPhotoFile()));
                        startActivityForResult(intent, CAMERA_PICTURE);
                        break;
                    case 1:
                        LogUtil.d("changeAvatar", "photo");
                        Intent intent2 = new Intent(Intent.ACTION_PICK, null);
                        intent2.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                        startActivityForResult(intent2, LOCAL_PICTURE);
                        break;
                    default:
                        break;
                }
            }
        });

        dialog.show().setCanceledOnTouchOutside(true);
    }

    public File getUserPhotoFile() {
        File imageFile = null;
        try {
            File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/yeah/userphoto");
            if (!file.exists()) {
                file.mkdir();
            }
            imageFile = new File(file, "/android_user_no_image.jpg");
            return imageFile;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return imageFile;
    }

    @OnClick(R.id.info_nickname_root)
    public void changeNickName() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("编辑昵称");

        LayoutInflater inflater = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout layout = (LinearLayout)inflater.inflate(R.layout.dialog_nickname, null);
        dialog.setView(layout);

        EditText editText  = (EditText)layout.findViewById(R.id.nickname_input);

        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {


                String nickName = StringUtils.makeSafe(editText.getText().toString());

                if(StringUtils.isEmpty(nickName) || nickName.length() < 4) {
                    ToastUtil.shortToast(UserInfoActivity.this, "昵称需至少4个字符");
                    return;
                }

                nicknameTV.setText(nickName);
                updateInfo.put("nickname", nickName);
            }
        });

        dialog.setNegativeButton("取消", null);
        dialog.show();

    }

    int sexId;

    @OnClick(R.id.info_sex_root)
    public void changeSex() {

        sexId = 0;
        AlertDialog.Builder builder = new AlertDialog.Builder(UserInfoActivity.this);
        builder.setTitle("请选择性别");
        builder.setSingleChoiceItems(sex, sexId, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                sexId = which;
            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                sexTV.setText(sex[sexId]);

                // TODO 保存
//                mUserInfo.set
//                updateInfo.put()
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }

    Calendar calendar = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            LogUtil.d("DatePickerDialog", year + "-" + monthOfYear + "-" + dayOfMonth);

            if(pickOk) {
                birthdayTV.setText(birthdayFormate(calendar.getTimeInMillis()));

                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                updateInfo.put("birthday", format.format(calendar.getTimeInMillis()));
            }
        }
    };
    private boolean pickOk = false;

    @OnClick(R.id.info_birthday_root)
    public void changeBirtday() {

        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTimeInMillis(mUserInfo.getBirthday());

        DatePickerDialog dialog = new DatePickerDialog(UserInfoActivity.this,
                dateSetListener, calendar1.get(Calendar.YEAR),
                calendar1.get(Calendar.MONTH), calendar1.get(Calendar.DAY_OF_MONTH));
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        LogUtil.d("DatePickerDialog", "OK");
                        pickOk = true;
                    }
                });

        DatePicker datePicker = dialog.getDatePicker();
//        datePicker.setMinDate(-28800L); // 1070-01-01
        datePicker.setMaxDate(Calendar.getInstance().getTimeInMillis());

        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        LogUtil.d("DatePickerDialog", "Cancle");
                        pickOk = false;
                    }
                });

        dialog.show();
    }

    int constellationId;
    @OnClick(R.id.info_constellation_root)
    public void changeConstellation() {

        constellationId = 0;

        AlertDialog.Builder builder = new AlertDialog.Builder(UserInfoActivity.this);
        builder.setTitle("星座选择");
        builder.setSingleChoiceItems(constellations, constellationId, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                constellationId = which;
            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                constellationTV.setText(constellations[constellationId]);

                int horoscopeId = constellationId + 1;
                updateInfo.put("horoscope", String.valueOf(horoscopeId));
                mUserInfo.setHoroscope(horoscopeId);
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();

    }

    private void setUserInfo() {

        mUserInfo = UserInfoManager.getUserInfo();

        nicknameTV.setText(StringUtils.makeSafe(mUserInfo.getNickname()));

        int horoscope = mUserInfo.getHoroscope();
        if (horoscope > 0 && horoscope < 12) {
            constellationTV.setText(constellations[horoscope]);
        }

        getUserInfo();
    }


    private void getUserInfo() {

        if (!UserInfoManager.isLogin()) {
            ToastUtil.shortToast(UserInfoActivity.this, "你尚未登录，请登录后重试");
            return;
        }

        int loginId = UserInfoManager.getUserId();
        String token = UserInfoManager.getToken();

        RequestParams requestParams = new RequestParams();
        requestParams.put("appId", Constants.APP_ID);
        requestParams.put("appKey", Constants.APP_KEY);
        requestParams.put("loginId", loginId);
        requestParams.put("loginToken", token);
        StickerHttpClient.post("/account/user/info", requestParams,
                new TypeReference<ResponseData<UserInfo>>() {
                }.getType(),
                new StickerHttpResponseHandler<UserInfo>() {

                    @Override
                    public void onStart() {
                        showProgressDialog("获取用户信息...");
                    }

                    @Override
                    public void onSuccess(UserInfo userInfo) {

                        mUserInfo = userInfo;

                        nicknameTV.setText(StringUtils.makeSafe(userInfo.getNickname()));
//                        sex.setText(StringUtils.makeSafe(userInfo.get));
                        birthdayTV.setText(birthdayFormate(userInfo.getBirthday()));
//                        constellation.setText(StringUtils.makeSafe(userInfo.getNickname()));
                        userAvatar.setImageURI(Uri.parse(userInfo.getAvatar()));
                    }

                    @Override
                    public void onFailure(String message) {
                        LogUtil.e("onFailure", message);
                        ToastUtil.shortToast(UserInfoActivity.this, "获取用户信息失败：" + message);
                    }

                    @Override
                    public void onFinish() {
                        dismissProgressDialog();
                    }
                });
    }

    private void updateUserInfo() {

        if (!UserInfoManager.isLogin()) {
            ToastUtil.shortToast(UserInfoActivity.this, "你尚未登录，请登录后重试");
            return;
        }

//        updateInfo.put("name", "呵呵");
//        updateInfo.put("nickname", "牛轰轰");
//        updateInfo.put("avatar", "http://img5.duitang.com/uploads/item/201409/08/20140908021251_Z2y2t.thumb.700_0.jpeg");
//        updateInfo.put("birthday", "2015-10-10 0:0:0");


//        mUserInfo.setNickname("牛轰轰");
//        mUserInfo.setAvatar("http://img5.duitang.com/uploads/item/201409/08/20140908021251_Z2y2t.thumb.700_0.jpeg");
//        mUserInfo.setBirthday(1440465912123L);
//        mUserInfo.setHoroscope(2);


//        Calendar calendar = Calendar.getInstance();
//        calendar.set();
//        calendar.getTimeInMillis();

        int loginId = UserInfoManager.getUserId();
        String token = UserInfoManager.getToken();

        RequestParams requestParams = new RequestParams();
        requestParams.put("appId", Constants.APP_ID);
        requestParams.put("appKey", Constants.APP_KEY);
        requestParams.put("loginId", loginId);
        requestParams.put("loginToken", token);


        Set<String> keys = updateInfo.keySet();
        Iterator<String> it = keys.iterator();
        while (it.hasNext()) {
            String key = it.next();
            requestParams.put(key, updateInfo.get(key));
        }


        StickerHttpClient.post("/account/user/update", requestParams,
                new TypeReference<ResponseData<UserInfo>>() {
                }.getType(),
                new StickerHttpResponseHandler<UserInfo>() {

                    @Override
                    public void onStart() {
                        showProgressDialog("更新用户信息...");
                    }

                    @Override
                    public void onSuccess(UserInfo userInfo) {
                        ToastUtil.shortToast(UserInfoActivity.this, "更新用户信息成功");
                        UserInfoManager.updateUserInfo(userInfo);
                    }

                    @Override
                    public void onFailure(String message) {
                        LogUtil.e("onFailure", message);
                        ToastUtil.shortToast(UserInfoActivity.this, "更新用户信息失败：" + message);
                    }

                    @Override
                    public void onFinish() {
                        dismissProgressDialog();
                    }
                });
    }

    private String birthdayFormate(long time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date bData = new Date(time);
        return format.format(bData);
    }

    public static void launch(Context context) {
        Intent intent = new Intent(context, UserInfoActivity.class);
        context.startActivity(intent);
    }
}
