import com.google.common.collect.Lists;
import dict.PrefixTree;
import func.udf.Addition;
import func.KeyAggregator;
import func.udf.Length;
import org.apache.log4j.PropertyConfigurator;
import parse.FrequencyParser;

import java.io.InputStreamReader;
import java.util.Map;

public class Driver {
    public static void main(String[] args) {
        PropertyConfigurator.configure(Driver.class.getResource("config/log4j.properties"));

        FrequencyParser parser = new FrequencyParser();
        InputStreamReader reader = new InputStreamReader(Driver.class.getResourceAsStream("dict/invokeit-freq-2012.txt"));
        PrefixTree<Character, Long> tree = parser.parse(reader);
        KeyAggregator<Character, Integer, Long> length = new KeyAggregator<>(new Length<Character>(), new Addition());
        Map<Integer, Long> frequencyByLength = length.aggregate(tree);
        WordSplitter<Character> splitter = new WordSplitter<>(tree, frequencyByLength, 0.97);

        System.out.println(splitter.split(Lists.charactersOf("DYLBNACANDINVINTHE")));
    }
}
