package dict;

import java.util.List;
import java.util.Set;

/**
 * A data structure that is traversable using a stack of keys.
 * getKey always matches only those keys that are pushed and popped
 * (e.g. even if the prefix stack is initialized with a node that isn't the root.)
 * @param <Key>
 * @param <Value>
 */
public interface PrefixStack<Key, Value> extends Pair<List<Key>, Value>{
    public void push(Key key);
    public Key pop();
    public Key peek();
    public List<Key> getKey();
    public Value getValue();
    public Set<Key> nextKeys();
    public int size();
}
