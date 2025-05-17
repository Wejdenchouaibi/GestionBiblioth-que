package model;

import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 * Modèle de table pour l'affichage des livres dans un JTable
 */
public class LivreTableModel extends AbstractTableModel {
    private final String[] columnNames = {"ID", "Titre", "Auteur", "ISBN", "Année", "Exemplaires"};
    private final List<Livre> livres;
    
    /**
     * Constructeur
     * @param livres La liste des livres à afficher
     */
    public LivreTableModel(List<Livre> livres) {
        if (livres == null) {
            throw new IllegalArgumentException("La liste de livres ne peut pas être null");
        }
        this.livres = livres;
    }
    
    @Override
    public int getRowCount() {
        return livres.size();
    }
    
    @Override
    public int getColumnCount() {
        return columnNames.length;
    }
    
    @Override
    public String getColumnName(int column) {
        if (column < 0 || column >= columnNames.length) {
            return "";
        }
        return columnNames[column];
    }
    
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (rowIndex < 0 || rowIndex >= livres.size()) {
            return null;
        }
        
        Livre livre = livres.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> livre.getId();
            case 1 -> livre.getTitre();
            case 2 -> livre.getAuteur();
            case 3 -> livre.getIsbn();
            case 4 -> livre.getAnneePublication();
            case 5 -> livre.getExemplairesDisponibles();
            default -> null;
        };
    }
    
    /**
     * Met à jour les données du modèle
     * @param newLivres La nouvelle liste de livres
     */
    public void updateData(List<Livre> newLivres) {
        if (newLivres != null) {
            this.livres.clear();
            this.livres.addAll(newLivres);
            fireTableDataChanged();
        }
    }
    
    /**
     * Récupère le livre à une ligne spécifique
     * @param rowIndex L'index de la ligne
     * @return Le livre correspondant
     */
    public Livre getLivreAt(int rowIndex) {
        if (rowIndex >= 0 && rowIndex < livres.size()) {
            return livres.get(rowIndex);
        }
        return null;
    }
}