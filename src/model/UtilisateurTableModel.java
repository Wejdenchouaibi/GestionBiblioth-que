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
import java.util.List;

public class UtilisateurTableModel extends AbstractTableModel {
    private final String[] columnNames = {"ID", "Nom", "Prénom", "Numéro Adhérent"};
    private List<Utilisateur> utilisateurs;
    
    public UtilisateurTableModel(List<Utilisateur> utilisateurs) {
        this.utilisateurs = utilisateurs;
    }
    
    @Override
    public int getRowCount() {
        return utilisateurs.size();
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
        Utilisateur utilisateur = utilisateurs.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> utilisateur.getId();
            case 1 -> utilisateur.getNom();
            case 2 -> utilisateur.getPrenom();
            case 3 -> utilisateur.getNumeroAdherent();
            default -> null;
        };
    }
    
    public void updateData(List<Utilisateur> newUtilisateurs) {
        this.utilisateurs = newUtilisateurs;
        fireTableDataChanged();
    }
    
    public Utilisateur getUtilisateurAt(int rowIndex) {
        return utilisateurs.get(rowIndex);
    }
}