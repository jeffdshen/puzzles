package func.aggregate;

public class Addition implements BinaryOperation<Long> {
    @Override
    public Long apply(Long a, Long b) {
        return a + b;
    }
}
