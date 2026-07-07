package androidx.appcompat.app;

import android.R;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import androidx.appcompat.widget.A;
import androidx.appcompat.widget.C0215d;
import androidx.appcompat.widget.C0217f;
import androidx.appcompat.widget.C0218g;
import androidx.appcompat.widget.C0219h;
import androidx.appcompat.widget.C0223l;
import androidx.appcompat.widget.C0227p;
import androidx.appcompat.widget.C0229s;
import androidx.appcompat.widget.C0232v;
import androidx.appcompat.widget.C0233w;
import androidx.appcompat.widget.C0235y;
import androidx.appcompat.widget.D;
import androidx.appcompat.widget.H;
import androidx.appcompat.widget.e0;
import androidx.core.view.Z;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/* JADX INFO: loaded from: classes.dex */
public class s {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private static final Class[] f3287b = {Context.class, AttributeSet.class};

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private static final int[] f3288c = {R.attr.onClick};

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private static final int[] f3289d = {R.attr.accessibilityHeading};

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private static final int[] f3290e = {R.attr.accessibilityPaneTitle};

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private static final int[] f3291f = {R.attr.screenReaderFocusable};

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private static final String[] f3292g = {"android.widget.", "android.view.", "android.webkit."};

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private static final l.g f3293h = new l.g();

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final Object[] f3294a = new Object[2];

    private static class a implements View.OnClickListener {

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        private final View f3295b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        private final String f3296c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        private Method f3297d;

        /* JADX INFO: renamed from: e, reason: collision with root package name */
        private Context f3298e;

        public a(View view, String str) {
            this.f3295b = view;
            this.f3296c = str;
        }

        private void a(Context context) {
            String str;
            Method method;
            while (context != null) {
                try {
                    if (!context.isRestricted() && (method = context.getClass().getMethod(this.f3296c, View.class)) != null) {
                        this.f3297d = method;
                        this.f3298e = context;
                        return;
                    }
                } catch (NoSuchMethodException unused) {
                }
                context = context instanceof ContextWrapper ? ((ContextWrapper) context).getBaseContext() : null;
            }
            int id = this.f3295b.getId();
            if (id == -1) {
                str = "";
            } else {
                str = " with id '" + this.f3295b.getContext().getResources().getResourceEntryName(id) + "'";
            }
            throw new IllegalStateException("Could not find method " + this.f3296c + "(View) in a parent or ancestor Context for android:onClick attribute defined on view " + this.f3295b.getClass() + str);
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (this.f3297d == null) {
                a(this.f3295b.getContext());
            }
            try {
                this.f3297d.invoke(this.f3298e, view);
            } catch (IllegalAccessException e4) {
                throw new IllegalStateException("Could not execute non-public method for android:onClick", e4);
            } catch (InvocationTargetException e5) {
                throw new IllegalStateException("Could not execute method for android:onClick", e5);
            }
        }
    }

    private void a(Context context, View view, AttributeSet attributeSet) {
        if (Build.VERSION.SDK_INT > 28) {
            return;
        }
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, f3289d);
        if (typedArrayObtainStyledAttributes.hasValue(0)) {
            Z.Y(view, typedArrayObtainStyledAttributes.getBoolean(0, false));
        }
        typedArrayObtainStyledAttributes.recycle();
        TypedArray typedArrayObtainStyledAttributes2 = context.obtainStyledAttributes(attributeSet, f3290e);
        if (typedArrayObtainStyledAttributes2.hasValue(0)) {
            Z.a0(view, typedArrayObtainStyledAttributes2.getString(0));
        }
        typedArrayObtainStyledAttributes2.recycle();
        TypedArray typedArrayObtainStyledAttributes3 = context.obtainStyledAttributes(attributeSet, f3291f);
        if (typedArrayObtainStyledAttributes3.hasValue(0)) {
            Z.j0(view, typedArrayObtainStyledAttributes3.getBoolean(0, false));
        }
        typedArrayObtainStyledAttributes3.recycle();
    }

    private void b(View view, AttributeSet attributeSet) {
        Context context = view.getContext();
        if ((context instanceof ContextWrapper) && view.hasOnClickListeners()) {
            TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, f3288c);
            String string = typedArrayObtainStyledAttributes.getString(0);
            if (string != null) {
                view.setOnClickListener(new a(view, string));
            }
            typedArrayObtainStyledAttributes.recycle();
        }
    }

    private View s(Context context, String str, String str2) {
        String str3;
        l.g gVar = f3293h;
        Constructor constructor = (Constructor) gVar.get(str);
        if (constructor == null) {
            if (str2 != null) {
                try {
                    str3 = str2 + str;
                } catch (Exception unused) {
                    return null;
                }
            } else {
                str3 = str;
            }
            constructor = Class.forName(str3, false, context.getClassLoader()).asSubclass(View.class).getConstructor(f3287b);
            gVar.put(str, constructor);
        }
        constructor.setAccessible(true);
        return (View) constructor.newInstance(this.f3294a);
    }

    private View t(Context context, String str, AttributeSet attributeSet) {
        if (str.equals("view")) {
            str = attributeSet.getAttributeValue(null, "class");
        }
        try {
            Object[] objArr = this.f3294a;
            objArr[0] = context;
            objArr[1] = attributeSet;
            if (-1 != str.indexOf(46)) {
                return s(context, str, null);
            }
            int i3 = 0;
            while (true) {
                String[] strArr = f3292g;
                if (i3 >= strArr.length) {
                    return null;
                }
                View viewS = s(context, str, strArr[i3]);
                if (viewS != null) {
                    return viewS;
                }
                i3++;
            }
        } catch (Exception unused) {
            return null;
        } finally {
            Object[] objArr2 = this.f3294a;
            objArr2[0] = null;
            objArr2[1] = null;
        }
    }

    private static Context u(Context context, AttributeSet attributeSet, boolean z3, boolean z4) {
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, d.j.H3, 0, 0);
        int resourceId = z3 ? typedArrayObtainStyledAttributes.getResourceId(d.j.I3, 0) : 0;
        if (z4 && resourceId == 0 && (resourceId = typedArrayObtainStyledAttributes.getResourceId(d.j.J3, 0)) != 0) {
            Log.i("AppCompatViewInflater", "app:theme is now deprecated. Please move to using android:theme instead.");
        }
        typedArrayObtainStyledAttributes.recycle();
        return resourceId != 0 ? ((context instanceof androidx.appcompat.view.d) && ((androidx.appcompat.view.d) context).c() == resourceId) ? context : new androidx.appcompat.view.d(context, resourceId) : context;
    }

    private void v(View view, String str) {
        if (view != null) {
            return;
        }
        throw new IllegalStateException(getClass().getName() + " asked to inflate view for <" + str + ">, but returned null");
    }

    protected C0215d c(Context context, AttributeSet attributeSet) {
        return new C0215d(context, attributeSet);
    }

    protected C0217f d(Context context, AttributeSet attributeSet) {
        return new C0217f(context, attributeSet);
    }

    protected C0218g e(Context context, AttributeSet attributeSet) {
        return new C0218g(context, attributeSet);
    }

    protected C0219h f(Context context, AttributeSet attributeSet) {
        return new C0219h(context, attributeSet);
    }

    protected C0223l g(Context context, AttributeSet attributeSet) {
        return new C0223l(context, attributeSet);
    }

    protected C0227p h(Context context, AttributeSet attributeSet) {
        return new C0227p(context, attributeSet);
    }

    protected androidx.appcompat.widget.r i(Context context, AttributeSet attributeSet) {
        return new androidx.appcompat.widget.r(context, attributeSet);
    }

    protected C0229s j(Context context, AttributeSet attributeSet) {
        return new C0229s(context, attributeSet);
    }

    protected C0232v k(Context context, AttributeSet attributeSet) {
        return new C0232v(context, attributeSet);
    }

    protected C0233w l(Context context, AttributeSet attributeSet) {
        return new C0233w(context, attributeSet);
    }

    protected C0235y m(Context context, AttributeSet attributeSet) {
        return new C0235y(context, attributeSet);
    }

    protected A n(Context context, AttributeSet attributeSet) {
        return new A(context, attributeSet);
    }

    protected D o(Context context, AttributeSet attributeSet) {
        return new D(context, attributeSet);
    }

    protected H p(Context context, AttributeSet attributeSet) {
        return new H(context, attributeSet);
    }

    protected View q(Context context, String str, AttributeSet attributeSet) {
        return null;
    }

    public final View r(View view, String str, Context context, AttributeSet attributeSet, boolean z3, boolean z4, boolean z5, boolean z6) {
        Context context2;
        View viewL;
        context2 = (!z3 || view == null) ? context : view.getContext();
        if (z4 || z5) {
            context2 = u(context2, attributeSet, z4, z5);
        }
        if (z6) {
            context2 = e0.b(context2);
        }
        str.hashCode();
        switch (str) {
            case "RatingBar":
                viewL = l(context2, attributeSet);
                v(viewL, str);
                break;
            case "CheckedTextView":
                viewL = f(context2, attributeSet);
                v(viewL, str);
                break;
            case "MultiAutoCompleteTextView":
                viewL = j(context2, attributeSet);
                v(viewL, str);
                break;
            case "TextView":
                viewL = o(context2, attributeSet);
                v(viewL, str);
                break;
            case "ImageButton":
                viewL = h(context2, attributeSet);
                v(viewL, str);
                break;
            case "SeekBar":
                viewL = m(context2, attributeSet);
                v(viewL, str);
                break;
            case "Spinner":
                viewL = n(context2, attributeSet);
                v(viewL, str);
                break;
            case "RadioButton":
                viewL = k(context2, attributeSet);
                v(viewL, str);
                break;
            case "ToggleButton":
                viewL = p(context2, attributeSet);
                v(viewL, str);
                break;
            case "ImageView":
                viewL = i(context2, attributeSet);
                v(viewL, str);
                break;
            case "AutoCompleteTextView":
                viewL = c(context2, attributeSet);
                v(viewL, str);
                break;
            case "CheckBox":
                viewL = e(context2, attributeSet);
                v(viewL, str);
                break;
            case "EditText":
                viewL = g(context2, attributeSet);
                v(viewL, str);
                break;
            case "Button":
                viewL = d(context2, attributeSet);
                v(viewL, str);
                break;
            default:
                viewL = q(context2, str, attributeSet);
                break;
        }
        if (viewL == null && context != context2) {
            viewL = t(context2, str, attributeSet);
        }
        if (viewL != null) {
            b(viewL, attributeSet);
            a(context2, viewL, attributeSet);
        }
        return viewL;
    }
}
