package sorter;

import java.io.FileWriter;
import java.io.IOException;

/**
 * ResultWriter's purpose is to take inputed results from the enduro race and writes them to a file
 * according to format
 */
public class ResultWriter {

  // Makes it private
  ResultWriter() {}

  /**
   * Writes enduro race results to a file
   *
   * @param model the model used for getting results
   * @param filename the name of the file
   * @throws IOException
   */
  public static void toFile(Model model, String filename) throws IOException {
    FileWriter fw = new FileWriter(filename);
    fw.write(model.toString());
    fw.close();
    /*======= TODO: FUCKING DÖ
      public void writeToFile(Model model, String info, String filename) throws IOException {
        List<String> data = new ArrayList<>();

        for (Map.Entry<String, List<Person>> entry : model.getPersonTypes().entrySet()) {
          String key = entry.getKey();
          List<Person> value = model.getContestants(entry.getValue());
          data.add(key);
          data.add("StartNr;Namn;Totaltid;Starttid;Måltid");
          data.addAll(value.stream().map(c -> c.toString()).collect(Collectors.toList()));
        }
        data.add(0, info);
        data.add(1, "");
        if (!model.getNameLessContestants().isEmpty()) {
          data.add("Icke existerande startnummer");
          data.add("StartNr;Totaltid;Starttid;Måltid");
          data.addAll(
              model
                  .getNameLessContestants()
                  .stream()
                  .map(c -> c.toString())
                  .collect(Collectors.toList()));
        }
        if (!model.getContestantsWithoutID().isEmpty()) {
          data.add("Icke existerande ID");
          data.add("StartNr;Totaltid;Starttid;Måltid");
          data.addAll(
              model
                  .getContestantsWithoutID()
                  .stream()
                  .map(c -> c.toString())
                  .collect(Collectors.toList()));
        }
        data.add("\nResultatgenerering av team 7");
        Files.write(Paths.get(filename), data);
    >>>>>>> dev*/
  }

  /**
   * Formats the results as a table and writes it to a html file
   *
   * @param model the model used for getting results
   * @param filename the name of the file
   * @throws IOException
   */
  /*
  public void writeToHTML(Model model, String info, String filename) throws IOException {
    // ADD FUNCTIONALITY TO FORMAT A HTML-TABLE (see template_table.html under /docs)
    // Files.write(Paths.get(filename), data);
    List<String> data = new ArrayList<>();
    data.add(
        "<!doctype html><html lang=\"en\"><head><meta charset=\"utf-8\"><title>Table Template</title><meta name=\"description\" content=\"Table Template\"><meta name=\"author\" content=\"Team 7\"><link rel=\"stylesheet\" type=\"text/css\" href=\"style.css\"></head><body><h1 style=\"color:red;\">RESULTAT.</h1>");
    data.add(info + "<br><br>");

    for (String entry : model.classes) {
   "   List<Person> value = model.getContestants;
      data.add(key);
      data.add("<table>");
      data.add(
          "<tr> <td>StartNr</td> <td>Namn</td> <td>Totaltid</td> <td>Starttid</td> <td>Måltid</td> <td>error</td> </tr>");
      data.addAll(
          value
              .stream()
              .map(c -> "<tr><td>" + c.toString().replaceAll(";", "</td><td>") + "</td></tr>")
              .collect(Collectors.toList()));
      data.add("</table>");
      data.add("<br>");
    }
    data.add("\nResultatgenerering av team 7");
    data.add("</body></html>");
    Files.write(Paths.get(filename), data);
  }*/
}
