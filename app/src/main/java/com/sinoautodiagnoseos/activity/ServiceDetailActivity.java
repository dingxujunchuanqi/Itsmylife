package com.sinoautodiagnoseos.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sinoautodiagnoseos.R;
import com.sinoautodiagnoseos.ui.loginui.SwipeBackActivity;
import com.sinoautodiagnoseos.utils.MyIntent;
import com.sinoautodiagnoseos.utils.PermissionUtils;
import com.sinoautodiagnoseos.utils.ToastUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by HQ_Demos on 2017/5/24.
 */

public class ServiceDetailActivity extends SwipeBackActivity{
    private String permission = Manifest.permission.READ_PHONE_STATE;
    private String[] permissionArray = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private RelativeLayout service_back_click;
    private TextView service_title_txt;
    private WebView webview;
    String title;
    int caseId;
    String url = "http://42.159.202.20:55555/index.html?";
    private final static int DownLoadSuccessful=1;
    private String fileAllPath,fileName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_servicedetail);
        getData();
        initView();
    }

    private void getData() {
        title=getIntent().getExtras().getString("title");
        caseId=getIntent().getExtras().getInt("caseId");
    }

    private void initView() {
        service_back_click= (RelativeLayout) findViewById(R.id.service_back_click);
        service_title_txt= (TextView) findViewById(R.id.service_title_txt);
        webview= (WebView) findViewById(R.id.webview);

        service_title_txt.setText(title);
        service_back_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        WebSettings webSettings = webview.getSettings();
        webSettings.setDomStorageEnabled(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        webview.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webview.setWebChromeClient(new WebChromeClient(){
            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                return super.onJsAlert(view, url, message, result);
            }

            @Override
            public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
                return super.onJsConfirm(view, url, message, result);
            }
        });

        webview.setWebViewClient(new WebViewClient(){
            Dialog progressDialog= ProgressDialog.show(ServiceDetailActivity.this,null,"正在加载...");
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressDialog.cancel();
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                progressDialog.show();
            }

            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                System.out.println("--------url-----"+url);
                PermissionUtils.checkPermissionArray(ServiceDetailActivity.this,permissionArray,2);
                File tempFile=new File(url.trim());
                fileName=tempFile.getName();
                System.out.println("---fileName--"+fileName);
                if (url!=null){
                    String sdcardDir = Environment.getExternalStorageDirectory().getPath()+"/SinoAuto/";
                    fileAllPath=sdcardDir+fileName;
                    getFilePath(sdcardDir,fileName);
                    downloadAsyn(url,sdcardDir,fileName);
                    return true;
                }else {
                    return false;
                }
//                return super.shouldOverrideUrlLoading(view, url);
            }
        });

        webview.setBackgroundColor(0);
        webview.loadUrl(url+"caseId="+caseId);

    }

    public boolean filesExists( String fileAllPath){
        try{
            File file = new File(fileAllPath);
            if(!file.exists()){
                return false;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return true;
    }

    public static File getFilePath(String filePath,
                                   String fileName) {
        File file = null;
        makeRootDirectory(filePath);
        try {
            file = new File(filePath + fileName);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return file;
    }

    public static void makeRootDirectory(String filePath) {
        File file = null;
        try {
            file = new File(filePath);
            if (!file.exists()) {
                file.mkdir();
            }
        } catch (Exception e) {

        }
    }

    Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case DownLoadSuccessful:
                    openFile(fileAllPath);
                    break;
            }
            super.handleMessage(msg);
        }
    };

    private void openFile(String fileAllPath) {
        if (filesExists(fileAllPath)){
            if (fileName.contains(".jpg")||fileName.contains(".png")||fileName.contains(".bmp")||
                    fileName.contains(".gif")){
                startActivity( MyIntent.getImageFileIntent(fileAllPath));
            } else if(fileName.contains(".html")){
                startActivity(MyIntent.getHtmlFileIntent(fileAllPath));
            }
            else if (fileName.contains(".txt")){
                startActivity(MyIntent.getTextFileIntent(fileAllPath,true));
            }
            else if (fileName.contains(".pdf")){
                startActivity(MyIntent.getPdfFileIntent(fileAllPath));
            }
            else if (fileName.contains(".mp3")||fileName.contains(".wav"))
            {
                startActivity(MyIntent.getAudioFileIntent(fileAllPath));
            }
            else if (fileName.contains(".mp4")||fileName.contains(".avi")||
                    fileName.contains(".rmvb")||fileName.contains(".3gp"))
            {
                startActivity(MyIntent.getVideoFileIntent(fileAllPath));
            }
            else if (fileName.contains(".doc")||fileName.contains(".docx")){
                startActivity(MyIntent.getWordFileIntent(fileAllPath));
            }
            else if (fileName.contains(".xls")||fileName.contains(".xlsx")){
                startActivity(MyIntent.getExcelFileIntent(fileAllPath));
            }
            else if (fileName.contains(".ppt")){
                startActivity( MyIntent.getPptFileIntent(fileAllPath));
            }else {
                ToastUtils.showShort(ServiceDetailActivity.this,"无法打开该格式文件");
            }
        }
    }

    public void downloadAsyn(final String url, final String destDir, final String fileNames){
        final OkHttpClient client= new OkHttpClient();
        final Request request = new Request.Builder()
                .url(url)
                .build();
        final Call call=client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                System.out.println(response.isSuccessful());
                InputStream is=null;
                byte[]buf=new byte[2048];
//                sendMsg(1);
                int numread=0;
                FileOutputStream fos=null;
                try {
                    is=response.body().byteStream();
                    final File file = new File(destDir + fileNames);
                    fos=new FileOutputStream(file);
                    while ((numread=is.read(buf))!=-1){
                        fos.write(buf,0,numread);
//                        sendMsg(1);// 更新进度条
                    }
                    Message msg=mHandler.obtainMessage(DownLoadSuccessful);
                    mHandler.sendMessage(msg);
////                    sendMsg(2);// 通知下载完成
//                    android.os.Message msg = msg_handler.obtainMessage();
//                    msg.what=5;
//                    msg_handler.sendMessage(msg);
                    fos.close();
                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    try {
                        if (is!=null)is.close();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    try {
                        if (fos!=null)fos.close();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PermissionUtils.PERMISSION_REQUEST_CODE:
                if (PermissionUtils.verifyPermissions(grantResults)) {
                    //do action
                    System.out.println("-----------1111111");
                }else {
                    ToastUtils.showShort(this, "权限被拒绝");
                }
                break;
            default:super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PermissionUtils.PERMISSION_SETTING_REQ_CODE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (Settings.canDrawOverlays(this)) {
                    //do action
                    System.out.println("-----------222222222");
                } else {
                    Toast.makeText(this, "没有设置程序运行所需权限", Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        }
    }
}
