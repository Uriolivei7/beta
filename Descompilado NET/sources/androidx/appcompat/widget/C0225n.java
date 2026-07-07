package androidx.appcompat.widget;

import android.content.res.TypedArray;
import android.text.InputFilter;
import android.text.method.TransformationMethod;
import android.util.AttributeSet;
import android.widget.TextView;

/* JADX INFO: renamed from: androidx.appcompat.widget.n, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
class C0225n {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final TextView f4285a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final A.f f4286b;

    C0225n(TextView textView) {
        this.f4285a = textView;
        this.f4286b = new A.f(textView, false);
    }

    InputFilter[] a(InputFilter[] inputFilterArr) {
        return this.f4286b.a(inputFilterArr);
    }

    public boolean b() {
        return this.f4286b.b();
    }

    void c(AttributeSet attributeSet, int i3) {
        TypedArray typedArrayObtainStyledAttributes = this.f4285a.getContext().obtainStyledAttributes(attributeSet, d.j.f8982g0, i3, 0);
        try {
            boolean z3 = typedArrayObtainStyledAttributes.hasValue(d.j.f9038u0) ? typedArrayObtainStyledAttributes.getBoolean(d.j.f9038u0, true) : true;
            typedArrayObtainStyledAttributes.recycle();
            e(z3);
        } catch (Throwable th) {
            typedArrayObtainStyledAttributes.recycle();
            throw th;
        }
    }

    void d(boolean z3) {
        this.f4286b.c(z3);
    }

    void e(boolean z3) {
        this.f4286b.d(z3);
    }

    public TransformationMethod f(TransformationMethod transformationMethod) {
        return this.f4286b.e(transformationMethod);
    }
}
