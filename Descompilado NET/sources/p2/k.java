package p2;

import K2.o;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Locale;

/* JADX INFO: loaded from: classes.dex */
public abstract class k {
    public static final int a(Integer num) {
        D2.h.c(num);
        return F2.a.c(num.intValue() * Resources.getSystem().getDisplayMetrics().density);
    }

    public static final boolean b(Object obj) {
        return obj != null && ((obj instanceof Integer) || (obj instanceof Long) || (obj instanceof Float) || (obj instanceof Double));
    }

    public static final Bitmap c(String str) {
        D2.h.f(str, "imageUri");
        Bitmap bitmapDecodeStream = null;
        try {
            URLConnection uRLConnectionOpenConnection = new URL(str).openConnection();
            uRLConnectionOpenConnection.connect();
            InputStream inputStream = uRLConnectionOpenConnection.getInputStream();
            bitmapDecodeStream = BitmapFactory.decodeStream(inputStream);
            inputStream.close();
            return bitmapDecodeStream;
        } catch (IOException e4) {
            e4.printStackTrace();
            return bitmapDecodeStream;
        }
    }

    public static final WritableMap d(ReadableMap readableMap, ReadableMap readableMap2) {
        D2.h.f(readableMap, "map1");
        D2.h.f(readableMap2, "map2");
        WritableMap writableMapCreateMap = Arguments.createMap();
        D2.h.e(writableMapCreateMap, "createMap(...)");
        writableMapCreateMap.merge(readableMap);
        writableMapCreateMap.merge(readableMap2);
        return writableMapCreateMap;
    }

    public static final int e(String str) {
        Integer num;
        K2.g gVar;
        String strA;
        Integer numJ;
        String strA2;
        Integer numJ2;
        String strA3;
        Integer numJ3;
        K2.g gVar2;
        String strA4;
        Float fI;
        D2.h.f(str, "colorString");
        if (o.z(str, "#", false, 2, null)) {
            return Color.parseColor(str);
        }
        try {
            String upperCase = str.toUpperCase(Locale.ROOT);
            D2.h.e(upperCase, "toUpperCase(...)");
            Object obj = Color.class.getField(upperCase).get(null);
            D2.h.d(obj, "null cannot be cast to non-null type kotlin.Int");
            num = (Integer) obj;
        } catch (Exception unused) {
            num = null;
        }
        if (num != null) {
            return num.intValue();
        }
        K2.i iVarA = new K2.k("rgba?\\((\\d{1,3}), (\\d{1,3}), (\\d{1,3})(, (\\d(\\.\\d)?))?\\)").a(str);
        if (iVarA != null && (gVar = iVarA.a().get(1)) != null && (strA = gVar.a()) != null && (numJ = o.j(strA)) != null) {
            int iIntValue = numJ.intValue();
            K2.g gVar3 = iVarA.a().get(2);
            if (gVar3 != null && (strA2 = gVar3.a()) != null && (numJ2 = o.j(strA2)) != null) {
                int iIntValue2 = numJ2.intValue();
                K2.g gVar4 = iVarA.a().get(3);
                if (gVar4 != null && (strA3 = gVar4.a()) != null && (numJ3 = o.j(strA3)) != null) {
                    int iIntValue3 = numJ3.intValue();
                    float fFloatValue = 1.0f;
                    if (!o.z(str, "rgb(", false, 2, null) && (gVar2 = iVarA.a().get(5)) != null && (strA4 = gVar2.a()) != null && (fI = o.i(strA4)) != null) {
                        fFloatValue = fI.floatValue();
                    }
                    return Color.argb((int) (fFloatValue * 255), iIntValue, iIntValue2, iIntValue3);
                }
            }
        }
        return -16777216;
    }
}
