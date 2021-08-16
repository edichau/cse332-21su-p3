package paralleltasks;

import cse332.types.CensusGroup;
import cse332.types.MapCorners;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/*
   1) This class is used in version 5 to create the initial grid holding the total population for each grid cell
        - You should not be using the ForkJoin framework but instead should make use of threads and locks
        - Note: the resulting grid after all threads have finished running should be the same as the final grid from
          PopulateGridTask.java
 */

public class PopulateLockedGridTask extends Thread{
    CensusGroup[] censusGroups;
    int lo, hi, numRows, numColumns;
    MapCorners corners;
    int[][] populationGrid;
    Lock[][] lockGrid;
    double yScale, xScale, xShift, yShift;


    public PopulateLockedGridTask(CensusGroup[] censusGroups, int lo, int hi, int numRows, int numColumns, MapCorners corners,
                                  int[][] popGrid, Lock[][] lockGrid) {
        this.censusGroups = censusGroups;
        this.lo = lo;
        this.hi = hi;
        this.numRows = numRows;
        this.numColumns = numColumns;

        this.corners = corners;
        this.populationGrid = popGrid;

        this.lockGrid = lockGrid;

        this.yScale = ( corners.north - corners.south) / (numRows);
        this.xScale = (corners.east - corners.west) / (numColumns);
        this.yShift = corners.south * -1;
        this.xShift = corners.west  * -1;
    }

    @Override
    public void run() {
        for(int i= lo; i<hi; i++) {
            CensusGroup group = censusGroups[i];

            int row = (int) Math.floor(((group.latitude + yShift) /yScale) + 1);
            int column = (int) Math.floor(((group.longitude + xShift)/ xScale) + 1) ;
            if(row > numRows) { row = numRows; }
            if(column > numColumns) { column = numColumns; }

            try {
                lockGrid[row][column].lock();
                populationGrid[row][column] += group.population;
            } finally {
                lockGrid[row][column].unlock();
            }
        }
    }

}
