package com.artog.flashlearn.util;

public class MemoryCueValidator {

    /**
     * Ensures the answer contains at least one of the required memory cues.
     * @param answer The flashcard answer text
     * @return true if valid, false otherwise
     */
    public static boolean isValid(String answer) {
        if (answer == null || answer.isBlank()) return false;

        // Must contain at least one of <strong>, <u>, <em>
        return answer.contains("<strong>")
            || answer.contains("<u>")
            || answer.contains("<em>");
    }
}


