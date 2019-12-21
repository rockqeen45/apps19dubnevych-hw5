package ua.edu.ucu.stream;

import ua.edu.ucu.function.*;

import java.util.*;

public class AsIntStream implements IntStream {
    private List<Integer> values;
    private Queue<Operation> operations;

    private Queue<IntUnaryOperator> mappers;
    private Queue<IntToIntStreamFunction> flatMappers;
    private Queue<IntPredicate> filters;
    
    private AsIntStream() {
        this.values = new LinkedList<>();
        this.operations = new LinkedList<>();

        this.mappers = new LinkedList<>();
        this.flatMappers = new LinkedList<>();
        this.filters = new LinkedList<>();
    }

    public static IntStream of(int... values) {
        AsIntStream stream = new AsIntStream();
        for (int value: values) {
            stream.values.add(value);
        }
        return stream;
    }

    @Override
    public Double average() {
        executeOperations();
        validate();
        double sum = 0.0;
        for (int value: values) {
            sum += value;
        }
        return sum / values.size();
    }

    @Override
    public Integer max() {
        executeOperations();
        validate();
        int max = Integer.MIN_VALUE;
        for (int value: values) {
            if (value > max) {
                max = value;
            }
        }
        return max;
    }

    @Override
    public Integer min() {
        executeOperations();
        validate();
        int min = Integer.MAX_VALUE;
        for (int value: values) {
            if (value < min) {
                min = value;
            }
        }
        return min;
    }

    @Override
    public long count() {
        executeOperations();
        validate();
        return values.size();
    }

    @Override
    public Integer sum() {
        executeOperations();
        validate();
        int sum = 0;
        for (int value: values) {
            sum += value;
        }
        return sum;
    }

    @Override
    public IntStream filter(IntPredicate predicate) {
       operations.add(Operation.FILTER);
       filters.add(predicate);
       return this;
    }

    @Override
    public void forEach(IntConsumer action) {
        executeOperations();
        for (int value: values) {
            action.accept(value);
        }
    }

    @Override
    public IntStream map(IntUnaryOperator mapper) {
        operations.add(Operation.MAP);
        mappers.add(mapper);
        return this;
    }

    @Override
    public IntStream flatMap(IntToIntStreamFunction func) {
        operations.add(Operation.FLAT_MAP);
        flatMappers.add(func);
        return this;
    }

    @Override
    public int reduce(int identity, IntBinaryOperator op) {
        executeOperations();
        int sum = identity;
        for (int value: values) {
            sum = op.apply(sum, value);
        }
        return sum;
    }

    @Override
    public int[] toArray() {
        executeOperations();
        int[] result = new int[values.size()];
        for (int i = 0; i < values.size(); i++) {
            result[i] = values.get(i);
        }
        return result;
    }


    private void executeMap(IntUnaryOperator mapper) {
        List<Integer> result = new LinkedList<>();
        for (int value: values) {
            int mappedValue = mapper.apply(value);
            result.add(mappedValue);
        }
        this.values = result;
    }

    private void executeFlatMap(IntToIntStreamFunction func) {
        List<Integer> result = new LinkedList<>();
        for (int value: values) {
            AsIntStream stream = (AsIntStream) func.applyAsIntStream(value);
            result.addAll(stream.values);
        }
        this.values = result;
    }

    private void executeFilter(IntPredicate predicate) {
        List<Integer> filteredValues = new LinkedList<>();
        for (int value: values) {
            if (predicate.test(value)) {
                filteredValues.add(value);
            }
        }
        this.values = filteredValues;
    }

    private void executeOperations() {
        while (!operations.isEmpty()) {
            Operation operation = operations.remove();
            if (operation == Operation.FILTER) {
                executeFilter(filters.remove());
            } else if (operation == Operation.MAP) {
                executeMap(mappers.remove());
            } else if (operation == Operation.FLAT_MAP) {
                executeFlatMap(flatMappers.remove());
            }
        }
    }

    private void validate() {
        if (values.isEmpty()) {
            throw new IllegalArgumentException("Stream is empty");
        }
    }
}
