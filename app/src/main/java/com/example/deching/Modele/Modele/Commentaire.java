package com.example.deching.Modele.Modele;

import java.sql.Date;

/**
 * Classe représentant un commentaire
 */
public class Commentaire {
    /**
     * Identifiant du commentaire dans la base de données
     */
    private String id;

    /**
     * Texte du commentaire
     */
    private String texte;

    /**
     * Date de publication du commentaire
     */
    private Date dateCommentaire;

    /**
     * L'utilisateur qui a écrit le commentaire
     */
    private Utilisateur monUtilisateur;

    /**
     * Constructeur de la classe Commentaire
     * @param id Identifiant du commentaire dans la base de données
     * @param texte Texte du commentaire
     * @param dateCommentaire Date de publication du commentaire
     * @param monUtilisateur L'utilisateur qui a écrit le commentaire
     */
    public Commentaire(String id, String texte, Date dateCommentaire, Utilisateur monUtilisateur) {
        this.id = id;
        this.texte = texte;
        this.dateCommentaire = dateCommentaire;
        this.monUtilisateur = monUtilisateur;
    }

    /**
     * Retourne l'identifiant du commentaire.
     * @return L'identifiant du commentaire.
     */
    public String getId() {
        return id;
    }

    /**
     * Modifie l'identifiant du commentaire
     * @param id Nouvel identifiant du commentaire
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Retourne le texte du commentaire
     * @return Le texte du commentaire
     */
    public String getTexte() {
        return texte;
    }

    /**
     * Modifie le texte du commentaire
     * @param texte Nouveau texte du commentaire
     */
    public void setTexte(String texte) {
        this.texte = texte;
    }

    /**
     * Retourne la date du commentaire
     * @return La date du commentaire
     */
    public Date getDateCommentaire() {
        return dateCommentaire;
    }

    /**
     * Modifie la date du commentaire
     * @param dateCommentaire Nouvelle date du commentaire
     */
    public void setDateCommentaire(Date dateCommentaire) {
        this.dateCommentaire = dateCommentaire;
    }

    /**
     * Retourne monUtilisateur
     * @return un utilisateur
     */
    public Utilisateur getMonUtilisateur() {
        return monUtilisateur;
    }

    /**
     * Modifie l'utilisateur
     * @param monUtilisateur Nouvel utilisateur
     */
    public void setMonUtilisateur(Utilisateur monUtilisateur) {
        this.monUtilisateur = monUtilisateur;
    }
}
