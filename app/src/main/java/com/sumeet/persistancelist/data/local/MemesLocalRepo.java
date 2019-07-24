package com.sumeet.persistancelist.data.local;

import androidx.annotation.NonNull;

import com.sumeet.persistancelist.data.Meme;
import com.sumeet.persistancelist.data.remote.MemesResponseDto;

import java.util.List;

import io.reactivex.Observable;

public interface MemesLocalRepo {

    Observable<List<Meme>> getAllMeme();

    void addMemes(@NonNull MemesResponseDto memes);

}
