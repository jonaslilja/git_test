package sorter;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FormatterLap extends Formatter {

  private String generateLapColumns(int thisNbrLaps) {
    String str = "";
    for (int i = 0; i < mostLaps - thisNbrLaps; i++) {
      str += "; ";
    }
    return str;
  }

  private String buildLapsString(List<String> times, int nbrOfLaps) {
    String str = "";
    List<String> registeredTimes = times;
    if (registeredTimes.size() > 0) {
      registeredTimes.remove(registeredTimes.size() - 1);
    }
    str += String.join("; ", registeredTimes);
    str += generateLapColumns(nbrOfLaps + 1);
    return str;
  }

  private String buildLaptimeString(List<String> times, int nbrOfLaps) {
    String str = "";
    str += String.join("; ", times);
    str += generateLapColumns(nbrOfLaps);
    return str;
  }

  /**
   * Takes contestants, sorts and returns a string representation.
   *
   * @param contestants List of all contestants
   * @return String with all contestants and their information.
   */
  public String printPersons(List<Person> contestants) {
    List<String> result = new ArrayList<>();
    for (Person p : contestants) {
      p.calculateLapTimes();
      List<String> startTimes = p.getStartTimes();
      List<String> registeredTimes = p.getRegisteredTimes();
      List<String> lapTimes = p.getLapTimes();
      List<String> finishtime = p.getFinishTime();
      String totalTime = p.getTotalTime();
      int nbrOfLaps = p.getNbrOfLaps();
      String name = p.getName();

      String personResult =
          p.getId()
              + "; "
              + (name != null ? name : "Namn saknas")
              + "; "
              + p.getNbrOfLaps()
              + "; "
              + totalTime
              + "; "
              + buildLaptimeString(lapTimes, nbrOfLaps)
              + "; "
              + (startTimes.size() != 0 ? startTimes.get(0) : "Start?")
              + "; "
              + buildLapsString(registeredTimes, nbrOfLaps)
              + "; "
              // + (registeredTimes.size() != 0
              //    ? registeredTimes.get(registeredTimes.size() - 1)
              //    : "Slut?");
              + (!finishtime.isEmpty() ? finishtime.get(0) : "Slut?");
      result.add(personResult + errorHandling(startTimes, finishtime, totalTime, lapTimes));
    }
    return String.join("\n", result);
  }

  @Override
  public String getHeader() {
    String header = "StartNr; Namn; #Varv; Totaltid; ";
    for (int i = 1; i < mostLaps + 1; i++) {
      header += "Varv" + i + "; ";
    }
    header += "Start; ";
    for (int i = 1; i < mostLaps; i++) {
      header += "Varvning" + i + "; ";
    }
    header += "Sluttid";

    return header;
  }

  public String errorHandling(
      List<String> startTimes, List<String> finishTimes, String totalTime, List<String> lapTimes) {
    List<String> errors = new ArrayList<>();
    Boolean impossible = false;

    if (startTimes.size() > 1) {
      errors.add(
          "Flera starttider? " + startTimes.stream().skip(1).collect(Collectors.joining(", ")));
    }

    if (finishTimes.size() > 1) {
      errors.add(
          "Flera måltider? " + finishTimes.stream().skip(1).collect(Collectors.joining(", ")));
    }

    if (!totalTime.equals("--.--.--")) {
      LocalTime time_1 = LocalTime.parse(totalTime, DateTimeFormatter.ofPattern("HH.mm.ss"));
      LocalTime time_2 = LocalTime.parse("00.00.15", DateTimeFormatter.ofPattern("HH.mm.ss"));

      if (time_1.compareTo(time_2) < 0) {
        errors.add(" Omöjlig Totaltid?");
      }
    }

    if (errors.size() > 0) {
      errors.add(0, ";");
    }
    for (String lap : lapTimes) {
      DateTimeFormatter format = DateTimeFormatter.ofPattern("HH.mm.ss");
      LocalTime startTime = LocalTime.parse(lap, format);
      LocalTime minimum = LocalTime.parse(minimumTime, format);
      impossible = (startTime.compareTo(minimum) < 0);
    }
    if (impossible) {
      errors.add(" Omöjlig Varvtid?");
    }
    return errors.stream().collect(Collectors.joining(" "));
  }
}
