package x0;

import X.k;
import X.n;
import android.content.Context;
import android.content.res.TypedArray;
import android.net.Uri;
import android.util.AttributeSet;
import f0.f;
import l0.AbstractC0590a;
import q0.AbstractC0646b;
import u0.C0734a;

/* JADX INFO: loaded from: classes.dex */
public class e extends d {

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    private static n f10953j;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    private AbstractC0646b f10954i;

    public e(Context context, C0734a c0734a) {
        super(context, c0734a);
        h(context, null);
    }

    private void h(Context context, AttributeSet attributeSet) {
        int resourceId;
        try {
            if (V0.b.d()) {
                V0.b.a("SimpleDraweeView#init");
            }
            if (isInEditMode()) {
                getTopLevelDrawable().setVisible(true, false);
                getTopLevelDrawable().invalidateSelf();
            } else {
                k.h(f10953j, "SimpleDraweeView was not initialized!");
                this.f10954i = (AbstractC0646b) f10953j.get();
            }
            if (attributeSet != null) {
                TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, AbstractC0590a.f9669E);
                try {
                    if (typedArrayObtainStyledAttributes.hasValue(AbstractC0590a.f9671G)) {
                        k(Uri.parse(typedArrayObtainStyledAttributes.getString(AbstractC0590a.f9671G)), null);
                    } else if (typedArrayObtainStyledAttributes.hasValue(AbstractC0590a.f9670F) && (resourceId = typedArrayObtainStyledAttributes.getResourceId(AbstractC0590a.f9670F, -1)) != -1) {
                        if (isInEditMode()) {
                            setImageResource(resourceId);
                        } else {
                            setActualImageResource(resourceId);
                        }
                    }
                    typedArrayObtainStyledAttributes.recycle();
                } catch (Throwable th) {
                    typedArrayObtainStyledAttributes.recycle();
                    throw th;
                }
            }
            if (V0.b.d()) {
                V0.b.b();
            }
        } catch (Throwable th2) {
            if (V0.b.d()) {
                V0.b.b();
            }
            throw th2;
        }
    }

    public static void i(n nVar) {
        f10953j = nVar;
    }

    public AbstractC0646b getControllerBuilder() {
        return this.f10954i;
    }

    public void j(int i3, Object obj) {
        k(f.g(i3), obj);
    }

    public void k(Uri uri, Object obj) {
        setController(this.f10954i.C(obj).c(uri).b(getController()).a());
    }

    public void l(String str, Object obj) {
        k(str != null ? Uri.parse(str) : null, obj);
    }

    public void setActualImageResource(int i3) {
        j(i3, null);
    }

    public void setImageRequest(U0.b bVar) {
        setController(this.f10954i.E(bVar).b(getController()).a());
    }

    @Override // x0.c, android.widget.ImageView
    public void setImageResource(int i3) {
        super.setImageResource(i3);
    }

    @Override // x0.c, android.widget.ImageView
    public void setImageURI(Uri uri) {
        k(uri, null);
    }

    public void setImageURI(String str) {
        l(str, null);
    }

    public e(Context context) {
        super(context);
        h(context, null);
    }

    public e(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        h(context, attributeSet);
    }

    public e(Context context, AttributeSet attributeSet, int i3) {
        super(context, attributeSet, i3);
        h(context, attributeSet);
    }

    public e(Context context, AttributeSet attributeSet, int i3, int i4) {
        super(context, attributeSet, i3, i4);
        h(context, attributeSet);
    }
}
