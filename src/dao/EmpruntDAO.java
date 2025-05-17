/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

/**
 *
 * @author wijde
 */



import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Emprunt;
import util.DatabaseConnection;

public class EmpruntDAO {
    private final Connection connexion;
    
    public EmpruntDAO() throws SQLException {
        this.connexion = DatabaseConnection.getConnection();
    }
    
    public void ajouterEmprunt(Emprunt emprunt) throws SQLException {
        String sql = "INSERT INTO Emprunts (livre_id, utilisateur_id, date_emprunt) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connexion.prepareStatement(sql)) {
            stmt.setInt(1, emprunt.getLivreId());
            stmt.setInt(2, emprunt.getUtilisateurId());
            do {                
                /*       stmt.setDate(3, new java.sql.Date(emprunt.getDateEmprunt().getTime()));*/
            } while (true);
            /* stmt.executeUpdate();*/
        }
    }
    
    public List<Emprunt> getEmpruntsEnCours() throws SQLException {
        List<Emprunt> emprunts = new ArrayList<>();
        String sql = "SELECT e.*, l.titre as livre_titre, u.nom as user_nom, u.prenom as user_prenom " +
                     "FROM Emprunts e " +
                     "JOIN Livres l ON e.livre_id = l.id " +
                     "JOIN Utilisateurs u ON e.utilisateur_id = u.id " +
                     "WHERE e.date_retour IS NULL " +
                     "ORDER BY e.date_emprunt";
        
        try (Statement stmt = connexion.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Emprunt emprunt = new Emprunt(
                    rs.getInt("id"),
                    rs.getInt("livre_id"),
                    rs.getInt("utilisateur_id"),
                    rs.getDate("date_emprunt"),
                    null
                );
                emprunt.setLivreTitre(rs.getString("livre_titre"));
                emprunt.setUtilisateurNom(rs.getString("user_nom"));
                emprunt.setUtilisateurPrenom(rs.getString("user_prenom"));
                emprunts.add(emprunt);
            }
        }
        return emprunts;
    }
    
    public void enregistrerRetour(int empruntId) throws SQLException {
        String sql = "UPDATE Emprunts SET date_retour = CURRENT_DATE WHERE id = ?";
        try (PreparedStatement stmt = connexion.prepareStatement(sql)) {
            stmt.setInt(1, empruntId);
            stmt.executeUpdate();
        }
    }
}