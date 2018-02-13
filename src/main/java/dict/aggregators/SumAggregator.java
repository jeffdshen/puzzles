package dict.aggregators;

import dict.Pair;
import dict.PrefixAggregator;

import java.util.List;

public class SumAggregator<Key> implements PrefixAggregator<Key, Long> {
    @Override
    public Long aggregate(List<Key> prefix, Iterable<? extends Pair<Key, Long>> data) {
        long sum = 0;
        for (Pair<Key, Long> datum : data) {
            sum += datum.getValue();
        }
        return sum;
    }
}
