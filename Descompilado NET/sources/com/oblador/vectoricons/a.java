package com.oblador.vectoricons;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import com.facebook.react.views.text.d;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class a {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private static final Map f8418a = new HashMap();

    public static String a(String str, String str2, Integer num, Integer num2, Context context) throws Throwable {
        FileOutputStream fileOutputStream;
        Throwable th;
        String str3 = context.getCacheDir().getAbsolutePath() + "/";
        float f3 = context.getResources().getDisplayMetrics().density;
        StringBuilder sb = new StringBuilder();
        sb.append("@");
        int i3 = (int) f3;
        sb.append(f3 == ((float) i3) ? Integer.toString(i3) : Float.toString(f3));
        sb.append("x");
        String string = sb.toString();
        int iRound = Math.round(num.intValue() * f3);
        String str4 = str3 + Integer.toString((str + ":" + str2 + ":" + num2).hashCode(), 32) + "_" + Integer.toString(num.intValue()) + string + ".png";
        String str5 = "file://" + str4;
        File file = new File(str4);
        if (file.exists()) {
            return str5;
        }
        Typeface typefaceD = d.c().d(str, 0, context.getAssets());
        Paint paint = new Paint();
        paint.setTypeface(typefaceD);
        paint.setColor(num2.intValue());
        paint.setTextSize(iRound);
        paint.setAntiAlias(true);
        paint.getTextBounds(str2, 0, str2.length(), new Rect());
        int i4 = iRound - ((int) paint.getFontMetrics().bottom);
        Bitmap bitmapCreateBitmap = Bitmap.createBitmap(iRound, iRound, Bitmap.Config.ARGB_8888);
        new Canvas(bitmapCreateBitmap).drawText(str2, 0, i4, paint);
        try {
            fileOutputStream = new FileOutputStream(file);
            try {
                bitmapCreateBitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
                fileOutputStream.flush();
                fileOutputStream.close();
                return str5;
            } catch (Throwable th2) {
                th = th2;
                if (fileOutputStream != null) {
                    try {
                        fileOutputStream.close();
                    } catch (IOException e4) {
                        e4.printStackTrace();
                    }
                }
                throw th;
            }
        } catch (Throwable th3) {
            fileOutputStream = null;
            th = th3;
        }
    }
}
