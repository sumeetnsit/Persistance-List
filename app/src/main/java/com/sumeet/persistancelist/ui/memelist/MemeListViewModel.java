package com.sumeet.persistancelist.ui.memelist;

import androidx.lifecycle.ViewModel;

import com.sumeet.persistancelist.data.Meme;

import java.util.List;

import io.reactivex.Observable;


public class MemeListViewModel extends ViewModel {

    private Observable<List<Meme>> memeList;

    public MemeListViewModel() {
    }

    public void setMemeList(Observable<List<Meme>> memeList) {
        this.memeList = memeList;
    }

    public Observable<List<Meme>> getMemeList() {
        return memeList;
    }
}
