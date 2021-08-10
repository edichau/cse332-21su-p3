package queryresponders;

import cse332.interfaces.QueryResponder;

import cse332.types.*;
import cse332.exceptions.*;

import java.util.HashMap;
import java.util.Map;


public class ComplexSequential extends QueryResponder {
    int[][] censusMatrix;
    MapCorners map;
    int numRows;
    int numColumns;

    public ComplexSequential(CensusGroup[] censusData, int numColumns, int numRows) {
        if(censusData == null) { throw new IllegalArgumentException(); }
        this.numRows = numRows;
        this.numColumns = numColumns;

        //pre-processing step 1: initialize corners of map and total population
        this.map = new MapCorners(censusData[0]);
        int population = 0;
        for(CensusGroup group: censusData) {
            map = map.encompass(new MapCorners(group));
            population += group.population;
        }
        this.totalPopulation = population;

        //pre-processing step 2: create 2D array to store population at location indexes
        int[][] censusMatrix = new int[numRows][numColumns];
        double xScale = (map.north - map.south) / (numColumns - 1);
        double yScale = (map.east - map.west) / (numRows - 1);
        double xShift = map.south * -1;
        double yShift = map.west * -1;

        for(CensusGroup group: censusData) {
            int column = (int) ((group.latitude + xShift ) /xScale) ;
            int row = (int) ((group.longitude + yShift) /yScale) ;
            censusMatrix[row][column] += group.population;

        }

        for(int i = 0; i<numRows; i++)
        {
            for(int j = 1; j<numColumns; j++)
            {
                censusMatrix[i][j] += censusMatrix[i][j-1];
            }
        }

        for(int j = 0; j < numColumns; j++) {
            for(int i = numRows-2; i>=0; i--)
            {
                censusMatrix[i][j] += censusMatrix[i + 1][j];
            }
        }

        this.censusMatrix = censusMatrix;

    }

    @Override
    public int getPopulation(int west, int south, int east, int north) {
        return 0;
    }
}
