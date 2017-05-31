package com.sinoautodiagnoseos.ui.personinfoui;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.sinoautodiagnoseos.Manifest;
import com.sinoautodiagnoseos.R;
import com.sinoautodiagnoseos.activity.TechnicianCertifActivity;
import com.sinoautodiagnoseos.app.AppContext;
import com.sinoautodiagnoseos.utils.Constant;

import java.io.File;

import static android.R.attr.type;
import static com.sinoautodiagnoseos.R.id.view;
import static com.sinoautodiagnoseos.utils.Constant.READ_EXTERNAL_STORAGE_REQUEST_CODE;
import static com.sinoautodiagnoseos.utils.Constant.REQUEST_CAPTURE;
import static com.sinoautodiagnoseos.utils.Constant.REQUEST_PICK;

/**
 * Created by dingxujun on 2017/5/9.
 */

public class HeadPortraitUtils {
    public static File tempFile;
    /**
     * 跳到系统相册页面
     * @author dingxujun
     * created at 2017/5/9 14:38
     */
    public static void gotoPhoto(Activity context) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        context.startActivityForResult(Intent.createChooser(intent, "请选择图片"), REQUEST_PICK);
    }
    /**
     * 创建调用系统照相机待存储的临时文件
     *
     * @author dingxujun
     * created at 2017/5/9 15:08
     */
    public static void createCameraTempFile(Bundle savedInstanceState) {
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
    public static String checkDirPath(String dirPath) {
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
     * 上传头像图片的方法
     *
     * @author dingxujun
     * created at 2017/5/9 14:21
     */
    public static void uploadHeadImage(final Activity context) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_popupwindow, null);
        TextView btnCarema = (TextView) view.findViewById(R.id.btn_camera);
        TextView btnPhoto = (TextView) view.findViewById(R.id.btn_photo);
        TextView btnCancel = (TextView) view.findViewById(R.id.btn_cancel);
        final PopupWindow popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setBackgroundDrawable(context.getResources().getDrawable(android.R.color.transparent));
        popupWindow.setOutsideTouchable(true);
        View parent = LayoutInflater.from(context).inflate(R.layout.activity_main, null);
        popupWindow.showAtLocation(parent, Gravity.BOTTOM, 20, 20);
        //popupWindow在弹窗的时候背景半透明
        final WindowManager.LayoutParams params = context.getWindow().getAttributes();
        params.alpha = 0.5f;
        context.getWindow().setAttributes(params);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                params.alpha = 1.0f;
                context.getWindow().setAttributes(params);
            }
        });

        btnCarema.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //权限判断
                if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    //申请WRITE_EXTERNAL_STORAGE权限
                    ActivityCompat.requestPermissions((Activity) context, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            REQUEST_CAPTURE);
                } else {
                    //跳转到调用系统相机
                    gotoCarema(context);
                }
                popupWindow.dismiss();
            }
        });
        btnPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //权限判断
                if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    //申请READ_EXTERNAL_STORAGE权限
                    ActivityCompat.requestPermissions((Activity) context, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                            READ_EXTERNAL_STORAGE_REQUEST_CODE);
                } else {
                    //跳转到调用系统图库
                    gotoPhoto((Activity) context);
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
    public static void gotoCarema(Activity context) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
        context.startActivityForResult(intent, Constant.REQUEST_CAPTURE);
    }

    /**
     * 打开截图界面
     *
     * @param uri
     */
    public static void gotoClipActivity(Uri uri, Activity context) {
        if (uri == null) {
            return;
        }
        Intent intent = new Intent();
        intent.setClass(context, ClipImageActivity.class);
        intent.putExtra("type", type);//添加标记
        intent.setData(uri);
        //打开新的Activity，等新的Activity关闭后，将把新的Activity的数据返回给自己
        context.startActivityForResult(intent, Constant.REQUEST_CROP_PHOTO);
    }

    /**
     * 根据Uri返回文件绝对路径
     * 兼容了file:///开头的 和 content://开头的情况
     * @param context
     * @param uri
     * @return the file path or null
     */
    public static String getRealFilePathFromUri(Context context, final Uri uri) {
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
}



