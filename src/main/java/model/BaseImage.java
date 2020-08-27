package model;

import javafx.scene.image.Image;

import java.net.URL;
import java.util.ArrayList;

public class BaseImage {

    static ArrayList<BaseImage> bases = new ArrayList<>();
    ArrayList<FilterStack> uses = new ArrayList<>();

    Image original;
    Image preview;
    String path;

    float[][][] fullSize;
    float[][][] preSize;

    boolean loaded = false;

    public BaseImage(String path, int preResX, int preResY){
        this.path = path;
        loaded = loadImage(preResX, preResY);
        bases.add(this);
    }

    public void delete(){
        bases.remove(this);
        for (FilterStack stack: uses){
            stack.baseImage = null;
        }
    }


    private boolean loadImage(int preResX, int preResY){
        Image tmp = null;
        Image tmpP = null;
        try {
            URL url = new URL("file:" + path);
            tmp = new Image(url.toString());
            tmpP = new Image(url.toString(), preResX, preResY, true, false);
        } catch (Exception e){
            System.out.println(e);
        }
        if(null == tmpP || null == tmp){
            System.out.println("no Preview");
            return false;
        } else {
            original = tmp;
            preview = tmpP;
        }
        preSize = imgToArray(preview);
        fullSize = imgToArray(original);
        return true;
    }

    public float[][][] imgToArray(Image img){
        float[][][] ret = new float[(int)img.getWidth()][(int)img.getHeight()][3];
        for (int i = 0; i < img.getWidth(); i++) {
            for (int j = 0; j < img.getHeight(); j++) {
                ret[i][j][0] = (float) img.getPixelReader().getColor(i, j).getRed();
                ret[i][j][1] = (float) img.getPixelReader().getColor(i, j).getGreen();
                ret[i][j][2] = (float) img.getPixelReader().getColor(i, j).getBlue();
            }
        }
        return ret;
    }

    public float[][][] getPreSize(){
        return preSize.clone();
    }

    public float[][][] getFullSize(){
        return fullSize.clone();
    }

}
