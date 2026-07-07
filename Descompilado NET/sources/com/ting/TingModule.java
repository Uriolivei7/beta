package com.ting;

import D2.o;
import D2.p;
import android.content.Context;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.UiThreadUtil;
import com.facebook.react.bridge.WritableMap;
import com.ting.TingModule;
import f2.h;
import kotlin.jvm.internal.DefaultConstructorMarker;
import p2.AbstractC0638a;
import p2.AbstractC0639b;
import p2.AbstractC0640c;
import p2.AbstractC0641d;
import p2.k;

/* JADX INFO: loaded from: classes.dex */
public final class TingModule extends TingSpec {
    public static final a Companion = new a(null);
    public static final String NAME = "Ting";
    private ReadableMap alertOptionInit;
    private final h alertWindow;
    private Context context;
    private ReadableMap toastOptionInit;
    private final h toastWindow;

    public static final class a {
        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private a() {
        }
    }

    public static final class b extends GestureDetector.SimpleOnGestureListener {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        final /* synthetic */ int f8653a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        final /* synthetic */ int f8654b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        final /* synthetic */ TingModule f8655c;

        b(int i3, int i4, TingModule tingModule) {
            this.f8653a = i3;
            this.f8654b = i4;
            this.f8655c = tingModule;
        }

        @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
        public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float f3, float f4) {
            D2.h.f(motionEvent2, "e2");
            if (Math.abs(f4) > Math.abs(f3)) {
                int i3 = this.f8653a;
                if (i3 == 48 && f4 > this.f8654b) {
                    this.f8655c.toastWindow.e();
                    return true;
                }
                if (i3 == 80 && f4 < (-this.f8654b)) {
                    this.f8655c.toastWindow.e();
                    return true;
                }
            }
            return super.onScroll(motionEvent, motionEvent2, f3, f4);
        }
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public TingModule(ReactApplicationContext reactApplicationContext) {
        super(reactApplicationContext);
        D2.h.f(reactApplicationContext, "context");
        this.context = reactApplicationContext;
        this.alertWindow = new h(getCurrentActivity());
        this.toastWindow = new h(getCurrentActivity());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void alert$lambda$8(TingModule tingModule, int i3, LinearLayout linearLayout, int i4, o oVar, final ReadableMap readableMap) {
        tingModule.alertWindow.e();
        h hVar = tingModule.alertWindow;
        hVar.A(i3);
        hVar.z(linearLayout);
        hVar.w(AbstractC0641d.f10312a);
        hVar.F(true);
        hVar.B(17);
        hVar.y(i4);
        hVar.x((float) oVar.f186b);
        hVar.D(AbstractC0639b.f10305a, new h.a() { // from class: p2.i
            @Override // f2.h.a
            public final void a(f2.h hVar2, View view) {
                TingModule.alert$lambda$8$lambda$7$lambda$6(readableMap, hVar2, (LinearLayout) view);
            }
        });
        hVar.J();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void alert$lambda$8$lambda$7$lambda$6(ReadableMap readableMap, h hVar, LinearLayout linearLayout) {
        D2.h.f(hVar, "alert");
        if (readableMap.hasKey("shouldDismissByTap") ? readableMap.getBoolean("shouldDismissByTap") : true) {
            hVar.e();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void dismissAlert$lambda$9(TingModule tingModule) {
        tingModule.alertWindow.e();
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Failed to restore switch over string. Please report as a decompilation issue */
    /* JADX WARN: Removed duplicated region for block: B:75:0x019b  */
    /* JADX WARN: Removed duplicated region for block: B:89:0x01d2  */
    /* JADX WARN: Removed duplicated region for block: B:92:0x01da  */
    /* JADX WARN: Removed duplicated region for block: B:94:0x01ec  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private final android.widget.LinearLayout getContainerView(int r18, com.facebook.react.bridge.ReadableMap r19, java.lang.String r20) {
        /*
            Method dump skipped, instruction units count: 524
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.ting.TingModule.getContainerView(int, com.facebook.react.bridge.ReadableMap, java.lang.String):android.widget.LinearLayout");
    }

    private final int getDuration(ReadableMap readableMap) {
        if (readableMap.hasKey("duration")) {
            return (int) (readableMap.getDouble("duration") * ((double) 1000));
        }
        return 3000;
    }

    private final void loadDoneIcon(ImageView imageView) {
        imageView.setImageResource(AbstractC0638a.f10303a);
        Drawable drawable = imageView.getDrawable();
        D2.h.d(drawable, "null cannot be cast to non-null type android.graphics.drawable.AnimatedVectorDrawable");
        final AnimatedVectorDrawable animatedVectorDrawable = (AnimatedVectorDrawable) drawable;
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() { // from class: p2.j
            @Override // java.lang.Runnable
            public final void run() {
                animatedVectorDrawable.start();
            }
        }, 300L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void toast$lambda$4(TingModule tingModule, int i3, LinearLayout linearLayout, int i4, p pVar, ReadableMap readableMap) {
        tingModule.toastWindow.e();
        h hVar = tingModule.toastWindow;
        hVar.A(i3);
        hVar.z(linearLayout);
        hVar.B(i4);
        hVar.I(48);
        hVar.w(pVar.f187b);
        hVar.F(true);
        if (readableMap.hasKey("shouldDismissByDrag") && readableMap.getBoolean("shouldDismissByDrag")) {
            int i5 = (int) ((12 * hVar.h().getResources().getDisplayMetrics().density) + 0.5f);
            View viewG = hVar.g();
            if (viewG != null) {
                final GestureDetector gestureDetector = new GestureDetector(hVar.h(), new b(i4, i5, tingModule));
                viewG.setOnTouchListener(new View.OnTouchListener() { // from class: p2.f
                    @Override // android.view.View.OnTouchListener
                    public final boolean onTouch(View view, MotionEvent motionEvent) {
                        return TingModule.toast$lambda$4$lambda$3$lambda$2$lambda$1(gestureDetector, view, motionEvent);
                    }
                });
            }
        }
        hVar.J();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final boolean toast$lambda$4$lambda$3$lambda$2$lambda$1(GestureDetector gestureDetector, View view, MotionEvent motionEvent) {
        gestureDetector.onTouchEvent(motionEvent);
        return true;
    }

    @Override // com.ting.NativeTingSpec
    @ReactMethod
    public void alert(ReadableMap readableMap) {
        WritableMap writableMapD;
        D2.h.f(readableMap, "rnOptions");
        ReadableMap readableMap2 = this.alertOptionInit;
        final ReadableMap readableMap3 = (readableMap2 == null || (writableMapD = k.d(readableMap2, readableMap)) == null) ? readableMap : writableMapD;
        final LinearLayout containerView = getContainerView(AbstractC0640c.f10310a, readableMap3, "alert");
        final int duration = getDuration(readableMap3);
        final int i3 = readableMap3.hasKey("blurBackdrop") ? readableMap3.getInt("blurBackdrop") : 0;
        final o oVar = new o();
        double d4 = readableMap3.hasKey("backdropOpacity") ? readableMap3.getDouble("backdropOpacity") : 0.0d;
        oVar.f186b = d4;
        if (d4 < 0.0d) {
            oVar.f186b = 0.0d;
        } else if (d4 > 1.0d) {
            oVar.f186b = 1.0d;
        }
        UiThreadUtil.runOnUiThread(new Runnable() { // from class: p2.g
            @Override // java.lang.Runnable
            public final void run() {
                TingModule.alert$lambda$8(this.f10322b, duration, containerView, i3, oVar, readableMap3);
            }
        });
    }

    @Override // com.ting.NativeTingSpec
    @ReactMethod
    public void dismissAlert() {
        if (this.alertWindow.m()) {
            UiThreadUtil.runOnUiThread(new Runnable() { // from class: p2.h
                @Override // java.lang.Runnable
                public final void run() {
                    TingModule.dismissAlert$lambda$9(this.f10328b);
                }
            });
        }
    }

    @Override // com.ting.NativeTingSpec, com.facebook.react.bridge.NativeModule
    public String getName() {
        return "Ting";
    }

    @Override // com.ting.NativeTingSpec
    @ReactMethod
    public void setup(ReadableMap readableMap) {
        D2.h.f(readableMap, "options");
        ReadableMap map = readableMap.getMap("toast");
        ReadableMap map2 = readableMap.getMap("alert");
        if (map != null) {
            this.toastOptionInit = map;
        }
        if (map2 != null) {
            this.alertOptionInit = map2;
        }
    }

    @Override // com.ting.NativeTingSpec
    @ReactMethod
    public void toast(ReadableMap readableMap) {
        int i3;
        WritableMap writableMapD;
        D2.h.f(readableMap, "rnOptions");
        ReadableMap readableMap2 = this.toastOptionInit;
        final ReadableMap readableMap3 = (readableMap2 == null || (writableMapD = k.d(readableMap2, readableMap)) == null) ? readableMap : writableMapD;
        final LinearLayout containerView = getContainerView(AbstractC0640c.f10311b, readableMap3, "toast");
        final int duration = getDuration(readableMap3);
        final p pVar = new p();
        pVar.f187b = AbstractC0641d.f10314c;
        if (D2.h.b(readableMap3.getString("position"), "bottom")) {
            pVar.f187b = AbstractC0641d.f10313b;
            i3 = 80;
        } else {
            i3 = 48;
        }
        final int i4 = i3;
        UiThreadUtil.runOnUiThread(new Runnable() { // from class: p2.e
            @Override // java.lang.Runnable
            public final void run() {
                TingModule.toast$lambda$4(this.f10315b, duration, containerView, i4, pVar, readableMap3);
            }
        });
    }
}
