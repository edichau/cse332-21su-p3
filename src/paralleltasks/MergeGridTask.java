package paralleltasks;

import cse332.exceptions.NotYetImplementedException;

import java.util.concurrent.RecursiveAction;

/*
   1) This class is used by PopulateGridTask to merge two grids in parallel
   2) SEQUENTIAL_CUTOFF refers to the maximum number of grid positions that should be processed by a single parallel task
 */

public class MergeGridTask extends RecursiveAction {
    int[][] left, right;
    int rowLo, rowHi, colLo, colHi;
    final static int SEQUENTIAL_CUTOFF = 10;

    public MergeGridTask(int[][] left, int[][] right, int rowLo, int rowHi, int colLo, int colHi) {
        throw new NotYetImplementedException();
    }

    protected void compute() {
        throw new NotYetImplementedException();
    }

    private void sequentialMergeGird() {
        throw new NotYetImplementedException();
    }
}
