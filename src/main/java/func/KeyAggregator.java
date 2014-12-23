package func;

import dict.ArrayListStack;
import dict.PrefixIterable;
import dict.PrefixStack;

import java.util.HashMap;
import java.util.Map;

public class KeyAggregator<Key1, Key2, Value> {
    private ListFunction<Key1, Key2> map;
    private BinaryOperation<Value> sum;

    public KeyAggregator(ListFunction<Key1, Key2> map, BinaryOperation<Value> sum) {
        this.map = map;
        this.sum = sum;
    }

    public Map<Key2, Value> aggregate(PrefixIterable<Key1, Value> tree) {
        PrefixStack<Key1, Value> stack = tree.prefixStack();
        AdditiveRecurser recurser = new AdditiveRecurser(stack);
        recurser.recurse();
        return recurser.getResults();
    }

    private class AdditiveRecurser {
        private PrefixStack<Key1, Value> stack;
        private Map<Key2, Value> results;
        private ArrayListStack<Key2> keyStack;

        private AdditiveRecurser(PrefixStack<Key1, Value> stack) {
            this.stack = stack;
            this.results = new HashMap<>();
            this.keyStack = new ArrayListStack<>();
            keyStack.push(map.apply());
        }

        public Map<Key2, Value> getResults() {
            return results;
        }

        public void recurse() {
            if (stack.nextKeys().size() == 0) {
                Key2 key2 = keyStack.peek();
                Value val = stack.getValue();
                if (results.containsKey(key2)) {
                    results.put(key2, sum.apply(results.get(key2), val));
                } else {
                    results.put(key2, val);
                }
                return;
            }

            for (Key1 key : stack.nextKeys()) {
                stack.push(key);
                Key2 val = keyStack.peek();
                keyStack.push(map.apply(key, val));

                recurse();

                keyStack.pop();
                stack.pop();
            }
        }
    }
}
