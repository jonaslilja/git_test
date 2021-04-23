package sorter;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.*;

public class PanelFileSelect extends JPanel {
  JButton btn = new JButton("Välj en startfil");
  JTextField fld = new JTextField();
  private List<String> paths = new ArrayList<>();
  boolean allowMultipleFiles;
  JFileChooser fc;
  ResultGUI parent;
  JButton clearBtn;
  int result;

  public void setPaths(List<String> paths) {
    this.paths = paths;
    setField(parent, fc, paths.stream().map(path -> new File(path)).collect(Collectors.toList()));
  }

  public List<String> getPaths() {
    return paths;
  }

  public PanelFileSelect(
      ResultGUI parent,
      String name,
      JFileChooser fc,
      boolean allowMultipleFiles,
      boolean useOpenDialog) {

    this.fc = fc;
    this.allowMultipleFiles = allowMultipleFiles;
    this.btn = new JButton(name);
    this.fld = new JTextField();
    this.parent = parent;
    fld.setEditable(false);
    this.clearBtn = new JButton("X");

    btn.setPreferredSize(new Dimension(200, 30));
    fld.setPreferredSize(new Dimension(300, 30));
    clearBtn.setPreferredSize(new Dimension(43, 30));

    add(btn);
    add(fld);
    add(clearBtn);

    clearBtn.addActionListener(
        e -> {
          int option =
              JOptionPane.showConfirmDialog(
                  null,
                  "Vill du rensa?",
                  "Dialogrensare",
                  JOptionPane.YES_NO_OPTION,
                  JOptionPane.QUESTION_MESSAGE);
          if (option == JOptionPane.YES_OPTION) {
            this.clear();
          }
        });

    btn.addActionListener(
        e -> {
          if (!allowMultipleFiles) {
            fc.setMultiSelectionEnabled(false);
            paths.clear();
          } else {
            fc.setMultiSelectionEnabled(true);
          }
          if (useOpenDialog) {
            result = fc.showOpenDialog(this);
          } else {
            result = fc.showSaveDialog(this);
          }
          if (result == JFileChooser.APPROVE_OPTION) {
            List<File> files = Arrays.asList(fc.getSelectedFiles());
            setPaths(files.stream().map(f -> f.getAbsolutePath()).collect(Collectors.toList()));
          }
        });
  }

  private void setField(ResultGUI parent, JFileChooser fc, List<File> files) {
    if (files.size() > 0) {
      if (paths.size() < 4) {
        if (fld.getText().isEmpty()) {
          fld.setText(files.stream().map(f -> f.getName()).collect(Collectors.joining(", ")));
        } else {
          fld.setText(
              fld.getText()
                  + ", "
                  + files.stream().map(f -> f.getName()).collect(Collectors.joining(", ")));
        }
      } else {
        fld.setText(paths.size() + " filer valda...");
      }
    } else {
      File file = fc.getSelectedFile();
      paths.add(file.getAbsolutePath());
      fld.setText(file.getName());
      String folderPath = file.getParentFile().getAbsolutePath();
      parent.pathSetInFileSelectPanel(folderPath);
    }
  }

  public void clear() {
    fld.setText("");
    paths.clear();
  }
}
