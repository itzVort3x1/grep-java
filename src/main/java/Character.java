public class Character {
    private static String sourceStr = "";
    Character(String source) { this.sourceStr = source; }
    public String getSource() { return sourceStr; }
    public static boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }
    public boolean checkDigit() {
        for (int i = 0; i < sourceStr.length(); i++) {
            if (Character.isDigit(sourceStr.charAt(i))) {
                return true;
            }
        }
        return false;
    }
    public boolean matchPattern(String inputLine, String pattern) {
        if (checkDigit()) {
            return true;
        } else if (pattern.length() == 1) {
            return inputLine.contains(pattern);
        } else {
            throw new RuntimeException("Unhandled pattern: " + pattern);
        }
    }
}