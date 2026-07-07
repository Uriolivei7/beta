package androidx.lifecycle;

import android.os.Binder;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Size;
import android.util.SizeF;
import android.util.SparseArray;
import androidx.savedstate.a;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import kotlin.jvm.internal.DefaultConstructorMarker;
import s2.AbstractC0696D;

/* JADX INFO: loaded from: classes.dex */
public final class x {

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    public static final a f5361f = new a(null);

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private static final Class[] f5362g = {Boolean.TYPE, boolean[].class, Double.TYPE, double[].class, Integer.TYPE, int[].class, Long.TYPE, long[].class, String.class, String[].class, Binder.class, Bundle.class, Byte.TYPE, byte[].class, Character.TYPE, char[].class, CharSequence.class, CharSequence[].class, ArrayList.class, Float.TYPE, float[].class, Parcelable.class, Parcelable[].class, Serializable.class, Short.TYPE, short[].class, SparseArray.class, Size.class, SizeF.class};

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final Map f5363a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final Map f5364b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final Map f5365c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private final Map f5366d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private final a.c f5367e;

    public static final class a {
        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final x a(Bundle bundle, Bundle bundle2) {
            if (bundle == null) {
                if (bundle2 == null) {
                    return new x();
                }
                HashMap map = new HashMap();
                for (String str : bundle2.keySet()) {
                    D2.h.e(str, "key");
                    map.put(str, bundle2.get(str));
                }
                return new x(map);
            }
            ArrayList parcelableArrayList = bundle.getParcelableArrayList("keys");
            ArrayList parcelableArrayList2 = bundle.getParcelableArrayList("values");
            if (parcelableArrayList == null || parcelableArrayList2 == null || parcelableArrayList.size() != parcelableArrayList2.size()) {
                throw new IllegalStateException("Invalid bundle passed as restored state");
            }
            LinkedHashMap linkedHashMap = new LinkedHashMap();
            int size = parcelableArrayList.size();
            for (int i3 = 0; i3 < size; i3++) {
                Object obj = parcelableArrayList.get(i3);
                D2.h.d(obj, "null cannot be cast to non-null type kotlin.String");
                linkedHashMap.put((String) obj, parcelableArrayList2.get(i3));
            }
            return new x(linkedHashMap);
        }

        public final boolean b(Object obj) {
            if (obj == null) {
                return true;
            }
            for (Class cls : x.f5362g) {
                D2.h.c(cls);
                if (cls.isInstance(obj)) {
                    return true;
                }
            }
            return false;
        }

        private a() {
        }
    }

    public x(Map<String, ? extends Object> map) {
        D2.h.f(map, "initialState");
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        this.f5363a = linkedHashMap;
        this.f5364b = new LinkedHashMap();
        this.f5365c = new LinkedHashMap();
        this.f5366d = new LinkedHashMap();
        this.f5367e = new a.c() { // from class: androidx.lifecycle.w
            @Override // androidx.savedstate.a.c
            public final Bundle a() {
                return x.d(this.f5360a);
            }
        };
        linkedHashMap.putAll(map);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Bundle d(x xVar) {
        D2.h.f(xVar, "this$0");
        for (Map.Entry entry : AbstractC0696D.o(xVar.f5364b).entrySet()) {
            xVar.e((String) entry.getKey(), ((a.c) entry.getValue()).a());
        }
        Set<String> setKeySet = xVar.f5363a.keySet();
        ArrayList arrayList = new ArrayList(setKeySet.size());
        ArrayList arrayList2 = new ArrayList(arrayList.size());
        for (String str : setKeySet) {
            arrayList.add(str);
            arrayList2.add(xVar.f5363a.get(str));
        }
        return androidx.core.os.c.a(r2.n.a("keys", arrayList), r2.n.a("values", arrayList2));
    }

    public final a.c c() {
        return this.f5367e;
    }

    public final void e(String str, Object obj) {
        D2.h.f(str, "key");
        if (!f5361f.b(obj)) {
            StringBuilder sb = new StringBuilder();
            sb.append("Can't put value with type ");
            D2.h.c(obj);
            sb.append(obj.getClass());
            sb.append(" into saved state");
            throw new IllegalArgumentException(sb.toString());
        }
        Object obj2 = this.f5365c.get(str);
        p pVar = obj2 instanceof p ? (p) obj2 : null;
        if (pVar != null) {
            pVar.i(obj);
        } else {
            this.f5363a.put(str, obj);
        }
        androidx.activity.result.d.a(this.f5366d.get(str));
    }

    public x() {
        this.f5363a = new LinkedHashMap();
        this.f5364b = new LinkedHashMap();
        this.f5365c = new LinkedHashMap();
        this.f5366d = new LinkedHashMap();
        this.f5367e = new a.c() { // from class: androidx.lifecycle.w
            @Override // androidx.savedstate.a.c
            public final Bundle a() {
                return x.d(this.f5360a);
            }
        };
    }
}
