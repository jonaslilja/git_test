package logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * The purpose of TimeLogger is to append startn number and current time to a file if it exists. If
 * the file doesn't exist, TimeLogger creates a new file with correct header formatting.
 */
public class TimeLogger {
  /**
   * If filename exists, appends startnr and current time to file. If filename doesn't exist, create
   * file with correct formatting and log the startnr and current time
   *
   * @param startnr starting number
   * @param filename name of the file to append/create
   * @return returns the formatted entry
   * @throws IOException
   */
  public String log(String startnr, String filename) throws IOException {
    DateTimeFormatter format = DateTimeFormatter.ofPattern("HH.mm.ss");
    LocalTime loggedTime = LocalTime.now();
    Path path = Paths.get(filename);
    String result = startnr + "; " + loggedTime.format(format);
    String data = "\n" + result;

    if (Files.exists(path)) {
      Files.write(path, data.getBytes(), StandardOpenOption.APPEND);
    } else {
      data = "StartNr;StartTid" + data;
      Files.write(path, data.getBytes());
    }

    return result;
  }
}
