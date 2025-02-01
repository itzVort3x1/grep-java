import java.util.HashSet;
import java.util.Set;

public class Character {
    private static String sourceStr = "";
    private static final Set<java.lang.Character> specialStartCharacters = Set.of('[', '\\', '$');
    private static final Set<String> specialEndCharacters = Set.of("]", "\\w", "\\d");

    Character(String source) { this.sourceStr = source; }

    public String getSource() { return sourceStr; }

    private static boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    private static boolean isLetter(char c) {
        return c >= 'A' && c <= 'Z' || c >= 'a' && c <= 'z';
    }

    private static String getCurrentRegex(String pattern, int currPointer) {
        String currPattern = pattern.substring(currPointer);
        if (currPattern.startsWith("\\d")) {
            return "\\d";
        } else if (currPattern.startsWith("\\w")) {
            return "\\w";
        } else if (currPattern.startsWith("[^")) {
            return currPattern.substring(0, currPattern.indexOf("]", currPattern.indexOf("[^")) + 1);
        } else if (currPattern.startsWith("[")) {
            return currPattern.substring(0, currPattern.indexOf("]", currPattern.indexOf("[")) + 1);
        } else if (currPattern.startsWith("^")) {
            int index =
                    Math.min(currPattern.indexOf("\\") > 0 ? currPattern.indexOf("\\")
                                    : Integer.MAX_VALUE,
                            currPattern.indexOf("[") > 0 ? currPattern.indexOf("[")
                                    : Integer.MAX_VALUE);
            if (index == Integer.MAX_VALUE) {
                return currPattern.substring(currPointer);
            }
            return currPattern.substring(currPointer, index);
        }else {
            return currPattern.substring(0, 1);
        }
    }

    private static boolean notContainsTheseParameters(char inputLine,
                                                      String pattern) {
        char[] list = pattern.toCharArray();
        Set<java.lang.Character> setOfChar = new HashSet<>();
        for (char curr : list) {
            setOfChar.add(curr);
        }
        setOfChar.remove('[');
        setOfChar.remove(']');
        setOfChar.remove('^');
        return !setOfChar.contains(inputLine);
    }

    private static boolean containsTheseParameters(char inputChar,
                                                   String pattern) {
        char[] list = pattern.toCharArray();
        Set<java.lang.Character> setOfChar = new HashSet<>();
        for (char curr : list) {
            setOfChar.add(curr);
        }
        setOfChar.remove('[');
        setOfChar.remove(']');
        return setOfChar.contains(inputChar);
    }

    private static boolean hasAlphaNumericAndUnderScore(char inputChar) {
        return (inputChar >= '0' && inputChar <= '9') ||
                (inputChar >= 'a' && inputChar <= 'z') ||
                (inputChar >= 'A' && inputChar <= 'Z') || (inputChar == '_');
    }

    private static boolean hasNumbers(char inputChar) {
        return inputChar >= '0' && inputChar <= '9';
    }

    private static String findEndString(String pattern) {
        int maxIndex = Integer.MIN_VALUE;
        String maxCurr = "";
        for (String s : specialEndCharacters) {
            if (pattern.lastIndexOf(s) > maxIndex) {
                maxIndex = pattern.lastIndexOf(s);
                maxCurr = s;
            }
        }
        if (maxIndex == -1) {
            return pattern.substring(0, pattern.length() - 1);
        }
        return pattern.substring(maxIndex + maxCurr.length(), pattern.length() - 1);
    }
    private static String findStartString(String pattern) {
        for (int i = 1; i < pattern.length(); i++) {
            if (specialStartCharacters.contains(pattern.charAt(i))) {
                return pattern.substring(1, i);
            }
        }
        return pattern.substring(1);
    }

    public boolean matchPattern(String inputLine, String pattern) {
        int patternPointer = 0;
        int inputPointer = 0;
        boolean ans;
        if (pattern.startsWith("^")) {
            String startPattern = findStartString(pattern);
            if (!inputLine.startsWith(startPattern))
                return false;
            pattern = pattern.substring(startPattern.length());
        }
        if (pattern.endsWith("$")) {
            String endPattern = findEndString(pattern);
            if (!inputLine.endsWith(endPattern))
                return false;
            pattern = pattern.substring(0, pattern.length() - endPattern.length());
        }
        while (inputPointer < inputLine.length()) {
            String currRegex = getCurrentRegex(pattern, patternPointer);
            patternPointer += currRegex.length();
            if (currRegex.equals("\\d")) {
                ans = hasNumbers(inputLine.charAt(inputPointer));
            } else if (currRegex.equals("\\w")) {
                ans = hasAlphaNumericAndUnderScore(inputLine.charAt(inputPointer));
            } else if (currRegex.contains("[^") && currRegex.contains("]")) {
                ans = notContainsTheseParameters(inputLine.charAt(inputPointer), currRegex);
            } else if (currRegex.contains("[") && currRegex.contains("]")) {
                ans = containsTheseParameters(inputLine.charAt(inputPointer), currRegex);
            } else if (currRegex.length() > 1) {
                ans = inputLine.equals(currRegex.substring(1));
                inputPointer += currRegex.substring(1).length();
            } else if (currRegex.length() == 1) {
                ans = inputLine.charAt(inputPointer) == currRegex.charAt(0);
            } else {
                throw new RuntimeException("Unhandled pattern: " + pattern);
            }
            if (!ans) {
                patternPointer = 0;
            }
            inputPointer++;
            if (ans && patternPointer == pattern.length())
                return true;
            if (inputPointer >= inputLine.length())
                return false;
        }
        return false;
    }
}