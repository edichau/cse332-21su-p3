package queryresponders;

import cse332.interfaces.QueryResponder;

import cse332.types.*;

import java.util.Random;


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
        int[][] censusMatrix = new int[numRows + 1][numColumns + 1];
        double yScale = ( map.north - map.south) / (numRows );
        double xScale = (map.east - map.west) / (numColumns );
        double yShift = map.south * -1;
        double xShift = map.west  * -1;

        //put censusData into table such that:
        //        [1,1](S,W)-------> (S,E) [1,numColumns]
        //
        //
        //
        //[numRows, 1] (N,W)-------> (N,E) [numRows, numColumns]
        for(CensusGroup group: censusData) {
            int row = (int) Math.floor(((group.latitude + yShift) /yScale) + 1);
            int column = (int) Math.floor(((group.longitude + xShift)/ xScale) + 1) ;

            //if row or column is on outermost boarder it goes into max row/column
            if(row > numRows) { row = numRows; }
            if(column > numColumns) { column = numColumns; }

            censusMatrix[row][column] += group.population;
        }

        //add indexes west to east
        for(int y = 1; y<=numRows; y++)
        {
            for(int x = 2; x <= numColumns; x++)
            {
                censusMatrix[y][x] += censusMatrix[y][x-1];
            }
        }

        //add indexes south to north
        for(int x = 1; x <= numColumns; x++) {
            for(int y = 2; y <= numRows; y++)
            {
                censusMatrix[y][x] += censusMatrix[y-1][x];
            }
        }

        this.censusMatrix = censusMatrix;
    }

    @Override
    public int getPopulation(int west, int south, int east, int north) {
        if(!validParameters(west, south, east, north)) { throw new IllegalArgumentException(); }

        int indexPop = censusMatrix[north][east];
        int subSE = 0;
        if(south - 1 > 0) {subSE = censusMatrix[south - 1][east];}
        int subNW = 0;
        if(west - 1 > 0) { subNW = censusMatrix[north][west -1];}
        int addSW = 0;
        if(south - 1 > 0 && west - 1 > 0) { addSW= censusMatrix[south-1][west - 1];}

        return indexPop + addSW - (subSE + subNW);
    }

    private boolean validParameters(int west, int south, int east, int north) {
        if(west < 1 || west > numColumns) { return false; }
        if(south < 1 || south > numRows) { return false; }
        if(east < west || east > numColumns) { return false; }
        if(north < south || north > numRows) { return false; }

        return true;
    }

    public static class Experiments {
        static Query[] queries;
        static CensusGroup[] data;



        public static void init() {
            Random r = new Random();
            queries = new Query[500];

            for(int i = 0; i < 500; i++) {
                int long1= r.nextInt(100);
                int long2= r.nextInt(100);
                int lat1= r.nextInt(500);
                int lat2= r.nextInt(500);
                int E = Math.max(lat1, lat2);
                int N = Math.max(long1, long2);
                int W = Math.min(lat1, lat2);
                int S = Math.min(long1, long2);
                queries[i] = new Query(N, S, E, W);
            }
        }


        private static class Query {
            int n;
            int s;
            int e;
            int w;

            public Query(int north, int south, int east, int west) {
                this.n = north;
                this.s = south;
                this.e = east;
                this.w = west;
            }
        }
    }
}
