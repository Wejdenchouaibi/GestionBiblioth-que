/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ui;

/**
 *
 * @author wijde
 */


import dao.EmpruntDAO;
import dao.LivreDAO;
import dao.UtilisateurDAO;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.sql.SQLException;
import java.util.Date;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import model.Emprunt;
import model.EmpruntTableModel;
import model.Livre;
import model.Utilisateur;

public class GestionEmpruntsPanel extends JPanel {
    
    private final EmpruntDAO empruntDAO;
    private final LivreDAO livreDAO;
    private final UtilisateurDAO utilisateurDAO;
    private final JTable tableEmprunts;
    
    public GestionEmpruntsPanel() throws SQLException {
        this.empruntDAO = new EmpruntDAO();
        this.livreDAO = new LivreDAO();
        this.utilisateurDAO = new UtilisateurDAO();
        this.tableEmprunts = new JTable();
        
        initUI();
        chargerEmprunts();
    }
    
    private void initUI() {
        setLayout(new BorderLayout());
        
        // Panel des boutons
        JPanel buttonPanel = new JPanel();
        
        JButton btnNouvelEmprunt = new JButton("Nouvel Emprunt");
        JButton btnRetourner = new JButton("Enregistrer Retour");
        JButton btnRafraichir = new JButton("Rafraîchir");
        
        btnNouvelEmprunt.addActionListener(e -> ouvrirDialogueNouvelEmprunt());
        btnRetourner.addActionListener(e -> enregistrerRetour());
        btnRafraichir.addActionListener(e -> chargerEmprunts());
        
        buttonPanel.add(btnNouvelEmprunt);
        buttonPanel.add(btnRetourner);
        buttonPanel.add(btnRafraichir);
        
        // Tableau des emprunts
        tableEmprunts.setAutoCreateRowSorter(true);
        JScrollPane scrollPane = new JScrollPane(tableEmprunts);
        
        add(buttonPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }
    
    private void chargerEmprunts() {
        try {
            tableEmprunts.setModel(new EmpruntTableModel(empruntDAO.getEmpruntsEnCours()));
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, 
                "Erreur lors du chargement des emprunts: " + e.getMessage(),
                "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void ouvrirDialogueNouvelEmprunt() {
        JDialog dialog = new JDialog(
            (java.awt.Frame)SwingUtilities.getWindowAncestor(this),
            "Nouvel Emprunt",
            true
        );
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);
        
        JPanel panel = new JPanel(new GridLayout(4, 2, 5, 5));
        
        // Combo box pour les utilisateurs
        JComboBox<Utilisateur> comboUtilisateurs = new JComboBox<>();
        try {
            utilisateurDAO.getAllUtilisateurs().forEach(comboUtilisateurs::addItem);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(dialog, 
                "Erreur lors du chargement des utilisateurs",
                "Erreur", JOptionPane.ERROR_MESSAGE);
        }
        
        // Combo box pour les livres disponibles
        JComboBox<Livre> comboLivres = new JComboBox<>();
        try {
            livreDAO.getLivresDisponibles().forEach(comboLivres::addItem);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(dialog, 
                "Erreur lors du chargement des livres",
                "Erreur", JOptionPane.ERROR_MESSAGE);
        }
        
        panel.add(new JLabel("Utilisateur:"));
        panel.add(comboUtilisateurs);
        panel.add(new JLabel("Livre:"));
        panel.add(comboLivres);
        
        JButton btnValider = new JButton("Valider");
        btnValider.addActionListener(e -> {
            Utilisateur utilisateur = (Utilisateur)comboUtilisateurs.getSelectedItem();
            Livre livre = (Livre)comboLivres.getSelectedItem();
            
            if (utilisateur != null && livre != null) {
                try {
                    Emprunt emprunt = new Emprunt(
                        livre.getId(),
                        utilisateur.getId(),
                        new Date(),
                        null
                    );
                    
                    empruntDAO.ajouterEmprunt(emprunt);
                    livreDAO.decrementerExemplaires(livre.getId());
                    chargerEmprunts();
                    dialog.dispose();
                    
                    JOptionPane.showMessageDialog(this,
                        "Emprunt enregistré avec succès",
                        "Succès", JOptionPane.INFORMATION_MESSAGE);
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this,
                        "Erreur lors de l'enregistrement: " + ex.getMessage(),
                        "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        dialog.add(panel, BorderLayout.CENTER);
        dialog.add(btnValider, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }
    
    private void enregistrerRetour() {
        int selectedRow = tableEmprunts.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Veuillez sélectionner un emprunt",
                "Avertissement", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        Emprunt emprunt = ((EmpruntTableModel)tableEmprunts.getModel()).getEmpruntAt(selectedRow);
        
        /*              if (emprunt.getDateRetour() != null) {
        JOptionPane.showMessageDialog(this,
        "Cet emprunt a déjà été retourné",
        "Information", JOptionPane.INFORMATION_MESSAGE);
        return;
        }
        */
        int confirm = JOptionPane.showConfirmDialog(this,
            "Confirmer le retour du livre '" + emprunt.getLivreTitre() + "' ?",
            "Confirmation", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                empruntDAO.enregistrerRetour(emprunt.getId());
                livreDAO.incrementerExemplaires(emprunt.getLivreId());
                chargerEmprunts();
                
                JOptionPane.showMessageDialog(this,
                    "Retour enregistré avec succès",
                    "Succès", JOptionPane.INFORMATION_MESSAGE);
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this,
                    "Erreur lors de l'enregistrement: " + e.getMessage(),
                    "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}