package com.sinoautodiagnoseos.utils;

import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by dingxujun on 2017/5/9.
 */

public class UpBitmapUtils {
    /**
    * 将bitmap对象转换为二进制
    *@author dingxujun
    *created at 2017/5/9 16:39
    */
    public static byte[] getBitmapByte(Bitmap bitmap){
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
        try {
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return out.toByteArray();
    }
    /**
     * 上传图片的请求封装
     *
     *
     * @param path
     * @param fileName
     * @return
     */
    public static MultipartBody upImage(String path, String fileName) {
        File file = new File(path);
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.addFormDataPart("file", fileName, requestFile);
        builder.addFormDataPart("title", fileName);
        builder.addFormDataPart("alt", "");
        builder.setType(MultipartBody.FORM);
        MultipartBody multipartBody = builder.build();
        return multipartBody;
    }
}
