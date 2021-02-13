package queryresponders;

import cse332.interfaces.QueryResponder;

import cse332.types.*;
import cse332.exceptions.*;

import java.util.concurrent.ForkJoinPool;

public class ComplexSequential extends QueryResponder {

    public ComplexSequential(CensusGroup[] censusData, int numColumns, int numRows) {
        throw new NotYetImplementedException();
    }

    @Override
    public int getPopulation(int west, int south, int east, int north) {
        throw new NotYetImplementedException();
    }
}
