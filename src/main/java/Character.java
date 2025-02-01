import java.util.HashSet;
import java.util.Set;

public class Character {
    private static String sourceStr = "";

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
        } else {
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

    public boolean matchPattern(String inputLine, String pattern) {
        int patternPointer = 0;
        int inputPointer = 0;
        boolean ans;
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