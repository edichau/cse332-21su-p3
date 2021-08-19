package queryresponders;

import cse332.interfaces.*;
import cse332.types.*;
import paralleltasks.CornerFindingTask;
import paralleltasks.PopulateLockedGridTask;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ComplexLockBased extends QueryResponder {
    private static final ForkJoinPool POOL = new ForkJoinPool(); // only to invoke CornerFindingTask
    public int NUM_THREADS = 4;
    CornerFindingResult corner;
    MapCorners corners;
    int[][] populationGrid;
    int cols, rows;
    CensusGroup[] censusData;
    Lock[][] lockGrid;


    public ComplexLockBased(CensusGroup[] censusData, int numColumns, int numRows) {
        this.censusData = censusData;
        this.cols = numColumns;
        this.rows = numRows;

        this.corner = POOL.invoke(new CornerFindingTask(censusData,0,censusData.length));
        this.totalPopulation = corner.getTotalPopulation();
        this.corners = corner.getMapCorners();

        this.populationGrid= new int[numRows + 1][numColumns + 1];
        //initialize matrix of locks to lock populationGrid cells
        this.lockGrid = new Lock[numRows + 1][numColumns + 1];
        for(int i = 0; i <= numRows; i++) {
            for(int j = 0; j<= numColumns; j++) {
                lockGrid[i][j] = new ReentrantLock();
            }
        }

        int work = censusData.length/NUM_THREADS;
        PopulateLockedGridTask[] tasks = new PopulateLockedGridTask[NUM_THREADS];
        for(int i=0; i< NUM_THREADS; i++) {
            tasks[i] = new PopulateLockedGridTask(censusData, (i * work), (i + 1) * work, numRows, numColumns, corners, populationGrid, lockGrid);
            if(i != NUM_THREADS - 1){
                tasks[i].start();
            } else {
                tasks[i].run();
            }

        }

        for(int i=0; i< NUM_THREADS; i++) {
            try {
                tasks[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
                System.exit(1);
            }
        }

        //add indexes west to east
        //add indexes west to east
        for(int y = 1; y<=numRows; y++)
        {
            for(int x = 2; x <= numColumns; x++)
            {
                populationGrid[y][x] += populationGrid[y][x-1];
            }
        }

        //add indexes south to north
        for(int x = 1; x <= numColumns; x++) {
            for(int y = 2; y <= numRows; y++)
            {
                populationGrid[y][x] += populationGrid[y-1][x];
            }
        }

    }


    @Override
    public int getPopulation(int west, int south, int east, int north) {
        int indexPop = populationGrid[north][east];
        int subSE = 0;
        if(south - 1 > 0) {subSE = populationGrid[south - 1][east];}
        int subNW = 0;
        if(west - 1 > 0) { subNW = populationGrid[north][west -1];}
        int addSW = 0;
        if(south - 1 > 0 && west - 1 > 0) { addSW= populationGrid[south-1][west - 1];}

        return indexPop + addSW - (subSE + subNW);
    }

}
