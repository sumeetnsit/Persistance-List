package com.sumeet.persistancelist.data.local;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.sumeet.persistancelist.data.Meme;

import java.util.List;

@Dao
public interface MemesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Meme> items);

    @Query("SELECT * FROM " + DBConstant.USERS_TABLE_NAME)
    List<Meme> getAll();

}

