package queryresponders;

import java.util.concurrent.ForkJoinPool;

import cse332.interfaces.*;
import cse332.types.*;
import cse332.exceptions.*;
import paralleltasks.CornerFindingTask;
import paralleltasks.GetPopulationTask;

public class SimpleParallel extends QueryResponder {
    private static final ForkJoinPool POOL = new ForkJoinPool();
    CensusGroup[] censusData;
    int numColumns;
    int numRows;
    MapCorners corners;
    CornerFindingResult cornerResult;

    public SimpleParallel(CensusGroup[] censusData, int numColumns, int numRows) {
        this.censusData = censusData;
        this.numColumns = numColumns;
        this.numRows = numRows;

        CornerFindingTask corner = new CornerFindingTask(censusData, 0, censusData.length);
        this.cornerResult = POOL.invoke(corner);

        this.totalPopulation += cornerResult.getTotalPopulation();
    }

    @Override
    public int getPopulation(int west, int south, int east, int north) {

            MapCorners map = cornerResult.getMapCorners();

            double xScale = (map.east - map.west) / numColumns;
            double yScale = (map.north - map.south) / numRows;

            double W = map.west + ((west - 1) * xScale);
            double E = map.west + (xScale * east);
            double N = map.south + ((north) * yScale);
            double S = map.south + (yScale * (south - 1));

            return POOL.invoke(new GetPopulationTask(censusData, 0, censusData.length, W, S, E, N));


    }
}
