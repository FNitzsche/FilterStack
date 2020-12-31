package main.filter.assiClasses;

import java.util.function.Supplier;

public class PixelSupplier implements Supplier<Integer[]> {

    int i = 0;
    int j = 0;
    int resX, resY;

    public PixelSupplier(int resX, int resY){
        this.resX = resX;
        this.resY = resY;
    }


    @Override
    public Integer[] get() {
        if (j >= resY){
            return new Integer[]{-1, -1};
        }
        if (i == resX){
            i = 0;
            j++;
        }
        return new Integer[]{i++, j};
    }
}
