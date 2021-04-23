package sorter;

import java.util.List;

public class FormatterMarathonHTML extends FormatterMarathon {
  @Override
  public String getPageHead() {
    return "<!doctype html><html lang=\"en\"><head><meta charset=\"utf-8\"><title>Table Template</title><meta name=\"description\""
        + "content=\"Table Template\"><meta name=\"author\" content=\"Team 7\"><link rel=\"stylesheet\" type=\"text/css\" href=\"style.css\"></head><body><h1 style=\"color:red;\">RESULTAT.</h1>"
        + "<br><br>";
  }

  @Override
  public String getHeader() {
    return "<table>\n<tr>\n<td>StartNr</td> <td>Namn</td> <td>Totaltid</td> <td>Starttid</td> <td>Måltid</td> <td>error</td> </tr>";
  }

  @Override
  public String getClass(String c) {
    return "<br><h2>" + c + "</h2>";
  }

  @Override
  public String printPersons(List<Person> contestants) {
    return "<tr><td>"
        + super.printPersons(contestants)
            .replaceAll(";", "</td><td>")
            .replaceAll("\n", "</td></tr>\n<tr><td>")
        + "</table>";
  }

  @Override
  public String getSignature() {
    return "<br>" + super.getSignature();
  }
}
