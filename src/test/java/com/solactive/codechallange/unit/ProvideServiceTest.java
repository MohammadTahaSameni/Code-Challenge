package com.solactive.codechallange.unit;

import com.solactive.codechallange.model.Statistic;
import com.solactive.codechallange.model.Ticks;
import com.solactive.codechallange.service.ProviderService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class ProvideServiceTest {

    @Autowired
    ProviderService service;

    @Test
    public void Test_Current_Statistics() {
        final var currentTime = System.currentTimeMillis();
        service.showTicksMsg(new Ticks("BTC", 1.d, currentTime));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertReflectionEquals(
                new Statistic(1.d, 1.d, 1.d, 1),
                service.currentAssetState("BTC")
        );
    }

    @Test
    public void Test_OverFlow() {
        final var currentTime = System.currentTimeMillis();
        int cnt = 128 * 10000 ;
        for (int i = 0; i < cnt; i++)
            service.showTicksMsg(new Ticks("BTC", 1.d, currentTime));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        final var statistic = service.currentAssetState("BTC");
        final double DELTA = 1e-15;
        assertEquals(
                1.d,
                statistic.getAvg(),
                DELTA
        );

        assertTrue(statistic.getCount() < cnt);
    }
}
