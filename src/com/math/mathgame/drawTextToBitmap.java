package com.math.mathgame;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

public class drawTextToBitmap {
    public Bitmap draw(Context mContext,  int resourceId,  String mText,int color) {
        try {
             Resources resources = mContext.getResources();
             //float scale = resources.getDisplayMetrics().density;
                Bitmap bitmap = BitmapFactory.decodeResource(resources, resourceId);

                android.graphics.Bitmap.Config bitmapConfig =   bitmap.getConfig();
                // set default bitmap config if none
                if(bitmapConfig == null) {
                  bitmapConfig = android.graphics.Bitmap.Config.ARGB_8888;
                }
                // resource bitmaps are imutable,
                // so we need to convert it to mutable one
                bitmap = bitmap.copy(bitmapConfig, true);

                Canvas canvas = new Canvas(bitmap);
                // new antialised Paint
                Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
                // text color - #3D3D3D
                paint.setColor(color);
                // text size in pixels
                paint.setTextSize((int) (36));
                // text shadow
                paint.setShadowLayer(1f, 0f, 1f, Color.BLACK);

                // draw text to the Canvas center
                Rect bounds = new Rect();
                paint.getTextBounds(mText, 0, mText.length(), bounds);
                int x = (bitmap.getWidth() - bounds.width())/2;
                int y = (bitmap.getHeight() + bounds.height())/2;

              canvas.drawText(mText, x , y , paint);

                return bitmap;
        } catch (Exception e) {

            return null;
        }

      }
}
