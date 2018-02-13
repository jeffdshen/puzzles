package modules.split;

/**
 * All known information about a sentence - whether a key can appear at a given index, and what the log probability is
 */
public interface Sentence<Key> {
    /**
     * Size of the sentence
     */
    int size();

    /**
     * Whether or not the sentence can contain the key at the given index. Behavior for index >= size is undefined
     */
    boolean contains(int index, Key key);

    /**
     * The log of the probability of the sentence is. Behavior for index >= size is undefined
     */
    double getLogProbability(int index, Key key);

}
