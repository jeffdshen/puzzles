package dict.aggregators;

import dict.Pair;
import dict.PrefixAggregator;
import dict.PrefixTree;

import java.util.List;

public class ConstantAggregator<Key, Value> implements PrefixAggregator<Key, Value> {
    private Value value;

    public ConstantAggregator(Value value) {
        this.value = value;
    }

    @Override
    public Value aggregate(List<Key> prefix, Iterable<? extends Pair<Key, Value>> data) {
        return value;
    }
}
