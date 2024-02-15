package com.example.deching.Modele.Modele;

/**
 * Classe représentant une image
 */
public class Image {

    /**
     * Identifiant de l'image dans la base de données
     */
    private String id;

    /**
     * Chemin absolu de l'image
     */
    private String chemin;

    /**
     * Constructeur de la classe Image
     * @param id Identifiant de l'image
     * @param chemin Chemin absolu de l'image
     */
    public Image(String id, String chemin) {
        this.id = id;
        this.chemin = chemin;
    }

    /**
     * Retour l'identifiant de l'image
     * @return L'identifiant de l'image
     */
    public String getId() {
        return id;
    }

    /**
     * Modifie l'identifiant de l'image
     * @param id Nouvel identifiant de l'image
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Retourne le chemin absolu de l'image
     * @return Le chemin absolu de l'image
     */
    public String getChemin() {
        return chemin;
    }

    /**
     * Modifie le chemin absolu de l'image
     * @param chemin Nouveau chemin absolu de l'image
     */
    public void setChemin(String chemin) {
        this.chemin = chemin;
    }

}
