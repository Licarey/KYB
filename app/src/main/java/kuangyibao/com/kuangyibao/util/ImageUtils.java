package kuangyibao.com.kuangyibao.util;

import android.content.Context;
import android.os.Environment;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;


/**加载图片方法类
 * Created by apple on 18-3-17.
 */

public class ImageUtils {

    //默认加载
    public static void loadImageView(Context context , String path, ImageView mImageView) {
        Picasso.with(context).load(path).into(mImageView);
    }

    //默认加载
    public static void loadImageView(Context context , String path , Target target) {
        Picasso.with(context).load(path).into(target);
    }

    //默认加载
    public static void loadImageView(Context context , String path, int width , int height , ImageView mImageView) {
        Picasso.with(context).load(path).resize(width , height).into(mImageView);
    }

    public static void loadImageFromSD(Context context , String sdPath , ImageView imageView){
        File file = new File(sdPath);
        Picasso.with(context).load(file).into(imageView);
    }
}
