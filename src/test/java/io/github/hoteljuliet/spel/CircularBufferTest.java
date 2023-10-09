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
            Boolean accepted = circularBuffer.offer(i);
            assertThat(accepted).isTrue();
        }
        for (; i < 32; i++) {
            Boolean accepted = circularBuffer.offer(i);
            assertThat(accepted).isFalse();
        }

        Integer[] data = circularBuffer.getData();
        for (i = 0; i < data.length; i++) {
            assertThat(data[i]).isLessThanOrEqualTo(15);
        }

        for (i = 0; i < 16; i++) {
            Optional<Integer> datum = circularBuffer.poll();
            assertThat(datum.isPresent()).isTrue();
        }
    }
}
