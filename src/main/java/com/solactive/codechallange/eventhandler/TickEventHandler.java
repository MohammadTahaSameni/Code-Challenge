package com.solactive.codechallange.eventhandler;

import com.lmax.disruptor.EventHandler;
import com.solactive.codechallange.calculator.*;
import com.solactive.codechallange.model.Ticks;

public class TickEventHandler implements EventHandler<Ticks> {

    private final AssetProvider assetProvider;
    private final StatisticsAggregator aggregator;

    public TickEventHandler(AssetProvider asstProvider, StatisticsAggregator agg) {
        assetProvider = asstProvider;
        aggregator = agg;
    }

    @Override
    public void onEvent(Ticks ticks, long l, boolean b) throws Exception {
        final var stockStats = assetProvider.stockStats(ticks.getInternalAssetId());
        if (stockStats == null)
            throw new RuntimeException("Stock tick event for unknown stock with internal ID: " + ticks.getInternalAssetId());

        final var now = System.currentTimeMillis();

        stockStats.show(now, ticks.getTimestamp(), ticks.getPrice());
        aggregator.recalculate();
    }
}
