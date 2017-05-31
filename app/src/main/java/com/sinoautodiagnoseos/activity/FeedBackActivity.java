package com.sinoautodiagnoseos.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.sinoautodiagnoseos.R;
import com.sinoautodiagnoseos.entity.Feedback.Feedback;
import com.sinoautodiagnoseos.entity.Feedback.Feedbackfiles;
import com.sinoautodiagnoseos.entity.Upload.Upload;
import com.sinoautodiagnoseos.net.requestApi.HttpRequestApi;
import com.sinoautodiagnoseos.net.requestSubscribers.HttpSubscriber;
import com.sinoautodiagnoseos.net.requestSubscribers.SubscriberOnListener;
import com.sinoautodiagnoseos.ui.loginui.SwipeBackActivity;
import com.sinoautodiagnoseos.ui.photopicker.ImageCaptureManager;
import com.sinoautodiagnoseos.ui.photopicker.PhotoPickerActivity;
import com.sinoautodiagnoseos.ui.photopicker.PhotoPreviewActivity;
import com.sinoautodiagnoseos.ui.photopicker.SelectModel;
import com.sinoautodiagnoseos.ui.photopicker.intent.PhotoPickerIntent;
import com.sinoautodiagnoseos.ui.photopicker.intent.PhotoPreviewIntent;
import com.sinoautodiagnoseos.utils.Constant;
import com.sinoautodiagnoseos.utils.OnMultiClickListener;
import com.sinoautodiagnoseos.utils.SharedPreferences;
import com.sinoautodiagnoseos.utils.ToastUtils;

import org.json.JSONArray;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by HQ_Demos on 2017/5/19.
 */
public class FeedBackActivity extends SwipeBackActivity{
    private final String TAG=FeedBackActivity.class.getSimpleName();
    private RelativeLayout feedback_back_click;
    private EditText content_edit;
    private GridView upload_gridview;
    private Button upload_btn;
    private GridAdapter adapter;
    private ArrayList<String>imagePaths =new ArrayList<>();
    private static final int REQUEST_CAMERA_CODE = 10;
    private static final int REQUEST_PREVIEW_CODE = 20;

    private ImageCaptureManager captureManager; // 相机拍照处理类
    int cols;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        initView();
    }

    private void initView() {
        content_edit= (EditText) findViewById(R.id.content_edit);
        upload_gridview= (GridView) findViewById(R.id.upload_gridview);
        feedback_back_click= (RelativeLayout) findViewById(R.id.feedback_back_click);
        feedback_back_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        cols = getResources().getDisplayMetrics().widthPixels / getResources().getDisplayMetrics().densityDpi;
        cols = cols < 3 ? 3 : cols;
        upload_gridview.setNumColumns(cols);

        upload_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String imgs = (String) parent.getItemAtPosition(position);
                if ("000000".equals(imgs)){
                    PhotoPickerIntent intent = new PhotoPickerIntent(FeedBackActivity.this);
                    intent.setSelectModel(SelectModel.MULTI);
                    intent.setShowCarema(true); // 是否显示拍照
                    intent.setMaxTotal(3); // 最多选择照片数量，默认为3
                    intent.setSelectedPaths(imagePaths); // 已选中的照片地址， 用于回显选中状态
                    startActivityForResult(intent, REQUEST_CAMERA_CODE);
                }else {
                    PhotoPreviewIntent intent = new PhotoPreviewIntent(FeedBackActivity.this);
                    intent.setCurrentItem(position);
                    intent.setPhotoPaths(imagePaths);
                    startActivityForResult(intent, REQUEST_PREVIEW_CODE);
                }
            }
        });
        imagePaths.add("000000");
        adapter=new GridAdapter(imagePaths );
        upload_gridview.setAdapter(adapter);

        upload_btn= (Button) findViewById(R.id.send_button);

        upload_btn.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                String content_txt=content_edit.getText().toString().trim();
                if (imagePaths.size()==0){
                    ToastUtils.showShort(FeedBackActivity.this,"请选择一张图片");
                }else if (content_txt.equals("")){
                    ToastUtils.showShort(FeedBackActivity.this,"请提出您的宝贵建议");
                }else {
                    postFeedback(content_txt,imagePaths);
                    finish();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK){
            switch (requestCode){
                //选择照片
                case REQUEST_CAMERA_CODE:
                    ArrayList<String> list = data.getStringArrayListExtra(PhotoPickerActivity.EXTRA_RESULT);
                    Log.d(TAG, "list: " + "list = [" + list.size()+"]");
                    loadAdapter(list);
                    break;
                //预览
                case REQUEST_PREVIEW_CODE:
                    ArrayList<String> ListExtra = data.getStringArrayListExtra(PhotoPreviewActivity.EXTRA_RESULT);
                    Log.d(TAG, "ListExtra: " + "ListExtra = [" + ListExtra.size()+"]");
                    loadAdapter(ListExtra);
                    break;
            }
        }
    }

    private void loadAdapter(ArrayList<String>paths){
        if (imagePaths!=null&&imagePaths.size()>0)
        {
            imagePaths.clear();
        }
        if (paths.contains("000000"))
        {
            paths.remove("000000");
        }
        paths.add("000000");
        imagePaths.addAll(paths);
        adapter=new GridAdapter(imagePaths);
        upload_gridview.setAdapter(adapter);
        try{
            JSONArray array = new JSONArray(imagePaths);
            Log.e("-----",array.toString());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private class GridAdapter extends BaseAdapter{
        private ArrayList<String> listUrls;
        private LayoutInflater inflater;

        public GridAdapter(ArrayList<String> listUrls) {
            this.listUrls = listUrls;
            if(listUrls.size() == 4){
                listUrls.remove(listUrls.size()-1);
            }
            inflater = LayoutInflater.from(FeedBackActivity.this);
        }
        @Override
        public int getCount() {
            return listUrls.size();
        }

        @Override
        public Object getItem(int position) {
            return listUrls.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.item_upload, parent,false);
                holder.image = (ImageView) convertView.findViewById(R.id.upload_img);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder)convertView.getTag();
            }
            final String path=listUrls.get(position);
            if (path.equals("000000")){
                holder.image.setImageResource(R.drawable.upload);
            }else {
                Glide.with(FeedBackActivity.this)
                        .load(path)
                        .placeholder(R.drawable.default_error)
                        .error(R.drawable.default_error)
                        .centerCrop()
                        .crossFade()
                        .into(holder.image);
            }
            return convertView;
        }

        class ViewHolder {
            ImageView image;
        }
    }

    /**
     * 意见反馈
     *
     * @param feedbackContent
     * @param imagePaths
     */
    public void postFeedback(final String feedbackContent, final ArrayList<String>imagePaths) {
        Constant.TOKEN= SharedPreferences.getInstance().getString("checktoken","");
        Constant.REGISTRATION=SharedPreferences.getInstance().getString("RegistrationId","");
        System.out.println("================>");
        System.out.println(feedbackContent);
        System.out.println("================>");
        for (int i=0;i<imagePaths.size();i++){
            /**
             * 上传图片
            */
            HttpRequestApi.getInstance().uploadFile(upImage(imagePaths.get(i).toString()), new HttpSubscriber<Upload>(new SubscriberOnListener<Upload>() {
                @Override
                public void onSucceed(Upload upload) {
                    System.out.println("陈功了" + Constant.RESPONSECODE + Constant.ERRMESSAGE);
                    Feedbackfiles feedbackfiles = new Feedbackfiles();
                    final List<Feedbackfiles> feedbackfilesList = new ArrayList<Feedbackfiles>();
                    feedbackfiles.setFileId(upload.getData().getId());
                    feedbackfiles.setContentType(upload.getData().getExtension());
                    feedbackfiles.setFileName(upload.getData().filename);
                    feedbackfiles.setFileUrl(upload.getData().getDownloadUrl());
                    feedbackfilesList.add(feedbackfiles);
                    Feedback feedback = new Feedback();
                    feedback.setMemberId(Constant.MEMBERID);
                    feedback.setContent(feedbackContent);
                    feedback.setFeedBackFilesDto(feedbackfilesList);
                    Gson gson = new Gson();
                    String verifyAuthCodeJson = gson.toJson(feedback);
                    RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), verifyAuthCodeJson);
                    HttpRequestApi.getInstance().postFeedback(requestBody, new HttpSubscriber<Feedback>(new SubscriberOnListener<Feedback>() {
                        @Override
                        public void onSucceed(Feedback demo) {
                            ToastUtils.showShort(FeedBackActivity.this, "提交成功！");
                        }

                        @Override
                        public void onError(int code, String msg) {

                        }
                    }, FeedBackActivity.this));
                }

                @Override
                public void onError(int code, String msg) {
                }
            }, FeedBackActivity.this));
        }
    }

    /**
     * 上传图片的请求封装
     *
     * @param imageUri
     * @return
     */
    public MultipartBody upImage(String imageUri) {
        System.out.println("11111====" + imageUri);
        File identityBackfFile = new File(String.valueOf(imageUri));
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), identityBackfFile);
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.addFormDataPart("image", identityBackfFile.getName(), requestFile);
        builder.addFormDataPart("title", "image");
        builder.addFormDataPart("alt", "image");
        builder.setType(MultipartBody.FORM);
        MultipartBody multipartBody = builder.build();
        return multipartBody;
    }

}
