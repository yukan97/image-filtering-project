package s13742.yuliia_kanarovska.imagefilteringapp.filters;

import android.graphics.Bitmap;
import android.graphics.Color;

import s13742.yuliia_kanarovska.imagefilteringapp.utils.Filter;

public class Brightness implements Filter{

    @Override
    public String toString() {
        return "Brightness";
    }
    public static Bitmap doBrightness(Bitmap src){

        int Vmax = 215;
        int Vmin = 40;
        Bitmap bmOut = Bitmap.createBitmap(src.getWidth(), src.getHeight(), src.getConfig());

        int pixel;

        int width = src.getWidth();
        int height = src.getHeight();

        int R, G, B;

        for (int x = 0; x < width; x++){
            for (int y = 0; y < height; y++){
                pixel = src.getPixel(x,y);

                R = Math.max(0, Math.min(255, 255 * (Color.red(pixel) - Vmin)/(Vmax - Vmin)));
                G = Math.max(0, Math.min(255, 255 * (Color.green(pixel) - Vmin)/(Vmax - Vmin)));
                B = Math.max(0, Math.min(255, 255 * (Color.blue(pixel) - Vmin)/(Vmax - Vmin)));
                bmOut.setPixel(x, y, Color.rgb(R,G,B));
            }
        }

        return bmOut;
    }


    @Override
    public Bitmap doFilter(Bitmap src) {
        return doBrightness(src);
    }
}
