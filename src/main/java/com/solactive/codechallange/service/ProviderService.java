package com.solactive.codechallange.service;

import com.solactive.codechallange.model.Statistic;
import com.solactive.codechallange.model.Ticks;

public interface ProviderService {
    Statistic currentAssetState(String instId) ;

    Statistic currAggregateStats();

    void showTicksMsg(final Ticks msg);
}
