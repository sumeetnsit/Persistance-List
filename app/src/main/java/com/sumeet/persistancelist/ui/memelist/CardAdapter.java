package com.sumeet.persistancelist.ui.memelist;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.sumeet.persistancelist.R;
import com.sumeet.persistancelist.data.Meme;

import java.util.ArrayList;
import java.util.List;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> implements Filterable {

    @NonNull
    private final Context context;
    @NonNull
    private final CardAdapterCallback<Meme> cardAdapterCallback;
    @NonNull
    private List<Meme> memes = new ArrayList<>();
    private ArrayList<Meme> filteredMemes = new ArrayList<>();

    CardAdapter(@NonNull Context context,
                @NonNull CardAdapterCallback<Meme> cardAdapterCallback) {
        this.context = context;
        this.cardAdapterCallback = cardAdapterCallback;
    }

    @Override
    public long getItemId(int position) {
        return Long.parseLong(filteredMemes.get(position).getId());
    }

    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@Nullable ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(context);
        return new ViewHolder(inflater.inflate(R.layout.row_meme_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Meme currentMeme = filteredMemes.get(position);
        if (position % 2 == 0) {
            holder.memeName.setBackgroundColor(ContextCompat.getColor(context, R.color.estonia_blue));
        } else {
            holder.memeName.setBackgroundColor(ContextCompat.getColor(context, R.color.estonia_black));
        }
        holder.memeName.setText(currentMeme.getName());
        holder.cardView.setOnClickListener(v -> cardAdapterCallback.onCardClicked(position, currentMeme));
    }

    @Override
    public int getItemCount() {
        return filteredMemes.size();
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

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView memeName;
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            memeName = itemView.findViewById(R.id.meme_name);
            cardView = itemView.findViewById(R.id.card_view);
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


}
