package queryresponders;

import java.util.concurrent.ForkJoinPool;

import cse332.interfaces.*;
import cse332.types.*;
import cse332.exceptions.*;

public class ComplexParallel extends QueryResponder {
    private static final ForkJoinPool POOL = new ForkJoinPool();

    public ComplexParallel(CensusGroup[] censusData, int numColumns, int numRows) {
        throw new NotYetImplementedException();
    }

    @Override
    public int getPopulation(int west, int south, int east, int north) {
        throw new NotYetImplementedException();
    }
}
