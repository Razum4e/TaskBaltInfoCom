package ru.baltinfocom;

public class Main {

    private final static String PATH = "src\\main\\resources\\";
    private final static String NAME_READ_FILE = "lng.csv";
    private final static String NAME_WRITE_FILE = "text.txt";
    private final static int SUM_COLUMNS = 3;
    private final static char DEFAULT_SEPARATOR = ';';
    private final static char DEFAULT_DELIMITER = '\"';

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        SearchEngine searchEngine = new SearchEngine(PATH, NAME_READ_FILE, NAME_WRITE_FILE, SUM_COLUMNS,
                DEFAULT_SEPARATOR, DEFAULT_DELIMITER);
        int sumGroups = searchEngine.start();
        System.out.println("Количество групп: " + sumGroups);
        System.out.println("Время работы: " + ((System.currentTimeMillis() - startTime) / 1000) + " сек.");
    }
}
