package b3;

import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.Arrays;
import kotlin.jvm.internal.DefaultConstructorMarker;
import s2.AbstractC0711h;

/* JADX INFO: loaded from: classes.dex */
public class l implements Serializable, Comparable {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private transient int f5640b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private transient String f5641c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private final byte[] f5642d;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    public static final a f5639f = new a(null);

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    public static final l f5638e = new l(new byte[0]);

    public static final class a {
        private a() {
        }

        public static /* synthetic */ l h(a aVar, byte[] bArr, int i3, int i4, int i5, Object obj) {
            if ((i5 & 1) != 0) {
                i3 = 0;
            }
            if ((i5 & 2) != 0) {
                i4 = bArr.length;
            }
            return aVar.g(bArr, i3, i4);
        }

        public final l a(String str) {
            D2.h.f(str, "string");
            return b(str);
        }

        public final l b(String str) {
            D2.h.f(str, "$this$decodeBase64");
            byte[] bArrA = AbstractC0318a.a(str);
            if (bArrA != null) {
                return new l(bArrA);
            }
            return null;
        }

        public final l c(String str) {
            D2.h.f(str, "$this$decodeHex");
            if (!(str.length() % 2 == 0)) {
                throw new IllegalArgumentException(("Unexpected hex string: " + str).toString());
            }
            int length = str.length() / 2;
            byte[] bArr = new byte[length];
            for (int i3 = 0; i3 < length; i3++) {
                int i4 = i3 * 2;
                bArr[i3] = (byte) ((c3.b.g(str.charAt(i4)) << 4) + c3.b.g(str.charAt(i4 + 1)));
            }
            return new l(bArr);
        }

        public final l d(String str, Charset charset) {
            D2.h.f(str, "$this$encode");
            D2.h.f(charset, "charset");
            byte[] bytes = str.getBytes(charset);
            D2.h.e(bytes, "(this as java.lang.String).getBytes(charset)");
            return new l(bytes);
        }

        public final l e(String str) {
            D2.h.f(str, "$this$encodeUtf8");
            l lVar = new l(AbstractC0322e.a(str));
            lVar.s(str);
            return lVar;
        }

        public final l f(byte... bArr) {
            D2.h.f(bArr, "data");
            byte[] bArrCopyOf = Arrays.copyOf(bArr, bArr.length);
            D2.h.e(bArrCopyOf, "java.util.Arrays.copyOf(this, size)");
            return new l(bArrCopyOf);
        }

        public final l g(byte[] bArr, int i3, int i4) {
            D2.h.f(bArr, "$this$toByteString");
            AbstractC0323f.b(bArr.length, i3, i4);
            return new l(AbstractC0711h.i(bArr, i3, i4 + i3));
        }

        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }

    public l(byte[] bArr) {
        D2.h.f(bArr, "data");
        this.f5642d = bArr;
    }

    public static final l c(String str) {
        return f5639f.b(str);
    }

    public static final l e(String str) {
        return f5639f.e(str);
    }

    public static final l o(byte... bArr) {
        return f5639f.f(bArr);
    }

    public void A(i iVar, int i3, int i4) {
        D2.h.f(iVar, "buffer");
        c3.b.f(this, iVar, i3, i4);
    }

    public String a() {
        return AbstractC0318a.c(g(), null, 1, null);
    }

    /* JADX WARN: Code restructure failed: missing block: B:13:0x0031, code lost:
    
        if (r0 < r1) goto L9;
     */
    /* JADX WARN: Code restructure failed: missing block: B:15:0x0034, code lost:
    
        return -1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:18:?, code lost:
    
        return 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:8:0x0028, code lost:
    
        if (r7 < r8) goto L9;
     */
    @Override // java.lang.Comparable
    /* JADX INFO: renamed from: b, reason: merged with bridge method [inline-methods] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public int compareTo(b3.l r10) {
        /*
            r9 = this;
            java.lang.String r0 = "other"
            D2.h.f(r10, r0)
            int r0 = r9.v()
            int r1 = r10.v()
            int r2 = java.lang.Math.min(r0, r1)
            r3 = 0
            r4 = r3
        L13:
            r5 = -1
            r6 = 1
            if (r4 >= r2) goto L2e
            byte r7 = r9.f(r4)
            r7 = r7 & 255(0xff, float:3.57E-43)
            byte r8 = r10.f(r4)
            r8 = r8 & 255(0xff, float:3.57E-43)
            if (r7 != r8) goto L28
            int r4 = r4 + 1
            goto L13
        L28:
            if (r7 >= r8) goto L2c
        L2a:
            r3 = r5
            goto L34
        L2c:
            r3 = r6
            goto L34
        L2e:
            if (r0 != r1) goto L31
            goto L34
        L31:
            if (r0 >= r1) goto L2c
            goto L2a
        L34:
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: b3.l.compareTo(b3.l):int");
    }

    public l d(String str) {
        D2.h.f(str, "algorithm");
        return c3.b.d(this, str);
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof l) {
            l lVar = (l) obj;
            if (lVar.v() == g().length && lVar.q(0, g(), 0, g().length)) {
                return true;
            }
        }
        return false;
    }

    public final byte f(int i3) {
        return m(i3);
    }

    public final byte[] g() {
        return this.f5642d;
    }

    public final int h() {
        return this.f5640b;
    }

    public int hashCode() {
        int iH = h();
        if (iH != 0) {
            return iH;
        }
        int iHashCode = Arrays.hashCode(g());
        r(iHashCode);
        return iHashCode;
    }

    public int i() {
        return g().length;
    }

    public final String j() {
        return this.f5641c;
    }

    public String k() {
        char[] cArr = new char[g().length * 2];
        int i3 = 0;
        for (byte b4 : g()) {
            int i4 = i3 + 1;
            cArr[i3] = c3.b.h()[(b4 >> 4) & 15];
            i3 += 2;
            cArr[i4] = c3.b.h()[b4 & 15];
        }
        return new String(cArr);
    }

    public byte[] l() {
        return g();
    }

    public byte m(int i3) {
        return g()[i3];
    }

    public final l n() {
        return d("MD5");
    }

    public boolean p(int i3, l lVar, int i4, int i5) {
        D2.h.f(lVar, "other");
        return lVar.q(i4, g(), i3, i5);
    }

    public boolean q(int i3, byte[] bArr, int i4, int i5) {
        D2.h.f(bArr, "other");
        return i3 >= 0 && i3 <= g().length - i5 && i4 >= 0 && i4 <= bArr.length - i5 && AbstractC0323f.a(g(), i3, bArr, i4, i5);
    }

    public final void r(int i3) {
        this.f5640b = i3;
    }

    public final void s(String str) {
        this.f5641c = str;
    }

    public final l t() {
        return d("SHA-1");
    }

    public String toString() {
        if (g().length == 0) {
            return "[size=0]";
        }
        int iC = c3.b.c(g(), 64);
        if (iC == -1) {
            if (g().length <= 64) {
                return "[hex=" + k() + ']';
            }
            StringBuilder sb = new StringBuilder();
            sb.append("[size=");
            sb.append(g().length);
            sb.append(" hex=");
            if (64 <= g().length) {
                sb.append((64 == g().length ? this : new l(AbstractC0711h.i(g(), 0, 64))).k());
                sb.append("…]");
                return sb.toString();
            }
            throw new IllegalArgumentException(("endIndex > length(" + g().length + ')').toString());
        }
        String strZ = z();
        if (strZ == null) {
            throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
        }
        String strSubstring = strZ.substring(0, iC);
        D2.h.e(strSubstring, "(this as java.lang.Strin…ing(startIndex, endIndex)");
        String strV = K2.o.v(K2.o.v(K2.o.v(strSubstring, "\\", "\\\\", false, 4, null), "\n", "\\n", false, 4, null), "\r", "\\r", false, 4, null);
        if (iC >= strZ.length()) {
            return "[text=" + strV + ']';
        }
        return "[size=" + g().length + " text=" + strV + "…]";
    }

    public final l u() {
        return d("SHA-256");
    }

    public final int v() {
        return i();
    }

    public final boolean w(l lVar) {
        D2.h.f(lVar, "prefix");
        return p(0, lVar, 0, lVar.v());
    }

    public l x() {
        byte b4;
        for (int i3 = 0; i3 < g().length; i3++) {
            byte b5 = g()[i3];
            byte b6 = (byte) 65;
            if (b5 >= b6 && b5 <= (b4 = (byte) 90)) {
                byte[] bArrG = g();
                byte[] bArrCopyOf = Arrays.copyOf(bArrG, bArrG.length);
                D2.h.e(bArrCopyOf, "java.util.Arrays.copyOf(this, size)");
                bArrCopyOf[i3] = (byte) (b5 + 32);
                for (int i4 = i3 + 1; i4 < bArrCopyOf.length; i4++) {
                    byte b7 = bArrCopyOf[i4];
                    if (b7 >= b6 && b7 <= b4) {
                        bArrCopyOf[i4] = (byte) (b7 + 32);
                    }
                }
                return new l(bArrCopyOf);
            }
        }
        return this;
    }

    public byte[] y() {
        byte[] bArrG = g();
        byte[] bArrCopyOf = Arrays.copyOf(bArrG, bArrG.length);
        D2.h.e(bArrCopyOf, "java.util.Arrays.copyOf(this, size)");
        return bArrCopyOf;
    }

    public String z() {
        String strJ = j();
        if (strJ != null) {
            return strJ;
        }
        String strB = AbstractC0322e.b(l());
        s(strB);
        return strB;
    }
}
