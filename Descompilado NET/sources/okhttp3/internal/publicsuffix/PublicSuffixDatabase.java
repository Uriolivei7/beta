package okhttp3.internal.publicsuffix;

import D2.h;
import J2.d;
import K2.o;
import N2.c;
import W2.j;
import b3.k;
import b3.q;
import b3.t;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.net.IDN;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;
import kotlin.jvm.internal.DefaultConstructorMarker;
import r2.r;
import s2.AbstractC0717n;

/* JADX INFO: loaded from: classes.dex */
public final class PublicSuffixDatabase {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final AtomicBoolean f10205a = new AtomicBoolean(false);

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final CountDownLatch f10206b = new CountDownLatch(1);

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private byte[] f10207c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private byte[] f10208d;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    public static final a f10204h = new a(null);

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private static final byte[] f10201e = {(byte) 42};

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private static final List f10202f = AbstractC0717n.b("*");

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private static final PublicSuffixDatabase f10203g = new PublicSuffixDatabase();

    public static final class a {
        private a() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public final String b(byte[] bArr, byte[][] bArr2, int i3) {
            int i4;
            int iB;
            boolean z3;
            int iB2;
            int length = bArr.length;
            int i5 = 0;
            while (i5 < length) {
                int i6 = (i5 + length) / 2;
                while (i6 > -1 && bArr[i6] != ((byte) 10)) {
                    i6--;
                }
                int i7 = i6 + 1;
                int i8 = 1;
                while (true) {
                    i4 = i7 + i8;
                    if (bArr[i4] == ((byte) 10)) {
                        break;
                    }
                    i8++;
                }
                int i9 = i4 - i7;
                int i10 = i3;
                boolean z4 = false;
                int i11 = 0;
                int i12 = 0;
                while (true) {
                    if (z4) {
                        iB = 46;
                        z3 = false;
                    } else {
                        boolean z5 = z4;
                        iB = c.b(bArr2[i10][i11], 255);
                        z3 = z5;
                    }
                    iB2 = iB - c.b(bArr[i7 + i12], 255);
                    if (iB2 != 0) {
                        break;
                    }
                    i12++;
                    i11++;
                    if (i12 == i9) {
                        break;
                    }
                    if (bArr2[i10].length != i11) {
                        z4 = z3;
                    } else {
                        if (i10 == bArr2.length - 1) {
                            break;
                        }
                        i10++;
                        z4 = true;
                        i11 = -1;
                    }
                }
                if (iB2 >= 0) {
                    if (iB2 <= 0) {
                        int i13 = i9 - i12;
                        int length2 = bArr2[i10].length - i11;
                        int length3 = bArr2.length;
                        for (int i14 = i10 + 1; i14 < length3; i14++) {
                            length2 += bArr2[i14].length;
                        }
                        if (length2 >= i13) {
                            if (length2 <= i13) {
                                Charset charset = StandardCharsets.UTF_8;
                                h.e(charset, "UTF_8");
                                return new String(bArr, i7, i9, charset);
                            }
                        }
                    }
                    i5 = i4 + 1;
                }
                length = i6;
            }
            return null;
        }

        public final PublicSuffixDatabase c() {
            return PublicSuffixDatabase.f10203g;
        }

        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }

    private final List b(List list) {
        String str;
        String strB;
        String str2;
        List listG;
        List listG2;
        if (this.f10205a.get() || !this.f10205a.compareAndSet(false, true)) {
            try {
                this.f10206b.await();
            } catch (InterruptedException unused) {
                Thread.currentThread().interrupt();
            }
        } else {
            e();
        }
        if (!(this.f10207c != null)) {
            throw new IllegalStateException("Unable to load publicsuffixes.gz resource from the classpath.");
        }
        int size = list.size();
        byte[][] bArr = new byte[size][];
        for (int i3 = 0; i3 < size; i3++) {
            String str3 = (String) list.get(i3);
            Charset charset = StandardCharsets.UTF_8;
            h.e(charset, "UTF_8");
            if (str3 == null) {
                throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
            }
            byte[] bytes = str3.getBytes(charset);
            h.e(bytes, "(this as java.lang.String).getBytes(charset)");
            bArr[i3] = bytes;
        }
        int i4 = 0;
        while (true) {
            str = null;
            if (i4 >= size) {
                strB = null;
                break;
            }
            a aVar = f10204h;
            byte[] bArr2 = this.f10207c;
            if (bArr2 == null) {
                h.s("publicSuffixListBytes");
            }
            strB = aVar.b(bArr2, bArr, i4);
            if (strB != null) {
                break;
            }
            i4++;
        }
        if (size > 1) {
            byte[][] bArr3 = (byte[][]) bArr.clone();
            int length = bArr3.length - 1;
            for (int i5 = 0; i5 < length; i5++) {
                bArr3[i5] = f10201e;
                a aVar2 = f10204h;
                byte[] bArr4 = this.f10207c;
                if (bArr4 == null) {
                    h.s("publicSuffixListBytes");
                }
                String strB2 = aVar2.b(bArr4, bArr3, i5);
                if (strB2 != null) {
                    str2 = strB2;
                    break;
                }
            }
            str2 = null;
        } else {
            str2 = null;
        }
        if (str2 != null) {
            int i6 = size - 1;
            int i7 = 0;
            while (true) {
                if (i7 >= i6) {
                    break;
                }
                a aVar3 = f10204h;
                byte[] bArr5 = this.f10208d;
                if (bArr5 == null) {
                    h.s("publicSuffixExceptionListBytes");
                }
                String strB3 = aVar3.b(bArr5, bArr, i7);
                if (strB3 != null) {
                    str = strB3;
                    break;
                }
                i7++;
            }
        }
        if (str != null) {
            return o.k0('!' + str, new char[]{'.'}, false, 0, 6, null);
        }
        if (strB == null && str2 == null) {
            return f10202f;
        }
        if (strB == null || (listG = o.k0(strB, new char[]{'.'}, false, 0, 6, null)) == null) {
            listG = AbstractC0717n.g();
        }
        if (str2 == null || (listG2 = o.k0(str2, new char[]{'.'}, false, 0, 6, null)) == null) {
            listG2 = AbstractC0717n.g();
        }
        return listG.size() > listG2.size() ? listG : listG2;
    }

    private final void d() throws IOException {
        InputStream resourceAsStream = PublicSuffixDatabase.class.getResourceAsStream("publicsuffixes.gz");
        if (resourceAsStream == null) {
            return;
        }
        k kVarD = t.d(new q(t.l(resourceAsStream)));
        try {
            byte[] bArrM = kVarD.M(kVarD.B());
            byte[] bArrM2 = kVarD.M(kVarD.B());
            r rVar = r.f10584a;
            A2.a.a(kVarD, null);
            synchronized (this) {
                h.c(bArrM);
                this.f10207c = bArrM;
                h.c(bArrM2);
                this.f10208d = bArrM2;
            }
            this.f10206b.countDown();
        } finally {
        }
    }

    private final void e() {
        boolean z3 = false;
        while (true) {
            try {
                try {
                    d();
                    break;
                } catch (InterruptedIOException unused) {
                    Thread.interrupted();
                    z3 = true;
                } catch (IOException e4) {
                    j.f2732c.g().k("Failed to read public suffix list", 5, e4);
                    if (z3) {
                        Thread.currentThread().interrupt();
                        return;
                    }
                    return;
                }
            } catch (Throwable th) {
                if (z3) {
                    Thread.currentThread().interrupt();
                }
                throw th;
            }
        }
        if (z3) {
            Thread.currentThread().interrupt();
        }
    }

    private final List f(String str) {
        List listK0 = o.k0(str, new char[]{'.'}, false, 0, 6, null);
        return h.b((String) AbstractC0717n.T(listK0), "") ? AbstractC0717n.K(listK0, 1) : listK0;
    }

    public final String c(String str) {
        int size;
        int size2;
        h.f(str, "domain");
        String unicode = IDN.toUnicode(str);
        h.e(unicode, "unicodeDomain");
        List listF = f(unicode);
        List listB = b(listF);
        if (listF.size() == listB.size() && ((String) listB.get(0)).charAt(0) != '!') {
            return null;
        }
        if (((String) listB.get(0)).charAt(0) == '!') {
            size = listF.size();
            size2 = listB.size();
        } else {
            size = listF.size();
            size2 = listB.size() + 1;
        }
        return d.e(d.b(AbstractC0717n.H(f(str)), size - size2), ".", null, null, 0, null, null, 62, null);
    }
}
