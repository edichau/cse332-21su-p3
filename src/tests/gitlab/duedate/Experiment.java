package tests.gitlab.duedate;

import cse332.types.CensusGroup;
import org.junit.BeforeClass;
import org.junit.Test;
import queryresponders.ComplexSequential;
import queryresponders.SimpleSequential;
import tests.gitlab.QueryResponderTests;

import java.util.Random;

import static org.junit.Assert.assertEquals;

public class Experiment extends QueryResponderTests{

    static CensusGroup[] data;
    static Query[] queries;

    @BeforeClass
    public static void init() {
        data = readCensusdata();
    }

    private static class Query {
        int north;
        int south;
        int east;
        int west;

        public Query(int north, int south, int east, int west) {
            this.north = north;
            this.south = south;
            this.east = east;
            this.west = west;
        }
    }

    /**
     * 100 x 500 grid
     */

    @Test
    public void testFullMap_100_500() {
        long start;
        long end;
        long[] simpleData = new long[10];
        long[] complexData = new long[10];
        int[] numQueries = {1, 2, 3, 4, 5, 10, 25, 50, 75, 100};

        for(int i=0; i< numQueries.length; i++) {
            //create new data set of length n
            Random r = new Random();
            queries = new Query[numQueries[i]];
            for(int k = 0; k < numQueries[i]; k++) {
                int long1= r.nextInt(100) + 1;
                int long2= r.nextInt(100) + 1;
                int lat1= r.nextInt(500) + 1;
                int lat2= r.nextInt(500) + 1;
                int N = Math.max(lat1, lat2);
                int E = Math.max(long1, long2);
                int S = Math.min(lat1, lat2);
                int W = Math.min(long1, long2);
                queries[k] = new Query(N, S, E, W);
            }


            //time to search n queries in Simple
            start = System.nanoTime();
            //we make sure to start with a new data structure each time
            STUDENT_100_500 = new SimpleSequential(data, 100, 500);
            for (int s = 0; s < numQueries[i]; s++) {
                Query q = queries[s];
                STUDENT_100_500.getPopulation(q.west, q.south, q.east, q.north);
            }
            end = System.nanoTime();
            long timeS = Math.subtractExact(end, start);
            simpleData[i] = timeS;

            //time to search n queries in Complex
            start = System.nanoTime();
            STUDENT_100_500 = new ComplexSequential(data, 100, 500);
            for (int c = 0; c < numQueries[i]; c++) {
                Query q = queries[c];
                STUDENT_100_500.getPopulation(q.west, q.south, q.east, q.north);
            }
            end = System.nanoTime();
            long timeC = Math.subtractExact(end, start);
            complexData[i] = timeC;
        }


        //Print results to put into table
        System.out.println("Simple");
        for(int i=0; i<numQueries.length; i++) {
            System.out.println(simpleData[i]);
        }

        System.out.println();
        System.out.println();
        System.out.println("Complex");
        for(int i=0; i<numQueries.length; i++) {
            System.out.println(complexData[i]);
        }
    }


}