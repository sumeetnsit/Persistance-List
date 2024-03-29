package com.sumeet.persistancelist.ui.meme_detail;


import androidx.annotation.NonNull;
import androidx.core.util.Pair;
import androidx.lifecycle.ViewModel;

import io.reactivex.subjects.BehaviorSubject;

public class MemeDetailViewModel extends ViewModel {

    @NonNull
    private BehaviorSubject<Pair<String, String>> memeUrlSubject = BehaviorSubject.create();


    public MemeDetailViewModel() {
    }

    public void setMemeUrl(@NonNull String memeUrl,
                           @NonNull String memeName) {
        this.memeUrlSubject.onNext(Pair.create(memeUrl, memeName));
    }

    public BehaviorSubject<Pair<String, String>> getMemeSubject() {
        return memeUrlSubject;
    }
}
