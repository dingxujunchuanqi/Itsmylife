package com.sinoautodiagnoseos.ui.personinfoui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;

import java.io.File;

import static com.sinoautodiagnoseos.utils.Constant.REQUEST_CAPTURE;
import static com.sinoautodiagnoseos.utils.Constant.REQUEST_PICK;

/**
 * Created by dingxujun on 2017/5/9.
 */

public class HeadPortrait {
    //调用照相机返回图片临时文件

    /**
     * 跳到系统相册页面
     *
     * @author dingxujun
     * created at 2017/5/9 14:38
     */

    public static void gotoPhoto(Activity context) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        context.startActivityForResult(Intent.createChooser(intent, "请选择图片"), REQUEST_PICK);
    }

}
