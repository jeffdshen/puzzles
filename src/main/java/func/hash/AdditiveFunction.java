package func.hash;

import java.util.List;

public abstract class AdditiveFunction<Key, Value> implements HashFunction<Key, Value> {
    @Override
    public final Value apply(int index, Key key, Value value) {
        return apply(key, value);
    }

    @Override
    public final boolean hasProperty(FunctionProperty property) {
        switch (property) {
            case ADDITIVE:
                return true;
            case INDEXED_ADDITIVE:
                return true;
            default:
                throw new IllegalArgumentException();
        }
    }

    @Override
    public final Value apply(List<Key> input) {
        Value val = apply();
        for (Key key : input) {
            val = apply(key, val);
        }
        return val;
    }
}
