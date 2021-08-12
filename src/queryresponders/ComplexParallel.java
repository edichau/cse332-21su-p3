package queryresponders;

import java.util.concurrent.ForkJoinPool;

import cse332.interfaces.*;
import cse332.types.*;
import cse332.exceptions.*;
import paralleltasks.CornerFindingTask;
import paralleltasks.PopulateGridTask;

public class ComplexParallel extends QueryResponder {
    private static final ForkJoinPool POOL = new ForkJoinPool();

    CensusGroup[] censusData;
    int[][] grid;

    double cellHeight;
    double cellWidth;
    int cols;
    int rows;
    CornerFindingResult corner;
    MapCorners corners;

    public ComplexParallel(CensusGroup[] censusData, int numColumns, int numRows) {
        this.censusData = censusData;
        this.cols = numColumns;
        this.rows = numRows;

        this.corner = POOL.invoke(new CornerFindingTask(censusData,0,censusData.length));
        this.totalPopulation = corner.getTotalPopulation();
        this.corners = corner.getMapCorners();

        this.cellWidth = (this.corners.east - this.corners.west)/numColumns;
        this.cellHeight = (this.corners.north - this.corners.south)/numRows;

        this.grid = POOL.invoke(new PopulateGridTask(censusData,0,censusData.length,numRows,numColumns,corners,cellWidth,cellHeight));

        for(int y = 1; y<=numRows; y++)
        {
            for(int x = 2; x <= numColumns; x++)
            {
                grid[y][x] += grid[y][x-1];
            }
        }

        //add indexes south to north
        for(int x = 1; x <= numColumns; x++) {
            for(int y = 2; y <= numRows; y++)
            {
                grid[y][x] += grid[y-1][x];
            }
        }
    }

    @Override
    public int getPopulation(int west, int south, int east, int north) {
        int indexPop = grid[north][east];
        int subSE = 0;
        if(south - 1 > 0) {subSE = grid[south - 1][east];}
        int subNW = 0;
        if(west - 1 > 0) { subNW = grid[north][west -1];}
        int addSW = 0;
        if(south - 1 > 0 && west - 1 > 0) { addSW= grid[south-1][west - 1];}

        return indexPop + addSW - (subSE + subNW);
    }
}
