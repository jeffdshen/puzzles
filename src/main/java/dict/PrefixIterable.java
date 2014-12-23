package dict;

import java.util.List;

public interface PrefixIterable<Key, Value> {
    public PrefixStack<Key, Value> prefixStack();
    public PrefixStack<Key, Value> prefixStack(List<Key> prefix);
}
