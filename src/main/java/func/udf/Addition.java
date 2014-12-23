package func.udf;

import func.BinaryOperation;

public class Addition implements BinaryOperation<Long> {
    @Override
    public Long apply(Long a, Long b) {
        return a + b;
    }
}
