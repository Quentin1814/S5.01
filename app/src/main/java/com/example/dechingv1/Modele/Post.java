package com.example.dechingv1.Modele;

import java.sql.Date;
import java.util.ArrayList;

public class Post {
    private String id;//Identifiant du post dans la base de données
    private String description;//Description du post
    private String lieu;//Adresse du lieu du post
    private Date datePost;//Date de publication du post
    private Boolean estVisible;//Définit si le post est publique(true=visible par tous) ou privé(false=visible pour les amis uniquement)
    private ArrayList<Commentaire> mesCommentaires;//Liste des commentaires qui ont été écrit pour ce post
    private ArrayList<Image> mesImages;//Liste des images défilant pour le post

    public Post(String id, String description, String lieu, Date datePost, Boolean estVisible) {
        this.id = id;
        this.description = description;
        this.lieu = lieu;
        this.datePost = datePost;
        this.estVisible = estVisible;
        this.mesCommentaires = new ArrayList<Commentaire>();
        this.mesImages = new ArrayList<Image>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLieu() {
        return lieu;
    }

    public void setLieu(String lieu) {
        this.lieu = lieu;
    }

    public Date getDatePost() {
        return datePost;
    }

    public void setDatePost(Date datePost) {
        this.datePost = datePost;
    }

    public Boolean getEstVisible() {
        return estVisible;
    }

    public void setEstVisible(Boolean estVisible) {
        this.estVisible = estVisible;
    }

    public ArrayList<Commentaire> getMesCommentaires() {
        return mesCommentaires;
    }

    public void setMesCommentaires(ArrayList<Commentaire> mesCommentaires) {
        this.mesCommentaires = mesCommentaires;
    }

    public ArrayList<Image> getMesImages() {
        return mesImages;
    }

    public void setMesImages(ArrayList<Image> mesImages) {
        this.mesImages = mesImages;
    }
}
