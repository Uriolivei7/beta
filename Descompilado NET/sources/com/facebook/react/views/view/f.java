package com.facebook.react.views.view;

import android.R;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.util.TypedValue;
import com.facebook.react.bridge.JSApplicationIllegalArgumentException;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.uimanager.C0429f0;

/* JADX INFO: loaded from: classes.dex */
public final class f {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static final f f8167a = new f();

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private static final TypedValue f8168b = new TypedValue();

    private f() {
    }

    public static final Drawable a(Context context, ReadableMap readableMap) {
        D2.h.f(context, "context");
        D2.h.f(readableMap, "drawableDescriptionDict");
        String string = readableMap.getString("type");
        if (!D2.h.b("ThemeAttrAndroid", string)) {
            if (D2.h.b("RippleAndroid", string)) {
                f fVar = f8167a;
                return fVar.g(readableMap, fVar.f(context, readableMap));
            }
            throw new JSApplicationIllegalArgumentException("Invalid type for android drawable: " + string);
        }
        String string2 = readableMap.getString("attribute");
        if (string2 == null) {
            throw new JSApplicationIllegalArgumentException("JS description missing 'attribute' field");
        }
        f fVar2 = f8167a;
        int iB = fVar2.b(context, string2);
        if (context.getTheme().resolveAttribute(iB, f8168b, true)) {
            return fVar2.g(readableMap, fVar2.d(context));
        }
        throw new JSApplicationIllegalArgumentException("Attribute " + string2 + " with id " + iB + " couldn't be resolved into a drawable");
    }

    private final int b(Context context, String str) {
        return D2.h.b("selectableItemBackground", str) ? R.attr.selectableItemBackground : D2.h.b("selectableItemBackgroundBorderless", str) ? R.attr.selectableItemBackgroundBorderless : context.getResources().getIdentifier(str, "attr", "android");
    }

    private final int c(Context context, ReadableMap readableMap) {
        if (readableMap.hasKey("color") && !readableMap.isNull("color")) {
            return readableMap.getInt("color");
        }
        Resources.Theme theme = context.getTheme();
        TypedValue typedValue = f8168b;
        if (theme.resolveAttribute(R.attr.colorControlHighlight, typedValue, true)) {
            return context.getResources().getColor(typedValue.resourceId, context.getTheme());
        }
        throw new JSApplicationIllegalArgumentException("Attribute colorControlHighlight couldn't be resolved into a drawable");
    }

    private final Drawable d(Context context) {
        return context.getResources().getDrawable(f8168b.resourceId, context.getTheme());
    }

    private final Drawable e(ReadableMap readableMap) {
        if (readableMap.hasKey("borderless") && !readableMap.isNull("borderless") && readableMap.getBoolean("borderless")) {
            return null;
        }
        return new ColorDrawable(-1);
    }

    private final RippleDrawable f(Context context, ReadableMap readableMap) {
        int iC = c(context, readableMap);
        return new RippleDrawable(new ColorStateList(new int[][]{new int[0]}, new int[]{iC}), null, e(readableMap));
    }

    private final Drawable g(ReadableMap readableMap, Drawable drawable) {
        if (readableMap.hasKey("rippleRadius") && (drawable instanceof RippleDrawable)) {
            ((RippleDrawable) drawable).setRadius((int) C0429f0.g(readableMap.getDouble("rippleRadius")));
        }
        return drawable;
    }
}
