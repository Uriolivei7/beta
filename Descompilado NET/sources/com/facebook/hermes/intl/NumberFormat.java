package com.facebook.hermes.intl;

import com.facebook.hermes.intl.c;
import com.facebook.hermes.intl.g;
import java.text.AttributedCharacterIterator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class NumberFormat {

    /* JADX INFO: renamed from: v, reason: collision with root package name */
    private static String[] f5737v = {"acre", "bit", "byte", "celsius", "centimeter", "day", "degree", "fahrenheit", "fluid-ounce", "foot", "gallon", "gigabit", "gigabyte", "gram", "hectare", "hour", "inch", "kilobit", "kilobyte", "kilogram", "kilometer", "liter", "megabit", "megabyte", "meter", "mile", "mile-scandinavian", "milliliter", "millimeter", "millisecond", "minute", "month", "ounce", "percent", "petabyte", "pound", "second", "stone", "terabit", "terabyte", "week", "yard", "year"};

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private c.h f5738a;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private c.i f5743f;

    /* JADX INFO: renamed from: m, reason: collision with root package name */
    private c.f f5750m;

    /* JADX INFO: renamed from: p, reason: collision with root package name */
    private boolean f5753p;

    /* JADX INFO: renamed from: s, reason: collision with root package name */
    private c.b f5756s;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private String f5739b = null;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private c.EnumC0091c f5740c = c.EnumC0091c.SYMBOL;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private c.d f5741d = c.d.STANDARD;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private String f5742e = null;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private boolean f5744g = true;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private int f5745h = -1;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    private int f5746i = -1;

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    private int f5747j = -1;

    /* JADX INFO: renamed from: k, reason: collision with root package name */
    private int f5748k = -1;

    /* JADX INFO: renamed from: l, reason: collision with root package name */
    private int f5749l = -1;

    /* JADX INFO: renamed from: n, reason: collision with root package name */
    private c.g f5751n = c.g.AUTO;

    /* JADX INFO: renamed from: q, reason: collision with root package name */
    private String f5754q = null;

    /* JADX INFO: renamed from: r, reason: collision with root package name */
    private c.e f5755r = null;

    /* JADX INFO: renamed from: t, reason: collision with root package name */
    private B0.b f5757t = null;

    /* JADX INFO: renamed from: u, reason: collision with root package name */
    private B0.b f5758u = null;

    /* JADX INFO: renamed from: o, reason: collision with root package name */
    private c f5752o = new j();

    public NumberFormat(List<String> list, Map<String, Object> map) throws B0.e {
        a(list, map);
        this.f5752o.g(this.f5757t, this.f5753p ? "" : this.f5754q, this.f5738a, this.f5741d, this.f5755r, this.f5756s).j(this.f5739b, this.f5740c).k(this.f5744g).i(this.f5745h).d(this.f5750m, this.f5748k, this.f5749l).l(this.f5750m, this.f5746i, this.f5747j).h(this.f5751n).f(this.f5742e, this.f5743f);
    }

    private void a(List list, Map map) throws B0.e {
        Object objP;
        Object objP2;
        Object objQ = B0.d.q();
        g.a aVar = g.a.STRING;
        B0.d.c(objQ, "localeMatcher", g.c(map, "localeMatcher", aVar, B0.a.f52a, "best fit"));
        Object objC = g.c(map, "numberingSystem", aVar, B0.d.d(), B0.d.d());
        if (!B0.d.n(objC) && !b(B0.d.h(objC))) {
            throw new B0.e("Invalid numbering system !");
        }
        B0.d.c(objQ, "nu", objC);
        HashMap mapA = f.a(list, objQ, Collections.singletonList("nu"));
        B0.b bVar = (B0.b) B0.d.g(mapA).get("locale");
        this.f5757t = bVar;
        this.f5758u = bVar.e();
        Object objA = B0.d.a(mapA, "nu");
        if (B0.d.j(objA)) {
            this.f5753p = true;
            this.f5754q = this.f5752o.c(this.f5757t);
        } else {
            this.f5753p = false;
            this.f5754q = B0.d.h(objA);
        }
        h(map);
        if (this.f5738a == c.h.CURRENCY) {
            double dN = j.n(this.f5739b);
            objP = B0.d.p(dN);
            objP2 = B0.d.p(dN);
        } else {
            objP = B0.d.p(0.0d);
            objP2 = this.f5738a == c.h.PERCENT ? B0.d.p(0.0d) : B0.d.p(3.0d);
        }
        this.f5755r = (c.e) g.d(c.e.class, B0.d.h(g.c(map, "notation", aVar, new String[]{"standard", "scientific", "engineering", "compact"}, "standard")));
        g(map, objP, objP2);
        Object objC2 = g.c(map, "compactDisplay", aVar, new String[]{"short", "long"}, "short");
        if (this.f5755r == c.e.COMPACT) {
            this.f5756s = (c.b) g.d(c.b.class, B0.d.h(objC2));
        }
        this.f5744g = B0.d.e(g.c(map, "useGrouping", g.a.BOOLEAN, B0.d.d(), B0.d.o(true)));
        this.f5751n = (c.g) g.d(c.g.class, B0.d.h(g.c(map, "signDisplay", aVar, new String[]{"auto", "never", "always", "exceptZero"}, "auto")));
    }

    private boolean b(String str) {
        return B0.c.e(str, 0, str.length() - 1);
    }

    private boolean c(String str) {
        return Arrays.binarySearch(f5737v, str) >= 0;
    }

    private boolean d(String str) {
        return f(str).matches("^[A-Z][A-Z][A-Z]$");
    }

    private boolean e(String str) {
        if (c(str)) {
            return true;
        }
        int iIndexOf = str.indexOf("-per-");
        return iIndexOf >= 0 && str.indexOf("-per-", iIndexOf + 1) < 0 && c(str.substring(0, iIndexOf)) && c(str.substring(iIndexOf + 5));
    }

    private String f(String str) {
        StringBuilder sb = new StringBuilder(str.length());
        for (int i3 = 0; i3 < str.length(); i3++) {
            char cCharAt = str.charAt(i3);
            if (cCharAt < 'a' || cCharAt > 'z') {
                sb.append(cCharAt);
            } else {
                sb.append((char) (cCharAt - ' '));
            }
        }
        return sb.toString();
    }

    private void g(Map map, Object obj, Object obj2) throws B0.e {
        Object objB = g.b(map, "minimumIntegerDigits", B0.d.p(1.0d), B0.d.p(21.0d), B0.d.p(1.0d));
        Object objA = B0.d.a(map, "minimumFractionDigits");
        Object objA2 = B0.d.a(map, "maximumFractionDigits");
        Object objA3 = B0.d.a(map, "minimumSignificantDigits");
        Object objA4 = B0.d.a(map, "maximumSignificantDigits");
        this.f5745h = (int) Math.floor(B0.d.f(objB));
        if (!B0.d.n(objA3) || !B0.d.n(objA4)) {
            this.f5750m = c.f.SIGNIFICANT_DIGITS;
            Object objA5 = g.a("minimumSignificantDigits", objA3, B0.d.p(1.0d), B0.d.p(21.0d), B0.d.p(1.0d));
            Object objA6 = g.a("maximumSignificantDigits", objA4, objA5, B0.d.p(21.0d), B0.d.p(21.0d));
            this.f5748k = (int) Math.floor(B0.d.f(objA5));
            this.f5749l = (int) Math.floor(B0.d.f(objA6));
            return;
        }
        if (B0.d.n(objA) && B0.d.n(objA2)) {
            c.e eVar = this.f5755r;
            if (eVar == c.e.COMPACT) {
                this.f5750m = c.f.COMPACT_ROUNDING;
                return;
            }
            if (eVar == c.e.ENGINEERING) {
                this.f5750m = c.f.FRACTION_DIGITS;
                this.f5747j = 5;
                return;
            } else {
                this.f5750m = c.f.FRACTION_DIGITS;
                this.f5746i = (int) Math.floor(B0.d.f(obj));
                this.f5747j = (int) Math.floor(B0.d.f(obj2));
                return;
            }
        }
        this.f5750m = c.f.FRACTION_DIGITS;
        Object objA7 = g.a("minimumFractionDigits", objA, B0.d.p(0.0d), B0.d.p(20.0d), B0.d.d());
        Object objA8 = g.a("maximumFractionDigits", objA2, B0.d.p(0.0d), B0.d.p(20.0d), B0.d.d());
        if (B0.d.n(objA7)) {
            objA7 = B0.d.p(Math.min(B0.d.f(obj), B0.d.f(objA8)));
        } else if (B0.d.n(objA8)) {
            objA8 = B0.d.p(Math.max(B0.d.f(obj2), B0.d.f(objA7)));
        } else if (B0.d.f(objA7) > B0.d.f(objA8)) {
            throw new B0.e("minimumFractionDigits is greater than maximumFractionDigits");
        }
        this.f5746i = (int) Math.floor(B0.d.f(objA7));
        this.f5747j = (int) Math.floor(B0.d.f(objA8));
    }

    private void h(Map map) throws B0.e {
        g.a aVar = g.a.STRING;
        this.f5738a = (c.h) g.d(c.h.class, B0.d.h(g.c(map, "style", aVar, new String[]{"decimal", "percent", "currency", "unit"}, "decimal")));
        Object objC = g.c(map, "currency", aVar, B0.d.d(), B0.d.d());
        if (B0.d.n(objC)) {
            if (this.f5738a == c.h.CURRENCY) {
                throw new B0.e("Expected currency style !");
            }
        } else if (!d(B0.d.h(objC))) {
            throw new B0.e("Malformed currency code !");
        }
        Object objC2 = g.c(map, "currencyDisplay", aVar, new String[]{"symbol", "narrowSymbol", "code", "name"}, "symbol");
        Object objC3 = g.c(map, "currencySign", aVar, new String[]{"accounting", "standard"}, "standard");
        Object objC4 = g.c(map, "unit", aVar, B0.d.d(), B0.d.d());
        if (B0.d.n(objC4)) {
            if (this.f5738a == c.h.UNIT) {
                throw new B0.e("Expected unit !");
            }
        } else if (!e(B0.d.h(objC4))) {
            throw new B0.e("Malformed unit identifier !");
        }
        Object objC5 = g.c(map, "unitDisplay", aVar, new String[]{"long", "short", "narrow"}, "short");
        c.h hVar = this.f5738a;
        if (hVar == c.h.CURRENCY) {
            this.f5739b = f(B0.d.h(objC));
            this.f5740c = (c.EnumC0091c) g.d(c.EnumC0091c.class, B0.d.h(objC2));
            this.f5741d = (c.d) g.d(c.d.class, B0.d.h(objC3));
        } else if (hVar == c.h.UNIT) {
            this.f5742e = B0.d.h(objC4);
            this.f5743f = (c.i) g.d(c.i.class, B0.d.h(objC5));
        }
    }

    public static List<String> supportedLocalesOf(List<String> list, Map<String, Object> map) {
        String strH = B0.d.h(g.c(map, "localeMatcher", g.a.STRING, B0.a.f52a, "best fit"));
        String[] strArr = new String[list.size()];
        return strH.equals("best fit") ? Arrays.asList(e.d((String[]) list.toArray(strArr))) : Arrays.asList(e.h((String[]) list.toArray(strArr)));
    }

    public String format(double d4) {
        return this.f5752o.b(d4);
    }

    public List<Map<String, String>> formatToParts(double d4) {
        ArrayList arrayList = new ArrayList();
        AttributedCharacterIterator attributedCharacterIteratorA = this.f5752o.a(d4);
        StringBuilder sb = new StringBuilder();
        for (char cFirst = attributedCharacterIteratorA.first(); cFirst != 65535; cFirst = attributedCharacterIteratorA.next()) {
            sb.append(cFirst);
            if (attributedCharacterIteratorA.getIndex() + 1 == attributedCharacterIteratorA.getRunLimit()) {
                Iterator<AttributedCharacterIterator.Attribute> it = attributedCharacterIteratorA.getAttributes().keySet().iterator();
                String strE = it.hasNext() ? this.f5752o.e(it.next(), d4) : "literal";
                String string = sb.toString();
                sb.setLength(0);
                HashMap map = new HashMap();
                map.put("type", strE);
                map.put("value", string);
                arrayList.add(map);
            }
        }
        return arrayList;
    }

    public Map<String, Object> resolvedOptions() {
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        linkedHashMap.put("locale", this.f5758u.a());
        linkedHashMap.put("numberingSystem", this.f5754q);
        linkedHashMap.put("style", this.f5738a.toString());
        c.h hVar = this.f5738a;
        if (hVar == c.h.CURRENCY) {
            linkedHashMap.put("currency", this.f5739b);
            linkedHashMap.put("currencyDisplay", this.f5740c.toString());
            linkedHashMap.put("currencySign", this.f5741d.toString());
        } else if (hVar == c.h.UNIT) {
            linkedHashMap.put("unit", this.f5742e);
            linkedHashMap.put("unitDisplay", this.f5743f.toString());
        }
        int i3 = this.f5745h;
        if (i3 != -1) {
            linkedHashMap.put("minimumIntegerDigits", Integer.valueOf(i3));
        }
        c.f fVar = this.f5750m;
        if (fVar == c.f.SIGNIFICANT_DIGITS) {
            int i4 = this.f5749l;
            if (i4 != -1) {
                linkedHashMap.put("maximumSignificantDigits", Integer.valueOf(i4));
            }
            int i5 = this.f5748k;
            if (i5 != -1) {
                linkedHashMap.put("minimumSignificantDigits", Integer.valueOf(i5));
            }
        } else if (fVar == c.f.FRACTION_DIGITS) {
            int i6 = this.f5746i;
            if (i6 != -1) {
                linkedHashMap.put("minimumFractionDigits", Integer.valueOf(i6));
            }
            int i7 = this.f5747j;
            if (i7 != -1) {
                linkedHashMap.put("maximumFractionDigits", Integer.valueOf(i7));
            }
        }
        linkedHashMap.put("useGrouping", Boolean.valueOf(this.f5744g));
        linkedHashMap.put("notation", this.f5755r.toString());
        if (this.f5755r == c.e.COMPACT) {
            linkedHashMap.put("compactDisplay", this.f5756s.toString());
        }
        linkedHashMap.put("signDisplay", this.f5751n.toString());
        return linkedHashMap;
    }
}
