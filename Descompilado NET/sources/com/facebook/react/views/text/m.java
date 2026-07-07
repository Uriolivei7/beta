package com.facebook.react.views.text;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.Layout;
import android.text.Spannable;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.ViewGroup;
import androidx.appcompat.widget.D;
import androidx.appcompat.widget.e0;
import androidx.core.view.C0237a;
import androidx.core.view.Z;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.uimanager.C0418a;
import com.facebook.react.uimanager.C0429f0;
import com.facebook.react.uimanager.InterfaceC0439k0;
import com.facebook.react.uimanager.W;
import com.facebook.react.uimanager.X;
import java.util.Comparator;
import q1.C0652c;
import x.AbstractC0766a;

/* JADX INFO: loaded from: classes.dex */
public class m extends D implements InterfaceC0439k0 {

    /* JADX INFO: renamed from: v, reason: collision with root package name */
    private static final ViewGroup.LayoutParams f7987v = new ViewGroup.LayoutParams(0, 0);

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    private boolean f7988i;

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    private int f7989j;

    /* JADX INFO: renamed from: k, reason: collision with root package name */
    private TextUtils.TruncateAt f7990k;

    /* JADX INFO: renamed from: l, reason: collision with root package name */
    private boolean f7991l;

    /* JADX INFO: renamed from: m, reason: collision with root package name */
    private float f7992m;

    /* JADX INFO: renamed from: n, reason: collision with root package name */
    private float f7993n;

    /* JADX INFO: renamed from: o, reason: collision with root package name */
    private float f7994o;

    /* JADX INFO: renamed from: p, reason: collision with root package name */
    private int f7995p;

    /* JADX INFO: renamed from: q, reason: collision with root package name */
    private boolean f7996q;

    /* JADX INFO: renamed from: r, reason: collision with root package name */
    private boolean f7997r;

    /* JADX INFO: renamed from: s, reason: collision with root package name */
    private boolean f7998s;

    /* JADX INFO: renamed from: t, reason: collision with root package name */
    private R1.p f7999t;

    /* JADX INFO: renamed from: u, reason: collision with root package name */
    private Spannable f8000u;

    class a implements Comparator {
        a() {
        }

        @Override // java.util.Comparator
        public int compare(Object obj, Object obj2) {
            return ((WritableMap) obj).getInt("index") - ((WritableMap) obj2).getInt("index");
        }
    }

    public m(Context context) {
        super(context);
        this.f7999t = R1.p.f2096c;
        u();
    }

    private ReactContext getReactContext() {
        Context context = getContext();
        return context instanceof e0 ? (ReactContext) ((e0) context).getBaseContext() : (ReactContext) context;
    }

    private void t() {
        if (!Float.isNaN(this.f7992m)) {
            setTextSize(0, this.f7992m);
        }
        if (Float.isNaN(this.f7994o)) {
            return;
        }
        super.setLetterSpacing(this.f7994o);
    }

    private void u() {
        this.f7989j = Integer.MAX_VALUE;
        this.f7991l = false;
        this.f7995p = 0;
        this.f7996q = false;
        this.f7997r = false;
        this.f7998s = false;
        this.f7990k = TextUtils.TruncateAt.END;
        this.f7992m = Float.NaN;
        this.f7993n = Float.NaN;
        this.f7994o = 0.0f;
        this.f7999t = R1.p.f2096c;
        this.f8000u = null;
    }

    private static WritableMap v(int i3, int i4, int i5, int i6, int i7, int i8) {
        WritableMap writableMapCreateMap = Arguments.createMap();
        if (i3 == 8) {
            writableMapCreateMap.putString("visibility", "gone");
            writableMapCreateMap.putInt("index", i4);
        } else if (i3 == 0) {
            writableMapCreateMap.putString("visibility", "visible");
            writableMapCreateMap.putInt("index", i4);
            writableMapCreateMap.putDouble("left", C0429f0.f(i5));
            writableMapCreateMap.putDouble("top", C0429f0.f(i6));
            writableMapCreateMap.putDouble("right", C0429f0.f(i7));
            writableMapCreateMap.putDouble("bottom", C0429f0.f(i8));
        } else {
            writableMapCreateMap.putString("visibility", "unknown");
            writableMapCreateMap.putInt("index", i4);
        }
        return writableMapCreateMap;
    }

    @Override // com.facebook.react.uimanager.InterfaceC0439k0
    public int c(float f3, float f4) {
        int i3;
        CharSequence text = getText();
        int id = getId();
        int i4 = (int) f3;
        int i5 = (int) f4;
        Layout layout = getLayout();
        if (layout == null) {
            return id;
        }
        int lineForVertical = layout.getLineForVertical(i5);
        int lineLeft = (int) layout.getLineLeft(lineForVertical);
        int lineRight = (int) layout.getLineRight(lineForVertical);
        if ((text instanceof Spanned) && i4 >= lineLeft && i4 <= lineRight) {
            Spanned spanned = (Spanned) text;
            try {
                int offsetForHorizontal = layout.getOffsetForHorizontal(lineForVertical, i4);
                Z1.k[] kVarArr = (Z1.k[]) spanned.getSpans(offsetForHorizontal, offsetForHorizontal, Z1.k.class);
                if (kVarArr != null) {
                    int length = text.length();
                    for (int i6 = 0; i6 < kVarArr.length; i6++) {
                        int spanStart = spanned.getSpanStart(kVarArr[i6]);
                        int spanEnd = spanned.getSpanEnd(kVarArr[i6]);
                        if (spanEnd >= offsetForHorizontal && (i3 = spanEnd - spanStart) <= length) {
                            id = kVarArr[i6].a();
                            length = i3;
                        }
                    }
                }
            } catch (ArrayIndexOutOfBoundsException e4) {
                Y.a.m("ReactNative", "Crash in HorizontalMeasurementProvider: " + e4.getMessage());
            }
        }
        return id;
    }

    @Override // android.view.View
    protected boolean dispatchHoverEvent(MotionEvent motionEvent) {
        if (Z.C(this)) {
            C0237a c0237aI = Z.i(this);
            if (c0237aI instanceof AbstractC0766a) {
                return ((AbstractC0766a) c0237aI).v(motionEvent) || super.dispatchHoverEvent(motionEvent);
            }
        }
        return super.dispatchHoverEvent(motionEvent);
    }

    @Override // android.view.View
    public boolean dispatchKeyEvent(KeyEvent keyEvent) {
        C0237a c0237aI = Z.i(this);
        return (c0237aI != null && (c0237aI instanceof n) && ((n) c0237aI).w(keyEvent)) || super.dispatchKeyEvent(keyEvent);
    }

    int getGravityHorizontal() {
        return getGravity() & 8388615;
    }

    public Spannable getSpanned() {
        return this.f8000u;
    }

    @Override // android.widget.TextView, android.view.View
    public boolean hasOverlappingRendering() {
        return false;
    }

    @Override // android.widget.TextView, android.view.View, android.graphics.drawable.Drawable.Callback
    public void invalidateDrawable(Drawable drawable) {
        if (this.f7988i && (getText() instanceof Spanned)) {
            Spanned spanned = (Spanned) getText();
            for (Z1.p pVar : (Z1.p[]) spanned.getSpans(0, spanned.length(), Z1.p.class)) {
                if (pVar.a() == drawable) {
                    invalidate();
                }
            }
        }
        super.invalidateDrawable(drawable);
    }

    @Override // android.widget.TextView, android.view.View
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        setTextIsSelectable(this.f7997r);
        if (this.f7988i && (getText() instanceof Spanned)) {
            Spanned spanned = (Spanned) getText();
            for (Z1.p pVar : (Z1.p[]) spanned.getSpans(0, spanned.length(), Z1.p.class)) {
                pVar.c();
            }
        }
    }

    @Override // androidx.appcompat.widget.D, android.view.View
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (this.f7988i && (getText() instanceof Spanned)) {
            Spanned spanned = (Spanned) getText();
            for (Z1.p pVar : (Z1.p[]) spanned.getSpans(0, spanned.length(), Z1.p.class)) {
                pVar.d();
            }
        }
    }

    @Override // android.widget.TextView, android.view.View
    protected void onDraw(Canvas canvas) {
        C0652c c0652c = new C0652c("ReactTextView.onDraw");
        try {
            if (this.f7991l && getSpanned() != null && this.f7998s) {
                this.f7998s = false;
                Spannable spanned = getSpanned();
                float width = getWidth();
                com.facebook.yoga.p pVar = com.facebook.yoga.p.EXACTLY;
                t.a(spanned, width, pVar, getHeight(), pVar, this.f7993n, this.f7989j, getIncludeFontPadding(), getBreakStrategy(), getHyphenationFrequency(), Layout.Alignment.ALIGN_NORMAL, Build.VERSION.SDK_INT < 26 ? -1 : getJustificationMode(), getPaint());
                setText(getSpanned());
            }
            if (this.f7999t != R1.p.f2096c) {
                C0418a.a(this, canvas);
            }
            super.onDraw(canvas);
            c0652c.close();
        } finally {
        }
    }

    @Override // android.view.View
    public void onFinishTemporaryDetach() {
        super.onFinishTemporaryDetach();
        if (this.f7988i && (getText() instanceof Spanned)) {
            Spanned spanned = (Spanned) getText();
            for (Z1.p pVar : (Z1.p[]) spanned.getSpans(0, spanned.length(), Z1.p.class)) {
                pVar.e();
            }
        }
    }

    @Override // android.widget.TextView, android.view.View
    public final void onFocusChanged(boolean z3, int i3, Rect rect) {
        super.onFocusChanged(z3, i3, rect);
        C0237a c0237aI = Z.i(this);
        if (c0237aI == null || !(c0237aI instanceof n)) {
            return;
        }
        ((n) c0237aI).G(z3, i3, rect);
    }

    /* JADX WARN: Removed duplicated region for block: B:41:0x00dd  */
    /* JADX WARN: Removed duplicated region for block: B:42:0x00e1  */
    /* JADX WARN: Removed duplicated region for block: B:54:0x0107  */
    /* JADX WARN: Removed duplicated region for block: B:56:0x010d  */
    /* JADX WARN: Removed duplicated region for block: B:59:0x0122 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:62:0x0127  */
    /* JADX WARN: Removed duplicated region for block: B:65:0x0137  */
    /* JADX WARN: Removed duplicated region for block: B:82:0x015f A[SYNTHETIC] */
    @Override // androidx.appcompat.widget.D, android.widget.TextView, android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    protected void onLayout(boolean r24, int r25, int r26, int r27, int r28) {
        /*
            Method dump skipped, instruction units count: 421
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.facebook.react.views.text.m.onLayout(boolean, int, int, int, int):void");
    }

    @Override // androidx.appcompat.widget.D, android.widget.TextView, android.view.View
    protected void onMeasure(int i3, int i4) {
        C0652c c0652c = new C0652c("ReactTextView.onMeasure");
        try {
            super.onMeasure(i3, i4);
            c0652c.close();
        } catch (Throwable th) {
            try {
                c0652c.close();
            } catch (Throwable th2) {
                th.addSuppressed(th2);
            }
            throw th;
        }
    }

    @Override // android.view.View
    public void onStartTemporaryDetach() {
        super.onStartTemporaryDetach();
        if (this.f7988i && (getText() instanceof Spanned)) {
            Spanned spanned = (Spanned) getText();
            for (Z1.p pVar : (Z1.p[]) spanned.getSpans(0, spanned.length(), Z1.p.class)) {
                pVar.f();
            }
        }
    }

    public void setAdjustFontSizeToFit(boolean z3) {
        this.f7991l = z3;
    }

    @Override // android.view.View
    public void setBackgroundColor(int i3) {
        C0418a.n(this, Integer.valueOf(i3));
    }

    public void setBorderRadius(float f3) {
        x(f3, R1.d.f1999b.ordinal());
    }

    public void setBorderStyle(String str) {
        C0418a.r(this, str == null ? null : R1.f.b(str));
    }

    @Override // android.widget.TextView
    public void setBreakStrategy(int i3) {
        super.setBreakStrategy(i3);
        this.f7998s = true;
    }

    public void setEllipsizeLocation(TextUtils.TruncateAt truncateAt) {
        this.f7990k = truncateAt;
    }

    public void setFontSize(float f3) {
        this.f7992m = (float) (this.f7991l ? Math.ceil(C0429f0.j(f3)) : Math.ceil(C0429f0.h(f3)));
        t();
    }

    void setGravityHorizontal(int i3) {
        if (i3 == 0) {
            i3 = 8388611;
        }
        setGravity(i3 | (getGravity() & (-8388616)));
    }

    void setGravityVertical(int i3) {
        if (i3 == 0) {
            i3 = 48;
        }
        setGravity(i3 | (getGravity() & (-113)));
    }

    @Override // android.widget.TextView
    public void setHyphenationFrequency(int i3) {
        super.setHyphenationFrequency(i3);
        this.f7998s = true;
    }

    @Override // android.widget.TextView
    public void setIncludeFontPadding(boolean z3) {
        super.setIncludeFontPadding(z3);
        this.f7998s = true;
    }

    @Override // android.widget.TextView
    public void setLetterSpacing(float f3) {
        if (Float.isNaN(f3)) {
            return;
        }
        this.f7994o = C0429f0.h(f3) / this.f7992m;
        t();
    }

    public void setLinkifyMask(int i3) {
        this.f7995p = i3;
    }

    public void setMinimumFontSize(float f3) {
        this.f7993n = f3;
        this.f7998s = true;
    }

    public void setNotifyOnInlineViewLayout(boolean z3) {
        this.f7996q = z3;
    }

    public void setNumberOfLines(int i3) {
        if (i3 == 0) {
            i3 = Integer.MAX_VALUE;
        }
        this.f7989j = i3;
        setMaxLines(i3);
        this.f7998s = true;
    }

    public void setOverflow(String str) {
        if (str == null) {
            this.f7999t = R1.p.f2096c;
        } else {
            R1.p pVarB = R1.p.b(str);
            if (pVarB == null) {
                pVarB = R1.p.f2096c;
            }
            this.f7999t = pVarB;
        }
        invalidate();
    }

    public void setSpanned(Spannable spannable) {
        this.f8000u = spannable;
        this.f7998s = true;
    }

    public void setText(i iVar) {
        C0652c c0652c = new C0652c("ReactTextView.setText(ReactTextUpdate)");
        try {
            this.f7988i = iVar.b();
            if (getLayoutParams() == null) {
                setLayoutParams(f7987v);
            }
            Spannable spannableI = iVar.i();
            int i3 = this.f7995p;
            if (i3 > 0) {
                Linkify.addLinks(spannableI, i3);
                setMovementMethod(LinkMovementMethod.getInstance());
            }
            setText(spannableI);
            float f3 = iVar.f();
            float fH = iVar.h();
            float fG = iVar.g();
            float fE = iVar.e();
            if (f3 != -1.0f && fH != -1.0f && fG != -1.0f && fE != -1.0f) {
                setPadding((int) Math.floor(f3), (int) Math.floor(fH), (int) Math.floor(fG), (int) Math.floor(fE));
            }
            int iJ = iVar.j();
            if (iJ != getGravityHorizontal()) {
                setGravityHorizontal(iJ);
            }
            if (getBreakStrategy() != iVar.k()) {
                setBreakStrategy(iVar.k());
            }
            if (Build.VERSION.SDK_INT >= 26 && getJustificationMode() != iVar.d()) {
                setJustificationMode(iVar.d());
            }
            requestLayout();
            c0652c.close();
        } catch (Throwable th) {
            try {
                c0652c.close();
            } catch (Throwable th2) {
                th.addSuppressed(th2);
            }
            throw th;
        }
    }

    @Override // android.widget.TextView
    public void setTextIsSelectable(boolean z3) {
        this.f7997r = z3;
        super.setTextIsSelectable(z3);
    }

    @Override // android.widget.TextView, android.view.View
    protected boolean verifyDrawable(Drawable drawable) {
        if (this.f7988i && (getText() instanceof Spanned)) {
            Spanned spanned = (Spanned) getText();
            for (Z1.p pVar : (Z1.p[]) spanned.getSpans(0, spanned.length(), Z1.p.class)) {
                if (pVar.a() == drawable) {
                    return true;
                }
            }
        }
        return super.verifyDrawable(drawable);
    }

    void w() {
        u();
        C0418a.m(this);
        setBreakStrategy(0);
        setMovementMethod(getDefaultMovementMethod());
        int i3 = Build.VERSION.SDK_INT;
        if (i3 >= 26) {
            setJustificationMode(0);
        }
        setLayoutParams(f7987v);
        super.setText((CharSequence) null);
        t();
        setGravity(8388659);
        setNumberOfLines(this.f7989j);
        setAdjustFontSizeToFit(this.f7991l);
        setLinkifyMask(this.f7995p);
        setTextIsSelectable(this.f7997r);
        setIncludeFontPadding(true);
        setEnabled(true);
        setLinkifyMask(0);
        setEllipsizeLocation(this.f7990k);
        setEnabled(true);
        if (i3 >= 26) {
            setFocusable(16);
        }
        setHyphenationFrequency(0);
        y();
    }

    public void x(float f3, int i3) {
        C0418a.q(this, R1.d.values()[i3], Float.isNaN(f3) ? null : new W(C0429f0.f(f3), X.f7408b));
    }

    public void y() {
        setEllipsize((this.f7989j == Integer.MAX_VALUE || this.f7991l) ? null : this.f7990k);
    }
}
