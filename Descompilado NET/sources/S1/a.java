package S1;

import D2.h;
import android.view.View;
import android.view.ViewGroup;
import androidx.activity.result.d;
import d1.AbstractC0505m;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/* JADX INFO: loaded from: classes.dex */
public final class a {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static final a f2312a = new a();

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private static final List f2313b = new ArrayList();

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private static final Map f2314c = new HashMap();

    private a() {
    }

    public static final View a(View view, String str) {
        h.f(view, "root");
        h.f(str, "nativeId");
        if (h.b(f2312a.b(view), str)) {
            return view;
        }
        if (!(view instanceof ViewGroup)) {
            return null;
        }
        ViewGroup viewGroup = (ViewGroup) view;
        int childCount = viewGroup.getChildCount();
        for (int i3 = 0; i3 < childCount; i3++) {
            View childAt = viewGroup.getChildAt(i3);
            h.e(childAt, "getChildAt(...)");
            View viewA = a(childAt, str);
            if (viewA != null) {
                return viewA;
            }
        }
        return null;
    }

    private final String b(View view) {
        Object tag = view.getTag(AbstractC0505m.f9227E);
        if (tag instanceof String) {
            return (String) tag;
        }
        return null;
    }

    public static final void c(View view) {
        h.f(view, "view");
        String strB = f2312a.b(view);
        if (strB == null) {
            return;
        }
        Iterator it = f2313b.iterator();
        if (it.hasNext()) {
            d.a(it.next());
            throw null;
        }
        for (Map.Entry entry : f2314c.entrySet()) {
            d.a(entry.getKey());
            if (((Set) entry.getValue()).contains(strB)) {
                throw null;
            }
        }
    }
}
