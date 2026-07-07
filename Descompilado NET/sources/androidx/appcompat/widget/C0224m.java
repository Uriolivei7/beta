package androidx.appcompat.widget;

import android.content.res.TypedArray;
import android.text.method.KeyListener;
import android.text.method.NumberKeyListener;
import android.util.AttributeSet;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.widget.EditText;

/* JADX INFO: renamed from: androidx.appcompat.widget.m, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
class C0224m {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final EditText f4283a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final A.a f4284b;

    C0224m(EditText editText) {
        this.f4283a = editText;
        this.f4284b = new A.a(editText, false);
    }

    KeyListener a(KeyListener keyListener) {
        return b(keyListener) ? this.f4284b.a(keyListener) : keyListener;
    }

    boolean b(KeyListener keyListener) {
        return !(keyListener instanceof NumberKeyListener);
    }

    void c(AttributeSet attributeSet, int i3) {
        TypedArray typedArrayObtainStyledAttributes = this.f4283a.getContext().obtainStyledAttributes(attributeSet, d.j.f8982g0, i3, 0);
        try {
            boolean z3 = typedArrayObtainStyledAttributes.hasValue(d.j.f9038u0) ? typedArrayObtainStyledAttributes.getBoolean(d.j.f9038u0, true) : true;
            typedArrayObtainStyledAttributes.recycle();
            e(z3);
        } catch (Throwable th) {
            typedArrayObtainStyledAttributes.recycle();
            throw th;
        }
    }

    InputConnection d(InputConnection inputConnection, EditorInfo editorInfo) {
        return this.f4284b.b(inputConnection, editorInfo);
    }

    void e(boolean z3) {
        this.f4284b.c(z3);
    }
}
