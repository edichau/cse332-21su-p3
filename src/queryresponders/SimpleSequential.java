package queryresponders;

import cse332.interfaces.*;
import cse332.types.*;
import cse332.exceptions.*;

public class SimpleSequential extends QueryResponder {
    MapCorners map;
    CensusGroup[] censusData;
    double xScale;
    double yScale;
    int numRows;
    int numColumns;


    public SimpleSequential(CensusGroup[] censusData, int numColumns, int numRows) {
        if(censusData == null) { throw new IllegalArgumentException(); }

        this.map = new MapCorners(censusData[0]);

        int population = 0;
        for(CensusGroup group: censusData) {
            map = map.encompass(new MapCorners(group));
            population += group.population;
        }

        this.totalPopulation = population;

        this.xScale = (map.east - map.west) / numColumns;
        this.yScale = (map.north - map.south) / numRows;
        this.numRows = numRows;
        this.numColumns = numColumns;
        this.censusData = censusData;
    }

    @Override
    public int getPopulation(int west, int south, int east, int north) {
        if(!validParameters(west, south, east, north)) {
            throw new IllegalArgumentException();
        }

        int queryPopulation = 0;

        double EWMin = map.west + ((west - 1) * xScale);
        double EWMax = map.west + (east * xScale);
        double NSMin = map.south + ((south - 1) * yScale);
        double NSMax = map.south + (north * yScale);

        for(CensusGroup group: censusData) {
            if(group.longitude >= EWMin) {
                if(group.latitude >= NSMin) {
                    if((group.longitude < EWMax) || ((east == numColumns) && (group.longitude <= EWMax))) {
                        if((group.latitude < NSMax) || ((north == numRows) && (group.latitude <= NSMax))) {
                            queryPopulation += group.population;
                        }
                    }
                }
            }
        }
        return queryPopulation;
    }

    private boolean validParameters(int west, int south, int east, int north) {
        if(west < 1 || west > numColumns) { return false; }
        if(south < 1 || south > numRows) { return false; }
        if(east < west || east > numColumns) { return false; }
        if(north < south || north > numRows) { return false; }

        return true;
    }


}
