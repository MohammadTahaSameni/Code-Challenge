package com.solactive.codechallange.model;

public class SlideTime implements Comparable<SlideTime> {
    private final long time;
    private double price;


    public SlideTime(double price, long time) {
        this.price = price;
        this.time = time;
    }

    @Override
    public int compareTo(SlideTime slideTime) {
        return (int) (this.time - slideTime.time);
    }

    public long getTime() {
        return time;
    }

    public double getPrice() {
        return price;
    }
}
