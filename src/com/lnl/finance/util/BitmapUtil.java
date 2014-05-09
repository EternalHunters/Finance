package com.lnl.finance.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

public class BitmapUtil {
	
	/**
	 * 水平平铺
	 * @param width
	 * @param src
	 * @return
	 */
	public static Bitmap createWidthRepeater(int width, Bitmap src) {
		if(width<=0||width<=src.getWidth()){
			return src;
		}
		int count = (width + src.getWidth() - 1) / src.getWidth();
		Bitmap bitmap = Bitmap.createBitmap(width, src.getHeight(),
				Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);

		for (int idx = 0; idx < count; ++idx) {
			canvas.drawBitmap(src, idx * src.getWidth(), 0, null);
		}
		return bitmap;
	}
	
	/**
	 * 竖直平铺
	 * @param height
	 * @param src
	 * @return
	 */
	public static Bitmap createHeightRepeater(int height, Bitmap src){
		if(height<=0||height<=src.getHeight()){
			return src;
		}
		int count = (height + src.getHeight() - 1) / src.getHeight();
		Bitmap bitmap = Bitmap.createBitmap(src.getWidth(), height,
				Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);

		for (int idx = 0; idx < count; ++idx) {
			canvas.drawBitmap(src,0,  idx * src.getHeight(), null);
		}
		return bitmap;
	}
	
	
	public static Bitmap drawable2Bitmap(Drawable drawable){
		BitmapDrawable bitmapDrawable = (BitmapDrawable)drawable;
		return bitmapDrawable.getBitmap();
	}
	
	public static Drawable bitmap2Drawable(Bitmap bitmap){
		BitmapDrawable bitmapDrawable = new BitmapDrawable(bitmap);
		return bitmapDrawable;
	}
	
	/**
	 * 把图片变成圆角
	 * 
	 * @param bitmap
	 *            需要变化的图片
	 * @param pixels
	 *            圆角的弧度
	 * @return 圆角图片
	 */
	public static Bitmap toRoundCorner(Bitmap bitmap, int pixels) {
		Bitmap output = bitmap;
		try {
			output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
			Canvas canvas = new Canvas(output);

			final int color = 0xff424242;
			final Paint paint = new Paint();
			final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
			final RectF rectF = new RectF(rect);
			final float roundPx = pixels;

			paint.setAntiAlias(true);
			canvas.drawARGB(0, 0, 0, 0);
			paint.setColor(color);
			canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

			paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
			canvas.drawBitmap(bitmap, rect, rect, paint);
			
			
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
		}
		return output;
	}
	
	
	
	private static Bitmap compassBitmap(Bitmap src, Bitmap.CompressFormat format, int quality) {
		
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		src.compress(format, quality, os);
 
		byte[] array = os.toByteArray();
		return BitmapFactory.decodeByteArray(array, 0, array.length);
	}

	
	public static Bitmap createRGBImage(Bitmap bitmap,int color)
	{
		int bitmap_w=bitmap.getWidth();
		int bitmap_h=bitmap.getHeight();
	    int count=0;
	    
	    int[] newArrayColor = new int[bitmap_w*bitmap_h*4];
	    for(int i=0;i<2*bitmap_h;i++){
	        for(int j=0;j<2*bitmap_w;j++){
	        	newArrayColor[count] = color;
	        	count++;
	        }
	    }
	    Bitmap bgBitMap = Bitmap.createBitmap( newArrayColor, (int)(1.5*bitmap_w), (int)(1.5*bitmap_h), Config.ARGB_4444 );
	    
	    Bitmap bitmap3 = Bitmap.createBitmap(bgBitMap.getWidth(), bgBitMap.getHeight(), bgBitMap.getConfig());
	    Canvas canvas = new Canvas(bitmap3);
	    canvas.drawBitmap(bgBitMap, new Matrix(), null);
	    canvas.drawBitmap(bitmap, (int)(1/6.0*bgBitMap.getWidth()), (int)(1/6.0*bgBitMap.getHeight()), null);  //120、350为bitmap2写入点的x、y坐标
	    
	    
	    
	    return bitmap3;
	}
	
	public static Bitmap replaceColorInImage(Bitmap bitmap, int color){
		int bitmap_w=bitmap.getWidth();
		int bitmap_h=bitmap.getHeight();
	    int count=0;
	    
	    int[] newArrayColor = new int[bitmap_w*bitmap_h];
	    for(int i=0;i<bitmap_h;i++){
	        for(int j=0;j<bitmap_w;j++){
	        	
	        	System.out.println(""+newArrayColor[count]);
	        	
	        	count++;
	        }
	    }
	    
	    Bitmap newBitMap = Bitmap.createBitmap( newArrayColor, (int)(bitmap_w), (int)(bitmap_h), Config.ARGB_4444 );
	    return newBitMap;
	}
	
}
