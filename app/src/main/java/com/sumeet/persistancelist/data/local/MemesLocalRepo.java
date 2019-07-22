package com.sumeet.persistancelist.data.local;

import com.sumeet.persistancelist.data.Meme;
import com.sumeet.persistancelist.data.remote.MemesResponseDto;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Response;

public interface MemesLocalRepo {

    public Observable<List<Meme>> getAllMeme();

    public void addMemes(Response<MemesResponseDto> users);

}
