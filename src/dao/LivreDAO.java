/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

/**
 *
 * @author wijde
 */

import model.Livre;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LivreDAO {
    private final Connection connection;

    public LivreDAO() throws SQLException {
        // Remplacez par vos paramètres de connexion
        String url = "jdbc:mysql://localhost:3306/bibliotheque";
        String user = "root";
        String password = "";
        this.connection = DriverManager.getConnection(url, user, password);
    }

    public List<Livre> getAllLivres() throws SQLException {
        List<Livre> livres = new ArrayList<>();
        String query = "SELECT * FROM livres";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                livres.add(new Livre(
                    rs.getInt("id"),
                    rs.getString("titre"),
                    rs.getString("auteur"),
                    rs.getString("isbn"),
                    rs.getInt("annee_publication"),
                    rs.getInt("exemplaires_disponibles")
                ));
            }
        }
        return livres;
    }

    public List<Livre> rechercherLivres(String terme) throws SQLException {
        List<Livre> livres = new ArrayList<>();
        String query = "SELECT * FROM livres WHERE titre LIKE ? OR auteur LIKE ? OR isbn LIKE ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            String likeTerm = "%" + terme + "%";
            stmt.setString(1, likeTerm);
            stmt.setString(2, likeTerm);
            stmt.setString(3, likeTerm);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    livres.add(new Livre(
                        rs.getInt("id"),
                        rs.getString("titre"),
                        rs.getString("auteur"),
                        rs.getString("isbn"),
                        rs.getInt("annee_publication"),
                        rs.getInt("exemplaires_disponibles")
                    ));
                }
            }
        }
        return livres;
    }

    public void ajouterLivre(Livre livre) throws SQLException {
        String query = "INSERT INTO livres (titre, auteur, isbn, annee_publication, exemplaires_disponibles) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, livre.getTitre());
            stmt.setString(2, livre.getAuteur());
            stmt.setString(3, livre.getIsbn());
            stmt.setInt(4, livre.getAnneePublication());
            stmt.setInt(5, livre.getExemplairesDisponibles());
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    livre.setId(rs.getInt(1));
                }
            }
        }
    }

    public void modifierLivre(Livre livre) throws SQLException {
        String query = "UPDATE livres SET titre = ?, auteur = ?, isbn = ?, annee_publication = ?, exemplaires_disponibles = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, livre.getTitre());
            stmt.setString(2, livre.getAuteur());
            stmt.setString(3, livre.getIsbn());
            stmt.setInt(4, livre.getAnneePublication());
            stmt.setInt(5, livre.getExemplairesDisponibles());
            stmt.setInt(6, livre.getId());
            stmt.executeUpdate();
        }
    }

    public void supprimerLivre(int id) throws SQLException {
        String query = "DELETE FROM livres WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public List<Livre> getLivresDisponibles() throws SQLException {
        List<Livre> livres = new ArrayList<>();
        String query = "SELECT * FROM livres WHERE exemplaires_disponibles > 0";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                livres.add(new Livre(
                    rs.getInt("id"),
                    rs.getString("titre"),
                    rs.getString("auteur"),
                    rs.getString("isbn"),
                    rs.getInt("annee_publication"),
                    rs.getInt("exemplaires_disponibles")
                ));
            }
        }
        return livres;
    }

    public void decrementerExemplaires(int livreId) throws SQLException {
        String query = "UPDATE livres SET exemplaires_disponibles = exemplaires_disponibles - 1 WHERE id = ? AND exemplaires_disponibles > 0";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, livreId);
            stmt.executeUpdate();
        }
    }

    public void incrementerExemplaires(int livreId) throws SQLException {
        String query = "UPDATE livres SET exemplaires_disponibles = exemplaires_disponibles + 1 WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, livreId);
            stmt.executeUpdate();
        }
    }
}