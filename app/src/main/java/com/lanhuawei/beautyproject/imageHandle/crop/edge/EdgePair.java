package com.lanhuawei.beautyproject.imageHandle.crop.edge;

/**
 * Created by Ivan.L LanHuaWei
 * on 2017/11/15.
 * Simple class to hold a pair of Edges.
 */

public class EdgePair {
    // Member Variables

    public Edge primary;
    public Edge secondary;

    // Constructor

    public EdgePair(Edge edge1, Edge edge2) {
        primary = edge1;
        secondary = edge2;
    }
}
