package com.codelabshq.roundedcornerpolygons;

import android.graphics.PointF;

import java.util.ArrayList;
import java.util.List;

public class Polygon {

    private final List<PointF> pointList;

    public Polygon() {
        this.pointList = new ArrayList<>();
    }

    public void addPoint(PointF pointF) {
        this.pointList.add(pointF);
    }

    public List<PointF> getPointList() {
        return pointList;
    }
}
