package sorter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/** Formats model into big string */
public abstract class Formatter {
  public int mostLaps = 0;
  public String minimumTime;

  /**
   * Takes contestants, sorts and returns a string representation.
   *
   * @param contestants List of all contestants
   * @return String with all contestants and their information.
   */
  String print(List<Person> contestants, Set<String> classes, String info, String minimumtime) {
    this.minimumTime = minimumtime;
    getMostLaps(contestants);
    List<String> result = new ArrayList<>();
    result.add(getPageHead() + info != null ? info : "Info saknas");
    for (String c : classes) {
      String persons =
          printPersons(
              contestants.stream().filter(p -> p.getClassType() == c).collect(Collectors.toList()));
      if (!persons.isEmpty()) {
        result.add(getClass(c));
        result.add(getHeader());
        result.add(persons);
      }
    }
    result.add(getSignature());
    String str = String.join("\n", result);
    return str;
  }

  public String getClass(String c) {
    return c;
  }

  private int getMostLaps(List<Person> contestants) {
    if (mostLaps == 0) {
      int temp = 0;
      for (Person p : contestants) {
        int nbrOfLaps = p.getNbrOfLaps();
        if (nbrOfLaps > temp) {
          temp = nbrOfLaps;
        }
      }
      this.mostLaps = temp;
    }
    return mostLaps;
  }

  abstract String printPersons(List<Person> contestants);

  String getSignature() {
    return "\n\n Resultatgenerering av team 7";
  }

  String getHeader() {
    return "StartNr; Namn; Totaltid; Start; Sluttid";
  };

  String getPageHead() {
    return "";
  }
}
