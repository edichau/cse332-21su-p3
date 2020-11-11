package paralleltasks;

import cse332.exceptions.NotYetImplementedException;
import cse332.types.CensusGroup;
import cse332.types.MapCorners;

import java.util.concurrent.locks.Lock;

/*
   1) This class is used in version 5 to create the initial grid holding the total population for each grid position
        - You should not be using the ForkJoin framework but instead should make use of threads and locks
        - Note: the resulting grid after all threads have finished running should be the same as the final grid from
          PopulateGridTask.java
 */

public class PopulateLockedGridTask extends Thread{
    CensusGroup[] censusGroups;
    int lo, hi, numRows, numColumns;
    MapCorners corners;
    float cellWidth, cellHeight;
    int[][] populationGrid;
    Lock[][] lockGrid;


    public PopulateLockedGridTask(CensusGroup[] censusGroups, int lo, int hi, int numRows, int numColumns, MapCorners corners,
                                float cellWidth, float cellHeight, int[][] popGrid, Lock[][] lockGrid) {
        throw new NotYetImplementedException();
    }

    @Override
    public void run() {
        throw new NotYetImplementedException();
    }
}
