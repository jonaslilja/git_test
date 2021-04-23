package sorter;

import javax.swing.*;

public class Main {
  public static void main(String[] args) throws Exception {
    // Foo foo = new Foo();
    // foo.bar(Foo.Goo);
    // Config.competitionType = Config.competitionType.Etapplopp;
    SwingUtilities.invokeLater(
        () -> {
          JFrame resultGUI = new ResultGUI("Enduro Resultatgenererare");
          resultGUI.setVisible(true);
        });
  }
}
