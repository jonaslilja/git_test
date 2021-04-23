package logger;

class IDReader {

  /**
   * Checks if s is a valid number and below 4 digits
   *
   * @param s The string that is to be checked.
   * @return True if string is valid, otherwise false.
   */
  static boolean isValidID(String s) {
    boolean isValidInteger = false;
    int validInt;
    try {
      validInt = Integer.parseInt(s);
      isValidInteger = validInt < 1000 && validInt > 0;
    } catch (NumberFormatException ex) {
      return false;
    }
    return isValidInteger;
  }
}
