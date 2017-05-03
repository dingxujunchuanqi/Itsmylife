package com.sinoautodiagnoseos.net.requestSubscribers;

/**
 * Created by HQ_Demos on 2017/4/27.
 */

public interface SubscriberOnListener<T> {
    void onSucceed(T data);
    void onError(int code, String msg);
}
