/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author wijde
 */


public class Livre {
    private int id;
    private String titre;
    private String auteur;
    private String isbn;
    private int anneePublication;
    private int exemplairesDisponibles;

    // Constructeur par défaut
    public Livre() {}

    // Constructeur avec paramètres
    public Livre(String titre, String auteur, String isbn, int anneePublication, int exemplairesDisponibles) {
        this.titre = titre;
        this.auteur = auteur;
        this.isbn = isbn;
        this.anneePublication = anneePublication;
        this.exemplairesDisponibles = exemplairesDisponibles;
    }

    // Constructeur complet
    public Livre(int id, String titre, String auteur, String isbn, int anneePublication, int exemplairesDisponibles) {
        this.id = id;
        this.titre = titre;
        this.auteur = auteur;
        this.isbn = isbn;
        this.anneePublication = anneePublication;
        this.exemplairesDisponibles = exemplairesDisponibles;
    }

    // Getters et setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getAuteur() {
        return auteur;
    }

    public void setAuteur(String auteur) {
        this.auteur = auteur;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public int getAnneePublication() {
        return anneePublication;
    }

    public void setAnneePublication(int anneePublication) {
        this.anneePublication = anneePublication;
    }

    public int getExemplairesDisponibles() {
        return exemplairesDisponibles;
    }

    public void setExemplairesDisponibles(int exemplairesDisponibles) {
        this.exemplairesDisponibles = exemplairesDisponibles;
    }

    @Override
    public String toString() {
        return titre + " par " + auteur + " (" + anneePublication + ")";
    }
}