package func.hash;

import com.google.common.base.Function;

import java.util.List;

public interface HashFunction<Key, Value> extends Function<List<Key>, Value> {
    public Value apply();
    public Value apply(Key key, Value value);
    public Value apply(int index, Key key, Value value);

    public boolean hasProperty(FunctionProperty property);
}
