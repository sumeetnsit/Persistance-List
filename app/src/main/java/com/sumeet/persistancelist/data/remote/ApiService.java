package com.sumeet.persistancelist.data.remote;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.GET;

public interface ApiService {

    @GET("get_memes")
    Observable<Response<MemesResponseDto>> getMemesFromServer();
}
