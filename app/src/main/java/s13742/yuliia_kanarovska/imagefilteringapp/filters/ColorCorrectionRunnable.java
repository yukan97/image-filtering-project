package s13742.yuliia_kanarovska.imagefilteringapp.filters;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

public class ColorCorrectionRunnable implements Runnable{

    private Bitmap img;
    private int x1, x2, y1, y2, red, green, blue;
    private Bitmap bmOut;

    public ColorCorrectionRunnable(Bitmap img, int x1, int y1, int x2, int y2, int red, int green, int blue){

        if(img == null) return;
        this.img = img;
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.red = red;
        this.green = green;
        this.blue = blue;
        Log.i("BRIGHTNESS RUNNABLE ", "started");

    }

    public Bitmap getResultingImg(){
        return bmOut;
    }


    @Override
    public void run() {

        bmOut = Bitmap.createBitmap(img.getWidth(), img.getHeight(), img.getConfig());

        int A, R, G, B;
        int pixel;

        double redCoef = red / 100.0;
        double greenCoef = green / 100.0;
        double blueCoef = blue / 100.0;

        double wr = 0.0;
        double wg = 0.0;
        double wb = 0.0;

        for(int x = x1; x < x2; ++x) {
            for (int y = y1; y < y2; ++y) {

                pixel = img.getPixel(x, y);

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
