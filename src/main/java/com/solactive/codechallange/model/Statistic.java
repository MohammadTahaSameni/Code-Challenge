package com.solactive.codechallange.model;

import java.io.Serializable;

public class Statistic implements Serializable {
    private double avg;
    private double max;
    private double min;
    private int count;

    public Statistic(double avg, double max, double min, int count) {
        this.avg = avg;
        this.max = max;
        this.min = min;
        this.count = count;
    }

    public Statistic(){}

    public double getAvg() {
        return avg;
    }

    public void setAvg(double avg) {
        this.avg = avg;
    }

    public double getMax() {
        return max;
    }

    public void setMax(double max) {
        this.max = max;
    }

    public double getMin() {
        return min;
    }

    public void setMin(double min) {
        this.min = min;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
