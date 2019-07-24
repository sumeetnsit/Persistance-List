package com.sumeet.persistancelist.data.remote;

import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;
import com.sumeet.persistancelist.data.Meme;

import java.io.Serializable;
import java.util.List;

public class MemesResponseDto implements Serializable {


    @SerializedName("success")
    private boolean success = false;
    @Nullable
    @SerializedName("data")
    private Data data;

    public MemesResponseDto(final boolean success, @Nullable Data data) {
        this.success = success;
        this.data = data;
    }

    public boolean getSuccess() {
        return success;
    }

    @Nullable
    public Data getData() {
        return data;
    }

    public static class Data {

        @Nullable
        @SerializedName("memes")
        private List<Meme> memes;

        public Data(@Nullable List<Meme> memes) {
            this.memes = memes;
        }

        @Nullable
        public List<Meme> getMemes() {
            return memes;
        }
    }
}
