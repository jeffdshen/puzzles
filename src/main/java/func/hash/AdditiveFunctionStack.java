package func.hash;

import com.google.common.base.Preconditions;
import dict.ArrayListStack;
import dict.PrefixStack;

import java.util.List;
import java.util.Set;

public class AdditiveFunctionStack<Key, Value> implements PrefixStack<Key, Value> {
    private final PrefixStack<Key, ?> stack;
    private final HashFunction<Key, Value> func;
    private final ArrayListStack<Value> values;

    public AdditiveFunctionStack(PrefixStack<Key, ?> stack, HashFunction<Key, Value> func) {
        Preconditions.checkArgument(func.hasProperty(FunctionProperty.INDEXED_ADDITIVE));

        this.stack = stack;
        this.func = func;
        values = new ArrayListStack<>();
        values.push(func.apply());
    }

    @Override
    public void push(Key key) {
        stack.push(key);
        values.push(func.apply(key, values.peek()));
    }

    @Override
    public Key pop() {
        values.pop();
        return stack.pop();
    }

    @Override
    public Key peek() {
        return stack.peek();
    }

    @Override
    public List<Key> getKey() {
        return stack.getKey();
    }

    @Override
    public Value getValue() {
        return values.peek();
    }

    @Override
    public Set<Key> nextKeys() {
        return stack.nextKeys();
    }

    @Override
    public int size() {
        return stack.size();
    }
}
