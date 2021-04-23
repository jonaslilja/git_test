package sorter;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class ConfigGUI implements ItemListener {
  private Model model;
  private JFrame parent;
  private JFrame frame;
  JTextField textMarathonMin = new JTextField("", 5);
  JTextField textLapMax = new JTextField("", 5);
  JTextField textLapMin = new JTextField("", 5);

  JPanel cards; // a panel that uses CardLayout
  static final String MARATHONPANEL = "Maratonlopp";
  static final String LAPPANEL = "Varvlopp";

  public ConfigGUI(Model model, JFrame parent, JFrame frame) {
    this.model = model;
    this.parent = parent;
    this.frame = frame;

    frame.addWindowListener(
        new java.awt.event.WindowAdapter() {
          @Override
          public void windowClosing(java.awt.event.WindowEvent windowEvent) {
            parent.setVisible(true);
          }
        });
  }

  public void addComponentToPane(Container pane) {
    // Put the JComboBox in a JPanel to get a nicer look.
    JPanel comboBoxPane = new JPanel(); // use FlowLayout
    String comboBoxItems[] = {MARATHONPANEL, LAPPANEL};
    JComboBox cb = new JComboBox(comboBoxItems);
    cb.setEditable(false);
    cb.addItemListener(this);
    comboBoxPane.add(cb);

    JButton btnSave = new JButton("Spara");

    // Create the "cards".
    JPanel card1 = new JPanel();
    card1.add(new JLabel("Minimitid (HH.mm.ss)"));
    card1.add(textMarathonMin);

    JPanel card2 = new JPanel();
    card2.add(new JLabel("Loppets maxtid (HH.mm.ss)"));
    card2.add(textLapMax);
    card2.add(new JLabel("Minimitid för varv (HH.mm.ss)"));
    card2.add(textLapMin);

    // Create the panel that contains the "cards".
    cards = new JPanel(new CardLayout());
    cards.add(card1, MARATHONPANEL);
    cards.add(card2, LAPPANEL);

    pane.add(comboBoxPane, BorderLayout.PAGE_START);
    pane.add(cards, BorderLayout.CENTER);
    pane.add(btnSave, BorderLayout.SOUTH);

    textMarathonMin.setPreferredSize(new Dimension(100, 30));
    textLapMax.setPreferredSize(new Dimension(100, 30));
    textLapMin.setPreferredSize(new Dimension(100, 30));

    // Add behaviour
    btnSave.addActionListener(
        new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
            if (cb.getSelectedItem() == MARATHONPANEL) {
              String mini = textMarathonMin.getText();
              if (model.isValidTime(mini)) {
                model.setFormatter(new FormatterMarathon());
                model.setMinimumTime(mini);
                parent.setVisible(true);
                frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
              } else {
                JOptionPane.showMessageDialog(null, "Använd giltigt format HH.mm.ss");
              }
            } else {
              String total = textLapMax.getText();
              String mini = textLapMin.getText();
              if (model.isValidTime(mini) && model.isValidTime(total)) {
                model.setFormatter(new FormatterLap());
                model.setCutOff(total);
                model.setMinimumTime(mini);
                parent.setVisible(true);
                frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
              } else {
                JOptionPane.showMessageDialog(null, "Använd giltigt format HH.mm.ss");
              }
            }
          }
        });
  }

  public void itemStateChanged(ItemEvent evt) {
    CardLayout cl = (CardLayout) (cards.getLayout());
    cl.show(cards, (String) evt.getItem());
  }

  /**
   * Create the GUI and show it. For thread safety, this method should be invoked from the event
   * dispatch thread.
   */
  public static void createAndShowGUI(Model model, JFrame parent) {
    // Create and set up the window.
    JFrame frame = new JFrame("Konfigurera lopp");

    // Create and set up the content pane.
    ConfigGUI demo = new ConfigGUI(model, parent, frame);
    demo.addComponentToPane(frame.getContentPane());

    // Display the window.
    frame.pack();
    frame.setVisible(true);
  }
}
