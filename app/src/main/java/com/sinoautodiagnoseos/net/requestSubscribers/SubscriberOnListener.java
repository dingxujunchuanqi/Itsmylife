package com.sinoautodiagnoseos.net.requestSubscribers;

import rx.Subscriber;

/**
 * Created by HQ_Demos on 2017/4/27.
 */

public interface SubscriberOnListener<T> {
    Subscriber<T> subscribe();
}
