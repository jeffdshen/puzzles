package func.hash;

import java.util.List;

public abstract class IndexedAdditiveFunction<Key, Value> implements HashFunction<Key, Value> {
    @Override
    public final Value apply(Key key, Value value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final boolean hasProperty(FunctionProperty property) {
        switch (property) {
            case ADDITIVE:
                return false;
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
