package com.sinoautodiagnoseos.net.requestApi;

import com.sinoautodiagnoseos.entity.Area.Area;
import com.sinoautodiagnoseos.entity.AuthCode.AuthCode;
import com.sinoautodiagnoseos.entity.CallRecord.CallRecord;
import com.sinoautodiagnoseos.entity.CarBrands.CarBrands;
import com.sinoautodiagnoseos.entity.ExceptionBean.ErrMessage;
import com.sinoautodiagnoseos.entity.Experts.SearchExpertsData;
import com.sinoautodiagnoseos.entity.FaulTranges.FaulTranges;
import com.sinoautodiagnoseos.entity.Feedback.Feedback;
import com.sinoautodiagnoseos.entity.LogBean.LogBean;
import com.sinoautodiagnoseos.entity.Password.Password;
import com.sinoautodiagnoseos.entity.Push.PushResult;
import com.sinoautodiagnoseos.entity.Station.Station;
import com.sinoautodiagnoseos.entity.Station.StationList;
import com.sinoautodiagnoseos.entity.Upload.Upload;
import com.sinoautodiagnoseos.entity.User.Directexpert;
import com.sinoautodiagnoseos.entity.User.ForgetPassword;
import com.sinoautodiagnoseos.entity.User.Skill;
import com.sinoautodiagnoseos.entity.User.Token;
import com.sinoautodiagnoseos.entity.User.User;
import com.sinoautodiagnoseos.entity.User.UserBaseData;
import com.sinoautodiagnoseos.entity.User.UserInfo;
import com.sinoautodiagnoseos.entity.Version.Version;
import com.sinoautodiagnoseos.net.requestService.HttpRequestService;
import com.sinoautodiagnoseos.net.requestUtil.HttpMethod;
import com.sinoautodiagnoseos.openvcall.model.ExpertsInfoList;
import com.sinoautodiagnoseos.openvcall.model.ListExpertsSearchLists;
import com.sinoautodiagnoseos.openvcall.model.ListResult;
import com.sinoautodiagnoseos.openvcall.model.RoomInfo;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Observable;
import rx.Subscriber;

/**
 * Created by HQ_Demos on 2017/4/27.
 */

public class HttpRequestApi extends BaseApi {
    public static HttpRequestApi httpRequestApi;
    public HttpRequestService httpRequestService;

    public HttpRequestApi(){
        httpRequestService= HttpMethod.getInstance().createApi(HttpRequestService.class);
    }

    public static HttpRequestApi getInstance(){
        if (httpRequestApi==null){
            httpRequestApi = new HttpRequestApi();
        }
        return httpRequestApi;
    }

    /**
     * 获取验证码
     *
     * @param body
     * @param subscriber
     */
    public void getAuthCode(RequestBody body, Subscriber<AuthCode> subscriber) {
        Observable observable = httpRequestService.getAuthCode(body)
                .map(new HttpResultFunc<AuthCode>());
        toSubscribe(observable, subscriber);
    }

    /**
     * 获取通话记录
     *
     * @param subscriber
     */
    public void getCallRecord(Subscriber<CallRecord> subscriber) {
        Observable observable = httpRequestService.getCallRecord()
                .map(new HttpResultFunc<CallRecord>());
        toSubscribe(observable, subscriber);
    }

    /**
     * 获取故障范围
     *
     * @param subscriber
     */
    public void getFaulTrangesApi(Subscriber<FaulTranges> subscriber) {
        Observable observable = httpRequestService.getFaulTranges()
                .map(new HttpResultFunc<FaulTranges>());
        toSubscribe(observable, subscriber);
    }

    /**
     * 上传日志
     *
     * @param body
     * @param subscriber
     */
    public void postLog(RequestBody body, Subscriber<LogBean> subscriber) {
        Observable observable = httpRequestService.postLog(body)
                .map(new HttpResultFunc<LogBean>());
        toSubscribe(observable, subscriber);
    }

    /**
     * 登录
     *
     * @param subscriber
     */
    public void getToken(Subscriber<Token> subscriber) {
        Observable observable = httpRequestService.getToken()
                .map(new HttpResultFunc<Token>());
        toSubscribe(observable, subscriber);
    }

    /**
     * 注册
     *
     //     * @param body
     * @param subscriber
     */
//    public void register(RequestBody body, Subscriber<User> subscriber) {
//        Observable observable = httpRequestService.register(body)
//                .map(new HttpResultFunc<User>());
//        toSubscribe(observable, subscriber);
//    }
    public void register(String userName,String passWord, Subscriber<User> subscriber) {
        Observable observable = httpRequestService.register(userName,passWord)
                .map(new HttpResultFunc<User>());
        toSubscribe(observable, subscriber);
    }

    /**
     * 验证码手机验证码
     *
     * @param body
     * @param subscriber
     */
    public void verifyAuthCode(RequestBody body, Subscriber<AuthCode> subscriber) {
        Observable observable = httpRequestService.postValidat(body)
                .map(new HttpResultFunc<AuthCode>());
        toSubscribe(observable, subscriber);
    }

    /**
     * 上传文件
     */
    public void uploadFile(MultipartBody body, Subscriber<Upload> subscriber) {
        Observable observable = httpRequestService.uploadFile(body)
                .map(new HttpResultFunc<Upload>());
        toSubscribe(observable, subscriber);
    }

    /**
     * 修改密码
     *
     * @param body
     * @param subscriber
     */
    public void changePassword(RequestBody body, Subscriber<Password> subscriber) {
        Observable observable = httpRequestService.changePassword(body)
                .map(new HttpResultFunc<Password>());
        toSubscribe(observable, subscriber);
    }


    /**
     * 获取用户信息
     *
     * @param subscriber
     */
    public void getUserInfo(Subscriber<UserInfo> subscriber) {
        Observable observable = httpRequestService.getUserInfo()
                .map(new HttpResultFunc<UserInfo>());
        toSubscribe(observable, subscriber);
    }

    /**
     * 修改用户状态
     *
     * @param subscriber
     */
    public void getUserState(Subscriber<Skill> subscriber) {
        Observable observable = httpRequestService.changeUserState()
                .map(new HttpResultFunc<Skill>());
        toSubscribe(observable, subscriber);
    }

    /**
     * 车辆品牌查询
     *
     * @param subscriber
     */
    public void getCarBrands(Subscriber<CarBrands> subscriber) {
        Observable observable = httpRequestService.getAllCarbrands()
                .map(new HttpResultFunc<CarBrands>());
        toSubscribe(observable, subscriber);
    }

    /**
     * 搜索门店信息
     *
     * @param stationName
     * @param subscriber
     */
    public void getStation(String stationName, Subscriber<Station> subscriber) {
        Observable observable = httpRequestService.getStation(stationName)
                .map(new HttpResultFunc<Station>());
        toSubscribe(observable, subscriber);
    }

    /**
     * 获取所有门店
     *
     * @param subscriber
     */
    public void getAllStations(Subscriber<StationList> subscriber) {
        Observable observable = httpRequestService.getAllStations()
                .map(new HttpResultFunc<StationList>());
        toSubscribe(observable, subscriber);
    }

    /**
     * 忘记密码
     *
     * @param body
     * @param subscriber
     */
    public void forgetPassword(RequestBody body, Subscriber<ForgetPassword> subscriber) {
        Observable observable = httpRequestService.forgetPassword(body)
                .map(new HttpResultFunc<ForgetPassword>());
        toSubscribe(observable, subscriber);
    }

    /**
     * 发送推送
     *
     * @param body
     * @param subscriber
     */
    public void pushMessage(RequestBody body, Subscriber<PushResult> subscriber) {
        System.out.println("推送");
        Observable observable = httpRequestService.pushMessage(body);
        toSubscribe(observable, subscriber);
    }

    /**
     * 版本更新
     *
     * @param subscriber
     */
    public void versionUpgrade(Subscriber<Version> subscriber) {
        Observable observable = httpRequestService.versionUpgrade()
                .map(new HttpResultFunc<Version>());
        toSubscribe(observable, subscriber);
    }

    /**
     * 匹配专家
     *
     * @param subscriber
     */
    public void findoneexperts(RequestBody body, Subscriber<RoomInfo> subscriber) {
        Observable observable = httpRequestService.findoneexperts(body)
                .map(new HttpResultFunc<RoomInfo>());
        toSubscribe(observable, subscriber);
    }

    /**
     * 意见反馈
     *
     * @param body
     * @param subscriber
     */
    public void postFeedback(RequestBody body, Subscriber<Feedback> subscriber) {
        Observable observable = httpRequestService.postFeedback(body)
                .map(new HttpResultFunc<Feedback>());
        toSubscribe(observable, subscriber);
    }

    /**
     * 专家认证
     *
     * @param body
     * @param subscriber
     */
    public void professor(RequestBody body, Subscriber<ErrMessage> subscriber) {
        Observable observable = httpRequestService.professorAuth(body)
                .map(new HttpResultFunc<ErrMessage>());
        toSubscribe(observable, subscriber);
    }

    /**
     * 视频中邀请专家列表接口
     *
     * @param subscriber
     */
    public void findexpertslist(RequestBody body, Subscriber<ExpertsInfoList> subscriber) {
        Observable observable = httpRequestService.findexpertslist(body)
                .map(new HttpResultFunc<ExpertsInfoList>());
        toSubscribe(observable, subscriber);
    }

    /**
     * 评分
     *
     * @param body
     * @param subscriber
     */
    public void updatecallhistoriesresult(RequestBody body, Subscriber<ListResult> subscriber) {
        Observable observable = httpRequestService.updatecallhistoriesresult(body)
                .map(new HttpResultFunc<ListResult>());
        toSubscribe(observable, subscriber);
    }

    /**
     * 技师认证
     *
     * @param body
     * @param subscriber
     */
    public void technicianAuth(RequestBody body, Subscriber<ErrMessage> subscriber) {
        Observable observable = httpRequestService.technicianAuth(body)
                .map(new HttpResultFunc<ErrMessage>());
        toSubscribe(observable, subscriber);
    }

    /**
     * 更新用户基础信息接口
     *
     * @param body
     * @param subscriber
     */
    public void updateUserbaseData(RequestBody body, Subscriber<Skill> subscriber) {
        Observable observable = httpRequestService.updateUserBaseData(body)
                .map(new HttpResultFunc<Skill>());
        toSubscribe(observable, subscriber);
    }

    /**
     * 技师获取工种
     *
     * @param subscriber
     */
    public void getSkill(Subscriber<Skill> subscriber) {
        Observable observable = httpRequestService.getSkill()
                .map(new HttpResultFunc<Skill>());
        toSubscribe(observable, subscriber);
    }

    /**
     * 直通专家R
     *
     * @param expertsId
     * @param subscriber
     */
    public void getDirectexpert(String expertsId,String roomId, Subscriber<SearchExpertsData> subscriber) {
        Observable observable = httpRequestService.getDirectexpert(expertsId,roomId)
                .map(new HttpResultFunc<SearchExpertsData>());
        toSubscribe(observable, subscriber);
    }

    /**
     * 获取专家技能
     *
     * @param subscriber
     */
    public void getExpertsSkills(Subscriber<Directexpert> subscriber) {
        Observable observable = httpRequestService.getExpertsSkills()
                .map(new HttpResultFunc<Directexpert>());
        toSubscribe(observable, subscriber);
    }

    /**
     * 专家应答
     *
     * @param body
     * @param subscriber
     */
    public void reCall(RequestBody body, Subscriber<Directexpert> subscriber) {
        Observable observable = httpRequestService.reCall(body)
                .map(new HttpResultFunc<Directexpert>());
        toSubscribe(observable, subscriber);
    }

    /**
     * 用户上线
     *
     * @param subscriber
     */
    public void onLine(Subscriber<Skill> subscriber) {
        Observable observable = httpRequestService.onLine()
                .map(new HttpResultFunc<Skill>());
        toSubscribe(observable, subscriber);
    }

    /**
     * 上传文件片到服务器
     *
     * @param body
     * @param subscriber
     */
    public void uploadFiles(RequestBody body, Subscriber<String> subscriber) {
        Observable observable = httpRequestService.uploadFiles(body)
                .map(new HttpResultFunc<String>());
        toSubscribe(observable, subscriber);
    }


    /**
     * 保存设备ID
     *
     * @param body
     * @param subscriber
     */
    public void postId(RequestBody body, Subscriber<UserInfo> subscriber) {
        Observable observable = httpRequestService.getPostId(body)
                .map(new HttpResultFunc<UserInfo>());
        toSubscribe(observable, subscriber);
    }

    /**
     * 获取省级地址
     *
     * @param subscriber
     */
    public void getProvinces(Subscriber<Area> subscriber) {
        Observable observable = httpRequestService.getProvinces()
                .map(new HttpResultFunc<Area>());
        toSubscribe(observable, subscriber);
    }

    /**
     * 呼叫者主动挂断记录保存接口
     * @param callHistoryId
     * @param subscriber
     */
    public void getCallStatus(String callHistoryId,Subscriber<Skill> subscriber){
        Observable observable=httpRequestService.getCallStatus(callHistoryId)
                .map(new HttpResultFunc<Skill>());
        toSubscribe(observable,subscriber);
    }

    /**
     * 专家挂断接口接口
     * @param callHistoryId
     * @param subscriber
     */
    public void getExpertsCallStatus(String callHistoryId,Subscriber<Skill> subscriber){
        Observable observable=httpRequestService.getCallStatus(callHistoryId)
                .map(new HttpResultFunc<Skill>());
        toSubscribe(observable,subscriber);
    }

    /**
     * 直通邀请专家
     * @param callHistoryId
     * @param subscriber
     */
    public void expertsRequestExperts(String callHistoryId,Subscriber<ListExpertsSearchLists> subscriber){
        Observable observable = httpRequestService.expertsRequestExperts(callHistoryId)
                .map(new HttpResultFunc<ListExpertsSearchLists>());
        toSubscribe(observable,subscriber);
    }
}
