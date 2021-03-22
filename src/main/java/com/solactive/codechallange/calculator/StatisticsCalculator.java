package com.solactive.codechallange.calculator;

import com.solactive.codechallange.model.SlideTime;
import com.solactive.codechallange.model.Statistic;

import java.util.PriorityQueue;

import static com.solactive.codechallange.constant.Cons.SLIDE_TIME_INTERVAL;

public class StatisticsCalculator {
    public final PriorityQueue<SlideTime> priorityQueue = new PriorityQueue<>();

    private String instrumentId;
    private volatile double min = 0f;
    private volatile double max = 0f;
    private volatile double avg = 0f;
    private volatile int count = 0;
    private volatile boolean isFilled = false;

    public StatisticsCalculator(double min, double max, double avg, int count) {
        this.min = min;
        this.max = max;
        this.avg = avg;
        this.count = count;
    }

    public StatisticsCalculator(String instrumentId) {
        this.instrumentId = instrumentId;
    }

    public Statistic show(final long now, final long time, final double price) {

        if (time < now - SLIDE_TIME_INTERVAL || price <= 0)
            return null;
        synchronized (this) {

            while (!this.priorityQueue.isEmpty() && this.priorityQueue.peek().getTime() < now - SLIDE_TIME_INTERVAL)
                this.priorityQueue.poll();

            this.priorityQueue.offer(new SlideTime(price, time));

            final var cnt = this.priorityQueue.size();
            var maximum = 0.d;
            var minimum = Double.MAX_VALUE;
            var average = 0.0;
            for (final var msg : this.priorityQueue) {
                final var prc = msg.getPrice();
                average += prc;
                minimum = Math.min(minimum, prc);
                maximum = Math.max(maximum, prc);
            }
            average /= cnt;
            this.count = cnt;
            this.max = maximum;
            this.min = minimum;
            this.avg = (double) average;
            this.isFilled = true;
            return new Statistic((double) average, maximum, minimum, cnt);
        }
    }

    public Statistic currentStatistic() {
        if (!this.isFilled) return null;
        return new Statistic(this.avg, this.max,
                this.min, this.count);
    }

    public String getInstrumentId() {
        return instrumentId;
    }

    public void setInstrumentId(String instrumentId) {
        this.instrumentId = instrumentId;
    }
}
