/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ui;

/**
 *
 * @author wijde
 */


import dao.UtilisateurDAO;
import model.Utilisateur;
import model.UtilisateurTableModel;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GestionUtilisateursPanel extends JPanel {
    private final UtilisateurDAO utilisateurDAO;
    private final JTable tableUtilisateurs;
    private final JTextField champRecherche;

    public GestionUtilisateursPanel() throws SQLException {
        this.utilisateurDAO = new UtilisateurDAO();
        this.tableUtilisateurs = new JTable();
        this.champRecherche = new JTextField(20);
        initUI();
        chargerUtilisateurs();
    }

    private void initUI() {
        setLayout(new BorderLayout());
        add(createTopPanel(), BorderLayout.NORTH);
        add(new JScrollPane(tableUtilisateurs), BorderLayout.CENTER);
    }

    private JPanel createTopPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        // Boutons d'action
        String[] buttonLabels = {"Ajouter", "Modifier", "Supprimer"};
        ActionListener[] actions = {
            this::ajouterUtilisateur,
            this::modifierUtilisateur,
            this::supprimerUtilisateur
        };

        for (int i = 0; i < buttonLabels.length; i++) {
            panel.add(createButton(buttonLabels[i], actions[i]));
        }

        // Composant de recherche
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(new JLabel("Recherche : "));
        searchPanel.add(champRecherche);
        searchPanel.add(createButton("Rechercher", this::rechercherUtilisateurs));
        panel.add(searchPanel);

        return panel;
    }

    private JButton createButton(String text, ActionListener listener) {
        JButton button = new JButton(text);
        button.setMargin(new Insets(5, 10, 5, 10));
        button.addActionListener(listener);
        return button;
    }

    private void chargerUtilisateurs() {
        try {
            List<Utilisateur> utilisateurs = utilisateurDAO.getAllUtilisateurs();
            tableUtilisateurs.setModel(new UtilisateurTableModel(utilisateurs));
        } catch (SQLException e) {
            showError("Erreur lors du chargement des utilisateurs", e);
        }
    }

    private void rechercherUtilisateurs(ActionEvent e) {
        String terme = champRecherche.getText().trim();
        try {
            List<Utilisateur> utilisateurs = utilisateurDAO.rechercherUtilisateurs(terme);
            ((UtilisateurTableModel) tableUtilisateurs.getModel()).updateData(utilisateurs);
        } catch (SQLException ex) {
            showError("Erreur lors de la recherche", ex);
        }
    }

    private void ajouterUtilisateur(ActionEvent e) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Ajouter un utilisateur", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);

        JPanel formPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        JTextField nomField = new JTextField();
        JTextField prenomField = new JTextField();
        JTextField numeroAdherentField = new JTextField();

        formPanel.add(new JLabel("Nom :"));
        formPanel.add(nomField);
        formPanel.add(new JLabel("Prénom :"));
        formPanel.add(prenomField);
        formPanel.add(new JLabel("Numéro Adhérent :"));
        formPanel.add(numeroAdherentField);

        JButton btnValider = new JButton("Valider");
        btnValider.addActionListener(ev -> {
            try {
                Utilisateur nouvelUtilisateur = new Utilisateur(
                    nomField.getText(),
                    prenomField.getText(),
                    numeroAdherentField.getText()
                );
                utilisateurDAO.ajouterUtilisateur(nouvelUtilisateur);
                chargerUtilisateurs();
                dialog.dispose();
            } catch (SQLException ex) {
                showError("Erreur lors de l'ajout", ex);
            }
        });

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(btnValider, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void modifierUtilisateur(ActionEvent e) {
        int selectedRow = tableUtilisateurs.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un utilisateur", "Avertissement", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Utilisateur utilisateur = ((UtilisateurTableModel) tableUtilisateurs.getModel()).getUtilisateurAt(selectedRow);
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Modifier un utilisateur", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);

        JPanel formPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        JTextField nomField = new JTextField(utilisateur.getNom());
        JTextField prenomField = new JTextField(utilisateur.getPrenom());
        JTextField numeroAdherentField = new JTextField(utilisateur.getNumeroAdherent());

        formPanel.add(new JLabel("Nom :"));
        formPanel.add(nomField);
        formPanel.add(new JLabel("Prénom :"));
        formPanel.add(prenomField);
        formPanel.add(new JLabel("Numéro Adhérent :"));
        formPanel.add(numeroAdherentField);

        JButton btnValider = new JButton("Valider");
        btnValider.addActionListener((ActionEvent ev) -> {
            utilisateur.setNom(nomField.getText());
            utilisateur.setPrenom(prenomField.getText());
            utilisateur.setNumeroAdherent(numeroAdherentField.getText());
            try {
                utilisateurDAO.modifierUtilisateur(utilisateur);
            } catch (SQLException ex) {
                Logger.getLogger(GestionUtilisateursPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
            chargerUtilisateurs();
            dialog.dispose();
        });

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(btnValider, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void supprimerUtilisateur(ActionEvent e) {
        int selectedRow = tableUtilisateurs.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un utilisateur", "Avertissement", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Utilisateur utilisateur = ((UtilisateurTableModel) tableUtilisateurs.getModel()).getUtilisateurAt(selectedRow);
            int confirm = JOptionPane.showConfirmDialog(this,
                "Êtes-vous sûr de vouloir supprimer '" + utilisateur.getPrenom() + " " + utilisateur.getNom() + "' ?",
                "Confirmation", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                utilisateurDAO.supprimerUtilisateur(utilisateur.getId());
                chargerUtilisateurs();
            }
        } catch (SQLException ex) {
            showError("Erreur lors de la suppression", ex);
        }
    }

    private void showError(String message, Exception e) {
        JOptionPane.showMessageDialog(this, message + ": " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
    }
}