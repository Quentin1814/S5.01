package com.example.dechingv1.Modele;

import java.sql.Blob;
import java.sql.Date;
import java.util.ArrayList;

public class Utilisateur {
    private int id;//Identifiant de l'utilisateur dans la base de données
    private String nom;//Nom de l'utilisateur
    private String prenom;//Prenom de l'utilisateur
    private Date dateNaiss;//Date de naissance de l'utilisateur
    private String mail;//Adresse mail de l'utilisateur
    private String mdp;//Mot de passe de l'utilisateur pour se connecter
    private String tel;//Numéro de téléphone de l'utilisateur
    private String type;//Type de l'utilisateur (normal, école, groupe, collectivité)
    private Blob photoProfil;//Photo du profil de l'utilisateur
    private ArrayList<Evenement> mesEvenementsParticipes;//Ensemble des événements auquel participe ou a participé l'utilisateur
    private ArrayList<Badge> mesBadges;//Ensemble des badges obtenus par l'utilisateur
    private ArrayList<Utilisateur> mesContacts;//Ensemble des utilisateurs qui sont en contact avec l'utilisateur
    private ArrayList<Dechet> mesDechets;//Ensemble des déchets qui ont été signalé par l'utilisateur

    public Utilisateur(String nom, String prenom, Date dateNaiss, String mail, String mdp, String tel, String type, Blob photoProfil) {
        this.id=0;
        this.nom = nom;
        this.prenom = prenom;
        this.dateNaiss = dateNaiss;
        this.mail = mail;
        this.mdp = mdp;
        this.tel = tel;
        this.type = type;
        this.photoProfil = photoProfil;
        this.mesEvenementsParticipes = new ArrayList<Evenement>();
        this.mesBadges = new ArrayList<Badge>();
        this.mesContacts = new ArrayList<Utilisateur>();
        this.mesDechets = new ArrayList<Dechet>();
    }

    public Utilisateur(int id, String nom, String prenom, Date dateNaiss, String mail, String mdp, String tel, String type, Blob photoProfil) {
        this.id=id;
        this.nom = nom;
        this.prenom = prenom;
        this.dateNaiss = dateNaiss;
        this.mail = mail;
        this.mdp = mdp;
        this.tel = tel;
        this.type = type;
        this.photoProfil = photoProfil;
        this.mesEvenementsParticipes = new ArrayList<Evenement>();
        this.mesBadges = new ArrayList<Badge>();
        this.mesContacts = new ArrayList<Utilisateur>();
        this.mesDechets = new ArrayList<Dechet>();
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

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public Date getDateNaiss() {
        return dateNaiss;
    }

    public void setDateNaiss(Date dateNaiss) {
        this.dateNaiss = dateNaiss;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getMdp() {
        return mdp;
    }

    public void setMdp(String mdp) {
        this.mdp = mdp;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Blob getPhotoProfil() {
        return photoProfil;
    }

    public void setPhotoProfil(Blob photoProfil) {
        this.photoProfil = photoProfil;
    }

    public ArrayList<Evenement> getMesEvenementsParticipes() {
        return mesEvenementsParticipes;
    }

    public void setMesEvenementsParticipes(ArrayList<Evenement> mesEvenementsParticipes) {
        this.mesEvenementsParticipes = mesEvenementsParticipes;
    }

    public ArrayList<Badge> getMesBadges() {
        return mesBadges;
    }

    public void setMesBadges(ArrayList<Badge> mesBadges) {
        this.mesBadges = mesBadges;
    }

    public ArrayList<Utilisateur> getMesContacts() {
        return mesContacts;
    }

    public void setMesContacts(ArrayList<Utilisateur> mesContacts) {
        this.mesContacts = mesContacts;
    }

    public ArrayList<Dechet> getMesDechets() {
        return mesDechets;
    }

    public void setMesDechets(ArrayList<Dechet> mesDechets) {
        this.mesDechets = mesDechets;
    }
}
