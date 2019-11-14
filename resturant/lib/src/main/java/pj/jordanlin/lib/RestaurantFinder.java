package pj.jordanlin.lib;

import com.google.gson.Gson;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;

public class RestaurantFinder {
    public static void main(String[] args) throws IOException
    {

        Gson gson = new Gson();

        String path = args[0];
        Reader restaurantReader = new FileReader(path);
        restaurant[] json = gson.fromJson(restaurantReader, restaurant[].class);


        ArrayList<String> restaurantsNotOpenOnSunday = new ArrayList<>();
        for (int i=0; i<json.length; i++)
        {
            restaurant temp = json[i];

            ArrayList<String> days = temp.separateDays();
            boolean openOnSunday = false;
            for (String tempDays: days)
            {
                if (tempDays.equals("Sun"))
                {
                    openOnSunday = true;
                }
            }

            if (!openOnSunday)
                restaurantsNotOpenOnSunday.add(temp.getName());
        }

        for (String s : restaurantsNotOpenOnSunday)
        {
            System.out.print(s);
            System.out.print("\n");
        }
    }

    public class restaurant
    {
        private String name;
        private String days ;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }


        public ArrayList<String> separateDays()
        {
            String daysInString = days;
            ArrayList<String> separatedList = new ArrayList<>();

            StringBuilder stringBuilder = new StringBuilder();
            for (int i=0; i<daysInString.length(); i++)
            {
                char c = daysInString.charAt(i);
                if (c== ',')
                {
                    separatedList.add(stringBuilder.toString());
                    stringBuilder = new StringBuilder();
                }
                else
                {
                    stringBuilder.append(c);
                }
            }
            separatedList.add(stringBuilder.toString());
            return separatedList;
        }

        public String getDays() {
            return days;
        }

        public void setDays(String days) {
            this.days = days;
        }
    }

}