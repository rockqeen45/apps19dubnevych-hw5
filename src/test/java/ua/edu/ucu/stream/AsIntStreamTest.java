package ua.edu.ucu.stream;

import org.junit.Before;
import org.junit.Test;
import ua.edu.ucu.stream.AsIntStream;
import ua.edu.ucu.stream.IntStream;

import static org.junit.Assert.*;

public class AsIntStreamTest {

    private IntStream stream;

    @Before
    public void setUp() {
        int[] arr = {2, 7, 9, -11, 12, 28, -33, 70, 5, 0};
        stream = AsIntStream.of(arr);
    }

    @Test
    public void testOf() {
        int[] testArr = {1, 2, 3, 4};
        IntStream intStream = AsIntStream.of(testArr);
        StringBuilder result = new StringBuilder();
        intStream.forEach(result::append);
        assertEquals(result.toString(), "1234");
    }

    @Test
    public void testAverage() {
        assertEquals(stream.average(), new Double(8.9));
    }

    @Test
    public void testMin() {
        assertEquals(stream.min(), new Integer(-33));
    }

    @Test
    public void testMax() {
        assertEquals(stream.max(), new Integer(70));
    }

    @Test
    public void testCount() {
        long size = 10;
        assertEquals(stream.count(), size);
    }

    @Test
    public void testSum() {
        assertEquals(stream.sum(), new Integer(89));
    }

    @Test
    public void testReduce() {
        assertEquals(stream.reduce(1, (sum, x) -> sum += x), 90);
    }

    @Test
    public void testForEach() {
        StringBuilder builder = new StringBuilder();
        stream.forEach(x -> builder.append(x).append(", "));
        String result = builder.toString();
        assertEquals(result.substring(0, result.length() - 2), "2, 7, 9, -11, 12, 28, -33, 70, 5, 0");
    }

    @Test
    public void testToArray() {
        int[] expected = {2, 7, 9, -11, 12, 28, -33, 70, 5, 0};
        assertArrayEquals(stream.toArray(), expected);
    }

    @Test
    public void testFilter() {
        int[] expected = {2, 7, 9, 12, 28, 70, 5};
        assertArrayEquals(stream.filter(x -> x > 0).toArray(), expected);
    }

    @Test
    public void testMap() {
        int[] expected = {4, 14, 18, -22, 24, 56, -66, 140, 10, 0};
        assertArrayEquals(stream.map(x -> x * 2).toArray(), expected);
    }

    @Test
    public void testFlatMap() {
        int[] expected = {2, -2, 7, -7, 9, -9, -11, 11, 12, -12, 28, -28, -33, 33, 70, -70, 5, -5, 0, 0};
        assertArrayEquals(stream.flatMap(x -> AsIntStream.of(x, -x)).toArray(), expected);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValidationInFilter() {
        stream.filter(x -> x > 100).sum();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValidationInFlatMap() {
        stream.flatMap(x -> AsIntStream.of()).sum();
    }
}
