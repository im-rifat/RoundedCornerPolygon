package com.codelabshq.roundedcornerpolygons;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class CollageView extends View {

    private final List<Polygon> mPolygonList;

    private final List<Content> mContentList;

    private final Paint mPaint;

    private float mCornerRadius = 10f;

    public CollageView(Context context) {
        this(context, null);
    }

    public CollageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CollageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public CollageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);

        mPolygonList = new ArrayList<>();
        addPolygon(createPolygon1());
        addPolygon(createPolygon2());
        setCornerRadius(1f);

        mContentList = new ArrayList<>();
        mContentList.add(new Content(BitmapFactory.decodeResource(context.getResources(), R.drawable.d2)));
        mContentList.add(new Content(BitmapFactory.decodeResource(context.getResources(), R.drawable.d1)));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawColor(Color.LTGRAY);

        RectF rectF = new RectF();

        for(int i = 0; i < mPolygonList.size(); i++) {
            canvas.save();
            Polygon polygon = mPolygonList.get(i);
            Path path = getPathFromPolygon(recreatePolygonBasedOnSize(polygon, getWidth(), getHeight()), mCornerRadius);
            Path basePath = getPathFromPolygon(recreatePolygonBasedOnSize(polygon, getWidth(), getHeight()), 1f);
            canvas.clipPath(path);

            basePath.computeBounds(rectF, true);

            Content content = mContentList.get(i);
            content.centerCrop(rectF).draw(canvas, mPaint);
            canvas.restore();
        }
    }

    Path getPathFromPolygon(Polygon polygon, float radius) {
        return PathUtil.createPathFromPoints(polygon.getPointList(), radius);
    }

    void addPolygon(Polygon polygon) {
        this.mPolygonList.add(polygon);
        invalidate();
    }

    float convertDpToPixel(float dp, Context context){
        return dp * ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    Polygon createPolygon1() {
        Polygon polygon = new Polygon();

        polygon.addPoint(new PointF(0.1f, 0.1f));
        polygon.addPoint(new PointF(0.1f, 0.9f));
        polygon.addPoint(new PointF(.5f, .7f));
        polygon.addPoint(new PointF(0.9f, 0.9f));
        polygon.addPoint(new PointF(0.9f, 0.1f));

        return polygon;
    }

    Polygon createPolygon2() {
        Polygon polygon = new Polygon();

        polygon.addPoint(new PointF(0.12f, 0.9f));
        polygon.addPoint(new PointF(0.5f, 0.71f));
        polygon.addPoint(new PointF(0.88f, 0.9f));

        return polygon;
    }

    Polygon recreatePolygonBasedOnSize(Polygon polygon, int size, int h) {
        Polygon polygon1 = new Polygon();

        for(PointF pointF : polygon.getPointList()) {
            polygon1.addPoint(new PointF(pointF.x*size, pointF.y*h));
        }

        return polygon1;
    }

    public void setCornerRadius(float radius) {
        this.mCornerRadius = convertDpToPixel(radius, getContext());
        invalidate();
    }
}