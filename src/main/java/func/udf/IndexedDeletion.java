package func.udf;

import com.google.common.collect.ImmutableList;
import func.UniFunction;

import java.util.List;

/**
 * Created by jdshen on 12/29/14.
 */
public class IndexedDeletion<Key> implements UniFunction<Key> {
    private final int n;

    public IndexedDeletion(int n) {
        this.n = n;
    }

    @Override
    public List<Key> apply(List<Key> input) {
        ImmutableList.Builder<Key> builder = ImmutableList.builder();

        int i = 0;
        for (Key key : input) {
            if (i != n) {
                builder.add(key);
            }
            i++;
        }

        return builder.build();
    }
}
