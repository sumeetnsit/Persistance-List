package com.sumeet.persistancelist.data;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;

interface MemesRepo {

    public Observable<List<Meme>> getMemes();
}
