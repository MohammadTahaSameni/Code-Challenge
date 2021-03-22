package com.solactive.codechallange.unit;

import com.solactive.codechallange.calculator.AssetProvider;
import com.solactive.codechallange.calculator.StatisticsAggregator;
import com.solactive.codechallange.model.Statistic;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class AssetProviderTest {

    AssetProvider assetProvider;

    @Before
    public void init() {
        assetProvider = new AssetProvider();
    }

    @Test
    public void InternalId_Test() {
        assertAll(
                () -> assertEquals(0, assetProvider.internalId("BTC")),
                () -> assertEquals(0, assetProvider.internalId("BTC")),
                () -> assertEquals(1, assetProvider.internalId("ADA")),
                () -> assertEquals(0, assetProvider.internalId("BTC")),
                () -> assertEquals(1, assetProvider.internalId("ADA"))
        );
    }

    @Test
    public void Should_Return_Null_When_Non_Exist_InternalId() {
        assertNull(assetProvider.stockStats(1000));
    }

    @Test
    public void Should_Return_Statistics() {
        var stats = assetProvider.stockStats(assetProvider.internalId("BTC"));
        assertAll(
                () -> assertNotNull(stats),
                () -> assertReflectionEquals(
                        new Statistic(1.0f, 1.0f, 1.0f, 1),
                        stats.show(0, 0, 1.0f)
                )
        );
    }

    @Test
    public void Test_Aggregator_one_Asset() {
        final var aggStats = new StatisticsAggregator(assetProvider);
        var stats = assetProvider.stockStats(assetProvider.internalId("BTC"));

        assertEquals(1, assetProvider.allStockStats().size());
        stats.show(0, 0, 1.d);
        aggStats.recalculate();
        assertReflectionEquals(stats.currentStatistic(), aggStats.currentStats());
    }

    @Test
    public void Test_All_aggregation() {
        final var aggStats = new StatisticsAggregator(assetProvider);

        var BTC = assetProvider.stockStats(assetProvider.internalId("BTC"));
        assertEquals(1, assetProvider.allStockStats().size());
        var ADA = assetProvider.stockStats(assetProvider.internalId("ADA"));
        assertEquals(2, assetProvider.allStockStats().size());

        BTC.show(0, 0, 1.d);
        aggStats.recalculate();

        ADA.show(0, 0, 2.d);
        aggStats.recalculate();

        assertReflectionEquals(
                new Statistic(1.5f, 2.d, 1.d, 2),
                aggStats.currentStats()
        );
    }
}
