package D2;

import C2.w;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import kotlin.jvm.internal.DefaultConstructorMarker;
import r2.C0686i;
import s2.AbstractC0696D;
import s2.AbstractC0717n;

/* JADX INFO: loaded from: classes.dex */
public final class e implements I2.b, d {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public static final a f174b = new a(null);

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private static final Map f175c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private static final HashMap f176d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private static final HashMap f177e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private static final HashMap f178f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private static final Map f179g;

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final Class f180a;

    public static final class a {
        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        /* JADX WARN: Code restructure failed: missing block: B:10:0x003b, code lost:
        
            if (r2 == null) goto L13;
         */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public final java.lang.String a(java.lang.Class r7) {
            /*
                r6 = this;
                java.lang.String r0 = "jClass"
                D2.h.f(r7, r0)
                boolean r0 = r7.isAnonymousClass()
                r1 = 0
                if (r0 == 0) goto Le
                goto Lb3
            Le:
                boolean r0 = r7.isLocalClass()
                if (r0 == 0) goto L6a
                java.lang.String r0 = r7.getSimpleName()
                java.lang.reflect.Method r2 = r7.getEnclosingMethod()
                r3 = 2
                r4 = 36
                if (r2 == 0) goto L41
                D2.h.c(r0)
                java.lang.StringBuilder r5 = new java.lang.StringBuilder
                r5.<init>()
                java.lang.String r2 = r2.getName()
                r5.append(r2)
                r5.append(r4)
                java.lang.String r2 = r5.toString()
                java.lang.String r2 = K2.o.t0(r0, r2, r1, r3, r1)
                if (r2 != 0) goto L3e
                goto L41
            L3e:
                r1 = r2
                goto Lb3
            L41:
                java.lang.reflect.Constructor r7 = r7.getEnclosingConstructor()
                if (r7 == 0) goto L62
                D2.h.c(r0)
                java.lang.StringBuilder r2 = new java.lang.StringBuilder
                r2.<init>()
                java.lang.String r7 = r7.getName()
                r2.append(r7)
                r2.append(r4)
                java.lang.String r7 = r2.toString()
                java.lang.String r1 = K2.o.t0(r0, r7, r1, r3, r1)
                goto Lb3
            L62:
                D2.h.c(r0)
                java.lang.String r1 = K2.o.s0(r0, r4, r1, r3, r1)
                goto Lb3
            L6a:
                boolean r0 = r7.isArray()
                if (r0 == 0) goto L9e
                java.lang.Class r7 = r7.getComponentType()
                boolean r0 = r7.isPrimitive()
                java.lang.String r2 = "Array"
                if (r0 == 0) goto L9b
                java.util.Map r0 = D2.e.c()
                java.lang.String r7 = r7.getName()
                java.lang.Object r7 = r0.get(r7)
                java.lang.String r7 = (java.lang.String) r7
                if (r7 == 0) goto L9b
                java.lang.StringBuilder r0 = new java.lang.StringBuilder
                r0.<init>()
                r0.append(r7)
                r0.append(r2)
                java.lang.String r1 = r0.toString()
            L9b:
                if (r1 != 0) goto Lb3
                goto L3e
            L9e:
                java.util.Map r0 = D2.e.c()
                java.lang.String r1 = r7.getName()
                java.lang.Object r0 = r0.get(r1)
                r1 = r0
                java.lang.String r1 = (java.lang.String) r1
                if (r1 != 0) goto Lb3
                java.lang.String r1 = r7.getSimpleName()
            Lb3:
                return r1
            */
            throw new UnsupportedOperationException("Method not decompiled: D2.e.a.a(java.lang.Class):java.lang.String");
        }

        private a() {
        }
    }

    static {
        List listJ = AbstractC0717n.j(C2.a.class, C2.l.class, C2.p.class, C2.q.class, C2.r.class, C2.s.class, C2.t.class, C2.u.class, C2.v.class, w.class, C2.b.class, C2.c.class, C2.d.class, C2.e.class, C2.f.class, C2.g.class, C2.h.class, C2.i.class, C2.j.class, C2.k.class, C2.m.class, C2.n.class, C2.o.class);
        ArrayList arrayList = new ArrayList(AbstractC0717n.q(listJ, 10));
        int i3 = 0;
        for (Object obj : listJ) {
            int i4 = i3 + 1;
            if (i3 < 0) {
                AbstractC0717n.p();
            }
            arrayList.add(r2.n.a((Class) obj, Integer.valueOf(i3)));
            i3 = i4;
        }
        f175c = AbstractC0696D.m(arrayList);
        HashMap map = new HashMap();
        map.put("boolean", "kotlin.Boolean");
        map.put("char", "kotlin.Char");
        map.put("byte", "kotlin.Byte");
        map.put("short", "kotlin.Short");
        map.put("int", "kotlin.Int");
        map.put("float", "kotlin.Float");
        map.put("long", "kotlin.Long");
        map.put("double", "kotlin.Double");
        f176d = map;
        HashMap map2 = new HashMap();
        map2.put("java.lang.Boolean", "kotlin.Boolean");
        map2.put("java.lang.Character", "kotlin.Char");
        map2.put("java.lang.Byte", "kotlin.Byte");
        map2.put("java.lang.Short", "kotlin.Short");
        map2.put("java.lang.Integer", "kotlin.Int");
        map2.put("java.lang.Float", "kotlin.Float");
        map2.put("java.lang.Long", "kotlin.Long");
        map2.put("java.lang.Double", "kotlin.Double");
        f177e = map2;
        HashMap map3 = new HashMap();
        map3.put("java.lang.Object", "kotlin.Any");
        map3.put("java.lang.String", "kotlin.String");
        map3.put("java.lang.CharSequence", "kotlin.CharSequence");
        map3.put("java.lang.Throwable", "kotlin.Throwable");
        map3.put("java.lang.Cloneable", "kotlin.Cloneable");
        map3.put("java.lang.Number", "kotlin.Number");
        map3.put("java.lang.Comparable", "kotlin.Comparable");
        map3.put("java.lang.Enum", "kotlin.Enum");
        map3.put("java.lang.annotation.Annotation", "kotlin.Annotation");
        map3.put("java.lang.Iterable", "kotlin.collections.Iterable");
        map3.put("java.util.Iterator", "kotlin.collections.Iterator");
        map3.put("java.util.Collection", "kotlin.collections.Collection");
        map3.put("java.util.List", "kotlin.collections.List");
        map3.put("java.util.Set", "kotlin.collections.Set");
        map3.put("java.util.ListIterator", "kotlin.collections.ListIterator");
        map3.put("java.util.Map", "kotlin.collections.Map");
        map3.put("java.util.Map$Entry", "kotlin.collections.Map.Entry");
        map3.put("kotlin.jvm.internal.StringCompanionObject", "kotlin.String.Companion");
        map3.put("kotlin.jvm.internal.EnumCompanionObject", "kotlin.Enum.Companion");
        map3.putAll(map);
        map3.putAll(map2);
        Collection<String> collectionValues = map.values();
        h.e(collectionValues, "<get-values>(...)");
        for (String str : collectionValues) {
            StringBuilder sb = new StringBuilder();
            sb.append("kotlin.jvm.internal.");
            h.c(str);
            sb.append(K2.o.v0(str, '.', null, 2, null));
            sb.append("CompanionObject");
            C0686i c0686iA = r2.n.a(sb.toString(), str + ".Companion");
            map3.put(c0686iA.c(), c0686iA.d());
        }
        for (Map.Entry entry : f175c.entrySet()) {
            map3.put(((Class) entry.getKey()).getName(), "kotlin.Function" + ((Number) entry.getValue()).intValue());
        }
        f178f = map3;
        LinkedHashMap linkedHashMap = new LinkedHashMap(AbstractC0696D.c(map3.size()));
        for (Map.Entry entry2 : map3.entrySet()) {
            linkedHashMap.put(entry2.getKey(), K2.o.v0((String) entry2.getValue(), '.', null, 2, null));
        }
        f179g = linkedHashMap;
    }

    public e(Class<?> cls) {
        h.f(cls, "jClass");
        this.f180a = cls;
    }

    @Override // I2.b
    public String a() {
        return f174b.a(b());
    }

    @Override // D2.d
    public Class b() {
        return this.f180a;
    }

    public boolean equals(Object obj) {
        return (obj instanceof e) && h.b(B2.a.b(this), B2.a.b((I2.b) obj));
    }

    public int hashCode() {
        return B2.a.b(this).hashCode();
    }

    public String toString() {
        return b().toString() + " (Kotlin reflection is not available)";
    }
}
