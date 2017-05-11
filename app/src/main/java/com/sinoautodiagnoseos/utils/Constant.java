package com.sinoautodiagnoseos.utils;

import com.sinoautodiagnoseos.entity.Download.FileDownload;
import com.sinoautodiagnoseos.openvcall.model.CallHistoriesExpertsDtos;
import com.sinoautodiagnoseos.openvcall.model.ListExpertsSearchDto;

import java.util.ArrayList;
import android.widget.ImageView;

import java.io.File;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Lanye on 2017/2/22.
 */

public class Constant {
    public static String ERRMESSAGE;
    public static int RESPONSECODE;
    public static String TOKEN;
    public static String BASEURL;
    public static String SUCCEED;
    public static String USERROLE;
    public static String ROOMID;//房间号
    public static String MEMBERID;
    public static String SACCOUNT;//求助方的电话
    public static String DOWNLOADURL;
    public static String FILENAME;
    public static ListExpertsSearchDto Experts;//求助专家的实力类
    public static String USERNAME;
    public static String ISINVITE;
    public static String REGISTRATION="";//账号只能一端登录的标识码
    public static String ISFROM;//判断用户操作来源 0--直通  1--匹配
    public static Map<String,Boolean> is_create = new Hashtable<>();//用来判断是否创建房间
    public static Map<String,Boolean> is_enter = new Hashtable<>();//用来判断是否创建房间
    public static boolean is_push =false;//是否已上传好的文件的标识
    public static Map<String,List<CallHistoriesExpertsDtos>>save_experts=new Hashtable<>();
//    public static List<FileDownload> file_list = new ArrayList<>();
    public static List<FileDownload> file_list = new ArrayList<>();
    public static boolean is_close = false;
    public static final int PAGE_SIZE = 10;
    //请求相机
    public static final int REQUEST_CAPTURE = 100;
    //请求相册
    public static final int REQUEST_PICK = 101;
    //请求截图
    public static final int REQUEST_CROP_PHOTO = 102;
    //请求访问外部存储
    public static final int READ_EXTERNAL_STORAGE_REQUEST_CODE = 103;
    //请求写入外部存储
    public static final int WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 104;
    //头像1
    public CircleImageView headImage1;
    //头像2
    public ImageView headImage2;
}
