package com.sumeet.persistancelist.data.local;

import androidx.annotation.NonNull;
import androidx.room.Room;

import com.sumeet.persistancelist.MemeApplication;
import com.sumeet.persistancelist.data.Meme;
import com.sumeet.persistancelist.data.remote.MemesResponseDto;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public class MemesLocalRepoImpl implements MemesLocalRepo {

    private MemesDao memesDao;

    public MemesLocalRepoImpl() {
        MemeDb db = Room.databaseBuilder(MemeApplication.getApplicationInstance(),
                MemeDb.class, "meme_app_db").build();
        this.memesDao = db.memesDao();
    }

    @Override
    public Observable<List<Meme>> getAllMeme() {
        return Observable.fromCallable(() -> memesDao.getAll()).observeOn(Schedulers.io());
    }

    @Override
    public void addMemes(@NonNull MemesResponseDto memes) {
        if (memes.getData() != null) {
            memesDao.insertAll(memes.getData().getMemes());
        }
    }

}