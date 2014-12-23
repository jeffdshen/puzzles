package dict;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * A list that doesn't construct until necessary
 */
@SuppressWarnings("ALL")
public class LazyList<E> implements List<E> {
    private List<E> list;
    private final Provider<List<E>> listProvider;

    public LazyList(Provider<List<E>> listProvider) {
        this.listProvider = listProvider;
    }

    public void construct() {
        if (list == null) {
            list = listProvider.get();
        }
    }

    @Override
    public int size() {
        construct();
        return list.size();
    }

    @Override
    public boolean isEmpty() {
        construct();
        return list.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        construct();
        return list.contains(o);
    }

    @Override
    public Iterator<E> iterator() {
        construct();
        return list.iterator();
    }

    @Override
    public Object[] toArray() {
        construct();
        return list.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        construct();
        return list.toArray(a);
    }

    @Override
    public boolean add(E e) {
        construct();
        return list.add(e);
    }

    @Override
    public boolean remove(Object o) {
        construct();
        return list.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        construct();
        return list.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        construct();
        return list.addAll(c);
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        construct();
        return list.addAll(index, c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        construct();
        return list.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        construct();
        return list.retainAll(c);
    }

    @Override
    public void clear() {
        construct();
        list.clear();
    }

    @Override
    public E get(int index) {
        construct();
        return list.get(index);
    }

    @Override
    public E set(int index, E element) {
        construct();
        return list.set(index, element);
    }

    @Override
    public void add(int index, E element) {
        construct();
        list.add(index, element);
    }

    @Override
    public E remove(int index) {
        construct();
        return list.remove(index);
    }

    @Override
    public int indexOf(Object o) {
        construct();
        return list.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        construct();
        return list.lastIndexOf(o);
    }

    @Override
    public ListIterator<E> listIterator() {
        construct();
        return list.listIterator();
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        construct();
        return list.listIterator(index);
    }

    @Override
    public List<E> subList(final int fromIndex, final int toIndex) {
        return new LazyList<>(new Provider<List<E>>() {
            @Override
            public List get() {
                construct();
                return list.subList(fromIndex, toIndex);
            }
        });
    }

    @Override
    public String toString() {
        construct();
        return list.toString();
    }

    @Override
    public int hashCode() {
        construct();
        return list.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        construct();
        return list.equals(obj);
    }
}
