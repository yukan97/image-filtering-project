package s13742.yuliia_kanarovska.imagefilteringapp.filters;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

import s13742.yuliia_kanarovska.imagefilteringapp.utils.Filter;

public class CustomFilter implements Filter{

    @Override
    public String toString() {
        return "Custom";
    }

    public static int redConf = -20;
    public static int greenConf = 10;
    public static int blueConf = -10;

    public static Bitmap doCustom(Bitmap src) {

        Bitmap bmOut = Bitmap.createBitmap(src.getWidth(), src.getHeight(), src.getConfig());

        int width = src.getWidth();
        int height = src.getHeight();

        int A, R, G, B;
        int pixel;

        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {

                pixel = src.getPixel(x, y);

                A = Color.alpha(pixel);

                R = Color.red(pixel) + redConf;
                G = Color.green(pixel) + greenConf;
                B = Color.blue(pixel) + blueConf;

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
        return doCustom(src);
    }
}
