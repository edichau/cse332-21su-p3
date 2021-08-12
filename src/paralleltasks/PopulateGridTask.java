package paralleltasks;

import cse332.exceptions.NotYetImplementedException;
import cse332.types.CensusGroup;
import cse332.types.MapCorners;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
/*
   1) This class is used in version 4 to create the initial grid holding the total population for each grid cell
   2) SEQUENTIAL_CUTOFF refers to the maximum number of census groups that should be processed by a single parallel task
   3) Note that merging the grids from the left and right subtasks should NOT be done in this class.
      You will need to implement the merging in parallel using a separate parallel class (MergeGridTask.java)
 */

public class PopulateGridTask extends RecursiveTask<int[][]> {
    private static final ForkJoinPool POOL = new ForkJoinPool();
    CensusGroup[] censusGroups;
    int lo, hi, numRows, numColumns;
    MapCorners corners;
    double cellWidth, cellHeight;
    final static int SEQUENTIAL_CUTOFF = 10000;

    public PopulateGridTask(CensusGroup[] censusGroups, int lo, int hi, int numRows, int numColumns, MapCorners corners, double cellWidth, double cellHeight) {
        this.lo = lo;
        this.hi = hi;
        this.censusGroups = censusGroups;
        this.numRows = numRows;
        this.numColumns = numColumns;
        this.corners = corners;
        this.cellWidth = cellWidth;
        this.cellHeight = cellHeight;
    }

    @Override
    protected int[][] compute() {
        if(hi-lo <= SEQUENTIAL_CUTOFF){
            return sequentialPopulateGrid(censusGroups,lo,hi,numRows,numColumns,corners,cellWidth,cellHeight);
        }
        int mid = lo + (hi - lo) / 2;

        PopulateGridTask left = new PopulateGridTask(censusGroups,lo,mid,numRows,numColumns,corners,cellWidth,cellHeight);
        PopulateGridTask right = new PopulateGridTask(censusGroups,mid,hi,numRows,numColumns,corners,cellWidth,cellHeight);

        left.fork();
        int[][] rightResult = right.compute();
        int[][] leftResult = left.join();

        POOL.invoke(new MergeGridTask(leftResult,rightResult,0,numRows+1,0, numColumns+1));
        return leftResult;
    }

    private int[][] sequentialPopulateGrid(CensusGroup[] censusGroups, int lo, int hi, int numRows, int numColumns, MapCorners corners, double cellWidth, double cellHeight) {
        int[][] ret = new int[numRows+1][numColumns+1];

        while (lo < hi) {

            int row =  (int) Math.floor(((censusGroups[lo].latitude + (corners.south * -1)) /cellHeight) + 1);
            int col =  (int) Math.floor(((censusGroups[lo].longitude + (corners.west * -1)) /cellWidth) + 1);

            if(row > numRows) row = ret.length - 1;
            if(col > numColumns) col = ret[0].length - 1;

            ret[row][col] += censusGroups[lo].population;

            lo++;
        }

        return ret;
    }
}

