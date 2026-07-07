package com.facebook.react.views.text;

import android.content.Context;
import android.graphics.Rect;
import android.text.Layout;
import android.text.TextPaint;
import android.util.DisplayMetrics;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;

/* JADX INFO: loaded from: classes.dex */
public final class b {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static final b f7918a = new b();

    private b() {
    }

    public static final WritableArray a(CharSequence charSequence, Layout layout, TextPaint textPaint, Context context) {
        D2.h.f(charSequence, "text");
        D2.h.f(layout, "layout");
        D2.h.f(textPaint, "paint");
        D2.h.f(context, "context");
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        WritableArray writableArrayCreateArray = Arguments.createArray();
        TextPaint textPaint2 = new TextPaint(textPaint);
        textPaint2.setTextSize(textPaint2.getTextSize() * 100.0f);
        int i3 = 0;
        int i4 = 1;
        textPaint2.getTextBounds("T", 0, 1, new Rect());
        float fHeight = (r2.height() / 100.0f) / displayMetrics.density;
        textPaint2.getTextBounds("x", 0, 1, new Rect());
        float fHeight2 = (r8.height() / 100.0f) / displayMetrics.density;
        int lineCount = layout.getLineCount();
        while (i3 < lineCount) {
            float lineWidth = (charSequence.length() <= 0 || charSequence.charAt(layout.getLineEnd(i3) - i4) != '\n') ? layout.getLineWidth(i3) : layout.getLineMax(i3);
            layout.getLineBounds(i3, new Rect());
            WritableMap writableMapCreateMap = Arguments.createMap();
            writableMapCreateMap.putDouble("x", layout.getLineLeft(i3) / displayMetrics.density);
            writableMapCreateMap.putDouble("y", r12.top / displayMetrics.density);
            writableMapCreateMap.putDouble("width", lineWidth / displayMetrics.density);
            writableMapCreateMap.putDouble("height", r12.height() / displayMetrics.density);
            writableMapCreateMap.putDouble("descender", layout.getLineDescent(i3) / displayMetrics.density);
            writableMapCreateMap.putDouble("ascender", (-layout.getLineAscent(i3)) / displayMetrics.density);
            writableMapCreateMap.putDouble("baseline", layout.getLineBaseline(i3) / displayMetrics.density);
            writableMapCreateMap.putDouble("capHeight", fHeight);
            writableMapCreateMap.putDouble("xHeight", fHeight2);
            writableMapCreateMap.putString("text", charSequence.subSequence(layout.getLineStart(i3), layout.getLineEnd(i3)).toString());
            writableArrayCreateArray.pushMap(writableMapCreateMap);
            i3++;
            i4 = 1;
        }
        D2.h.c(writableArrayCreateArray);
        return writableArrayCreateArray;
    }
}
