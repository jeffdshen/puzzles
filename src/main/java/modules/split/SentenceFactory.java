package modules.split;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class SentenceFactory<Key> {
    /**
     * Compares everything against a list of keys with probabilities p and 1-p
     */
    public Sentence<Key> key(final List<Key> sentence, double prob) {
        final double p = Math.log(prob);
        final double q = Math.log(1 - prob);
        return new Sentence<Key>() {
            @Override
            public int size() {
                return sentence.size();
            }

            @Override
            public boolean contains(int index, Key key) {
                return true;
            }

            @Override
            public double getLogProbability(int index, Key key) {
                return key.equals(sentence.get(index)) ? p : q;
            }
        };
    }

    /**
     * Compares everything against a set (the probability is split evenly among them).
     */
    public Sentence<Key> set(final List<Set<Key>> sentence) {
        final ArrayList<Double> p = new ArrayList<>();
        for (int i = 0; i < sentence.size(); i++) {
            p.add(Math.log(1.0 / sentence.get(i).size()));
        }

        return new Sentence<Key>() {
            @Override
            public int size() {
                return sentence.size();
            }

            @Override
            public boolean contains(int index, Key key) {
                return sentence.get(index).contains(key);
            }

            @Override
            public double getLogProbability(int index, Key key) {
                return p.get(index);
            }
        };
    }
}
