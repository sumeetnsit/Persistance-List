package com.sumeet.persistancelist.data;

import android.util.Log;

import androidx.annotation.NonNull;

import com.sumeet.persistancelist.data.local.MemesLocalRepo;
import com.sumeet.persistancelist.data.remote.MemesRemoteRepo;
import com.sumeet.persistancelist.data.remote.MemesResponseDto;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

/**
 * responsibility : to provide list of memes fetching from remote or getting it locally from db.
 */
public class MemesRepoImpl implements MemesRepo {

    @NonNull
    private MemesLocalRepo localRepo;

    @NonNull
    private MemesRemoteRepo remoteRepo;

    public MemesRepoImpl(@NonNull MemesRemoteRepo remoteRepo,
                         @NonNull MemesLocalRepo localRepo) {
        this.localRepo = localRepo;
        this.remoteRepo = remoteRepo;
    }

    /**
     * @return an Observable with remote & local data sources merged
     * and only distinct item are passed from both the sources.
     */
    @Override
    public Observable<List<Meme>> getMemes() {

        return remoteRepo.getAllMemes()
                .doOnNext(localRepo::addMemes)
                .filter(MemesRepoImpl::isValidResponse)
                .map(this::extractListFromNetworkResponse)
                .doOnNext(logResults -> Log.i("api response", String.format("%d  memes receives from remote server", logResults.size())))
                .observeOn(Schedulers.io())
                .doOnError(e -> Log.e("log", "sumeet", e))
                .onErrorResumeNext(getMemesFromLocalRepo())
                .subscribeOn(Schedulers.io());
    }

    public Observable<List<Meme>> getMemesFromLocalRepo() {
        return localRepo.getAllMeme();
    }

    public List<Meme> extractListFromNetworkResponse(@NonNull MemesResponseDto it) {
        //noinspection ConstantConditions
        return it.getData().getMemes();
    }

    private static boolean isValidResponse(MemesResponseDto memesResponseDtoResponse) {
        return memesResponseDtoResponse.getData() != null &&
                memesResponseDtoResponse.getData().getMemes() != null &&
                memesResponseDtoResponse.getData().getMemes().size() > 0;
    }
}
