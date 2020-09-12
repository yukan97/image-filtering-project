package s13742.yuliia_kanarovska.imagefilteringapp.filters;

import android.graphics.Bitmap;
import android.graphics.Color;

import s13742.yuliia_kanarovska.imagefilteringapp.utils.Filter;

public class Contrast implements Filter {
    @Override
    public String toString() {
        return "Contrast";
    }
    public Bitmap doContrast(Bitmap src) {
        double k = 0.4;
        double d = Math.tan(((k + 1.0) * Math.PI) / 4.0);

        Bitmap bmOut = Bitmap.createBitmap(src.getWidth(), src.getHeight(), src.getConfig());

        int pixel;

        int width = src.getWidth();
        int height = src.getHeight();

        int  A, R, G, B;


        for (int x = 0; x < width; x++){
            for (int y = 0; y < height; y++){
                pixel = src.getPixel(x,y);

                A = Color.alpha(pixel);
                R = Color.red(pixel);
                G = Color.green(pixel);
                B = Color.blue(pixel);

                R = (int)((0.5 + (((R / 255.0) - 0.5)*d)) * 255.0);
                if (R < 0) { R = 0; }
                else if(R > 255) { R = 255; }
                G = (int)((0.5 + (((G / 255.0) - 0.5)*d)) * 255.0);
                if (G < 0) { G = 0; }
                else if(G > 255) { G = 255; }
                B = (int)((0.5 + (((B / 255.0) - 0.5)*d)) * 255.0);
                if (B < 0) { B = 0; }
                else if(B > 255) { B = 255; }
                bmOut.setPixel(x, y, Color.argb(A,R,G,B));
            }
        }

        return bmOut;
    }

    @Override
    public Bitmap doFilter(Bitmap src) {
        return doContrast(src);
    }
}
