package com.sinoautodiagnoseos.openvcall.ui;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.sinoautodiagnoseos.R;
import com.sinoautodiagnoseos.entity.CallRecord.ReCall;
import com.sinoautodiagnoseos.entity.User.Directexpert;
import com.sinoautodiagnoseos.net.requestApi.HttpRequestApi;
import com.sinoautodiagnoseos.net.requestSubscribers.HttpSubscriber;
import com.sinoautodiagnoseos.net.requestSubscribers.SubscriberOnListener;
import com.sinoautodiagnoseos.utils.Constant;
import com.sinoautodiagnoseos.utils.SharedPreferences;

import java.text.SimpleDateFormat;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.RequestBody;

/**
 * Created by HQ_Demos on 2017/2/23.
 */
public class ExpertsActivity extends Activity {

    private ImageView hand_up_btn , answer_btn;
    private TextView experts_name,text_chage;
    private CircleImageView experts_avatar;
    public String PAccount;
    public String SAccount;
    public String SName;
    private String which_activity;
    private MediaPlayer mp=null;//mediaPlayer对象
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_experts);
//        play();
        initData();
        initView();
    }



    private void initData(){
        System.out.println("专家手机="+PAccount+"技师手机="+SAccount);
        PAccount = getIntent().getStringExtra("PAccount");
        SAccount = getIntent().getStringExtra("SAccount");
        SName = getIntent().getStringExtra("SName");
        which_activity=getIntent().getStringExtra("From_Activity");
    }

    private void play() {
        try{
            mp = MediaPlayer.create(ExpertsActivity.this, R.raw.audio);
            mp.setLooping(true);
            mp.start();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void initView() {
        experts_avatar= (CircleImageView) findViewById(R.id.request_avatar);
        experts_name= (TextView) findViewById(R.id.request_name);
        text_chage= (TextView) findViewById(R.id.text_change);
        hand_up_btn= (ImageView) findViewById(R.id.hand_up_btn);
        answer_btn= (ImageView) findViewById(R.id.answer_btn);
        if (which_activity.equals("Waiting_To_Experts")){
            experts_name.setText(SName);
            text_chage.setText(getResources().getString(R.string.str_zhenduan));
       }else {
            experts_name.setText("专家");
            text_chage.setText(getResources().getString(R.string.str_agree_zhenduan));
        }
        hand_up_btn.setOnClickListener(listener);
        answer_btn.setOnClickListener(listener);
    }

    View.OnClickListener listener=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
//            stop();
            Constant.TOKEN= SharedPreferences.getInstance().getString("checktoken","");
            Constant.REGISTRATION=SharedPreferences.getInstance().getString("RegistrationId","");
            SimpleDateFormat formatter   =   new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
            Date curDate =  new Date(System.currentTimeMillis());
            String str   =   formatter.format(curDate);
            ReCall reCall = new ReCall();
            Gson gson = new Gson();
            String reCallJson;
            RequestBody requestBody;
            switch (view.getId()){
                case R.id.hand_up_btn:
                    reCall.setCallhistoryId(Constant.ROOMID);
                    reCall.setCallSource(1);
                    reCall.setCallStatus(2);//1-同意 2-拒绝
                    reCall.setEndOnUtc(str);
//                    reCall.setExpertNum(Constant.USERNAME);
//                    reCall.setInvited(false);
//                    reCall.setMemberId(Constant.MEMBERID);
                    reCallJson = gson.toJson(reCall);
                    requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), reCallJson);
                    HttpRequestApi.getInstance().reCall(requestBody, new HttpSubscriber<Directexpert>(new SubscriberOnListener<Directexpert>() {
                        @Override
                        public void onSucceed(Directexpert demo) {
                        }

                        @Override
                        public void onError(int code, String msg) {
                        }
                    }, ExpertsActivity.this));
                    finish();
                    break;
                case R.id.answer_btn:
                  //  System.out.println("是否被邀请"+Constant.ISINVITE);
//                    if (Constant.ISINVITE.equals("true")){
//                        vSettings().mChannelName = Constant.ROOMID;
//                        vSettings().mEncryptionKey = "";
//                        Intent i = new Intent(ExpertsActivity.this, ChatActivity.class);
//                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        i.putExtra(ConstantApp.ACTION_KEY_CHANNEL_NAME, Constant.ROOMID);
//                        i.putExtra(ConstantApp.ACTION_KEY_ENCRYPTION_KEY, "");
//                        i.putExtra(ConstantApp.ACTION_KEY_ENCRYPTION_MODE, ExpertsActivity.this.getResources().
//                                getStringArray(R.array.encryption_mode_values)[vSettings().mEncryptionModeIndex]);
//                        ExpertsActivity.this.startActivity(i);
//                    }else if(Constant.ISINVITE.equals("false")){
                        Intent wait_intent = new Intent(ExpertsActivity.this,WaitingActivity.class);
                        wait_intent.putExtra("from_where","1");
                        startActivity(wait_intent);

//                        System.out.println("技师账号"+Constant.USERNAME+"专家账号"+SAccount);
//                        PushUtil.AgreePush(ExpertsActivity.this, Constant.USERNAME,SAccount,"专家");
                        finish();
                  //  }
                    reCall.setBeginOnUtc(str);
                    reCall.setCallhistoryId(Constant.ROOMID);
                    reCall.setCallSource(1);
                    reCall.setCallStatus(1);//1-同意 2-拒绝
//                    reCall.setExpertNum(Constant.USERNAME);
//                    reCall.setInvited(false);
//                    reCall.setMemberId(Constant.MEMBERID);
                    reCallJson = gson.toJson(reCall);
                    System.out.println("reCallJson="+reCallJson);
                    requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), reCallJson);
                    HttpRequestApi.getInstance().reCall(requestBody, new HttpSubscriber<Directexpert>(new SubscriberOnListener<Directexpert>() {
                        @Override
                        public void onSucceed(Directexpert demo) {
                        }

                        @Override
                        public void onError(int code, String msg) {
                        }
                    }, ExpertsActivity.this));

                    break;
            }
        }
    };

//    protected static CurrentUserSettings vSettings() {
//        return AppContext.getInstance().mVideoSettings;
//    }

    public void stop(){
        if (mp!=null){
            try {
                mp.stop();
            }catch (IllegalStateException e){
                mp=null;
                mp=new MediaPlayer();
                mp.stop();
            }
            mp.release();
            mp=null;
        }
    }

    @Override
    protected void onDestroy() {
//        mp.stop();
//        mp.release();
        super.onDestroy();
    }

    @Override
    protected void onStop() {
//        mp.stop();
//        mp.release();
        super.onStop();
    }
}
