package com.example.deching.Modele.Modele;

import java.sql.Blob;

/**
 * Classe représentant un badge.
 */
public class Badge {
    /**
     * Identifiant du badge dans la base de données
     */
    private int id;

    /**
     * Nom donné au badge
     */
    private String nom;

    /**
     * Image qui sera affiché sur l'écran pour représenter le badge
     */
    private Blob image;

    /**
     * Court texte expliquant la condition d'obtention de ce badge
     */
    private String description;

    /**
     * Constructeur de la classe Badge
     * @param nom Nom du badge
     * @param image Image du badge
     * @param description Description du badge
     */
    public Badge(String nom,Blob image,String description){
        id=0;
        this.nom=nom;
        this.image=image;
        this.description=description;
    }

    /**
     * Retourne l'identifiant du badge
     * @return L'identifiant du badge
     */
    public int getId() {
        return id;
    }

    /**
     * Modifie l'identifiant du badge
     * @param id Nouvel identifiant du badge
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Retourne le nom du badge
     * @return Le nom du badge
     */
    public String getNom() {
        return nom;
    }

    /**
     * Modifie le nom du badge
     * @param nom Nouveau nom du badge
     */
    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
     * Retourne l'image du badge
     * @return L'image du badge
     */
    public Blob getImage() {
        return image;
    }

    /**
     * Modifie l'image du badge
     * @param image Nouvelle image du badge
     */
    public void setImage(Blob image) {
        this.image = image;
    }

    /**
     * Retourne la description du badge
     * @return La description du badge
     */
    public String getDescription() {
        return description;
    }

    /**
     * Modifie la description du badge
     * @param description Nouvelle description du badge
     */
    public void setDescription(String description) {
        this.description = description;
    }
}