package com.facebook.hermes.intl;

import com.facebook.hermes.intl.a;
import com.facebook.hermes.intl.g;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class Collator {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private a.d f5706a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private a.c f5707b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private boolean f5708c;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private boolean f5710e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private a.b f5711f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private B0.b f5712g;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private B0.b f5713h;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private String f5709d = "default";

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    private a f5714i = new h();

    public Collator(List<String> list, Map<String, Object> map) throws B0.e {
        a(list, map);
        this.f5714i.b(this.f5712g).c(this.f5710e).a(this.f5711f).e(this.f5707b).g(this.f5708c);
    }

    private void a(List list, Map map) throws B0.e {
        g.a aVar = g.a.STRING;
        this.f5706a = (a.d) g.d(a.d.class, B0.d.h(g.c(map, "usage", aVar, B0.a.f56e, "sort")));
        Object objQ = B0.d.q();
        B0.d.c(objQ, "localeMatcher", g.c(map, "localeMatcher", aVar, B0.a.f52a, "best fit"));
        Object objC = g.c(map, "numeric", g.a.BOOLEAN, B0.d.d(), B0.d.d());
        if (!B0.d.n(objC)) {
            objC = B0.d.r(String.valueOf(B0.d.e(objC)));
        }
        B0.d.c(objQ, "kn", objC);
        B0.d.c(objQ, "kf", g.c(map, "caseFirst", aVar, B0.a.f55d, B0.d.d()));
        HashMap mapA = f.a(list, objQ, Arrays.asList("co", "kf", "kn"));
        B0.b bVar = (B0.b) B0.d.g(mapA).get("locale");
        this.f5712g = bVar;
        this.f5713h = bVar.e();
        Object objA = B0.d.a(mapA, "co");
        if (B0.d.j(objA)) {
            objA = B0.d.r("default");
        }
        this.f5709d = B0.d.h(objA);
        Object objA2 = B0.d.a(mapA, "kn");
        if (B0.d.j(objA2)) {
            this.f5710e = false;
        } else {
            this.f5710e = Boolean.parseBoolean(B0.d.h(objA2));
        }
        Object objA3 = B0.d.a(mapA, "kf");
        if (B0.d.j(objA3)) {
            objA3 = B0.d.r("false");
        }
        this.f5711f = (a.b) g.d(a.b.class, B0.d.h(objA3));
        if (this.f5706a == a.d.SEARCH) {
            ArrayList arrayListC = this.f5712g.c("collation");
            ArrayList arrayList = new ArrayList();
            Iterator it = arrayListC.iterator();
            while (it.hasNext()) {
                arrayList.add(B0.i.e((String) it.next()));
            }
            arrayList.add(B0.i.e("search"));
            this.f5712g.g("co", arrayList);
        }
        Object objC2 = g.c(map, "sensitivity", g.a.STRING, B0.a.f54c, B0.d.d());
        if (!B0.d.n(objC2)) {
            this.f5707b = (a.c) g.d(a.c.class, B0.d.h(objC2));
        } else if (this.f5706a == a.d.SORT) {
            this.f5707b = a.c.VARIANT;
        } else {
            this.f5707b = a.c.LOCALE;
        }
        this.f5708c = B0.d.e(g.c(map, "ignorePunctuation", g.a.BOOLEAN, B0.d.d(), Boolean.FALSE));
    }

    public static List<String> supportedLocalesOf(List<String> list, Map<String, Object> map) {
        return B0.d.h(g.c(map, "localeMatcher", g.a.STRING, B0.a.f52a, "best fit")).equals("best fit") ? Arrays.asList(e.d((String[]) list.toArray(new String[list.size()]))) : Arrays.asList(e.h((String[]) list.toArray(new String[list.size()])));
    }

    public double compare(String str, String str2) {
        return this.f5714i.d(str, str2);
    }

    public Map<String, Object> resolvedOptions() {
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        linkedHashMap.put("locale", this.f5713h.a().replace("-kn-true", "-kn"));
        linkedHashMap.put("usage", this.f5706a.toString());
        a.c cVar = this.f5707b;
        if (cVar == a.c.LOCALE) {
            linkedHashMap.put("sensitivity", this.f5714i.f().toString());
        } else {
            linkedHashMap.put("sensitivity", cVar.toString());
        }
        linkedHashMap.put("ignorePunctuation", Boolean.valueOf(this.f5708c));
        linkedHashMap.put("collation", this.f5709d);
        linkedHashMap.put("numeric", Boolean.valueOf(this.f5710e));
        linkedHashMap.put("caseFirst", this.f5711f.toString());
        return linkedHashMap;
    }
}
