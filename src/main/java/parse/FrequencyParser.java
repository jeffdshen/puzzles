package parse;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import dict.PrefixTree;
import dict.aggregators.SumAggregator;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

public class FrequencyParser {
    private static final Logger logger = Logger.getLogger(FrequencyParser.class.getName());
    public static final String DEFAULT_DELIMITER = " \t";
    public static final String DEFAULT_TERMINATOR = "\0";
    public static final Function<String, List<Character>> UPPERCASE =
        new Function<String, List<Character>>() {
            @Override
            public List<Character> apply(String input) {
                return Lists.charactersOf((input + DEFAULT_TERMINATOR).toUpperCase(Locale.ENGLISH));
            }
        };

    public static final Function<String, List<Character>> UPPERCASE_ALPHANUMERIC =
        new Function<String, List<Character>>() {
            @Override
            public List<Character> apply(String input) {
                String stripped = input.replaceAll("[^0-9A-Za-z]+", "");
                return Lists.charactersOf((stripped + DEFAULT_TERMINATOR).toUpperCase(Locale.ENGLISH));
            }
        };


    private final String delimiter;
    private Function<String, List<Character>> normalizer;


    public FrequencyParser() {
        this(DEFAULT_DELIMITER, UPPERCASE);
    }

    public FrequencyParser(String delimiter, Function<String, List<Character>> normalizer) {
        this.delimiter = delimiter;
        this.normalizer = normalizer;
    }

    public PrefixTree<Character, Long> parse(Reader reader) {
        PrefixTree<Character, Long> tree = new PrefixTree<>(new SumAggregator<Character>());
        int errors = 0;

        try (BufferedReader in = new BufferedReader(reader)) {
            int i = 0;
            for (String s = in.readLine(); s != null; s = in.readLine()) {
                try {
                    StringTokenizer st = new StringTokenizer(s, delimiter);
                    int tokens = st.countTokens();
                    if (i % 100000 == 0) {
                        logger.info(i);
                    }
                    i++;
                    if (tokens >= 2) {
                        String word = st.nextToken();
                        long freq = Long.parseLong(st.nextToken());
                        tree.insert(normalizer.apply(word), freq);
                    } else {
                        logger.debug(String.format("Fewer than two tokens on line %s", i));
                        errors++;
                    }
                } catch (Exception e) {
                    logger.debug(String.format("Error on line %s", i), e);
                    errors++;
                }
            }
        } catch (IOException e) {
            logger.error("Error parsing frequency dictionary", e);
        }

        if (errors > 0) {
            logger.error(String.format("Frequency dictionary had errors in %s lines.", errors));
        }

        logger.info("Done");
        return tree;
    }
}
