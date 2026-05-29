package escapeShip.core;

public final class PolygonShape {
    private final Vec2[] points;

    public PolygonShape(Vec2[] points) {
        this.points = points.clone();
    }

    public Vec2[] getPoints() {
        return points.clone();
    }

    public Rect bounds() {
        double minX = points[0].getX();
        double minY = points[0].getY();
        double maxX = minX;
        double maxY = minY;
        for (int i = 1; i < points.length; i++) {
            Vec2 point = points[i];
            minX = Math.min(minX, point.getX());
            minY = Math.min(minY, point.getY());
            maxX = Math.max(maxX, point.getX());
            maxY = Math.max(maxY, point.getY());
        }
        return new Rect(minX, minY, maxX - minX, maxY - minY);
    }

    public boolean intersects(Rect rect) {
        if (!bounds().intersects(rect)) {
            return false;
        }

        Vec2[] corners = new Vec2[] {
                new Vec2(rect.getX(), rect.getY()),
                new Vec2(rect.getX() + rect.getWidth(), rect.getY()),
                new Vec2(rect.getX() + rect.getWidth(), rect.getY() + rect.getHeight()),
                new Vec2(rect.getX(), rect.getY() + rect.getHeight())
        };
        PolygonShape rectangle = new PolygonShape(corners);
        return intersects(rectangle) || contains(corners[0]) || rectangle.contains(points[0]);
    }

    public boolean intersects(PolygonShape other) {
        if (!bounds().intersects(other.bounds())) {
            return false;
        }
        for (int i = 0; i < points.length; i++) {
            Vec2 a1 = points[i];
            Vec2 a2 = points[(i + 1) % points.length];
            Vec2[] otherPoints = other.points;
            for (int j = 0; j < otherPoints.length; j++) {
                Vec2 b1 = otherPoints[j];
                Vec2 b2 = otherPoints[(j + 1) % otherPoints.length];
                if (segmentsIntersect(a1, a2, b1, b2)) {
                    return true;
                }
            }
        }
        return contains(other.points[0]) || other.contains(points[0]);
    }

    private boolean contains(Vec2 point) {
        boolean inside = false;
        for (int i = 0, j = points.length - 1; i < points.length; j = i++) {
            double xi = points[i].getX();
            double yi = points[i].getY();
            double xj = points[j].getX();
            double yj = points[j].getY();
            boolean intersects = ((yi > point.getY()) != (yj > point.getY()))
                    && (point.getX() < (xj - xi) * (point.getY() - yi) / (yj - yi) + xi);
            if (intersects) {
                inside = !inside;
            }
        }
        return inside;
    }

    private static boolean segmentsIntersect(Vec2 p1, Vec2 p2, Vec2 q1, Vec2 q2) {
        double d1 = direction(q1, q2, p1);
        double d2 = direction(q1, q2, p2);
        double d3 = direction(p1, p2, q1);
        double d4 = direction(p1, p2, q2);
        return (((d1 > 0 && d2 < 0) || (d1 < 0 && d2 > 0))
                && ((d3 > 0 && d4 < 0) || (d3 < 0 && d4 > 0)))
                || (d1 == 0 && onSegment(q1, q2, p1))
                || (d2 == 0 && onSegment(q1, q2, p2))
                || (d3 == 0 && onSegment(p1, p2, q1))
                || (d4 == 0 && onSegment(p1, p2, q2));
    }

    private static double direction(Vec2 a, Vec2 b, Vec2 c) {
        return ((c.getX() - a.getX()) * (b.getY() - a.getY()))
                - ((b.getX() - a.getX()) * (c.getY() - a.getY()));
    }

    private static boolean onSegment(Vec2 a, Vec2 b, Vec2 c) {
        return Math.min(a.getX(), b.getX()) <= c.getX()
                && c.getX() <= Math.max(a.getX(), b.getX())
                && Math.min(a.getY(), b.getY()) <= c.getY()
                && c.getY() <= Math.max(a.getY(), b.getY());
    }
}
