package com.example.deching.Modele.Modele;

/**
 * Classe représentant un déchet
 */
public class Dechet {
    /**
     * Identifiant du déchet dans la base de données
     */
    private int id;

    /**
     * Latitude de la position du déchet
     */
    private double latitude;

    /**
     * Longitude de la position du déchet
     */
    private double longitude;

    /**
     * Taille du déchet : grand, moyen, petit
     */
    private String taille;

    /**
     * Description optionnelle ajoutée lors de la description d'un déchet
     */
    private String description;

    /**
     * Constructeur de la classe Dechet
     * @param latitude Latitude de la position du déchet
     * @param longitude Longitude de la position du déchet
     * @param taille Taille du déchet : grand, moyen, petit
     * @param description Description optionnelle ajoutée lors de la description d'un déchet
     */
    public Dechet( double latitude, double longitude, String taille, String description) {
        this.id=0;
        this.latitude = latitude;
        this.longitude = longitude;
        this.taille=taille;
        this.description = description;
    }

    /**
     * Constructeur de la classe Dechet
     * @param latitude Latitude de la position du déchet
     * @param longitude Longitude de la position du déchet
     * @param taille Taille du déchet : grand, moyen, petit
     * @param description Description optionnelle ajoutée lors de la description d'un déchet
     */
    public Dechet(int id, double latitude, double longitude, String taille, String description) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.taille=taille;
        this.description = description;
        this.monUtilisateur = monUtilisateur;
    }

    /**
     * Retourne l'identifiant du déchet
     * @return L'identifiant du déchet
     */
    public int getId(){
        return this.id;
    }

    /**
     * Modifie l'identifiant du déchet
     * @param id Nouvel identifiant du déchet
     */
    public void setId(int id){
        this.id = id;
    }

    /**
     * Retourne la longitude du déchet
     * @return La longitude du déchet
     */
    public double getLongitude(){
        return this.longitude;
    }

    /**
     * Modifie la longitude du déchet
     * @param longitude Nouvelle longitude du déchet
     */
    public void setLongitude(double longitude){
        this.longitude = longitude;
    }

    /**
     * Retourne la latitude du déchet
     * @return La latitude du déchet
     */
    public double getLatitude(){
        return this.latitude;
    }

    /**
     * Modifie la latitude du déchet
     * @param latitude Nouvelle latitude du déchet
     */
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    /**
     * Retourne la taille du déchet
     * @return La taille du déchet
     */
    public String getTaille(){return this.taille;}

    /**
     * Modifie la taille du déchet
     * @param taille Nouvelle taille du déchet
     */
    public void setTaille(String taille) { this.taille=taille;}

    /**
     * Retourne la description du déchet
     * @return La description d'un déchet
     */
    public String getDescription(){
        return this.description;
    }

    /**
     * Modifie la description du déchet
     * @param description Nouvelle description du déchet
     */
    public void setDescription(String description) {
        this.description = description;
    }

}
