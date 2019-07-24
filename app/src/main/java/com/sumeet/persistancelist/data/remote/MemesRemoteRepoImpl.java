package com.sumeet.persistancelist.data.remote;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class MemesRemoteRepoImpl extends RetrofitHelper implements MemesRemoteRepo {

    @Override
    public Observable<MemesResponseDto> getAllMemes() {
        return create(ApiService.class, RemoteConfiguration.BASE_URL)
                .getMemesFromServer().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext((response) -> {
                    if (!response.isSuccessful() ||
                            response.body() == null ||
                            !response.body().getSuccess()) {
                        throw new NetworkError();
                    }
                }).onErrorResumeNext(
                        (Function<Throwable, ObservableSource<? extends Response<MemesResponseDto>>>) Observable::error)
                .map(Response::body);
    }
}