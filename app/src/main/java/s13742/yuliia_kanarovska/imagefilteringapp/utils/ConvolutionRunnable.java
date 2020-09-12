package s13742.yuliia_kanarovska.imagefilteringapp.utils;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

public class ConvolutionRunnable implements Runnable {

    private static double[][] kernel = new double[3][3];
    private int sumR = 0;
    private int sumG = 0;
    private int sumB = 0;
    private Bitmap img;
    private int x1, x2, y1, y2;
    private Bitmap bmOut;

    public static void setKernel(double[][] k){
        kernel = k;
    }

    public ConvolutionRunnable(Bitmap img, int x1, int y1, int x2, int y2){

        if(img == null) return;
        this.img = img;
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        Log.i("CONVOLUTION RUNNABLE ", "started");

    }

    public Bitmap getResultingImg(){
        return bmOut;
    }


    @Override
    public void run() {

        int n = kernel.length;
        int sumK = 0;
        int pixel;
        for(int i = 0; i < kernel.length; i++){
            for(int j = 0; j < kernel[i].length; j++){
                sumK += kernel[i][j];
            }
        }

        int beta;

        if(sumK == 0){
            beta = 1;
        }else{
            beta = (int) Math.round(1/sumK);
        }

        bmOut = Bitmap.createBitmap(img.getWidth(), img.getHeight(), img.getConfig());

        int alpha = 1;
        int[][] pixels = new int[3][3];
        for(int x = x1; x < x2-2; x++){
            for(int y = y1; y < y2-2; y++){
                sumR = 0;
                sumG = 0;
                sumB = 0;

                for(int k = 0; k < 3; k++){
                    for (int l = 0; l < 3; l++){
                        pixels[k][l] = img.getPixel(x + k, y + l);
                    }
                }

                for(int k = 0; k < 3; k++){
                    for (int l = 0; l < 3; l++){


                        sumR += Color.red(pixels[k][l]) * kernel[k][l];
                        sumG += Color.green(pixels[k][l]) * kernel[k][l];
                        sumB += Color.blue(pixels[k][l]) * kernel[k][l];
                    }
                }

                if(sumR < 0 ) sumR = 0;
                else if (sumR > 255) sumR = 255;

                if(sumG < 0 ) sumG = 0;
                else if (sumG > 255) sumG = 255;

                if(sumB < 0 ) sumB = 0;
                else if (sumB > 255) sumB = 255;


                bmOut.setPixel(x + 1, y + 1,
                        Color.argb(Color.alpha(pixels[1][1]),
                                beta*sumR, beta*sumG, beta * sumB));

            }
        }

    }

}
