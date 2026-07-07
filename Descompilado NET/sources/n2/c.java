package N2;

import D2.h;
import K2.d;
import K2.k;
import K2.o;
import M2.C;
import M2.E;
import M2.InterfaceC0194e;
import M2.r;
import M2.t;
import M2.u;
import M2.z;
import b3.D;
import b3.F;
import b3.i;
import b3.j;
import b3.l;
import b3.w;
import java.io.Closeable;
import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import r2.AbstractC0678a;
import s2.AbstractC0695C;
import s2.AbstractC0696D;
import s2.AbstractC0711h;
import s2.AbstractC0717n;

/* JADX INFO: loaded from: classes.dex */
public abstract class c {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static final byte[] f1402a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public static final t f1403b = t.f1224c.h(new String[0]);

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public static final E f1404c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public static final C f1405d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private static final w f1406e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    public static final TimeZone f1407f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private static final k f1408g;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    public static final boolean f1409h;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    public static final String f1410i;

    static final class a implements r.c {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        final /* synthetic */ r f1411a;

        a(r rVar) {
            this.f1411a = rVar;
        }

        @Override // M2.r.c
        public final r a(InterfaceC0194e interfaceC0194e) {
            h.f(interfaceC0194e, "it");
            return this.f1411a;
        }
    }

    static final class b implements ThreadFactory {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        final /* synthetic */ String f1412a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        final /* synthetic */ boolean f1413b;

        b(String str, boolean z3) {
            this.f1412a = str;
            this.f1413b = z3;
        }

        @Override // java.util.concurrent.ThreadFactory
        public final Thread newThread(Runnable runnable) {
            Thread thread = new Thread(runnable, this.f1412a);
            thread.setDaemon(this.f1413b);
            return thread;
        }
    }

    static {
        byte[] bArr = new byte[0];
        f1402a = bArr;
        f1404c = E.a.d(E.f942b, bArr, null, 1, null);
        f1405d = C.a.h(C.f908a, bArr, null, 0, 0, 7, null);
        w.a aVar = w.f5661e;
        l.a aVar2 = l.f5639f;
        f1406e = aVar.d(aVar2.c("efbbbf"), aVar2.c("feff"), aVar2.c("fffe"), aVar2.c("0000ffff"), aVar2.c("ffff0000"));
        TimeZone timeZone = TimeZone.getTimeZone("GMT");
        h.c(timeZone);
        f1407f = timeZone;
        f1408g = new k("([0-9a-fA-F]*:[0-9a-fA-F:.]*)|([\\d.]+)");
        f1409h = false;
        String name = z.class.getName();
        h.e(name, "OkHttpClient::class.java.name");
        f1410i = o.e0(o.d0(name, "okhttp3."), "Client");
    }

    public static final int A(String str, int i3) {
        h.f(str, "$this$indexOfNonWhitespace");
        int length = str.length();
        while (i3 < length) {
            char cCharAt = str.charAt(i3);
            if (cCharAt != ' ' && cCharAt != '\t') {
                return i3;
            }
            i3++;
        }
        return str.length();
    }

    public static final String[] B(String[] strArr, String[] strArr2, Comparator comparator) {
        h.f(strArr, "$this$intersect");
        h.f(strArr2, "other");
        h.f(comparator, "comparator");
        ArrayList arrayList = new ArrayList();
        for (String str : strArr) {
            int length = strArr2.length;
            int i3 = 0;
            while (true) {
                if (i3 >= length) {
                    break;
                }
                if (comparator.compare(str, strArr2[i3]) == 0) {
                    arrayList.add(str);
                    break;
                }
                i3++;
            }
        }
        Object[] array = arrayList.toArray(new String[0]);
        if (array != null) {
            return (String[]) array;
        }
        throw new NullPointerException("null cannot be cast to non-null type kotlin.Array<T>");
    }

    public static final boolean C(V2.a aVar, File file) throws IOException {
        h.f(aVar, "$this$isCivilized");
        h.f(file, "file");
        D dC = aVar.c(file);
        try {
            try {
                aVar.a(file);
                A2.a.a(dC, null);
                return true;
            } catch (IOException unused) {
                r2.r rVar = r2.r.f10584a;
                A2.a.a(dC, null);
                aVar.a(file);
                return false;
            }
        } finally {
        }
    }

    public static final boolean D(Socket socket, b3.k kVar) {
        h.f(socket, "$this$isHealthy");
        h.f(kVar, "source");
        try {
            int soTimeout = socket.getSoTimeout();
            try {
                socket.setSoTimeout(1);
                boolean z3 = !kVar.J();
                socket.setSoTimeout(soTimeout);
                return z3;
            } catch (Throwable th) {
                socket.setSoTimeout(soTimeout);
                throw th;
            }
        } catch (SocketTimeoutException unused) {
            return true;
        } catch (IOException unused2) {
            return false;
        }
    }

    public static final boolean E(String str) {
        h.f(str, "name");
        return o.n(str, "Authorization", true) || o.n(str, "Cookie", true) || o.n(str, "Proxy-Authorization", true) || o.n(str, "Set-Cookie", true);
    }

    public static final int F(char c4) {
        if ('0' <= c4 && '9' >= c4) {
            return c4 - '0';
        }
        if ('a' <= c4 && 'f' >= c4) {
            return c4 - 'W';
        }
        if ('A' <= c4 && 'F' >= c4) {
            return c4 - '7';
        }
        return -1;
    }

    public static final Charset G(b3.k kVar, Charset charset) {
        h.f(kVar, "$this$readBomAsCharset");
        h.f(charset, "default");
        int iK = kVar.K(f1406e);
        if (iK == -1) {
            return charset;
        }
        if (iK == 0) {
            Charset charset2 = StandardCharsets.UTF_8;
            h.e(charset2, "UTF_8");
            return charset2;
        }
        if (iK == 1) {
            Charset charset3 = StandardCharsets.UTF_16BE;
            h.e(charset3, "UTF_16BE");
            return charset3;
        }
        if (iK == 2) {
            Charset charset4 = StandardCharsets.UTF_16LE;
            h.e(charset4, "UTF_16LE");
            return charset4;
        }
        if (iK == 3) {
            return d.f815a.a();
        }
        if (iK == 4) {
            return d.f815a.b();
        }
        throw new AssertionError();
    }

    public static final int H(b3.k kVar) {
        h.f(kVar, "$this$readMedium");
        return b(kVar.r0(), 255) | (b(kVar.r0(), 255) << 16) | (b(kVar.r0(), 255) << 8);
    }

    public static final int I(i iVar, byte b4) throws EOFException {
        h.f(iVar, "$this$skipAll");
        int i3 = 0;
        while (!iVar.J() && iVar.a0(0L) == b4) {
            i3++;
            iVar.r0();
        }
        return i3;
    }

    public static final boolean J(F f3, int i3, TimeUnit timeUnit) {
        h.f(f3, "$this$skipAll");
        h.f(timeUnit, "timeUnit");
        long jNanoTime = System.nanoTime();
        long jC = f3.f().e() ? f3.f().c() - jNanoTime : Long.MAX_VALUE;
        f3.f().d(Math.min(jC, timeUnit.toNanos(i3)) + jNanoTime);
        try {
            i iVar = new i();
            while (f3.x(iVar, 8192L) != -1) {
                iVar.v();
            }
            if (jC == Long.MAX_VALUE) {
                f3.f().a();
            } else {
                f3.f().d(jNanoTime + jC);
            }
            return true;
        } catch (InterruptedIOException unused) {
            if (jC == Long.MAX_VALUE) {
                f3.f().a();
            } else {
                f3.f().d(jNanoTime + jC);
            }
            return false;
        } catch (Throwable th) {
            if (jC == Long.MAX_VALUE) {
                f3.f().a();
            } else {
                f3.f().d(jNanoTime + jC);
            }
            throw th;
        }
    }

    public static final ThreadFactory K(String str, boolean z3) {
        h.f(str, "name");
        return new b(str, z3);
    }

    public static final List L(t tVar) {
        h.f(tVar, "$this$toHeaderList");
        H2.c cVarI = H2.d.i(0, tVar.size());
        ArrayList arrayList = new ArrayList(AbstractC0717n.q(cVarI, 10));
        Iterator it = cVarI.iterator();
        while (it.hasNext()) {
            int iA = ((AbstractC0695C) it).a();
            arrayList.add(new U2.c(tVar.b(iA), tVar.h(iA)));
        }
        return arrayList;
    }

    public static final t M(List list) {
        h.f(list, "$this$toHeaders");
        t.a aVar = new t.a();
        Iterator it = list.iterator();
        while (it.hasNext()) {
            U2.c cVar = (U2.c) it.next();
            aVar.c(cVar.a().z(), cVar.b().z());
        }
        return aVar.e();
    }

    public static final String N(int i3) {
        String hexString = Integer.toHexString(i3);
        h.e(hexString, "Integer.toHexString(this)");
        return hexString;
    }

    public static final String O(long j3) {
        String hexString = Long.toHexString(j3);
        h.e(hexString, "java.lang.Long.toHexString(this)");
        return hexString;
    }

    public static final String P(u uVar, boolean z3) {
        String strH;
        h.f(uVar, "$this$toHostHeader");
        if (o.E(uVar.h(), ":", false, 2, null)) {
            strH = '[' + uVar.h() + ']';
        } else {
            strH = uVar.h();
        }
        if (!z3 && uVar.l() == u.f1228l.c(uVar.p())) {
            return strH;
        }
        return strH + ':' + uVar.l();
    }

    public static /* synthetic */ String Q(u uVar, boolean z3, int i3, Object obj) {
        if ((i3 & 1) != 0) {
            z3 = false;
        }
        return P(uVar, z3);
    }

    public static final List R(List list) {
        h.f(list, "$this$toImmutableList");
        List listUnmodifiableList = Collections.unmodifiableList(AbstractC0717n.g0(list));
        h.e(listUnmodifiableList, "Collections.unmodifiableList(toMutableList())");
        return listUnmodifiableList;
    }

    public static final Map S(Map map) {
        h.f(map, "$this$toImmutableMap");
        if (map.isEmpty()) {
            return AbstractC0696D.f();
        }
        Map mapUnmodifiableMap = Collections.unmodifiableMap(new LinkedHashMap(map));
        h.e(mapUnmodifiableMap, "Collections.unmodifiableMap(LinkedHashMap(this))");
        return mapUnmodifiableMap;
    }

    public static final long T(String str, long j3) {
        h.f(str, "$this$toLongOrDefault");
        try {
            return Long.parseLong(str);
        } catch (NumberFormatException unused) {
            return j3;
        }
    }

    public static final int U(String str, int i3) {
        if (str != null) {
            try {
                long j3 = Long.parseLong(str);
                if (j3 > Integer.MAX_VALUE) {
                    return Integer.MAX_VALUE;
                }
                if (j3 < 0) {
                    return 0;
                }
                return (int) j3;
            } catch (NumberFormatException unused) {
            }
        }
        return i3;
    }

    public static final String V(String str, int i3, int i4) {
        h.f(str, "$this$trimSubstring");
        int iW = w(str, i3, i4);
        String strSubstring = str.substring(iW, y(str, iW, i4));
        h.e(strSubstring, "(this as java.lang.Strin…ing(startIndex, endIndex)");
        return strSubstring;
    }

    public static /* synthetic */ String W(String str, int i3, int i4, int i5, Object obj) {
        if ((i5 & 1) != 0) {
            i3 = 0;
        }
        if ((i5 & 2) != 0) {
            i4 = str.length();
        }
        return V(str, i3, i4);
    }

    public static final Throwable X(Exception exc, List list) {
        h.f(exc, "$this$withSuppressed");
        h.f(list, "suppressed");
        if (list.size() > 1) {
            System.out.println(list);
        }
        Iterator it = list.iterator();
        while (it.hasNext()) {
            AbstractC0678a.a(exc, (Exception) it.next());
        }
        return exc;
    }

    public static final void Y(j jVar, int i3) {
        h.f(jVar, "$this$writeMedium");
        jVar.L((i3 >>> 16) & 255);
        jVar.L((i3 >>> 8) & 255);
        jVar.L(i3 & 255);
    }

    public static final void a(List list, Object obj) {
        h.f(list, "$this$addIfAbsent");
        if (list.contains(obj)) {
            return;
        }
        list.add(obj);
    }

    public static final int b(byte b4, int i3) {
        return b4 & i3;
    }

    public static final int c(short s3, int i3) {
        return s3 & i3;
    }

    public static final long d(int i3, long j3) {
        return ((long) i3) & j3;
    }

    public static final r.c e(r rVar) {
        h.f(rVar, "$this$asFactory");
        return new a(rVar);
    }

    public static final boolean f(String str) {
        h.f(str, "$this$canParseAsIpAddress");
        return f1408g.b(str);
    }

    public static final boolean g(u uVar, u uVar2) {
        h.f(uVar, "$this$canReuseConnectionFor");
        h.f(uVar2, "other");
        return h.b(uVar.h(), uVar2.h()) && uVar.l() == uVar2.l() && h.b(uVar.p(), uVar2.p());
    }

    public static final int h(String str, long j3, TimeUnit timeUnit) {
        h.f(str, "name");
        if (!(j3 >= 0)) {
            throw new IllegalStateException((str + " < 0").toString());
        }
        if (!(timeUnit != null)) {
            throw new IllegalStateException("unit == null");
        }
        long millis = timeUnit.toMillis(j3);
        if (!(millis <= ((long) Integer.MAX_VALUE))) {
            throw new IllegalArgumentException((str + " too large.").toString());
        }
        if (millis != 0 || j3 <= 0) {
            return (int) millis;
        }
        throw new IllegalArgumentException((str + " too small.").toString());
    }

    public static final void i(long j3, long j4, long j5) {
        if ((j4 | j5) < 0 || j4 > j3 || j3 - j4 < j5) {
            throw new ArrayIndexOutOfBoundsException();
        }
    }

    public static final void j(Closeable closeable) {
        h.f(closeable, "$this$closeQuietly");
        try {
            closeable.close();
        } catch (RuntimeException e4) {
            throw e4;
        } catch (Exception unused) {
        }
    }

    public static final void k(Socket socket) {
        h.f(socket, "$this$closeQuietly");
        try {
            socket.close();
        } catch (AssertionError e4) {
            throw e4;
        } catch (RuntimeException e5) {
            if (!h.b(e5.getMessage(), "bio == null")) {
                throw e5;
            }
        } catch (Exception unused) {
        }
    }

    public static final String[] l(String[] strArr, String str) {
        h.f(strArr, "$this$concat");
        h.f(str, "value");
        Object[] objArrCopyOf = Arrays.copyOf(strArr, strArr.length + 1);
        h.e(objArrCopyOf, "java.util.Arrays.copyOf(this, newSize)");
        String[] strArr2 = (String[]) objArrCopyOf;
        strArr2[AbstractC0711h.r(strArr2)] = str;
        return strArr2;
    }

    public static final int m(String str, char c4, int i3, int i4) {
        h.f(str, "$this$delimiterOffset");
        while (i3 < i4) {
            if (str.charAt(i3) == c4) {
                return i3;
            }
            i3++;
        }
        return i4;
    }

    public static final int n(String str, String str2, int i3, int i4) {
        h.f(str, "$this$delimiterOffset");
        h.f(str2, "delimiters");
        while (i3 < i4) {
            if (o.D(str2, str.charAt(i3), false, 2, null)) {
                return i3;
            }
            i3++;
        }
        return i4;
    }

    public static /* synthetic */ int o(String str, char c4, int i3, int i4, int i5, Object obj) {
        if ((i5 & 2) != 0) {
            i3 = 0;
        }
        if ((i5 & 4) != 0) {
            i4 = str.length();
        }
        return m(str, c4, i3, i4);
    }

    public static final boolean p(F f3, int i3, TimeUnit timeUnit) {
        h.f(f3, "$this$discard");
        h.f(timeUnit, "timeUnit");
        try {
            return J(f3, i3, timeUnit);
        } catch (IOException unused) {
            return false;
        }
    }

    public static final String q(String str, Object... objArr) {
        h.f(str, "format");
        h.f(objArr, "args");
        D2.u uVar = D2.u.f192a;
        Locale locale = Locale.US;
        Object[] objArrCopyOf = Arrays.copyOf(objArr, objArr.length);
        String str2 = String.format(locale, str, Arrays.copyOf(objArrCopyOf, objArrCopyOf.length));
        h.e(str2, "java.lang.String.format(locale, format, *args)");
        return str2;
    }

    public static final boolean r(String[] strArr, String[] strArr2, Comparator comparator) {
        h.f(strArr, "$this$hasIntersection");
        h.f(comparator, "comparator");
        if (strArr.length != 0 && strArr2 != null && strArr2.length != 0) {
            for (String str : strArr) {
                for (String str2 : strArr2) {
                    if (comparator.compare(str, str2) == 0) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static final long s(M2.D d4) {
        h.f(d4, "$this$headersContentLength");
        String strA = d4.d0().a("Content-Length");
        if (strA != null) {
            return T(strA, -1L);
        }
        return -1L;
    }

    public static final List t(Object... objArr) {
        h.f(objArr, "elements");
        Object[] objArr2 = (Object[]) objArr.clone();
        List listUnmodifiableList = Collections.unmodifiableList(AbstractC0717n.j(Arrays.copyOf(objArr2, objArr2.length)));
        h.e(listUnmodifiableList, "Collections.unmodifiable…istOf(*elements.clone()))");
        return listUnmodifiableList;
    }

    public static final int u(String[] strArr, String str, Comparator comparator) {
        h.f(strArr, "$this$indexOf");
        h.f(str, "value");
        h.f(comparator, "comparator");
        int length = strArr.length;
        for (int i3 = 0; i3 < length; i3++) {
            if (comparator.compare(strArr[i3], str) == 0) {
                return i3;
            }
        }
        return -1;
    }

    public static final int v(String str) {
        h.f(str, "$this$indexOfControlOrNonAscii");
        int length = str.length();
        for (int i3 = 0; i3 < length; i3++) {
            char cCharAt = str.charAt(i3);
            if (h.g(cCharAt, 31) <= 0 || h.g(cCharAt, 127) >= 0) {
                return i3;
            }
        }
        return -1;
    }

    public static final int w(String str, int i3, int i4) {
        h.f(str, "$this$indexOfFirstNonAsciiWhitespace");
        while (i3 < i4) {
            char cCharAt = str.charAt(i3);
            if (cCharAt != '\t' && cCharAt != '\n' && cCharAt != '\f' && cCharAt != '\r' && cCharAt != ' ') {
                return i3;
            }
            i3++;
        }
        return i4;
    }

    public static /* synthetic */ int x(String str, int i3, int i4, int i5, Object obj) {
        if ((i5 & 1) != 0) {
            i3 = 0;
        }
        if ((i5 & 2) != 0) {
            i4 = str.length();
        }
        return w(str, i3, i4);
    }

    public static final int y(String str, int i3, int i4) {
        h.f(str, "$this$indexOfLastNonAsciiWhitespace");
        int i5 = i4 - 1;
        if (i5 >= i3) {
            while (true) {
                char cCharAt = str.charAt(i5);
                if (cCharAt != '\t' && cCharAt != '\n' && cCharAt != '\f' && cCharAt != '\r' && cCharAt != ' ') {
                    return i5 + 1;
                }
                if (i5 == i3) {
                    break;
                }
                i5--;
            }
        }
        return i3;
    }

    public static /* synthetic */ int z(String str, int i3, int i4, int i5, Object obj) {
        if ((i5 & 1) != 0) {
            i3 = 0;
        }
        if ((i5 & 2) != 0) {
            i4 = str.length();
        }
        return y(str, i3, i4);
    }
}
