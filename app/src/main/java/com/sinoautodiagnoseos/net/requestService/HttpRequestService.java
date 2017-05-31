package com.sinoautodiagnoseos.net.requestService;

import com.sinoautodiagnoseos.entity.Area.Area;
import com.sinoautodiagnoseos.entity.AuthCode.AuthCode;
import com.sinoautodiagnoseos.entity.Brand.Brand;
import com.sinoautodiagnoseos.entity.CallRecord.CallRecord;
import com.sinoautodiagnoseos.entity.CarBrands.CarBrands;
import com.sinoautodiagnoseos.entity.CarBrands.ZKCarBrands;
import com.sinoautodiagnoseos.entity.CaseDetail.CaseDetail;
import com.sinoautodiagnoseos.entity.ExceptionBean.ErrMessage;
import com.sinoautodiagnoseos.entity.Experts.SearchExpertsData;
import com.sinoautodiagnoseos.entity.FaulTranges.FaulTranges;
import com.sinoautodiagnoseos.entity.Fault.Fault;
import com.sinoautodiagnoseos.entity.Feedback.Feedback;
import com.sinoautodiagnoseos.entity.LogBean.LogBean;
import com.sinoautodiagnoseos.entity.Password.Password;
import com.sinoautodiagnoseos.entity.Push.PushResult;
import com.sinoautodiagnoseos.entity.Station.Station;
import com.sinoautodiagnoseos.entity.Station.StationList;
import com.sinoautodiagnoseos.entity.ThinkTank.ThinkTank;
import com.sinoautodiagnoseos.entity.Upload.Upload;
import com.sinoautodiagnoseos.entity.User.Directexpert;
import com.sinoautodiagnoseos.entity.User.ForgetPassword;
import com.sinoautodiagnoseos.entity.User.Skill;
import com.sinoautodiagnoseos.entity.User.Token;
import com.sinoautodiagnoseos.entity.User.User;
import com.sinoautodiagnoseos.entity.User.UserBaseData;
import com.sinoautodiagnoseos.entity.User.UserInfo;
import com.sinoautodiagnoseos.entity.Version.Version;
import com.sinoautodiagnoseos.openvcall.model.ExpertsInfoList;
import com.sinoautodiagnoseos.openvcall.model.ListExpertsSearchLists;
import com.sinoautodiagnoseos.openvcall.model.ListResult;
import com.sinoautodiagnoseos.openvcall.model.RoomInfo;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;
import retrofit2.http.Streaming;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by HQ_Demos on 2017/4/27.
 */

public interface HttpRequestService {
    /**
     * 登录
     * 获取用户Token信息
     *
     * @return
     */
    @GET("validate/token")
    Observable<Token> getToken();

    /**
     * 注册接口
     */
    @GET("diag/user/registration")
    Observable<User> register(@Query("userName") String userName, @Query("password") String password);

    /**
     * 获取短信验证码
     *
     * @param body
     * @return
     */
    @POST("message/code")
    Observable<AuthCode> getAuthCode(@Body RequestBody body);

    /**
     * 获取呼叫记录
     *
     * @return
     */
    @GET("diag/callhistories/getcallhistory")
    Observable<CallRecord> getCallRecord();

    /**
     * 故障范围接口
     *
     * @return
     */
    @GET("diag/faultranges/findfaultranges")
    Observable<FaulTranges> getFaulTranges();

    /**
     * 日志上传
     *
     * @param log
     * @return
     */
    @POST("logger/Log")
    Observable<LogBean> postLog(@Body RequestBody log);

    /**
     * 验证验证码
     *
     * @param code
     * @return
     */
    @POST("message/code/Validate")
    Observable<AuthCode> postValidat(@Body RequestBody code);

    /**
     * 上传文件
     *
     * @return
     */

    @POST("media/download/")
    Observable<Upload> uploadFile(@Body MultipartBody body);

    /**
     * 文件下载
     *
     * @param url
     * @return
     */
    @Streaming
    @GET
    Observable<ResponseBody> download(@Url String url);

    /**
     * 修改密码
     *
     * @param json
     * @return
     */
    @PUT("identity/Users/ChangePassword")
    Observable<Password> changePassword(@Body RequestBody json);

    /**
     * 获取用户信息
     *
     * @return
     */
    @GET("diag/members/getmemberinfo")
    Observable<UserInfo> getUserInfo();

    /**
     * 更新在线状态
     *
     * @return
     */
    @POST("diag/members/updateonlinestatu/3")
    Observable<Skill> changeUserState();

    /**
     * 用户上线
     *
     * @return
     */
    @POST("diag/members/updateonlinestatu/1")
    Observable<Skill> onLine();

    /**
     * 获取车辆品牌
     *
     * @return
     */
    @GET("diag/carbrands/findallcarbrands")
    Observable<CarBrands> getAllCarbrands();

    /**
     * 搜索门店
     *
     * @param name
     * @return
     */
    @GET("diag/stations/findstation")
    Observable<Station> getStation(@Query("name") String name);

    /**
     * 获取所有门店列表
     *
     * @return
     */
    @GET("diag/stations/findallstations")
    Observable<StationList> getAllStations();

    /**
     * 忘记密码
     *
     * @param json
     * @return
     */
    @PUT("identity/Users/ForgetPassword")
    Observable<ForgetPassword> forgetPassword(@Body RequestBody json);

    /**
     * 推送接口
     *
     * @param json
     * @return
     */
//    @POST("message/senders/app")
    @POST("diag/pushmessage/pushmessage")
    Observable<PushResult> pushMessage(@Body RequestBody json);

    /**
     * 版本更新
     *
     * @return
     */
    @GET("platform/versions/81b26a8f-ebc9-4814-966a-0e6f7925ec63")
    Observable<Version> versionUpgrade();

    /**
     * 匹配专家接口
     *
     * @return
     */
    @POST("diag/experts/getoneexperts")
    Observable<RoomInfo> findoneexperts(@Body RequestBody json);

    /**
     * 意见反馈
     *
     * @param json
     * @return
     */
    @POST("diag/feedbacks/addfeedbacks")
    Observable<Feedback> postFeedback(@Body RequestBody json);

    /**
     * 专家认证
     *
     * @param json
     * @return
     */
    @POST("diag/technicianCredentials/saveexpertsciancredentials")
    Observable<ErrMessage> professorAuth(@Body RequestBody json);

    /**
     * 视频中邀请专家列表接口
     *
     * @return
     */
    @POST("diag/experts/findexpertlist")
    Observable<ExpertsInfoList> findexpertslist(@Body RequestBody json);

    /**
     * 退出房间，评分接口
     *
     * @param body
     * @return
     */
    @POST("diag/callhistoryfiles/updatecallhistoriesresult")
    Observable<ListResult> updatecallhistoriesresult(@Body RequestBody body);

    /**
     * 技师认证
     *
     * @param json
     * @return
     */
    @POST("diag/technicianCredentials/savetechniciancredentials")
    Observable<ErrMessage> technicianAuth(@Body RequestBody json);

    /**
     * 修改用户基本信息
     *
     * @param json
     * @return
     */
    @POST("diag/members/updatemember")
    Observable<UserBaseData> updateUserBaseData(@Body RequestBody json);

    /**
     * 获取工种
     *
     * @return
     */
    @GET("diag/technicians/getskills")
    Observable<Skill> getSkill();

    /**
     * 直通专家
     *
     * @param expertsId
     * @return
     */
    @POST("diag/experts/straightexperts/")
    Observable<SearchExpertsData> getDirectexpert(@Query("expertsId") String expertsId, @Query("roomId")String roomId);

    /**
     * 获取专家技能
     *
     * @return
     */
    @GET("diag/members/findexpertskills")
    Observable<Directexpert> getExpertsSkills();

    /**
     * 专家应答
     *
     * @param json
     * @return
     */
    @POST("diag/experts/reply")
    Observable<Directexpert> reCall(@Body RequestBody json);

    /**
     * 上传图片到服务器
     */
    @POST("diag/callhistoryfiles/savecallhistoriesfile")
    Observable<String> uploadFiles(@Body RequestBody json);

    /**
     * 保存设备ID
     *
     * @param body
     * @return
     */
    @POST("diag/members/login")
    Observable<UserInfo> getPostId(@Body RequestBody body);

    /**
     * 获取省级地址
     * @return
     */
    @GET("location/Areas/provinces")
    Observable<Area> getProvinces();

    /**
     * 呼叫者主动挂断记录保存接口
     * @param callHistoryId
     * @return
     */
    @POST("diag/callhistoryfiles/updatecallhistories")
    Observable<Skill> getCallStatus(@Query("callHistoryId") String callHistoryId);

    /**
     * 专家挂断接口接口
     * @param callHistoryId
     * @return
     */
    @POST("diag/callhistoryfiles/updatecallhistoryCallStatus")
    Observable<Skill> getExpertsCallStatus(@Query("callHistoryId")String callHistoryId);

    /**
     * 专家邀请专家
     * @param callHistoryId
     * @return
     */
    @GET("/diag/callHistoryFaults/findcallhistoryfaultlist")
    Observable<ListExpertsSearchLists> expertsRequestExperts(@Query("callHistoryId")String callHistoryId);

    /**
     * 智库 1-维修案例  2-技术通报 3-汽车资料
     * @param caseCategory
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @GET("/thinkcaselist")
    Observable<ThinkTank>getThinkTank(@Query("caseCategory")int caseCategory//案例类型
                                      ,@Query("caseType")String caseType//汽车资料-子类型
                                      ,@Query("keyword") String keyword//关键字
                                      ,@Query("brandId")String brandId//品牌ID
                                      ,@Query("faultId")String faultId//系统ID
                                      ,@Query("carYear")String carYear//年限
                                      ,@Query("pageIndex")int pageIndex
                                      ,@Query("pageSize")int pageSize);

    /**
     * 详情页
     * @param caseId
     * @return
     */
    @GET("/findcasedetail")
    Observable<CaseDetail> findcasedetail(@Query("caseId") int caseId);

    /**
     * 品牌 查询所有
     * @return
     */
    @GET("/findallbrand")
    Observable<ZKCarBrands> findbrandpage();

    /**
     * 品牌 分页查询
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @GET("/findbrandpage")
    Observable<Brand> findbrandpage(@Query("pageIndex")int pageIndex
                                    ,@Query("pageSize")int pageSize);


    @GET("/findallfault")
    Observable<Fault> findallfault();
}
