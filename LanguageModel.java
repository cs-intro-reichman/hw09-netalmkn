import java.util.HashMap;
import java.util.Random;

public class LanguageModel {

    HashMap<String, List> CharDataMap;
    int windowLength;
    private Random randomGenerator;

    public LanguageModel(int windowLength, int seed) {
        this.windowLength = windowLength;
        randomGenerator = new Random(seed);
        CharDataMap = new HashMap<>();
    }

    public LanguageModel(int windowLength) {
        this.windowLength = windowLength;
        randomGenerator = new Random();
        CharDataMap = new HashMap<>();
    }

    public void train(String fileName) {
        String window = "";
        In in = new In(fileName);

        while (window.length() < windowLength && !in.isEmpty()) {
            window += in.readChar();
        }

        while (!in.isEmpty()) {
            char c = in.readChar();

            List probs = CharDataMap.get(window);
            if (probs == null) {
                probs = new List();
                CharDataMap.put(window, probs);
            }

            probs.update(c);
            window = window.substring(1) + c;
        }

        for (List probs : CharDataMap.values()) {
            calculateProbabilities(probs);
        }
    }

    void calculateProbabilities(List probs) {
        CharData[] arr = probs.toArray();

        int total = 0;
        for (CharData cd : arr) total += cd.count;

        double cumulative = 0.0;
        for (CharData cd : arr) {
            cd.p = (double) cd.count / total;
            cumulative += cd.p;
            cd.cp = cumulative;
        }
    }

    char getRandomChar(List probs) {
        double r = randomGenerator.nextDouble();
        CharData[] arr = probs.toArray();

        for (CharData cd : arr) {
            if (cd.cp >= r) return cd.chr;   // ← FIX קריטי
        }
        return arr[arr.length - 1].chr;
    }

    public String generate(String initialText, int textLength) {
        if (initialText.length() < windowLength) return initialText;

        StringBuilder generated = new StringBuilder(initialText);
        String window = generated.substring(generated.length() - windowLength);

        while (generated.length() < textLength) {
            List probs = CharDataMap.get(window);
            if (probs == null) break;

            char next = getRandomChar(probs);
            generated.append(next);
            window = generated.substring(generated.length() - windowLength);
        }

        return generated.toString();
    }

    public String toString() {
        StringBuilder str = new StringBuilder();
        for (String key : CharDataMap.keySet()) {
            str.append(key).append(" : ").append(CharDataMap.get(key)).append("\n");
        }
        return str.toString();
    }
}

