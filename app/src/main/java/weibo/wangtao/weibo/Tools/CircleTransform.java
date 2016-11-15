package weibo.wangtao.weibo.Tools;

/**
 * Created by wangtao on 2016/11/3.
 */
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;

import com.squareup.picasso.Transformation;

import weibo.wangtao.weibo.R;

/**
 * Created by 刘楠 on 2016/8/31 0031.23:09
 */
public class CircleTransform implements Transformation {
    private Context context;
    public CircleTransform(Context context)
    {
        this.context=context;
    }
    @Override
    public Bitmap transform(Bitmap source) {

        /**
         * 求出宽和高的哪个小
         */
        int  size = Math.min(source.getWidth(), source.getHeight());

        /**
         * 求中心点
         */
        int x = (source.getWidth() - size) / 2;
        int y = (source.getHeight() - size) / 2;

        /**
         * 生成BitMap
         */

        Bitmap squaredBitmap = Bitmap.createBitmap(source, x, y, size, size);

        if (squaredBitmap != source) {
            //释放
            source.recycle();
        }

        /**
         * 建立新的Bitmap
         */

          Bitmap bitmap = Bitmap.createBitmap(size, size, source.getConfig());
          /**
           * 画布画笔
           */
          Canvas canvas = new Canvas(bitmap);
          Paint  paint  = new Paint();

          BitmapShader shader = new BitmapShader(squaredBitmap,
                  BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
          paint.setShader(shader);
          paint.setAntiAlias(true);

          float r = size / 2f;
          /**
           * 画圆
           */
          canvas.drawCircle(r, r, r-5, paint);


        Paint  paint1  = new Paint();
        paint1.setAntiAlias(true);
        paint1.setColor(Color.WHITE);
        paint1.setStyle(Paint.Style.STROKE);
        paint1.setStrokeWidth(2);
        canvas.drawCircle(r, r, r-4, paint1);
        paint1.setColor(Color.GRAY);
        paint1.setStyle(Paint.Style.STROKE);
        paint1.setStrokeWidth(2);
        canvas.drawCircle(r, r, r-2, paint1);
          squaredBitmap.recycle();
          return bitmap;
    }

    @Override
    public String key() {
        return "circle";
    }
}
