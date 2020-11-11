package paralleltasks;

import cse332.exceptions.NotYetImplementedException;
import cse332.types.CensusGroup;

import java.util.concurrent.RecursiveTask;
/*
   1) This class is the parallel version of the getPopulation() method from version 1 for use in version 2
   2) SEQUENTIAL_CUTOFF refers to the maximum number of census groups that should be processed by a single parallel task
   3) The double parameters(w, s, e, n) represent the bounds of the query rectangle
   4) The compute method returns an Integer representing the total population contained in the query rectangle
 */
public class GetPopulationTask extends RecursiveTask<Integer> {
    CensusGroup[] censusGroups;
    int lo, hi;
    double w, s, e, n;
    final static int SEQUENTIAL_CUTOFF = 1000;

    public GetPopulationTask(CensusGroup[] censusGroups, int lo, int hi, double w, double s, double e, double n) {
        throw new NotYetImplementedException();
    }

    // Returns a number for the total population
    @Override
    protected Integer compute() {
        throw new NotYetImplementedException();
    }

    private Integer sequentialGetPopulation(CensusGroup[] censusGroups, int lo, int hi, double w, double s, double e, double n) {
        throw new NotYetImplementedException();
    }
}
