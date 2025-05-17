/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author wijde
 */



import javax.swing.table.AbstractTableModel;
import java.text.SimpleDateFormat;
import java.util.List;

public class EmpruntTableModel extends AbstractTableModel {
    private final String[] columnNames = {"ID", "Livre", "Utilisateur", "Date Emprunt", "Date Retour", "Statut"};
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    private final List<Emprunt> emprunts;

    public EmpruntTableModel(List<Emprunt> emprunts) {
        if (emprunts == null) {
            throw new IllegalArgumentException("La liste d'emprunts ne peut pas être null");
        }
        this.emprunts = emprunts;
    }

    @Override
    public int getRowCount() {
        return emprunts.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (rowIndex < 0 || rowIndex >= emprunts.size()) {
            return null;
        }
        Emprunt emprunt = emprunts.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> emprunt.getId();
            case 1 -> emprunt.getLivreTitre();
            case 2 -> emprunt.getUtilisateurPrenom() + " " + emprunt.getUtilisateurNom();
            case 3 -> dateFormat.format(emprunt.getDateEmprunt());
            case 4 -> emprunt.getDateRetour() != null ? dateFormat.format(emprunt.getDateRetour()) : "-";
            case 5 -> emprunt.getDateRetour() == null ? "En cours" : "Retourné";
            default -> null;
        };
    }

    public void updateData(List<Emprunt> newEmprunts) {
        if (newEmprunts != null) {
            this.emprunts.clear();
            this.emprunts.addAll(newEmprunts);
            fireTableDataChanged();
        }
    }

    public Emprunt getEmpruntAt(int rowIndex) {
        if (rowIndex >= 0 && rowIndex < emprunts.size()) {
            return emprunts.get(rowIndex);
        }
        return null;
    }
}