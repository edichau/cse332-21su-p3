package queryresponders;

import cse332.interfaces.*;
import cse332.types.*;
import cse332.exceptions.*;

import java.util.concurrent.ForkJoinPool;

public class ComplexLockBased extends QueryResponder {
    private static final ForkJoinPool POOL = new ForkJoinPool(); // only to invoke CornerFindingTask
    public int NUM_THREADS = 4;

    public ComplexLockBased(CensusGroup[] censusData, int numColumns, int numRows) {
        throw new NotYetImplementedException();
    }

    @Override
    public int getPopulation(int west, int south, int east, int north) {
        throw new NotYetImplementedException();
    }

}
