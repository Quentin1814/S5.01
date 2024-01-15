package com.example.dechingv1;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Message.class, User.class}, version = 1)
public abstract class MessageDatabase extends RoomDatabase {

    private static MessageDatabase instance;

    public abstract MessageDao messageDao();
    public abstract UserDao userDao();

    public static synchronized MessageDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            MessageDatabase.class, "message_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
