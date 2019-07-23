package com.sumeet.persistancelist.ui.memelist;


import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sumeet.persistancelist.R;
import com.sumeet.persistancelist.data.EspressoTestingIdlingResource;
import com.sumeet.persistancelist.data.FetcherListener;
import com.sumeet.persistancelist.data.Meme;
import com.sumeet.persistancelist.data.MemesRepoImpl;
import com.sumeet.persistancelist.ui.BaseFragmentInteractionListener;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;

public class MemeListFragment extends Fragment implements CardAdapterCallback<Meme>,
        FetcherListener {

    @NonNull
    private CompositeDisposable disposables = new CompositeDisposable();

    @Nullable
    private Context context;

    @Nullable
    private CardAdapter memesAdapter;

    @NonNull
    private MemesRepoImpl memesRepo = new MemesRepoImpl();

    @Nullable
    private MemeListFragmentInteractionListener activityCommunicator;

    private RecyclerView recyclerView;

    static MemeListFragment newInstance() {
        return new MemeListFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.meme_list_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        MemeListViewModel mViewModel = ViewModelProviders.of(this).get(MemeListViewModel.class);
        mViewModel.setMemeList(memesRepo.getMemes());
        context = getContext();
        if (context instanceof MemeListFragmentInteractionListener) {
            activityCommunicator = (MemeListFragmentInteractionListener) context;
        }
        View rootView = getView();

        if (rootView != null) {
            setViews(rootView);
            initListeners(mViewModel, rootView);
        }

    }

    private void initListeners(@NonNull MemeListViewModel mViewModel,
                               @NonNull View rootView) {

        TextView searchEditText = rootView.findViewById(R.id.et_search);
        TextView clearSearch = rootView.findViewById(R.id.clear_search);

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence newCharSequence, int start, int before, int count) {
                if (memesAdapter != null) {
                    memesAdapter.getFilter().filter(newCharSequence);
                    if (newCharSequence != null && newCharSequence.length() > 0 &&
                            clearSearch.getVisibility() == View.GONE) {
                        clearSearch.setVisibility(View.VISIBLE);
                    } else {
                        clearSearch.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        clearSearch.setOnClickListener(v -> searchEditText.setText(""));

        onFetchStarted();
        disposables.add(
                mViewModel.getMemeList()
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnNext(newMemes -> {
                            if (memesAdapter != null) {
                                memesAdapter.addData(newMemes);
                                onFetchFinished();
                                setRecyclerViewLoadAnimation();
                            }
                        })
                        .doOnError(e -> {
                            Log.e("sumeet", "sumeet", e);
                            onFetchFinished();
                        })
                        .subscribe()
        );

    }

    private void setRecyclerViewLoadAnimation() {
        int resId = R.anim.layout_animation_fall_down;
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(context, resId);
        recyclerView.setLayoutAnimation(animation);
    }


    private void setViews(@NonNull View rootView) {
        if (context != null) {

            recyclerView = rootView.findViewById(R.id.rv_memeList);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            memesAdapter = new CardAdapter(context, this);
            memesAdapter.setHasStableIds(true);
            recyclerView.setAdapter(memesAdapter);
            if (activityCommunicator != null) {
                activityCommunicator.setToolbar(getString(R.string.memes));
            }

        }
    }

    @Override
    public void onCardClicked(final int position, @NonNull Meme data) {
        if (activityCommunicator != null) {
            activityCommunicator.onCardClicked(data);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        disposables.clear();
    }

    @Override
    public void onFetchStarted() {
        EspressoTestingIdlingResource.increment();
    }

    @Override
    public void onFetchFinished() {
        EspressoTestingIdlingResource.decrement();
    }

    public interface MemeListFragmentInteractionListener extends BaseFragmentInteractionListener {
        void onCardClicked(@NonNull Meme meme);

    }
}
