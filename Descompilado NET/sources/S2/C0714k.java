package s2;

import java.util.Arrays;
import java.util.List;

/* JADX INFO: Access modifiers changed from: package-private */
/* JADX INFO: renamed from: s2.k, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public class C0714k extends C0713j {
    public static List d(Object[] objArr) {
        D2.h.f(objArr, "<this>");
        List listA = AbstractC0716m.a(objArr);
        D2.h.e(listA, "asList(...)");
        return listA;
    }

    public static byte[] e(byte[] bArr, byte[] bArr2, int i3, int i4, int i5) {
        D2.h.f(bArr, "<this>");
        D2.h.f(bArr2, "destination");
        System.arraycopy(bArr, i4, bArr2, i3, i5 - i4);
        return bArr2;
    }

    public static final Object[] f(Object[] objArr, Object[] objArr2, int i3, int i4, int i5) {
        D2.h.f(objArr, "<this>");
        D2.h.f(objArr2, "destination");
        System.arraycopy(objArr, i4, objArr2, i3, i5 - i4);
        return objArr2;
    }

    public static /* synthetic */ byte[] g(byte[] bArr, byte[] bArr2, int i3, int i4, int i5, int i6, Object obj) {
        if ((i6 & 2) != 0) {
            i3 = 0;
        }
        if ((i6 & 4) != 0) {
            i4 = 0;
        }
        if ((i6 & 8) != 0) {
            i5 = bArr.length;
        }
        return AbstractC0711h.e(bArr, bArr2, i3, i4, i5);
    }

    public static /* synthetic */ Object[] h(Object[] objArr, Object[] objArr2, int i3, int i4, int i5, int i6, Object obj) {
        if ((i6 & 2) != 0) {
            i3 = 0;
        }
        if ((i6 & 4) != 0) {
            i4 = 0;
        }
        if ((i6 & 8) != 0) {
            i5 = objArr.length;
        }
        return f(objArr, objArr2, i3, i4, i5);
    }

    public static byte[] i(byte[] bArr, int i3, int i4) {
        D2.h.f(bArr, "<this>");
        C0712i.b(i4, bArr.length);
        byte[] bArrCopyOfRange = Arrays.copyOfRange(bArr, i3, i4);
        D2.h.e(bArrCopyOfRange, "copyOfRange(...)");
        return bArrCopyOfRange;
    }

    public static void j(Object[] objArr, Object obj, int i3, int i4) {
        D2.h.f(objArr, "<this>");
        Arrays.fill(objArr, i3, i4, obj);
    }

    public static /* synthetic */ void k(Object[] objArr, Object obj, int i3, int i4, int i5, Object obj2) {
        if ((i5 & 2) != 0) {
            i3 = 0;
        }
        if ((i5 & 4) != 0) {
            i4 = objArr.length;
        }
        AbstractC0711h.j(objArr, obj, i3, i4);
    }
}
