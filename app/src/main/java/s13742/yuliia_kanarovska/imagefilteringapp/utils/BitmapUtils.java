package s13742.yuliia_kanarovska.imagefilteringapp.utils;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import java.io.ByteArrayOutputStream;

public class BitmapUtils {

    public static Bitmap createThumbnail(Bitmap src){

        double width = Double.valueOf(src.getWidth());
        double height = Double.valueOf(src.getWidth());
        double ratio = width/height;

        Log.i("Height ", src.getHeight() + " ");
        Log.i("Width  ", src.getWidth() + " ");
        Log.i("ratio ", (80.0 * ratio) + " ");
        Bitmap thumbnail = Bitmap.createScaledBitmap(src, (int)(80.0 * ratio), 80, true);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, baos);

        return thumbnail;
    }


}
