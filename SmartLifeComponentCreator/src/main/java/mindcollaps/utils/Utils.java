package mindcollaps.utils;

import java.util.ArrayList;

public class Utils {

    public static String[] convertArrayListToStringArray(ArrayList<String> arrayList){
        String[] array = new String[arrayList.size()];
        for (int i = 0; i < arrayList.size(); i++) {
            array[i] = arrayList.get(i);
        }
        return array;
    }
}
