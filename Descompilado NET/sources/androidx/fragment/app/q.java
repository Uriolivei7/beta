package androidx.fragment.app;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/* JADX INFO: loaded from: classes.dex */
class q implements LayoutInflater.Factory2 {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    final x f5173b;

    class a implements View.OnAttachStateChangeListener {

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        final /* synthetic */ D f5174b;

        a(D d4) {
            this.f5174b = d4;
        }

        @Override // android.view.View.OnAttachStateChangeListener
        public void onViewAttachedToWindow(View view) {
            Fragment fragmentK = this.f5174b.k();
            this.f5174b.m();
            L.n((ViewGroup) fragmentK.f4920J.getParent(), q.this.f5173b).j();
        }

        @Override // android.view.View.OnAttachStateChangeListener
        public void onViewDetachedFromWindow(View view) {
        }
    }

    q(x xVar) {
        this.f5173b = xVar;
    }

    @Override // android.view.LayoutInflater.Factory
    public View onCreateView(String str, Context context, AttributeSet attributeSet) {
        return onCreateView(null, str, context, attributeSet);
    }

    @Override // android.view.LayoutInflater.Factory2
    public View onCreateView(View view, String str, Context context, AttributeSet attributeSet) {
        D dV;
        if (C0291m.class.getName().equals(str)) {
            return new C0291m(context, attributeSet, this.f5173b);
        }
        if (!"fragment".equals(str)) {
            return null;
        }
        String attributeValue = attributeSet.getAttributeValue(null, "class");
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, B.c.f45a);
        if (attributeValue == null) {
            attributeValue = typedArrayObtainStyledAttributes.getString(B.c.f46b);
        }
        int resourceId = typedArrayObtainStyledAttributes.getResourceId(B.c.f47c, -1);
        String string = typedArrayObtainStyledAttributes.getString(B.c.f48d);
        typedArrayObtainStyledAttributes.recycle();
        if (attributeValue == null || !o.b(context.getClassLoader(), attributeValue)) {
            return null;
        }
        int id = view != null ? view.getId() : 0;
        if (id == -1 && resourceId == -1 && string == null) {
            throw new IllegalArgumentException(attributeSet.getPositionDescription() + ": Must specify unique android:id, android:tag, or have a parent with an id for " + attributeValue);
        }
        Fragment fragmentG0 = resourceId != -1 ? this.f5173b.g0(resourceId) : null;
        if (fragmentG0 == null && string != null) {
            fragmentG0 = this.f5173b.h0(string);
        }
        if (fragmentG0 == null && id != -1) {
            fragmentG0 = this.f5173b.g0(id);
        }
        if (fragmentG0 == null) {
            fragmentG0 = this.f5173b.r0().a(context.getClassLoader(), attributeValue);
            fragmentG0.f4953p = true;
            fragmentG0.f4962y = resourceId != 0 ? resourceId : id;
            fragmentG0.f4963z = id;
            fragmentG0.f4911A = string;
            fragmentG0.f4954q = true;
            x xVar = this.f5173b;
            fragmentG0.f4958u = xVar;
            fragmentG0.f4959v = xVar.t0();
            fragmentG0.w0(this.f5173b.t0().k(), attributeSet, fragmentG0.f4940c);
            dV = this.f5173b.j(fragmentG0);
            if (x.G0(2)) {
                Log.v("FragmentManager", "Fragment " + fragmentG0 + " has been inflated via the <fragment> tag: id=0x" + Integer.toHexString(resourceId));
            }
        } else {
            if (fragmentG0.f4954q) {
                throw new IllegalArgumentException(attributeSet.getPositionDescription() + ": Duplicate id 0x" + Integer.toHexString(resourceId) + ", tag " + string + ", or parent id 0x" + Integer.toHexString(id) + " with another fragment for " + attributeValue);
            }
            fragmentG0.f4954q = true;
            x xVar2 = this.f5173b;
            fragmentG0.f4958u = xVar2;
            fragmentG0.f4959v = xVar2.t0();
            fragmentG0.w0(this.f5173b.t0().k(), attributeSet, fragmentG0.f4940c);
            dV = this.f5173b.v(fragmentG0);
            if (x.G0(2)) {
                Log.v("FragmentManager", "Retained Fragment " + fragmentG0 + " has been re-attached via the <fragment> tag: id=0x" + Integer.toHexString(resourceId));
            }
        }
        ViewGroup viewGroup = (ViewGroup) view;
        C.c.g(fragmentG0, viewGroup);
        fragmentG0.f4919I = viewGroup;
        dV.m();
        dV.j();
        View view2 = fragmentG0.f4920J;
        if (view2 == null) {
            throw new IllegalStateException("Fragment " + attributeValue + " did not create a view.");
        }
        if (resourceId != 0) {
            view2.setId(resourceId);
        }
        if (fragmentG0.f4920J.getTag() == null) {
            fragmentG0.f4920J.setTag(string);
        }
        fragmentG0.f4920J.addOnAttachStateChangeListener(new a(dV));
        return fragmentG0.f4920J;
    }
}
