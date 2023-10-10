package io.github.hoteljuliet.spel;

import org.junit.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
public class CircularBufferTest {

    @Test
    public void test() {
        CircularBuffer<Integer> circularBuffer = new CircularBuffer<>(Integer.class, 16);

        int i = 0;
        for (; i < 16; i++) {
            circularBuffer.add(i);
        }
        for (; i < 32; i++) {
            circularBuffer.add(i);
        }

        Integer[] data = circularBuffer.getData();
        for (i = 0; i < data.length; i++) {
            assertThat(data[i]).isGreaterThanOrEqualTo(15);
        }
    }
}
