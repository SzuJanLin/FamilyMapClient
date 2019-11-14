package pj.jordanlin.lib;

import java.io.*;
import java.util.*;

public class Solution {

    public static void main(String[] args) {
        /* Enter your code here. Read input from STDIN. Print output to STDOUT. Your class should be named Solution. */
        Scanner scanner = new Scanner(System.in);

        ArrayList<Double[]> Eminems = new ArrayList<>();
        int test = 0;

        try {
            while (scanner.hasNext())
            {
                System.out.print(scanner.hasNextLine()+" "+test);
                String sr = scanner.nextLine();

                System.out.print(" String is: " +sr);

                Scanner sc = new Scanner(sr);
                double m = 0;
                if (sc.hasNextDouble())
                    m = sc.nextDouble();
                double M = 0;
                if (sc.hasNextDouble())
                    M = sc.nextDouble();

                Double[] Eminem = new Double[2];
                Eminem[0] = m;
                Eminem[1] = M;
                Eminems.add(Eminem);

                System.out.println(" " + scanner.hasNext());
                test++;

            }
        }
        catch (java.util.InputMismatchException e)
        {
            scanner.nextLine();
            System.out.print("Input Error");
            e.printStackTrace();
        }


        for (int i =0; i< Eminems.size(); i++)
        {

            System.out.print(Eminems.get(i)[0] +" " + Eminems.get(i)[1] +"\n");
            double logDistance = (Eminems.get(i)[0] - Eminems.get(i)[1])/5 + 1;
            int distance =  (int) Math.pow(10,logDistance);
            System.out.println(distance);
        }



    }
}