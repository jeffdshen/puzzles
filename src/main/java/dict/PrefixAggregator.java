package dict;

import java.util.List;

public interface PrefixAggregator<Key, Value> {
    Value aggregate(List<Key> prefix, Iterable<? extends Pair<Key, Value>> data);
}
