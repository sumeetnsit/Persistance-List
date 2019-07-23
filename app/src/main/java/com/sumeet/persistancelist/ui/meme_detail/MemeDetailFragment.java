package com.sumeet.persistancelist.ui.meme_detail;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.sumeet.persistancelist.R;
import com.sumeet.persistancelist.ui.BaseFragmentInteractionListener;

import io.reactivex.disposables.CompositeDisposable;

public class MemeDetailFragment extends Fragment {

    private static String ARGS_MEME_URL = "ARGS_MEME_URL";
    private static String ARGS_MEME_NAME = "ARGS_MEME_NAME";

    @NonNull
    private CompositeDisposable disposables = new CompositeDisposable();

    @Nullable
    private Context context;

    @Nullable
    private MemeDetailFragmentInteractionListener activityCommunicator;

    public static Fragment newInstance(@NonNull String imgUrl,
                                       @NonNull String memeName) {
        Bundle args = new Bundle();
        args.putString(ARGS_MEME_URL, imgUrl);
        args.putString(ARGS_MEME_NAME, memeName);
        MemeDetailFragment memeFragment = new MemeDetailFragment();
        memeFragment.setArguments(args);
        return memeFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.meme_detail_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getArguments() != null &&
                getArguments().getString(ARGS_MEME_URL) != null) {
            MemeDetailViewModel mViewModel = ViewModelProviders.of(this).get(MemeDetailViewModel.class);
            //noinspection ConstantConditions
            mViewModel.setMemeUrl(getArguments().getString(ARGS_MEME_URL),
                    getArguments().getString(ARGS_MEME_NAME));

            context = getContext();
            if (context instanceof MemeDetailFragmentInteractionListener) {
                activityCommunicator = (MemeDetailFragmentInteractionListener) context;

            }
            View rootView = getView();

            if (rootView != null) {
                setViews(rootView, mViewModel);
            }
        }

    }

    private void setViews(@NonNull View rootView, @NonNull MemeDetailViewModel mViewModel) {

        TextView memeName = rootView.findViewById(R.id.meme_name);
        ImageView memeImg = rootView.findViewById(R.id.meme_img);
        ImageView loaderImg = rootView.findViewById(R.id.loader_img);

        memeName.setText(mViewModel.getMemeSubject().getValue().second);
        if (activityCommunicator != null) {
            activityCommunicator.setToolbar(getString(R.string.meme_details));
        }
        if (context != null) {

            Glide.with(context)
                    .asGif()
                    .load(R.drawable.loading_gif)
                    .into(loaderImg);

            Glide.with(this).
                    load(mViewModel.getMemeSubject().getValue().first)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            loaderImg.setVisibility(View.GONE);
                            Toast.makeText(context, "Something went wrong", Toast.LENGTH_LONG).show();
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            loaderImg.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .error(R.drawable.error_img)
                    .into(memeImg);
        }
    }


    public interface MemeDetailFragmentInteractionListener
            extends BaseFragmentInteractionListener {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        disposables.clear();
    }
}
