package com.example.deching;

public class Evenement {
        private int id;
        private String nom;
        private String description;
        private int nbParticipantTotal;
        private String lieu;
        private String dateEvent;

        public Evenement(int id, String nom, String description, int nbParticipantTotal, String lieu, String dateEvent) {
            this.id = id;
            this.nom = nom;
            this.description = description;
            this.nbParticipantTotal = nbParticipantTotal;
            this.lieu = lieu;
            this.dateEvent = dateEvent;
        }

        // Getters et Setters
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

        public String getDateEvent() {
            return dateEvent;
        }

        public void setDateEvent(String dateEvent) {
            this.dateEvent = dateEvent;
        }

        // Méthodes BD
        public boolean getEvenement() {
            // Implémentez la récupération des données de l'événement depuis la base de données
            return false;
        }

        public boolean createEvenement() {
            // Implémentez la création de l'événement dans la base de données
            return false;
        }

        public boolean updateEvenement() {
            // Implémentez la mise à jour de l'événement dans la base de données
            return false;
        }

        public boolean deleteEvenement() {
            // Implémentez la suppression de l'événement de la base de données
            return false;
        }


}
