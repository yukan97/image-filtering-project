package s13742.yuliia_kanarovska.imagefilteringapp.filters;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.os.Debug;
import android.util.Log;

import s13742.yuliia_kanarovska.imagefilteringapp.utils.Filter;

public class ColorCorrection {

    public static Bitmap doColorCorrection(Bitmap src, int red, int green, int blue){

        Bitmap bmOut = Bitmap.createBitmap(src.getWidth(), src.getHeight(), src.getConfig());

        int A, R, G, B;
        int pixel;

        double redCoef = red / 100.0;
        double greenCoef = green / 100.0;
        double blueCoef = blue / 100.0;

        int width = src.getWidth();
        int height = src.getHeight();
        double wr = 0.0;
        double wg = 0.0;
        double wb = 0.0;

//            for(int x = 0; x < width; ++x) {
//                for(int y = 0; y < height; ++y) {
//
//                    pixel = src.getPixel(x, y);
//
//                    A = Color.alpha(pixel);
//                    R = Color.red(pixel);
//                    G = Color.green(pixel);
//                    B = Color.blue(pixel);
//                    //wx = calcWeight(R);
//                    R = (R + red);
//                    if(R > 255){
//                        R = 255;
//                    }else if(R < 0){
//                        R = 0;
//                    }
//                    G = (G + green);
//                    if(G > 255){
//                        G = 255;
//                    }else if(G < 0){
//                        G = 0;
//                    }
//                    B = (B + blue);
//                    if(B > 255){
//                        B = 255;
//                    }else if(B < 0){
//                        B = 0;
//                    }
//                    bmOut.setPixel(x, y, Color.argb(A, R, G, B));
//                }
//            }

        for(int x = 0; x < width; ++x) {
            for(int y = 0; y < height; ++y) {

                pixel = src.getPixel(x, y);

                A = Color.alpha(pixel);
                R = Color.red(pixel);
                G = Color.green(pixel);
                B = Color.blue(pixel);
                wr = calcWeight(R);
                wg = calcWeight(G);
                wb = calcWeight(B);
                R = (int)(R + 255.0*redCoef*wr);
                if(R > 255){
                    R = 255;
                }else if(R < 0){
                    R = 0;
                }
                G = (int)(G + 255.0*greenCoef*wg);
                if(G > 255){
                    G = 255;
                }else if(G < 0){
                    G = 0;
                }
                B = (int)(B + 255.0*blueCoef*wb);
                if(B > 255){
                    B = 255;
                }else if(B < 0){
                    B = 0;
                }
                bmOut.setPixel(x, y, Color.argb(A, R, G, B));
            }
        }

        return bmOut;
    }

    private static double calcWeight(int color){
        double output = 0.0;
        double x = Double.valueOf(color);

        if(color >= 170 ){
            output = 1.075 - (1.0 / ((((255.0 - x) / 255.0) * 16.0 ) + 1.0));
        }else if(color >= 85){
            output = 0.667 * (1.0 - (2.0 * (x/255.0) - 1.0 )*2.0);

        }else {
            output = 1.075 - (1.0 / ((x/255.0) * 16.0 + 1.0));
        }

        return output;
    }

}
