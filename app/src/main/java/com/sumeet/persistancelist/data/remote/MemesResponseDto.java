package com.sumeet.persistancelist.data.remote;

import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;
import com.sumeet.persistancelist.data.Meme;

import java.io.Serializable;
import java.util.List;

public class MemesResponseDto implements Serializable {

    @Nullable
    @SerializedName("success")
    private Boolean success;
    @Nullable
    @SerializedName("data")
    private Data data;

    public MemesResponseDto(@Nullable Boolean success, @Nullable Data data) {
        this.success = success;
        this.data = data;
    }

    @Nullable
    public Boolean getSuccess() {
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
