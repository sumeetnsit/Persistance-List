package com.sumeet.persistancelist.data.local;

import androidx.room.Room;

import com.sumeet.persistancelist.MemeApplication;
import com.sumeet.persistancelist.data.Meme;
import com.sumeet.persistancelist.data.remote.MemesResponseDto;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Response;

import static com.sumeet.persistancelist.data.MemesRepoImpl.isValidResponse;

public class MemesLocalRepoImpl implements MemesLocalRepo {

    private MemesDao memesDao;

    public MemesLocalRepoImpl() {
        MemeDb db = Room.databaseBuilder(MemeApplication.getApplicationInstance(),
                MemeDb.class, "meme_app_db").build();
        this.memesDao = db.memesDao();
    }

    @Override
    public Observable<List<Meme>> getAllMeme() {
        return Observable.fromCallable(() -> memesDao.getAll());
    }

    @Override
    public void addMemes(Response<MemesResponseDto> memes) {
        if (isValidResponse(memes)) {
            //noinspection ConstantConditions
            memesDao.insertAll(memes.body().getData().getMemes());
        }
    }


}