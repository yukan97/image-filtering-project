package s13742.yuliia_kanarovska.imagefilteringapp.filters;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

public class ContrastRunnable implements Runnable {

    private Bitmap img;
    private int x1, x2, y1, y2;
    private Bitmap bmOut;

    public ContrastRunnable(Bitmap img, int x1, int y1, int x2, int y2){

        if(img == null) return;
        this.img = img;
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        Log.i("CONTRAST RUNNABLE ", "started");

    }

    public Bitmap getResultingImg(){
        return bmOut;
    }

    @Override
    public String toString() {
        return "Contrast";
    }

    @Override
    public void run() {
        double k = 0.4;
        double d = Math.tan(((k + 1.0) * Math.PI) / 4.0);

        bmOut = Bitmap.createBitmap(img.getWidth(), img.getHeight(), img.getConfig());

        int pixel;
        int  A, R, G, B;


        for (int x = x1; x < x2; x++){
            for (int y = y1; y < y2; y++){
                pixel = img.getPixel(x,y);

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
    }
}
