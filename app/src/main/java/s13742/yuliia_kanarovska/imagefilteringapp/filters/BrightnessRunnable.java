package s13742.yuliia_kanarovska.imagefilteringapp.filters;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

public class BrightnessRunnable implements Runnable {

    private Bitmap img;
    private int x1, x2, y1, y2;
    private Bitmap bmOut;

    public BrightnessRunnable(Bitmap img, int x1, int y1, int x2, int y2){

        if(img == null) return;
        this.img = img;
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        Log.i("BRIGHTNESS RUNNABLE ", "started");

    }

    public Bitmap getResultingImg(){
        return bmOut;
    }

    @Override
    public String toString() {
        return "Brightness";
    }


    @Override
    public void run() {

        int Vmax = 225;
        int Vmin = 30;
        bmOut = Bitmap.createBitmap(img.getWidth(), img.getHeight(), img.getConfig());

        int pixel;
        int R, G, B;

        for (int x = x1; x < x2; x++){
            for (int y = y1; y < y2; y++){
                pixel = img.getPixel(x,y);

                R = Math.max(0, Math.min(255, 255 * (Color.red(pixel) - Vmin)/(Vmax - Vmin)));
                G = Math.max(0, Math.min(255, 255 * (Color.green(pixel) - Vmin)/(Vmax - Vmin)));
                B = Math.max(0, Math.min(255, 255 * (Color.blue(pixel) - Vmin)/(Vmax - Vmin)));
                bmOut.setPixel(x, y, Color.rgb(R,G,B));
            }
        }

    }
}
