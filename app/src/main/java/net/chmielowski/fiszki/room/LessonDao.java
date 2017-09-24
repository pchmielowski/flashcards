package net.chmielowski.fiszki.room;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import io.reactivex.Flowable;

@Dao
public interface LessonDao {
    @Query("SELECT * FROM LessonInfo")
    Flowable<List<LessonInfo>> getAll();

    @Insert
    void insertAll(LessonInfo... data);
}