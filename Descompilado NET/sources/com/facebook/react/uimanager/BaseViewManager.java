package com.facebook.react.uimanager;

import P1.o;
import android.graphics.Paint;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewParent;
import com.facebook.react.bridge.Dynamic;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableMapKeySetIterator;
import com.facebook.react.bridge.ReadableType;
import com.facebook.react.uimanager.C0433h0;
import com.facebook.react.uimanager.U;
import com.facebook.react.uimanager.Y;
import d1.AbstractC0505m;
import d1.AbstractC0508p;
import e1.C0527d;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import org.chromium.support_lib_boundary.WebSettingsBoundaryInterface;

/* JADX INFO: loaded from: classes.dex */
public abstract class BaseViewManager<T extends View, C extends U> extends ViewManager<T, C> implements View.OnLayoutChangeListener {
    private static final int PERSPECTIVE_ARRAY_INVERTED_CAMERA_DISTANCE_INDEX = 2;
    private static final String STATE_BUSY = "busy";
    private static final String STATE_CHECKED = "checked";
    private static final String STATE_EXPANDED = "expanded";
    private static final String STATE_MIXED = "mixed";
    private static final float CAMERA_DISTANCE_NORMALIZATION_MULTIPLIER = (float) Math.sqrt(5.0d);
    private static final Y.a sMatrixDecompositionContext = new Y.a();
    private static final double[] sTransformDecompositionArray = new double[16];

    private static class a {
        public static void a(View view, ReadableArray readableArray, Boolean bool) {
            Paint paint;
            int i3 = Build.VERSION.SDK_INT;
            if (i3 >= 31) {
                view.setRenderEffect(null);
            }
            if (readableArray == null) {
                paint = null;
            } else if (K.t(readableArray)) {
                paint = new Paint();
                paint.setColorFilter(K.v(readableArray));
            } else {
                if (i3 >= 31) {
                    view.setRenderEffect(K.w(readableArray));
                }
                paint = null;
            }
            if (paint == null) {
                view.setLayerType((bool == null || !bool.booleanValue()) ? 0 : 2, null);
            } else {
                view.setLayerType(2, paint);
            }
        }
    }

    public BaseViewManager() {
        super(null);
    }

    private void logUnsupportedPropertyWarning(String str) {
        Y.a.K("ReactNative", "%s doesn't support property '%s'", getName(), str);
    }

    private static float sanitizeFloatPropertyValue(float f3) {
        if (f3 >= -3.4028235E38f && f3 <= Float.MAX_VALUE) {
            return f3;
        }
        if (f3 < -3.4028235E38f || f3 == Float.NEGATIVE_INFINITY) {
            return -3.4028235E38f;
        }
        if (f3 > Float.MAX_VALUE || f3 == Float.POSITIVE_INFINITY) {
            return Float.MAX_VALUE;
        }
        if (Float.isNaN(f3)) {
            return 0.0f;
        }
        throw new IllegalStateException("Invalid float property value: " + f3);
    }

    private static void setPointerEventsFlag(View view, o.a aVar, boolean z3) {
        Integer num = (Integer) view.getTag(AbstractC0505m.f9246s);
        int iIntValue = num != null ? num.intValue() : 0;
        int iOrdinal = 1 << aVar.ordinal();
        view.setTag(AbstractC0505m.f9246s, Integer.valueOf(z3 ? iOrdinal | iIntValue : (~iOrdinal) & iIntValue));
    }

    private void updateViewContentDescription(T t3) {
        Dynamic dynamic;
        String str = (String) t3.getTag(AbstractC0505m.f9232e);
        ReadableMap readableMap = (ReadableMap) t3.getTag(AbstractC0505m.f9235h);
        ArrayList arrayList = new ArrayList();
        ReadableMap readableMap2 = (ReadableMap) t3.getTag(AbstractC0505m.f9237j);
        if (str != null) {
            arrayList.add(str);
        }
        if (readableMap != null) {
            ReadableMapKeySetIterator readableMapKeySetIteratorKeySetIterator = readableMap.keySetIterator();
            while (readableMapKeySetIteratorKeySetIterator.hasNextKey()) {
                String strNextKey = readableMapKeySetIteratorKeySetIterator.nextKey();
                Dynamic dynamic2 = readableMap.getDynamic(strNextKey);
                if (strNextKey.equals(STATE_CHECKED) && dynamic2.getType() == ReadableType.String && dynamic2.asString().equals(STATE_MIXED)) {
                    arrayList.add(t3.getContext().getString(AbstractC0508p.f9268G));
                } else if (strNextKey.equals(STATE_BUSY) && dynamic2.getType() == ReadableType.Boolean && dynamic2.asBoolean()) {
                    arrayList.add(t3.getContext().getString(AbstractC0508p.f9267F));
                }
            }
        }
        if (readableMap2 != null && readableMap2.hasKey("text") && (dynamic = readableMap2.getDynamic("text")) != null && dynamic.getType() == ReadableType.String) {
            arrayList.add(dynamic.asString());
        }
        if (arrayList.isEmpty()) {
            return;
        }
        t3.setContentDescription(TextUtils.join(", ", arrayList));
    }

    @Override // com.facebook.react.uimanager.ViewManager
    public Map<String, Object> getExportedCustomBubblingEventTypeConstants() {
        Map<String, Object> exportedCustomDirectEventTypeConstants = super.getExportedCustomDirectEventTypeConstants();
        if (exportedCustomDirectEventTypeConstants == null) {
            exportedCustomDirectEventTypeConstants = new HashMap<>();
        }
        C0527d.a aVarB = C0527d.a().b("topPointerCancel", C0527d.d("phasedRegistrationNames", C0527d.e("bubbled", "onPointerCancel", "captured", "onPointerCancelCapture"))).b("topPointerDown", C0527d.d("phasedRegistrationNames", C0527d.e("bubbled", "onPointerDown", "captured", "onPointerDownCapture")));
        Boolean bool = Boolean.TRUE;
        exportedCustomDirectEventTypeConstants.putAll(aVarB.b("topPointerEnter", C0527d.d("phasedRegistrationNames", C0527d.f("bubbled", "onPointerEnter", "captured", "onPointerEnterCapture", "skipBubbling", bool))).b("topPointerLeave", C0527d.d("phasedRegistrationNames", C0527d.f("bubbled", "onPointerLeave", "captured", "onPointerLeaveCapture", "skipBubbling", bool))).b("topPointerMove", C0527d.d("phasedRegistrationNames", C0527d.e("bubbled", "onPointerMove", "captured", "onPointerMoveCapture"))).b("topPointerUp", C0527d.d("phasedRegistrationNames", C0527d.e("bubbled", "onPointerUp", "captured", "onPointerUpCapture"))).b("topPointerOut", C0527d.d("phasedRegistrationNames", C0527d.e("bubbled", "onPointerOut", "captured", "onPointerOutCapture"))).b("topPointerOver", C0527d.d("phasedRegistrationNames", C0527d.e("bubbled", "onPointerOver", "captured", "onPointerOverCapture"))).b("topClick", C0527d.d("phasedRegistrationNames", C0527d.e("bubbled", "onClick", "captured", "onClickCapture"))).a());
        return exportedCustomDirectEventTypeConstants;
    }

    @Override // com.facebook.react.uimanager.ViewManager
    public Map<String, Object> getExportedCustomDirectEventTypeConstants() {
        Map<String, Object> exportedCustomDirectEventTypeConstants = super.getExportedCustomDirectEventTypeConstants();
        if (exportedCustomDirectEventTypeConstants == null) {
            exportedCustomDirectEventTypeConstants = new HashMap<>();
        }
        exportedCustomDirectEventTypeConstants.putAll(C0527d.a().b("topAccessibilityAction", C0527d.d("registrationName", "onAccessibilityAction")).a());
        return exportedCustomDirectEventTypeConstants;
    }

    @Override // com.facebook.react.uimanager.ViewManager
    protected void onAfterUpdateTransaction(T t3) {
        super.onAfterUpdateTransaction(t3);
        updateViewAccessibility(t3);
        Boolean bool = (Boolean) t3.getTag(AbstractC0505m.f9243p);
        if (bool != null && bool.booleanValue()) {
            t3.addOnLayoutChangeListener(this);
            setTransformProperty(t3, (ReadableArray) t3.getTag(AbstractC0505m.f9223A), (ReadableArray) t3.getTag(AbstractC0505m.f9224B));
            t3.setTag(AbstractC0505m.f9243p, Boolean.FALSE);
        }
        a.a(t3, (ReadableArray) t3.getTag(AbstractC0505m.f9241n), (Boolean) t3.getTag(AbstractC0505m.f9225C));
    }

    @Override // android.view.View.OnLayoutChangeListener
    public void onLayoutChange(View view, int i3, int i4, int i5, int i6, int i7, int i8, int i9, int i10) {
        int i11 = i9 - i7;
        int i12 = i5 - i3;
        if (i6 - i4 == i10 - i8 && i12 == i11) {
            return;
        }
        ReadableArray readableArray = (ReadableArray) view.getTag(AbstractC0505m.f9224B);
        ReadableArray readableArray2 = (ReadableArray) view.getTag(AbstractC0505m.f9223A);
        if (readableArray2 == null && readableArray == null) {
            return;
        }
        setTransformProperty(view, readableArray2, readableArray);
    }

    @Override // com.facebook.react.uimanager.ViewManager
    protected T prepareToRecycleView(B0 b02, T t3) {
        t3.setTag(null);
        t3.setTag(AbstractC0505m.f9246s, null);
        t3.setTag(AbstractC0505m.f9247t, null);
        t3.setTag(AbstractC0505m.f9227E, null);
        t3.setTag(AbstractC0505m.f9244q, null);
        t3.setTag(AbstractC0505m.f9232e, null);
        t3.setTag(AbstractC0505m.f9231d, null);
        t3.setTag(AbstractC0505m.f9234g, null);
        t3.setTag(AbstractC0505m.f9235h, null);
        t3.setTag(AbstractC0505m.f9228a, null);
        t3.setTag(AbstractC0505m.f9237j, null);
        t3.setTag(AbstractC0505m.f9236i, null);
        t3.setTag(AbstractC0505m.f9226D, null);
        setTransformProperty(t3, null, null);
        int i3 = Build.VERSION.SDK_INT;
        if (i3 < 28) {
            return null;
        }
        t3.resetPivot();
        t3.setTop(0);
        t3.setBottom(0);
        t3.setLeft(0);
        t3.setRight(0);
        t3.setElevation(0.0f);
        if (i3 >= 29) {
            t3.setAnimationMatrix(null);
        }
        t3.setTag(AbstractC0505m.f9223A, null);
        t3.setTag(AbstractC0505m.f9224B, null);
        t3.setTag(AbstractC0505m.f9243p, null);
        t3.removeOnLayoutChangeListener(this);
        t3.setTag(AbstractC0505m.f9225C, null);
        t3.setTag(AbstractC0505m.f9241n, null);
        t3.setTag(AbstractC0505m.f9245r, null);
        a.a(t3, null, null);
        if (i3 >= 28) {
            t3.setOutlineAmbientShadowColor(-16777216);
            t3.setOutlineSpotShadowColor(-16777216);
        }
        t3.setNextFocusDownId(-1);
        t3.setNextFocusForwardId(-1);
        t3.setNextFocusRightId(-1);
        t3.setNextFocusUpId(-1);
        t3.setFocusable(false);
        t3.setFocusableInTouchMode(false);
        t3.setElevation(0.0f);
        t3.setAlpha(1.0f);
        setPadding(t3, 0, 0, 0, 0);
        t3.setForeground(null);
        return t3;
    }

    @L1.a(name = "accessibilityActions")
    public void setAccessibilityActions(T t3, ReadableArray readableArray) {
        if (readableArray == null) {
            return;
        }
        t3.setTag(AbstractC0505m.f9228a, readableArray);
    }

    @L1.a(name = "accessibilityCollection")
    public void setAccessibilityCollection(T t3, ReadableMap readableMap) {
        t3.setTag(AbstractC0505m.f9229b, readableMap);
    }

    @L1.a(name = "accessibilityCollectionItem")
    public void setAccessibilityCollectionItem(T t3, ReadableMap readableMap) {
        t3.setTag(AbstractC0505m.f9230c, readableMap);
    }

    @L1.a(name = "accessibilityHint")
    public void setAccessibilityHint(T t3, String str) {
        t3.setTag(AbstractC0505m.f9231d, str);
        updateViewContentDescription(t3);
    }

    @L1.a(name = "accessibilityLabel")
    public void setAccessibilityLabel(T t3, String str) {
        t3.setTag(AbstractC0505m.f9232e, str);
        updateViewContentDescription(t3);
    }

    @L1.a(name = "accessibilityLabelledBy")
    public void setAccessibilityLabelledBy(T t3, Dynamic dynamic) {
        if (dynamic.isNull()) {
            return;
        }
        if (dynamic.getType() == ReadableType.String) {
            t3.setTag(AbstractC0505m.f9244q, dynamic.asString());
        } else if (dynamic.getType() == ReadableType.Array) {
            t3.setTag(AbstractC0505m.f9244q, dynamic.asArray().getString(0));
        }
    }

    @L1.a(name = "accessibilityLiveRegion")
    public void setAccessibilityLiveRegion(T t3, String str) {
        if (str == null || str.equals("none")) {
            androidx.core.view.Z.Z(t3, 0);
        } else if (str.equals("polite")) {
            androidx.core.view.Z.Z(t3, 1);
        } else if (str.equals("assertive")) {
            androidx.core.view.Z.Z(t3, 2);
        }
    }

    @L1.a(name = "accessibilityRole")
    public void setAccessibilityRole(T t3, String str) {
        if (str == null) {
            t3.setTag(AbstractC0505m.f9234g, null);
        } else {
            t3.setTag(AbstractC0505m.f9234g, C0433h0.d.c(str));
        }
    }

    @L1.a(name = "accessibilityValue")
    public void setAccessibilityValue(T t3, ReadableMap readableMap) {
        if (readableMap == null) {
            t3.setTag(AbstractC0505m.f9237j, null);
            t3.setContentDescription(null);
        } else {
            t3.setTag(AbstractC0505m.f9237j, readableMap);
            if (readableMap.hasKey("text")) {
                updateViewContentDescription(t3);
            }
        }
    }

    @L1.a(customType = "Color", defaultInt = WebSettingsBoundaryInterface.ForceDarkBehavior.FORCE_DARK_ONLY, name = "backgroundColor")
    public void setBackgroundColor(T t3, int i3) {
        C0418a.n(t3, Integer.valueOf(i3));
    }

    public void setBorderBottomLeftRadius(T t3, float f3) {
        logUnsupportedPropertyWarning("borderBottomLeftRadius");
    }

    public void setBorderBottomRightRadius(T t3, float f3) {
        logUnsupportedPropertyWarning("borderBottomRightRadius");
    }

    public void setBorderRadius(T t3, float f3) {
        logUnsupportedPropertyWarning("borderRadius");
    }

    public void setBorderTopLeftRadius(T t3, float f3) {
        logUnsupportedPropertyWarning("borderTopLeftRadius");
    }

    public void setBorderTopRightRadius(T t3, float f3) {
        logUnsupportedPropertyWarning("borderTopRightRadius");
    }

    @L1.a(customType = "BoxShadow", name = "boxShadow")
    public void setBoxShadow(T t3, ReadableArray readableArray) {
        C0418a.t(t3, readableArray);
    }

    @L1.a(name = "onClick")
    public void setClick(T t3, boolean z3) {
        setPointerEventsFlag(t3, o.a.f1672d, z3);
    }

    @L1.a(name = "onClickCapture")
    public void setClickCapture(T t3, boolean z3) {
        setPointerEventsFlag(t3, o.a.f1673e, z3);
    }

    @L1.a(name = "elevation")
    public void setElevation(T t3, float f3) {
        androidx.core.view.Z.e0(t3, C0429f0.h(f3));
    }

    @L1.a(customType = "Filter", name = "filter")
    public void setFilter(T t3, ReadableArray readableArray) {
        if (M1.a.c(t3) == 2) {
            t3.setTag(AbstractC0505m.f9241n, readableArray);
        }
    }

    @L1.a(name = "importantForAccessibility")
    public void setImportantForAccessibility(T t3, String str) {
        if (str == null || str.equals("auto")) {
            androidx.core.view.Z.f0(t3, 0);
            return;
        }
        if (str.equals("yes")) {
            androidx.core.view.Z.f0(t3, 1);
        } else if (str.equals("no")) {
            androidx.core.view.Z.f0(t3, 2);
        } else if (str.equals("no-hide-descendants")) {
            androidx.core.view.Z.f0(t3, 4);
        }
    }

    @L1.a(name = "mixBlendMode")
    public void setMixBlendMode(T t3, String str) {
        if (M1.a.c(t3) == 2) {
            t3.setTag(AbstractC0505m.f9245r, C0461w.b(str));
            if (t3.getParent() instanceof View) {
                ((View) t3.getParent()).invalidate();
            }
        }
    }

    @L1.a(name = "onMoveShouldSetResponder")
    public void setMoveShouldSetResponder(T t3, boolean z3) {
    }

    @L1.a(name = "onMoveShouldSetResponderCapture")
    public void setMoveShouldSetResponderCapture(T t3, boolean z3) {
    }

    @L1.a(name = "nativeID")
    public void setNativeId(T t3, String str) {
        t3.setTag(AbstractC0505m.f9227E, str);
        S1.a.c(t3);
    }

    @L1.a(defaultFloat = 1.0f, name = "opacity")
    public void setOpacity(T t3, float f3) {
        t3.setAlpha(f3);
    }

    @L1.a(customType = "Color", name = "outlineColor")
    public void setOutlineColor(T t3, Integer num) {
        C0418a.w(t3, num);
    }

    @L1.a(name = "outlineOffset")
    public void setOutlineOffset(T t3, float f3) {
        C0418a.x(t3, f3);
    }

    @L1.a(name = "outlineStyle")
    public void setOutlineStyle(T t3, String str) {
        C0418a.y(t3, str == null ? null : R1.o.b(str));
    }

    @L1.a(name = "outlineWidth")
    public void setOutlineWidth(T t3, float f3) {
        C0418a.z(t3, f3);
    }

    @L1.a(name = "onPointerEnter")
    public void setPointerEnter(T t3, boolean z3) {
        setPointerEventsFlag(t3, o.a.f1676h, z3);
    }

    @L1.a(name = "onPointerEnterCapture")
    public void setPointerEnterCapture(T t3, boolean z3) {
        setPointerEventsFlag(t3, o.a.f1677i, z3);
    }

    @L1.a(name = "onPointerLeave")
    public void setPointerLeave(T t3, boolean z3) {
        setPointerEventsFlag(t3, o.a.f1678j, z3);
    }

    @L1.a(name = "onPointerLeaveCapture")
    public void setPointerLeaveCapture(T t3, boolean z3) {
        setPointerEventsFlag(t3, o.a.f1679k, z3);
    }

    @L1.a(name = "onPointerMove")
    public void setPointerMove(T t3, boolean z3) {
        setPointerEventsFlag(t3, o.a.f1680l, z3);
    }

    @L1.a(name = "onPointerMoveCapture")
    public void setPointerMoveCapture(T t3, boolean z3) {
        setPointerEventsFlag(t3, o.a.f1681m, z3);
    }

    @L1.a(name = "onPointerOut")
    public void setPointerOut(T t3, boolean z3) {
        setPointerEventsFlag(t3, o.a.f1684p, z3);
    }

    @L1.a(name = "onPointerOutCapture")
    public void setPointerOutCapture(T t3, boolean z3) {
        setPointerEventsFlag(t3, o.a.f1685q, z3);
    }

    @L1.a(name = "onPointerOver")
    public void setPointerOver(T t3, boolean z3) {
        setPointerEventsFlag(t3, o.a.f1686r, z3);
    }

    @L1.a(name = "onPointerOverCapture")
    public void setPointerOverCapture(T t3, boolean z3) {
        setPointerEventsFlag(t3, o.a.f1687s, z3);
    }

    @L1.a(name = "renderToHardwareTextureAndroid")
    public void setRenderToHardwareTexture(T t3, boolean z3) {
        t3.setTag(AbstractC0505m.f9225C, Boolean.valueOf(z3));
    }

    @L1.a(name = "onResponderEnd")
    public void setResponderEnd(T t3, boolean z3) {
    }

    @L1.a(name = "onResponderGrant")
    public void setResponderGrant(T t3, boolean z3) {
    }

    @L1.a(name = "onResponderMove")
    public void setResponderMove(T t3, boolean z3) {
    }

    @L1.a(name = "onResponderReject")
    public void setResponderReject(T t3, boolean z3) {
    }

    @L1.a(name = "onResponderRelease")
    public void setResponderRelease(T t3, boolean z3) {
    }

    @L1.a(name = "onResponderStart")
    public void setResponderStart(T t3, boolean z3) {
    }

    @L1.a(name = "onResponderTerminate")
    public void setResponderTerminate(T t3, boolean z3) {
    }

    @L1.a(name = "onResponderTerminationRequest")
    public void setResponderTerminationRequest(T t3, boolean z3) {
    }

    @L1.a(name = "role")
    public void setRole(T t3, String str) {
        if (str == null) {
            t3.setTag(AbstractC0505m.f9253z, null);
        } else {
            t3.setTag(AbstractC0505m.f9253z, C0433h0.e.b(str));
        }
    }

    @L1.a(name = "rotation")
    @Deprecated
    public void setRotation(T t3, float f3) {
        t3.setRotation(f3);
    }

    @L1.a(defaultFloat = 1.0f, name = "scaleX")
    @Deprecated
    public void setScaleX(T t3, float f3) {
        t3.setScaleX(f3);
    }

    @L1.a(defaultFloat = 1.0f, name = "scaleY")
    @Deprecated
    public void setScaleY(T t3, float f3) {
        t3.setScaleY(f3);
    }

    @L1.a(customType = "Color", defaultInt = -16777216, name = "shadowColor")
    public void setShadowColor(T t3, int i3) {
        if (Build.VERSION.SDK_INT >= 28) {
            t3.setOutlineAmbientShadowColor(i3);
            t3.setOutlineSpotShadowColor(i3);
        }
    }

    @L1.a(name = "onShouldBlockNativeResponder")
    public void setShouldBlockNativeResponder(T t3, boolean z3) {
    }

    @L1.a(name = "onStartShouldSetResponder")
    public void setStartShouldSetResponder(T t3, boolean z3) {
    }

    @L1.a(name = "onStartShouldSetResponderCapture")
    public void setStartShouldSetResponderCapture(T t3, boolean z3) {
    }

    @L1.a(name = "testID")
    public void setTestId(T t3, String str) {
        t3.setTag(AbstractC0505m.f9247t, str);
        t3.setTag(str);
    }

    @L1.a(name = "onTouchCancel")
    public void setTouchCancel(T t3, boolean z3) {
    }

    @L1.a(name = "onTouchEnd")
    public void setTouchEnd(T t3, boolean z3) {
    }

    @L1.a(name = "onTouchMove")
    public void setTouchMove(T t3, boolean z3) {
    }

    @L1.a(name = "onTouchStart")
    public void setTouchStart(T t3, boolean z3) {
    }

    @L1.a(name = "transform")
    public void setTransform(T t3, ReadableArray readableArray) {
        if (Objects.equals((ReadableArray) t3.getTag(AbstractC0505m.f9223A), readableArray)) {
            return;
        }
        t3.setTag(AbstractC0505m.f9223A, readableArray);
        t3.setTag(AbstractC0505m.f9243p, Boolean.TRUE);
    }

    @L1.a(name = "transformOrigin")
    public void setTransformOrigin(T t3, ReadableArray readableArray) {
        if (Objects.equals((ReadableArray) t3.getTag(AbstractC0505m.f9224B), readableArray)) {
            return;
        }
        t3.setTag(AbstractC0505m.f9224B, readableArray);
        t3.setTag(AbstractC0505m.f9243p, Boolean.TRUE);
    }

    protected void setTransformProperty(T t3, ReadableArray readableArray, ReadableArray readableArray2) {
        if (readableArray == null) {
            t3.setTranslationX(C0429f0.h(0.0f));
            t3.setTranslationY(C0429f0.h(0.0f));
            t3.setRotation(0.0f);
            t3.setRotationX(0.0f);
            t3.setRotationY(0.0f);
            t3.setScaleX(1.0f);
            t3.setScaleY(1.0f);
            t3.setCameraDistance(0.0f);
            return;
        }
        boolean z3 = M1.a.c(t3) == 2;
        Y.a aVar = sMatrixDecompositionContext;
        aVar.a();
        double[] dArr = sTransformDecompositionArray;
        E0.d(readableArray, dArr, C0429f0.f(t3.getWidth()), C0429f0.f(t3.getHeight()), readableArray2, z3);
        Y.k(dArr, aVar);
        t3.setTranslationX(C0429f0.h(sanitizeFloatPropertyValue((float) aVar.f7433d[0])));
        t3.setTranslationY(C0429f0.h(sanitizeFloatPropertyValue((float) aVar.f7433d[1])));
        t3.setRotation(sanitizeFloatPropertyValue((float) aVar.f7434e[2]));
        t3.setRotationX(sanitizeFloatPropertyValue((float) aVar.f7434e[0]));
        t3.setRotationY(sanitizeFloatPropertyValue((float) aVar.f7434e[1]));
        t3.setScaleX(sanitizeFloatPropertyValue((float) aVar.f7431b[0]));
        t3.setScaleY(sanitizeFloatPropertyValue((float) aVar.f7431b[1]));
        double[] dArr2 = aVar.f7430a;
        if (dArr2.length > 2) {
            float f3 = (float) dArr2[2];
            if (f3 == 0.0f) {
                f3 = 7.8125E-4f;
            }
            float f4 = (-1.0f) / f3;
            float f5 = C0463x.c().density;
            t3.setCameraDistance(sanitizeFloatPropertyValue(f5 * f5 * f4 * CAMERA_DISTANCE_NORMALIZATION_MULTIPLIER));
        }
    }

    @L1.a(defaultFloat = 0.0f, name = "translateX")
    @Deprecated
    public void setTranslateX(T t3, float f3) {
        t3.setTranslationX(C0429f0.h(f3));
    }

    @L1.a(defaultFloat = 0.0f, name = "translateY")
    @Deprecated
    public void setTranslateY(T t3, float f3) {
        t3.setTranslationY(C0429f0.h(f3));
    }

    @L1.a(name = "accessibilityState")
    public void setViewState(T t3, ReadableMap readableMap) {
        if (readableMap == null) {
            return;
        }
        if (readableMap.hasKey(STATE_EXPANDED)) {
            t3.setTag(AbstractC0505m.f9236i, Boolean.valueOf(readableMap.getBoolean(STATE_EXPANDED)));
        }
        if (readableMap.hasKey("selected")) {
            boolean zIsSelected = t3.isSelected();
            boolean z3 = readableMap.getBoolean("selected");
            t3.setSelected(z3);
            if (t3.isAccessibilityFocused() && zIsSelected && !z3) {
                t3.announceForAccessibility(t3.getContext().getString(AbstractC0508p.f9269H));
            }
        } else {
            t3.setSelected(false);
        }
        t3.setTag(AbstractC0505m.f9235h, readableMap);
        if (readableMap.hasKey("disabled") && !readableMap.getBoolean("disabled")) {
            t3.setEnabled(true);
        }
        ReadableMapKeySetIterator readableMapKeySetIteratorKeySetIterator = readableMap.keySetIterator();
        while (readableMapKeySetIteratorKeySetIterator.hasNextKey()) {
            String strNextKey = readableMapKeySetIteratorKeySetIterator.nextKey();
            if (strNextKey.equals(STATE_BUSY) || strNextKey.equals(STATE_EXPANDED) || (strNextKey.equals(STATE_CHECKED) && readableMap.getType(STATE_CHECKED) == ReadableType.String)) {
                updateViewContentDescription(t3);
                return;
            } else if (t3.isAccessibilityFocused()) {
                t3.sendAccessibilityEvent(1);
            }
        }
    }

    @L1.a(name = "zIndex")
    public void setZIndex(T t3, float f3) {
        ViewGroupManager.setViewZIndex(t3, Math.round(f3));
        ViewParent parent = t3.getParent();
        if (parent instanceof InterfaceC0460v0) {
            ((InterfaceC0460v0) parent).f();
        }
    }

    protected void updateViewAccessibility(T t3) {
        C0433h0.g0(t3, t3.isFocusable(), t3.getImportantForAccessibility());
    }

    public BaseViewManager(ReactApplicationContext reactApplicationContext) {
        super(reactApplicationContext);
    }
}
