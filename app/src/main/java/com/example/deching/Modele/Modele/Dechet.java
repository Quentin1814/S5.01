package com.example.deching.Modele.Modele;


public class Dechet {
    private int id;//Id du déchet dans la base de données
    private double latitude;//Latitude de la position du déchet
    private double longitude;//Longitude de la position du déchet
    private String taille;//Taille du déchet : grand, moyen, petit
    private String description;//Description optionnelle ajoutée lors de la description d'un déchet
    private Utilisateur monUtilisateur;//Id de l'utilisateur qui a signalé le déchet
    private Evenement monEvenement;//Evenement associé à ce déchet


    public Dechet(double latitude, double longitude, String taille, String description) {
        this.monUtilisateur = monUtilisateur;
        this.id=0;
        this.latitude = latitude;
        this.longitude = longitude;
        this.taille=taille;
        this.description = description;
    }

    public Dechet(int id, double latitude, double longitude, String taille, String description) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.taille=taille;
        this.description = description;
        this.monUtilisateur = monUtilisateur;
    }

    public int getId(){
        return this.id;
    }

    public void setId(int id){
        this.id = id;
    }

    public double getLongitude(){
        return this.longitude;
    }

    public void setLongitude(double longitude){
        this.longitude = longitude;
    }

    public double getLatitude(){
        return this.latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getTaille(){return this.taille;}

    public void setTaille(String taille) { this.taille=taille;}

    public String getDescription(){
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


}
