package s13742.yuliia_kanarovska.imagefilteringapp.filters;

import android.graphics.Bitmap;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import s13742.yuliia_kanarovska.imagefilteringapp.utils.Convolution;
import s13742.yuliia_kanarovska.imagefilteringapp.utils.ConvolutionRunnable;
import s13742.yuliia_kanarovska.imagefilteringapp.utils.Filter;

public class Sharpen implements Filter{

    public static Bitmap applySharpenRunnable(Bitmap src){
        //double d = -1.0/9.0;
        double[][] kernelSharpen = new double[][] {
                {-1, -1, -1},
                {-1,  9, -1},
                {-1, -1, -1}
        };
        ConvolutionRunnable.setKernel(kernelSharpen);
        ConvolutionRunnable k1 = new ConvolutionRunnable(src,
                0, 0, src.getWidth()/2 + 2, src.getHeight()/2 + 2);
        ConvolutionRunnable k2 = new ConvolutionRunnable(src,
                src.getWidth()/2 - 2, 0, src.getWidth(), src.getHeight()/2 +2);
        ConvolutionRunnable k3 = new ConvolutionRunnable(src,
                0, src.getHeight()/2 - 2, src.getWidth()/2 + 2, src.getHeight());
        ConvolutionRunnable k4 = new ConvolutionRunnable(src,
                src.getWidth()/2 - 2, src.getHeight()/2 - 2, src.getWidth(), src.getHeight());

        ExecutorService es = Executors.newCachedThreadPool();
        es.execute(new Thread(k1));
        es.execute(new Thread(k2));
        es.execute(new Thread(k3));
        es.execute(new Thread(k4));

        es.shutdown();

        try {
            es.awaitTermination(5, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        Bitmap bmOut = Bitmap.createBitmap(src.getWidth(), src.getHeight(), src.getConfig());

        for (int i = 0; i < src.getWidth(); i++){
            for (int j = 0; j < src.getHeight(); j++){
                if(i <= src.getWidth()/2 && j <= src.getHeight()/2){
                    bmOut.setPixel(i, j, k1.getResultingImg().getPixel(i, j));
                } else if(i >= src.getWidth()/2 && j <= src.getHeight()/2){
                    bmOut.setPixel(i, j, k2.getResultingImg().getPixel(i, j));
                } else if(i <= src.getWidth()/2 && j >= src.getHeight()/2){
                    bmOut.setPixel(i, j, k3.getResultingImg().getPixel(i, j));
                } else if(i >= src.getWidth()/2 && j >= src.getHeight()/2){
                    bmOut.setPixel(i, j, k4.getResultingImg().getPixel(i, j));
                }
            }
        }

        return bmOut;
    }

    @Override
    public String toString() {
        return "Sharpen";
    }

    public static Bitmap doSharpen(Bitmap src){

        double kernelSharpen[][] = new double[][]{
                {-1, -1, -1},
                {-1,  9, -1},
                {-1, -1, -1}
        };


        Convolution.setKernel(kernelSharpen);
        return Convolution.calculate(src);
    }


    @Override
    public Bitmap doFilter(Bitmap src) {
        return doSharpen(src);
    }
}
