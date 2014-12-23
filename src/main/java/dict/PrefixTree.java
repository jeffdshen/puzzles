package dict;

import com.google.common.collect.ImmutableList;

import java.util.*;

public class PrefixTree<Key, Value> implements PrefixIterable<Key, Value> {
    private Node<Key, Value> root;
    private final PrefixAggregator<Key, Value> aggregator;

    public PrefixTree(PrefixAggregator<Key, Value> aggregator) {
        this.aggregator = aggregator;
        root = new Node<>(null, null);
    }

    public void insert(List<Key> keys, Value datum){
        Node<Key, Value> node = root;
        for (Key key : keys) {
            node.add(key);
            node = node.getChild(key);
        }

        node.set(datum);

        for (int i = keys.size() - 1; i >= 0; i--) {
            node = node.getParent();
            node.set(node.aggregate(aggregator, keys.subList(0, i)));
        }
    }

    public Value get(List<Key> keys) {
        return getNode(keys).getValue();
    }

    private Node<Key, Value> getNode(List<Key> keys) {
        Node<Key, Value> node = root;
        for (Key key : keys) {
            node.add(key);
            node = node.getChild(key);
        }
        return node;
    }

    @Override
    public PrefixStack<Key, Value> prefixStack() {
        return prefixStack(ImmutableList.<Key>of());
    }

    @Override
    public PrefixStack<Key, Value> prefixStack(List<Key> prefix) {
        return new PrefixTreeStack<>(getNode(prefix));
    }

    private static class PrefixTreeStack<Key, Value> implements PrefixStack<Key, Value> {
        public Node<Key, Value> node;
        public Node<Key, Value> root;
        public int size;

        public PrefixTreeStack(Node<Key, Value> node) {
            this.root = node;
            this.node = node;
            size = 0;
        }

        @Override
        public void push(Key key) {
            node = node.getChild(key);
            size++;
        }

        @Override
        public Key pop() {
            if (node == root) {
                throw new EmptyStackException();
            }

            Key key = node.key;
            node = node.getParent();
            size--;
            return key;
        }

        @Override
        public Key peek() {
            if (node == root) {
                throw new EmptyStackException();
            }

            return node.getKey();
        }

        @Override
        public List<Key> getKey() {
            final Node<Key, Value> lockedNode = node;
            final Node<Key, Value> lockedRoot = root;
            return new LazyList<>(
                new Provider<List<Key>>() {
                    @Override
                    public List<Key> get() {
                        ImmutableList.Builder<Key> builder = ImmutableList.builder();
                        for (Node<Key, Value> cur = lockedNode; cur != lockedRoot; cur = cur.getParent()) {
                            builder.add(cur.getKey());
                        }
                        return builder.build().reverse();
                    }
                }
            );
        }

        @Override
        public Value getValue() {
            if (node == root) {
                throw new EmptyStackException();
            }

            return node.getValue();
        }

        @Override
        public Set<Key> nextKeys() {
            return Collections.unmodifiableSet(node.children.keySet());
        }

        @Override
        public int size() {
            return size;
        }
    }

    private static class Node<Key, Value> implements Pair<Key, Value> {
        private final Key key;
        private final Node<Key, Value> parent;
        private Map<Key, Node<Key, Value>> children;
        private Value item;

        public Node(Node<Key, Value> parent, Key key) {
            this.key = key;
            this.parent = parent;

            // faster than LinkedHashMap, and gives fast iteration order.
            children = new TreeMap<>();
        }

        @Override
        public Key getKey() {
            return key;
        }

        @Override
        public Value getValue() {
            return item;
        }

        public Node<Key, Value> getChild(Key key) {
            return children.get(key);
        }

        public Node<Key, Value> getParent() {
            return parent;
        }

        public void add(Key key) {
            if (!children.containsKey(key)) {
                children.put(key, new Node<>(this, key));
            }
        }

        public void set(Value item) {
            this.item = item;
        }

        public Value aggregate(PrefixAggregator<Key, Value> aggregator, List<Key> prefix) {
            return aggregator.aggregate(prefix, children.values());
        }
    }
}
