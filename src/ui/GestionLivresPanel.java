/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ui;

/**
 *
 * @author wijde
 */




import dao.LivreDAO;
import model.Livre;
import model.LivreTableModel;
import javax.swing.*;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;

public class GestionLivresPanel extends JPanel {
    private final LivreDAO livreDAO;
    private final JTable tableLivres;
    private final JTextField champRecherche;

    public GestionLivresPanel() throws SQLException {
        this.livreDAO = new LivreDAO();
        this.tableLivres = new JTable();
        this.champRecherche = new JTextField(20);
        initUI();
        chargerLivres();
    }

    private void initUI() {
        setLayout(new BorderLayout());
        add(createTopPanel(), BorderLayout.NORTH);
        add(new JScrollPane(tableLivres), BorderLayout.CENTER);
        tableLivres.setAutoCreateRowSorter(true); // Activer le tri
    }

    private JPanel createTopPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        addActionButtons(panel);
        addSearchComponent(panel);
        return panel;
    }

    private void addActionButtons(JPanel panel) {
        String[] buttonLabels = {"Ajouter", "Modifier", "Supprimer"};
        ActionListener[] actions = {
            e -> ajouterLivre(e),
            e -> modifierLivre(e),
            e -> supprimerLivre()
        };

        for (int i = 0; i < buttonLabels.length; i++) {
            panel.add(createButton(buttonLabels[i], actions[i]));
        }
    }

    private void addSearchComponent(JPanel panel) {
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(new JLabel("Recherche : "));
        searchPanel.add(champRecherche);
        searchPanel.add(createButton("Rechercher", e -> rechercherLivres(e)));
        panel.add(searchPanel);
    }

    private JButton createButton(String text, ActionListener listener) {
        JButton button = new JButton(text);
        button.setMargin(new Insets(5, 10, 5, 10));
        button.addActionListener(listener);
        return button;
    }

    private void chargerLivres() {
        try {
            List<Livre> livres = livreDAO.getAllLivres();
            LivreTableModel model = new LivreTableModel(livres);
            tableLivres.setModel(model);
            tableLivres.setRowSorter(new TableRowSorter<>(model));
        } catch (SQLException e) {
            showError("Erreur lors du chargement des livres", e);
        }
    }

    private void rechercherLivres(ActionEvent e) {
        String terme = champRecherche.getText().trim();
        try {
            List<Livre> livres = livreDAO.rechercherLivres(terme);
            ((LivreTableModel) tableLivres.getModel()).updateData(livres);
        } catch (SQLException ex) {
            showError("Erreur lors de la recherche", ex);
        }
    }

    private void ajouterLivre(ActionEvent e) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Ajouter un livre", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);

        JPanel formPanel = new JPanel(new GridLayout(6, 2, 5, 5));
        JTextField titreField = new JTextField();
        JTextField auteurField = new JTextField();
        JTextField isbnField = new JTextField();
        JTextField anneeField = new JTextField();
        JTextField exemplairesField = new JTextField();

        formPanel.add(new JLabel("Titre :"));
        formPanel.add(titreField);
        formPanel.add(new JLabel("Auteur :"));
        formPanel.add(auteurField);
        formPanel.add(new JLabel("ISBN :"));
        formPanel.add(isbnField);
        formPanel.add(new JLabel("Année :"));
        formPanel.add(anneeField);
        formPanel.add(new JLabel("Exemplaires :"));
        formPanel.add(exemplairesField);

        JButton btnValider = new JButton("Valider");
        btnValider.addActionListener(ev -> {
            if (titreField.getText().trim().isEmpty() || auteurField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Titre et auteur sont obligatoires", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }
            try {
                int annee = anneeField.getText().trim().isEmpty() ? 0 : Integer.parseInt(anneeField.getText());
                int exemplaires = exemplairesField.getText().trim().isEmpty() ? 1 : Integer.parseInt(exemplairesField.getText());
                Livre nouveauLivre = new Livre(
                    titreField.getText(),
                    auteurField.getText(),
                    isbnField.getText(),
                    annee,
                    exemplaires
                );
                livreDAO.ajouterLivre(nouveauLivre);
                chargerLivres();
                dialog.dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Année et exemplaires doivent être des nombres", "Erreur", JOptionPane.ERROR_MESSAGE);
            } catch (SQLException ex) {
                showError("Erreur lors de l'ajout", ex);
            }
        });

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(btnValider, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void modifierLivre(ActionEvent e) {
        int selectedRow = tableLivres.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un livre", "Avertissement", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Livre livre = ((LivreTableModel) tableLivres.getModel()).getLivreAt(selectedRow);
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Modifier un livre", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);

        JPanel formPanel = new JPanel(new GridLayout(6, 2, 5, 5));
        JTextField titreField = new JTextField(livre.getTitre());
        JTextField auteurField = new JTextField(livre.getAuteur());
        JTextField isbnField = new JTextField(livre.getIsbn());
        JTextField anneeField = new JTextField(String.valueOf(livre.getAnneePublication()));
        JTextField exemplairesField = new JTextField(String.valueOf(livre.getExemplairesDisponibles()));

        formPanel.add(new JLabel("Titre :"));
        formPanel.add(titreField);
        formPanel.add(new JLabel("Auteur :"));
        formPanel.add(auteurField);
        formPanel.add(new JLabel("ISBN :"));
        formPanel.add(isbnField);
        formPanel.add(new JLabel("Année :"));
        formPanel.add(anneeField);
        formPanel.add(new JLabel("Exemplaires :"));
        formPanel.add(exemplairesField);

        JButton btnValider = new JButton("Valider");
        btnValider.addActionListener(ev -> {
            if (titreField.getText().trim().isEmpty() || auteurField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Titre et auteur sont obligatoires", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }
            try {
                int annee = anneeField.getText().trim().isEmpty() ? 0 : Integer.parseInt(anneeField.getText());
                int exemplaires = exemplairesField.getText().trim().isEmpty() ? 1 : Integer.parseInt(exemplairesField.getText());
                livre.setTitre(titreField.getText());
                livre.setAuteur(auteurField.getText());
                livre.setIsbn(isbnField.getText());
                livre.setAnneePublication(annee);
                livre.setExemplairesDisponibles(exemplaires);
                livreDAO.modifierLivre(livre);
                chargerLivres();
                dialog.dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Année et exemplaires doivent être des nombres", "Erreur", JOptionPane.ERROR_MESSAGE);
            } catch (SQLException ex) {
                showError("Erreur lors de la modification", ex);
            }
        });

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(btnValider, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void supprimerLivre() {
        if (!(tableLivres.getModel() instanceof LivreTableModel)) {
            JOptionPane.showMessageDialog(this, "Erreur : Modèle de table non initialisé", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int selectedRow = tableLivres.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un livre", "Avertissement", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int modelRow = tableLivres.convertRowIndexToModel(selectedRow);
            LivreTableModel model = (LivreTableModel) tableLivres.getModel();
            Livre livre = model.getLivreAt(modelRow);

            if (livre == null) {
                JOptionPane.showMessageDialog(this, "Erreur : Livre non trouvé", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this,
                "Êtes-vous sûr de vouloir supprimer '" + livre.getTitre() + "' ?",
                "Confirmation", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                livreDAO.supprimerLivre(livre.getId());
                chargerLivres();
                JOptionPane.showMessageDialog(this, "Livre supprimé avec succès", "Succès", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException ex) {
            showError("Erreur lors de la suppression", ex);
        } catch (ArrayIndexOutOfBoundsException ex) {
            showError("Erreur : Index de ligne invalide", ex);
        }
    }

    private void showError(String message, Exception e) {
        JOptionPane.showMessageDialog(this, message + ": " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
    }
}