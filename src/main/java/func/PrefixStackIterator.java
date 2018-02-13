package func;

import dict.PrefixStack;

public class PrefixStackIterator<Key, Value, Result> {
    private final PrefixStack<Key, Value> stack;

    public PrefixStackIterator(PrefixStack<Key, Value> stack) {
        this.stack = stack;
    }


}
