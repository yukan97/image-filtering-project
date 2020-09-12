package s13742.yuliia_kanarovska.imagefilteringapp.filters;

import android.graphics.Bitmap;
import android.graphics.Color;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import s13742.yuliia_kanarovska.imagefilteringapp.utils.Convolution;
import s13742.yuliia_kanarovska.imagefilteringapp.utils.ConvolutionRunnable;
import s13742.yuliia_kanarovska.imagefilteringapp.utils.Filter;

public class Blur implements Filter {

    public static Bitmap applyBlurRunnable(Bitmap src){
        double d = 1.0/9.0;
        double[][] kernelBlur = new double[][] {
                { d, d, d },
                { d, d, d },
                { d, d, d }
        };
        ConvolutionRunnable.setKernel(kernelBlur);
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
        return "Blur";
    }

    public static Bitmap applyBlur(Bitmap src){
        double d = 1.0/9.0;
        double[][] kernelBlur = new double[][] {
                { d, d, d },
                { d, d, d },
                { d, d, d }
        };
        Convolution.setKernel(kernelBlur);
        return Convolution.calculate(src);
    }

    @Override
    public Bitmap doFilter(Bitmap src) {
        return applyBlur(src);
    }
}
