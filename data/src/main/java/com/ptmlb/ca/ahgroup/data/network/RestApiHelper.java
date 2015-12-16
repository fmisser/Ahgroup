package com.ptmlb.ca.ahgroup.data.network;

import com.ptmlb.ca.ahgroup.data.BuildConfig;
import com.ptmlb.ca.ahgroup.data.entity.LoginInfoEntity;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.RequestBody;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;
import rx.Observable;
import rx.Subscriber;

/**
 * Created by Administrator on 2015/12/16.
 *
 */

@Singleton
public class RestApiHelper {

    private RestApi restApi;

    @Inject
    public RestApiHelper() {

        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setConnectTimeout(BuildConfig.DEFAULT_CONNECTION_TIMEOUT_SEC, TimeUnit.SECONDS);
        okHttpClient.setReadTimeout(BuildConfig.DEFAULT_READ_TIMEOUT_SEC, TimeUnit.SECONDS);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.API_BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        restApi = retrofit.create(RestApi.class);
    }

    public Observable<LoginInfoEntity> getLoginInfo() {
        String requestString = "{\"sign\":\"9FD318ACAABFC74F55792827FE52BB72\",\"action\":\"GetCityList\",\"CityVersion\":\"0\"}\n";
        final RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), requestString);

        final Call<LoginInfoEntity> call = restApi.getLoginInfo(requestBody);
//        Observable<LoginInfoEntity> observable = observableForCall(call);
        //        Observable.timer(5, TimeUnit.SECONDS).map(new Func1<Long, Object>() {
//            @Override
//            public Object call(Long aLong) {
//                call.cancel();
//                return null;
//            }
//        }).subscribe();
//        return observable;

        return observableForCall(call);
    }

    private <T> Observable<T> observableForCall(final  Call<T> call) {
        return Observable.create(new Observable.OnSubscribe<T>() {
            @Override
            public void call(final Subscriber<? super T> subscriber) {
                call.enqueue(new Callback<T>() {
                    @Override
                    public void onResponse(Response<T> response, Retrofit retrofit) {
                        if (response.isSuccess()) {
                            subscriber.onNext(response.body());
                            subscriber.onCompleted();
                        } else {
                            subscriber.onError(new Throwable(response.message()));
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        subscriber.onError(t);
                    }
                });
            }
        });
    }
}
