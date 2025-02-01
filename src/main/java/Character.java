public class Character {
    private static String sourceStr = "";

    Character(String source) { this.sourceStr = source; }

    public String getSource() { return sourceStr; }

    private static boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    private boolean checkDigit() {
        for (int i = 0; i < sourceStr.length(); i++) {
            if (Character.isDigit(sourceStr.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    private static boolean isLetter(char c) {
        return c >= 'A' && c <= 'Z' || c >= 'a' && c <= 'z';
    }

    private boolean checkLetter() {
        for(int i = 0; i < sourceStr.length(); i++) {
            if(Character.isLetter(sourceStr.charAt(i))) {
                return true;
            }
        }

        return false;
    }

    private boolean positiveCharacterGroups(String pattern) {
        char[] patternArr = pattern.toCharArray();
        for (char c : patternArr) {
            if (sourceStr.contains(c + "")) {
                return true;
            }
        }
        return false;
    }

    public boolean matchPattern(String inputLine, String pattern) {
        if (pattern.equals("\\d")) {
            return checkDigit();
        } else if (pattern.equals("\\w")) {
            return checkLetter();
        } else if (pattern.charAt(0) == '[' &&
                pattern.charAt(pattern.length() - 1) == ']') {
            return positiveCharacterGroups(
                    pattern.substring(1, pattern.length() - 1));
        } else if (pattern.length() == 1) {
            return inputLine.contains(pattern);
        } else {
            throw new RuntimeException("Unhandled pattern: " + pattern);
        }
    }
}