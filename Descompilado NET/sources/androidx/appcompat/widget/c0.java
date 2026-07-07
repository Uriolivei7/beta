package androidx.appcompat.widget;

import android.R;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.InputFilter;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.TransformationMethod;
import android.util.AttributeSet;
import android.util.Property;
import android.view.ActionMode;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.CompoundButton;
import androidx.emoji2.text.f;
import d.AbstractC0487a;
import e.AbstractC0521a;
import h.C0545a;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/* JADX INFO: loaded from: classes.dex */
public class c0 extends CompoundButton {

    /* JADX INFO: renamed from: T, reason: collision with root package name */
    private static final Property f4140T = new a(Float.class, "thumbPos");

    /* JADX INFO: renamed from: U, reason: collision with root package name */
    private static final int[] f4141U = {R.attr.state_checked};

    /* JADX INFO: renamed from: A, reason: collision with root package name */
    float f4142A;

    /* JADX INFO: renamed from: B, reason: collision with root package name */
    private int f4143B;

    /* JADX INFO: renamed from: C, reason: collision with root package name */
    private int f4144C;

    /* JADX INFO: renamed from: D, reason: collision with root package name */
    private int f4145D;

    /* JADX INFO: renamed from: E, reason: collision with root package name */
    private int f4146E;

    /* JADX INFO: renamed from: F, reason: collision with root package name */
    private int f4147F;

    /* JADX INFO: renamed from: G, reason: collision with root package name */
    private int f4148G;

    /* JADX INFO: renamed from: H, reason: collision with root package name */
    private int f4149H;

    /* JADX INFO: renamed from: I, reason: collision with root package name */
    private boolean f4150I;

    /* JADX INFO: renamed from: J, reason: collision with root package name */
    private final TextPaint f4151J;

    /* JADX INFO: renamed from: K, reason: collision with root package name */
    private ColorStateList f4152K;

    /* JADX INFO: renamed from: L, reason: collision with root package name */
    private Layout f4153L;

    /* JADX INFO: renamed from: M, reason: collision with root package name */
    private Layout f4154M;

    /* JADX INFO: renamed from: N, reason: collision with root package name */
    private TransformationMethod f4155N;

    /* JADX INFO: renamed from: O, reason: collision with root package name */
    ObjectAnimator f4156O;

    /* JADX INFO: renamed from: P, reason: collision with root package name */
    private final C f4157P;

    /* JADX INFO: renamed from: Q, reason: collision with root package name */
    private C0225n f4158Q;

    /* JADX INFO: renamed from: R, reason: collision with root package name */
    private b f4159R;

    /* JADX INFO: renamed from: S, reason: collision with root package name */
    private final Rect f4160S;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private Drawable f4161b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private ColorStateList f4162c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private PorterDuff.Mode f4163d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private boolean f4164e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private boolean f4165f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private Drawable f4166g;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private ColorStateList f4167h;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    private PorterDuff.Mode f4168i;

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    private boolean f4169j;

    /* JADX INFO: renamed from: k, reason: collision with root package name */
    private boolean f4170k;

    /* JADX INFO: renamed from: l, reason: collision with root package name */
    private int f4171l;

    /* JADX INFO: renamed from: m, reason: collision with root package name */
    private int f4172m;

    /* JADX INFO: renamed from: n, reason: collision with root package name */
    private int f4173n;

    /* JADX INFO: renamed from: o, reason: collision with root package name */
    private boolean f4174o;

    /* JADX INFO: renamed from: p, reason: collision with root package name */
    private CharSequence f4175p;

    /* JADX INFO: renamed from: q, reason: collision with root package name */
    private CharSequence f4176q;

    /* JADX INFO: renamed from: r, reason: collision with root package name */
    private CharSequence f4177r;

    /* JADX INFO: renamed from: s, reason: collision with root package name */
    private CharSequence f4178s;

    /* JADX INFO: renamed from: t, reason: collision with root package name */
    private boolean f4179t;

    /* JADX INFO: renamed from: u, reason: collision with root package name */
    private int f4180u;

    /* JADX INFO: renamed from: v, reason: collision with root package name */
    private int f4181v;

    /* JADX INFO: renamed from: w, reason: collision with root package name */
    private float f4182w;

    /* JADX INFO: renamed from: x, reason: collision with root package name */
    private float f4183x;

    /* JADX INFO: renamed from: y, reason: collision with root package name */
    private VelocityTracker f4184y;

    /* JADX INFO: renamed from: z, reason: collision with root package name */
    private int f4185z;

    class a extends Property {
        a(Class cls, String str) {
            super(cls, str);
        }

        @Override // android.util.Property
        /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
        public Float get(c0 c0Var) {
            return Float.valueOf(c0Var.f4142A);
        }

        @Override // android.util.Property
        /* JADX INFO: renamed from: b, reason: merged with bridge method [inline-methods] */
        public void set(c0 c0Var, Float f3) {
            c0Var.setThumbPosition(f3.floatValue());
        }
    }

    static class b extends f.AbstractC0072f {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        private final Reference f4186a;

        b(c0 c0Var) {
            this.f4186a = new WeakReference(c0Var);
        }

        @Override // androidx.emoji2.text.f.AbstractC0072f
        public void a(Throwable th) {
            c0 c0Var = (c0) this.f4186a.get();
            if (c0Var != null) {
                c0Var.j();
            }
        }

        @Override // androidx.emoji2.text.f.AbstractC0072f
        public void b() {
            c0 c0Var = (c0) this.f4186a.get();
            if (c0Var != null) {
                c0Var.j();
            }
        }
    }

    public c0(Context context) {
        this(context, null);
    }

    private void a(boolean z3) {
        ObjectAnimator objectAnimatorOfFloat = ObjectAnimator.ofFloat(this, (Property<c0, Float>) f4140T, z3 ? 1.0f : 0.0f);
        this.f4156O = objectAnimatorOfFloat;
        objectAnimatorOfFloat.setDuration(250L);
        this.f4156O.setAutoCancel(true);
        this.f4156O.start();
    }

    private void b() {
        Drawable drawable = this.f4161b;
        if (drawable != null) {
            if (this.f4164e || this.f4165f) {
                Drawable drawableMutate = androidx.core.graphics.drawable.a.j(drawable).mutate();
                this.f4161b = drawableMutate;
                if (this.f4164e) {
                    androidx.core.graphics.drawable.a.g(drawableMutate, this.f4162c);
                }
                if (this.f4165f) {
                    androidx.core.graphics.drawable.a.h(this.f4161b, this.f4163d);
                }
                if (this.f4161b.isStateful()) {
                    this.f4161b.setState(getDrawableState());
                }
            }
        }
    }

    private void c() {
        Drawable drawable = this.f4166g;
        if (drawable != null) {
            if (this.f4169j || this.f4170k) {
                Drawable drawableMutate = androidx.core.graphics.drawable.a.j(drawable).mutate();
                this.f4166g = drawableMutate;
                if (this.f4169j) {
                    androidx.core.graphics.drawable.a.g(drawableMutate, this.f4167h);
                }
                if (this.f4170k) {
                    androidx.core.graphics.drawable.a.h(this.f4166g, this.f4168i);
                }
                if (this.f4166g.isStateful()) {
                    this.f4166g.setState(getDrawableState());
                }
            }
        }
    }

    private void d() {
        ObjectAnimator objectAnimator = this.f4156O;
        if (objectAnimator != null) {
            objectAnimator.cancel();
        }
    }

    private void e(MotionEvent motionEvent) {
        MotionEvent motionEventObtain = MotionEvent.obtain(motionEvent);
        motionEventObtain.setAction(3);
        super.onTouchEvent(motionEventObtain);
        motionEventObtain.recycle();
    }

    private static float f(float f3, float f4, float f5) {
        return f3 < f4 ? f4 : f3 > f5 ? f5 : f3;
    }

    private CharSequence g(CharSequence charSequence) {
        TransformationMethod transformationMethodF = getEmojiTextViewHelper().f(this.f4155N);
        return transformationMethodF != null ? transformationMethodF.getTransformation(charSequence, this) : charSequence;
    }

    private C0225n getEmojiTextViewHelper() {
        if (this.f4158Q == null) {
            this.f4158Q = new C0225n(this);
        }
        return this.f4158Q;
    }

    private boolean getTargetCheckedState() {
        return this.f4142A > 0.5f;
    }

    private int getThumbOffset() {
        return (int) (((s0.b(this) ? 1.0f - this.f4142A : this.f4142A) * getThumbScrollRange()) + 0.5f);
    }

    private int getThumbScrollRange() {
        Drawable drawable = this.f4166g;
        if (drawable == null) {
            return 0;
        }
        Rect rect = this.f4160S;
        drawable.getPadding(rect);
        Drawable drawable2 = this.f4161b;
        Rect rectC = drawable2 != null ? O.c(drawable2) : O.f3804c;
        return ((((this.f4143B - this.f4145D) - rect.left) - rect.right) - rectC.left) - rectC.right;
    }

    private boolean h(float f3, float f4) {
        if (this.f4161b == null) {
            return false;
        }
        int thumbOffset = getThumbOffset();
        this.f4161b.getPadding(this.f4160S);
        int i3 = this.f4147F;
        int i4 = this.f4181v;
        int i5 = i3 - i4;
        int i6 = (this.f4146E + thumbOffset) - i4;
        int i7 = this.f4145D + i6;
        Rect rect = this.f4160S;
        return f3 > ((float) i6) && f3 < ((float) (((i7 + rect.left) + rect.right) + i4)) && f4 > ((float) i5) && f4 < ((float) (this.f4149H + i4));
    }

    private Layout i(CharSequence charSequence) {
        return new StaticLayout(charSequence, this.f4151J, charSequence != null ? (int) Math.ceil(Layout.getDesiredWidth(charSequence, r2)) : 0, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, true);
    }

    private void k() {
        if (Build.VERSION.SDK_INT >= 30) {
            CharSequence string = this.f4177r;
            if (string == null) {
                string = getResources().getString(d.h.f8831b);
            }
            androidx.core.view.Z.l0(this, string);
        }
    }

    private void l() {
        if (Build.VERSION.SDK_INT >= 30) {
            CharSequence string = this.f4175p;
            if (string == null) {
                string = getResources().getString(d.h.f8832c);
            }
            androidx.core.view.Z.l0(this, string);
        }
    }

    private void o(int i3, int i4) {
        n(i3 != 1 ? i3 != 2 ? i3 != 3 ? null : Typeface.MONOSPACE : Typeface.SERIF : Typeface.SANS_SERIF, i4);
    }

    private void p() {
        if (this.f4159R == null && this.f4158Q.b() && androidx.emoji2.text.f.i()) {
            androidx.emoji2.text.f fVarC = androidx.emoji2.text.f.c();
            int iE = fVarC.e();
            if (iE == 3 || iE == 0) {
                b bVar = new b(this);
                this.f4159R = bVar;
                fVarC.t(bVar);
            }
        }
    }

    private void q(MotionEvent motionEvent) {
        this.f4180u = 0;
        boolean targetCheckedState = true;
        boolean z3 = motionEvent.getAction() == 1 && isEnabled();
        boolean zIsChecked = isChecked();
        if (z3) {
            this.f4184y.computeCurrentVelocity(1000);
            float xVelocity = this.f4184y.getXVelocity();
            if (Math.abs(xVelocity) <= this.f4185z) {
                targetCheckedState = getTargetCheckedState();
            } else if (!s0.b(this) ? xVelocity <= 0.0f : xVelocity >= 0.0f) {
                targetCheckedState = false;
            }
        } else {
            targetCheckedState = zIsChecked;
        }
        if (targetCheckedState != zIsChecked) {
            playSoundEffect(0);
        }
        setChecked(targetCheckedState);
        e(motionEvent);
    }

    private void setTextOffInternal(CharSequence charSequence) {
        this.f4177r = charSequence;
        this.f4178s = g(charSequence);
        this.f4154M = null;
        if (this.f4179t) {
            p();
        }
    }

    private void setTextOnInternal(CharSequence charSequence) {
        this.f4175p = charSequence;
        this.f4176q = g(charSequence);
        this.f4153L = null;
        if (this.f4179t) {
            p();
        }
    }

    @Override // android.view.View
    public void draw(Canvas canvas) {
        int i3;
        int i4;
        Rect rect = this.f4160S;
        int i5 = this.f4146E;
        int i6 = this.f4147F;
        int i7 = this.f4148G;
        int i8 = this.f4149H;
        int thumbOffset = getThumbOffset() + i5;
        Drawable drawable = this.f4161b;
        Rect rectC = drawable != null ? O.c(drawable) : O.f3804c;
        Drawable drawable2 = this.f4166g;
        if (drawable2 != null) {
            drawable2.getPadding(rect);
            int i9 = rect.left;
            thumbOffset += i9;
            if (rectC != null) {
                int i10 = rectC.left;
                if (i10 > i9) {
                    i5 += i10 - i9;
                }
                int i11 = rectC.top;
                int i12 = rect.top;
                i3 = i11 > i12 ? (i11 - i12) + i6 : i6;
                int i13 = rectC.right;
                int i14 = rect.right;
                if (i13 > i14) {
                    i7 -= i13 - i14;
                }
                int i15 = rectC.bottom;
                int i16 = rect.bottom;
                if (i15 > i16) {
                    i4 = i8 - (i15 - i16);
                }
                this.f4166g.setBounds(i5, i3, i7, i4);
            } else {
                i3 = i6;
            }
            i4 = i8;
            this.f4166g.setBounds(i5, i3, i7, i4);
        }
        Drawable drawable3 = this.f4161b;
        if (drawable3 != null) {
            drawable3.getPadding(rect);
            int i17 = thumbOffset - rect.left;
            int i18 = thumbOffset + this.f4145D + rect.right;
            this.f4161b.setBounds(i17, i6, i18, i8);
            Drawable background = getBackground();
            if (background != null) {
                androidx.core.graphics.drawable.a.d(background, i17, i6, i18, i8);
            }
        }
        super.draw(canvas);
    }

    @Override // android.widget.CompoundButton, android.widget.TextView, android.view.View
    public void drawableHotspotChanged(float f3, float f4) {
        super.drawableHotspotChanged(f3, f4);
        Drawable drawable = this.f4161b;
        if (drawable != null) {
            androidx.core.graphics.drawable.a.c(drawable, f3, f4);
        }
        Drawable drawable2 = this.f4166g;
        if (drawable2 != null) {
            androidx.core.graphics.drawable.a.c(drawable2, f3, f4);
        }
    }

    @Override // android.widget.CompoundButton, android.widget.TextView, android.view.View
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        int[] drawableState = getDrawableState();
        Drawable drawable = this.f4161b;
        boolean state = (drawable == null || !drawable.isStateful()) ? false : drawable.setState(drawableState);
        Drawable drawable2 = this.f4166g;
        if (drawable2 != null && drawable2.isStateful()) {
            state |= drawable2.setState(drawableState);
        }
        if (state) {
            invalidate();
        }
    }

    @Override // android.widget.CompoundButton, android.widget.TextView
    public int getCompoundPaddingLeft() {
        if (!s0.b(this)) {
            return super.getCompoundPaddingLeft();
        }
        int compoundPaddingLeft = super.getCompoundPaddingLeft() + this.f4143B;
        return !TextUtils.isEmpty(getText()) ? compoundPaddingLeft + this.f4173n : compoundPaddingLeft;
    }

    @Override // android.widget.CompoundButton, android.widget.TextView
    public int getCompoundPaddingRight() {
        if (s0.b(this)) {
            return super.getCompoundPaddingRight();
        }
        int compoundPaddingRight = super.getCompoundPaddingRight() + this.f4143B;
        return !TextUtils.isEmpty(getText()) ? compoundPaddingRight + this.f4173n : compoundPaddingRight;
    }

    @Override // android.widget.TextView
    public ActionMode.Callback getCustomSelectionActionModeCallback() {
        return androidx.core.widget.i.n(super.getCustomSelectionActionModeCallback());
    }

    public boolean getShowText() {
        return this.f4179t;
    }

    public boolean getSplitTrack() {
        return this.f4174o;
    }

    public int getSwitchMinWidth() {
        return this.f4172m;
    }

    public int getSwitchPadding() {
        return this.f4173n;
    }

    public CharSequence getTextOff() {
        return this.f4177r;
    }

    public CharSequence getTextOn() {
        return this.f4175p;
    }

    public Drawable getThumbDrawable() {
        return this.f4161b;
    }

    protected final float getThumbPosition() {
        return this.f4142A;
    }

    public int getThumbTextPadding() {
        return this.f4171l;
    }

    public ColorStateList getThumbTintList() {
        return this.f4162c;
    }

    public PorterDuff.Mode getThumbTintMode() {
        return this.f4163d;
    }

    public Drawable getTrackDrawable() {
        return this.f4166g;
    }

    public ColorStateList getTrackTintList() {
        return this.f4167h;
    }

    public PorterDuff.Mode getTrackTintMode() {
        return this.f4168i;
    }

    void j() {
        setTextOnInternal(this.f4175p);
        setTextOffInternal(this.f4177r);
        requestLayout();
    }

    @Override // android.widget.CompoundButton, android.widget.TextView, android.view.View
    public void jumpDrawablesToCurrentState() {
        super.jumpDrawablesToCurrentState();
        Drawable drawable = this.f4161b;
        if (drawable != null) {
            drawable.jumpToCurrentState();
        }
        Drawable drawable2 = this.f4166g;
        if (drawable2 != null) {
            drawable2.jumpToCurrentState();
        }
        ObjectAnimator objectAnimator = this.f4156O;
        if (objectAnimator == null || !objectAnimator.isStarted()) {
            return;
        }
        this.f4156O.end();
        this.f4156O = null;
    }

    public void m(Context context, int i3) {
        h0 h0VarS = h0.s(context, i3, d.j.f8923S2);
        ColorStateList colorStateListC = h0VarS.c(d.j.f8939W2);
        if (colorStateListC != null) {
            this.f4152K = colorStateListC;
        } else {
            this.f4152K = getTextColors();
        }
        int iE = h0VarS.e(d.j.f8927T2, 0);
        if (iE != 0) {
            float f3 = iE;
            if (f3 != this.f4151J.getTextSize()) {
                this.f4151J.setTextSize(f3);
                requestLayout();
            }
        }
        o(h0VarS.j(d.j.f8931U2, -1), h0VarS.j(d.j.f8935V2, -1));
        if (h0VarS.a(d.j.f8961b3, false)) {
            this.f4155N = new C0545a(getContext());
        } else {
            this.f4155N = null;
        }
        setTextOnInternal(this.f4175p);
        setTextOffInternal(this.f4177r);
        h0VarS.w();
    }

    public void n(Typeface typeface, int i3) {
        if (i3 <= 0) {
            this.f4151J.setFakeBoldText(false);
            this.f4151J.setTextSkewX(0.0f);
            setSwitchTypeface(typeface);
        } else {
            Typeface typefaceDefaultFromStyle = typeface == null ? Typeface.defaultFromStyle(i3) : Typeface.create(typeface, i3);
            setSwitchTypeface(typefaceDefaultFromStyle);
            int i4 = (~(typefaceDefaultFromStyle != null ? typefaceDefaultFromStyle.getStyle() : 0)) & i3;
            this.f4151J.setFakeBoldText((i4 & 1) != 0);
            this.f4151J.setTextSkewX((i4 & 2) != 0 ? -0.25f : 0.0f);
        }
    }

    @Override // android.widget.CompoundButton, android.widget.TextView, android.view.View
    protected int[] onCreateDrawableState(int i3) {
        int[] iArrOnCreateDrawableState = super.onCreateDrawableState(i3 + 1);
        if (isChecked()) {
            View.mergeDrawableStates(iArrOnCreateDrawableState, f4141U);
        }
        return iArrOnCreateDrawableState;
    }

    @Override // android.widget.CompoundButton, android.widget.TextView, android.view.View
    protected void onDraw(Canvas canvas) {
        int width;
        super.onDraw(canvas);
        Rect rect = this.f4160S;
        Drawable drawable = this.f4166g;
        if (drawable != null) {
            drawable.getPadding(rect);
        } else {
            rect.setEmpty();
        }
        int i3 = this.f4147F;
        int i4 = this.f4149H;
        int i5 = i3 + rect.top;
        int i6 = i4 - rect.bottom;
        Drawable drawable2 = this.f4161b;
        if (drawable != null) {
            if (!this.f4174o || drawable2 == null) {
                drawable.draw(canvas);
            } else {
                Rect rectC = O.c(drawable2);
                drawable2.copyBounds(rect);
                rect.left += rectC.left;
                rect.right -= rectC.right;
                int iSave = canvas.save();
                canvas.clipRect(rect, Region.Op.DIFFERENCE);
                drawable.draw(canvas);
                canvas.restoreToCount(iSave);
            }
        }
        int iSave2 = canvas.save();
        if (drawable2 != null) {
            drawable2.draw(canvas);
        }
        Layout layout = getTargetCheckedState() ? this.f4153L : this.f4154M;
        if (layout != null) {
            int[] drawableState = getDrawableState();
            ColorStateList colorStateList = this.f4152K;
            if (colorStateList != null) {
                this.f4151J.setColor(colorStateList.getColorForState(drawableState, 0));
            }
            this.f4151J.drawableState = drawableState;
            if (drawable2 != null) {
                Rect bounds = drawable2.getBounds();
                width = bounds.left + bounds.right;
            } else {
                width = getWidth();
            }
            canvas.translate((width / 2) - (layout.getWidth() / 2), ((i5 + i6) / 2) - (layout.getHeight() / 2));
            layout.draw(canvas);
        }
        canvas.restoreToCount(iSave2);
    }

    @Override // android.view.View
    public void onInitializeAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        super.onInitializeAccessibilityEvent(accessibilityEvent);
        accessibilityEvent.setClassName("android.widget.Switch");
    }

    @Override // android.view.View
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        accessibilityNodeInfo.setClassName("android.widget.Switch");
        if (Build.VERSION.SDK_INT < 30) {
            CharSequence charSequence = isChecked() ? this.f4175p : this.f4177r;
            if (TextUtils.isEmpty(charSequence)) {
                return;
            }
            CharSequence text = accessibilityNodeInfo.getText();
            if (TextUtils.isEmpty(text)) {
                accessibilityNodeInfo.setText(charSequence);
                return;
            }
            StringBuilder sb = new StringBuilder();
            sb.append(text);
            sb.append(' ');
            sb.append(charSequence);
            accessibilityNodeInfo.setText(sb);
        }
    }

    @Override // android.widget.TextView, android.view.View
    protected void onLayout(boolean z3, int i3, int i4, int i5, int i6) {
        int iMax;
        int width;
        int paddingLeft;
        int i7;
        int paddingTop;
        int height;
        super.onLayout(z3, i3, i4, i5, i6);
        int iMax2 = 0;
        if (this.f4161b != null) {
            Rect rect = this.f4160S;
            Drawable drawable = this.f4166g;
            if (drawable != null) {
                drawable.getPadding(rect);
            } else {
                rect.setEmpty();
            }
            Rect rectC = O.c(this.f4161b);
            iMax = Math.max(0, rectC.left - rect.left);
            iMax2 = Math.max(0, rectC.right - rect.right);
        } else {
            iMax = 0;
        }
        if (s0.b(this)) {
            paddingLeft = getPaddingLeft() + iMax;
            width = ((this.f4143B + paddingLeft) - iMax) - iMax2;
        } else {
            width = (getWidth() - getPaddingRight()) - iMax2;
            paddingLeft = (width - this.f4143B) + iMax + iMax2;
        }
        int gravity = getGravity() & 112;
        if (gravity == 16) {
            int paddingTop2 = ((getPaddingTop() + getHeight()) - getPaddingBottom()) / 2;
            i7 = this.f4144C;
            paddingTop = paddingTop2 - (i7 / 2);
        } else {
            if (gravity == 80) {
                height = getHeight() - getPaddingBottom();
                paddingTop = height - this.f4144C;
                this.f4146E = paddingLeft;
                this.f4147F = paddingTop;
                this.f4149H = height;
                this.f4148G = width;
            }
            paddingTop = getPaddingTop();
            i7 = this.f4144C;
        }
        height = i7 + paddingTop;
        this.f4146E = paddingLeft;
        this.f4147F = paddingTop;
        this.f4149H = height;
        this.f4148G = width;
    }

    @Override // android.widget.TextView, android.view.View
    public void onMeasure(int i3, int i4) {
        int intrinsicWidth;
        int intrinsicHeight;
        if (this.f4179t) {
            if (this.f4153L == null) {
                this.f4153L = i(this.f4176q);
            }
            if (this.f4154M == null) {
                this.f4154M = i(this.f4178s);
            }
        }
        Rect rect = this.f4160S;
        Drawable drawable = this.f4161b;
        int intrinsicHeight2 = 0;
        if (drawable != null) {
            drawable.getPadding(rect);
            intrinsicWidth = (this.f4161b.getIntrinsicWidth() - rect.left) - rect.right;
            intrinsicHeight = this.f4161b.getIntrinsicHeight();
        } else {
            intrinsicWidth = 0;
            intrinsicHeight = 0;
        }
        this.f4145D = Math.max(this.f4179t ? Math.max(this.f4153L.getWidth(), this.f4154M.getWidth()) + (this.f4171l * 2) : 0, intrinsicWidth);
        Drawable drawable2 = this.f4166g;
        if (drawable2 != null) {
            drawable2.getPadding(rect);
            intrinsicHeight2 = this.f4166g.getIntrinsicHeight();
        } else {
            rect.setEmpty();
        }
        int iMax = rect.left;
        int iMax2 = rect.right;
        Drawable drawable3 = this.f4161b;
        if (drawable3 != null) {
            Rect rectC = O.c(drawable3);
            iMax = Math.max(iMax, rectC.left);
            iMax2 = Math.max(iMax2, rectC.right);
        }
        int iMax3 = this.f4150I ? Math.max(this.f4172m, (this.f4145D * 2) + iMax + iMax2) : this.f4172m;
        int iMax4 = Math.max(intrinsicHeight2, intrinsicHeight);
        this.f4143B = iMax3;
        this.f4144C = iMax4;
        super.onMeasure(i3, i4);
        if (getMeasuredHeight() < iMax4) {
            setMeasuredDimension(getMeasuredWidthAndState(), iMax4);
        }
    }

    @Override // android.view.View
    public void onPopulateAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        super.onPopulateAccessibilityEvent(accessibilityEvent);
        CharSequence charSequence = isChecked() ? this.f4175p : this.f4177r;
        if (charSequence != null) {
            accessibilityEvent.getText().add(charSequence);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:34:0x0089  */
    @Override // android.widget.TextView, android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public boolean onTouchEvent(android.view.MotionEvent r7) {
        /*
            r6 = this;
            android.view.VelocityTracker r0 = r6.f4184y
            r0.addMovement(r7)
            int r0 = r7.getActionMasked()
            r1 = 1
            if (r0 == 0) goto L9d
            r2 = 2
            if (r0 == r1) goto L89
            if (r0 == r2) goto L16
            r3 = 3
            if (r0 == r3) goto L89
            goto Lb7
        L16:
            int r0 = r6.f4180u
            if (r0 == r1) goto L55
            if (r0 == r2) goto L1e
            goto Lb7
        L1e:
            float r7 = r7.getX()
            int r0 = r6.getThumbScrollRange()
            float r2 = r6.f4182w
            float r2 = r7 - r2
            r3 = 1065353216(0x3f800000, float:1.0)
            r4 = 0
            if (r0 == 0) goto L32
            float r0 = (float) r0
            float r2 = r2 / r0
            goto L3b
        L32:
            int r0 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1))
            if (r0 <= 0) goto L38
            r2 = r3
            goto L3b
        L38:
            r0 = -1082130432(0xffffffffbf800000, float:-1.0)
            r2 = r0
        L3b:
            boolean r0 = androidx.appcompat.widget.s0.b(r6)
            if (r0 == 0) goto L42
            float r2 = -r2
        L42:
            float r0 = r6.f4142A
            float r0 = r0 + r2
            float r0 = f(r0, r4, r3)
            float r2 = r6.f4142A
            int r2 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1))
            if (r2 == 0) goto L54
            r6.f4182w = r7
            r6.setThumbPosition(r0)
        L54:
            return r1
        L55:
            float r0 = r7.getX()
            float r3 = r7.getY()
            float r4 = r6.f4182w
            float r4 = r0 - r4
            float r4 = java.lang.Math.abs(r4)
            int r5 = r6.f4181v
            float r5 = (float) r5
            int r4 = (r4 > r5 ? 1 : (r4 == r5 ? 0 : -1))
            if (r4 > 0) goto L7b
            float r4 = r6.f4183x
            float r4 = r3 - r4
            float r4 = java.lang.Math.abs(r4)
            int r5 = r6.f4181v
            float r5 = (float) r5
            int r4 = (r4 > r5 ? 1 : (r4 == r5 ? 0 : -1))
            if (r4 <= 0) goto Lb7
        L7b:
            r6.f4180u = r2
            android.view.ViewParent r7 = r6.getParent()
            r7.requestDisallowInterceptTouchEvent(r1)
            r6.f4182w = r0
            r6.f4183x = r3
            return r1
        L89:
            int r0 = r6.f4180u
            if (r0 != r2) goto L94
            r6.q(r7)
            super.onTouchEvent(r7)
            return r1
        L94:
            r0 = 0
            r6.f4180u = r0
            android.view.VelocityTracker r0 = r6.f4184y
            r0.clear()
            goto Lb7
        L9d:
            float r0 = r7.getX()
            float r2 = r7.getY()
            boolean r3 = r6.isEnabled()
            if (r3 == 0) goto Lb7
            boolean r3 = r6.h(r0, r2)
            if (r3 == 0) goto Lb7
            r6.f4180u = r1
            r6.f4182w = r0
            r6.f4183x = r2
        Lb7:
            boolean r7 = super.onTouchEvent(r7)
            return r7
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.appcompat.widget.c0.onTouchEvent(android.view.MotionEvent):boolean");
    }

    @Override // android.widget.TextView
    public void setAllCaps(boolean z3) {
        super.setAllCaps(z3);
        getEmojiTextViewHelper().d(z3);
    }

    @Override // android.widget.CompoundButton, android.widget.Checkable
    public void setChecked(boolean z3) {
        super.setChecked(z3);
        boolean zIsChecked = isChecked();
        if (zIsChecked) {
            l();
        } else {
            k();
        }
        if (getWindowToken() != null && isLaidOut()) {
            a(zIsChecked);
        } else {
            d();
            setThumbPosition(zIsChecked ? 1.0f : 0.0f);
        }
    }

    @Override // android.widget.TextView
    public void setCustomSelectionActionModeCallback(ActionMode.Callback callback) {
        super.setCustomSelectionActionModeCallback(androidx.core.widget.i.o(this, callback));
    }

    public void setEmojiCompatEnabled(boolean z3) {
        getEmojiTextViewHelper().e(z3);
        setTextOnInternal(this.f4175p);
        setTextOffInternal(this.f4177r);
        requestLayout();
    }

    protected final void setEnforceSwitchWidth(boolean z3) {
        this.f4150I = z3;
        invalidate();
    }

    @Override // android.widget.TextView
    public void setFilters(InputFilter[] inputFilterArr) {
        super.setFilters(getEmojiTextViewHelper().a(inputFilterArr));
    }

    public void setShowText(boolean z3) {
        if (this.f4179t != z3) {
            this.f4179t = z3;
            requestLayout();
            if (z3) {
                p();
            }
        }
    }

    public void setSplitTrack(boolean z3) {
        this.f4174o = z3;
        invalidate();
    }

    public void setSwitchMinWidth(int i3) {
        this.f4172m = i3;
        requestLayout();
    }

    public void setSwitchPadding(int i3) {
        this.f4173n = i3;
        requestLayout();
    }

    public void setSwitchTypeface(Typeface typeface) {
        if ((this.f4151J.getTypeface() == null || this.f4151J.getTypeface().equals(typeface)) && (this.f4151J.getTypeface() != null || typeface == null)) {
            return;
        }
        this.f4151J.setTypeface(typeface);
        requestLayout();
        invalidate();
    }

    public void setTextOff(CharSequence charSequence) {
        setTextOffInternal(charSequence);
        requestLayout();
        if (isChecked()) {
            return;
        }
        k();
    }

    public void setTextOn(CharSequence charSequence) {
        setTextOnInternal(charSequence);
        requestLayout();
        if (isChecked()) {
            l();
        }
    }

    public void setThumbDrawable(Drawable drawable) {
        Drawable drawable2 = this.f4161b;
        if (drawable2 != null) {
            drawable2.setCallback(null);
        }
        this.f4161b = drawable;
        if (drawable != null) {
            drawable.setCallback(this);
        }
        requestLayout();
    }

    void setThumbPosition(float f3) {
        this.f4142A = f3;
        invalidate();
    }

    public void setThumbResource(int i3) {
        setThumbDrawable(AbstractC0521a.b(getContext(), i3));
    }

    public void setThumbTextPadding(int i3) {
        this.f4171l = i3;
        requestLayout();
    }

    public void setThumbTintList(ColorStateList colorStateList) {
        this.f4162c = colorStateList;
        this.f4164e = true;
        b();
    }

    public void setThumbTintMode(PorterDuff.Mode mode) {
        this.f4163d = mode;
        this.f4165f = true;
        b();
    }

    public void setTrackDrawable(Drawable drawable) {
        Drawable drawable2 = this.f4166g;
        if (drawable2 != null) {
            drawable2.setCallback(null);
        }
        this.f4166g = drawable;
        if (drawable != null) {
            drawable.setCallback(this);
        }
        requestLayout();
    }

    public void setTrackResource(int i3) {
        setTrackDrawable(AbstractC0521a.b(getContext(), i3));
    }

    public void setTrackTintList(ColorStateList colorStateList) {
        this.f4167h = colorStateList;
        this.f4169j = true;
        c();
    }

    public void setTrackTintMode(PorterDuff.Mode mode) {
        this.f4168i = mode;
        this.f4170k = true;
        c();
    }

    @Override // android.widget.CompoundButton, android.widget.Checkable
    public void toggle() {
        setChecked(!isChecked());
    }

    @Override // android.widget.CompoundButton, android.widget.TextView, android.view.View
    protected boolean verifyDrawable(Drawable drawable) {
        return super.verifyDrawable(drawable) || drawable == this.f4161b || drawable == this.f4166g;
    }

    public c0(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, AbstractC0487a.f8669N);
    }

    public c0(Context context, AttributeSet attributeSet, int i3) {
        super(context, attributeSet, i3);
        this.f4162c = null;
        this.f4163d = null;
        this.f4164e = false;
        this.f4165f = false;
        this.f4167h = null;
        this.f4168i = null;
        this.f4169j = false;
        this.f4170k = false;
        this.f4184y = VelocityTracker.obtain();
        this.f4150I = true;
        this.f4160S = new Rect();
        d0.a(this, getContext());
        TextPaint textPaint = new TextPaint(1);
        this.f4151J = textPaint;
        textPaint.density = getResources().getDisplayMetrics().density;
        h0 h0VarU = h0.u(context, attributeSet, d.j.f8863D2, i3, 0);
        androidx.core.view.Z.V(this, context, d.j.f8863D2, attributeSet, h0VarU.q(), i3, 0);
        Drawable drawableF = h0VarU.f(d.j.f8875G2);
        this.f4161b = drawableF;
        if (drawableF != null) {
            drawableF.setCallback(this);
        }
        Drawable drawableF2 = h0VarU.f(d.j.f8911P2);
        this.f4166g = drawableF2;
        if (drawableF2 != null) {
            drawableF2.setCallback(this);
        }
        setTextOnInternal(h0VarU.o(d.j.f8867E2));
        setTextOffInternal(h0VarU.o(d.j.f8871F2));
        this.f4179t = h0VarU.a(d.j.f8879H2, true);
        this.f4171l = h0VarU.e(d.j.f8899M2, 0);
        this.f4172m = h0VarU.e(d.j.f8887J2, 0);
        this.f4173n = h0VarU.e(d.j.f8891K2, 0);
        this.f4174o = h0VarU.a(d.j.f8883I2, false);
        ColorStateList colorStateListC = h0VarU.c(d.j.f8903N2);
        if (colorStateListC != null) {
            this.f4162c = colorStateListC;
            this.f4164e = true;
        }
        PorterDuff.Mode modeD = O.d(h0VarU.j(d.j.f8907O2, -1), null);
        if (this.f4163d != modeD) {
            this.f4163d = modeD;
            this.f4165f = true;
        }
        if (this.f4164e || this.f4165f) {
            b();
        }
        ColorStateList colorStateListC2 = h0VarU.c(d.j.f8915Q2);
        if (colorStateListC2 != null) {
            this.f4167h = colorStateListC2;
            this.f4169j = true;
        }
        PorterDuff.Mode modeD2 = O.d(h0VarU.j(d.j.f8919R2, -1), null);
        if (this.f4168i != modeD2) {
            this.f4168i = modeD2;
            this.f4170k = true;
        }
        if (this.f4169j || this.f4170k) {
            c();
        }
        int iM = h0VarU.m(d.j.f8895L2, 0);
        if (iM != 0) {
            m(context, iM);
        }
        C c4 = new C(this);
        this.f4157P = c4;
        c4.m(attributeSet, i3);
        h0VarU.w();
        ViewConfiguration viewConfiguration = ViewConfiguration.get(context);
        this.f4181v = viewConfiguration.getScaledTouchSlop();
        this.f4185z = viewConfiguration.getScaledMinimumFlingVelocity();
        getEmojiTextViewHelper().c(attributeSet, i3);
        refreshDrawableState();
        setChecked(isChecked());
    }
}
