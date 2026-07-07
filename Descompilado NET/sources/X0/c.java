package x0;

import X.i;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import w0.InterfaceC0759a;
import w0.InterfaceC0760b;
import x0.C0769a;

/* JADX INFO: loaded from: classes.dex */
public class c extends ImageView {

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private static boolean f10946h = false;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final C0769a.C0155a f10947b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private float f10948c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private C0770b f10949d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private boolean f10950e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private boolean f10951f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private Object f10952g;

    public c(Context context) {
        super(context);
        this.f10947b = new C0769a.C0155a();
        this.f10948c = 0.0f;
        this.f10950e = false;
        this.f10951f = false;
        this.f10952g = null;
        c(context);
    }

    private void c(Context context) {
        try {
            if (V0.b.d()) {
                V0.b.a("DraweeView#init");
            }
            if (this.f10950e) {
                if (V0.b.d()) {
                    V0.b.b();
                    return;
                }
                return;
            }
            boolean z3 = true;
            this.f10950e = true;
            this.f10949d = C0770b.c(null, context);
            ColorStateList imageTintList = getImageTintList();
            if (imageTintList == null) {
                if (V0.b.d()) {
                    V0.b.b();
                    return;
                }
                return;
            }
            setColorFilter(imageTintList.getDefaultColor());
            if (!f10946h || context.getApplicationInfo().targetSdkVersion < 24) {
                z3 = false;
            }
            this.f10951f = z3;
            if (V0.b.d()) {
                V0.b.b();
            }
        } catch (Throwable th) {
            if (V0.b.d()) {
                V0.b.b();
            }
            throw th;
        }
    }

    private void d() {
        Drawable drawable;
        if (!this.f10951f || (drawable = getDrawable()) == null) {
            return;
        }
        drawable.setVisible(getVisibility() == 0, false);
    }

    public static void setGlobalLegacyVisibilityHandlingEnabled(boolean z3) {
        f10946h = z3;
    }

    protected void a() {
        this.f10949d.j();
    }

    protected void b() {
        this.f10949d.k();
    }

    protected void e() {
        a();
    }

    protected void f() {
        b();
    }

    public float getAspectRatio() {
        return this.f10948c;
    }

    public InterfaceC0759a getController() {
        return this.f10949d.e();
    }

    public Object getExtraData() {
        return this.f10952g;
    }

    public InterfaceC0760b getHierarchy() {
        return this.f10949d.f();
    }

    public Drawable getTopLevelDrawable() {
        return this.f10949d.g();
    }

    @Override // android.widget.ImageView, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        d();
        e();
    }

    @Override // android.widget.ImageView, android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        d();
        f();
    }

    @Override // android.view.View
    public void onFinishTemporaryDetach() {
        super.onFinishTemporaryDetach();
        d();
        e();
    }

    @Override // android.widget.ImageView, android.view.View
    protected void onMeasure(int i3, int i4) {
        C0769a.C0155a c0155a = this.f10947b;
        c0155a.f10938a = i3;
        c0155a.f10939b = i4;
        C0769a.b(c0155a, this.f10948c, getLayoutParams(), getPaddingLeft() + getPaddingRight(), getPaddingTop() + getPaddingBottom());
        C0769a.C0155a c0155a2 = this.f10947b;
        super.onMeasure(c0155a2.f10938a, c0155a2.f10939b);
    }

    @Override // android.view.View
    public void onStartTemporaryDetach() {
        super.onStartTemporaryDetach();
        d();
        f();
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (this.f10949d.l(motionEvent)) {
            return true;
        }
        return super.onTouchEvent(motionEvent);
    }

    @Override // android.view.View
    protected void onVisibilityChanged(View view, int i3) {
        super.onVisibilityChanged(view, i3);
        d();
    }

    public void setAspectRatio(float f3) {
        if (f3 == this.f10948c) {
            return;
        }
        this.f10948c = f3;
        requestLayout();
    }

    public void setController(InterfaceC0759a interfaceC0759a) {
        this.f10949d.o(interfaceC0759a);
        super.setImageDrawable(this.f10949d.g());
    }

    public void setExtraData(Object obj) {
        this.f10952g = obj;
    }

    public void setHierarchy(InterfaceC0760b interfaceC0760b) {
        this.f10949d.p(interfaceC0760b);
        super.setImageDrawable(this.f10949d.g());
    }

    @Override // android.widget.ImageView
    @Deprecated
    public void setImageBitmap(Bitmap bitmap) {
        c(getContext());
        this.f10949d.n();
        super.setImageBitmap(bitmap);
    }

    @Override // android.widget.ImageView
    @Deprecated
    public void setImageDrawable(Drawable drawable) {
        c(getContext());
        this.f10949d.n();
        super.setImageDrawable(drawable);
    }

    @Override // android.widget.ImageView
    @Deprecated
    public void setImageResource(int i3) {
        c(getContext());
        this.f10949d.n();
        super.setImageResource(i3);
    }

    @Override // android.widget.ImageView
    @Deprecated
    public void setImageURI(Uri uri) {
        c(getContext());
        this.f10949d.n();
        super.setImageURI(uri);
    }

    public void setLegacyVisibilityHandlingEnabled(boolean z3) {
        this.f10951f = z3;
    }

    @Override // android.view.View
    public String toString() {
        i.a aVarB = i.b(this);
        C0770b c0770b = this.f10949d;
        return aVarB.b("holder", c0770b != null ? c0770b.toString() : "<no holder set>").toString();
    }

    public c(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.f10947b = new C0769a.C0155a();
        this.f10948c = 0.0f;
        this.f10950e = false;
        this.f10951f = false;
        this.f10952g = null;
        c(context);
    }

    public c(Context context, AttributeSet attributeSet, int i3) {
        super(context, attributeSet, i3);
        this.f10947b = new C0769a.C0155a();
        this.f10948c = 0.0f;
        this.f10950e = false;
        this.f10951f = false;
        this.f10952g = null;
        c(context);
    }

    public c(Context context, AttributeSet attributeSet, int i3, int i4) {
        super(context, attributeSet, i3, i4);
        this.f10947b = new C0769a.C0155a();
        this.f10948c = 0.0f;
        this.f10950e = false;
        this.f10951f = false;
        this.f10952g = null;
        c(context);
    }
}
