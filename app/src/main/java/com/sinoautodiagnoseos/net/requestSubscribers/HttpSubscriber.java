package com.sinoautodiagnoseos.net.requestSubscribers;

import android.content.Context;

import com.sinoautodiagnoseos.ui.loadingdialog.view.LoadingDialog;

import rx.Subscriber;

/**
 * Created by HQ_Demos on 2017/4/27.
 */

public class HttpSubscriber<T> extends LoadingDialog implements SubscriberOnListener{


    public HttpSubscriber(Context context) {
        super(context);
        setLoadingText("加载中...");
    }

    @Override
    public Subscriber<T> subscribe() {
        show();
        return new Subscriber<T>() {
            @Override
            public void onCompleted() {
                loadSuccess();
            }

            @Override
            public void onError(Throwable e) {
                loadFailed();
            }

            @Override
            public void onNext(T t) {

            }
        };
    }
}
