package com.facebook.hermes.intl;

import com.facebook.hermes.intl.e;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class f {
    public static HashMap a(List list, Object obj, List list2) {
        HashMap map = new HashMap();
        e.a aVarF = B0.d.h(B0.d.a(obj, "localeMatcher")).equals("lookup") ? e.f((String[]) list.toArray(new String[list.size()])) : e.c((String[]) list.toArray(new String[list.size()]));
        HashSet<String> hashSet = new HashSet();
        Iterator it = list2.iterator();
        while (it.hasNext()) {
            String str = (String) it.next();
            Object objB = B0.d.b();
            Object obj2 = objB;
            if (!aVarF.f5896b.isEmpty()) {
                obj2 = objB;
                if (aVarF.f5896b.containsKey(str)) {
                    String str2 = (String) aVarF.f5896b.get(str);
                    boolean zIsEmpty = str2.isEmpty();
                    Object objR = str2;
                    if (zIsEmpty) {
                        objR = B0.d.r("true");
                    }
                    hashSet.add(str);
                    obj2 = objR;
                }
            }
            Object obj3 = obj2;
            if (B0.d.g(obj).containsKey(str)) {
                Object objA = B0.d.a(obj, str);
                boolean zM = B0.d.m(objA);
                Object objO = objA;
                if (zM) {
                    boolean zIsEmpty2 = B0.d.h(objA).isEmpty();
                    objO = objA;
                    if (zIsEmpty2) {
                        objO = B0.d.o(true);
                    }
                }
                obj3 = obj2;
                if (!B0.d.n(objO)) {
                    boolean zEquals = objO.equals(obj2);
                    obj3 = obj2;
                    if (!zEquals) {
                        hashSet.remove(str);
                        obj3 = objO;
                    }
                }
            }
            boolean zJ = B0.d.j(obj3);
            Object objF = obj3;
            if (!zJ) {
                objF = B0.i.f(str, obj3);
            }
            if (!B0.d.m(objF) || B0.i.c(str, B0.d.h(objF), aVarF.f5895a)) {
                map.put(str, objF);
            } else {
                map.put(str, B0.d.b());
            }
        }
        for (String str3 : hashSet) {
            ArrayList arrayList = new ArrayList();
            String strH = B0.d.h(B0.i.f(str3, B0.d.r((String) aVarF.f5896b.get(str3))));
            if (!B0.d.m(strH) || B0.i.c(str3, B0.d.h(strH), aVarF.f5895a)) {
                arrayList.add(strH);
                aVarF.f5895a.g(str3, arrayList);
            }
        }
        map.put("locale", aVarF.f5895a);
        return map;
    }
}
