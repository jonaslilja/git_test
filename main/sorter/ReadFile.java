package sorter;

import java.io.*;
import java.lang.reflect.Method;

/** Abstract class for reading start and finish times */
class ReadFile {
  private static Model model;

  private ReadFile() {}

  /**
   * Calls ReadFile with the its parameters and specifies that it's a name file
   *
   * @param model what model Readfile should use
   * @param path String with the path to the file the user wants to read
   * @throws Exception
   */
  public static void personFile(Model model, String path) throws Exception {
    callReadFile(model, path, "putName");
  }

  /**
   * Calls ReadFile with the its parameters and specifies that it's a starttimes file
   *
   * @param model what model Readfile should use
   * @param path String with the path to the file the user wants to read
   * @throws Exception
   */
  public static void startFile(Model model, String path) throws Exception {
    callReadFile(model, path, "putStart");
  }
  /**
   * Calls ReadFile with the its parameters and specifies that it's a finishtimes file
   *
   * @param model what model Readfile should use
   * @param path String with the path to the file the user wants to read
   * @throws Exception
   */
  public static void finishFile(Model model, String path) throws Exception {
    callReadFile(model, path, "putFinish");
  }

  private static void callReadFile(Model model, String path, String putMethod) throws Exception {
    Method method = model.getClass().getMethod(putMethod, int.class, String.class);
    readFile(model, path, method);
  }

  private static void readFile(Model model, String path, Method method) throws Exception {
    if (path.isEmpty()) {
      throw new Exception("No file path");
    }

    ReadFile.model = model;
    File file = new File(path);
    FileReader reader = null;
    try {
      reader = new FileReader(file);
      BufferedReader br = new BufferedReader(reader);
      String st;
      while ((st = br.readLine()) != null) {
        if (!st.isEmpty()) {
          if (!st.contains(";")) {
            model.classes.add(st);
            model.setCurrentPersonType(st);
          } else {
            String[] ar = st.replaceAll("; ", ";").split(";");
            if (!ar[0].equals("StartNr")) {
              int id;
              if (ar[0].isEmpty()) {
                id = -1;
                while (model.getPerson(id).isPresent()) {
                  id--;
                }
              } else {
                id = Integer.parseInt(ar[0]);
              }
              method.invoke(model, id, ar[1]);
            }
          }
        }
      }
      br.close();
    } catch (FileNotFoundException e) {
      System.out.println(e);
      throw new Exception("File not found");
    }
  }
}
