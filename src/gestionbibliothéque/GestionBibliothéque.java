package gestionbibliothéque;

import javax.swing.*;
import java.sql.SQLException;
import ui.GestionEmpruntsPanel;
import ui.GestionLivresPanel;
import ui.GestionUtilisateursPanel;

/**
 * Classe principale de l'application de gestion de bibliothèque
 */

public class GestionBibliothéque extends JFrame {
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                GestionBibliothéque app = new GestionBibliothéque();
                app.setVisible(true);
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null,
                        "Erreur de connexion à la base de données: " + e.getMessage(),
                        "Erreur Critique", JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null,
                        "Erreur inattendue: " + e.getMessage(),
                        "Erreur Critique", JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }
        });
    }
    
    public GestionBibliothéque() throws SQLException {
        super("Gestion de Bibliothèque");
        configureFrame();
        initUI();
    }
    
    private void configureFrame() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
    }
    
    private void initUI() throws SQLException {
        JTabbedPane onglets = new JTabbedPane();
        onglets.addTab("Livres", new GestionLivresPanel());
        onglets.addTab("Utilisateurs", new GestionUtilisateursPanel());
        onglets.addTab("Emprunts", new GestionEmpruntsPanel());
        add(onglets);
    }
}