package com.sinoautodiagnoseos.utils;

import android.content.Context;
import android.widget.Toast;

import com.google.gson.Gson;
import com.sinoautodiagnoseos.entity.Push.Dataaa;
import com.sinoautodiagnoseos.entity.Push.Msg;
import com.sinoautodiagnoseos.entity.Push.MyMessage;
import com.sinoautodiagnoseos.entity.Push.PushResult;
import com.sinoautodiagnoseos.entity.Push.PushSend;
import com.sinoautodiagnoseos.net.requestApi.HttpRequestApi;
import com.sinoautodiagnoseos.net.requestSubscribers.HttpSubscriber;
import com.sinoautodiagnoseos.net.requestSubscribers.SubscriberOnListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;

/**
 * Created by Lanye on 2017/3/7.
 * 客户端发送推送接口
 */

public class PushUtil {
    /**
     * 邀请专家推送
     *
     * @param context  上下文
     * @param pAccount 专家账号
     * @param sAccount 请求者账号
     * @param sName    请求者姓名
     * @param roomId   房间号
     */
    public static void PermissionPush(final Context context, String pAccount, String sAccount, String sName, String roomId) {
        Constant.TOKEN=SharedPreferences.getInstance().getString("checktoken","");
        Constant.REGISTRATION=SharedPreferences.getInstance().getString("RegistrationId","");
        System.out.println("邀请专家"+pAccount+sAccount+roomId);
        PushSend pushSend = new PushSend();
        List<Msg> list = new ArrayList<Msg>();
        List<String> strList = new ArrayList<String>();
        MyMessage myMessage = new MyMessage();
        Dataaa dataaa = new Dataaa();
        Msg msg = new Msg();
        msg.setAccount(pAccount);
        msg.setData(strList);
        msg.setClientid("llyj");
        msg.setTemplateId("7b3026db-7f58-4e80-a723-8db4a5134931");
        msg.setExtension(myMessage);
        dataaa.setSAccount(sAccount);
        dataaa.setSName(sName);
        dataaa.setRoomID(roomId);
        myMessage.setType("PERMISSION");
        myMessage.setData(dataaa);
        strList.add("专家许可推送");
        list.add(msg);
        pushSend.setMessages(list);
        Gson gson = new Gson();
        String userJson = gson.toJson(pushSend);
        System.out.println("封装好的json===" + userJson);
        Map<String,Object> map=new HashMap<>();
        map.put("dataContent",userJson);
        map.put("dataFrom","1");
        String permissJson = gson.toJson(map);
        System.out.println("封装好的enterJson===" + permissJson);
        RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), permissJson);
        HttpRequestApi.getInstance().pushMessage(requestBody, new HttpSubscriber<PushResult>(new SubscriberOnListener<PushResult>() {
            @Override
            public void onSucceed(PushResult response) {
                System.out.println("成功" + response.getContent());
                Constant.SUCCEED = "true";
            }

            @Override
            public void onError(int code, String msg) {
                System.out.println("失败");
                Constant.SUCCEED = "flase";
                if (Constant.RESPONSECODE == 400) {
                    Toast.makeText(context, "呼叫专家失败，请稍再试。", Toast.LENGTH_SHORT).show();
                } else if (Constant.RESPONSECODE == 500) {
                    Toast.makeText(context, "服务器异常请稍后再试！", Toast.LENGTH_SHORT).show();
                }
            }
        }, context));
    }

    /**
     * 专家同意推送
     *
     * @param context  上下文
     * @param pAccount 专家账号
     * @param sAccount 请求者账号
     * @param pName    专家姓名
     */
    public static void AgreePush(final Context context, String pAccount, String sAccount, String pName) {
        Constant.TOKEN=SharedPreferences.getInstance().getString("checktoken","");
        Constant.REGISTRATION=SharedPreferences.getInstance().getString("RegistrationId","");
       System.out.println("接受者" + sAccount+"专家账号"+pAccount);
        PushSend pushSend = new PushSend();
        List<Msg> list = new ArrayList<Msg>();
        List<String> strList = new ArrayList<String>();
        MyMessage myMessage = new MyMessage();
        Dataaa dataaa = new Dataaa();
        Msg msg = new Msg();
        msg.setAccount(sAccount);
        msg.setData(strList);
        msg.setClientid("llyj");
        msg.setTemplateId("7b3026db-7f58-4e80-a723-8db4a5134931");
        msg.setExtension(myMessage);
        dataaa.setPAccount(pAccount);
        dataaa.setPName(pName);
        myMessage.setType("AGREE");
        myMessage.setData(dataaa);
        strList.add("专家同意");
        list.add(msg);
        pushSend.setMessages(list);
        Gson gson = new Gson();
        String userJson = gson.toJson(pushSend);
        Map<String,Object> map=new HashMap<>();
        map.put("dataContent",userJson);
        map.put("dataFrom","1");
        String agreeJson = gson.toJson(map);
        System.out.println("封装好的agreeJson===" + agreeJson);
        RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),agreeJson );
        HttpRequestApi.getInstance().pushMessage(requestBody, new HttpSubscriber<PushResult>(new SubscriberOnListener<PushResult>() {
            @Override
            public void onSucceed(PushResult response) {
                System.out.println("成功" + response.getContent());
            }

            @Override
            public void onError(int code, String msg) {
                System.out.println("111111111" + Constant.RESPONSECODE + Constant.ERRMESSAGE);
                if (Constant.RESPONSECODE == 400) {
                    Toast.makeText(context, "通信中断，连接失败！", Toast.LENGTH_SHORT).show();
                } else if (Constant.RESPONSECODE == 500) {
                    Toast.makeText(context, "服务器异常请稍后再试！", Toast.LENGTH_SHORT).show();
                }
            }
        }, context));
    }

    /**
     * 技师发送房间号推送
     *
     * @param context  上下文
     * @param pAccount 专家账号
     * @param roomId   房间ID
     */
    public static void RoomIdPush(final Context context, String pAccount, String roomId) {
        Constant.TOKEN=SharedPreferences.getInstance().getString("checktoken","");
        Constant.REGISTRATION=SharedPreferences.getInstance().getString("RegistrationId","");
        System.out.println("专家"+pAccount+"房间号"+roomId);
        PushSend pushSend = new PushSend();
        List<Msg> list = new ArrayList<Msg>();
        List<String> strList = new ArrayList<String>();
        MyMessage myMessage = new MyMessage();
        Dataaa dataaa = new Dataaa();
        Msg msg = new Msg();
        msg.setAccount(pAccount);
        msg.setData(strList);
        msg.setClientid("llyj");
        msg.setTemplateId("7b3026db-7f58-4e80-a723-8db4a5134931");
        msg.setExtension(myMessage);
        dataaa.setRoomID(roomId);
        myMessage.setType("ENTER");
        myMessage.setData(dataaa);
        strList.add("房间号");
        list.add(msg);
        pushSend.setMessages(list);
        Gson gson = new Gson();
        String userJson = gson.toJson(pushSend);
        System.out.println("封装好的enterJson===" + userJson);
        Map<String,Object> map=new HashMap<>();
        map.put("dataContent",userJson);
        map.put("dataFrom","1");
        String enterJson = gson.toJson(map);
        System.out.println("封装好的enterJson===" + enterJson);
        RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), enterJson);
        HttpRequestApi.getInstance().pushMessage(requestBody, new HttpSubscriber<PushResult>(new SubscriberOnListener<PushResult>() {
            @Override
            public void onSucceed(PushResult response) {
                System.out.println("成功" + response.getContent());
            }

            @Override
            public void onError(int code, String msg) {
                System.out.println("失败");
                if (Constant.RESPONSECODE == 400) {
                    Toast.makeText(context, "通信中断，连接失败！", Toast.LENGTH_SHORT).show();
                } else if (Constant.RESPONSECODE == 500) {
                    Toast.makeText(context, "服务器异常请稍后再试！", Toast.LENGTH_SHORT).show();
                }
            }
        }, context));
    }

    /**
     * 下载推送
     *
     * @param context
     * @param pAccount
     * @param fileUrl
     * @param fileTitle
     */
    public static void DownloadPush(final Context context, String pAccount, String fileUrl, String fileTitle) {
        Constant.BASEURL = "http://api.sinoauto.com/";
        PushSend pushSend = new PushSend();
        List<Msg> list = new ArrayList<Msg>();
        List<String> strList = new ArrayList<String>();
        MyMessage myMessage = new MyMessage();
        Dataaa dataaa = new Dataaa();
        Msg msg = new Msg();
        msg.setAccount(pAccount);
        msg.setData(strList);
        msg.setClientid("llyj");
        msg.setTemplateId("7b3026db-7f58-4e80-a723-8db4a5134931");
        msg.setExtension(myMessage);
        dataaa.setFileUrl(fileUrl);
        dataaa.setFileTitle(fileTitle);
        myMessage.setType("FILEDOWNLOAD");
        myMessage.setData(dataaa);
        strList.add("下载");
        list.add(msg);
        pushSend.setMessages(list);
        Gson gson = new Gson();
        String userJson = gson.toJson(pushSend);
        System.out.println("封装好的json===" + userJson);
        Map<String,Object> map=new HashMap<>();
        map.put("dataContent",userJson);
        map.put("dataFrom","1");
        String download_Json = gson.toJson(map);
        System.out.println("封装好的enterJson===" + download_Json);
        RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), download_Json);
        HttpRequestApi.getInstance().pushMessage(requestBody, new HttpSubscriber<PushResult>(new SubscriberOnListener<PushResult>() {
            @Override
            public void onSucceed(PushResult response) {
                System.out.println("成功" + response.getContent());
            }

            @Override
            public void onError(int code, String msg) {
                System.out.println("失败");
                if (Constant.RESPONSECODE == 400) {
                    Toast.makeText(context, "通信中断，连接失败！", Toast.LENGTH_SHORT).show();
                } else if (Constant.RESPONSECODE == 500) {
                    Toast.makeText(context, "服务器异常请稍后再试！", Toast.LENGTH_SHORT).show();
                }
            }
        }, context));
    }

    /**
     * 邀请其他专家
     * @param context
     * @param pAccount
     * @param sAccount
     * @param sName
     * @param roomId
     */
    public static void InvitePush(final Context context, String pAccount, String sAccount, String sName, String roomId) {
        Constant.TOKEN=SharedPreferences.getInstance().getString("checktoken","");
        Constant.REGISTRATION=SharedPreferences.getInstance().getString("RegistrationId","");
            System.out.println("邀请专家"+pAccount+sAccount+roomId);
            PushSend pushSend = new PushSend();
            List<Msg> list = new ArrayList<Msg>();
            List<String> strList = new ArrayList<String>();
            MyMessage myMessage = new MyMessage();
            Dataaa dataaa = new Dataaa();
            Msg msg = new Msg();
            msg.setAccount(pAccount);
            msg.setData(strList);
            msg.setClientid("llyj");
            msg.setTemplateId("7b3026db-7f58-4e80-a723-8db4a5134931");
            msg.setExtension(myMessage);
            dataaa.setSAccount(sAccount);
            dataaa.setSName(sName);
            dataaa.setRoomID(roomId);
            myMessage.setType("INVITE");
            myMessage.setData(dataaa);
            strList.add("专家许可推送");
            list.add(msg);
            pushSend.setMessages(list);
            Gson gson = new Gson();
            String userJson = gson.toJson(pushSend);
            System.out.println("封装好的request_other_experts_Json===" + userJson);
            Map<String,Object> map=new HashMap<>();
            map.put("dataContent",userJson);
            map.put("dataFrom","1");
            String request_other_experts_Json = gson.toJson(map);
            System.out.println("封装好的enterJson===" + request_other_experts_Json);
            RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), request_other_experts_Json);
            HttpRequestApi.getInstance().pushMessage(requestBody, new HttpSubscriber<PushResult>(new SubscriberOnListener<PushResult>() {
                @Override
                public void onSucceed(PushResult response) {
                    System.out.println("成功" + response.getContent());
                    Constant.SUCCEED = "true";
                }

                @Override
                public void onError(int code, String msg) {
                    System.out.println("失败");
                    Constant.SUCCEED = "flase";
                    if (Constant.RESPONSECODE == 400) {
                        Toast.makeText(context, "呼叫专家失败，请稍再试。", Toast.LENGTH_SHORT).show();
                    } else if (Constant.RESPONSECODE == 500) {
                        Toast.makeText(context, "服务器异常请稍后再试！", Toast.LENGTH_SHORT).show();
                    }
                }
            }, context));
    }

}
