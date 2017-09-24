package net.chmielowski.fiszki;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.os.AsyncTask;

import net.chmielowski.fiszki.room.AppDatabase;
import net.chmielowski.fiszki.room.User;

public class RoomService {
    public void saveData(Context context) {
        new MyAsyncTask().execute(context);
    }

    private static class MyAsyncTask extends AsyncTask<Context, Object, Object> {

        @Override
        protected Object doInBackground(Context[] objects) {
            User user = new User();
            user.firstName = "Piotrek";
            user.lastName = "Chmielowski";
            Room.databaseBuilder(objects[0], AppDatabase.class, "training")
                .build()
                .userDao()
                .insertAll(user);
            return null;
        }
    }
}
