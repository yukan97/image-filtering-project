package s13742.yuliia_kanarovska.imagefilteringapp.filters;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

public class SepiaRunnable implements Runnable {

    @Override
    public String toString() {
        return "Sepia";
    }

    private static double GS_RED = 0.299;
    private static double GS_GREEN = 0.587;
    private static double GS_BLUE = 0.114;
    private static int depth = 20;
    private Bitmap img;
    private int x1, x2, y1, y2;
    private Bitmap bmOut;

    public SepiaRunnable(Bitmap img, int x1, int y1, int x2, int y2){

        if(img == null) return;
        this.img = img;
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        Log.i("SEPIA RUNNABLE ", "started");

    }

    public Bitmap getResultingImg(){
        return bmOut;
    }

    @Override
    public void run() {

        bmOut = Bitmap.createBitmap(img.getWidth(), img.getHeight(), img.getConfig());

        int A, R, G, B;
        int pixel;

        for(int x = x1; x < x2; ++x) {
            for (int y = y1; y < y2; ++y) {

                pixel = img.getPixel(x, y);

                A = Color.alpha(pixel);
                R = Color.red(pixel);
                G = Color.green(pixel);
                B = Color.blue(pixel);

                R = G = B = (int) (GS_RED * R + GS_GREEN * G + GS_BLUE * B);

                R = R + (depth * 2);
                G = G + depth;
                B = B - depth;

                if(R > 255){
                    R = 255;
                }
                if(G > 255){
                    G = 255;
                }
                if(B < 0){
                    B = 0;
                }

                bmOut.setPixel(x, y, Color.argb(A, R, G, B));
            }
        }
    }

}
