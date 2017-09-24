package net.chmielowski.fiszki;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.os.AsyncTask;

import net.chmielowski.fiszki.room.AppDatabase;
import net.chmielowski.fiszki.room.User;

public class RoomService {
    public void saveData(Context context) {
        AsyncTask.execute(() -> {
            User user = new User();
            user.firstName = "Piotrek";
            user.lastName = "Chmielowski";
            Room.databaseBuilder(context, AppDatabase.class, "training")
                .fallbackToDestructiveMigration()
                .build()
                .userDao()
                .insertAll(user);
        });
    }
}
