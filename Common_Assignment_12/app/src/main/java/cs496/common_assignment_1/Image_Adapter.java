package cs496.common_assignment_1;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.opengl.GLES20;
import android.support.v4.view.PagerAdapter;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;
import android.graphics.drawable.Drawable;
import android.content.res.Resources;

import java.io.InputStream;

import uk.co.senab.photoview.PhotoViewAttacher;


public class Image_Adapter extends PagerAdapter{
    Context context;
    private int[] GalImages = Images.images;
    PhotoViewAttacher mAttacher;


    Image_Adapter(Context context){
        this.context=context;
    }
    @Override
    public int getCount() {
        return GalImages.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((ImageView) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView imageView = new ImageView(context);
        int padding = 10;
        imageView.setPadding(padding, padding, padding, padding);
        imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(context.getResources(), GalImages[position], options);
        int scale = 1;

        DisplayMetrics displaymetrics = new DisplayMetrics();
        ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int height = displaymetrics.heightPixels;
        int width = displaymetrics.widthPixels;
        int minsize = Math.min(height / 2 , width / 2);
        if(options.outHeight > minsize || options.outWidth > minsize)
            scale = (int)Math.pow(2, (int)Math.round(Math.log(minsize / (double)Math.max(options.outHeight, options.outWidth)) / Math.log(0.5)));
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        Bitmap resized = BitmapFactory.decodeResource(context.getResources(), GalImages[position], o2);

        imageView.setImageBitmap(resized);
        ((ViewPager) container).addView(imageView, 0);
        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        Drawable d = ((ImageView)object).getDrawable();
        if(d instanceof BitmapDrawable)
        {
            Bitmap bitmap = ((BitmapDrawable)d).getBitmap();
            bitmap.recycle();
            bitmap = null;
        }
        d.setCallback(null);

        ((ViewPager) container).removeView((ImageView) object);
    }
}