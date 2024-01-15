package com.example.dechingv1;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

@Dao
public interface MessageDao {

    @Insert
    void insertMessage(Message message);

    @Query("SELECT * FROM messages WHERE userId = :userId ORDER BY id ASC")
    List<Message> getMessagesForUser(int userId);
}
