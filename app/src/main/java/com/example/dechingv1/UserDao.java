package com.example.dechingv1;

import androidx.room.Dao;
import androidx.room.Query;

import com.example.dechingv1.Modele.Utilisateur;

@Dao
public interface UserDao {
    @Query("SELECT * FROM utilisateur WHERE id = :userId")
    Utilisateur getUserById(int userId);
}
