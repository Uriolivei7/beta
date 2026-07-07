package androidx.appcompat.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import androidx.core.content.res.f;
import e.AbstractC0521a;

/* JADX INFO: loaded from: classes.dex */
public class h0 {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final Context f4227a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final TypedArray f4228b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private TypedValue f4229c;

    private h0(Context context, TypedArray typedArray) {
        this.f4227a = context;
        this.f4228b = typedArray;
    }

    public static h0 s(Context context, int i3, int[] iArr) {
        return new h0(context, context.obtainStyledAttributes(i3, iArr));
    }

    public static h0 t(Context context, AttributeSet attributeSet, int[] iArr) {
        return new h0(context, context.obtainStyledAttributes(attributeSet, iArr));
    }

    public static h0 u(Context context, AttributeSet attributeSet, int[] iArr, int i3, int i4) {
        return new h0(context, context.obtainStyledAttributes(attributeSet, iArr, i3, i4));
    }

    public boolean a(int i3, boolean z3) {
        return this.f4228b.getBoolean(i3, z3);
    }

    public int b(int i3, int i4) {
        return this.f4228b.getColor(i3, i4);
    }

    public ColorStateList c(int i3) {
        int resourceId;
        ColorStateList colorStateListA;
        return (!this.f4228b.hasValue(i3) || (resourceId = this.f4228b.getResourceId(i3, 0)) == 0 || (colorStateListA = AbstractC0521a.a(this.f4227a, resourceId)) == null) ? this.f4228b.getColorStateList(i3) : colorStateListA;
    }

    public int d(int i3, int i4) {
        return this.f4228b.getDimensionPixelOffset(i3, i4);
    }

    public int e(int i3, int i4) {
        return this.f4228b.getDimensionPixelSize(i3, i4);
    }

    public Drawable f(int i3) {
        int resourceId;
        return (!this.f4228b.hasValue(i3) || (resourceId = this.f4228b.getResourceId(i3, 0)) == 0) ? this.f4228b.getDrawable(i3) : AbstractC0521a.b(this.f4227a, resourceId);
    }

    public Drawable g(int i3) {
        int resourceId;
        if (!this.f4228b.hasValue(i3) || (resourceId = this.f4228b.getResourceId(i3, 0)) == 0) {
            return null;
        }
        return C0222k.b().d(this.f4227a, resourceId, true);
    }

    public float h(int i3, float f3) {
        return this.f4228b.getFloat(i3, f3);
    }

    public Typeface i(int i3, int i4, f.e eVar) {
        int resourceId = this.f4228b.getResourceId(i3, 0);
        if (resourceId == 0) {
            return null;
        }
        if (this.f4229c == null) {
            this.f4229c = new TypedValue();
        }
        return androidx.core.content.res.f.g(this.f4227a, resourceId, this.f4229c, i4, eVar);
    }

    public int j(int i3, int i4) {
        return this.f4228b.getInt(i3, i4);
    }

    public int k(int i3, int i4) {
        return this.f4228b.getInteger(i3, i4);
    }

    public int l(int i3, int i4) {
        return this.f4228b.getLayoutDimension(i3, i4);
    }

    public int m(int i3, int i4) {
        return this.f4228b.getResourceId(i3, i4);
    }

    public String n(int i3) {
        return this.f4228b.getString(i3);
    }

    public CharSequence o(int i3) {
        return this.f4228b.getText(i3);
    }

    public CharSequence[] p(int i3) {
        return this.f4228b.getTextArray(i3);
    }

    public TypedArray q() {
        return this.f4228b;
    }

    public boolean r(int i3) {
        return this.f4228b.hasValue(i3);
    }

    public TypedValue v(int i3) {
        return this.f4228b.peekValue(i3);
    }

    public void w() {
        this.f4228b.recycle();
    }
}
