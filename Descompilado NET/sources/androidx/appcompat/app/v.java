package androidx.appcompat.app;

import java.util.LinkedHashSet;
import java.util.Locale;

/* JADX INFO: loaded from: classes.dex */
abstract class v {
    private static androidx.core.os.e a(androidx.core.os.e eVar, androidx.core.os.e eVar2) {
        LinkedHashSet linkedHashSet = new LinkedHashSet();
        int i3 = 0;
        while (i3 < eVar.f() + eVar2.f()) {
            Locale localeC = i3 < eVar.f() ? eVar.c(i3) : eVar2.c(i3 - eVar.f());
            if (localeC != null) {
                linkedHashSet.add(localeC);
            }
            i3++;
        }
        return androidx.core.os.e.a((Locale[]) linkedHashSet.toArray(new Locale[linkedHashSet.size()]));
    }

    static androidx.core.os.e b(androidx.core.os.e eVar, androidx.core.os.e eVar2) {
        return (eVar == null || eVar.e()) ? androidx.core.os.e.d() : a(eVar, eVar2);
    }
}
