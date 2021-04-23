package logger;

import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.table.AbstractTableModel;

public class TableModel extends AbstractTableModel {

  List<TableModelEntry> modelList = new ArrayList<>();

  @Override
  public int getRowCount() {
    return modelList.size();
  }

  @Override
  public int getColumnCount() {
    return 2;
  }

  @Override
  public boolean isCellEditable(int rowIndex, int columnIndex) {
    return columnIndex == 0;
  }

  @Override
  public Object getValueAt(int i, int i1) {
    if (i1 == 0) {
      return modelList.get(i).id;
    } else {
      return modelList.get(i).time;
    }
  }

  /**
   * Checks if the specified row is valid
   *
   * @param row Row to check
   * @return Return true if row is valid, otherwise false.
   */
  public boolean getStatus(int row) {
    return modelList.get(row).isComplete();
  }

  /**
   * Sets an ALREADY EXISTING value, and makes sure inputted values have the right format.
   *
   * @param value
   * @param row
   * @param column
   */
  @Override
  public void setValueAt(Object value, int row, int column) {
    if (modelList.size() > row) {
      switch (column) {
        case 0:
          if (IDReader.isValidID((String) value)) {
            modelList.get(row).id = (String) value;
            fireTableDataChanged();
          } else {
            JOptionPane.showMessageDialog(null, "Felaktigt ID format");
          }
          break;
        case 1:
          modelList.get(row).time = (String) value;
          break;
        default:
          break;
      }
    }
  }

  @Override
  public String getColumnName(int i) {
    if (i == 0) {
      return "Id";
    } else {
      return "Time";
    }
  }

  /**
   * Adds a entry to the list
   *
   * @param tableModelEntry The entry that should be added
   */
  public void addEntry(TableModelEntry tableModelEntry) {
    modelList.add(tableModelEntry);
  }

  /**
   * Removes a row from the list
   *
   * @param row The row that should be removed
   * @throws Exception
   */
  public void removeRow(int row) throws Exception {
    modelList.remove(modelList.get(row));
    fireTableRowsDeleted(row, row);
  }

  /**
   * Print all contestants times. If entry is not valid it will not be printed
   *
   * @return List of contestant times
   */
  public List<String> printAll() {
    List<String> allEntries = new ArrayList<>();
    for (TableModelEntry entry : modelList) {
      allEntries.add(entry.toString());
    }
    return allEntries;
  }
}
