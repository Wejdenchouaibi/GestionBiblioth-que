/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

/**
 *
 * @author wijde
 */


import model.Utilisateur;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UtilisateurDAO {
    private final Connection connection;

    public UtilisateurDAO() throws SQLException {
        // Remplacez par vos paramètres de connexion
        String url = "jdbc:mysql://localhost:3306/bibliotheque";
        String user = "root";
        String password = "";
        this.connection = DriverManager.getConnection(url, user, password);
    }

    public List<Utilisateur> getAllUtilisateurs() throws SQLException {
        List<Utilisateur> utilisateurs = new ArrayList<>();
        String query = "SELECT * FROM utilisateurs";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                utilisateurs.add(new Utilisateur(
                    rs.getInt("id"),
                    rs.getString("nom"),
                    rs.getString("prenom"),
                    rs.getString("numero_adherent")
                ));
            }
        }
        return utilisateurs;
    }

    public List<Utilisateur> rechercherUtilisateurs(String terme) throws SQLException {
        List<Utilisateur> utilisateurs = new ArrayList<>();
        String query = "SELECT * FROM utilisateurs WHERE nom LIKE ? OR prenom LIKE ? OR numero_adherent LIKE ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            String likeTerm = "%" + terme + "%";
            stmt.setString(1, likeTerm);
            stmt.setString(2, likeTerm);
            stmt.setString(3, likeTerm);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    utilisateurs.add(new Utilisateur(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("numero_adherent")
                    ));
                }
            }
        }
        return utilisateurs;
    }

    public void ajouterUtilisateur(Utilisateur utilisateur) throws SQLException {
        String query = "INSERT INTO utilisateurs (nom, prenom, numero_adherent) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, utilisateur.getNom());
            stmt.setString(2, utilisateur.getPrenom());
            stmt.setString(3, utilisateur.getNumeroAdherent());
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    utilisateur.setId(rs.getInt(1));
                }
            }
        }
    }

    public void modifierUtilisateur(Utilisateur utilisateur) throws SQLException {
        String query = "UPDATE utilisateurs SET nom = ?, prenom = ?, numero_adherent = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, utilisateur.getNom());
            stmt.setString(2, utilisateur.getPrenom());
            stmt.setString(3, utilisateur.getNumeroAdherent());
            stmt.setInt(4, utilisateur.getId());
            stmt.executeUpdate();
        }
    }

    public void supprimerUtilisateur(int id) throws SQLException {
        String query = "DELETE FROM utilisateurs WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}