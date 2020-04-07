package jsonTempClass;

import java.util.ArrayList;

/**
 * temp class to store data from json- names
 */
public class Names {
    ArrayList<String> data;

    /**
     * initialize the class
     *
     * @param data
     */
    public Names(ArrayList<String> data) {
        this.data = data;
    }

    public ArrayList<String> getData() {
        return data;
    }

    public void setData(ArrayList<String> data) {
        this.data = data;
    }
}
