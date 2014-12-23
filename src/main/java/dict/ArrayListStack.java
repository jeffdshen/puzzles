package dict;

import java.util.ArrayList;
import java.util.EmptyStackException;

public class ArrayListStack<T> extends ArrayList<T> {
    public void push(T o) {
        add(o);
    }

    public T pop() {
        if (isEmpty()) {
            throw new EmptyStackException();
        }

        return remove(size() - 1);
    }

    public T peek() {
        if (isEmpty()) {
            throw new EmptyStackException();
        }

        return get(size() - 1);
    }
}
