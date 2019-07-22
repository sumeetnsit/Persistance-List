package com.sumeet.persistancelist.ui.memelist;

public interface CardAdapterCallback<T> {
    void onCardClicked(int position, T data);
}
