package ru.baltinfocom;

import java.util.Objects;

public class Word {
    private final int numColumn;
    private final String word;
    private final boolean empty;

    public Word(int numColumn, String word) {
        this.numColumn = numColumn;
        this.word = word;
        empty = word.isEmpty();
    }

    public boolean isEmpty() {
        return empty;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Word word = (Word) o;
        return numColumn == word.numColumn &&
                this.word.equals(word.word);
    }

    @Override
    public int hashCode() {
        return Objects.hash(numColumn, word);
    }

    @Override
    public String toString() {
        return word;
    }
}
