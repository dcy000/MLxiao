package com.witspring.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.os.Build;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.View;

import com.witspring.util.file.FileCst;
import com.witspring.util.file.SizeCst;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Bitmap工具类
 */
public class BitmapUtil {

    private final static String TAG = BitmapUtil.class.getSimpleName();

    /**
     * 高斯模糊
     */
    public static Bitmap blurBitmap(Context context, Bitmap bitmap){
        Bitmap outBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
        RenderScript rs = RenderScript.create(context);
        ScriptIntrinsicBlur blurScript = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            blurScript = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
        }
        Allocation allIn = Allocation.createFromBitmap(rs, bitmap);
        Allocation allOut = Allocation.createFromBitmap(rs, outBitmap);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            blurScript.setRadius(25.f);
            blurScript.setInput(allIn);
            blurScript.forEach(allOut);
        }
        allOut.copyTo(outBitmap);
        rs.destroy();
        return outBitmap;

    }

    /**
     * RGB_565
     */
    public static Config getRGBConfig() {
        return Config.RGB_565;
    }

    /**
     * 画圆形图片
     * @param bitmap 输入的图片bitmap
     * @param pixels 圆形弧度
     */
    public static Bitmap toRoundCorner(Bitmap bitmap, float pixels) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), getRGBConfig());
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
        return output;
    }

    /**
     * 缩放图片
     */
    public static Bitmap zoom(Bitmap bitmap, float zf) {
        Matrix matrix = new Matrix();
        matrix.postScale(zf, zf);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    /**
     * 图片拉伸
     */
    public static Bitmap scaleBitmap(Bitmap bm, int dstWidth, int dstHeight) {
        Bitmap outBm = Bitmap.createBitmap(dstWidth, dstHeight, getRGBConfig());
        Canvas canvas = new Canvas(outBm);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        int width = bm.getWidth(), height = bm.getHeight();
        int left = 0, top = 0;
        if (width > dstWidth) {
            left = (width - dstWidth) / 2;
        } else {
            width = dstWidth;
        }
        if (height > dstHeight) {
            top = (height - dstHeight) / 2;
        } else {
            height = dstHeight;
        }
        Rect rect = new Rect(left, top, left + dstWidth, top + dstHeight);
        Rect bmRect = new Rect(0, 0, width, height);
        canvas.save();
        canvas.drawBitmap(bm, null, bmRect, paint);
        canvas.clipRect(rect);
        canvas.restore();
        return outBm;
    }

    /**
     * 图片转Base64
     */
    public static String bitmapToString(Bitmap bitmap) {
        String baseStr = null;
        try {
            ByteArrayOutputStream bStream = new ByteArrayOutputStream();
            bitmap.compress(CompressFormat.PNG, 100, bStream);
            byte[] bytes = bStream.toByteArray();
            baseStr = Base64.encodeToString(bytes, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return baseStr;
    }

    /**
     * 压缩图片
     * @params image 图片Bitmap对象
     */
    public static Bitmap compressImage(Bitmap image, int maxKB) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(CompressFormat.PNG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int quality = 100;
        while (baos.toByteArray().length / SizeCst.KB > maxKB) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();// 重置baos即清空baos
            quality -= 5;// 每次都减少5
            image.compress(CompressFormat.PNG, quality, baos);// 这里压缩options%，把压缩后的数据存放到baos中
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
        return bitmap;
    }

    /**
     * 压缩图片
     * @params input 输入的图片路径
     * @params output 输出的图片路径
     */
    public static boolean compressImage(String input, String output) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        int sampleSize = 100;
        opts.inSampleSize = sampleSize;
        Bitmap inImage = BitmapFactory.decodeFile(input, opts);
        return true;
    }

    public static String compressAndSave(String imgPath, int maxKB, boolean isThum) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;// 只是获取图片的头部信息宽、高
        BitmapFactory.decodeFile(imgPath, options);
        int imageHeight = options.outHeight;
        int imageWidth = options.outWidth;
        int scale = 1;
        int maxValue = imageWidth > imageHeight ? imageWidth : imageHeight;
        if (isThum) {
            if (imageWidth > 64 || imageHeight > 64) {
                scale = maxValue / 64;
            }
        } else {
            if (imageWidth > 512 || imageHeight > 512) {
                scale = maxValue / 512;
            }
        }
        options.inJustDecodeBounds = false;// 解析图片
        options.inSampleSize = scale;// 设置缩放比
        Bitmap bitmap = BitmapFactory.decodeFile(imgPath, options);// 缩放图片

        CompressFormat format = CompressFormat.JPEG;
//        if (imgPath.endsWith(FileCst.SUFFIX_JPG) || imgPath.endsWith(FileCst.SUFFIX_JPEG)) {
//            format = CompressFormat.JPEG;
//        } else {
//            format = CompressFormat.PNG;
//        }
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        bitmap.compress(format, 100, byteOut);// 把数据存放到baos中
        int quality = 100;
        // 循环判断压缩后图片是否大于maxKB,大于继续压缩
        while (byteOut.toByteArray().length / SizeCst.KB > maxKB) {
            byteOut.reset();// 重置baos
            quality -= 5;// 每次都减少5
            bitmap.compress(format, quality, byteOut);
        }
        try {
            FileOutputStream fileOut = new FileOutputStream(imgPath);
            byte[] buffer = byteOut.toByteArray();
            fileOut.write(buffer, 0, buffer.length);
            fileOut.flush();
            fileOut.close();
            return imgPath;
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        } finally {
            if (bitmap != null && !bitmap.isRecycled()) {
                bitmap.recycle();
            }
        }
        return null;
    }

    /**
     * 获取图片信息，主要是宽，高和大小
     * @return int[] [0] 宽|[1] 高 |[2] 文件大小
     */
    public static int[] getImageInfo(String input) {
        int[] values = new int[3];
        Bitmap inImage = null;
        try {
            File f = new File(input);
            if (f.exists()) {
                BitmapFactory.Options opts = new BitmapFactory.Options();
                opts.inJustDecodeBounds = true;
                inImage = BitmapFactory.decodeFile(f.getAbsolutePath(), opts);
                values[0] = opts.outWidth;
                values[1] = opts.outHeight;
                values[2] = (int) f.length();
            }
        } catch (Exception e) {
        } finally {
            if (null != inImage && !inImage.isRecycled()) {
                inImage.recycle();
                System.gc();
            }
        }
        return values;
    }

    /**
     * //获取相应路径下压缩的图片，避免OOM
     * @param isThum 是否缩略图
     */
    public static Bitmap openImgByPath(String path, boolean isThum) {
        BitmapFactory.Options options = new BitmapFactory.Options();// 不去真的解析图片，只是获取图片的头部信息宽，高
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path,options);
        int imageHeight = options.outHeight ;
        int imageWidth = options.outWidth;
        int scale = 1;
        int maxValue = imageWidth > imageHeight ? imageWidth : imageHeight;
        if (isThum) {
            if (imageWidth > 64 || imageHeight > 64) {
                scale = maxValue / 64;
            }
        } else {
            if (imageWidth > 512 || imageHeight > 512) {
                scale = maxValue / 512;
            }
        }
        options.inJustDecodeBounds = false;//真的解析图片
        options.inSampleSize = scale;//设置采样率
        return BitmapFactory.decodeFile(path, options);
    }

    /**
     * 保存位图
     */
    public static boolean saveBitmap(Bitmap bitmap, String path, CompressFormat format) {
        if (bitmap == null || StringUtil.isTrimBlank(path))
            return false;
        FileOutputStream fileOut = null;
        try {
            ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
            if (format == null) {
                if (path.endsWith(FileCst.SUFFIX_PNG)) {
                    format = CompressFormat.PNG;
                } else if (path.endsWith(FileCst.SUFFIX_JPG) || path.endsWith(FileCst.SUFFIX_JPEG)) {
                    format = CompressFormat.JPEG;
                } else {
                    format = CompressFormat.PNG;
                }
            }
            bitmap.compress(format, 100, byteOut);
            byte[] buffer = byteOut.toByteArray();

            fileOut = new FileOutputStream(path);
            fileOut.write(buffer, 0, buffer.length);
            fileOut.flush();
        } catch (Exception ex) {
            return false;
        } finally {
            try {
                if (fileOut != null) {
                    fileOut.close();
                    fileOut = null;
                    return true;
                } else {
                    return false;
                }
            } catch (Exception ex) {
                return false;
            }
        }
    }

    /**
     * 图片通过base64编码成字符串输出String
     */
    public static String convertBitmapToString(String path) {
        if (StringUtil.isNotBlank(path) && new File(path).exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(path);
            if (bitmap != null) {
                try {
                    ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
                    CompressFormat format;
                    if (path.endsWith(FileCst.SUFFIX_PNG)) {
                        format = CompressFormat.PNG;
                    } else if (path.endsWith(FileCst.SUFFIX_JPG) || path.endsWith(FileCst.SUFFIX_JPEG)) {
                        format = CompressFormat.JPEG;
                    } else {
                        format = CompressFormat.PNG;
                    }
                    bitmap.compress(format, 100, byteOut);
                    byte[] buffer = byteOut.toByteArray();
                    return new String(Base64.encode(buffer, Base64.DEFAULT));

                } catch (Exception ex) {
                    return null;
                } finally {
                    try {
                        if (bitmap != null) {
                            bitmap.recycle();
                            bitmap = null;
                        }
                    } catch (Exception ex) {
                    }
                }
            } else {
                return null;
            }

        } else {
            return null;
        }
    }

    /**
     * 获取图片信息，主要是宽，高
     * @return int[] [0] 宽|[1] 高
     */
    public static int[] getImageInfo(int id ,Context ctx) {
        int[] values = new int[2];
        Bitmap bitmap = null;
        try {
            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inJustDecodeBounds = true;
            bitmap = BitmapFactory.decodeResource(ctx.getResources(), id, opts);
            values[0] = opts.outWidth;
            values[1] = opts.outHeight;
        } catch (Exception e) {
        } finally {
            if (null != bitmap && !bitmap.isRecycled()) {
                bitmap.recycle();
                System.gc();
            }
        }
        return values;
    }

    /**
     * 读取图片的旋转的角度
     * @param path 图片绝对路径
     * @return 图片的旋转角度:0 or 90 or 180 or 270
     */
    public static int getBitmapDegree(String path) {
        int degree = 0;
        try {
            // 从指定路径下读取图片，并获取其EXIF信息
            ExifInterface exifInterface = new ExifInterface(path);
            // 获取图片的旋转信息
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * 将图片按照某个角度进行旋转
     * @param bm     需要旋转的图片
     * @param degree 旋转角度
     * @return 旋转后的图片
     */
    public static Bitmap rotateBitmapByDegree(Bitmap bm, int degree) {
        if (bm == null) {
            return null;
        }
        Bitmap returnBm = null;
        // 根据旋转角度，生成旋转矩阵
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        try {
            if (!bm.isRecycled()) {
                // 将原始图片按照旋转矩阵进行旋转，并得到新的图片
                returnBm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
            }
        } catch (Throwable th) {
        }
        if (returnBm == null) {
            returnBm = bm;
        }
        if (bm != returnBm) {
            bm.recycle();
        }
        return returnBm;
    }

    public static Bitmap convertViewToBitmap(View view){
        view.clearFocus();
        view.setPressed(false);

        //能画缓存就返回false
        boolean willNotCache = view.willNotCacheDrawing();
        view.setWillNotCacheDrawing(false);
        int color = view.getDrawingCacheBackgroundColor();
        view.setDrawingCacheBackgroundColor(0);
        if (color != 0) {
            view.destroyDrawingCache();
        }
        view.buildDrawingCache();
        Bitmap cacheBitmap = view.getDrawingCache();
        if (cacheBitmap == null) {
            Log.e(TAG, "failed getViewBitmap(" + view + ")", new RuntimeException());
            return null;
        }
        Bitmap bitmap = Bitmap.createBitmap(cacheBitmap);
        // Restore the view
        view.destroyDrawingCache();
        view.setWillNotCacheDrawing(willNotCache);
        view.setDrawingCacheBackgroundColor(color);
        return bitmap;
    }

    /**
     * 截取可见屏幕，包括状态栏
     */
    public static Bitmap shotScreen(Activity activity) {
        View view = activity.getWindow().getDecorView();
        Display display = activity.getWindowManager().getDefaultDisplay();
        view.layout(0, 0, display.getWidth(), display.getHeight());
        // 允许当前窗口保存缓存信息，这样getDrawingCache()方法才会返回一个Bitmap
        view.setDrawingCacheEnabled(true);
        return Bitmap.createBitmap(view.getDrawingCache());
    }

    /**
     * 截取可见屏幕，不包括状态栏
     */
    public static Bitmap shotActivity(Activity activity) throws Exception {
        View view = activity.getWindow().getDecorView();
        Rect frame = new Rect();
        view.getWindowVisibleDisplayFrame(frame);
        view.setDrawingCacheEnabled(true);
        view.destroyDrawingCache();// 更新cache
        return Bitmap.createBitmap(view.getDrawingCache(), 0, frame.top, frame.width(), frame.height());
    }

    /**
     * 截取view的根层可见屏幕部分的视图
     */
    public static Bitmap getRootViewBitmap(View view) {
        return shotViewBitmap(view.getRootView());
    }

    /**
     * 截取可见屏幕部分的view视图
     */
    public static Bitmap shotViewBitmap(View v) {
        v.clearFocus();
        v.setPressed(false);
        Bitmap bmp = null;
        try {
            v.layout(0, 0, v.getWidth(), v.getHeight());
            // 允许当前窗口保存缓存信息，这样getDrawingCache()方法才会返回一个Bitmap
            v.setDrawingCacheEnabled(true);
            v.buildDrawingCache();
            bmp = Bitmap.createBitmap(v.getDrawingCache());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bmp;
    }

    /**
     * 获取view的完整视图图片（即使没有显示出来的部分）
     */
    public static Bitmap convertBitmap(View view){//view.getWidth()view.getHeight()
        return convertViewToBitmap(view, view.getMeasuredWidth(), view.getMeasuredHeight());
    }

    /**
     * 通过计算的方法宽高后，获取view的完整视图图片（即使没有显示出来的部分）
     */
    public static Bitmap convertMeasureBitmap(View view){
        view.measure(View.MeasureSpec.makeMeasureSpec(view.getWidth(), View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(view.getHeight(), View.MeasureSpec.AT_MOST));
        return convertViewToBitmap(view, view.getMeasuredWidth(), view.getMeasuredHeight());
    }

    /**
     * convert view to bitmap according to with and height
     */
    public static Bitmap convertViewToBitmap(View view, int width, int height){
        Bitmap bitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        view.draw(new Canvas(bitmap));
        return bitmap;
    }

    public static Drawable bitmapToDrawable(Bitmap bitmap) {
        return new BitmapDrawable(bitmap);
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        // 取 drawable 的长宽
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();
        return drawableToBitmap(drawable, w, h);
    }

    public static Bitmap drawableToBitmap(Drawable drawable, int width, int height) {
        // 取 drawable 的颜色格式
        Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Config.ARGB_8888
                : Config.RGB_565;
        // 建立对应 bitmap
        Bitmap bitmap = Bitmap.createBitmap(width, height, config);
        // 建立对应 bitmap 的画布
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, width, height);
        // 把 drawable 内容画到画布中
        drawable.draw(canvas);
        return bitmap;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static Bitmap decodeMutableBitmapFromResourceId(final Context context, final int bitmapResId) {
        final BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            bitmapOptions.inMutable = true;
        }
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), bitmapResId, bitmapOptions);
        if (!bitmap.isMutable()) {
            bitmap = convertToMutable(context, bitmap);
        }
        return bitmap;
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public static Bitmap convertToMutable(final Context context, final Bitmap imgIn) {
        final int width = imgIn.getWidth(), height = imgIn.getHeight();
        final Config type = imgIn.getConfig();
        File outputFile = null;
        final File outputDir = context.getCacheDir();
        try {
            outputFile = File.createTempFile(Long.toString(System.currentTimeMillis()), null, outputDir);
            outputFile.deleteOnExit();
            final RandomAccessFile randomAccessFile = new RandomAccessFile(outputFile, "rw");
            final FileChannel channel = randomAccessFile.getChannel();
            final MappedByteBuffer map = channel.map(FileChannel.MapMode.READ_WRITE, 0, imgIn.getRowBytes() * height);
            imgIn.copyPixelsToBuffer(map);
            imgIn.recycle();
            final Bitmap result = Bitmap.createBitmap(width, height, type);
            map.position(0);
            result.copyPixelsFromBuffer(map);
            channel.close();
            randomAccessFile.close();
            outputFile.delete();
            return result;
        } catch (final Exception e) {
        } finally {
            if (outputFile != null)
                outputFile.delete();
        }
        return null;
    }

}
