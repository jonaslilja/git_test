package sorter;

import java.awt.*;
import javax.swing.*;

public class PanelInfo extends JPanel {
  private JLabel label;
  private JTextField field;
  private JButton clearBtn;

  public PanelInfo(String text, String defaultString) {
    this.label = new JLabel(text);
    this.field = new JTextField(defaultString);
    this.clearBtn = new JButton("X");

    label.setPreferredSize(new Dimension(200, 30));
    field.setPreferredSize(new Dimension(300, 30));
    clearBtn.setPreferredSize(new Dimension(43, 30));

    add(label);
    add(field);
    add(clearBtn);

    clearBtn.addActionListener(
        e -> {
          int result =
              JOptionPane.showConfirmDialog(
                  null,
                  "Vill du rensa?",
                  "Dialogrensare",
                  JOptionPane.YES_NO_OPTION,
                  JOptionPane.QUESTION_MESSAGE);
          if (result == JOptionPane.YES_OPTION) {
            this.clear();
          }
        });
  }

  public String getText() {
    return field.getText();
  }

  public void clear() {
    field.setText("");
  }
}
