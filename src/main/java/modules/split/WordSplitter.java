package modules.split;

import com.google.common.collect.ImmutableList;
import dict.PrefixStack;
import dict.PrefixTree;
import func.aggregate.BinaryFunction;
import func.hash.IndexedAdditiveFunction;
import func.hash.IndexedAdditiveFunctionStack;
import org.apache.log4j.Logger;

import java.util.*;

public class WordSplitter<Key> {
    private static final Logger logger = Logger.getLogger(WordSplitter.class.getName());

    private final PrefixTree<Key, Long> tree;
    private final Map<Integer, Long> freqByLength;
    private Sentence<Key> sentence;
    private final Double logFreqTotal;


    public WordSplitter(PrefixTree<Key, Long> tree, Map<Integer, Long> freqByLength, Sentence<Key> sentence) {
        this.tree = tree;
        this.freqByLength = freqByLength;
        this.sentence = sentence;

        long freqTotal = 0;
        for (long i : freqByLength.values()) {
            freqTotal += i;
        }
        this.logFreqTotal = Math.log(freqTotal);

    }

    private PrefixStack<Key, Double> getProbabilityStack(final Sentence<Key> sentence, final int start) {
        return new IndexedAdditiveFunctionStack<>(tree.prefixStack(),
            new IndexedAdditiveFunction<Key, Double>() {
                @Override
                public Double apply() {
                    return 0.0;
                }

                @Override
                public Double apply(int index, Key key, Double prob) {
                    if (start + index >= sentence.size()) {
                        return prob;
                    }

                    if (start + index >= sentence.size() || !sentence.contains(start + index, key)) {
                        return null;
                    }

                    double hit = sentence.getLogProbability(start + index, key);
                    return hit + prob;
                }
            },
            new BinaryFunction<Long, Double, Double>() {
                @Override
                public Double apply(Long freq, Double logProb) {
                    return Math.log(freq) - logFreqTotal + logProb;
                }
            }
        );
    }

    public List<List<Key>> split() {
        Map<Integer, Double> dist = new HashMap<>();
        Map<Integer, List<Key>> prev = new HashMap<>();
        Set<Integer> seen = new HashSet<>();
        dist.put(0, 0.0);

        // nodes are 0, ..., len(sentence). represents the length of the sentence constructed thus far
        while (seen.size() <= sentence.size()) {
            int u = -1;
            for (int i = 0; i <= sentence.size(); i++) {
                if (!seen.contains(i) && dist.containsKey(i) && (u == -1 || dist.get(i) < dist.get(u))) {
                    u = i;
                }
            }

            // no path
            if (u == -1) {
                return null;
            }

            List<List<Key>> path = tracePath(prev, u);
            logger.info(String.format("u = %s, dist[u] = %s, words = %s", u, dist.get(u), path));

            // finished
            if (u == sentence.size()) {
                return path;
            }

            seen.add(u);
            EdgeIterator edges = new EdgeIterator(u, getProbabilityStack(sentence, u), sentence, dist, prev);
            edges.handleKeys();
        }

        return null;
    }

    private List<List<Key>> tracePath(Map<Integer, List<Key>> prev, int size) {
        ImmutableList.Builder<List<Key>> builder = ImmutableList.builder();
        int index = size;
        while (index > 0) {
            if (!prev.containsKey(index)) {
                throw new ArrayIndexOutOfBoundsException(index);
            }
            List<Key> word = prev.get(index);
            builder.add(word);
            index -= word.size();
        }

        return builder.build().reverse();
    }

    // TODO
    private class EdgeIterator {
        private final int u;
        private Sentence<Key> sentence;
        private Map<Integer, Double> dist;
        private Map<Integer, List<Key>> prev;
        private final PrefixStack<Key, Double> stack;

        public EdgeIterator(
            int u,
            PrefixStack<Key, Double> stack,
            Sentence<Key> sentence,
            Map<Integer, Double> dist,
            Map<Integer, List<Key>> prev
        ) {
            this.stack = stack;
            this.u = u;
            this.sentence = sentence;
            this.dist = dist;
            this.prev = prev;
        }

        public void handleKeys() {
            Set<Key> keys = stack.nextKeys();

            // ignore last character
            int v = u + stack.size() - 1;
            if (v > sentence.size()) {
                return;
            }

            if (keys.isEmpty()) {
                // ignore last character
                double alt = dist.get(u) - stack.getValue();
                if (!dist.containsKey(v) || alt < dist.get(v)) {
                    dist.put(v, alt);
                    prev.put(v, stack.getKey().subList(0, stack.size() - 1));
                }
                return;
            }

            for (Key k : keys) {
                stack.push(k);
                handleKeys();
                stack.pop();
            }

        }
    }
}
