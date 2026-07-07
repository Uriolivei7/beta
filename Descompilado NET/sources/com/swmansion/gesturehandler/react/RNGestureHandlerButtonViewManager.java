package com.swmansion.gesturehandler.react;

import U1.q;
import U1.r;
import android.R;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.PathEffect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.PaintDrawable;
import android.graphics.drawable.RippleDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityNodeInfo;
import androidx.core.view.AbstractC0248f0;
import com.facebook.react.uimanager.B0;
import com.facebook.react.uimanager.C0429f0;
import com.facebook.react.uimanager.Q0;
import com.facebook.react.uimanager.ViewGroupManager;
import com.swmansion.gesturehandler.react.RNGestureHandlerButtonViewManager;
import d1.AbstractC0505m;
import java.util.ArrayList;
import java.util.Iterator;
import kotlin.jvm.internal.DefaultConstructorMarker;
import n2.C0625d;
import n2.q;
import s2.AbstractC0717n;
import v1.InterfaceC0756a;

/* JADX INFO: loaded from: classes.dex */
@InterfaceC0756a(name = RNGestureHandlerButtonViewManager.REACT_CLASS)
public final class RNGestureHandlerButtonViewManager extends ViewGroupManager<a> implements r {
    public static final b Companion = new b(null);
    public static final String REACT_CLASS = "RNGestureHandlerButton";
    private final Q0 mDelegate = new q(this);

    public static final class a extends ViewGroup implements q.d {

        /* JADX INFO: renamed from: w, reason: collision with root package name */
        private static a f8576w;

        /* JADX INFO: renamed from: x, reason: collision with root package name */
        private static a f8577x;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        private Integer f8579b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        private Integer f8580c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        private boolean f8581d;

        /* JADX INFO: renamed from: e, reason: collision with root package name */
        private boolean f8582e;

        /* JADX INFO: renamed from: f, reason: collision with root package name */
        private float f8583f;

        /* JADX INFO: renamed from: g, reason: collision with root package name */
        private float f8584g;

        /* JADX INFO: renamed from: h, reason: collision with root package name */
        private float f8585h;

        /* JADX INFO: renamed from: i, reason: collision with root package name */
        private float f8586i;

        /* JADX INFO: renamed from: j, reason: collision with root package name */
        private float f8587j;

        /* JADX INFO: renamed from: k, reason: collision with root package name */
        private float f8588k;

        /* JADX INFO: renamed from: l, reason: collision with root package name */
        private Integer f8589l;

        /* JADX INFO: renamed from: m, reason: collision with root package name */
        private String f8590m;

        /* JADX INFO: renamed from: n, reason: collision with root package name */
        private boolean f8591n;

        /* JADX INFO: renamed from: o, reason: collision with root package name */
        private int f8592o;

        /* JADX INFO: renamed from: p, reason: collision with root package name */
        private boolean f8593p;

        /* JADX INFO: renamed from: q, reason: collision with root package name */
        private long f8594q;

        /* JADX INFO: renamed from: r, reason: collision with root package name */
        private int f8595r;

        /* JADX INFO: renamed from: s, reason: collision with root package name */
        private boolean f8596s;

        /* JADX INFO: renamed from: t, reason: collision with root package name */
        private boolean f8597t;

        /* JADX INFO: renamed from: u, reason: collision with root package name */
        public static final C0122a f8574u = new C0122a(null);

        /* JADX INFO: renamed from: v, reason: collision with root package name */
        private static TypedValue f8575v = new TypedValue();

        /* JADX INFO: renamed from: y, reason: collision with root package name */
        private static View.OnClickListener f8578y = new View.OnClickListener() { // from class: com.swmansion.gesturehandler.react.b
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                RNGestureHandlerButtonViewManager.a.n(view);
            }
        };

        /* JADX INFO: renamed from: com.swmansion.gesturehandler.react.RNGestureHandlerButtonViewManager$a$a, reason: collision with other inner class name */
        public static final class C0122a {
            public /* synthetic */ C0122a(DefaultConstructorMarker defaultConstructorMarker) {
                this();
            }

            private C0122a() {
            }
        }

        public a(Context context) {
            super(context);
            this.f8590m = "solid";
            this.f8591n = true;
            this.f8594q = -1L;
            this.f8595r = -1;
            setOnClickListener(f8578y);
            setClickable(true);
            setFocusable(true);
            this.f8593p = true;
            setClipChildren(false);
        }

        private final boolean getHasBorderRadii() {
            return (this.f8583f == 0.0f && this.f8584g == 0.0f && this.f8585h == 0.0f && this.f8586i == 0.0f && this.f8587j == 0.0f) ? false : true;
        }

        private final float[] j() {
            float f3 = this.f8584g;
            float f4 = this.f8585h;
            float f5 = this.f8587j;
            float f6 = this.f8586i;
            float[] fArr = {f3, f3, f4, f4, f5, f5, f6, f6};
            ArrayList arrayList = new ArrayList(8);
            for (int i3 = 0; i3 < 8; i3++) {
                float f7 = fArr[i3];
                if (f7 == 0.0f) {
                    f7 = this.f8583f;
                }
                arrayList.add(Float.valueOf(f7));
            }
            return AbstractC0717n.d0(arrayList);
        }

        private final PathEffect k() {
            String str = this.f8590m;
            if (D2.h.b(str, "dotted")) {
                float f3 = this.f8588k;
                return new DashPathEffect(new float[]{f3, f3, f3, f3}, 0.0f);
            }
            if (!D2.h.b(str, "dashed")) {
                return null;
            }
            float f4 = this.f8588k;
            float f5 = 3;
            return new DashPathEffect(new float[]{f4 * f5, f4 * f5, f4 * f5, f4 * f5}, 0.0f);
        }

        private final Drawable l() {
            PaintDrawable paintDrawable = new PaintDrawable(0);
            if (getHasBorderRadii()) {
                paintDrawable.setCornerRadii(j());
            }
            if (this.f8588k > 0.0f) {
                Paint paint = paintDrawable.getPaint();
                paint.setStyle(Paint.Style.STROKE);
                paint.setStrokeWidth(this.f8588k);
                Integer num = this.f8589l;
                paint.setColor(num != null ? num.intValue() : -16777216);
                paint.setPathEffect(k());
            }
            return paintDrawable;
        }

        private final Drawable m() {
            ColorStateList colorStateList;
            Integer num = this.f8579b;
            if (num != null && num.intValue() == 0) {
                return null;
            }
            int[][] iArr = {new int[]{R.attr.state_enabled}};
            Integer num2 = this.f8580c;
            Integer num3 = this.f8579b;
            if (num3 != null) {
                D2.h.c(num3);
                colorStateList = new ColorStateList(iArr, new int[]{num3.intValue()});
            } else {
                getContext().getTheme().resolveAttribute(R.attr.colorControlHighlight, f8575v, true);
                colorStateList = new ColorStateList(iArr, new int[]{f8575v.data});
            }
            RippleDrawable rippleDrawable = new RippleDrawable(colorStateList, null, this.f8582e ? null : new ShapeDrawable(new RectShape()));
            if (num2 != null) {
                rippleDrawable.setRadius((int) C0429f0.h(num2.intValue()));
            }
            return rippleDrawable;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static final void n(View view) {
        }

        private final k o() {
            k kVar = null;
            for (ViewParent parent = getParent(); parent != null; parent = parent.getParent()) {
                if (parent instanceof k) {
                    kVar = (k) parent;
                }
            }
            return kVar;
        }

        private final boolean p(J2.c cVar) {
            Iterator it = cVar.iterator();
            while (it.hasNext()) {
                View view = (View) it.next();
                if (view instanceof a) {
                    a aVar = (a) view;
                    if (aVar.f8597t || aVar.isPressed()) {
                        return true;
                    }
                }
                if ((view instanceof ViewGroup) && p(AbstractC0248f0.a((ViewGroup) view))) {
                    return true;
                }
            }
            return false;
        }

        static /* synthetic */ boolean q(a aVar, J2.c cVar, int i3, Object obj) {
            if ((i3 & 1) != 0) {
                cVar = AbstractC0248f0.a(aVar);
            }
            return aVar.p(cVar);
        }

        private final void r() {
            if (f8576w == this) {
                f8576w = null;
                f8577x = this;
            }
        }

        private final boolean s() {
            if (q(this, null, 1, null)) {
                return false;
            }
            a aVar = f8576w;
            if (aVar == null) {
                f8576w = this;
                return true;
            }
            if (!this.f8591n) {
                if (!(aVar != null ? aVar.f8591n : false)) {
                    return true;
                }
            } else if (aVar == this) {
                return true;
            }
            return false;
        }

        private final void u(int i3, Drawable drawable, Drawable drawable2) {
            PaintDrawable paintDrawable = new PaintDrawable(i3);
            if (getHasBorderRadii()) {
                paintDrawable.setCornerRadii(j());
            }
            setBackground(new LayerDrawable(drawable2 != null ? new Drawable[]{paintDrawable, drawable2, drawable} : new Drawable[]{paintDrawable, drawable}));
        }

        @Override // n2.q.d
        public boolean a() {
            return q.d.a.f(this);
        }

        @Override // n2.q.d
        public void b(MotionEvent motionEvent) {
            q.d.a.d(this, motionEvent);
        }

        @Override // n2.q.d
        public boolean c(MotionEvent motionEvent) {
            D2.h.f(motionEvent, "event");
            if (motionEvent.getAction() == 3 || motionEvent.getAction() == 1 || motionEvent.getActionMasked() == 6) {
                return false;
            }
            boolean zS = s();
            if (zS) {
                this.f8597t = true;
            }
            return zS;
        }

        @Override // n2.q.d
        public Boolean d(View view, MotionEvent motionEvent) {
            return q.d.a.e(this, view, motionEvent);
        }

        @Override // android.view.ViewGroup, android.view.View
        public void dispatchDrawableHotspotChanged(float f3, float f4) {
        }

        @Override // android.view.View
        public void drawableHotspotChanged(float f3, float f4) {
            a aVar = f8576w;
            if (aVar == null || aVar == this) {
                super.drawableHotspotChanged(f3, f4);
            }
        }

        @Override // n2.q.d
        public boolean e() {
            return q.d.a.h(this);
        }

        @Override // n2.q.d
        public void f(MotionEvent motionEvent) {
            D2.h.f(motionEvent, "event");
            r();
            this.f8597t = false;
        }

        @Override // n2.q.d
        public Boolean g(C0625d c0625d) {
            return q.d.a.g(this, c0625d);
        }

        public final float getBorderBottomLeftRadius() {
            return this.f8586i;
        }

        public final float getBorderBottomRightRadius() {
            return this.f8587j;
        }

        public final Integer getBorderColor() {
            return this.f8589l;
        }

        public final float getBorderRadius() {
            return this.f8583f;
        }

        public final String getBorderStyle() {
            return this.f8590m;
        }

        public final float getBorderTopLeftRadius() {
            return this.f8584g;
        }

        public final float getBorderTopRightRadius() {
            return this.f8585h;
        }

        public final float getBorderWidth() {
            return this.f8588k;
        }

        public final boolean getExclusive() {
            return this.f8591n;
        }

        public final Integer getRippleColor() {
            return this.f8579b;
        }

        public final Integer getRippleRadius() {
            return this.f8580c;
        }

        public final boolean getUseBorderlessDrawable() {
            return this.f8582e;
        }

        public final boolean getUseDrawableOnForeground() {
            return this.f8581d;
        }

        @Override // n2.q.d
        public boolean h(View view) {
            return q.d.a.b(this, view);
        }

        @Override // android.view.View
        public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
            D2.h.f(accessibilityNodeInfo, "info");
            super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
            Object tag = super.getTag(AbstractC0505m.f9247t);
            if (tag instanceof String) {
                accessibilityNodeInfo.setViewIdResourceName((String) tag);
            }
        }

        @Override // android.view.ViewGroup
        public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
            D2.h.f(motionEvent, "ev");
            if (super.onInterceptTouchEvent(motionEvent)) {
                return true;
            }
            onTouchEvent(motionEvent);
            return isPressed();
        }

        @Override // android.view.View, android.view.KeyEvent.Callback
        public boolean onKeyUp(int i3, KeyEvent keyEvent) {
            this.f8596s = true;
            return super.onKeyUp(i3, keyEvent);
        }

        @Override // android.view.ViewGroup, android.view.View
        protected void onLayout(boolean z3, int i3, int i4, int i5, int i6) {
        }

        @Override // android.view.View
        public boolean onTouchEvent(MotionEvent motionEvent) {
            D2.h.f(motionEvent, "event");
            long eventTime = motionEvent.getEventTime();
            int action = motionEvent.getAction();
            a aVar = f8576w;
            if (aVar != null && aVar != this) {
                D2.h.c(aVar);
                if (aVar.f8591n) {
                    if (isPressed()) {
                        setPressed(false);
                    }
                    this.f8594q = eventTime;
                    this.f8595r = action;
                    return false;
                }
            }
            if (motionEvent.getAction() == 3) {
                r();
            }
            if (this.f8594q == eventTime && this.f8595r == action && action != 3) {
                return false;
            }
            this.f8594q = eventTime;
            this.f8595r = action;
            return super.onTouchEvent(motionEvent);
        }

        @Override // android.view.View
        public boolean performClick() {
            if (q(this, null, 1, null)) {
                return false;
            }
            Context context = getContext();
            D2.h.e(context, "getContext(...)");
            if (com.swmansion.gesturehandler.react.a.c(context)) {
                k kVarO = o();
                if (kVarO != null) {
                    kVarO.F(this);
                }
            } else if (this.f8596s) {
                k kVarO2 = o();
                if (kVarO2 != null) {
                    kVarO2.F(this);
                }
                this.f8596s = false;
            }
            if (f8577x != this) {
                return false;
            }
            r();
            f8577x = null;
            return super.performClick();
        }

        @Override // android.view.View
        public void setBackgroundColor(int i3) {
            this.f8592o = i3;
            this.f8593p = true;
        }

        public final void setBorderBottomLeftRadius(float f3) {
            this.f8586i = f3 * getResources().getDisplayMetrics().density;
            this.f8593p = true;
        }

        public final void setBorderBottomRightRadius(float f3) {
            this.f8587j = f3 * getResources().getDisplayMetrics().density;
            this.f8593p = true;
        }

        public final void setBorderColor(Integer num) {
            this.f8589l = num;
            this.f8593p = true;
        }

        public final void setBorderRadius(float f3) {
            this.f8583f = f3 * getResources().getDisplayMetrics().density;
            this.f8593p = true;
        }

        public final void setBorderStyle(String str) {
            this.f8590m = str;
            this.f8593p = true;
        }

        public final void setBorderTopLeftRadius(float f3) {
            this.f8584g = f3 * getResources().getDisplayMetrics().density;
            this.f8593p = true;
        }

        public final void setBorderTopRightRadius(float f3) {
            this.f8585h = f3 * getResources().getDisplayMetrics().density;
            this.f8593p = true;
        }

        public final void setBorderWidth(float f3) {
            this.f8588k = f3 * getResources().getDisplayMetrics().density;
            this.f8593p = true;
        }

        public final void setExclusive(boolean z3) {
            this.f8591n = z3;
        }

        /* JADX WARN: Removed duplicated region for block: B:16:0x0021  */
        @Override // android.view.View
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public void setPressed(boolean r4) {
            /*
                r3 = this;
                if (r4 == 0) goto La
                boolean r0 = r3.s()
                if (r0 == 0) goto La
                com.swmansion.gesturehandler.react.RNGestureHandlerButtonViewManager.a.f8577x = r3
            La:
                boolean r0 = r3.f8591n
                r1 = 0
                if (r0 != 0) goto L21
                com.swmansion.gesturehandler.react.RNGestureHandlerButtonViewManager$a r0 = com.swmansion.gesturehandler.react.RNGestureHandlerButtonViewManager.a.f8576w
                r2 = 1
                if (r0 == 0) goto L19
                boolean r0 = r0.f8591n
                if (r0 != r2) goto L19
                goto L21
            L19:
                r0 = 0
                boolean r0 = q(r3, r0, r2, r0)
                if (r0 != 0) goto L21
                goto L22
            L21:
                r2 = r1
            L22:
                if (r4 == 0) goto L2a
                com.swmansion.gesturehandler.react.RNGestureHandlerButtonViewManager$a r0 = com.swmansion.gesturehandler.react.RNGestureHandlerButtonViewManager.a.f8576w
                if (r0 == r3) goto L2a
                if (r2 == 0) goto L2f
            L2a:
                r3.f8597t = r4
                super.setPressed(r4)
            L2f:
                if (r4 != 0) goto L37
                com.swmansion.gesturehandler.react.RNGestureHandlerButtonViewManager$a r4 = com.swmansion.gesturehandler.react.RNGestureHandlerButtonViewManager.a.f8576w
                if (r4 != r3) goto L37
                r3.f8597t = r1
            L37:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.swmansion.gesturehandler.react.RNGestureHandlerButtonViewManager.a.setPressed(boolean):void");
        }

        public final void setRippleColor(Integer num) {
            this.f8579b = num;
            this.f8593p = true;
        }

        public final void setRippleRadius(Integer num) {
            this.f8580c = num;
            this.f8593p = true;
        }

        public final void setTouched(boolean z3) {
            this.f8597t = z3;
        }

        public final void setUseBorderlessDrawable(boolean z3) {
            this.f8582e = z3;
        }

        public final void setUseDrawableOnForeground(boolean z3) {
            this.f8581d = z3;
            this.f8593p = true;
        }

        public final void t() {
            if (this.f8593p) {
                this.f8593p = false;
                if (this.f8592o == 0) {
                    setBackground(null);
                }
                setForeground(null);
                Drawable drawableM = m();
                Drawable drawableL = l();
                if (getHasBorderRadii() && (drawableM instanceof RippleDrawable)) {
                    PaintDrawable paintDrawable = new PaintDrawable(-1);
                    paintDrawable.setCornerRadii(j());
                    ((RippleDrawable) drawableM).setDrawableByLayerId(R.id.mask, paintDrawable);
                }
                if (this.f8581d) {
                    setForeground(drawableM);
                    int i3 = this.f8592o;
                    if (i3 != 0) {
                        u(i3, drawableL, null);
                        return;
                    }
                    return;
                }
                int i4 = this.f8592o;
                if (i4 == 0 && this.f8579b == null) {
                    setBackground(new LayerDrawable(new Drawable[]{drawableM, drawableL}));
                } else {
                    u(i4, drawableL, drawableM);
                }
            }
        }
    }

    public static final class b {
        public /* synthetic */ b(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private b() {
        }
    }

    @Override // com.facebook.react.uimanager.ViewManager
    protected Q0 getDelegate() {
        return this.mDelegate;
    }

    @Override // com.facebook.react.uimanager.ViewManager, com.facebook.react.bridge.NativeModule
    public String getName() {
        return REACT_CLASS;
    }

    @Override // com.facebook.react.uimanager.ViewGroupManager, com.facebook.react.uimanager.N
    public /* bridge */ /* synthetic */ void removeAllViews(View view) {
        super.removeAllViews(view);
    }

    @Override // com.facebook.react.uimanager.ViewManager
    public a createViewInstance(B0 b02) {
        D2.h.f(b02, "context");
        return new a(b02);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.facebook.react.uimanager.BaseViewManager, com.facebook.react.uimanager.ViewManager
    public void onAfterUpdateTransaction(a aVar) {
        D2.h.f(aVar, "view");
        super.onAfterUpdateTransaction(aVar);
        aVar.t();
    }

    @Override // com.facebook.react.uimanager.BaseViewManager
    @L1.a(name = "backgroundColor")
    public void setBackgroundColor(a aVar, int i3) {
        D2.h.f(aVar, "view");
        aVar.setBackgroundColor(i3);
    }

    @Override // com.facebook.react.uimanager.BaseViewManager
    @L1.a(name = "borderBottomLeftRadius")
    public void setBorderBottomLeftRadius(a aVar, float f3) {
        D2.h.f(aVar, "view");
        aVar.setBorderBottomLeftRadius(f3);
    }

    @Override // com.facebook.react.uimanager.BaseViewManager
    @L1.a(name = "borderBottomRightRadius")
    public void setBorderBottomRightRadius(a aVar, float f3) {
        D2.h.f(aVar, "view");
        aVar.setBorderBottomRightRadius(f3);
    }

    @Override // U1.r
    @L1.a(name = "borderColor")
    public void setBorderColor(a aVar, Integer num) {
        D2.h.f(aVar, "view");
        aVar.setBorderColor(num);
    }

    @Override // com.facebook.react.uimanager.BaseViewManager
    @L1.a(name = "borderRadius")
    public void setBorderRadius(a aVar, float f3) {
        D2.h.f(aVar, "view");
        aVar.setBorderRadius(f3);
    }

    @Override // U1.r
    @L1.a(name = "borderStyle")
    public void setBorderStyle(a aVar, String str) {
        D2.h.f(aVar, "view");
        aVar.setBorderStyle(str);
    }

    @Override // com.facebook.react.uimanager.BaseViewManager
    @L1.a(name = "borderTopLeftRadius")
    public void setBorderTopLeftRadius(a aVar, float f3) {
        D2.h.f(aVar, "view");
        aVar.setBorderTopLeftRadius(f3);
    }

    @Override // com.facebook.react.uimanager.BaseViewManager
    @L1.a(name = "borderTopRightRadius")
    public void setBorderTopRightRadius(a aVar, float f3) {
        D2.h.f(aVar, "view");
        aVar.setBorderTopRightRadius(f3);
    }

    @Override // U1.r
    @L1.a(name = "borderWidth")
    public void setBorderWidth(a aVar, float f3) {
        D2.h.f(aVar, "view");
        aVar.setBorderWidth(f3);
    }

    @Override // U1.r
    @L1.a(name = "borderless")
    public void setBorderless(a aVar, boolean z3) {
        D2.h.f(aVar, "view");
        aVar.setUseBorderlessDrawable(z3);
    }

    @Override // U1.r
    @L1.a(name = "enabled")
    public void setEnabled(a aVar, boolean z3) {
        D2.h.f(aVar, "view");
        aVar.setEnabled(z3);
    }

    @Override // U1.r
    @L1.a(name = "exclusive")
    public void setExclusive(a aVar, boolean z3) {
        D2.h.f(aVar, "view");
        aVar.setExclusive(z3);
    }

    @Override // U1.r
    @L1.a(name = "foreground")
    public void setForeground(a aVar, boolean z3) {
        D2.h.f(aVar, "view");
        aVar.setUseDrawableOnForeground(z3);
    }

    @Override // U1.r
    @L1.a(name = "rippleColor")
    public void setRippleColor(a aVar, Integer num) {
        D2.h.f(aVar, "view");
        aVar.setRippleColor(num);
    }

    @Override // U1.r
    @L1.a(name = "rippleRadius")
    public void setRippleRadius(a aVar, int i3) {
        D2.h.f(aVar, "view");
        aVar.setRippleRadius(Integer.valueOf(i3));
    }

    @Override // U1.r
    @L1.a(name = "touchSoundDisabled")
    public void setTouchSoundDisabled(a aVar, boolean z3) {
        D2.h.f(aVar, "view");
        aVar.setSoundEffectsEnabled(!z3);
    }
}
