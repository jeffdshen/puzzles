package func.udf;

import dict.PrefixStack;
import dict.PrefixTree;
import dict.aggregators.ConstantAggregator;
import func.MultiFunction;

import java.util.List;

public class SingleDeletion<Key> implements MultiFunction<Key> {
    @Override
    public PrefixStack<Key, ?> apply(List<Key> input) {
        PrefixTree<Key, Integer> tree = new PrefixTree<Key, Integer>(new ConstantAggregator(1));
        return tree.prefixStack();
    }
}
