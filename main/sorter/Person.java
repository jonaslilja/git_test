package sorter;

import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Person's purpose is to save id, start times, finish times and calculate total time for each
 * contestant.
 */
public class Person {
  private String classType;
  private int id;
  private List<String> startTimes;
  private List<String> registeredTimes;
  private List<String> finishTime;
  private String name;
  private List<String> lapTimes;

  public Person(int id) {
    this.id = id;
    this.startTimes = new ArrayList<>();
    this.name = "";
    this.registeredTimes = new ArrayList<>();
    finishTime = new ArrayList<>();
    this.lapTimes = new ArrayList<>();

    if (id < 0) {
      this.classType = "ID SAKNAS";
    } else {
      this.classType = "NAMN SAKNAS";
    }
  }

  /**
   * gets persons Id
   *
   * @return persons Id
   */
  public int getId() {
    return id;
  }

  /**
   * Gets the starttime(s) for the contestant
   *
   * @return list of startimes
   */
  public List<String> getStartTimes() {
    return startTimes;
  }

  /**
   * Gets this contestants name
   *
   * @return String with contestants name
   */
  public String getName() {
    return name;
  }

  /**
   * Gets a list of contestants registered times
   *
   * @return List of registered times
   */
  public List<String> getRegisteredTimes() {
    return registeredTimes;
  }

  /**
   * Gets a list of contestants registered laptimes.
   *
   * @return List of registered laptimes
   */
  public List<String> getLapTimes() {
    return lapTimes;
  }

  /**
   * sets persons starttime
   *
   * @param startTime time person started
   */
  public void setStartTime(String startTime) {
    this.startTimes.add(startTime);
  }

  /**
   * Adds time to persons list of finishtimes
   *
   * @param time time to append to list
   */
  public void setFinishTime(String time) {
    this.finishTime.add(time);
  }

  /**
   * sets persons laptime
   *
   * @param lapTime time person finished
   */
  public void registerLapTime(String lapTime) {
    this.registeredTimes.add(lapTime);
    this.registeredTimes.sort(new RegisteredTimeComparator());
  }

  /** @return number of laps */
  public int getNbrOfLaps() {
    int i = registeredTimes.size();
    if (finishTime.size() != 0) {
      i++;
    }
    return i;
  }

  /** Calculates all lap times from registeredTimes. */
  public void calculateLapTimes() {
    if (!registeredTimes.isEmpty() && !startTimes.isEmpty() && !finishTime.isEmpty()) {
      lapTimes.add(calculateTimeDifference(startTimes.get(0), registeredTimes.get(0)));
      for (int i = 1; i < registeredTimes.size(); i++) {
        lapTimes.add(calculateTimeDifference(registeredTimes.get(i - 1), registeredTimes.get(i)));
      }
      lapTimes.add(
          calculateTimeDifference(
              registeredTimes.get(registeredTimes.size() - 1), finishTime.get(0)));
    } else if (!startTimes.isEmpty() && !finishTime.isEmpty()) {
      lapTimes.add(calculateTimeDifference(startTimes.get(0), finishTime.get(0)));
    }
  }

  private String prependIfSingleDigit(String s) {
    if (s.length() == 1) {
      return "0" + s;
    }
    return s;
  }

  private String calculateTimeDifference(String start, String finish) {
    DateTimeFormatter format = DateTimeFormatter.ofPattern("HH.mm.ss");
    LocalTime startTime = LocalTime.parse(start, format);
    LocalTime finishTime = LocalTime.parse(finish, format);
    Duration totalSeconds = Duration.between(startTime, finishTime);
    if (totalSeconds.isNegative()) {
      totalSeconds = totalSeconds.plusDays(1);
    }
    String hours = prependIfSingleDigit(Long.toString(totalSeconds.getSeconds() / 3600));
    String minutes = prependIfSingleDigit(Long.toString((totalSeconds.getSeconds() / 60) % 60));
    String seconds = prependIfSingleDigit(Long.toString(totalSeconds.getSeconds() % 60));
    return hours + "." + minutes + "." + seconds;
  }

  /**
   * Returns totalTime or --.--.-- if not set yet
   *
   * @return The person's total time
   */
  public String getTotalTime() {
    if (startTimes.size() > 0 && finishTime.size() > 0) {
      // finishTime.add(registeredTimes.get(registeredTimes.size() - 1));
      return calculateTimeDifference(startTimes.get(0), finishTime.get(0));
    } else {
      return "--.--.--";
    }
  }

  public List<String> getFinishTime() {
    return finishTime;
  }

  /** Creates a String for a persons data */
  public void setName(String name) {
    this.name = name;
  }

  public String getClassType() {
    return classType;
  }

  public void setClassType(String classType) {
    this.classType = classType;
  }

  public class RegisteredTimeComparator implements Comparator<String> {

    @Override
    public int compare(String s0, String s1) {
      DateTimeFormatter format = DateTimeFormatter.ofPattern("HH.mm.ss");
      LocalTime t0 = LocalTime.parse(s0, format);
      LocalTime t1 = LocalTime.parse(s1, format);
      return t0.compareTo(t1);
    }
  }
}
