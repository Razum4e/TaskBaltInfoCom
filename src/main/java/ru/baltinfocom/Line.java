package ru.baltinfocom;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

public class Line {
    private final char separator;
    private final char delimiter;
    private final List<Word> lines = new ArrayList<>();

    public Line(String[] strings, char separator, char delimiter) {
        for (int i = 0; i < strings.length; i++)
            lines.add(new Word(i, strings[i]));
        this.separator = separator;
        this.delimiter = delimiter;
    }

    public List<Word> getLines() {
        return lines;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Line that = (Line) o;
        return lines.equals(that.lines);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lines);
    }

    @Override
    public String toString() {
        StringJoiner joiner = new StringJoiner(String.valueOf(separator));
        for (Word word : lines)
            joiner.add(delimiter + word.toString() + delimiter);
        return joiner.toString();
    }
}
