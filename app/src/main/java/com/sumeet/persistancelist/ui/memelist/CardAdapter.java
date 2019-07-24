package com.sumeet.persistancelist.ui.memelist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.sumeet.persistancelist.R;
import com.sumeet.persistancelist.data.Meme;

import java.util.ArrayList;
import java.util.List;

public class CardAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {

    private static final int TYPE_LOADING = 0;
    private static final int TYPE_NO_RESULT = 1;
    private static final int TYPE_NORMAL = 2;
    private static final int LOADER_COUNT = 3;
    @NonNull
    private final Context context;
    @NonNull
    private final CardAdapterCallback<Meme> cardAdapterCallback;
    @NonNull
    private List<Meme> memes = new ArrayList<>();
    private ArrayList<Meme> filteredMemes = new ArrayList<>();

    private boolean mShowLoaders;

    CardAdapter(@NonNull Context context,
                @NonNull CardAdapterCallback<Meme> cardAdapterCallback) {
        this.context = context;
        this.cardAdapterCallback = cardAdapterCallback;
    }

    @Override
    @NonNull
    public RecyclerView.ViewHolder onCreateViewHolder(@Nullable ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        final LayoutInflater inflater = LayoutInflater.from(context);
        switch (viewType) {
            case TYPE_NORMAL:
                viewHolder = new MemeItemViewHolder(inflater.inflate(R.layout.row_meme_card, parent, false));
                break;

            case TYPE_LOADING:
                View viewLoader = inflater.inflate(R.layout.meme_loader_card, parent, false);
                ImageView imageView = viewLoader.findViewById(R.id.gif_image_view);
                Glide.with(context)
                        .asGif()
                        .load(R.drawable.gif_meme_card_loader)
                        .into(imageView);
                viewHolder = new LoaderGifViewHolder(viewLoader);
                break;

            case TYPE_NO_RESULT:
                viewHolder = new NoResultsViewHolder(inflater.inflate(R.layout.no_results_view, parent, false));
        }
        if (viewHolder != null) {
            return viewHolder;
        } else {
            throw new RuntimeException("view holder cannot be null");
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MemeItemViewHolder) {
            MemeItemViewHolder currentMemeItemViewHolder = (MemeItemViewHolder) holder;
            Meme currentMeme = filteredMemes.get(position);
            if (position % 2 == 0) {
                currentMemeItemViewHolder.memeName.setBackgroundColor(ContextCompat.getColor(context, R.color.estonia_blue));
            } else {
                currentMemeItemViewHolder.memeName.setBackgroundColor(ContextCompat.getColor(context, R.color.estonia_black));
            }
            currentMemeItemViewHolder.memeName.setText(currentMeme.getName());
            currentMemeItemViewHolder.cardView.setOnClickListener(v -> cardAdapterCallback.onCardClicked(position, currentMeme));
        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                List<Meme> filteredList = new ArrayList<>();
                String charString = charSequence.toString().toLowerCase();
                if (charString.isEmpty()) {
                    filteredList.addAll(memes);
                } else {

                    for (Meme meme : memes) {

                        if (meme.getName() != null &&
                                meme.getName().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(meme);
                        }
                    }
                }
//
                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredList;
                return filterResults;

            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredMemes = (ArrayList<Meme>) results.values; // -_- Android? Why?
                notifyDataSetChanged();
            }
        };
    }

    static class MemeItemViewHolder extends RecyclerView.ViewHolder {

        TextView memeName;
        CardView cardView;

        public MemeItemViewHolder(@NonNull View itemView) {
            super(itemView);
            memeName = itemView.findViewById(R.id.meme_name);
            cardView = itemView.findViewById(R.id.card_view);
        }
    }

    static class LoaderGifViewHolder extends RecyclerView.ViewHolder {

        public LoaderGifViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    static class NoResultsViewHolder extends RecyclerView.ViewHolder {

        public NoResultsViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public void clearData() {
        this.memes = new ArrayList<>();
        notifyDataSetChanged();
    }

    public void addData(List<Meme> memes) {
        this.memes.addAll(memes);
        filteredMemes.addAll(memes);
        notifyDataSetChanged();
    }

    @Override
    public final int getItemCount() {
        if (mShowLoaders) return filteredMemes.size() + LOADER_COUNT;
        else return (filteredMemes.size() == 0) ? 1 // No results view
                : filteredMemes.size();
    }

    @Override
    public final int getItemViewType(int position) {
        if (mShowLoaders) {
            return TYPE_LOADING;
        } else {
            if (filteredMemes.size() == 0) {
                return TYPE_NO_RESULT;
            }
            return TYPE_NORMAL;
        }
    }

    /**
     * Toggle Gif loader with this function.
     */
    void showLoaders(boolean show) {
        int itemCount = filteredMemes.size();
        if (show && !mShowLoaders) {
            mShowLoaders = true;
            if (itemCount > 0) {
                notifyItemRangeInserted(itemCount, LOADER_COUNT);
            } else {
                notifyDataSetChanged();
            }
        } else if (!show && mShowLoaders) {
            mShowLoaders = false;
            if (itemCount > 0) {
                notifyItemRangeRemoved(itemCount, LOADER_COUNT);
            } else {
                notifyDataSetChanged();
            }
        }
    }


}
