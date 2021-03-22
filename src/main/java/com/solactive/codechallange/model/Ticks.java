package com.solactive.codechallange.model;

import java.io.Serializable;

public class Ticks implements Serializable {
    private String instrument;
    private double price;
    private long timestamp;
    private int internalAssetId;

    public Ticks(String instrument, double price, long timestamp) {
        this.instrument = instrument;
        this.price = price;
        this.timestamp = timestamp;
    }

    public Ticks() {}

    public String getInstrument() {
        return instrument;
    }

    public void setInstrument(String instrument) {
        this.instrument = instrument;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public int getInternalAssetId() {
        return internalAssetId;
    }

    public void setInternalAssetId(int internalAssetId) {
        this.internalAssetId = internalAssetId;
    }
}
