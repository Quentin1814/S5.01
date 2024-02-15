package com.example.deching.Modele.Modele;

import java.sql.Date;
import java.util.ArrayList;

/**
 * Classe représentant un post
 */
public class Post {
    /**
     * Identifiant du post dans la base de données
     */
    private String id;

    /**
     * Description du post
     */
    private String description;

    /**
     * Adresse du lieu du post
     */
    private String lieu;

    /**
     * Date de publication du post
     */
    private Date datePost;

    /**
     * Définit si le post est publique(true=visible par tous) ou privé(false=visible pour les amis uniquement)
     */
    private Boolean estVisible;

    /**
     * Liste des commentaires qui ont été écrit pour ce post
     */
    private ArrayList<Commentaire> mesCommentaires;

    /**
     * Liste des images défilant pour le post
     */
    private ArrayList<Image> mesImages;

    /**
     * Constructeur de la classe Post
     * @param id Identifiant du post
     * @param description Description du post
     * @param lieu Lieu du post
     * @param datePost Date de publication du post
     * @param estVisible Visibilité du post
     */
    public Post(String id, String description, String lieu, Date datePost, Boolean estVisible) {
        this.id = id;
        this.description = description;
        this.lieu = lieu;
        this.datePost = datePost;
        this.estVisible = estVisible;
        this.mesCommentaires = new ArrayList<>();
        this.mesImages = new ArrayList<>();
    }

    /**
     * Retourne l'identifiant du post
     * @return L'identifiant du post
     */
    public String getId() {
        return id;
    }

    /**
     * Modifie l'identifiant du post
     * @param id Nouvel identifiant du post
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Retourne la description du post
     * @return La description du post
     */
    public String getDescription() {
        return description;
    }

    /**
     * Modifie la description du post
     * @param description Nouvelle description du post
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Retourne le lieu du post
     * @return Le lieu du post
     */
    public String getLieu() {
        return lieu;
    }

    /**
     * Modifie le lieu du post
     * @param lieu Nouveau lieu de post
     */
    public void setLieu(String lieu) {
        this.lieu = lieu;
    }

    /**
     * Retourne la date de publication du post
     * @return La date de publication du post
     */
    public Date getDatePost() {
        return datePost;
    }

    /**
     * Modifie la date de publication du post
     * @param datePost Nouvelle date de publication du post
     */
    public void setDatePost(Date datePost) {
        this.datePost = datePost;
    }

    /**
     * Retourne vrai ou faux en fonction de si le post doit être visible sous certaines conditions
     * @return vrai ou faux en fonction de si le post doit être visible sous certaines conditions
     */
    public Boolean getEstVisible() {
        return estVisible;
    }

    /**
     * Modifie la valeur du booléen estVisible par vrai ou faux en fonction de si le post doit être visible sous certaines conditions
     * @param estVisible
     */
    public void setEstVisible(Boolean estVisible) {
        this.estVisible = estVisible;
    }

    /**
     * Retourne la liste des commentaires d'un post
     * @return La liste des commentaires d'un post
     */
    public ArrayList<Commentaire> getMesCommentaires() {
        return mesCommentaires;
    }

    /**
     * Modifie la liste des commentaires d'un post
     * @param mesCommentaires Nouvelle liste des commentaires d'un post
     */
    public void setMesCommentaires(ArrayList<Commentaire> mesCommentaires) {
        this.mesCommentaires = mesCommentaires;
    }

    /**
     * Retourne la liste des images d'un post
     * @return La liste des images d'un post
     */
    public ArrayList<Image> getMesImages() {
        return mesImages;
    }

    /**
     * Modifie la liste des images d'un post
     * @param mesImages Nouvelle liste des images d'un post
     */
    public void setMesImages(ArrayList<Image> mesImages) {
        this.mesImages = mesImages;
    }
}
