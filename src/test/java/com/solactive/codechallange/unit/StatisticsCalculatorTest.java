package com.solactive.codechallange.unit;

import com.solactive.codechallange.calculator.StatisticsCalculator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import static com.solactive.codechallange.constant.Cons.SLIDE_TIME_INTERVAL;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class StatisticsCalculatorTest {

    StatisticsCalculator calculator;

    @Before
    public void init() {
        calculator = new StatisticsCalculator("TESLA");
    }

    @Test
    public void Should_Null_When_IsEmpty() {
        assertNull(calculator.currentStatistic());
    }

    @Test
    public void Test_InstrumentId() {
        assertEquals(calculator.getInstrumentId(), "TESLA");
    }

    @Test
    public void Test_Ticks_IsExpired() {
        calculator.show(SLIDE_TIME_INTERVAL, SLIDE_TIME_INTERVAL - 1, 1.0f);
        calculator.show(SLIDE_TIME_INTERVAL + 1, 0, 100.0f);
        final var statistics = calculator.currentStatistic();
        final double DELTA = 1e-15;

        assertAll(
                () -> assertEquals(1.d, statistics.getAvg(), DELTA),
                () -> assertEquals(1.d, statistics.getMin(), DELTA),
                () -> assertEquals(1.d, statistics.getMax(), DELTA),
                () -> assertEquals(1, statistics.getCount())
        );
    }

    @Test
    public void Test_Queue() {
        var start = 1;
        var end = 5000;

        for (int i = start; i <= end; i++)
            calculator.show(0, 0, i);

        final var statistic = calculator.currentStatistic();
        var sum = (double) end / 2 * (start + end);
        final double DELTA = 1e-15;
        assertAll(
                () -> assertEquals(sum / end, statistic.getAvg(), DELTA),
                () -> assertEquals((double) start, statistic.getMin(), DELTA),
                () -> assertEquals((double) end, statistic.getMax(), DELTA),
                () -> assertEquals(end, statistic.getCount())
        );
    }

    @Test
    public void Test_OutOf_Order() {
        for (int i = 1; i <= 10; i++)
            calculator.show(10, 10 - i, i);

        final var statistic = calculator.currentStatistic();

        var sum = 10.d / 2 * (1 + 10);
        final double DELTA = 1e-15;
        assertAll(
                () -> assertEquals(sum / 10, statistic.getAvg(), DELTA),
                () -> assertEquals(1.d, statistic.getMin(), DELTA),
                () -> assertEquals(10.d, statistic.getMax(), DELTA),
                () -> assertEquals(10, statistic.getCount())
        );
    }
}
