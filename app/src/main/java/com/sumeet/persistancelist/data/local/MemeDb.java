package com.sumeet.persistancelist.data.local;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.sumeet.persistancelist.data.Meme;

@Database(entities = {Meme.class}, version = 1, exportSchema = false)
public abstract class MemeDb extends RoomDatabase {

   public abstract MemesDao memesDao();

}
