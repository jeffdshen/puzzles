import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import dict.PrefixTree;
import func.aggregate.Addition;
import func.aggregate.KeyAggregator;
import func.udf.Length;
import modules.split.Sentence;
import modules.split.SentenceFactory;
import modules.split.WordSplitter;
import org.apache.log4j.PropertyConfigurator;
import parse.FrequencyParser;

import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Driver {
    public static void main(String[] args) {
        PropertyConfigurator.configure(Driver.class.getResource("config/log4j.properties"));

        FrequencyParser parser = new FrequencyParser();
        InputStreamReader reader = new InputStreamReader(Driver.class.getResourceAsStream("dict/google-freq-all"));
        PrefixTree<Character, Long> tree = parser.parse(reader);
        KeyAggregator<Character, Integer, Long> length = new KeyAggregator<>(new Length<Character>(), new Addition());
        Map<Integer, Long> frequencyByLength = length.aggregate(tree);
        SentenceFactory<Character> sentenceFactory = new SentenceFactory<>();

        Sentence<Character> sentenceSet = sentenceFactory.set(
            Lists.transform(
                ImmutableList.of(
                    "WAGES",
                    "MOSES",
                    "APHORISTIC",
                    "MAYER",
                    "INEPT",
                    "TREACHEROUS",
                    "CANOODLE",
                    "JEKYLL",
                    "EMAIL",
                    "THUNDER",
                    "INMATES",
                    "DEALT"
                ),
                new Function<String, Set<Character>>() {
                    @Override
                    public Set<Character> apply(String input) {
                        return new HashSet<>(Lists.charactersOf(input));
                    }
                }
            )
        );

        Sentence<Character> sentenceKey = sentenceFactory.key(Lists.charactersOf("DYLBNACANDINVINTHE"), 0.99);

        WordSplitter<Character> splitter = new WordSplitter<>(
            tree,
            frequencyByLength,
            sentenceSet
        );

        System.out.println(splitter.split());
    }
}
