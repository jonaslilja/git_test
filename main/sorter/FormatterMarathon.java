package sorter;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FormatterMarathon extends Formatter {

  List<String> startTimes;
  String totalTime;
  String name;
  List<String> finishTimes;

  @Override
  public String printPersons(List<Person> contestants) {
    List<String> result = new ArrayList<>();

    for (Person p : contestants) {
      startTimes = p.getStartTimes();
      finishTimes = p.getFinishTime();
      totalTime = p.getTotalTime();
      name = p.getName();
      String personResult =
          p.getId()
              + "; "
              + (name != null ? name : "Namn saknas")
              + "; "
              + totalTime
              + "; "
              + (startTimes.size() != 0 ? startTimes.get(0) : "Start?")
              + "; "
              + (!finishTimes.isEmpty() ? finishTimes.get(0) : "Slut?");
      result.add(personResult + errorHandling());
    }
    return String.join("\n", result);
  }

  public String errorHandling() {
    List<String> errors = new ArrayList<>();

    if (startTimes.size() > 1) {
      errors.add(
          "Flera starttider? " + startTimes.stream().skip(1).collect(Collectors.joining(", ")));
    }

    if (finishTimes.size() > 1) {
      errors.add(
          "Flera sluttider? " + finishTimes.stream().skip(1).collect(Collectors.joining(", ")));
    }

    if (!totalTime.equals("--.--.--")) {
      LocalTime time_1 = LocalTime.parse(totalTime, DateTimeFormatter.ofPattern("HH.mm.ss"));
      LocalTime time_2 = LocalTime.parse(minimumTime, DateTimeFormatter.ofPattern("HH.mm.ss"));

      if (time_1.compareTo(time_2) < 0) {
        errors.add("Omöjlig Totaltid?");
      }
    }

    if (errors.size() > 0) {
      errors.add(0, ";");
    }

    return String.join(" ", errors);
  }
}
