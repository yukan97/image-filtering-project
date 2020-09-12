package s13742.yuliia_kanarovska.imagefilteringapp.utils;

import android.graphics.Bitmap;

public class ThumbnailModel {
    public String filterTitle;
    public Bitmap img;
    public Filter filter;

    public ThumbnailModel(Bitmap src, Filter filter){
        this.filter = filter;
        img = filter.doFilter(src);
        filterTitle = filter.toString();
    }
}
