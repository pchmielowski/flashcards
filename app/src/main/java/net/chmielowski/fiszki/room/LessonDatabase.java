package net.chmielowski.fiszki.room;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {LessonInfo.class}, version = 2)
public abstract class LessonDatabase extends RoomDatabase {
    public abstract LessonDao dao();
}

