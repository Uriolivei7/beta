package c3;

import D2.h;
import b3.C;
import b3.i;
import b3.l;

/* JADX INFO: loaded from: classes.dex */
public abstract class b {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private static final char[] f5701a = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code restructure failed: missing block: B:39:0x005b, code lost:
    
        return -1;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static final int c(byte[] r18, int r19) {
        /*
            Method dump skipped, instruction units count: 425
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: c3.b.c(byte[], int):int");
    }

    public static final l d(l lVar, String str) {
        h.f(lVar, "$this$commonDigest");
        h.f(str, "algorithm");
        c cVarA = d.a(str);
        cVarA.b(lVar.g(), 0, lVar.v());
        return new l(cVarA.a());
    }

    public static final l e(C c4, String str) {
        h.f(c4, "$this$commonSegmentDigest");
        h.f(str, "algorithm");
        c cVarA = d.a(str);
        int length = c4.C().length;
        int i3 = 0;
        int i4 = 0;
        while (i3 < length) {
            int i5 = c4.B()[length + i3];
            int i6 = c4.B()[i3];
            cVarA.b(c4.C()[i3], i5, i6 - i4);
            i3++;
            i4 = i6;
        }
        return new l(cVarA.a());
    }

    public static final void f(l lVar, i iVar, int i3, int i4) {
        h.f(lVar, "$this$commonWrite");
        h.f(iVar, "buffer");
        iVar.k(lVar.g(), i3, i4);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final int g(char c4) {
        if ('0' <= c4 && '9' >= c4) {
            return c4 - '0';
        }
        if ('a' <= c4 && 'f' >= c4) {
            return c4 - 'W';
        }
        if ('A' <= c4 && 'F' >= c4) {
            return c4 - '7';
        }
        throw new IllegalArgumentException("Unexpected hex digit: " + c4);
    }

    public static final char[] h() {
        return f5701a;
    }
}
