package func;

import com.google.common.base.Preconditions;
import dict.ArrayListStack;
import dict.PrefixStack;

import java.util.List;
import java.util.Set;

public class IndexedAdditiveFunctionStack<Key, A, B, Value> implements PrefixStack<Key, Value> {
    private final PrefixStack<Key, A> stack;
    private final ListFunction<Key, B> func;
    private final ArrayListStack<B> values;
    private BinaryFunction<A, B, Value> sum;

    public IndexedAdditiveFunctionStack(
        PrefixStack<Key, A> stack, ListFunction<Key, B> func, BinaryFunction<A, B, Value> sum
    ) {
        Preconditions.checkArgument(func.hasProperty(FunctionProperty.INDEXED_ADDITIVE));

        this.stack = stack;
        this.func = func;
        this.sum = sum;

        values = new ArrayListStack<>();
        values.push(func.apply());
    }

    @Override
    public void push(Key key) {
        values.push(func.apply(stack.size(), key, values.peek()));
        stack.push(key);
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
        return sum.apply(stack.getValue(), values.peek());
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
