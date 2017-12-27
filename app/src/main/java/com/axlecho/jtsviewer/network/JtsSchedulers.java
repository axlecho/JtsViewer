package com.axlecho.jtsviewer.network;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class JtsSchedulers {
    public <T> Observable<T> switchSchedulers(Observable<T> a) {
        return a.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }
}
