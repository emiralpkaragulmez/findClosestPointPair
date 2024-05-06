import java.awt.geom.Point2D;
public class FindClosest {

    private PointPair closestPointPair;
    private final QuickSort quicksort = new QuickSort();



    /** Constructor
     *
     * @param points --> point array
     */
    public FindClosest(Point2D.Double[] points)
    {
        //Sort points by X coordinate
        quicksort.sort(points, 0, points.length - 1, "compareX");
        this.closestPointPair = calculateClosestPointPair(points, 0, points.length - 1);
        //*********************************do nothing***************************************//
    }

    /** Get closest Point Pair
     *
     * @return closestPointPair
     */
    public PointPair getClosestPointPair()
    {
        return this.closestPointPair;
    }

    /** Main method for calculate and return closest point pair
     *
     * @param p --> point array
     * @param startIndex --> First index of p[]
     * @param lastIndex --> last index of p[]
     * @return
     */
    private PointPair calculateClosestPointPair(Point2D.Double[] p, int startIndex, int lastIndex) {
        PointPair result;
        if ((lastIndex - startIndex) < 2) {
            result = new PointPair(p[startIndex], p[lastIndex]);
            for (int i = startIndex; i < lastIndex; ++i) {
                PointPair currentMinDist = new PointPair(p[i], p[i + 1]);
                if (currentMinDist.getDistance() < result.getDistance()) {
                    result = currentMinDist;
                }
            }
            return result;
        } else if (lastIndex - startIndex == 2) {
            return getClosestPointPair(p[startIndex], p[startIndex + 1], p[lastIndex]);
        }

        int middlePos = ((lastIndex + startIndex) / 2);

        PointPair leftSub = calculateClosestPointPair(p, startIndex, middlePos);
        PointPair rightSub = calculateClosestPointPair(p, middlePos + 1, lastIndex);
        double distance;

        if (leftSub.getDistance() < rightSub.getDistance()) {
            distance = leftSub.getDistance();
            result = leftSub;
        }
        else {
            distance = rightSub.getDistance();
            result = rightSub;
        }

        Point2D.Double[] strip = new Point2D.Double[lastIndex + 1];
        int j = 0;
        for (int i = startIndex; i <= lastIndex; i++) {
            if (Math.abs(p[i].getX() - p[middlePos].getX()) < distance) {
                strip[j] = p[i];
                j++;
            }
        }

        if (stripClosest(strip, j, result).getDistance() < distance) {
            return stripClosest(strip, j, result);
        }

        return result;
    }

    /** calculate and return closest point pair from 3 points
     *
     * @param p1 --> point 1
     * @param p2 --> point 2
     * @param p3 --> point 3
     * @return
     */
    private PointPair getClosestPointPair(Point2D.Double p1, Point2D.Double p2, Point2D.Double p3) {
        PointPair pair12 = new PointPair(p1, p2);
        PointPair pair23 = new PointPair(p2, p3);
        PointPair pair13 = new PointPair(p1, p3);

        return getClosestPointPair(getClosestPointPair(pair12, pair23), pair13);
    }

    private PointPair getClosestPointPair(PointPair p1, PointPair p2){
        if (p1.getDistance() < p2.getDistance()) return p1;
        else return p2;
    }

    /**
     * A utility function to find the distance between the closest points of
     * strip of given size. All points in strip[] are sorted according to
     * y coordinate. They all have an upper bound on minimum distance as d.
     * Note that this method seems to be a O(n^2) method, but it's a O(n)
     * method as the inner loop runs at most 6 times
     *
     * @param strip --> point array
     * @param size --> strip array element count
     * @param shortestLine --> shortest line calculated so far
     * @return --> new shortest line
     */
    private PointPair stripClosest(Point2D.Double strip[], int size, PointPair shortestLine) {
        quicksort.sort(strip, 0, size - 1, "compareY");
        for (int i = 0; i < size; ++i) {
            for (int j = i + 1; j < size && (strip[j].getY() - strip[i].getY()) < shortestLine.getDistance(); ++j) {
                PointPair currentShortest = new PointPair(strip[i], strip[j]);
                if (currentShortest.getDistance() < shortestLine.getDistance())
                    shortestLine = currentShortest;
            }
        }
        return shortestLine;
    }

}
