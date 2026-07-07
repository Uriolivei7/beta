package q2;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/* JADX INFO: renamed from: q2.c, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public class C0655c extends FrameLayout {

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private static final String f10395d = "c";

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    InterfaceC0654b f10396b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private int f10397c;

    public C0655c(Context context) {
        super(context);
        this.f10396b = new f();
        a(null, 0);
    }

    private void a(AttributeSet attributeSet, int i3) {
        TypedArray typedArrayObtainStyledAttributes = getContext().obtainStyledAttributes(attributeSet, h.f10412a, i3, 0);
        this.f10397c = typedArrayObtainStyledAttributes.getColor(h.f10413b, 0);
        typedArrayObtainStyledAttributes.recycle();
    }

    private InterfaceC0653a getBlurAlgorithm() {
        return Build.VERSION.SDK_INT >= 31 ? new p() : new q(getContext());
    }

    public InterfaceC0657e b(boolean z3) {
        return this.f10396b.f(z3);
    }

    public InterfaceC0657e c(boolean z3) {
        return this.f10396b.d(z3);
    }

    public InterfaceC0657e d(float f3) {
        return this.f10396b.e(f3);
    }

    @Override // android.view.View
    public void draw(Canvas canvas) {
        if (this.f10396b.c(canvas)) {
            super.draw(canvas);
        }
    }

    public InterfaceC0657e e(int i3) {
        this.f10397c = i3;
        return this.f10396b.g(i3);
    }

    public InterfaceC0657e f(ViewGroup viewGroup) {
        return g(viewGroup, getBlurAlgorithm());
    }

    public InterfaceC0657e g(ViewGroup viewGroup, InterfaceC0653a interfaceC0653a) {
        this.f10396b.destroy();
        g gVar = new g(this, viewGroup, this.f10397c, interfaceC0653a);
        this.f10396b = gVar;
        return gVar;
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (isHardwareAccelerated()) {
            this.f10396b.f(true);
        } else {
            Log.e(f10395d, "BlurView can't be used in not hardware-accelerated window!");
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.f10396b.f(false);
    }

    @Override // android.view.View
    protected void onSizeChanged(int i3, int i4, int i5, int i6) {
        super.onSizeChanged(i3, i4, i5, i6);
        this.f10396b.b();
    }

    public C0655c(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.f10396b = new f();
        a(attributeSet, 0);
    }

    public C0655c(Context context, AttributeSet attributeSet, int i3) {
        super(context, attributeSet, i3);
        this.f10396b = new f();
        a(attributeSet, i3);
    }
}
