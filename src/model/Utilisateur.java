/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author wijde
 */


public class Utilisateur {
    private int id;
    private String nom;
    private String prenom;
    private String numeroAdherent;

    // Constructeurs
    public Utilisateur() {}

    public Utilisateur(String nom, String prenom, String numeroAdherent) {
        this.nom = nom;
        this.prenom = prenom;
        this.numeroAdherent = numeroAdherent;
    }

    public Utilisateur(int id, String nom, String prenom, String numeroAdherent) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.numeroAdherent = numeroAdherent;
    }

    // Getters et setters
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

    public String getNumeroAdherent() {
        return numeroAdherent;
    }

    public void setNumeroAdherent(String numeroAdherent) {
        this.numeroAdherent = numeroAdherent;
    }

    @Override
    public String toString() {
        return prenom + " " + nom + " (" + numeroAdherent + ")";
    }
}