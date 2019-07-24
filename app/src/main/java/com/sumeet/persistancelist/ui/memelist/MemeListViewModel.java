package com.sumeet.persistancelist.ui.memelist;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModel;

import com.sumeet.persistancelist.data.Meme;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;


public class MemeListViewModel extends ViewModel {

    @Nullable
    private MemeListViewModelInteraction interaction;
    @NonNull
    private CompositeDisposable disposables = new CompositeDisposable();

    public MemeListViewModel() {

    }

    public void setDataAndSubscribe(@NonNull MemeListViewModelInteraction memeListViewModelInteraction,
                                    @NonNull Observable<List<Meme>> memeList) {
        this.interaction = memeListViewModelInteraction;

        disposables.add(
                memeList.observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(__ -> interaction.onFetchStart())
                        .doOnNext(newMemes -> interaction.onFetchSuccess(newMemes))
                        .doOnError(e -> interaction.onFetchFailed(e))
                        .subscribe()
        );
    }

    public interface MemeListViewModelInteraction {
        void onFetchStart();

        void onFetchSuccess(@NonNull List<Meme> newMemes);

        void onFetchFailed(@NonNull Throwable e);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposables.clear();
    }
}
