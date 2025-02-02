import java.io.IOException;
import java.util.Scanner;
public class Main {
  public static void main(String[] args) {
    if (args.length != 2 || !args[0].equals("-E")) {
      System.out.println("Usage: ./your_program.sh -E <pattern>");
      System.exit(1);
    }
    String pattern = args[1];
    Scanner scanner = new Scanner(System.in);
    String inputLine = scanner.nextLine();
    // You can use print statements as follows for debugging, they'll be visible when running tests.
    System.err.println("Logs from your here program will appear here!");
    // Uncomment this block to pass the first stage
    //
    if (matchPattern(inputLine, pattern)) {
      System.exit(0);
    } else {
      System.exit(1);
    }
  }
  public static boolean matchPattern(String inputLine, String pattern) {
    if (pattern.contains("(") && pattern.contains(")")) {
      String beforeBracket = pattern.substring(0, pattern.indexOf('('));
      String afterBracket = pattern.substring(pattern.indexOf(')') + 1);
      String[] subPatterns = pattern.substring(pattern.indexOf('(') + 1, pattern.indexOf(')')).split("\\|");
      for (String subPattern : subPatterns) {
        String newPattern = beforeBracket + subPattern + afterBracket;
        if (matchPattern(inputLine, newPattern)) {
          return true;
        }
      }
      return false;
    } else if ("\\d".equals(pattern)) {
      return inputLine.matches(".*\\d.*");
    } else if ("\\w".equals(pattern)) {
      return inputLine.matches(".*\\w.*");
    } else if (pattern.startsWith("[^") && pattern.endsWith("]")) {
      String charGroup = pattern.substring(2, pattern.length() - 1);
      return inputLine.matches(".*[^" + charGroup + "].*");
    } else if (pattern.startsWith("[") && pattern.endsWith("]")) {
      String charGroup = pattern.substring(1, pattern.length() - 1);
      return inputLine.matches(".*[" + charGroup + "].*");
    } else if (pattern.contains("\\d") || pattern.contains("\\w")) {
      for (int i = 0; i < inputLine.length(); i++) {
        boolean match = matchPatternV2(inputLine.substring(i), pattern);
        if (match) {
          return true;
        }
      }
      return false;
    } else if (pattern.startsWith("^")) {
      return inputLine.startsWith(pattern.substring(1));
    } else if (pattern.endsWith("$")) {
      return inputLine.endsWith(pattern.substring(0, pattern.length() - 1));
//        } else if (pattern.contains("+")) {
//            for (int i = 0; i < inputLine.length(); i++) {
//                boolean match = matchOneOrMore(inputLine.substring(i), pattern);
//                if (match) {
//                    return true;
//                }
//            }
//            return false;
    }
    if (pattern.contains(".+")) {
//            split the pattern into two parts on the basis of ".+"
      String[] parts = pattern.split("\\.\\+");
      String firstPart = parts[0];
      String secondPart = parts[1];
      for (int i = 0; i < inputLine.length(); i++) {
        if (inputLine.substring(i).startsWith(firstPart)) {
          for (int j = i + firstPart.length(); j < inputLine.length(); j++) {
            if (inputLine.substring(j).startsWith(secondPart)) {
              return true;
            }
          }
        }
      }
      return false;
    }
    if (pattern.contains("+") || pattern.contains("?") || pattern.contains(("."))) {
      for (int i = 0; i < inputLine.length(); i++) {
        boolean match = handleForTheseTogether(inputLine.substring(i), pattern);
        if (match) {
          return true;
        }
      }
      for (int i = 0; i < pattern.length(); i++) {
        boolean match = handleForTheseTogether(inputLine, pattern.substring(i));
        if (match) {
          return true;
        }
      }
      return false;
//        }
//        else if (pattern.contains("?")) {
//            for (int i = 0; i < inputLine.length(); i++) {
//                if (matchZeroOrOne(inputLine.substring(i), pattern)) {
//                    return true;
//                }
//            }
//            for (int i = 0; i < pattern.length(); i++) {
//                if (matchZeroOrOne(inputLine, pattern.substring(i))) {
//                    return true;
//                }
//            }
//            return false;
//        } else if (pattern.contains(".")) {
//            if (pattern.length() != inputLine.length()) {
//                return false;
//            }
//            for (int i = 0; i < inputLine.length(); i++) {
//                if (pattern.charAt(i) != '.' && pattern.charAt(i) != inputLine.charAt(i)) {
//                    return false;
//                }
//            }
//            return true;
    } else if (pattern.length() == inputLine.length()) {
      return inputLine.equals(pattern);
    }
    else if (pattern.length() == 1) {
      return inputLine.contains(pattern);
    } else {
      throw new RuntimeException("Unhandled pattern: " + pattern);
    }
  }
  public static boolean matchPatternV2(String inputLine, String pattern) {
    return matchPatternRecursive(inputLine, pattern, 0, 0);
  }
  public static boolean matchPatternRecursive(String inputLine, String pattern, int inputIndex, int patternIndex) {
    if (patternIndex == pattern.length()) {
      return true;
    }
    if (patternIndex < pattern.length() && pattern.charAt(patternIndex) == '\\') {
      patternIndex++;
      if (patternIndex < pattern.length() && pattern.charAt(patternIndex) == 'd') {
        return inputIndex < inputLine.length() && Character.isDigit(inputLine.charAt(inputIndex)) && matchPatternRecursive(inputLine, pattern, inputIndex + 1, patternIndex + 1);
      } else if (patternIndex < pattern.length() && pattern.charAt(patternIndex) == 'w') {
        return inputIndex < inputLine.length() && Character.isLetterOrDigit(inputLine.charAt(inputIndex)) && matchPatternRecursive(inputLine, pattern, inputIndex + 1, patternIndex + 1);
      }
    } else {
      if (inputIndex < inputLine.length() && patternIndex < pattern.length() && inputLine.charAt(inputIndex) == pattern.charAt(patternIndex)) {
        return matchPatternRecursive(inputLine, pattern, inputIndex + 1, patternIndex + 1);
      }
    }
    return false;
  }
  public static boolean matchOneOrMore(String inputLine, String pattern) {
    return matchOneOrMoreRecursive(inputLine, pattern, 0, 0);
  }
  public static boolean matchOneOrMoreRecursive(String inputLine, String pattern, int inputIndex, int patternIndex) {
    if (patternIndex == pattern.length()) {
      return true;
    }
    if (inputIndex < inputLine.length() && patternIndex < pattern.length() && inputLine.charAt(inputIndex) == pattern.charAt(patternIndex)) {
      return matchOneOrMoreRecursive(inputLine, pattern, inputIndex + 1, patternIndex + 1);
    }
    if (patternIndex < pattern.length() && pattern.charAt(patternIndex) == '+') {
      int countInPattern = 1;
      int indexAfterPlus = patternIndex + 1;
      while (indexAfterPlus < pattern.length() && pattern.charAt(patternIndex - 1) == pattern.charAt(indexAfterPlus)) {
        countInPattern++;
        indexAfterPlus++;
      }
      int countInInput = 1;
      int indexInInput = inputIndex;
      while (indexInInput < inputLine.length() && inputLine.charAt(inputIndex - 1) == inputLine.charAt(indexInInput)) {
        countInInput++;
        indexInInput++;
      }
      if (countInInput >= countInPattern) {
        return matchOneOrMoreRecursive(inputLine, pattern, indexInInput, indexAfterPlus);
      } else {
        return false;
      }
    }
    return false;
  }
  public static boolean matchZeroOrOne(String inputLine, String pattern) {
    return matchZeroOrOneRecursive(inputLine, pattern, 0, 0);
  }
  public static boolean matchZeroOrOneRecursive(String inputLine, String pattern, int inputIndex, int patternIndex) {
    if (patternIndex == pattern.length()) {
      return true;
    }
    if (inputIndex < inputLine.length() && patternIndex < pattern.length() && inputLine.charAt(inputIndex) == pattern.charAt(patternIndex)) {
      return matchZeroOrOneRecursive(inputLine, pattern, inputIndex + 1, patternIndex + 1);
    }
    if (patternIndex < pattern.length() && pattern.charAt(patternIndex) == '?') {
      return matchZeroOrOneRecursive(inputLine, pattern, inputIndex, patternIndex + 1) || matchZeroOrOneRecursive(inputLine, pattern, inputIndex + 1, patternIndex + 1);
    }
    return false;
  }
  public static boolean handleForTheseTogether(String inputLine, String pattern) {
    return handleForTheseTogetherRecursive(inputLine, pattern, 0, 0);
  }
  public static boolean handleForTheseTogetherRecursive(String inputLine, String pattern, int inputIndex, int patternIndex) {
    if (patternIndex == pattern.length()) {
      return true;
    }
    if (inputIndex < inputLine.length() && patternIndex < pattern.length() && inputLine.charAt(inputIndex) == pattern.charAt(patternIndex)) {
      return handleForTheseTogetherRecursive(inputLine, pattern, inputIndex + 1, patternIndex + 1);
    }
    if (patternIndex < pattern.length() && pattern.charAt(patternIndex) == '+') {
      int countInPattern = 1;
      int indexAfterPlus = patternIndex + 1;
      while (indexAfterPlus < pattern.length() && pattern.charAt(patternIndex - 1) == pattern.charAt(indexAfterPlus)) {
        countInPattern++;
        indexAfterPlus++;
      }
      int countInInput = 1;
      int indexInInput = inputIndex;
      while (indexInInput < inputLine.length() && inputLine.charAt(inputIndex - 1) == inputLine.charAt(indexInInput)) {
        countInInput++;
        indexInInput++;
      }
      if (countInInput >= countInPattern) {
        return handleForTheseTogetherRecursive(inputLine, pattern, indexInInput, indexAfterPlus);
      } else {
        return false;
      }
    }
    if (patternIndex < pattern.length() && pattern.charAt(patternIndex) == '?') {
      return handleForTheseTogetherRecursive(inputLine, pattern, inputIndex, patternIndex + 1) || handleForTheseTogetherRecursive(inputLine, pattern, inputIndex + 1, patternIndex + 1);
    }
    if (patternIndex < pattern.length() && pattern.charAt(patternIndex) == '.') {
      return handleForTheseTogetherRecursive(inputLine, pattern, inputIndex + 1, patternIndex + 1);
    }
    return false;
  }
}