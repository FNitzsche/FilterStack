package main.filter;

import main.FXMLLoad;

public abstract class Filter {

    public FXMLLoad screen = null;
    
    public abstract float[][][] run(float[][][] img, int resX, int resY, float delta);
}
