/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author wijde
 */

import java.util.Date;

public class Emprunt {
    private int id;
    private int livreId;
    private int utilisateurId;
    private Date dateEmprunt;
    private Date dateRetour;
    private String livreTitre; // Pour affichage
    private String utilisateurNom; // Pour affichage
    private String utilisateurPrenom; // Pour affichage

    // Constructeurs
    public Emprunt() {}

    public Emprunt(int livreId, int utilisateurId, Date dateEmprunt) {
        this.livreId = livreId;
        this.utilisateurId = utilisateurId;
        this.dateEmprunt = dateEmprunt;
    }

    public Emprunt(int id, int livreId, int utilisateurId, Date dateEmprunt, Date dateRetour) {
        this.id = id;
        this.livreId = livreId;
        this.utilisateurId = utilisateurId;
        this.dateEmprunt = dateEmprunt;
        this.dateRetour = dateRetour;
    }

    public Emprunt(int id, int id0, Date date, Object object) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    // Getters et setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLivreId() {
        return livreId;
    }

    public void setLivreId(int livreId) {
        this.livreId = livreId;
    }

    public int getUtilisateurId() {
        return utilisateurId;
    }

    public void setUtilisateurId(int utilisateurId) {
        this.utilisateurId = utilisateurId;
    }

    public Date getDateEmprunt() {
        return dateEmprunt;
    }

    public void setDateEmprunt(Date dateEmprunt) {
        this.dateEmprunt = dateEmprunt;
    }

    public Date getDateRetour() {
        return dateRetour;
    }

    public void setDateRetour(Date dateRetour) {
        this.dateRetour = dateRetour;
    }

    public String getLivreTitre() {
        return livreTitre;
    }

    public void setLivreTitre(String livreTitre) {
        this.livreTitre = livreTitre;
    }

    public String getUtilisateurNom() {
        return utilisateurNom;
    }

    public void setUtilisateurNom(String utilisateurNom) {
        this.utilisateurNom = utilisateurNom;
    }

    public String getUtilisateurPrenom() {
        return utilisateurPrenom;
    }

    public void setUtilisateurPrenom(String utilisateurPrenom) {
        this.utilisateurPrenom = utilisateurPrenom;
    }

    public boolean estRetourne() {
        return dateRetour != null;
    }
}