import java.util.*;

public class Classifier {

    /**
     * A hash map of all words and their spam coefficients.
     * <p>
     */
    private final HashMap<String, Double> allWords;

    public Classifier(List<Email> emails) {
        allWords = new HashMap<>();

        // Calculate word frequencies
        var spamWords = new HashMap<String, Integer>();
        var nonSpamWords = new HashMap<String, Integer>();

        for(Email e: emails) {
            var freqs = Utils.countFrequencies(e.body());
            for(String s: freqs.keySet()) {

                if(!spamWords.containsKey(s) && !nonSpamWords.containsKey(s)) {
                    if(e.isSpam()) {
                        spamWords.put(s, 1);
                    }
                    else {
                        nonSpamWords.put(s, 1);
                    }
                }
                else {
                    if(e.isSpam()) {
                        spamWords.merge(s, freqs.get(s), Integer::sum);
                    } else {
                        nonSpamWords.merge(s, freqs.get(s), Integer::sum);
                    }
                }

            }
        }

        // Calculate each word's spam coefficient
        // (how often it appears in spam emails minus how often it appears in non-spam emails,
        // divided by how often it appears total)

        // Why doesn't Java provide easier ways to work with hash sets?
        // Pretty much every other language isn't this stupid.
        var result = new HashSet<>(spamWords.keySet());
        result.addAll(nonSpamWords.keySet());

        for(String s : result) {
            var spamCount = spamWords.getOrDefault(s, 0);
            var nonSpamCount = nonSpamWords.getOrDefault(s, 0).doubleValue();

            var coefficient = (nonSpamCount - spamCount) / (spamCount + nonSpamCount);
            allWords.put(s, coefficient);
        }

    }

    /**
     * Classifies each email based on the average spam coefficient of each word
     * @param email The body of the email to classify
     * @return A double representing how likely it is that an email is spam
     */
    public Classification classify(String email) {
        var words = Utils.stripText(email).split(" ");
        var scores = Arrays.stream(words)
                .mapToDouble(x -> allWords.getOrDefault(x, 0.5))
                .filter(x -> x != 0.0)
                .toArray();

        var score = Arrays.stream(scores).average();
        var susWords = Arrays.stream(words)
                .sorted(Comparator.comparing(x -> allWords.getOrDefault(x, 0.0).intValue()))
                .sorted(Comparator.reverseOrder())
                .limit(5)
                .toList();

        return new Classification(score.orElse(0), words.length, susWords);

    }

}
