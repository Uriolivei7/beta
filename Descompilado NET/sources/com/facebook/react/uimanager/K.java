package com.facebook.react.uimanager;

import android.graphics.BlendMode;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.RenderEffect;
import android.graphics.Shader;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public final class K {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static final K f7255a = new K();

    private K() {
    }

    private final ColorMatrix b(float f3) {
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setScale(f3, f3, f3, 1.0f);
        return colorMatrix;
    }

    private final RenderEffect d(ColorMatrix colorMatrix, RenderEffect renderEffect) {
        if (renderEffect == null) {
            RenderEffect renderEffectCreateColorFilterEffect = RenderEffect.createColorFilterEffect(new ColorMatrixColorFilter(colorMatrix));
            D2.h.c(renderEffectCreateColorFilterEffect);
            return renderEffectCreateColorFilterEffect;
        }
        RenderEffect renderEffectCreateColorFilterEffect2 = RenderEffect.createColorFilterEffect(new ColorMatrixColorFilter(colorMatrix), renderEffect);
        D2.h.c(renderEffectCreateColorFilterEffect2);
        return renderEffectCreateColorFilterEffect2;
    }

    private final ColorMatrix e(float f3) {
        float f4 = 255 * ((-(f3 / 2.0f)) + 0.5f);
        return new ColorMatrix(new float[]{f3, 0.0f, 0.0f, 0.0f, f4, 0.0f, f3, 0.0f, 0.0f, f4, 0.0f, 0.0f, f3, 0.0f, f4, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f});
    }

    private final ColorMatrix h(float f3) {
        float f4 = 1 - f3;
        float f5 = 0.7152f - (f4 * 0.7152f);
        float f6 = 0.0722f - (f4 * 0.0722f);
        float f7 = 0.2126f - (f4 * 0.2126f);
        return new ColorMatrix(new float[]{(0.7874f * f4) + 0.2126f, f5, f6, 0.0f, 0.0f, f7, (0.2848f * f4) + 0.7152f, f6, 0.0f, 0.0f, f7, f5, (f4 * 0.9278f) + 0.0722f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f});
    }

    private final ColorMatrix j(float f3) {
        double radians = Math.toRadians(f3);
        float fCos = (float) Math.cos(radians);
        float fSin = (float) Math.sin(radians);
        float f4 = 0.715f - (fCos * 0.715f);
        float f5 = fSin * 0.715f;
        float f6 = 0.072f - (fCos * 0.072f);
        float f7 = 0.213f - (fCos * 0.213f);
        return new ColorMatrix(new float[]{((fCos * 0.787f) + 0.213f) - (fSin * 0.213f), f4 - f5, (fSin * 0.928f) + f6, 0.0f, 0.0f, (0.143f * fSin) + f7, (0.285f * fCos) + 0.715f + (0.14f * fSin), f6 - (0.283f * fSin), 0.0f, 0.0f, f7 - (0.787f * fSin), f4 + f5, (fCos * 0.928f) + 0.072f + (fSin * 0.072f), 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f});
    }

    private final ColorMatrix l(float f3) {
        float f4 = 1 - (2 * f3);
        float f5 = f3 * 255;
        return new ColorMatrix(new float[]{f4, 0.0f, 0.0f, 0.0f, f5, 0.0f, f4, 0.0f, 0.0f, f5, 0.0f, 0.0f, f4, 0.0f, f5, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f});
    }

    private final ColorMatrix p(float f3) {
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setSaturation(f3);
        return colorMatrix;
    }

    private final ColorMatrix r(float f3) {
        float f4 = 1 - f3;
        return new ColorMatrix(new float[]{(0.607f * f4) + 0.393f, 0.769f - (f4 * 0.769f), 0.189f - (f4 * 0.189f), 0.0f, 0.0f, 0.349f - (f4 * 0.349f), (0.314f * f4) + 0.686f, 0.168f - (f4 * 0.168f), 0.0f, 0.0f, 0.272f - (f4 * 0.272f), 0.534f - (f4 * 0.534f), (f4 * 0.869f) + 0.131f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f});
    }

    public static final boolean t(ReadableArray readableArray) {
        if (readableArray == null || readableArray.size() == 0) {
            return false;
        }
        int size = readableArray.size();
        for (int i3 = 0; i3 < size; i3++) {
            ReadableMap map = readableArray.getMap(i3);
            D2.h.c(map);
            String key = map.getEntryIterator().next().getKey();
            if (D2.h.b(key, "blur") || D2.h.b(key, "dropShadow")) {
                return false;
            }
        }
        return true;
    }

    /* JADX WARN: Failed to restore switch over string. Please report as a decompilation issue */
    public static final ColorMatrixColorFilter v(ReadableArray readableArray) {
        ColorMatrix colorMatrixP;
        if (readableArray == null) {
            return null;
        }
        ColorMatrix colorMatrix = new ColorMatrix();
        int size = readableArray.size();
        for (int i3 = 0; i3 < size; i3++) {
            ReadableMap map = readableArray.getMap(i3);
            if (map == null) {
                throw new IllegalStateException("Required value was null.");
            }
            Map.Entry<String, Object> next = map.getEntryIterator().next();
            String key = next.getKey();
            Object value = next.getValue();
            D2.h.d(value, "null cannot be cast to non-null type kotlin.Double");
            float fDoubleValue = (float) ((Double) value).doubleValue();
            switch (key.hashCode()) {
                case -2114203985:
                    if (!key.equals("saturate")) {
                        throw new IllegalArgumentException("Invalid color matrix filter: " + key);
                    }
                    colorMatrixP = f7255a.p(fDoubleValue);
                    colorMatrix.preConcat(colorMatrixP);
                    break;
                    break;
                case -1267206133:
                    if (!key.equals("opacity")) {
                        throw new IllegalArgumentException("Invalid color matrix filter: " + key);
                    }
                    colorMatrixP = f7255a.n(fDoubleValue);
                    colorMatrix.preConcat(colorMatrixP);
                    break;
                    break;
                case -1183703082:
                    if (!key.equals("invert")) {
                        throw new IllegalArgumentException("Invalid color matrix filter: " + key);
                    }
                    colorMatrixP = f7255a.l(fDoubleValue);
                    colorMatrix.preConcat(colorMatrixP);
                    break;
                    break;
                case -905411385:
                    if (!key.equals("grayscale")) {
                        throw new IllegalArgumentException("Invalid color matrix filter: " + key);
                    }
                    colorMatrixP = f7255a.h(fDoubleValue);
                    colorMatrix.preConcat(colorMatrixP);
                    break;
                    break;
                case -566947070:
                    if (!key.equals("contrast")) {
                        throw new IllegalArgumentException("Invalid color matrix filter: " + key);
                    }
                    colorMatrixP = f7255a.e(fDoubleValue);
                    colorMatrix.preConcat(colorMatrixP);
                    break;
                    break;
                case 109324790:
                    if (!key.equals("sepia")) {
                        throw new IllegalArgumentException("Invalid color matrix filter: " + key);
                    }
                    colorMatrixP = f7255a.r(fDoubleValue);
                    colorMatrix.preConcat(colorMatrixP);
                    break;
                    break;
                case 648162385:
                    if (!key.equals("brightness")) {
                        throw new IllegalArgumentException("Invalid color matrix filter: " + key);
                    }
                    colorMatrixP = f7255a.b(fDoubleValue);
                    colorMatrix.preConcat(colorMatrixP);
                    break;
                    break;
                case 650888307:
                    if (!key.equals("hueRotate")) {
                        throw new IllegalArgumentException("Invalid color matrix filter: " + key);
                    }
                    colorMatrixP = f7255a.j(fDoubleValue);
                    colorMatrix.preConcat(colorMatrixP);
                    break;
                    break;
                default:
                    throw new IllegalArgumentException("Invalid color matrix filter: " + key);
            }
        }
        return new ColorMatrixColorFilter(colorMatrix);
    }

    /* JADX WARN: Failed to restore switch over string. Please report as a decompilation issue */
    public static final RenderEffect w(ReadableArray readableArray) {
        RenderEffect renderEffectQ = null;
        if (readableArray == null) {
            return null;
        }
        int size = readableArray.size();
        for (int i3 = 0; i3 < size; i3++) {
            ReadableMap map = readableArray.getMap(i3);
            if (map == null) {
                throw new IllegalStateException("Required value was null.");
            }
            Map.Entry<String, Object> next = map.getEntryIterator().next();
            String key = next.getKey();
            switch (key.hashCode()) {
                case -2114203985:
                    if (!key.equals("saturate")) {
                        throw new IllegalArgumentException("Invalid filter name: " + key);
                    }
                    K k3 = f7255a;
                    Object value = next.getValue();
                    D2.h.d(value, "null cannot be cast to non-null type kotlin.Double");
                    renderEffectQ = k3.q((float) ((Double) value).doubleValue(), renderEffectQ);
                    break;
                    break;
                case -1267206133:
                    if (!key.equals("opacity")) {
                        throw new IllegalArgumentException("Invalid filter name: " + key);
                    }
                    K k4 = f7255a;
                    Object value2 = next.getValue();
                    D2.h.d(value2, "null cannot be cast to non-null type kotlin.Double");
                    renderEffectQ = k4.o((float) ((Double) value2).doubleValue(), renderEffectQ);
                    break;
                    break;
                case -1183703082:
                    if (!key.equals("invert")) {
                        throw new IllegalArgumentException("Invalid filter name: " + key);
                    }
                    K k5 = f7255a;
                    Object value3 = next.getValue();
                    D2.h.d(value3, "null cannot be cast to non-null type kotlin.Double");
                    renderEffectQ = k5.m((float) ((Double) value3).doubleValue(), renderEffectQ);
                    break;
                    break;
                case -905411385:
                    if (!key.equals("grayscale")) {
                        throw new IllegalArgumentException("Invalid filter name: " + key);
                    }
                    K k6 = f7255a;
                    Object value4 = next.getValue();
                    D2.h.d(value4, "null cannot be cast to non-null type kotlin.Double");
                    renderEffectQ = k6.i((float) ((Double) value4).doubleValue(), renderEffectQ);
                    break;
                    break;
                case -566947070:
                    if (!key.equals("contrast")) {
                        throw new IllegalArgumentException("Invalid filter name: " + key);
                    }
                    K k7 = f7255a;
                    Object value5 = next.getValue();
                    D2.h.d(value5, "null cannot be cast to non-null type kotlin.Double");
                    renderEffectQ = k7.f((float) ((Double) value5).doubleValue(), renderEffectQ);
                    break;
                    break;
                case 3027047:
                    if (!key.equals("blur")) {
                        throw new IllegalArgumentException("Invalid filter name: " + key);
                    }
                    K k8 = f7255a;
                    Object value6 = next.getValue();
                    D2.h.d(value6, "null cannot be cast to non-null type kotlin.Double");
                    renderEffectQ = k8.a((float) ((Double) value6).doubleValue(), renderEffectQ);
                    break;
                    break;
                case 109324790:
                    if (!key.equals("sepia")) {
                        throw new IllegalArgumentException("Invalid filter name: " + key);
                    }
                    K k9 = f7255a;
                    Object value7 = next.getValue();
                    D2.h.d(value7, "null cannot be cast to non-null type kotlin.Double");
                    renderEffectQ = k9.s((float) ((Double) value7).doubleValue(), renderEffectQ);
                    break;
                    break;
                case 648162385:
                    if (!key.equals("brightness")) {
                        throw new IllegalArgumentException("Invalid filter name: " + key);
                    }
                    K k10 = f7255a;
                    Object value8 = next.getValue();
                    D2.h.d(value8, "null cannot be cast to non-null type kotlin.Double");
                    renderEffectQ = k10.c((float) ((Double) value8).doubleValue(), renderEffectQ);
                    break;
                    break;
                case 650888307:
                    if (!key.equals("hueRotate")) {
                        throw new IllegalArgumentException("Invalid filter name: " + key);
                    }
                    K k11 = f7255a;
                    Object value9 = next.getValue();
                    D2.h.d(value9, "null cannot be cast to non-null type kotlin.Double");
                    renderEffectQ = k11.k((float) ((Double) value9).doubleValue(), renderEffectQ);
                    break;
                    break;
                case 906978543:
                    if (!key.equals("dropShadow")) {
                        throw new IllegalArgumentException("Invalid filter name: " + key);
                    }
                    K k12 = f7255a;
                    Object value10 = next.getValue();
                    D2.h.d(value10, "null cannot be cast to non-null type com.facebook.react.bridge.ReadableMap");
                    renderEffectQ = k12.u((ReadableMap) value10, renderEffectQ);
                    break;
                    break;
                default:
                    throw new IllegalArgumentException("Invalid filter name: " + key);
            }
        }
        return renderEffectQ;
    }

    public final RenderEffect a(float f3, RenderEffect renderEffect) {
        if (f3 <= 0.5d) {
            return null;
        }
        float fX = x(f3);
        return renderEffect == null ? RenderEffect.createBlurEffect(fX, fX, Shader.TileMode.DECAL) : RenderEffect.createBlurEffect(fX, fX, renderEffect, Shader.TileMode.DECAL);
    }

    public final RenderEffect c(float f3, RenderEffect renderEffect) {
        return d(b(f3), renderEffect);
    }

    public final RenderEffect f(float f3, RenderEffect renderEffect) {
        return d(e(f3), renderEffect);
    }

    public final RenderEffect g(float f3, float f4, float f5, int i3, RenderEffect renderEffect) {
        RenderEffect renderEffectCreateOffsetEffect;
        RenderEffect renderEffectCreateOffsetEffect2;
        if (renderEffect == null) {
            renderEffectCreateOffsetEffect2 = RenderEffect.createOffsetEffect(0.0f, 0.0f);
            renderEffectCreateOffsetEffect = RenderEffect.createOffsetEffect(f3, f4);
        } else {
            RenderEffect renderEffectCreateOffsetEffect3 = RenderEffect.createOffsetEffect(0.0f, 0.0f, renderEffect);
            renderEffectCreateOffsetEffect = RenderEffect.createOffsetEffect(f3, f4, renderEffect);
            renderEffectCreateOffsetEffect2 = renderEffectCreateOffsetEffect3;
        }
        B.a();
        RenderEffect renderEffectCreateColorFilterEffect = RenderEffect.createColorFilterEffect(AbstractC0465y.a(i3, BlendMode.SRC_IN), renderEffectCreateOffsetEffect);
        D2.h.e(renderEffectCreateColorFilterEffect, "createColorFilterEffect(...)");
        RenderEffect renderEffectCreateBlurEffect = RenderEffect.createBlurEffect(f5, f5, renderEffectCreateColorFilterEffect, Shader.TileMode.DECAL);
        D2.h.e(renderEffectCreateBlurEffect, "createBlurEffect(...)");
        RenderEffect renderEffectCreateBlendModeEffect = RenderEffect.createBlendModeEffect(renderEffectCreateBlurEffect, renderEffectCreateOffsetEffect2, BlendMode.SRC_OVER);
        D2.h.e(renderEffectCreateBlendModeEffect, "createBlendModeEffect(...)");
        return renderEffectCreateBlendModeEffect;
    }

    public final RenderEffect i(float f3, RenderEffect renderEffect) {
        return d(h(f3), renderEffect);
    }

    public final RenderEffect k(float f3, RenderEffect renderEffect) {
        return d(j(f3), renderEffect);
    }

    public final RenderEffect m(float f3, RenderEffect renderEffect) {
        return d(l(f3), renderEffect);
    }

    public final ColorMatrix n(float f3) {
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setScale(1.0f, 1.0f, 1.0f, f3);
        return colorMatrix;
    }

    public final RenderEffect o(float f3, RenderEffect renderEffect) {
        return d(n(f3), renderEffect);
    }

    public final RenderEffect q(float f3, RenderEffect renderEffect) {
        return d(p(f3), renderEffect);
    }

    public final RenderEffect s(float f3, RenderEffect renderEffect) {
        return d(r(f3), renderEffect);
    }

    public final RenderEffect u(ReadableMap readableMap, RenderEffect renderEffect) {
        D2.h.f(readableMap, "filterValues");
        C0429f0 c0429f0 = C0429f0.f7476a;
        return g(c0429f0.a(readableMap.getDouble("offsetX")), c0429f0.a(readableMap.getDouble("offsetY")), readableMap.hasKey("standardDeviation") ? x((float) readableMap.getDouble("standardDeviation")) : 0.0f, readableMap.hasKey("color") ? readableMap.getInt("color") : -16777216, renderEffect);
    }

    public final float x(float f3) {
        float fH = C0429f0.h(f3);
        if (fH > 0.5f) {
            return (fH - 0.5f) / 0.57735f;
        }
        return 0.0f;
    }
}
