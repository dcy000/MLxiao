package com.ml.zxing;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.annotation.ColorInt;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by afirez on 18-1-9.
 */

public class QrCodeUtils {
    public static Bitmap encodeWifiQrCode(String ssid, String password, int width, int height) {
        String content = String.format("WIFI:T:PWA;S:%s;P:%s", ssid, password);
        return encodeQrCode(content, width, height);
    }

    public static String urlEncode(String text) {
        try {
            return URLEncoder.encode(text, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return text;
    }

    public static Bitmap encodeQrCode(String content, int width, int height) {
        return encodeQrCode(
                content,
                width,
                height,
                "UTF-8",
                "H",
                "2",
                Color.BLACK,
                Color.WHITE
        );
    }

    public static Bitmap encodeQrCode(
            String content,
            int width,
            int height,
            String charset,
            String errorCorrection,
            String margin,
            @ColorInt int fgColor,
            @ColorInt int bgColor) {
        if (TextUtils.isEmpty(content)) {
            return null;
        }
        if (width < 0 || height < 0) {
            return null;
        }
        try {
            HashMap<EncodeHintType, String> hints = new HashMap<>();
            if (!TextUtils.isEmpty(charset)) {
                hints.put(EncodeHintType.CHARACTER_SET, charset);
            }
            if (!TextUtils.isEmpty(errorCorrection)) {
                hints.put(EncodeHintType.ERROR_CORRECTION, errorCorrection);
            }
            if (!TextUtils.isEmpty(margin)) {
                hints.put(EncodeHintType.MARGIN, margin);
            }
            BitMatrix matrix = new QRCodeWriter().encode(
                    content,
                    BarcodeFormat.QR_CODE,
                    width,
                    height,
                    hints
            );
            int[] pixels = new int[width * height];
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    if (matrix.get(x, y)) {
                        pixels[y * width + x] = fgColor;
                    } else {
                        pixels[y * width + x] = bgColor;
                    }
                }
            }
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
            return bitmap;
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static final Map<DecodeHintType, Object> HINTS = new EnumMap<>(DecodeHintType.class);
    @SuppressLint("StaticFieldLeak")
    public static AsyncTask decodeQRCode(final Context context, final Bitmap bitmap) {
        AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                try {
                    int width = bitmap.getWidth();
                    int height = bitmap.getHeight();
                    int[] pixels = new int[width * height];
                    bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
                    RGBLuminanceSource source = new RGBLuminanceSource(width, height, pixels);
                    Result result = new MultiFormatReader().decode(new BinaryBitmap(new HybridBinarizer(source)), HINTS);
                    return result.getText();
                } catch (Exception e) {
                    return null;
                }
            }

            @Override
            protected void onPostExecute(String result) {
                Log.d("wxl", "result=" + result);
                if (context == null) {
                    return;
                }
                Toast.makeText(context, result, Toast.LENGTH_LONG).show();
            }
        };
        task.execute();
        return task;
    }
}
