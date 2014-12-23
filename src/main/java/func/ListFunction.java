package func;

import com.google.common.base.Function;

import java.util.List;

public interface ListFunction<Key, Value> extends Function<List<Key>, Value> {
    public Value apply();
    public Value apply(Key key, Value value);
    public Value apply(int index, Key key, Value value);
    public ListFunction<Value, Key> inverse();
    public boolean hasProperty(FunctionProperty property);
}
