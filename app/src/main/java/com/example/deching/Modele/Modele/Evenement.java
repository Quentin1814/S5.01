package com.example.deching.Modele.Modele;

import java.sql.Date;

public class Evenement {
    private int id;//Identifiant de l'événement dans la base de données
    private String nom;//Nom de l'événement
    private String description;//Description de l'événement
    private int nbParticipantTotal;//Nombre total de participant à l'événement
    private String lieu;//Adresse du lieu de l'événement
    private Date dateEvent;//Date de l'événement
    private Utilisateur monUtilisateur;//Objet "Utilisateur" qui a créé l'événement

    public Evenement(String nom, String description, int nbParticipantTotal, String lieu, Date dateEvent, Utilisateur monUtilisateur) {
        id=0;
        this.nom = nom;
        this.description = description;
        this.nbParticipantTotal = nbParticipantTotal;
        this.lieu = lieu;
        this.dateEvent = dateEvent;
        this.monUtilisateur = monUtilisateur;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getNbParticipantTotal() {
        return nbParticipantTotal;
    }

    public void setNbParticipantTotal(int nbParticipantTotal) {
        this.nbParticipantTotal = nbParticipantTotal;
    }

    public String getLieu() {
        return lieu;
    }

    public void setLieu(String lieu) {
        this.lieu = lieu;
    }

    public Date getDateEvent() {
        return dateEvent;
    }

    public void setDateEvent(Date dateEvent) {
        this.dateEvent = dateEvent;
    }

    public Utilisateur getMonUtilisateur() {
        return monUtilisateur;
    }

    public void setMonUtilisateur(Utilisateur monUtilisateur) {
        this.monUtilisateur = monUtilisateur;
    }
}
