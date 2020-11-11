package queryresponders;

import cse332.interfaces.*;
import cse332.types.*;
import cse332.exceptions.*;

public class ComplexLockBased extends QueryResponder {
    final static int NUM_THREADS = 4;

    public ComplexLockBased(CensusData censusData, int numColumns, int numRows) {
        throw new NotYetImplementedException();
    }

    @Override
    public int getPopulation(int west, int south, int east, int north) {
        throw new NotYetImplementedException();
    }
}
