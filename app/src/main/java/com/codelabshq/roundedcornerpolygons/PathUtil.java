package com.codelabshq.roundedcornerpolygons;

import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;

import androidx.annotation.FloatRange;
import androidx.annotation.NonNull;

import java.util.List;

/**
 * ------------------- credits -------------------
 *
 * https://stackoverflow.com/questions/24771828/how-to-calculate-rounded-corners-for-a-polygon#answer-24780108
 * https://www.kristakingmath.com/blog/polar-coordinates-vs-rectangular-coordinates
 *
 */
public class PathUtil {

    static void roundCorner(PointF cornerPoint, PointF p1, PointF p2, float radius, Path path) {
        double dx1 = cornerPoint.x - p1.x;
        double dy1 = cornerPoint.y - p1.y;
        double len1 = Math.hypot(dx1, dy1);

        double dx2 = cornerPoint.x - p2.x;
        double dy2 = cornerPoint.y - p2.y;
        double len2 = Math.hypot(dx2, dy2);

        double angle = positiveAngle(Math.atan2(dy1, dx1) - Math.atan2(dy2, dx2));

        double tan = Math.abs(Math.tan(angle / 2));
        double segment = radius / tan;
        double minMagnitude = Math.min(len1, len2);

        if (segment > minMagnitude) {
            segment = minMagnitude;
            radius = (float) (segment * tan);
        }

        PointF startPoint = getProportionPoint(cornerPoint, segment, len1, dx1, dy1);
        PointF endPoint = getProportionPoint(cornerPoint, segment, len2, dx2, dy2);

        float dx = 2 * cornerPoint.x - startPoint.x - endPoint.x;
        float dy = 2 * cornerPoint.y - startPoint.y - endPoint.y;

        double L = Math.hypot(dx, dy);
        double d = Math.hypot(radius, segment);

        PointF centerPoint = getProportionPoint(cornerPoint, d, L, dx, dy);

        double startAngle = positiveAngle(Math.atan2(startPoint.y - centerPoint.y, startPoint.x - centerPoint.x));
        double endAngle = positiveAngle(Math.atan2(endPoint.y - centerPoint.y, endPoint.x - centerPoint.x));

        double sweepAngle = positiveAngle(endAngle - startAngle);

        if (sweepAngle < 0) {
            startAngle = endAngle;
            sweepAngle = -sweepAngle;
        }

        if (sweepAngle > Math.PI) {
            sweepAngle = -(2 * Math.PI - sweepAngle);
        }

        float left = (centerPoint.x - radius);
        float top = (centerPoint.y - radius);
        float right = (left + (2 * radius));
        float bottom = (top + (2 * radius));

        RectF oval = new RectF(left, top, right, bottom);
        path.arcTo(oval, (float) Math.toDegrees(startAngle), (float) Math.toDegrees(sweepAngle));
    }

    static double positiveAngle(double angle) {
        return angle < 0f ? angle + (2 * Math.PI) : angle;
        // return angle;
    }

    public static Path createPathFromPoints(@NonNull List<PointF> pointList, @FloatRange(from = 1f, to = 25f) float cornerRadiusInDp) {
        Path path = new Path();

        createPathFromPoints(path, pointList, cornerRadiusInDp);

        return path;
    }

    public static void createPathFromPoints(@NonNull Path path, @NonNull List<PointF> pointList, @FloatRange(from = 1f, to = 25f) float cornerRadiusInDp) {
        path.reset();

        PointF pointA;
        PointF pointB;
        PointF pointC;

        for (int i = 0; i < pointList.size(); i++) {
            pointA = pointList.get(i % pointList.size());
            pointB = pointList.get((i + 1) % pointList.size());
            pointC = pointList.get((i + 2) % pointList.size());

            roundCorner(pointB, pointA, pointC, cornerRadiusInDp, path);

            pointA = pointB;
            pointB = pointC;
        }

        path.close();
    }

    static PointF getProportionPoint(PointF cornerPoint, double segment, double len, double dx, double dy) {
        double factor = segment / len;

        return new PointF((float) (cornerPoint.x - (dx * factor)), (float) (cornerPoint.y - (dy * factor)));
    }
}
