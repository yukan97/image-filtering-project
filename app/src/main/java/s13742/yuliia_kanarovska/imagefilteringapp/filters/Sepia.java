package s13742.yuliia_kanarovska.imagefilteringapp.filters;

import android.graphics.Bitmap;
import android.graphics.Color;

import s13742.yuliia_kanarovska.imagefilteringapp.utils.Filter;

public class Sepia implements Filter{

    @Override
    public String toString() {
        return "Sepia";
    }

    public static double GS_RED = 0.299;
    public static double GS_GREEN = 0.587;
    public static double GS_BLUE = 0.114;
    public static int depth = 20;

    public static Bitmap doSepia(Bitmap src){
        // create output bitmap
        Bitmap bmOut = Bitmap.createBitmap(src.getWidth(), src.getHeight(), src.getConfig());
        // pixel information
        int A, R, G, B;
        int pixel;

        // get image size
        int width = src.getWidth();
        int height = src.getHeight();

        // scan through every single pixel
        for(int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
                // get one pixel color
                pixel = src.getPixel(x, y);
                // retrieve color of all channels
                A = Color.alpha(pixel);
                R = Color.red(pixel);
                G = Color.green(pixel);
                B = Color.blue(pixel);
                // take conversion up to one single value
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
        return bmOut;
    }

    @Override
    public Bitmap doFilter(Bitmap src) {
        return doSepia(src);
    }
}
