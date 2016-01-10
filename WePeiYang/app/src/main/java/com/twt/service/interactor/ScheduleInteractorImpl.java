package com.twt.service.interactor;

import com.twt.service.api.ApiClient;
import com.twt.service.bean.ClassTable;
import com.twt.service.bean.RefreshedToken;
import com.twt.service.ui.schedule.OnGetScheduleCallback;
import com.twt.service.ui.schedule.OnRefreshTokenCallback;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by sunjuntao on 15/12/5.
 */
public class ScheduleInteractorImpl implements ScheduleInteractor {
    @Override
    public void getSchedule(String authorization, final OnGetScheduleCallback onGetScheduleCallback) {
        ApiClient.getClassTable(authorization, new Callback<ClassTable>() {
            @Override
            public void success(ClassTable classTable, Response response) {
                onGetScheduleCallback.onSuccess(classTable);
            }

            @Override
            public void failure(RetrofitError error) {
                onGetScheduleCallback.onFailure(error);
            }
        });
    }

    @Override
    public void refreshToken(String authorization, final OnRefreshTokenCallback onRefreshTokenCallback) {
        ApiClient.refreshToken(authorization, new Callback<RefreshedToken>() {
            @Override
            public void success(RefreshedToken refreshedToken, Response response) {
                onRefreshTokenCallback.onSuccess(refreshedToken);
            }

            @Override
            public void failure(RetrofitError error) {
                onRefreshTokenCallback.onFailure();
            }
        });
    }
}