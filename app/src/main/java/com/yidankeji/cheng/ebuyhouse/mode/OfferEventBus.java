package com.yidankeji.cheng.ebuyhouse.mode;

/**
 * Created by ${syj} on 2018/3/27.
 */

public class OfferEventBus {
    String offerid;

    public String getOfferid() {
        return offerid;
    }

    public void setOfferid(String offerid) {
        this.offerid = offerid;
    }

    public OfferEventBus(String offerid) {
        this.offerid = offerid;
    }
}
