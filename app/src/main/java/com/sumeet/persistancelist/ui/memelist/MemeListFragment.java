package com.sumeet.persistancelist.ui.memelist;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.TextView;

import com.sumeet.persistancelist.R;
import com.sumeet.persistancelist.data.Meme;
import com.sumeet.persistancelist.data.MemesRepoImpl;
import com.sumeet.persistancelist.ui.BaseFragmentInteractionListener;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;

public class MemeListFragment extends Fragment implements CardAdapterCallback<Meme> {

    @NonNull
    private CompositeDisposable disposables = new CompositeDisposable();

    @Nullable
    Context context;

    @Nullable
    CardAdapter memesAdapter;

    @NonNull
    MemesRepoImpl memesRepo = new MemesRepoImpl();

    @Nullable
    MemeListFragmentInteractionListener activityCommunicator;
    private RecyclerView recyclerView;

    public static MemeListFragment newInstance() {
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

        disposables.add(
                mViewModel.getMemeList()
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnNext(newMemes -> {
                            if (memesAdapter != null) {
                                memesAdapter.addData(newMemes);
                                recyclerView.scheduleLayoutAnimation();
                            }
                        })
                        .doOnError(e -> Log.e("sumeet", "sumeet", e))
                        .subscribe()
        );

    }

    private void setViews(@NonNull View rootView) {
        if (context != null) {
            int resId = R.anim.layout_animation_fall_down;
            LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(context, resId);
            recyclerView = rootView.findViewById(R.id.rv_memeList);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setLayoutAnimation(animation);
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

    public interface MemeListFragmentInteractionListener extends BaseFragmentInteractionListener {
        void onCardClicked(@NonNull Meme meme);

    }
}
