package logger;

public class TableModelEntry {
  String time;
  String id;

  public TableModelEntry(String time, String id) {
    this.time = time;
    this.id = id;
  }

  /**
   * Check if the id is valid.
   *
   * @return Returns true if id is valid, otherwise false.
   */
  public boolean isComplete() {
    return IDReader.isValidID(id);
  }

  @Override
  public String toString() {
    return id + "; " + time;
  }
}
