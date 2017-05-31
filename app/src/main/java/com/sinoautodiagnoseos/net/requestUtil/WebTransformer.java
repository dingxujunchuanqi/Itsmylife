package com.sinoautodiagnoseos.net.requestUtil;

import android.content.Context;

import com.sinoautodiagnoseos.ui.loadingdialog.view.LoadingDialog;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

/**
 * Created by HQ_Demos on 2017/5/23.
 */

public class WebTransformer<T> implements Observable.Transformer<T, T> {
    Context context;
    boolean hasDialog = true;
    LoadingDialog DialogHelper;

    public WebTransformer(Context context) {
        this.context = context;
    }

    public WebTransformer(Context context, boolean hasDialog) {
        this.context = context;
        this.hasDialog = hasDialog;
    }


    @Override
    public Observable<T> call(Observable<T> tObservable) {
        return tObservable.subscribeOn(Schedulers.io())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        if (hasDialog) {
                            DialogHelper.show();;
//                            DialogHelper.showDialog(context);
                        }
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
