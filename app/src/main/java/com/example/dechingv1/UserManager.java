package com.example.dechingv1;

public class UserManager {

    private MessageDatabase messageDatabase;

    public UserManager(MessageDatabase messageDatabase) {
        this.messageDatabase = messageDatabase;
    }

    public User getCurrentUser() {
        // (Remplacez cette partie par la logique pour obtenir l'utilisateur actuel)
        // Cela pourrait être basé sur l'authentification de l'utilisateur
        // ou en fonction de la session de l'utilisateur, etc.
        return messageDatabase.userDao().getUserById(1);
    }
}

