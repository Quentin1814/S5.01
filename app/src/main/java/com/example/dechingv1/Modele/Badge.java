package com.example.deching.Modele;

import java.sql.Blob;

public class Badge {
    private int id;//Identifiant du badge dans la base de données
    private String nom;//Nom donné au badge
    private Blob image;//Image qui sera affiché sur l'écran pour représenter le badge
    private String description;//Court texte expliquant la condition d'obtention de ce badge

    public Badge(String nom,Blob image,String description){
        id=0;
        this.nom=nom;
        this.image=image;
        this.description=description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Blob getImage() {
        return image;
    }

    public void setImage(Blob image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
