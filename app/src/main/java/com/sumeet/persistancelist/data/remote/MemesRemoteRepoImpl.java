package com.sumeet.persistancelist.data.remote;

import io.reactivex.Observable;
import retrofit2.Response;

public class MemesRemoteRepoImpl extends RetrofitHelper implements MemesRemoteRepo {

    @Override
    public Observable<Response<MemesResponseDto>> getAllMemes() {
        return create(ApiService.class, RemoteConfiguration.BASE_URL).getMemesFromServer();
    }
}