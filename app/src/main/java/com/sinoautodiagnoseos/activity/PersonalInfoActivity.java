package com.sinoautodiagnoseos.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.sinoautodiagnoseos.R;
import com.sinoautodiagnoseos.entity.Upload.Upload;
import com.sinoautodiagnoseos.entity.User.UserBaseData;
import com.sinoautodiagnoseos.entity.User.UserInfo;
import com.sinoautodiagnoseos.net.requestApi.HttpRequestApi;
import com.sinoautodiagnoseos.net.requestSubscribers.HttpSubscriber;
import com.sinoautodiagnoseos.net.requestSubscribers.SubscriberOnListener;
import com.sinoautodiagnoseos.openvcall.model.UploadDatas;
import com.sinoautodiagnoseos.ui.loginui.SwipeBackActivity;
import com.sinoautodiagnoseos.ui.personinfoui.ClipImageActivity;
import com.sinoautodiagnoseos.ui.personinfoui.HeadPortrait;
import com.sinoautodiagnoseos.ui.personinfoui.LoginDialogFragment;
import com.sinoautodiagnoseos.utils.OnMultiClickListener;
import com.sinoautodiagnoseos.utils.PicassoUtils;
import com.sinoautodiagnoseos.utils.SharedPreferences;
import com.sinoautodiagnoseos.utils.ToastUtils;
import com.sinoautodiagnoseos.utils.UpBitmapUtils;
import com.sinoautodiagnoseos.utils.Constant;
import com.sinoautodiagnoseos.utils.LogUtils;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static android.R.attr.button;
import static android.R.id.empty;
import static com.sinoautodiagnoseos.R.id.avatar;
import static com.sinoautodiagnoseos.R.id.view;
import static com.sinoautodiagnoseos.R.style.dialog;
import static com.sinoautodiagnoseos.utils.Constant.READ_EXTERNAL_STORAGE_REQUEST_CODE;
import static com.sinoautodiagnoseos.utils.Constant.REQUEST_CAPTURE;
import static com.sinoautodiagnoseos.utils.LogUtils.deBug;

/**
 * Created by dingxujun on 2017/5/4.
 */

public class PersonalInfoActivity extends SwipeBackActivity implements View.OnClickListener, LoginDialogFragment.LoginInputListener {
    private static final String TAG = "PersonalInfoActivity";
    private static final int DATE_DIALOG = 1;
    private FrameLayout image_back;
    private RelativeLayout headPtClick;
    private File tempFile;
    private int type;
    private CircleImageView circlrimage;
    private String cropImagePath, imageNme;
    private String download_url;
    private RelativeLayout setpetnamee;
    private UserInfo userdata;
    private String userId;
    private String birthday;
    private String avatarimage,avatar;
    private String download_id;
    private TextView birthdtv;
    private int mYear, mMonth, mDay;
    private RelativeLayout birthday_update;
    private RelativeLayout addressupd;
    private TextView provicialtv;
    private TextView citytv;
    private TextView countrytv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);
        initView();
        initListenerOclick();
        getUserInfo();
        initDialog();
        createCameraTempFile(savedInstanceState);

//        avatarimage = SharedPreferences.getInstance().getString("avatar", "");
//        LogUtils.deBug(TAG, "=我是oncreat" + avatarimage);
//        if (avatarimage !=null&&!TextUtils.isEmpty(avatarimage)) {
//                PicassoUtils.loadImageView(PersonalInfoActivity.this, avatarimage, circlrimage);
//            }
//        userId = userdata.getData().userId;
//        this.avatar = userdata.getData().getAvatar();
//        birthday = userdata.getData().getBirthday();
        System.out.println("----------------userid----------------"+ userId);
    }
/**
* 初始化dialog常量
*@author dingxujun
*created at 2017/5/11 14:07
*/
    private void initDialog() {
        final Calendar ca = Calendar.getInstance();
        mYear = ca.get(Calendar.YEAR);
        mMonth = ca.get(Calendar.MONTH);
        mDay = ca.get(Calendar.DAY_OF_MONTH);
        deBug(TAG,"\\\\\\\\\\\\\\\\\\\\\\\\"+mYear);
        deBug(TAG,"\\\\\\\\\\\\\\\\\\\\\\\\"+mMonth);
        deBug(TAG,"\\\\\\\\\\\\\\\\\\\\\\\\"+mDay);
    }

    /**
    * 设置日期 利用StringBuffer追加
    *@author dingxujun
    *created at 2017/5/11 14:11
    */

    public void display() {
        birthdtv.setText(new StringBuffer().append(mYear).append("-").append(mMonth+1).append("-").append(mDay).append(" "));
        String birthdata = birthdtv.getText().toString().trim();

      //  uPDateUsermod(userBaseData,3,userId,birthdata);
        updateUserBaseData(3,userId,birthdata);
        System.out.println("====================我是UserID"+userId+birthdata);
        System.out.println("============================"+birthdata);
    }

    private DatePickerDialog.OnDateSetListener mdateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;
            display();
        }
    };
    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG:
                return new DatePickerDialog(this, mdateListener, mYear, mMonth, mDay);
        }
        return null;
    }
    private void initView() {
        image_back = (FrameLayout) findViewById(R.id.back_click);
        headPtClick = (RelativeLayout) findViewById(R.id.head_pt_click);
        circlrimage = (CircleImageView) findViewById(R.id.round_head);
        setpetnamee = (RelativeLayout) findViewById(R.id.set_petname);
        birthdtv = (TextView) findViewById(R.id.birthday_tv);
        birthday_update = (RelativeLayout) findViewById(R.id.birthday_update);
        addressupd = (RelativeLayout) findViewById(R.id.address_update);
        provicialtv = (TextView) findViewById(R.id.provincial_tv);
        citytv = (TextView) findViewById(R.id.city_tv);
        countrytv = (TextView) findViewById(R.id.country_tv);

    }

    private void initListenerOclick() {
        image_back.setOnClickListener(this);
        headPtClick.setOnClickListener(this);
        setpetnamee.setOnClickListener(this);
        birthday_update.setOnClickListener(this);
        addressupd.setOnClickListener(this);
    }
    /**
    *修改昵称的方法 dialog
    *@author dingxujun
    *created at 2017/5/10 16:19
    */
    private void showSubmitAlertDialog() {
        final AlertDialog dialog = new AlertDialog.Builder(this,R.style.DialogTheme).create();
        dialog.setView(LayoutInflater.from(this).inflate(R.layout.pesonrinfo_alert_dialog, null));
        dialog.show();
        dialog.getWindow().setContentView(R.layout.pesonrinfo_alert_dialog);
        Button btnCommit = (Button) dialog.findViewById(R.id.commit_but);
        Button btnNegative = (Button) dialog.findViewById(R.id.btn_cancel);
        final EditText etContent = (EditText) dialog.findViewById(R.id.et_content);
        btnCommit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                String str = etContent.getText().toString();
                if (isNullEmptyBlank(str)) {
                    etContent.setError(String.valueOf(R.string.Input_box_cannot_be_empty));
                } else {
                    updateUserBaseData(2,"",str);
                    dialog.dismiss();
                    Toast.makeText(PersonalInfoActivity.this, "修改成功", Toast.LENGTH_LONG).show();
                    download_id = SharedPreferences.getInstance().getString("download_id", "");
                    deBug(TAG,"44444444444444444444444444444"+ download_id);
                }
            }
        });
        btnNegative.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                dialog.dismiss();
            }
        });
    }

    /**
    *输入框内容不能为空的方法
    *@author dingxujun
    *created at 2017/5/10 16:24
    */
    private static boolean isNullEmptyBlank(String str) {
        if (str == null || "".equals(str) || "".equals(str.trim()))
            return true;
        return false;
    }

    /**
     * 修改用户信息
     *
     * @param code    标示修改的信息 1-头像 2-昵称 3-生日 4-地区
     * @param id      头像id／地区id
     * @param value   头像Url／昵称／生日／地区名称（第三级地址）
     */
    public void updateUserBaseData(final int code, final String id, final String value) {
        System.out.println("Token============" + Constant.TOKEN);
        Constant.TOKEN=SharedPreferences.getInstance().getString("checktoken","");
        Constant.REGISTRATION=SharedPreferences.getInstance().getString("RegistrationId","");
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
                        uPDateUsermod(userBaseData, code, id, value);

                    }

                    @Override
                    public void onError(int code, String msg) {

                    }
                }, PersonalInfoActivity.this));
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
    *修改用户信息的接口
    *@author dingxujun
    *created at 2017/5/11 10:59
    */
    private void uPDateUsermod(UserBaseData userBaseData, int code, final String id, final String value) {
        Gson gson = new Gson();
        String userJson = gson.toJson(userBaseData);
        Log.e("TAG",userJson);
        RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), userJson);
        Log.e("TAG",requestBody.toString()+"44444444444444444444444444444444444444444444");
        HttpRequestApi.getInstance().updateUserbaseData(requestBody,new HttpSubscriber<UserBaseData>(new SubscriberOnListener<UserBaseData>() {
            @Override
            public void onSucceed(UserBaseData data) {
                ToastUtils.makeShortText("设置成功",PersonalInfoActivity.this);
                System.out.println("--------------------请求成功我是用户信息-------------------");
            }

            @Override
            public void onError(int code, String msg) {
                System.out.println("--------------------请求失败我是用户信息-------------------"+"\n"
                +code+"----"+msg);
            }
        },PersonalInfoActivity.this));
//        final Map<String, Object> map = new HashMap<>();
//        map.put("userid", userId);
//        map.put("avatar", avatar);
//        map.put("download_id",download_id);
//        Gson gson = new Gson();
//        String updatejson = gson.toJson(map);
//        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), updatejson);
//        HttpRequestApi.getInstance().updateUserbaseData(requestBody,new HttpSubscriber<UserBaseData>(new SubscriberOnListener<UserBaseData>() {
//            @Override
//            public void onSucceed(UserBaseData data) {
//
//            }
//
//            @Override
//            public void onError(int code, String msg) {
//
//            }
//        },PersonalInfoActivity.this));
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_click:
                finish();
                break;
            case R.id.head_pt_click:
                type = 1;
                uploadHeadImage();
                break;
            case R.id.set_petname:
                showSubmitAlertDialog();
                break;
            case R.id.birthday_update:
                showDialog(DATE_DIALOG);
                break;
            case R.id.address_update:
                showSelectDialog();
                break;
            default:
                break;
        }
    }
/**
*显示省市区的dialog
*@author dingxujun
*created at 2017/5/12 9:49
*/
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public void showSelectDialog() {
    LoginDialogFragment dialog = new LoginDialogFragment();
    dialog.show(getFragmentManager(), "loginDialog");
}

    /**
     * 外部存储权限申请返回
     *
     * @author dingxujun
     * created at 2017/5/9 16:22
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Constant.WRITE_EXTERNAL_STORAGE_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission Granted
                gotoCarema();
            } else {
                // Permission Denied
            }
        } else if (requestCode == Constant.READ_EXTERNAL_STORAGE_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission Granted
                HeadPortrait.gotoPhoto(PersonalInfoActivity.this);
            } else {
                // Permission Denied
            }
        }
    }

    /**
     * 接收传过来的对象
     *
     * @author dingxujun
     * created at 2017/5/9 9:40
     */
    private void getUserInfo() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        userdata = (UserInfo) bundle.getSerializable("userdata");
        userId = userdata.getData().getUserId();
        download_id= SharedPreferences.getInstance().getString("download_id", "");

       // userdata.getData().
        //.deBug(TAG, "==================" + userdata.getData().getRoleName());
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
                if (ContextCompat.checkSelfPermission(PersonalInfoActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    //申请WRITE_EXTERNAL_STORAGE权限
                    ActivityCompat.requestPermissions(PersonalInfoActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
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
                if (ContextCompat.checkSelfPermission(PersonalInfoActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    //申请READ_EXTERNAL_STORAGE权限
                    ActivityCompat.requestPermissions(PersonalInfoActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            READ_EXTERNAL_STORAGE_REQUEST_CODE);
                } else {
                    //跳转到调用系统图库
                    HeadPortrait.gotoPhoto(PersonalInfoActivity.this);
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
     * 创建调用系统照相机待存储的临时文件
     *
     * @author dingxujun
     * created at 2017/5/9 15:08
     */
    private void createCameraTempFile(Bundle savedInstanceState) {
        if (savedInstanceState != null && savedInstanceState.containsKey("tempFile")) {
            tempFile = (File) savedInstanceState.getSerializable("tempFile");
        } else {
            tempFile = new File(checkDirPath(Environment.getExternalStorageDirectory().getPath() + "/image/"),
                    System.currentTimeMillis() + ".jpg");
        }
    }

    /**
     * 检查文件是否存在
     *
     * @author dingxujun
     * created at 2017/5/9 15:07
     */
    private static String checkDirPath(String dirPath) {
        if (TextUtils.isEmpty(dirPath)) {
            return "";
        }
        File dir = new File(dirPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dirPath;
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
                    deBug(TAG, cropImagePath);
                    deBug(TAG, imageNme);

                    //此处后面可以将bitMap转为二进制上传后台网络

                  //  byte[] mbitmapByte = UpBitmapUtils.getBitmapByte(bitMap);
                    uploadBitmap();

                }
                break;
        }
    }

    UploadDatas uploadDatas;

    /**
     * 把头像图片上传给服务器
     *
     * @author dingxujun
     * created at 2017/5/9 17:49
     */
    private void uploadBitmap() {

        com.sinoautodiagnoseos.utils.Constant.TOKEN = com.sinoautodiagnoseos.utils.SharedPreferences.getInstance().getString("checktoken", "");
        com.sinoautodiagnoseos.utils.Constant.REGISTRATION = com.sinoautodiagnoseos.utils.SharedPreferences.getInstance().getString("RegistrationId", "");
        HttpRequestApi.getInstance().uploadFile(UpBitmapUtils.upImage(cropImagePath, imageNme),
                new HttpSubscriber<Upload>(new SubscriberOnListener<Upload>() {



                    @Override
                    public void onSucceed(Upload upload) {
                        deBug(TAG, "==============请求成功1");
                        //图片下载文件地址
                        download_url = upload.getData().getDownloadUrl();
                        String download_fileName = upload.getData().getFilename();//图片名
                        String download_id = upload.getData().getId();//图片id
                        String extensio = upload.getData().getExtension();
                        SharedPreferences.getInstance().putString("avatar",download_url);
                        SharedPreferences.getInstance().putString("download_id",download_id);
                        avatarimage= SharedPreferences.getInstance().getString("avatar", "");
                        if (type == 1) {
                            if (avatarimage !=null&&!TextUtils.isEmpty(avatarimage)) {
                                PicassoUtils.loadImageViewSize(PersonalInfoActivity.this, download_url, 200, 300, circlrimage);
                                EventBus.getDefault().post(avatarimage);
                            }
                        }
                        uploadDatas = new UploadDatas();
                        uploadDatas.setCallHistoryId(com.sinoautodiagnoseos.utils.Constant.ROOMID);
                        uploadDatas.setFileId(download_id);
                        uploadDatas.setFileName(download_fileName);
                        uploadDatas.setFileUrl(download_url);
                        uploadDatas.setFileSize(0);
                        uploadDatas.setContentType(extensio);
                        Gson gson = new Gson();
                        String json = gson.toJson(uploadDatas);

                        Log.e("JSON", json);
                        com.sinoautodiagnoseos.utils.Constant.TOKEN = com.sinoautodiagnoseos.utils.SharedPreferences.getInstance().getString("checktoken", "");
                        com.sinoautodiagnoseos.utils.Constant.REGISTRATION = com.sinoautodiagnoseos.utils.SharedPreferences.getInstance().getString("RegistrationId", "");
                    }

                    @Override
                    public void onError(int code, String msg) {
                        deBug(TAG, "==============请求失败1");
                    }
                }, PersonalInfoActivity.this));
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
    public void onLoginInputComplete(String username1,String username2,String username3) {
        Toast.makeText(PersonalInfoActivity.this,"设置成功",Toast.LENGTH_SHORT).show();
        provicialtv.setText(username1);
        citytv.setText(username2);
        countrytv.setText(username3);
    }
}