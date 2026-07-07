package com.facebook.react.views.view;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.View;
import com.facebook.react.bridge.Dynamic;
import com.facebook.react.bridge.DynamicFromObject;
import com.facebook.react.bridge.JSApplicationIllegalArgumentException;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableType;
import com.facebook.react.uimanager.B0;
import com.facebook.react.uimanager.C0418a;
import com.facebook.react.uimanager.C0429f0;
import com.facebook.react.uimanager.EnumC0431g0;
import com.facebook.react.uimanager.H0;
import com.facebook.react.uimanager.W;
import com.facebook.react.uimanager.X;
import com.facebook.react.uimanager.events.EventDispatcher;
import java.util.ArrayList;
import java.util.Map;
import kotlin.jvm.internal.DefaultConstructorMarker;
import r1.C0670b;
import s2.AbstractC0696D;
import v1.InterfaceC0756a;

/* JADX INFO: loaded from: classes.dex */
@InterfaceC0756a(name = ReactViewManager.REACT_CLASS)
public class ReactViewManager extends ReactClippingViewManager<g> {
    private static final int CMD_HOTSPOT_UPDATE = 1;
    private static final int CMD_SET_PRESSED = 2;
    private static final String HOTSPOT_UPDATE_KEY = "hotspotUpdate";
    public static final String REACT_CLASS = "RCTView";
    public static final a Companion = new a(null);
    private static final int[] SPACING_TYPES = {8, 0, 2, 1, 3, 4, 5, 9, 10, 11};

    public static final class a {
        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private a() {
        }
    }

    public /* synthetic */ class b {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public static final /* synthetic */ int[] f8159a;

        static {
            int[] iArr = new int[ReadableType.values().length];
            try {
                iArr[ReadableType.Map.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                iArr[ReadableType.Number.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                iArr[ReadableType.Null.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            f8159a = iArr;
        }
    }

    public ReactViewManager() {
        if (C0670b.l()) {
            setupViewRecycling();
        }
    }

    private final void handleHotspotUpdate(g gVar, ReadableArray readableArray) {
        if (readableArray == null || readableArray.size() != 2) {
            throw new JSApplicationIllegalArgumentException("Illegal number of arguments for 'updateHotspot' command");
        }
        C0429f0 c0429f0 = C0429f0.f7476a;
        gVar.drawableHotspotChanged(c0429f0.a(readableArray.getDouble(0)), c0429f0.a(readableArray.getDouble(1)));
    }

    private final void handleSetPressed(g gVar, ReadableArray readableArray) {
        if (readableArray == null || readableArray.size() != 1) {
            throw new JSApplicationIllegalArgumentException("Illegal number of arguments for 'setPressed' command");
        }
        gVar.setPressed(readableArray.getBoolean(0));
    }

    private final int px(ReadableMap readableMap, String str) {
        if (readableMap.hasKey(str)) {
            return (int) C0429f0.f7476a.a(readableMap.getDouble(str));
        }
        return 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void setFocusable$lambda$2(g gVar, View view) {
        Context context = gVar.getContext();
        D2.h.d(context, "null cannot be cast to non-null type com.facebook.react.bridge.ReactContext");
        EventDispatcher eventDispatcherC = H0.c((ReactContext) context, gVar.getId());
        if (eventDispatcherC != null) {
            eventDispatcherC.b(new j(H0.e(gVar.getContext()), gVar.getId()));
        }
    }

    @Override // com.facebook.react.uimanager.ViewManager
    public Map<String, Integer> getCommandsMap() {
        return AbstractC0696D.i(r2.n.a(HOTSPOT_UPDATE_KEY, 1), r2.n.a("setPressed", 2));
    }

    @Override // com.facebook.react.uimanager.ViewManager, com.facebook.react.bridge.NativeModule
    public String getName() {
        return REACT_CLASS;
    }

    @L1.a(defaultInt = -1, name = "nextFocusDown")
    public void nextFocusDown(g gVar, int i3) {
        D2.h.f(gVar, "view");
        gVar.setNextFocusDownId(i3);
    }

    @L1.a(defaultInt = -1, name = "nextFocusForward")
    public void nextFocusForward(g gVar, int i3) {
        D2.h.f(gVar, "view");
        gVar.setNextFocusForwardId(i3);
    }

    @L1.a(defaultInt = -1, name = "nextFocusLeft")
    public void nextFocusLeft(g gVar, int i3) {
        D2.h.f(gVar, "view");
        gVar.setNextFocusLeftId(i3);
    }

    @L1.a(defaultInt = -1, name = "nextFocusRight")
    public void nextFocusRight(g gVar, int i3) {
        D2.h.f(gVar, "view");
        gVar.setNextFocusRightId(i3);
    }

    @L1.a(defaultInt = -1, name = "nextFocusUp")
    public void nextFocusUp(g gVar, int i3) {
        D2.h.f(gVar, "view");
        gVar.setNextFocusUpId(i3);
    }

    @L1.a(name = "accessible")
    public void setAccessible(g gVar, boolean z3) {
        D2.h.f(gVar, "view");
        gVar.setFocusable(z3);
    }

    @L1.a(name = "backfaceVisibility")
    public void setBackfaceVisibility(g gVar, String str) {
        D2.h.f(gVar, "view");
        D2.h.f(str, "backfaceVisibility");
        gVar.setBackfaceVisibility(str);
    }

    @L1.a(customType = "BackgroundImage", name = "experimental_backgroundImage")
    public void setBackgroundImage(g gVar, ReadableArray readableArray) {
        D2.h.f(gVar, "view");
        if (M1.a.c(gVar) == 2) {
            if (readableArray == null || readableArray.size() <= 0) {
                C0418a.o(gVar, null);
                return;
            }
            ArrayList arrayList = new ArrayList(readableArray.size());
            int size = readableArray.size();
            for (int i3 = 0; i3 < size; i3++) {
                ReadableMap map = readableArray.getMap(i3);
                Context context = gVar.getContext();
                D2.h.e(context, "getContext(...)");
                arrayList.add(new R1.a(map, context));
            }
            C0418a.o(gVar, arrayList);
        }
    }

    @L1.b(customType = "Color", names = {"borderColor", "borderLeftColor", "borderRightColor", "borderTopColor", "borderBottomColor", "borderStartColor", "borderEndColor", "borderBlockColor", "borderBlockEndColor", "borderBlockStartColor"})
    public void setBorderColor(g gVar, int i3, Integer num) {
        D2.h.f(gVar, "view");
        C0418a.p(gVar, R1.n.f2074b.a(SPACING_TYPES[i3]), num);
    }

    @L1.b(names = {"borderRadius", "borderTopLeftRadius", "borderTopRightRadius", "borderBottomRightRadius", "borderBottomLeftRadius", "borderTopStartRadius", "borderTopEndRadius", "borderBottomStartRadius", "borderBottomEndRadius", "borderEndEndRadius", "borderEndStartRadius", "borderStartEndRadius", "borderStartStartRadius"})
    public void setBorderRadius(g gVar, int i3, Dynamic dynamic) {
        D2.h.f(gVar, "view");
        D2.h.f(dynamic, "rawBorderRadius");
        W wA = W.f7404c.a(dynamic);
        if (M1.a.c(gVar) != 2 && wA != null && wA.a() == X.f7409c) {
            wA = null;
        }
        C0418a.q(gVar, R1.d.values()[i3], wA);
    }

    @L1.a(name = "borderStyle")
    public void setBorderStyle(g gVar, String str) {
        D2.h.f(gVar, "view");
        C0418a.r(gVar, str == null ? null : R1.f.f2028b.a(str));
    }

    @L1.b(defaultFloat = Float.NaN, names = {"borderWidth", "borderLeftWidth", "borderRightWidth", "borderTopWidth", "borderBottomWidth", "borderStartWidth", "borderEndWidth"})
    public void setBorderWidth(g gVar, int i3, float f3) {
        D2.h.f(gVar, "view");
        C0418a.s(gVar, R1.n.values()[i3], Float.valueOf(f3));
    }

    @L1.a(name = "collapsable")
    public void setCollapsable(g gVar, boolean z3) {
        D2.h.f(gVar, "view");
    }

    @L1.a(name = "collapsableChildren")
    public void setCollapsableChildren(g gVar, boolean z3) {
        D2.h.f(gVar, "view");
    }

    @L1.a(name = "focusable")
    public void setFocusable(final g gVar, boolean z3) {
        D2.h.f(gVar, "view");
        if (z3) {
            gVar.setOnClickListener(new View.OnClickListener() { // from class: com.facebook.react.views.view.i
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    ReactViewManager.setFocusable$lambda$2(gVar, view);
                }
            });
            gVar.setFocusable(true);
        } else {
            gVar.setOnClickListener(null);
            gVar.setClickable(false);
        }
    }

    @L1.a(name = "hitSlop")
    public void setHitSlop(g gVar, Dynamic dynamic) {
        D2.h.f(gVar, "view");
        D2.h.f(dynamic, "hitSlop");
        int i3 = b.f8159a[dynamic.getType().ordinal()];
        if (i3 == 1) {
            ReadableMap readableMapAsMap = dynamic.asMap();
            gVar.setHitSlopRect(new Rect(px(readableMapAsMap, "left"), px(readableMapAsMap, "top"), px(readableMapAsMap, "right"), px(readableMapAsMap, "bottom")));
            return;
        }
        if (i3 == 2) {
            int iA = (int) C0429f0.f7476a.a(dynamic.asDouble());
            gVar.setHitSlopRect(new Rect(iA, iA, iA, iA));
        } else {
            if (i3 == 3) {
                gVar.setHitSlopRect(null);
                return;
            }
            Y.a.I("ReactNative", "Invalid type for 'hitSlop' value " + dynamic.getType());
            gVar.setHitSlopRect(null);
        }
    }

    @L1.a(name = "nativeBackgroundAndroid")
    public void setNativeBackground(g gVar, ReadableMap readableMap) {
        Drawable drawableA;
        D2.h.f(gVar, "view");
        if (readableMap != null) {
            Context context = gVar.getContext();
            D2.h.e(context, "getContext(...)");
            drawableA = f.a(context, readableMap);
        } else {
            drawableA = null;
        }
        C0418a.v(gVar, drawableA);
    }

    @L1.a(name = "nativeForegroundAndroid")
    public void setNativeForeground(g gVar, ReadableMap readableMap) {
        Drawable drawableA;
        D2.h.f(gVar, "view");
        if (readableMap != null) {
            Context context = gVar.getContext();
            D2.h.e(context, "getContext(...)");
            drawableA = f.a(context, readableMap);
        } else {
            drawableA = null;
        }
        gVar.setForeground(drawableA);
    }

    @L1.a(name = "needsOffscreenAlphaCompositing")
    public void setNeedsOffscreenAlphaCompositing(g gVar, boolean z3) {
        D2.h.f(gVar, "view");
        gVar.setNeedsOffscreenAlphaCompositing(z3);
    }

    @L1.a(name = "overflow")
    public void setOverflow(g gVar, String str) {
        D2.h.f(gVar, "view");
        gVar.setOverflow(str);
    }

    @L1.a(name = "pointerEvents")
    public void setPointerEvents(g gVar, String str) {
        D2.h.f(gVar, "view");
        gVar.setPointerEvents(EnumC0431g0.f7478b.c(str));
    }

    @L1.a(name = "hasTVPreferredFocus")
    public void setTVPreferredFocus(g gVar, boolean z3) {
        D2.h.f(gVar, "view");
        if (z3) {
            gVar.setFocusable(true);
            gVar.setFocusableInTouchMode(true);
            gVar.requestFocus();
        }
    }

    @Override // com.facebook.react.uimanager.ViewManager
    public g createViewInstance(B0 b02) {
        D2.h.f(b02, "context");
        return new g(b02);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.facebook.react.uimanager.BaseViewManager, com.facebook.react.uimanager.ViewManager
    public g prepareToRecycleView(B0 b02, g gVar) {
        D2.h.f(b02, "reactContext");
        D2.h.f(gVar, "view");
        g gVar2 = (g) super.prepareToRecycleView(b02, gVar);
        if (gVar2 != null) {
            gVar2.s();
        }
        return gVar;
    }

    @Override // com.facebook.react.uimanager.BaseViewManager
    public void setOpacity(g gVar, float f3) {
        D2.h.f(gVar, "view");
        gVar.setOpacityIfPossible(f3);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.facebook.react.uimanager.BaseViewManager
    public void setTransformProperty(g gVar, ReadableArray readableArray, ReadableArray readableArray2) {
        D2.h.f(gVar, "view");
        super.setTransformProperty(gVar, readableArray, readableArray2);
        gVar.x();
    }

    @Override // com.facebook.react.uimanager.ViewManager
    public void receiveCommand(g gVar, int i3, ReadableArray readableArray) {
        D2.h.f(gVar, "root");
        if (i3 == 1) {
            handleHotspotUpdate(gVar, readableArray);
        } else {
            if (i3 != 2) {
                return;
            }
            handleSetPressed(gVar, readableArray);
        }
    }

    @Override // com.facebook.react.uimanager.ViewManager
    public void receiveCommand(g gVar, String str, ReadableArray readableArray) {
        D2.h.f(gVar, "root");
        D2.h.f(str, "commandId");
        if (D2.h.b(str, HOTSPOT_UPDATE_KEY)) {
            handleHotspotUpdate(gVar, readableArray);
        } else if (D2.h.b(str, "setPressed")) {
            handleSetPressed(gVar, readableArray);
        }
    }

    public void setBorderRadius(g gVar, int i3, float f3) {
        D2.h.f(gVar, "view");
        setBorderRadius(gVar, i3, new DynamicFromObject(Float.valueOf(f3)));
    }
}
