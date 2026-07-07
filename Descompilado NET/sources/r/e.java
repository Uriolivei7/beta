package R;

import f0.C0532c;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public final class e {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static final e f1906a = new e();

    private e() {
    }

    public static final String a(d dVar) {
        D2.h.f(dVar, "key");
        try {
            if (!(dVar instanceof f)) {
                return f1906a.c(dVar);
            }
            List listD = ((f) dVar).d();
            D2.h.e(listD, "getCacheKeys(...)");
            e eVar = f1906a;
            Object obj = listD.get(0);
            D2.h.e(obj, "get(...)");
            return eVar.c((d) obj);
        } catch (UnsupportedEncodingException e4) {
            throw new RuntimeException(e4);
        }
    }

    public static final List b(d dVar) {
        ArrayList arrayList;
        D2.h.f(dVar, "key");
        try {
            if (dVar instanceof f) {
                List listD = ((f) dVar).d();
                D2.h.e(listD, "getCacheKeys(...)");
                arrayList = new ArrayList(listD.size());
                int size = listD.size();
                for (int i3 = 0; i3 < size; i3++) {
                    e eVar = f1906a;
                    Object obj = listD.get(i3);
                    D2.h.e(obj, "get(...)");
                    arrayList.add(eVar.c((d) obj));
                }
            } else {
                arrayList = new ArrayList(1);
                arrayList.add(dVar.a() ? dVar.c() : f1906a.c(dVar));
            }
            return arrayList;
        } catch (UnsupportedEncodingException e4) {
            throw new RuntimeException(e4);
        }
    }

    private final String c(d dVar) {
        String strC = dVar.c();
        D2.h.e(strC, "getUriString(...)");
        Charset charsetForName = Charset.forName("UTF-8");
        D2.h.e(charsetForName, "forName(...)");
        byte[] bytes = strC.getBytes(charsetForName);
        D2.h.e(bytes, "getBytes(...)");
        String strA = C0532c.a(bytes);
        D2.h.e(strA, "makeSHA1HashBase64(...)");
        return strA;
    }
}
