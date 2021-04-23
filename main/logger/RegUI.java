package logger;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalTime;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;

public class RegUI extends JFrame {
  private String filename;
  private TableModel tModel = new TableModel();
  private final JTable table = new JTable(tModel);
  private JScrollPane scrollPane = new JScrollPane(table);
  private int currentRow;

  public RegUI(String title, String filename) {
    // Set title
    super(title);
    this.filename = filename;
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    // Sets jTable
    table.setPreferredScrollableViewportSize(new Dimension(500, 200));
    table.setFillsViewportHeight(true);
    table.getColumnModel().getColumn(0).setCellRenderer(new CellRender());

    // Set layout manager
    setLayout(new FlowLayout());

    // Add the scroll pane to this frame.
    add(scrollPane);

    // Components
    JLabel idLabel = new JLabel("Välj nummer: ");
    JButton timeButton = new JButton("Ta tid");
    JTextField idField = new JTextField();
    JButton exportButton = new JButton("Öppna sorter");
    JButton deleteButton = new JButton("Radera rad");

    // Add components to app
    add(idLabel);
    add(idField);
    add(timeButton);
    add(deleteButton);
    add(exportButton);

    // Set size
    idField.setPreferredSize(new Dimension(50, 30));
    pack();

    // Get selected row
    table
        .getSelectionModel()
        .addListSelectionListener(
            new ListSelectionListener() {
              public void valueChanged(ListSelectionEvent e) {
                currentRow = table.getSelectedRow();
                updateFile();
              }
            });

    // Removes the selected row if a row is selected
    deleteButton.addActionListener(
        (e) -> {
          int result =
              JOptionPane.showConfirmDialog(
                  this,
                  "Ta bort markerad rad? TIDEN GÅR FÖRLORAD",
                  "VARNING!",
                  JOptionPane.YES_NO_OPTION,
                  JOptionPane.QUESTION_MESSAGE);
          if (result == JOptionPane.YES_OPTION) {
            try {
              tModel.removeRow(currentRow);
              updateFile();
            } catch (Exception e1) {
              // IT BROKE
            }
          }
        });

    // Export to a file if everything is correct
    exportButton.addActionListener(
        (e) -> {
          updateFile();
          JOptionPane.showMessageDialog(this, "Resultatet har exporterats till " + filename);
          JFrame frame =
              new sorter.ResultGUI(
                  "Enduro resultatgenererare",
                  new File(filename).getParentFile().getAbsolutePath());
          setVisible(false);
          frame.setVisible(true);
        });

    // Set action of timeButton
    timeButton.addActionListener(
        (e) -> {
          takeTime(idField, timeButton);
        });

    KeyListener listener =
        new KeyListener() {
          boolean pressed = false;

          @Override
          public void keyPressed(KeyEvent event) {
            if (event.getKeyCode() == KeyEvent.VK_ENTER) {
              takeTime(idField, timeButton);
            }
            if (event.getKeyCode() == KeyEvent.VK_Z && event.isControlDown() && !pressed) {
              int result =
                  JOptionPane.showConfirmDialog(
                      null,
                      "Ta bort senaste rad? TIDEN GÅR FÖRLORAD",
                      "VARNING!",
                      JOptionPane.YES_NO_OPTION,
                      JOptionPane.QUESTION_MESSAGE);
              if (result == JOptionPane.YES_OPTION) {
                int rows = table.getModel().getRowCount();
                if (rows > 0) {
                  pressed = true;
                  table.setRowSelectionInterval(rows - 1, rows - 1);
                  try {
                    tModel.removeRow(currentRow);
                  } catch (Exception e) {
                    e.printStackTrace();
                  }
                }
              }
            }
          }

          @Override
          public void keyReleased(KeyEvent event) {
            if (event.getKeyCode() == KeyEvent.VK_Z && event.isControlDown()) {
              pressed = false;
            }
          }

          @Override
          public void keyTyped(KeyEvent event) {}
        };

    idField.addKeyListener(listener);
  }

  private void takeTime(JTextField idField, JButton timebutton) {
    // Lägg in tiden i table
    LocalTime now = LocalTime.now();
    String time = now.toString().replaceAll(":", ".").substring(0, 8);
    String id = idField.getText().trim();
    tModel.addEntry(new TableModelEntry(time, id));
    // Updates table
    tModel.fireTableDataChanged();
    updateFile();

    SwingUtilities.invokeLater(
        new Runnable() {
          @Override
          public void run() {
            JScrollBar scroll = scrollPane.getVerticalScrollBar();
            scroll.setValue(scroll.getMaximum());
          }
        });
    idField.selectAll();

    // Transfer focus to JTextArea to show the selected
    // text.
    timebutton.transferFocusBackward();
  }

  private void updateFile() {
    try {
      Files.write(Paths.get(filename), tModel.printAll());
    } catch (IOException e) {
      JOptionPane.showMessageDialog(this, "Vänligen välj giltlig path");
    }
  }

  private class CellRender extends DefaultTableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(
        JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
      // Cells are by default rendered as a JLabel.
      JLabel l =
          (JLabel)
              super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);

      if (tModel.getStatus(row)) {
        l.setBackground(Color.WHITE);
      } else {
        l.setBackground(Color.RED);
      }

      // Return the JLabel which renders the cell.
      return l;
    }
  }
}
