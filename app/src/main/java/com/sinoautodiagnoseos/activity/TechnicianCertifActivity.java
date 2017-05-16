package com.sinoautodiagnoseos.activity;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.sinoautodiagnoseos.R;
import com.sinoautodiagnoseos.entity.Upload.Upload;
import com.sinoautodiagnoseos.entity.User.Skill;
import com.sinoautodiagnoseos.entity.User.UserBaseData;
import com.sinoautodiagnoseos.net.requestApi.HttpRequestApi;
import com.sinoautodiagnoseos.net.requestSubscribers.HttpSubscriber;
import com.sinoautodiagnoseos.net.requestSubscribers.SubscriberOnListener;
import com.sinoautodiagnoseos.ui.UIHelper;
import com.sinoautodiagnoseos.ui.loginui.SwipeBackActivity;
import com.sinoautodiagnoseos.ui.personinfoui.ClipImageActivity;
import com.sinoautodiagnoseos.ui.personinfoui.HeadPortrait;
import com.sinoautodiagnoseos.utils.Constant;
import com.sinoautodiagnoseos.utils.OnMultiClickListener;
import com.sinoautodiagnoseos.utils.PicassoUtils;
import com.sinoautodiagnoseos.utils.SharedPreferences;
import com.sinoautodiagnoseos.utils.ToastUtils;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static android.R.attr.type;
import static com.sinoautodiagnoseos.R.id.shop_place_rl;
import static com.sinoautodiagnoseos.utils.Constant.READ_EXTERNAL_STORAGE_REQUEST_CODE;
import static com.sinoautodiagnoseos.utils.Constant.REQUEST_CAPTURE;
import static com.sinoautodiagnoseos.utils.LogUtils.deBug;

/**
 * Created by dingxujun on 2017/5/15.
 */

public class TechnicianCertifActivity extends SwipeBackActivity implements View.OnClickListener {

    private FrameLayout identity_fl;
    private String cropImagePath, imageNme;
    private File tempFile;
    private ImageView identityimage;
    private RelativeLayout shop_place;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_technician_certification);
        initView();
        initListenerOclick();
        getUserIfoData();
        createCameraTempFile(savedInstanceState);
    }

    private void getUserIfoData() {

    }

    /**
     * 初始化控件
     *
     * @author dingxujun
     * created at 2017/5/15 16:15
     */
    private void initView() {
        identity_fl = (FrameLayout) findViewById(R.id.identity_fl);
        identityimage = (ImageView) findViewById(R.id.identity_frontage_img);
        shop_place = (RelativeLayout) findViewById(shop_place_rl);
    }

    /**
     * 点击事件
     *
     * @author dingxujun
     * created at 2017/5/15 16:17
     */
    private void initListenerOclick() {
        shop_place.setOnClickListener(this);
        identity_fl.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                uploadHeadImage();
            }
        });
    }

    /**
     * 上传头像的方法
     *
     * @author dingxujun
     * created at 2017/5/9 14:21
     */
    private void uploadHeadImage() {
        View view = LayoutInflater.from(this).inflate(R.layout.layout_popupwindow, null);
        TextView btnCarema = (TextView) view.findViewById(R.id.btn_camera);
        TextView btnPhoto = (TextView) view.findViewById(R.id.btn_photo);
        TextView btnCancel = (TextView) view.findViewById(R.id.btn_cancel);
        final PopupWindow popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setBackgroundDrawable(getResources().getDrawable(android.R.color.transparent));
        popupWindow.setOutsideTouchable(true);
        View parent = LayoutInflater.from(this).inflate(R.layout.activity_main, null);
        popupWindow.showAtLocation(parent, Gravity.BOTTOM, 20, 20);
        //popupWindow在弹窗的时候背景半透明
        final WindowManager.LayoutParams params = getWindow().getAttributes();
        params.alpha = 0.5f;
        getWindow().setAttributes(params);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                params.alpha = 1.0f;
                getWindow().setAttributes(params);
            }
        });

        btnCarema.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //权限判断
                if (ContextCompat.checkSelfPermission(TechnicianCertifActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    //申请WRITE_EXTERNAL_STORAGE权限
                    ActivityCompat.requestPermissions(TechnicianCertifActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            REQUEST_CAPTURE);
                } else {
                    //跳转到调用系统相机
                    gotoCarema();
                }
                popupWindow.dismiss();
            }
        });
        btnPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //权限判断
                if (ContextCompat.checkSelfPermission(TechnicianCertifActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    //申请READ_EXTERNAL_STORAGE权限
                    ActivityCompat.requestPermissions(TechnicianCertifActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            READ_EXTERNAL_STORAGE_REQUEST_CODE);
                } else {
                    //跳转到调用系统图库
                    HeadPortrait.gotoPhoto(TechnicianCertifActivity.this);
                }
                popupWindow.dismiss();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });


    }

    /**
     * 跳转到照相机页面
     *
     * @author dingxujun
     * created at 2017/5/9 14:53
     */
    private void gotoCarema() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
        startActivityForResult(intent, Constant.REQUEST_CAPTURE);
    }

    /**
     * 检查文件是否存在
     *
     * @author dingxujun
     * created at 2017/5/9 15:07
     */
    private String checkDirPath(String dirPath) {
        if (TextUtils.isEmpty(dirPath)) {
            return "";
        }
        File dir = new File(dirPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dirPath;
    }
    /**
     * 创建调用系统照相机待存储的临时文件
     *
     * @author dingxujun
     * created at 2017/5/9 15:08
     */
    private  void createCameraTempFile(Bundle savedInstanceState) {
        if (savedInstanceState != null && savedInstanceState.containsKey("tempFile")) {
            tempFile    = (File) savedInstanceState.getSerializable("tempFile");
        } else {
            tempFile = new File(checkDirPath(Environment.getExternalStorageDirectory().getPath() + "/image/"),
                    System.currentTimeMillis() + ".jpg");
        }
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("tempFile", tempFile);
    }

    /**
     * 获取新的Activity返回过来的数据
     *
     * @author dingxujun
     * created at 2017/5/9 16:54
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        switch (requestCode) {
            case Constant.REQUEST_CAPTURE: //调用系统相机返回
                if (resultCode == RESULT_OK) {
                    gotoClipActivity(Uri.fromFile(tempFile));
                }
                break;
            case Constant.REQUEST_PICK:  //调用系统相册返回
                if (resultCode == RESULT_OK) {
                    Uri uri = intent.getData();
                    gotoClipActivity(uri);
                }
                break;
            case Constant.REQUEST_CROP_PHOTO:  //剪切图片返回
                if (resultCode == RESULT_OK) {
                    final Uri uri = intent.getData();
                    if (uri == null) {
                        return;
                    }
                    cropImagePath = getRealFilePathFromUri(getApplicationContext(), uri);//图片的路径
                    Bitmap bitMap = BitmapFactory.decodeFile(cropImagePath);//将bitmap文件转换为bitmap对象
                    //文件的名字
                    imageNme = cropImagePath.substring(cropImagePath.lastIndexOf("/") + 1);
                    //将bitmap文件转换为bitmap对象
                    //此处后面可以将bitMap转为二进制上传后台网络

                    //  byte[] mbitmapByte = UpBitmapUtils.getBitmapByte(bitMap);
                    //uploadBitmap();
                    updateUserBaseData(1, "", cropImagePath);

                }
                break;
        }
    }

    /**
     * 修改用户信息
     *
     * @param code  标示修改的信息 1-头像 2-昵称 3-生日 4-地区
     * @param id    头像id／地区id
     * @param value 头像Url／昵称／生日／地区名称（第三级地址）
     */
    public void updateUserBaseData(final int code, final String id, final String value) {
        System.out.println("Token============" + Constant.TOKEN);
        Constant.TOKEN = SharedPreferences.getInstance().getString("checktoken", "");
        Constant.REGISTRATION = SharedPreferences.getInstance().getString("RegistrationId", "");
        final UserBaseData userBaseData = new UserBaseData();
        switch (code) {
            case 1:
                File identityBackfFile = new File(value);
                RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), identityBackfFile);
                MultipartBody.Builder builder = new MultipartBody.Builder();
                builder.addFormDataPart("image", identityBackfFile.getName(), requestFile);
                builder.addFormDataPart("title", "image");
                builder.addFormDataPart("alt", "image");
                builder.setType(MultipartBody.FORM);
                MultipartBody multipartBody = builder.build();
                HttpRequestApi.getInstance().uploadFile(multipartBody, new HttpSubscriber<Upload>(new SubscriberOnListener<Upload>() {
                    @Override
                    public void onSucceed(final Upload upload) {
                        userBaseData.setAvatarId(upload.getData().getId());
                        userBaseData.setAvatorUrl(upload.getData().downloadUrl);
                        String downloadUrl = upload.getData().downloadUrl;
                        SharedPreferences.getInstance().putString("avatar", downloadUrl);
                        //avatarimage= SharedPreferences.getInstance().getString("avatar", "");
                            if (downloadUrl !=null&&!TextUtils.isEmpty(downloadUrl)) {
                                PicassoUtils.loadImageViewHolder(TechnicianCertifActivity.this, downloadUrl, identityimage);
                               // EventBus.getDefault().post(downloadUrl);
                        }
                        uPDateUsermod(userBaseData, code, id, value);
                        System.out.println("========我是成功提价=========");
                    }

                    @Override
                    public void onError(int code, String msg) {
                    }
                }, TechnicianCertifActivity.this));

                break;
            case 2:
                userBaseData.setName(value);
                uPDateUsermod(userBaseData, code, id, value);


                break;
            case 3:
                userBaseData.setBirthday(value);
                uPDateUsermod(userBaseData, code, id, value);

                break;
            case 4:
                userBaseData.setAreaId(id);
                userBaseData.setAreaNames(value);
                uPDateUsermod(userBaseData, code, id, value);
                break;

        }

    }

    /**
     * 修改用户信息的接口
     *
     * @author dingxujun
     * created at 2017/5/11 10:59
     */
    private void uPDateUsermod(final UserBaseData userBaseData, int code, final String id, final String value) {
        Gson gson = new Gson();
        String userJson = gson.toJson(userBaseData);
        Log.e("TAG", userJson);
        RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), userJson);
        Log.e("TAG", requestBody.toString() + "44444444444444444444444444444444444444444444");
        HttpRequestApi.getInstance().updateUserbaseData(requestBody, new HttpSubscriber<Skill>(new SubscriberOnListener<Skill>() {
            @Override
            public void onSucceed(Skill data) {
                ToastUtils.makeShortText("设置成功", TechnicianCertifActivity.this);
                System.out.println("--------------------请求成功我是用户信息-------------------");
            }

            @Override
            public void onError(int code, String msg) {
                String name = userBaseData.getName();

                System.out.println("--------------------请求失败我是用户信息-------------------" + name + "\n"
                        + code + "----" + msg);
            }
        }, TechnicianCertifActivity.this));
    }

    /**
     * 打开截图界面
     *
     * @param uri
     */
    public void gotoClipActivity(Uri uri) {
        if (uri == null) {
            return;
        }
        Intent intent = new Intent();
        intent.setClass(this, ClipImageActivity.class);
        intent.putExtra("type", type);
        intent.setData(uri);
        //打开新的Activity，等新的Activity关闭后，将把新的Activity的数据返回给自己
        startActivityForResult(intent, Constant.REQUEST_CROP_PHOTO);
    }


    /**
     * 根据Uri返回文件绝对路径
     * 兼容了file:///开头的 和 content://开头的情况
     *
     * @param context
     * @param uri
     * @return the file path or null
     */
    public static String getRealFilePathFromUri(final Context context, final Uri uri) {
        if (null == uri) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.shop_place_rl:
                UIHelper.showSelectStore(TechnicianCertifActivity.this);
                break;
            default:
                break;

        }
    }
}
