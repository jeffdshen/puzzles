package func.udf;

import func.AdditiveFunction;

public class Length<Key> extends AdditiveFunction<Key, Integer> {
    @Override
    public Integer apply() {
        return 0;
    }

    @Override
    public Integer apply(Key key, Integer integer) {
        return integer + 1;
    }
}
