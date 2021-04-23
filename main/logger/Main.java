package logger;

import java.awt.*;
import java.io.IOException;
import javax.swing.*;

public class Main {

  static JFrame frame;

  public static void main(String[] args) throws IOException {
    SwingUtilities.invokeLater(
        new Runnable() {
          public void run() {
            JFrame confUI = new ConfUI("Välj filnamn");
            confUI.setVisible(true);
          }
        });
  }
}
