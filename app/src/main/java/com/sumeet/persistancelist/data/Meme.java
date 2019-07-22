package com.sumeet.persistancelist.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;
import com.sumeet.persistancelist.data.local.DBConstant;

import java.io.Serializable;

@Entity(tableName = DBConstant.USERS_TABLE_NAME)
public class Meme implements Serializable {

    @PrimaryKey()
    @SerializedName("id")
    @NonNull
    @ColumnInfo(name = DBConstant.USER_ID)
    private String id;

    @Nullable
    @SerializedName("name")
    @ColumnInfo(name = DBConstant.NAME)
    private String name;
    @Nullable
    @SerializedName("url")
    @ColumnInfo(name = DBConstant.URL)
    private String url;

    public Meme(@NonNull String id, @Nullable String name, @Nullable String url) {
        this.id = id;
        this.name = name;
        this.url = url;
    }

    @NonNull
    public String getId() {
        return id;
    }

    @Nullable
    public String getName() {
        return name;
    }

    @Nullable
    public String getUrl() {
        return url;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        return (obj instanceof Meme && ((Meme) obj).name != null
                && this.name != null && this.name.equals(((Meme) obj).name))
                && this.url != null && this.url.equals(((Meme) obj).url);
    }
}
