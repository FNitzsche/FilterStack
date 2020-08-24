package main.filter.assiClasses;

import java.util.Comparator;

public class MyCom implements Comparator {
    @Override
    public int compare(Object o, Object t1) {
        float[] a = (float[])o;
        float[] b = (float[])t1;
        if (a[0]>b[0]){
            return 1;
        } else if (a[0]<b[0]){
            return -1;
        }
        return 0;
    }
}
