package com.solactive.codechallange.calculator;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static com.solactive.codechallange.constant.Cons.MAX_ASSETS;

public class AssetProvider {

    private Map<String, Integer> instrumentMap = new HashMap<>();
    private List<StatisticsCalculator> statsList = new ArrayList<>(MAX_ASSETS);
    private final AtomicInteger lastId = new AtomicInteger(0);

    public synchronized int internalId(String instId) {
        var internalId = instrumentMap.get(instId);
        if (internalId == null) {
            internalId = lastId.getAndIncrement();
            instrumentMap.put(instId, internalId);
            statsList.add(new StatisticsCalculator(instId));
        }
        return internalId;
    }

    public StatisticsCalculator stockStats(int internalId) {
        if (internalId >= statsList.size()) return null;
        return statsList.get(internalId);
    }
    public List<StatisticsCalculator> allStockStats() {
        return statsList;
    }
}
