package com.user_management_API.automation.util;

public class StringUtil {

    public static String addSpace(String word, int noOfCharac) {

        int wordLength = word.length();
        int noOfSpace = noOfCharac - wordLength;
        if (noOfSpace < 0) {
            return word;
        } else {
            String space = "";
            for (int val = 0; val < noOfSpace; val++) {
                space += " ";
            }
            word = word + space;
        }
        return word;
    }

    /**
     * Adds the indent.
     *
     * @param indentLevel the indent level
     * @return the string
     */
    public static String addIndent(int indentLevel) {

        indentLevel += indentLevel;
        final StringBuilder space = new StringBuilder("");
        for (int i = 0; i < indentLevel; i++) {
            space.append("  ");
        }
        return space.toString();
    }

    /**
     * Adds the underline to log statements.
     *
     * @param indentLevel the indent level
     * @return the string
     */
    public static String addUnderline(final int indentLevel) {

        final StringBuilder space = new StringBuilder("");
        for (int i = 0; i < indentLevel; i++) {
            space.append('-');
        }
        return space.toString();
    }

    /**
     * Change html to plain.
     *
     * @param text the text
     * @return the string
     */
    public static String changeHtmlToPlain(final String text) {

        String plainText = text.replaceAll("\\<.*?\\>", "");
        return plainText;
    }
}
