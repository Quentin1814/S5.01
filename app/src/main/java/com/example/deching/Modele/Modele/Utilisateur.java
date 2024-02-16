package com.example.deching.Modele.Modele;

import java.sql.Blob;
import java.sql.Date;
import java.util.ArrayList;

/**
 * Classe représentant un utilisateur
 */
public class Utilisateur {

    /**
     * Identifiant de l'utilisateur dans la base de données
     */
    private int id;

    /**
     * Nom de famille de l'utilisateur
     */
    private String nom;

    /**
     * Prénom de l'utilisateur
     */
    private String prenom;

    /**
     * Date de naissance de l'utilisateur
     */
    private Date dateNaiss;

    /**
     * Adresse mail de l'utilisateur
     */
    private String mail;

    /**
     * Nom d'utilisateur choisi par celui-ci et unique
     */
    private String username;

    /**Mot de passe de l'utilisateur pour se connecter
     *
     */
    private String mdp;

    /**
     * Numéro de téléphone de l'utilisateur
     */
    private String tel;

    /**Type de l'utilisateur (normal, école, groupe, collectivité)
     *
     */
    private String type;

    /**Photo du profil de l'utilisateur
     *
     */
    private Blob photoProfil;

    /**
     * Ensemble des événements auquel participe ou a participé l'utilisateur
     */
    private ArrayList<Evenement> mesEvenementsParticipes;

    /**
     * Ensemble des badges obtenus par l'utilisateur
     */
    private ArrayList<Badge> mesBadges;

    /**Ensemble des utilisateurs qui sont en contact avec l'utilisateur
     *
     */
    private ArrayList<Utilisateur> mesContacts;

    /**Ensemble des déchets qui ont été signalé par l'utilisateur
     *
     */
    private ArrayList<Dechet> mesDechets;

    /**
     * Constructeur de la classe Utilisateur
     * @param nom Nom de l'utilisateur
     * @param prenom Prénom de l'utilisateur
     * @param dateNaiss Date de Naissance de l'utilisateur
     * @param mail Adresse mail de l'utilisateur
     * @param mdp Mot de passe de l'utilisateur
     * @param tel Numéro de téléphone de l'utilisateur
     * @param type Type de l'utilisateur
     * @param photoProfil Photo de profil de l'utilisateur
     */
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
        this.mesEvenementsParticipes = new ArrayList<>();
        this.mesBadges = new ArrayList<>();
        this.mesContacts = new ArrayList<>();
        this.mesDechets = new ArrayList<>();
    }

    /**
     * Constructeur de la classe Utilisateur
     * @param nom Nom de l'utilisateur
     * @param prenom Prénom de l'utilisateur
     * @param dateNaiss Date de Naissance de l'utilisateur
     * @param mail Adresse mail de l'utilisateur
     * @param mdp Mot de passe de l'utilisateur
     * @param tel Numéro de téléphone de l'utilisateur
     * @param type Type de l'utilisateur
     * @param photoProfil Photo de profil de l'utilisateur
     */
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
        this.mesEvenementsParticipes = new ArrayList<>();
        this.mesBadges = new ArrayList<>();
        this.mesContacts = new ArrayList<>();
        this.mesDechets = new ArrayList<>();
    }

    /**
     * Retourne l'identifiant de l'utilisateur
     * @return L'identifiant de l'utilisateur
     */
    public int getId() {
        return id;
    }

    /**
     * Modifie l'identifiant de l'utilisateur
     * @param id Nouvel identifiant de l'utilisateur
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Retourne le nom de l'utilisateur
     * @return Le nom de l'utilisateur
     */
    public String getNom() {
        return nom;
    }

    /**
     * Modifie le nom de l'utilisateur
     * @param nom Nouveau nom de l'utilisateur
     */
    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
     * Retourne le prénom de l'utilisateur
     * @return
     */
    public String getPrenom() {
        return prenom;
    }

    /**
     * Modifie le prénom de l'utilisateur
     * @param prenom Le prénom de l'utilisateur
     */
    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    /**
     * Retourne la date de naissance de l'utilisateur
     * @return La date de naissance de l'utilisateur
     */
    public Date getDateNaiss() {
        return dateNaiss;
    }

    /**
     * Modifie la date de naissance de l'utilisateur
     * @param dateNaiss Nouvel date de naissance de l'utilisateur
     */
    public void setDateNaiss(Date dateNaiss) {
        this.dateNaiss = dateNaiss;
    }

    /**
     * Retourne l'adresse mail de l'utilisateur
     * @return L'adresse mail de l'utilisateur
     */
    public String getMail() {
        return mail;
    }

    /**
     * Modifie l'adresse mail de l'utilisateur
     * @param mail Nouvel adresse mail de l'utilisateur
     */
    public void setMail(String mail) {
        this.mail = mail;
    }

    /**
     * Retourne le mot de passe de l'utilisateur
     * @return Le mot de passe de l'utilisateur
     */
    public String getMdp() {
        return mdp;
    }

    /**
     * Modifie le mot de passe de l'utilisateur
     * @param mdp Nouveau mot de passe de l'utilisateur
     */
    public void setMdp(String mdp) {
        this.mdp = mdp;
    }

    /**
     * Retourne le numéro de téléphone de l'utilisateur
     * @return Le numéro de téléphone de l'utilisateur
     */
    public String getTel() {
        return tel;
    }

    /**
     * Modifie le numéro de téléphone de l'utilisateur
     * @param tel Nouveau numéro de téléphone de l'utilisateur
     */
    public void setTel(String tel) {
        this.tel = tel;
    }

    /**
     * Retourne le type de l'utilisateur
     * @return Le type de l'utilisateur
     */
    public String getType() {
        return type;
    }

    /**
     * Modifie le type de l'utilisateur
     * @param type Nouveau type de l'utilisateur
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Retourne la photo de profil de l'utilisateur
     * @return La photo de profil de l'utilisateur
     */
    public Blob getPhotoProfil() {
        return photoProfil;
    }

    /**
     * Modifie la photo de profil de l'utilisateur
     * @param photoProfil Nouvelle photo de profil de l'utilisateur
     */
    public void setPhotoProfil(Blob photoProfil) {
        this.photoProfil = photoProfil;
    }

    /**
     * Retourne la liste des événements de l'utilisateur
     * @return La liste des événements de l'utilisateur
     */
    public ArrayList<Evenement> getMesEvenementsParticipes() {
        return mesEvenementsParticipes;
    }

    /**
     * Modifie la liste des événements de l'utilisateur
     * @param mesEvenementsParticipes Nouvelle liste des événements de l'utilisateur
     */
    public void setMesEvenementsParticipes(ArrayList<Evenement> mesEvenementsParticipes) {
        this.mesEvenementsParticipes = mesEvenementsParticipes;
    }

    /**
     * Retourne la liste des badges de l'utilisateur
     * @return La liste des badges de l'utilisateur
     */
    public ArrayList<Badge> getMesBadges() {
        return mesBadges;
    }

    /**
     * Modifie la liste des badges de l'utilisateur
     * @param mesBadges Nouvelle liste des badges de l'utilisateur
     */
    public void setMesBadges(ArrayList<Badge> mesBadges) {
        this.mesBadges = mesBadges;
    }

    /**
     * Retourne la liste des contacts de l'utilisateur
     * @return La liste des contacts de l'utilisateur
     */
    public ArrayList<Utilisateur> getMesContacts() {
        return mesContacts;
    }

    /**
     * Modifie la liste des contacts de l'utilisateur
     * @param mesContacts Nouvelle liste des contacts de l'utilisateur
     */
    public void setMesContacts(ArrayList<Utilisateur> mesContacts) {
        this.mesContacts = mesContacts;
    }

    /**
     * Retourne la liste des déchets rapportés par l'utilisateur
     * @return La liste des déchets rapportés par l'utilisateur
     */
    public ArrayList<Dechet> getMesDechets() {
        return mesDechets;
    }

    /**
     * Modifie la liste des déchets rapportés par l'utilisateur
     * @param mesDechets Nouvelle liste des déchets rapportés par l'utilisateur
     */
    public void setMesDechets(ArrayList<Dechet> mesDechets) {
        this.mesDechets = mesDechets;
    }
}
