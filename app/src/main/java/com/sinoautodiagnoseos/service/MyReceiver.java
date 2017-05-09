package com.sinoautodiagnoseos.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.sinoautodiagnoseos.R;
import com.sinoautodiagnoseos.app.AppContext;
import com.sinoautodiagnoseos.app.AppManager;
import com.sinoautodiagnoseos.entity.Download.FileDownload;
import com.sinoautodiagnoseos.entity.Push.PushResult;
import com.sinoautodiagnoseos.openvcall.model.CallHistoriesExpertsDtos;
import com.sinoautodiagnoseos.openvcall.model.ConstantApp;
import com.sinoautodiagnoseos.openvcall.model.CurrentUserSettings;
import com.sinoautodiagnoseos.openvcall.ui.ChatActivity;
import com.sinoautodiagnoseos.openvcall.ui.ExpertsActivity;
import com.sinoautodiagnoseos.utils.Constant;
import com.sinoautodiagnoseos.utils.PushUtil;
import com.sinoautodiagnoseos.utils.ToastUtils;
import com.sinoautodiagnoseos.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cn.jpush.android.api.JPushInterface;

import static com.sinoautodiagnoseos.utils.Constant.ISINVITE;

/**
 * Created by HQ_Demos on 2017/3/28.
 */

public class MyReceiver extends BroadcastReceiver {
    private static final String TAG = "JPush";
    private static final String PERMISSION = "PERMISSION";
    private static final String AGREE = "AGREE";
    private static final String ENTER = "ENTER";
    private static final String FILEDOWNLOAD = "FILEDOWNLOAD";
    private static final String INVITE="INVITE";
    private static final String REFUSE="REFUSE";
    private static final String CLOSE="CLOSE";

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        Log.d(TAG, "[MyReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));

        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
            Log.d(TAG, "[MyReceiver] 接收Registration Id : " + regId);
            //send the Registration Id to your server...
            if (regId!=null||!regId.equals("")){
                /**
                 * 将生产的唯一识别码保存起来
                 */
                com.sinoautodiagnoseos.utils.SharedPreferences.getInstance().putString("RegistrationId",regId);
                Constant.REGISTRATION= com.sinoautodiagnoseos.utils.SharedPreferences.getInstance()
                        .getString("RegistrationId","");
//                saveRegistion(regId,context);
            }
            System.out.println("[MyReceiver] 接收Registration Id : " + regId);
        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
            String extras = bundle.getString(JPushInterface.EXTRA_MESSAGE);
            PushResult permissionPush = new PushResult();
            System.out.println("extras=" + extras);
            String type = "", sName = "";
            try {
                MessageType messageType = new MessageType(extras);
                boolean has_type = messageType.getExtension().containsKey("type");
                if (has_type) {
                    type = String.valueOf(messageType.getExtension().get("type"));
                    switch (type) {
                        case PERMISSION:
                            ISINVITE = "false";
                            Constant.ROOMID = messageType.getData().get("RoomID").toString();
                            Constant.SACCOUNT = messageType.getData().get("SAccount").toString();
                            Intent to_experts_intent = new Intent();
                            to_experts_intent.setClass(context, ExpertsActivity.class);
                            to_experts_intent.putExtra("From_Activity", "Waiting_To_Experts");
                            to_experts_intent.putExtra("PAccount", messageType.getMessage().get("Account").toString());//专家手机号
                            to_experts_intent.putExtra("SAccount", messageType.getData().get("SAccount").toString());//技师手机号
                            to_experts_intent.putExtra("SName", messageType.getData().get("SName").toString());//技师姓名
                            to_experts_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(to_experts_intent);
                            break;
                        case AGREE:
                            Gson gson = new Gson();
                            String str=gson.toJson(messageType.getData());
                            Log.e("Agree----------",str+"\n"+"RoomId------"+ Constant.ROOMID);
                            String PAccount = messageType.getData().get("PAccount").toString();
                            if (Utils.isMobileNO(PAccount)){
                                System.out.println("-------已在电脑端登录");
                            }else {
                                Constant.is_enter.remove(Constant.ROOMID+PAccount);
                                PushUtil.RoomIdPush(context, messageType.getData().get("PAccount").toString(), Constant.ROOMID);
                            }
                            Constant.SACCOUNT = Constant.USERNAME;
                            String P_Name=messageType.getData().get("PName").toString();
                            String P_Account = messageType.getData().get("PAccount").toString();
                            Map<String,List<CallHistoriesExpertsDtos>> map= Constant.save_experts;
                            if (map.get(Constant.ROOMID)==null){

                            }
                            List<CallHistoriesExpertsDtos> expertsDtosList =map.get(Constant.ROOMID);
                            CallHistoriesExpertsDtos expertsDtos=new CallHistoriesExpertsDtos();
                            expertsDtos.setExpertNum(P_Account);
                            expertsDtos.setCallhistoryId(Constant.ROOMID);
                            expertsDtos.setName(P_Name);
                            if(expertsDtosList==null){
                                expertsDtosList=new ArrayList<>();
                            }
                            expertsDtosList.add(expertsDtos);
                            map.put(Constant.ROOMID,expertsDtosList);
                            System.out.println("AGREE=" + Constant.ROOMID);
                            System.out.println("收到了");
                            vSettings().mChannelName = Constant.ROOMID;
                            vSettings().mEncryptionKey = "";
                            Boolean is_create= Constant.is_create.get(Constant.ROOMID);
                            if (is_create==null){
                                Intent experts_agree = new Intent(context, ChatActivity.class);
                                experts_agree.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                experts_agree.putExtra("From_Activity", "Experts_To_Waiting");
                                experts_agree.putExtra(ConstantApp.ACTION_KEY_CHANNEL_NAME, Constant.ROOMID);
                                experts_agree.putExtra(ConstantApp.ACTION_KEY_ENCRYPTION_KEY, "");
                                experts_agree.putExtra(ConstantApp.ACTION_KEY_ENCRYPTION_MODE, context.getResources().
                                        getStringArray(R.array.encryption_mode_values)[vSettings().mEncryptionModeIndex]);
                                experts_agree.putExtra("P_Name",P_Name);
                                experts_agree.putExtra("expertNum",P_Account);
                                experts_agree.putExtra("AGREE",AGREE);
                                context.startActivity(experts_agree);
                                Constant.is_create.put(Constant.ROOMID,true);
                            }

                            break;

                        case ENTER:
                            ISINVITE = "false";
                            System.out.println("你好ENTER=" + messageType.getData().get("RoomID").toString());
                            String Account=messageType.getMessage().get("Account").toString();
                            Log.e("TAG--------",messageType.getMessage().get("Account").toString());
//                               Constant.SACCOUNT = messageType.getData().get("SAccount").toString();

                            vSettings().mChannelName = messageType.getData().get("RoomID").toString();
                            vSettings().mEncryptionKey = "";
                            Boolean is_enter = Constant.is_enter.get(Constant.ROOMID+Account);
                            Log.e("----is_enter-------","~~"+is_enter);
                            if (is_enter==null&&messageType.getData().get("RoomID").toString().equals(Constant.ROOMID)){
                                Log.e("---------------","```````````````");
                                Constant.is_enter.put(Constant.ROOMID+Account,true);
                                Intent experts_enter_intent = new Intent(context, ChatActivity.class);
                                experts_enter_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                experts_enter_intent.putExtra(ConstantApp.ACTION_KEY_CHANNEL_NAME, messageType.getData().get("RoomID").toString());
                                experts_enter_intent.putExtra(ConstantApp.ACTION_KEY_ENCRYPTION_KEY, "");
                                experts_enter_intent.putExtra(ConstantApp.ACTION_KEY_ENCRYPTION_MODE, context.getResources().
                                        getStringArray(R.array.encryption_mode_values)[vSettings().mEncryptionModeIndex]);
                                context.startActivity(experts_enter_intent);
                            }
                            break;
                        case INVITE:
                            ISINVITE = "true";
                            System.out.println("你好INVITE=" + messageType.getData().get("RoomID").toString());
//                               Constant.SACCOUNT = messageType.getData().get("SAccount").toString();
                            vSettings().mChannelName = messageType.getData().get("RoomID").toString();
                            vSettings().mEncryptionKey = "";
                            Intent more_enter_intent = new Intent(context, ChatActivity.class);
                            more_enter_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            more_enter_intent.putExtra(ConstantApp.ACTION_KEY_CHANNEL_NAME, messageType.getData().get("RoomID").toString());
                            more_enter_intent.putExtra(ConstantApp.ACTION_KEY_ENCRYPTION_KEY, "");
                            more_enter_intent.putExtra(ConstantApp.ACTION_KEY_ENCRYPTION_MODE, context.getResources().
                                    getStringArray(R.array.encryption_mode_values)[vSettings().mEncryptionModeIndex]);
                            context.startActivity(more_enter_intent);
                            break;
                        case REFUSE:
//                            System.out.println("被拒绝了=");
                            ToastUtils.showShort(context,"很遗憾，被人家拒绝了！");
                        case CLOSE:
                            Constant.is_close=true;
                            String experts_id=messageType.getMessage().get("Account").toString();
                            String registion_id= Constant.REGISTRATION;
                            if (experts_id.equals(registion_id)){
                                AppManager.getAppManager().finishActivity();
                            }else {

                            }
                            Log.e("---PushType---",experts_id+"-----CLOSE-----"+registion_id);
                            break;
                        case FILEDOWNLOAD:
                            Constant.is_push=true;
//                            Map<String,List<FileDownload>> file_map = Constant.file_list;
//                            List<FileDownload> fileList=file_map.get(Constant.ROOMID);
                            List<FileDownload> fileList= Constant.file_list;
                            FileDownload fileDownload = new FileDownload();
                            String fileName=messageType.getData().get("FileTitle").toString();
                            String fileUrl=messageType.getData().get("FileUrl").toString();
//                            Constant.FILENAME=fileName;
                            Constant.DOWNLOADURL=fileUrl;
                            fileDownload.setFileName(fileName);
                            fileDownload.setFileUrl(fileUrl);
                            if (fileList==null){
                                fileList=new ArrayList<>();
                            }
                            fileList.add(fileDownload);
//                            file_map.put(Constant.ROOMID,fileList);
                            System.out.println("---------"+fileList.size());
                            break;
                        default:
                            break;
                    }
                }
//                boolean has_sName=messageType.getData().containsKey("SName");
//                if (has_sName){
//                    sName=messageType.getData().get("SName").toString();
//                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            System.out.println("permissionPush=" + permissionPush);
            System.out.println("推送类型" + type);
            System.out.println("SName=" + sName);


        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 接收到推送下来的通知");
            int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
            Log.d(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);

        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 用户点击打开了通知");

            //打开自定义的Activity
//            Intent i = new Intent(context, TestActivity.class);
//            i.putExtras(bundle);
//            //i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
//            context.startActivity(i);

        } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
            //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..

        } else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
            boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
            Log.w(TAG, "[MyReceiver]" + intent.getAction() + " connected state change to " + connected);
        } else {
            Log.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
        }
    }

    // 打印所有的 intent extra 数据
    private static String printBundle(Bundle bundle) {
        StringBuilder sb = new StringBuilder();
        for (String key : bundle.keySet()) {
            if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
            } else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
            } else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
                if (TextUtils.isEmpty(bundle.getString(JPushInterface.EXTRA_EXTRA))) {
                    Log.i(TAG, "This message has no Extra data");
                    continue;
                }

                try {
                    JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
                    Iterator<String> it = json.keys();

                    while (it.hasNext()) {
                        String myKey = it.next().toString();
                        sb.append("\nkey:" + key + ", value: [" +
                                myKey + " - " + json.optString(myKey) + "]");
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "Get message extra JSON error!");
                }

            } else {
                sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
            }
        }
        return sb.toString();
    }

    protected static CurrentUserSettings vSettings() {
        return AppContext.getInstance().mVideoSettings;
    }


    //send msg to MainActivity
//    private void processCustomMessage(Context context, Bundle bundle) {
//        if (MainActivity.isForeground) {
//            String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
//            String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
//            Intent msgIntent = new Intent(MainActivity.MESSAGE_RECEIVED_ACTION);
//            msgIntent.putExtra(MainActivity.KEY_MESSAGE, message);
//            if (!ExampleUtil.isEmpty(extras)) {
//                try {
//                    JSONObject extraJson = new JSONObject(extras);
//                    if (extraJson.length() > 0) {
//                        msgIntent.putExtra(MainActivity.KEY_EXTRAS, extras);
//                    }
//                } catch (JSONException e) {
//
//                }
//
//            }
//            context.sendBroadcast(msgIntent);
//        }
//    }
}
