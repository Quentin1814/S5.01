package com.example.dechingv1.Modele;

import java.sql.Date;

public class Commentaire {
    private String id;//Id du commentaire dans la base de données
    private String texte;//Texte du commentaire
    private Date dateCommentaire;//Date de publication du commentaire
    private Utilisateur monUtilisateur;//L'utilisateur qui a écrit le commentaire

    public Commentaire(String id, String texte, Date dateCommentaire, Utilisateur monUtilisateur) {
        this.id = id;
        this.texte = texte;
        this.dateCommentaire = dateCommentaire;
        this.monUtilisateur = monUtilisateur;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTexte() {
        return texte;
    }

    public void setTexte(String texte) {
        this.texte = texte;
    }

    public Date getDateCommentaire() {
        return dateCommentaire;
    }

    public void setDateCommentaire(Date dateCommentaire) {
        this.dateCommentaire = dateCommentaire;
    }

    public Utilisateur getMonUtilisateur() {
        return monUtilisateur;
    }

    public void setMonUtilisateur(Utilisateur monUtilisateur) {
        this.monUtilisateur = monUtilisateur;
    }
}
