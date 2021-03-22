package com.solactive.codechallange.calculator;

import com.solactive.codechallange.model.Statistic;

import java.util.ArrayList;
import java.util.List;

import static com.solactive.codechallange.constant.Cons.MAX_ASSETS;

public class StatisticsAggregator {

    private AssetProvider assetProvider;

    private volatile double maximum = 0f;
    private volatile double minimum = Double.MAX_VALUE;
    private volatile double average = 0f;
    private volatile int cnt = 0;

    private final List<Statistic> statisticList = new ArrayList<>(MAX_ASSETS);

    public StatisticsAggregator(AssetProvider assetPro) {
        assetProvider = assetPro;

        for (int i = 0; i < MAX_ASSETS; i++)
            statisticList.add(null);
    }

    public void recalculate() {
        var count = 0;
        var avg = 0.0;
        var min = Double.MAX_VALUE;
        var max = 0.d;

        final var allStockStats = assetProvider.allStockStats();

        for (final var stockStats: allStockStats) {
            final var stats = stockStats.currentStatistic();
            if (stats == null) continue;

            count += stats.getCount();
            avg += stats.getAvg();
            min = Math.min(min, stats.getMin());
            max = Math.max(max, stats.getMax());
        }
        cnt = count;
        average = (double) avg / (double) allStockStats.size();
        minimum = min;
        maximum = max;
    }

    public Statistic currentStats() {
        return new Statistic(average,maximum,minimum,cnt);
    }

}
