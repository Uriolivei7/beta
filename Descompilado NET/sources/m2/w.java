package M2;

import M2.m;
import java.io.IOException;
import java.net.CookieHandler;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import s2.AbstractC0696D;
import s2.AbstractC0717n;

/* JADX INFO: loaded from: classes.dex */
public final class w implements n {

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final CookieHandler f1248c;

    public w(CookieHandler cookieHandler) {
        D2.h.f(cookieHandler, "cookieHandler");
        this.f1248c = cookieHandler;
    }

    private final List e(u uVar, String str) {
        ArrayList arrayList = new ArrayList();
        int length = str.length();
        int i3 = 0;
        while (i3 < length) {
            int iN = N2.c.n(str, ";,", i3, length);
            int iM = N2.c.m(str, '=', i3, iN);
            String strV = N2.c.V(str, i3, iM);
            if (!K2.o.z(strV, "$", false, 2, null)) {
                String strV2 = iM < iN ? N2.c.V(str, iM + 1, iN) : "";
                if (K2.o.z(strV2, "\"", false, 2, null) && K2.o.m(strV2, "\"", false, 2, null)) {
                    strV2 = strV2.substring(1, strV2.length() - 1);
                    D2.h.e(strV2, "(this as java.lang.Strin…ing(startIndex, endIndex)");
                }
                arrayList.add(new m.a().d(strV).e(strV2).b(uVar.h()).a());
            }
            i3 = iN + 1;
        }
        return arrayList;
    }

    @Override // M2.n
    public void a(u uVar, List list) {
        D2.h.f(uVar, "url");
        D2.h.f(list, "cookies");
        ArrayList arrayList = new ArrayList();
        Iterator it = list.iterator();
        while (it.hasNext()) {
            arrayList.add(N2.b.a((m) it.next(), true));
        }
        try {
            this.f1248c.put(uVar.q(), AbstractC0696D.d(r2.n.a("Set-Cookie", arrayList)));
        } catch (IOException e4) {
            W2.j jVarG = W2.j.f2732c.g();
            StringBuilder sb = new StringBuilder();
            sb.append("Saving cookies failed for ");
            u uVarO = uVar.o("/...");
            D2.h.c(uVarO);
            sb.append(uVarO);
            jVarG.k(sb.toString(), 5, e4);
        }
    }

    @Override // M2.n
    public List c(u uVar) {
        D2.h.f(uVar, "url");
        try {
            Map<String, List<String>> map = this.f1248c.get(uVar.q(), AbstractC0696D.f());
            D2.h.e(map, "cookieHeaders");
            ArrayList arrayList = null;
            for (Map.Entry<String, List<String>> entry : map.entrySet()) {
                String key = entry.getKey();
                List<String> value = entry.getValue();
                if (K2.o.n("Cookie", key, true) || K2.o.n("Cookie2", key, true)) {
                    D2.h.e(value, "value");
                    if (!value.isEmpty()) {
                        for (String str : value) {
                            if (arrayList == null) {
                                arrayList = new ArrayList();
                            }
                            D2.h.e(str, "header");
                            arrayList.addAll(e(uVar, str));
                        }
                    }
                }
            }
            if (arrayList == null) {
                return AbstractC0717n.g();
            }
            List listUnmodifiableList = Collections.unmodifiableList(arrayList);
            D2.h.e(listUnmodifiableList, "Collections.unmodifiableList(cookies)");
            return listUnmodifiableList;
        } catch (IOException e4) {
            W2.j jVarG = W2.j.f2732c.g();
            StringBuilder sb = new StringBuilder();
            sb.append("Loading cookies failed for ");
            u uVarO = uVar.o("/...");
            D2.h.c(uVarO);
            sb.append(uVarO);
            jVarG.k(sb.toString(), 5, e4);
            return AbstractC0717n.g();
        }
    }
}
