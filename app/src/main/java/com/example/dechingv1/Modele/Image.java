package com.example.dechingv1.Modele;

public class Image {
    private String id;//Id de l'image dans la base de données
    private String chemin;//Chemin absolu de l'image

    public Image(String id, String chemin) {
        this.id = id;
        this.chemin = chemin;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getChemin() {
        return chemin;
    }

    public void setChemin(String chemin) {
        this.chemin = chemin;
    }

}
