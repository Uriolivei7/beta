package E1;

import D2.h;
import K2.o;
import android.util.Pair;
import d2.C0518a;
import java.util.LinkedHashMap;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public final class d extends Q0.a {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private int f207a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final Map f208b = new LinkedHashMap();

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final Map f209c = new LinkedHashMap();

    @Override // Q0.a, Q0.e
    public void a(U0.b bVar, Object obj, String str, boolean z3) {
        h.f(bVar, "request");
        h.f(obj, "callerContext");
        h.f(str, "requestId");
        if (C0518a.j(0L)) {
            StringBuilder sb = new StringBuilder();
            sb.append("FRESCO_REQUEST_");
            String string = bVar.v().toString();
            h.e(string, "toString(...)");
            sb.append(o.u(string, ':', '_', false, 4, null));
            Pair pairCreate = Pair.create(Integer.valueOf(this.f207a), sb.toString());
            Object obj2 = pairCreate.second;
            h.e(obj2, "second");
            C0518a.a(0L, (String) obj2, this.f207a);
            this.f209c.put(str, pairCreate);
            this.f207a++;
        }
    }

    @Override // Q0.a, Q0.e
    public void b(U0.b bVar, String str, boolean z3) {
        Pair pair;
        h.f(bVar, "request");
        h.f(str, "requestId");
        if (C0518a.j(0L) && (pair = (Pair) this.f209c.get(str)) != null) {
            Object obj = pair.second;
            h.e(obj, "second");
            Object obj2 = pair.first;
            h.e(obj2, "first");
            C0518a.g(0L, (String) obj, ((Number) obj2).intValue());
            this.f209c.remove(str);
        }
    }

    @Override // Q0.a, com.facebook.imagepipeline.producers.i0
    public boolean c(String str) {
        h.f(str, "requestId");
        return false;
    }

    @Override // Q0.a, com.facebook.imagepipeline.producers.i0
    public void d(String str, String str2, String str3) {
        h.f(str, "requestId");
        h.f(str2, "producerName");
        h.f(str3, "eventName");
        if (C0518a.j(0L)) {
            C0518a.n(0L, "FRESCO_PRODUCER_EVENT_" + o.u(str, ':', '_', false, 4, null) + "_" + o.u(str2, ':', '_', false, 4, null) + "_" + o.u(str3, ':', '_', false, 4, null), C0518a.EnumC0124a.f9333c);
        }
    }

    @Override // Q0.a, com.facebook.imagepipeline.producers.i0
    public void e(String str, String str2, Map map) {
        Pair pair;
        h.f(str, "requestId");
        h.f(str2, "producerName");
        if (C0518a.j(0L) && (pair = (Pair) this.f208b.get(str)) != null) {
            Object obj = pair.second;
            h.e(obj, "second");
            Object obj2 = pair.first;
            h.e(obj2, "first");
            C0518a.g(0L, (String) obj, ((Number) obj2).intValue());
            this.f208b.remove(str);
        }
    }

    @Override // Q0.a, com.facebook.imagepipeline.producers.i0
    public void f(String str, String str2) {
        h.f(str, "requestId");
        h.f(str2, "producerName");
        if (C0518a.j(0L)) {
            Pair pairCreate = Pair.create(Integer.valueOf(this.f207a), "FRESCO_PRODUCER_" + o.u(str2, ':', '_', false, 4, null));
            Object obj = pairCreate.second;
            h.e(obj, "second");
            C0518a.a(0L, (String) obj, this.f207a);
            this.f208b.put(str, pairCreate);
            this.f207a++;
        }
    }

    @Override // Q0.a, com.facebook.imagepipeline.producers.i0
    public void g(String str, String str2, Throwable th, Map map) {
        Pair pair;
        h.f(str, "requestId");
        h.f(str2, "producerName");
        h.f(th, "t");
        if (C0518a.j(0L) && (pair = (Pair) this.f208b.get(str)) != null) {
            Object obj = pair.second;
            h.e(obj, "second");
            Object obj2 = pair.first;
            h.e(obj2, "first");
            C0518a.g(0L, (String) obj, ((Number) obj2).intValue());
            this.f208b.remove(str);
        }
    }

    @Override // Q0.a, com.facebook.imagepipeline.producers.i0
    public void h(String str, String str2, Map map) {
        Pair pair;
        h.f(str, "requestId");
        h.f(str2, "producerName");
        if (C0518a.j(0L) && (pair = (Pair) this.f208b.get(str)) != null) {
            Object obj = pair.second;
            h.e(obj, "second");
            Object obj2 = pair.first;
            h.e(obj2, "first");
            C0518a.g(0L, (String) obj, ((Number) obj2).intValue());
            this.f208b.remove(str);
        }
    }

    @Override // Q0.a, Q0.e
    public void i(String str) {
        Pair pair;
        h.f(str, "requestId");
        if (C0518a.j(0L) && (pair = (Pair) this.f209c.get(str)) != null) {
            Object obj = pair.second;
            h.e(obj, "second");
            Object obj2 = pair.first;
            h.e(obj2, "first");
            C0518a.g(0L, (String) obj, ((Number) obj2).intValue());
            this.f209c.remove(str);
        }
    }

    @Override // Q0.a, Q0.e
    public void k(U0.b bVar, String str, Throwable th, boolean z3) {
        Pair pair;
        h.f(bVar, "request");
        h.f(str, "requestId");
        h.f(th, "throwable");
        if (C0518a.j(0L) && (pair = (Pair) this.f209c.get(str)) != null) {
            Object obj = pair.second;
            h.e(obj, "second");
            Object obj2 = pair.first;
            h.e(obj2, "first");
            C0518a.g(0L, (String) obj, ((Number) obj2).intValue());
            this.f209c.remove(str);
        }
    }
}
