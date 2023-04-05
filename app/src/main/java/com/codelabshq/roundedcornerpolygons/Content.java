package com.codelabshq.roundedcornerpolygons;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;

public class Content {

    private final Bitmap bitmap;

    private final Matrix matrix = new Matrix();

    public Content(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public Content centerCrop(RectF area) {
        float xScale = area.width() / bitmap.getWidth();
        float yScale = area.height() / bitmap.getHeight();
        float scale = Math.max(xScale, yScale);

        float dx = (area.width() - (bitmap.getWidth()*scale))*.5f;
        float dy = (area.height() - (bitmap.getHeight()*scale))*.5f;
        matrix.reset();
        matrix.setTranslate(area.left+dx, area.top+dy);
        matrix.postScale(scale, scale, area.left+dx, area.top+dy);

        return this;
    }

    public void draw(Canvas canvas, Paint paint) {
        canvas.drawBitmap(bitmap, matrix, paint);
    }

}
