package com.sumeet.persistancelist.data.remote;

import io.reactivex.Observable;
import retrofit2.Response;

public interface MemesRemoteRepo {

    public Observable<MemesResponseDto> getAllMemes();

}
