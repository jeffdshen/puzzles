package func;

import com.google.common.base.Function;
import dict.PrefixStack;

import java.util.List;

public interface MultiFunction<Key> extends Function<List<Key>, PrefixStack<Key, ?>> {
}
