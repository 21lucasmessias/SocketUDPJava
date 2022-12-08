package server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FortuneDB {
    private final List<String> fortunes = new ArrayList<>(List.of("A", "B", "C", "D"));

    public synchronized String getRandomFortune() {
        int max = fortunes.size() - 1;
        int min = 0;
        int range = max - min + 1;

        return fortunes.get((int)(Math.random() * range));
    }

    public int getRandomFortuneIndex() {
        int max = fortunes.size();
        int min = 0;
        int range = max - min + 1;

        return (int)(Math.random() * range);
    }

    public synchronized String updateFortune(int index, String fortune) {
        final var oldFortune = this.fortunes.get(index);
        this.fortunes.set(index, fortune);

        return oldFortune;
    }
}
