package logger;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class ConfUI extends JFrame {
  private JTextField field;
  private static JFileChooser fileChooser;

  public ConfUI(String title) {
    // Set title of frame
    super(title);

    // Ends program if you close the window
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    // Set layout manager
    setLayout(new FlowLayout());

    // Create Swing components or panels
    fileChooser = new JFileChooser();
    JLabel label = new JLabel("Skriv in filnamn: ");
    JButton pathButton = new JButton("Välj filnamn");
    field = new JTextField();
    field.setEditable(false);
    JButton startButton = new JButton("Starttider");
    JButton finishButton = new JButton("Måltider");

    field.setPreferredSize(new Dimension(200, 30));

    // Add Swing components and panels
    Container container = getContentPane();
    container.add(label);
    container.add(pathButton);
    container.add(field);
    container.add(startButton);
    container.add(finishButton);

    // Set size
    pack();

    // Add behaviour
    pathButton.addActionListener(
        e -> {
          int result = fileChooser.showSaveDialog(null);
          if (result == JFileChooser.APPROVE_OPTION) {
            field.setText(fileChooser.getSelectedFile().getAbsolutePath());
          }
        });
    startButton.addActionListener(
        (ActionEvent actionEvent) -> {
          if (!field.getText().isEmpty()) {
            String fileName = generateNameWithExtension(field.getText().trim());
            JOptionPane.showMessageDialog(this, "Startfil skapad med namnet: " + fileName);
            JFrame registrationUI = new RegUI("Registrera starttider", fileName);
            registrationUI.setVisible(true);
            setVisible(false);
          } else {
            JOptionPane.showMessageDialog(this, "Ange filnamn");
          }
        });

    finishButton.addActionListener(
        (ActionEvent actionEvent) -> {
          if (!field.getText().isEmpty()) {
            String fileName = generateNameWithExtension(field.getText().trim());
            JOptionPane.showMessageDialog(this, "Målfil skapad med namnet: " + fileName);
            JFrame regUI = new RegUI("Registrera måltider", fileName);
            regUI.setVisible(true);
            setVisible(false);
          } else {
            JOptionPane.showMessageDialog(this, "Ange filnamn");
          }
        });
  }

  private String generateNameWithExtension(String filename) {
    if (!filename.endsWith(".txt")) {
      return filename + ".txt";
    }
    return filename;
  }
}
