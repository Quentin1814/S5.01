package com.example.deching.Modele.Modele;

import java.sql.Date;

/**
 * Classe représentant un Evenement
 */
public class Evenement {
    private String photoBase64;
    // image de l'événement
    /**
     * Identifiant de l'événement dans la base de données
     */
    private int id;

    /**
     * Nom de l'événement
     */
    private String nom;

    /**
     * Description de l'événement
     */
    private String description;

    /**
     * Nombre total de participant à l'événement
     */
    private int nbParticipantTotal;

    /**
     * Adresse du lieu de l'événement
     */
    private String lieu;

    /**
     * Date de l'événement
     */
    private String dateEvent;

    /**
     * Objet "Utilisateur" qui a créé l'événement
     */
    private int monUtilisateur;

    /**
     * Constructeur
     * @param nom Nom de l'événement
     * @param description Description de événement
     * @param nbParticipantTotal Nombre de participant Total à l'événement
     * @param lieu Lieu de l'événement
     * @param dateEvent Date de l'événement
     * @param monUtilisateur Un utilisateur
     */
    public Evenement(String nom, String description, int nbParticipantTotal, String lieu, String dateEvent, int monUtilisateur, String photoBase64) {
        id=0;
        this.nom = nom;
        this.description = description;
        this.nbParticipantTotal = nbParticipantTotal;
        this.lieu = lieu;
        this.dateEvent = dateEvent;
        this.monUtilisateur = monUtilisateur;
        this.photoBase64=photoBase64;
    }

    /**
     * Retourne l'identifiant de l'événement
     * @return L'identifiant de l'événement
     */

    public String getPhotoBase64() {
        return photoBase64;
    }

    public int getId() {
        return id;
    }

    /**
     * Modifie l'identifiant de l'événement
     * @param id Nouvel identifiant de l'événement
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Retourne le nom de l'événement
     * @return Le nom de l'événement
     */
    public String getNom() {
        return nom;
    }

    /**
     * Modifie le nom de l'événement
     * @param nom Nouveau nom de l'événement
     */
    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
     * Retourne la description de l'événement
     * @return La description de l'événement
     */
    public String getDescription() {
        return description;
    }

    /**
     * Modifie la description de l'événement
     * @param description Nouvelle description de l'événement
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Retourne le nombre total de participant de l'événement
     * @return Le nombre total de participant de l'événement
     */
    public int getNbParticipantTotal() {
        return nbParticipantTotal;
    }

    /**
     * Modifie le nombre total de participant de l'événement
     * @param nbParticipantTotal Nouveau nombre total de participant de l'événement
     */
    public void setNbParticipantTotal(int nbParticipantTotal) {
        this.nbParticipantTotal = nbParticipantTotal;
    }

    /**
     * Retourne le lieu de l'événement
     * @return Le lieu de l'événement
     */
    public String getLieu() {
        return lieu;
    }

    /**
     * Modifie le lieu de l'événement
     * @param lieu Nouveau lieu de l'événement
     */
    public void setLieu(String lieu) {
        this.lieu = lieu;
    }

    /**
     * Retourne la date de l'événement
     * @return La date de l'événement
     */
    public String getDateEvent() {
        return dateEvent;
    }

    /**
     * Modifie la date de l'événement
     * @param dateEvent Nouvelle date de l'événement
     */
    public void setDateEvent(String dateEvent) {
        this.dateEvent = dateEvent;
    }

    /**
     * Retourne un utilisateur
     * @return Un utilisateur
     */
    public int getMonUtilisateur() {
        return monUtilisateur;
    }

    /**
     * Modifie l'utilisateur
     * @param monUtilisateur Nouvel utilisateur
     */
    public void setMonUtilisateur(int monUtilisateur) {
        this.monUtilisateur = monUtilisateur;
    }
}
