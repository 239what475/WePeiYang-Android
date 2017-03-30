package com.twtstudio.retrox.tjulibrary.provider;

import android.content.Context;

import com.orhanobut.logger.Logger;
import com.twt.wepeiyang.commons.network.ApiException;
import com.twt.wepeiyang.commons.network.ApiResponse;
import com.twt.wepeiyang.commons.network.RetrofitProvider;
import com.twt.wepeiyang.commons.network.RxErrorHandler;

import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by retrox on 2017/2/21.
 */

public class TjuLibProvider {

    private LibApi libApi;

    private Context mContext;

    public TjuLibProvider(Context context) {
        libApi = RetrofitProvider.getRetrofit().create(LibApi.class);
        mContext = context;
    }

    /**
     * bind tju lib
     *
     * @param action1   -1:ok others:to Api doc
     * @param libpasswd
     */
    public void bindLibrary(Action1<Integer> action1, String libpasswd) {

        libApi.bindLib(libpasswd).map(ApiResponse::getData)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> action1.call(-1), new RxErrorHandler(mContext));

    }

    public void getUserInfo(Action1<Info> action1) {

        libApi.getLibUserInfo().map(ApiResponse::getData)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(action1, new RxErrorHandler(mContext));

    }

    public void getUserInfo(Action1<Info> action1, Action1<Throwable> throwableAction1) {

        libApi.getLibUserInfo().map(ApiResponse::getData)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(action1, throwable -> {
                    throwableAction1.call(throwable);
                    new RxErrorHandler(mContext).call(throwable);
                });

    }

    public void getUserHistory(Action1<List<Histroy>> action1) {
        libApi.getLibUserHistroy().map(ApiResponse::getData)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(action1, new RxErrorHandler(mContext));
    }

    public void renewAllBooks(Action1<List<RenewResult>> action1) {
        renewBooks(action1, "all");
    }

    public void renewBooks(Action1<List<RenewResult>> action1, String barcode) {

        libApi.renewBooks(barcode).map(ApiResponse::getData)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(action1, new RxErrorHandler(mContext));

    }
}
