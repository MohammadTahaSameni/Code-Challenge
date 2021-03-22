package com.solactive.codechallange.service;

import com.lmax.disruptor.InsufficientCapacityException;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.util.DaemonThreadFactory;
import com.solactive.codechallange.calculator.AssetProvider;
import com.solactive.codechallange.calculator.StatisticsAggregator;
import com.solactive.codechallange.eventhandler.TickEventHandler;
import com.solactive.codechallange.model.Statistic;
import com.solactive.codechallange.model.Ticks;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

import static com.solactive.codechallange.constant.Cons.DEF_BUFFER_CAPACITY;

@Service
public class ProviderServiceImpl implements ProviderService {

    private Disruptor<Ticks> disruptor;
    private AssetProvider assetProvider;
    private StatisticsAggregator aggregator;

    @PostConstruct
    public void init() {
        disruptor = new Disruptor<>(
                Ticks::new,
                DEF_BUFFER_CAPACITY,
                DaemonThreadFactory.INSTANCE
        );
        assetProvider = new AssetProvider();
        aggregator = new StatisticsAggregator(assetProvider);

        disruptor.handleEventsWith(new TickEventHandler(assetProvider, aggregator));
        disruptor.start();
    }

    public Statistic currentAssetState(String instId) {
        final var internalStockId = assetProvider.internalId(instId);
        final var stockStats = assetProvider.stockStats(internalStockId);
        return stockStats != null ? stockStats.currentStatistic() : null;
    }

    public Statistic currAggregateStats() {
        return aggregator.currentStats();
    }

    public void showTicksMsg(final Ticks msg) {

        final var buffer = disruptor.getRingBuffer();
        final var internalStockId = assetProvider.internalId(msg.getInstrument());
        try {
            long sequence = buffer.tryNext();
            try {
                var event = buffer.get(sequence);
                event.setInternalAssetId(internalStockId);
                event.setPrice(msg.getPrice());
                event.setTimestamp(msg.getTimestamp());
            } finally {
                buffer.publish(sequence);
            }
        } catch (InsufficientCapacityException e) {
            // TODO: really we should log the error here, or keep or something...
        }
    }
}
