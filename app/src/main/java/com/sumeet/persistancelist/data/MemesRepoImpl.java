package com.sumeet.persistancelist.data;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.sumeet.persistancelist.data.local.MemesLocalRepoImpl;
import com.sumeet.persistancelist.data.remote.MemesRemoteRepoImpl;
import com.sumeet.persistancelist.data.remote.MemesResponseDto;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

/**
 * responsibility : to provide list of memes fetching from remote or getting it locally from db.
 */
public class MemesRepoImpl implements MemesRepo {

    @NonNull
    private final MemesLocalRepoImpl localRepo = new MemesLocalRepoImpl();

    @NonNull
    private final MemesRemoteRepoImpl remoteRepo = new MemesRemoteRepoImpl();

    public MemesRepoImpl() {

    }

    /**
     * @return an Observable with remote & local data sources merged
     * and only distinct item are passed from both the sources.
     */
    @Override
    public Observable<List<Meme>> getMemes() {
        //noinspection ConstantConditions
        return remoteRepo.getAllMemes()

                        .doOnNext(localRepo::addMemes)
                        .filter(MemesRepoImpl::isValidResponse)
                        .map(it -> it.body().getData().getMemes())
                        .onErrorResumeNext(localRepo.getAllMeme())
                        .subscribeOn(Schedulers.io());
    }

    public static boolean isValidResponse(Response<MemesResponseDto> memesResponseDtoResponse) {
        return memesResponseDtoResponse.isSuccessful()
                && memesResponseDtoResponse.body() != null
                && memesResponseDtoResponse.body().getSuccess() != null &&
                memesResponseDtoResponse.body().getSuccess() &&
                memesResponseDtoResponse.body().getData() != null &&
                memesResponseDtoResponse.body().getData().getMemes() != null &&
                memesResponseDtoResponse.body().getData().getMemes().size() > 0;
    }
}
