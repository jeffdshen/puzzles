package func.udf;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import dict.LazyList;
import dict.Provider;
import func.UniFunction;

import java.util.List;

public class Reverse<Key> implements UniFunction<Key> {
    @Override
    public List<Key> apply(final List<Key> input) {
        return new LazyList<>(
            new Provider<List<Key>>() {
                @Override
                public List<Key> get() {
                    return Lists.reverse(input);
                }
            }
        );
    }
}
