package server;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Hangman {

    final private List<String> words = List.of(
            "exceto",
            "cinico",
            "ciencia",
            "empatia",
            "escopo",
            "indole",
            "prazer",
            "buscar",
            "ciente",
            "comico",
            "dadiva",
            "insano"
    );
    public List<String> characters = new ArrayList<>(List.of());
    private String word;

    private int getRandomIndex() {
        int max = words.size() - 1;
        int min = 0;
        int range = max - min + 1;

        return (int) (Math.random() * range);
    }

    public void refresh() {
        this.word = words.get(this.getRandomIndex());
        this.characters.clear();
    }

    public String getPartialWord() {
        return Stream.of(this.word.split("")).map(w -> {
            if (this.characters.contains(w)) {
                return w;
            }
            return "_";
        }).collect(Collectors.joining()).toLowerCase(Locale.ROOT);
    }

    public boolean hasCharInWord(String c) {
        return this.word.contains(c);
    }
}
