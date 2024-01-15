package com.example.dechingv1;

import androidx.room.Dao;
import androidx.room.Query;

@Dao
public interface UserDao {

    @Query("SELECT * FROM users WHERE id = :userId")
    User getUserById(int userId);
}
