package jordan.lin;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;

public class MoonSensors {
    public static void main(String[] args)
    {
        /* Enter your code here. Read input from STDIN. Print output to STDOUT. Your class should be named Solution. */

        Scanner scanner = new Scanner(System.in);


        ArrayList<Integer[]> brokenSensors = new ArrayList<>();

        for (int i = 0; i < 51; i++)
        {
            String inputString = scanner.nextLine();
            for (int j=0; j<51;j++)
            {
                char c = inputString.charAt(j);
                if (c=='0')
                {
                    Integer[] coordination = new Integer[2];
                    coordination[0] = j;
                    coordination[1] = i;
                    brokenSensors.add(coordination);
                }
            }
        }

        TreeMap<Double,Integer[]> distanceMap = new TreeMap<>();

        for (Integer[] brokenSensor: brokenSensors)
        {
            double distance = Math.sqrt((brokenSensor[0]-25)*(brokenSensor[0]-25)+(brokenSensor[1]-25)*(brokenSensor[1]-25));
            distanceMap.put(distance,brokenSensor);
        }

        Set set=distanceMap.entrySet();
        Iterator iterator = set.iterator();
        System.out.print("(");
        while (iterator.hasNext())
        {
            Map.Entry entry = (Map.Entry)iterator.next();
            Integer[] target =  (Integer[])entry.getValue();
            System.out.print("(" + target[0] + "," +target[1] + ")");
            if (iterator.hasNext())
                System.out.print(",");
        }
        System.out.print(")");




    }
}
