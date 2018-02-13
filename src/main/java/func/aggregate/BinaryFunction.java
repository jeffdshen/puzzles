package func.aggregate;

public interface BinaryFunction<A, B, C> {
    public C apply(A a, B b);
}
