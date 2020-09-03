package s13742.yuliia_kanarovska.imagefilteringapp.utils;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

public class Convolution {

    public static Bitmap result;
    public static double[][] kernel = new double[3][3];
    public static int sumR = 0;
    public static int sumG = 0;
    public static int sumB = 0;

    public static void setKernel(double[][] k){
        kernel = k;
    }

    public static Bitmap calculate(Bitmap src){
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

        result = Bitmap.createBitmap(src.getWidth(), src.getHeight(), src.getConfig());

        int alpha = 1;
        int[][] pixels = new int[3][3];
        for(int x = 0; x < src.getWidth()-2; x++){
            for(int y = 0; y < src.getHeight()-2; y++){
                sumR = 0;
                sumG = 0;
                sumB = 0;

                for(int k = 0; k < 3; k++){
                    for (int l = 0; l < 3; l++){
                        pixels[k][l] = src.getPixel(x + k, y + l);
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


                result.setPixel(x + 1, y + 1, Color.argb(Color.alpha(pixels[1][1]),beta*sumR, beta*sumG, beta * sumB));

            }
        }
        return result;
    }

}
