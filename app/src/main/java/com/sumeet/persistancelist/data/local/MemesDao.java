package com.sumeet.persistancelist.data.local;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.sumeet.persistancelist.data.Meme;

import java.util.List;

@Dao
public interface MemesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Meme> items);

    @Query("SELECT * FROM " + DBConstant.USERS_TABLE_NAME)
    List<Meme> getAll();

}

