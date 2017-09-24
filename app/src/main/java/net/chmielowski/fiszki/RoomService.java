package net.chmielowski.fiszki;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.os.AsyncTask;

import net.chmielowski.fiszki.room.LessonDatabase;
import net.chmielowski.fiszki.room.LessonInfo;

import java.util.Date;
import java.util.List;

import io.reactivex.Flowable;

public class RoomService {

    private LessonDatabase database;

    public RoomService(Context context) {
        database = Room.databaseBuilder(context, LessonDatabase.class, "lessons")
                       .fallbackToDestructiveMigration()
                       .build();
    }

    public void saveData() {
        AsyncTask.execute(() -> {
            LessonInfo lessonInfo = new LessonInfo();
            lessonInfo.time = new Date().getTime();
            database.dao()
                    .insertAll(lessonInfo);
        });
    }

    public Flowable<List<LessonInfo>> data() {
        return database.dao().getAll();
    }
}
