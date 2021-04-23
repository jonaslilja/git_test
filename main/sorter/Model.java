package sorter;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Model {
  private List<Person> contestants = new ArrayList<>();
  private String totalTime;
  private String minimumTime = "00.10.00";
  private Formatter formatter = new FormatterMarathon();
  private String info = "";
  private String currentPersonType = "STANDARDKLASS";
  private LocalTime cutoff = LocalTime.parse("00:00:00");
  public Set<String> classes =
      Stream.of("STANDARDKLASS", "ID SAKNAS", "NAMN SAKNAS").collect(Collectors.toSet());

  public void setInfo(String info) {
    this.info = info;
  }

  public boolean isValidTime(String time) {
    try {
      DateTimeFormatter format = DateTimeFormatter.ofPattern("HH.mm.ss");
      LocalTime startTime = LocalTime.parse(time, format);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  /**
   * TODO
   *
   * @param formatter
   */
  public void setFormatter(Formatter formatter) {
    this.formatter = formatter;
  }

  /**
   * sets the starttime for a person with a certain id. If this person doesn't exist then a new one
   * is created.
   *
   * @param id the persons id
   * @param time the start time for the person with id.
   */
  public void putStart(int id, String time) {
    getOrCreatePerson(id).setStartTime(time);
  }

  /**
   * registers a lap time for a person with a certain id. If this person doesn't exists then a new
   * one is created.
   *
   * @param id the persons id
   * @param time the finish time for the person with id .
   */
  public void putFinish(int id, String time) {
    DateTimeFormatter format = DateTimeFormatter.ofPattern("HH.mm.ss");
    LocalTime endTime = LocalTime.parse(time, format);
    if (endTime.compareTo(cutoff) >= 0) {
      getOrCreatePerson(id).setFinishTime(time);
    } else {
      getOrCreatePerson(id).registerLapTime(time);
    }
  }

  /**
   * Sets the name for a contestant with a certain ID.
   *
   * @param id contestants ID number
   * @param name the name we want to set
   */
  public void putName(int id, String name) {
    Person p = getOrCreatePerson(id);
    p.setName(name);
    p.setClassType(currentPersonType);
  }

  /**
   * gets a persons result time if a person with this id exists otherwise throw exception
   *
   * @param id the persons id
   * @return person with Id id
   * @throws Exception
   */
  public Person getResult(int id) throws Exception {
    Optional<Person> optionalPerson = getPerson(id);
    if (optionalPerson.isPresent()) {
      return optionalPerson.get();
    } else {
      throw new Exception("No contestant with this ID");
    }
  }

  public String getTotalTime() {
    return totalTime;
  }

  public void setTotalTime(String totalTime) {
    this.totalTime = totalTime;
  }

  public String getMinimumTime() {
    return minimumTime;
  }

  public void setMinimumTime(String minimumTime) {
    this.minimumTime = minimumTime;
  }

  public void setCutoff(LocalTime cutoff) {
    this.cutoff = cutoff;
  }

  public List<Person> getContestants() {
    return contestants;
  }

  public List<Person> getContestants(String classType) {
    return contestants
        .stream()
        .filter(p -> p.getClassType() == classType)
        .collect(Collectors.toList());
  }

  /**
   * gets the list of contestants with names
   *
   * @return list of contestants
   */
  public List<Person> getContestantsWithName() {
    List<Person> contestantsWithName = new ArrayList<>();
    for (Person p : contestants) {
      if (!p.getName().isEmpty()) {
        contestantsWithName.add(p);
      }
    }
    return contestantsWithName;
  }

  /**
   * Gets the list of contestants who doesn't have a registered name.
   *
   * @return list of nameless contestants.
   */
  public List<Person> getNameLessContestants() {
    List<Person> contestantsWithoutName = new ArrayList<>();
    for (Person p : contestants) {
      if (p.getName().isEmpty() && p.getId() > 0) {
        contestantsWithoutName.add(p);
      }
    }
    return contestantsWithoutName;
  }

  /**
   * Gets the list of times without an ID.
   *
   * @return list of contestants without ID.
   */
  public List<Person> getContestantsWithoutID() {
    List<Person> contestantsWithoutID = new ArrayList<>();
    for (Person p : contestants) {
      if (p.getId() < 0) {
        contestantsWithoutID.add(p);
      }
    }
    return contestantsWithoutID;
  }

  /**
   * get person with Id id
   *
   * @param id persons id
   * @return person with Id id
   */
  public Optional<Person> getPerson(int id) {
    return contestants.stream().filter(p -> p.getId() == id).findFirst();
  }

  private Person getOrCreatePerson(int id) {

    Optional<Person> optionalPerson = getPerson(id);
    if (optionalPerson.isPresent()) {
      return optionalPerson.get();
    } else {
      Person p = new Person(id);
      contestants.add(p);
      return p;
    }
  }

  /**
   * Creates a string representation of the whole model, using a formatter.
   *
   * @return printable string.
   */
  @Override
  public String toString() {
    return formatter.print(contestants, classes, info, minimumTime);
  }

  /**
   * Sets the current persontype to String st
   *
   * @param st the String that sets the current persontype
   */
  public void setCurrentPersonType(String st) {
    currentPersonType = st;
  }

  public void clear() {
    contestants.clear();
  }

  public void setCutOff(String cutOff) {
    DateTimeFormatter format = DateTimeFormatter.ofPattern("HH.mm.ss");
    LocalTime correctcutoff = LocalTime.parse(cutOff, format);
    this.cutoff = correctcutoff;
  }
}
