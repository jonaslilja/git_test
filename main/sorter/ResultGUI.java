package sorter;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

/** Controls the view for the result GUI */
public class ResultGUI extends JFrame {
  private static JFileChooser fileChooser = new JFileChooser();
  private static JFileChooser multipleFilesChooser = new JFileChooser();
  private static Model model = new Model();
  private static ResultWriter rw = new ResultWriter();
  private PanelInfo pnlInfoName;
  private PanelInfo pnlInfoStart;
  private PanelInfo pnlInfoEnd;
  private PanelInfo pnlInfoLocation;
  private PanelFileSelect pnlStart;
  private PanelFileSelect pnlFinish;
  private PanelFileSelect pnlName;
  private PanelFileSelect pnlResult;

  public ResultGUI(String title) {
    super(title);
    setup();
  }

  public ResultGUI(String title, String path) {
    super(title);
    setup();
    pathSetInFileSelectPanel(path);
  }

  private void setup() {
    FileNameExtensionFilter filter = new FileNameExtensionFilter("TEXT FILES", "txt", "text");
    fileChooser.setFileFilter(filter);

    // Components

    pnlInfoName = new PanelInfo("Tävlingsnamn:", "Endurotävling");
    pnlInfoStart =
        new PanelInfo("Startdatum:", LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE));
    pnlInfoEnd =
        new PanelInfo("Slutdatum:", LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE));
    pnlInfoLocation = new PanelInfo("Plats:", "Lund");
    pnlStart = new PanelFileSelect(this, "Välj en startfil", fileChooser, false, false);
    pnlFinish = new PanelFileSelect(this, "Välj en eller flera målfiler", fileChooser, true, false);
    pnlName = new PanelFileSelect(this, "Välj en namnfil", fileChooser, false, false);
    pnlResult = new PanelFileSelect(this, "Välj en resultatfil", fileChooser, false, true);

    JButton btnGenerate = new JButton("Generera resultat");
    JButton btnDelete = new JButton("Rensa allt");
    JButton btnHTML = new JButton("Generera html");
    JButton btnConf = new JButton("Konfiguration");
    // JOptionPane optionPane = new JOptionPane(null, JOptionPane.YES_NO_OPTION);

    // Add components to form
    Container container = getContentPane();
    container.add(pnlInfoName);
    container.add(pnlInfoStart);
    container.add(pnlInfoEnd);
    container.add(pnlInfoLocation);
    container.add(pnlStart);
    container.add(pnlFinish);
    container.add(pnlName);
    container.add(pnlResult);
    container.add(btnConf);
    container.add(btnDelete);
    container.add(btnHTML);
    container.add(btnGenerate);

    // Show components
    setLayout(new GridLayout(12, 1));
    pack();
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    // Opens the ConfUI
    // AtomicBoolean pressedConfigButton = new AtomicBoolean(false);
    btnConf.addActionListener(
        e -> {
          try {
            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
          } catch (UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
          } catch (IllegalAccessException ex) {
            ex.printStackTrace();
          } catch (InstantiationException ex) {
            ex.printStackTrace();
          } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
          }
          // Turn off metal's use of bold fonts
          UIManager.put("swing.boldMetal", Boolean.FALSE);

          // Opens ConfUI

          //     if (!pressedConfigButton.get()) {
          this.setEnabled(false);
          ConfigGUI.createAndShowGUI(model, this);
          //      pressedConfigButton.set(true);
          //  }
        });

    // Removes the text from the textfields
    btnDelete.addActionListener(
        e -> {
          int result =
              JOptionPane.showConfirmDialog(
                  null,
                  "Vill du rensa?",
                  "Dialogrensare",
                  JOptionPane.YES_NO_OPTION,
                  JOptionPane.QUESTION_MESSAGE);
          if (result == JOptionPane.YES_OPTION) {
            pnlInfoName.clear();
            pnlInfoStart.clear();
            pnlInfoEnd.clear();
            pnlInfoLocation.clear();
            pnlStart.clear();
            pnlFinish.clear();
            pnlName.clear();
            pnlResult.clear();
          }
        });

    btnHTML.addActionListener(
        e -> {
          model.setFormatter(new FormatterMarathonHTML());
          List<String> failedFiles = new ArrayList<String>();
          if (!pnlStart.getPaths().isEmpty()) {
            try {
              ReadFile.startFile(model, pnlStart.getPaths().get(0));
            } catch (Exception ex) {
              pnlStart.clear();
              failedFiles.add("startfil");
            }
          }

          if (!pnlFinish.getPaths().isEmpty()) {
            try {
              for (String path : pnlFinish.getPaths()) {
                ReadFile.finishFile(model, path);
              }
            } catch (Exception ex) {
              pnlFinish.clear();
              failedFiles.add("målfil/målfiler");
            }
          }

          if (!pnlName.getPaths().isEmpty()) {
            try {
              ReadFile.personFile(model, pnlName.getPaths().get(0));
            } catch (Exception ex) {
              pnlName.clear();
              failedFiles.add("namnfil");
            }
          }

          if (failedFiles.isEmpty()) {
            if (!pnlResult.getPaths().isEmpty()) {
              try {
                model.setInfo(
                    pnlInfoName.getText()
                        + " "
                        + pnlInfoStart.getText()
                        + " - "
                        + pnlInfoEnd.getText()
                        + ", "
                        + pnlInfoLocation.getText());

                ResultWriter.toFile(model, pnlResult.getPaths().get(0).replaceAll(".txt", ".html"));
              } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Gick ej att skriva till resultatfilen");
              }
              try {
                Desktop.getDesktop()
                    .open(new File(pnlResult.getPaths().get(0).replaceAll(".txt", ".html")));
              } catch (IOException ex) {
                ex.printStackTrace();
              }
              model = new Model();

            } else {
              JOptionPane.showMessageDialog(this, "Välj resultatfil");
            }
          } else {
            String errorMessage =
                "Följande filer gick ej att läsa: " + String.join(", ", failedFiles);
            JOptionPane.showMessageDialog(this, errorMessage);
          }
        });

    // Sends the files to the model
    btnGenerate.addActionListener(
        e -> {
          List<String> failedFiles = new ArrayList<String>();
          if (!pnlStart.getPaths().isEmpty()) {
            try {
              ReadFile.startFile(model, pnlStart.getPaths().get(0));
            } catch (Exception ex) {
              pnlStart.clear();
              failedFiles.add("startfil");
            }
          }

          if (!pnlFinish.getPaths().isEmpty()) {
            try {
              for (String path : pnlFinish.getPaths()) {
                ReadFile.finishFile(model, path);
              }
            } catch (Exception ex) {
              pnlFinish.clear();
              failedFiles.add("målfil/målfiler");
            }
          }

          if (!pnlName.getPaths().isEmpty()) {
            try {
              ReadFile.personFile(model, pnlName.getPaths().get(0));
            } catch (Exception ex) {
              pnlName.clear();
              failedFiles.add("namnfil");
            }
          }

          if (failedFiles.isEmpty()) {
            if (!pnlResult.getPaths().isEmpty()) {
              try {
                model.setInfo(
                    pnlInfoName.getText()
                        + " "
                        + pnlInfoStart.getText()
                        + " - "
                        + pnlInfoEnd.getText()
                        + ", "
                        + pnlInfoLocation.getText());

                ResultWriter.toFile(model, generateNameWithExtension(pnlResult.getPaths().get(0)));
              } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Gick ej att skriva till resultatfilen");
              }
              try {
                Desktop.getDesktop().open(new File(pnlResult.getPaths().get(0)));
              } catch (IOException ex) {
                ex.printStackTrace();
              }
              model = new Model();

            } else {
              JOptionPane.showMessageDialog(this, "Välj resultatfil");
            }
          } else {
            String errorMessage =
                "Följande filer gick ej att läsa: " + String.join(", ", failedFiles);
            JOptionPane.showMessageDialog(this, errorMessage);
          }
        });
  }

  public void pathSetInFileSelectPanel(String folderPath) {
    if (pnlResult.getPaths().isEmpty()) {
      ArrayList<String> paths = new ArrayList<>();
      paths.add(folderPath + "/exporterad.txt");
      pnlResult.setPaths(paths);
    }

    if (pnlName.getPaths().isEmpty()) {
      ArrayList<String> paths = new ArrayList<>();
      File folder = new File(folderPath);
      for (File file : folder.listFiles()) {
        if (file.getName().contains("namn")) {
          paths.add(file.getAbsolutePath());
          pnlName.setPaths(paths);
          break;
        }
      }
    }
    if (pnlFinish.getPaths().isEmpty()) {
      ArrayList<String> paths = new ArrayList<>();
      File folder = new File(folderPath);
      for (File file : folder.listFiles()) {
        if (file.getName().contains("mål") || file.getName().contains("mal")) {
          paths.add(file.getAbsolutePath());
        }
      }
      if (!paths.isEmpty()) {
        pnlFinish.setPaths(paths);
      }
    }

    if (pnlStart.getPaths().isEmpty()) {
      ArrayList<String> paths = new ArrayList<>();
      File folder = new File(folderPath);
      for (File file : folder.listFiles()) {
        if (file.getName().contains("start")) {
          paths.add(file.getAbsolutePath());
        }
      }
      if (!paths.isEmpty()) {
        pnlStart.setPaths(paths);
      }
    }
  }

  private String generateNameWithExtension(String filename) {
    if (!filename.endsWith(".txt")) {
      return filename + ".txt";
    }
    return filename;
  }
}
