package ru.baltinfocom;

import java.io.*;
import java.util.*;

public class SearchEngine {
    private final String path;
    private final String nameReadFile;
    private final String nameWriteFile;
    private final int sumColumns;
    private final char separator;
    private final char delimiter;
    private static Map<Word, ArrayList<Line>> groups = new HashMap<>();
    private static final Map<Integer, ArrayList<List<Line>>> sortedGroups = new TreeMap<>(Collections.reverseOrder());

    public SearchEngine(String path, String nameReadFile, String nameWriteFile, int sumColumns,
                        char separator, char delimiter) {
        this.path = path;
        this.nameReadFile = nameReadFile;
        this.nameWriteFile = nameWriteFile;
        this.sumColumns = sumColumns;
        this.separator = separator;
        this.delimiter = delimiter;
    }

    public int start() {
        readFile();
        int sumGroups = sortedRows();
        recordFile();
        return sumGroups;
    }

    private void readFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(path + nameReadFile))) {
            Set<Line> uniqueLines = new HashSet<>();
            String readLine;
            //читаю по строчно
            while ((readLine = reader.readLine()) != null) {
                String[] lineStrings = readLine.split(String.valueOf(separator));
                if (lineStrings.length != sumColumns)
                    continue;
                for (int i = 0; i < lineStrings.length; i++)
                    lineStrings[i] = lineStrings[i].replace(String.valueOf(delimiter), "");
                // создание объекта из массива строк
                Line line = new Line(lineStrings, separator, delimiter);
                // фильтрую не уникальные строки файла
                if (uniqueLines.add(line)) {
                    //перебор каждой строки для сохранения в мапу
                    for (Word word : line.getLines()) {
                        //исключаю пустые строки
                        if (word.isEmpty())
                            continue;
                        //сохраняю все в мапу
                        ArrayList<Line> lines;
                        if (!groups.containsKey(word))
                            lines = new ArrayList<>();
                        else
                            lines = groups.get(word);
                        lines.add(line);
                        groups.put(word, lines);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int sortedRows() {
        int sumGroups = 0;
        //удаляю группы, у которых нет совпадений
        Map<Word, ArrayList<Line>> newGroups = new HashMap<>();
        for (Map.Entry<Word, ArrayList<Line>> entry : groups.entrySet())
            if (entry.getValue().size() > 1)
                newGroups.put(entry.getKey(), entry.getValue());
        groups = newGroups;
        //считаем сколько раз встречается определенная строка
        Map<Line, Integer> lineCount = new HashMap<>();
        for (Map.Entry<Word, ArrayList<Line>> entry : groups.entrySet()) {
            for (Line line : entry.getValue()) {
                if (!lineCount.containsKey(line))
                    lineCount.put(line, 1);
                else
                    lineCount.put(line, lineCount.get(line) + 1);
            }
        }
            /*обход по каждой строке(с кол-вом встречаемости), отсеиваю меньше двух
            и сохраняю каждое непустое значение снова проверяя и удаляя из общей мапы*/
        Set<Set<Line>> lineGroups = new HashSet<>();
        for (Map.Entry<Line, Integer> entry : lineCount.entrySet()) {
            if (entry.getValue() > 1) {
                Set<Line> set = new HashSet<>();
                for (Word word : entry.getKey().getLines()) {
                    if (word.isEmpty())
                        continue;
                    if (groups.containsKey(word)) {
                        set.addAll(groups.remove(word));
                    }
                }
                if (set.size() > 1)
                    lineGroups.add(set);
            }
        }
        for (Set<Line> lineGroup : lineGroups) {
            ArrayList<List<Line>> value;
            if (!sortedGroups.containsKey(lineGroup.size()))
                value = new ArrayList<>();
            else
                value = sortedGroups.get(lineGroup.size());
            List<Line> group = new ArrayList<>(new ArrayList<>(lineGroup));
            value.add(group);
            sortedGroups.put(group.size(), value);
            sumGroups++;
        }
        for (Map.Entry<Word, ArrayList<Line>> entry : groups.entrySet()) {
            ArrayList<List<Line>> value;
            if (!sortedGroups.containsKey(entry.getValue().size()))
                value = new ArrayList<>();
            else
                value = sortedGroups.get(entry.getValue().size());
            List<Line> group = new ArrayList<>(entry.getValue());
            value.add(group);
            sortedGroups.put(group.size(), value);
            sumGroups++;
        }
        return sumGroups;
    }

    private void recordFile() {
        File file = new File(path + nameWriteFile);
        try (BufferedWriter bf = new BufferedWriter(new FileWriter(file, false))) {
            file.createNewFile();
            int sum = 1;
            for (Map.Entry<Integer, ArrayList<List<Line>>> entry : sortedGroups.entrySet()) {
                for (List<Line> group : entry.getValue()) {
                    bf.write("\n" + "Группа № " + sum++);
                    for (Line line : group)
                        bf.write("\n" + line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
