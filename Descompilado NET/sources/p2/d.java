package P2;

import C2.l;
import D2.h;
import D2.i;
import K2.k;
import b3.D;
import b3.F;
import b3.j;
import b3.o;
import b3.t;
import java.io.Closeable;
import java.io.EOFException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.Flushable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import kotlin.jvm.internal.DefaultConstructorMarker;
import r2.C0680c;
import r2.r;

/* JADX INFO: loaded from: classes.dex */
public final class d implements Closeable, Flushable {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private long f1745b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final File f1746c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private final File f1747d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private final File f1748e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private long f1749f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private j f1750g;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private final LinkedHashMap f1751h;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    private int f1752i;

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    private boolean f1753j;

    /* JADX INFO: renamed from: k, reason: collision with root package name */
    private boolean f1754k;

    /* JADX INFO: renamed from: l, reason: collision with root package name */
    private boolean f1755l;

    /* JADX INFO: renamed from: m, reason: collision with root package name */
    private boolean f1756m;

    /* JADX INFO: renamed from: n, reason: collision with root package name */
    private boolean f1757n;

    /* JADX INFO: renamed from: o, reason: collision with root package name */
    private boolean f1758o;

    /* JADX INFO: renamed from: p, reason: collision with root package name */
    private long f1759p;

    /* JADX INFO: renamed from: q, reason: collision with root package name */
    private final Q2.d f1760q;

    /* JADX INFO: renamed from: r, reason: collision with root package name */
    private final e f1761r;

    /* JADX INFO: renamed from: s, reason: collision with root package name */
    private final V2.a f1762s;

    /* JADX INFO: renamed from: t, reason: collision with root package name */
    private final File f1763t;

    /* JADX INFO: renamed from: u, reason: collision with root package name */
    private final int f1764u;

    /* JADX INFO: renamed from: v, reason: collision with root package name */
    private final int f1765v;

    /* JADX INFO: renamed from: H, reason: collision with root package name */
    public static final a f1740H = new a(null);

    /* JADX INFO: renamed from: w, reason: collision with root package name */
    public static final String f1741w = "journal";

    /* JADX INFO: renamed from: x, reason: collision with root package name */
    public static final String f1742x = "journal.tmp";

    /* JADX INFO: renamed from: y, reason: collision with root package name */
    public static final String f1743y = "journal.bkp";

    /* JADX INFO: renamed from: z, reason: collision with root package name */
    public static final String f1744z = "libcore.io.DiskLruCache";

    /* JADX INFO: renamed from: A, reason: collision with root package name */
    public static final String f1733A = "1";

    /* JADX INFO: renamed from: B, reason: collision with root package name */
    public static final long f1734B = -1;

    /* JADX INFO: renamed from: C, reason: collision with root package name */
    public static final k f1735C = new k("[a-z0-9_-]{1,120}");

    /* JADX INFO: renamed from: D, reason: collision with root package name */
    public static final String f1736D = "CLEAN";

    /* JADX INFO: renamed from: E, reason: collision with root package name */
    public static final String f1737E = "DIRTY";

    /* JADX INFO: renamed from: F, reason: collision with root package name */
    public static final String f1738F = "REMOVE";

    /* JADX INFO: renamed from: G, reason: collision with root package name */
    public static final String f1739G = "READ";

    public static final class a {
        private a() {
        }

        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }

    public final class b {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        private final boolean[] f1766a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        private boolean f1767b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        private final c f1768c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        final /* synthetic */ d f1769d;

        static final class a extends i implements l {

            /* JADX INFO: renamed from: d, reason: collision with root package name */
            final /* synthetic */ int f1771d;

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            a(int i3) {
                super(1);
                this.f1771d = i3;
            }

            @Override // C2.l
            public /* bridge */ /* synthetic */ Object d(Object obj) {
                e((IOException) obj);
                return r.f10584a;
            }

            public final void e(IOException iOException) {
                h.f(iOException, "it");
                synchronized (b.this.f1769d) {
                    b.this.c();
                    r rVar = r.f10584a;
                }
            }
        }

        public b(d dVar, c cVar) {
            h.f(cVar, "entry");
            this.f1769d = dVar;
            this.f1768c = cVar;
            this.f1766a = cVar.g() ? null : new boolean[dVar.u0()];
        }

        public final void a() {
            synchronized (this.f1769d) {
                try {
                    if (this.f1767b) {
                        throw new IllegalStateException("Check failed.");
                    }
                    if (h.b(this.f1768c.b(), this)) {
                        this.f1769d.P(this, false);
                    }
                    this.f1767b = true;
                    r rVar = r.f10584a;
                } catch (Throwable th) {
                    throw th;
                }
            }
        }

        public final void b() {
            synchronized (this.f1769d) {
                try {
                    if (this.f1767b) {
                        throw new IllegalStateException("Check failed.");
                    }
                    if (h.b(this.f1768c.b(), this)) {
                        this.f1769d.P(this, true);
                    }
                    this.f1767b = true;
                    r rVar = r.f10584a;
                } catch (Throwable th) {
                    throw th;
                }
            }
        }

        public final void c() {
            if (h.b(this.f1768c.b(), this)) {
                if (this.f1769d.f1754k) {
                    this.f1769d.P(this, false);
                } else {
                    this.f1768c.q(true);
                }
            }
        }

        public final c d() {
            return this.f1768c;
        }

        public final boolean[] e() {
            return this.f1766a;
        }

        public final D f(int i3) {
            synchronized (this.f1769d) {
                if (this.f1767b) {
                    throw new IllegalStateException("Check failed.");
                }
                if (!h.b(this.f1768c.b(), this)) {
                    return t.b();
                }
                if (!this.f1768c.g()) {
                    boolean[] zArr = this.f1766a;
                    h.c(zArr);
                    zArr[i3] = true;
                }
                try {
                    return new P2.e(this.f1769d.t0().c((File) this.f1768c.c().get(i3)), new a(i3));
                } catch (FileNotFoundException unused) {
                    return t.b();
                }
            }
        }
    }

    public final class c {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        private final long[] f1772a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        private final List f1773b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        private final List f1774c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        private boolean f1775d;

        /* JADX INFO: renamed from: e, reason: collision with root package name */
        private boolean f1776e;

        /* JADX INFO: renamed from: f, reason: collision with root package name */
        private b f1777f;

        /* JADX INFO: renamed from: g, reason: collision with root package name */
        private int f1778g;

        /* JADX INFO: renamed from: h, reason: collision with root package name */
        private long f1779h;

        /* JADX INFO: renamed from: i, reason: collision with root package name */
        private final String f1780i;

        /* JADX INFO: renamed from: j, reason: collision with root package name */
        final /* synthetic */ d f1781j;

        public static final class a extends o {

            /* JADX INFO: renamed from: c, reason: collision with root package name */
            private boolean f1782c;

            /* JADX INFO: renamed from: e, reason: collision with root package name */
            final /* synthetic */ F f1784e;

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            a(F f3, F f4) {
                super(f4);
                this.f1784e = f3;
            }

            @Override // b3.o, b3.F, java.io.Closeable, java.lang.AutoCloseable
            public void close() {
                super.close();
                if (this.f1782c) {
                    return;
                }
                this.f1782c = true;
                synchronized (c.this.f1781j) {
                    try {
                        c.this.n(r1.f() - 1);
                        if (c.this.f() == 0 && c.this.i()) {
                            c cVar = c.this;
                            cVar.f1781j.D0(cVar);
                        }
                        r rVar = r.f10584a;
                    } catch (Throwable th) {
                        throw th;
                    }
                }
            }
        }

        public c(d dVar, String str) {
            h.f(str, "key");
            this.f1781j = dVar;
            this.f1780i = str;
            this.f1772a = new long[dVar.u0()];
            this.f1773b = new ArrayList();
            this.f1774c = new ArrayList();
            StringBuilder sb = new StringBuilder(str);
            sb.append('.');
            int length = sb.length();
            int iU0 = dVar.u0();
            for (int i3 = 0; i3 < iU0; i3++) {
                sb.append(i3);
                this.f1773b.add(new File(dVar.n0(), sb.toString()));
                sb.append(".tmp");
                this.f1774c.add(new File(dVar.n0(), sb.toString()));
                sb.setLength(length);
            }
        }

        private final Void j(List list) throws IOException {
            throw new IOException("unexpected journal line: " + list);
        }

        private final F k(int i3) {
            F fB = this.f1781j.t0().b((File) this.f1773b.get(i3));
            if (this.f1781j.f1754k) {
                return fB;
            }
            this.f1778g++;
            return new a(fB, fB);
        }

        public final List a() {
            return this.f1773b;
        }

        public final b b() {
            return this.f1777f;
        }

        public final List c() {
            return this.f1774c;
        }

        public final String d() {
            return this.f1780i;
        }

        public final long[] e() {
            return this.f1772a;
        }

        public final int f() {
            return this.f1778g;
        }

        public final boolean g() {
            return this.f1775d;
        }

        public final long h() {
            return this.f1779h;
        }

        public final boolean i() {
            return this.f1776e;
        }

        public final void l(b bVar) {
            this.f1777f = bVar;
        }

        public final void m(List list) throws IOException {
            h.f(list, "strings");
            if (list.size() != this.f1781j.u0()) {
                j(list);
                throw new C0680c();
            }
            try {
                int size = list.size();
                for (int i3 = 0; i3 < size; i3++) {
                    this.f1772a[i3] = Long.parseLong((String) list.get(i3));
                }
            } catch (NumberFormatException unused) {
                j(list);
                throw new C0680c();
            }
        }

        public final void n(int i3) {
            this.f1778g = i3;
        }

        public final void o(boolean z3) {
            this.f1775d = z3;
        }

        public final void p(long j3) {
            this.f1779h = j3;
        }

        public final void q(boolean z3) {
            this.f1776e = z3;
        }

        public final C0026d r() {
            d dVar = this.f1781j;
            if (N2.c.f1409h && !Thread.holdsLock(dVar)) {
                StringBuilder sb = new StringBuilder();
                sb.append("Thread ");
                Thread threadCurrentThread = Thread.currentThread();
                h.e(threadCurrentThread, "Thread.currentThread()");
                sb.append(threadCurrentThread.getName());
                sb.append(" MUST hold lock on ");
                sb.append(dVar);
                throw new AssertionError(sb.toString());
            }
            if (!this.f1775d) {
                return null;
            }
            if (!this.f1781j.f1754k && (this.f1777f != null || this.f1776e)) {
                return null;
            }
            ArrayList arrayList = new ArrayList();
            long[] jArr = (long[]) this.f1772a.clone();
            try {
                int iU0 = this.f1781j.u0();
                for (int i3 = 0; i3 < iU0; i3++) {
                    arrayList.add(k(i3));
                }
                return new C0026d(this.f1781j, this.f1780i, this.f1779h, arrayList, jArr);
            } catch (FileNotFoundException unused) {
                Iterator it = arrayList.iterator();
                while (it.hasNext()) {
                    N2.c.j((F) it.next());
                }
                try {
                    this.f1781j.D0(this);
                } catch (IOException unused2) {
                }
                return null;
            }
        }

        public final void s(j jVar) {
            h.f(jVar, "writer");
            for (long j3 : this.f1772a) {
                jVar.L(32).i0(j3);
            }
        }
    }

    /* JADX INFO: renamed from: P2.d$d, reason: collision with other inner class name */
    public final class C0026d implements Closeable {

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        private final String f1785b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        private final long f1786c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        private final List f1787d;

        /* JADX INFO: renamed from: e, reason: collision with root package name */
        private final long[] f1788e;

        /* JADX INFO: renamed from: f, reason: collision with root package name */
        final /* synthetic */ d f1789f;

        public C0026d(d dVar, String str, long j3, List<? extends F> list, long[] jArr) {
            h.f(str, "key");
            h.f(list, "sources");
            h.f(jArr, "lengths");
            this.f1789f = dVar;
            this.f1785b = str;
            this.f1786c = j3;
            this.f1787d = list;
            this.f1788e = jArr;
        }

        public final b a() {
            return this.f1789f.a0(this.f1785b, this.f1786c);
        }

        @Override // java.io.Closeable, java.lang.AutoCloseable
        public void close() {
            Iterator it = this.f1787d.iterator();
            while (it.hasNext()) {
                N2.c.j((F) it.next());
            }
        }

        public final F i(int i3) {
            return (F) this.f1787d.get(i3);
        }
    }

    public static final class e extends Q2.a {
        e(String str) {
            super(str, false, 2, null);
        }

        @Override // Q2.a
        public long f() {
            synchronized (d.this) {
                if (!d.this.f1755l || d.this.e0()) {
                    return -1L;
                }
                try {
                    d.this.F0();
                } catch (IOException unused) {
                    d.this.f1757n = true;
                }
                try {
                    if (d.this.w0()) {
                        d.this.B0();
                        d.this.f1752i = 0;
                    }
                } catch (IOException unused2) {
                    d.this.f1758o = true;
                    d.this.f1750g = t.c(t.b());
                }
                return -1L;
            }
        }
    }

    static final class f extends i implements l {
        f() {
            super(1);
        }

        @Override // C2.l
        public /* bridge */ /* synthetic */ Object d(Object obj) {
            e((IOException) obj);
            return r.f10584a;
        }

        public final void e(IOException iOException) {
            h.f(iOException, "it");
            d dVar = d.this;
            if (!N2.c.f1409h || Thread.holdsLock(dVar)) {
                d.this.f1753j = true;
                return;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("Thread ");
            Thread threadCurrentThread = Thread.currentThread();
            h.e(threadCurrentThread, "Thread.currentThread()");
            sb.append(threadCurrentThread.getName());
            sb.append(" MUST hold lock on ");
            sb.append(dVar);
            throw new AssertionError(sb.toString());
        }
    }

    public d(V2.a aVar, File file, int i3, int i4, long j3, Q2.e eVar) {
        h.f(aVar, "fileSystem");
        h.f(file, "directory");
        h.f(eVar, "taskRunner");
        this.f1762s = aVar;
        this.f1763t = file;
        this.f1764u = i3;
        this.f1765v = i4;
        this.f1745b = j3;
        this.f1751h = new LinkedHashMap(0, 0.75f, true);
        this.f1760q = eVar.i();
        this.f1761r = new e(N2.c.f1410i + " Cache");
        if (!(j3 > 0)) {
            throw new IllegalArgumentException("maxSize <= 0");
        }
        if (!(i4 > 0)) {
            throw new IllegalArgumentException("valueCount <= 0");
        }
        this.f1746c = new File(file, f1741w);
        this.f1747d = new File(file, f1742x);
        this.f1748e = new File(file, f1743y);
    }

    private final void A0(String str) throws IOException {
        String strSubstring;
        int iN = K2.o.N(str, ' ', 0, false, 6, null);
        if (iN == -1) {
            throw new IOException("unexpected journal line: " + str);
        }
        int i3 = iN + 1;
        int iN2 = K2.o.N(str, ' ', i3, false, 4, null);
        if (iN2 == -1) {
            if (str == null) {
                throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
            }
            strSubstring = str.substring(i3);
            h.e(strSubstring, "(this as java.lang.String).substring(startIndex)");
            String str2 = f1738F;
            if (iN == str2.length() && K2.o.z(str, str2, false, 2, null)) {
                this.f1751h.remove(strSubstring);
                return;
            }
        } else {
            if (str == null) {
                throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
            }
            strSubstring = str.substring(i3, iN2);
            h.e(strSubstring, "(this as java.lang.Strin…ing(startIndex, endIndex)");
        }
        c cVar = (c) this.f1751h.get(strSubstring);
        if (cVar == null) {
            cVar = new c(this, strSubstring);
            this.f1751h.put(strSubstring, cVar);
        }
        if (iN2 != -1) {
            String str3 = f1736D;
            if (iN == str3.length() && K2.o.z(str, str3, false, 2, null)) {
                int i4 = iN2 + 1;
                if (str == null) {
                    throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
                }
                String strSubstring2 = str.substring(i4);
                h.e(strSubstring2, "(this as java.lang.String).substring(startIndex)");
                List listK0 = K2.o.k0(strSubstring2, new char[]{' '}, false, 0, 6, null);
                cVar.o(true);
                cVar.l(null);
                cVar.m(listK0);
                return;
            }
        }
        if (iN2 == -1) {
            String str4 = f1737E;
            if (iN == str4.length() && K2.o.z(str, str4, false, 2, null)) {
                cVar.l(new b(this, cVar));
                return;
            }
        }
        if (iN2 == -1) {
            String str5 = f1739G;
            if (iN == str5.length() && K2.o.z(str, str5, false, 2, null)) {
                return;
            }
        }
        throw new IOException("unexpected journal line: " + str);
    }

    private final synchronized void D() {
        if (this.f1756m) {
            throw new IllegalStateException("cache is closed");
        }
    }

    private final boolean E0() {
        for (c cVar : this.f1751h.values()) {
            if (!cVar.i()) {
                h.e(cVar, "toEvict");
                D0(cVar);
                return true;
            }
        }
        return false;
    }

    private final void G0(String str) {
        if (f1735C.b(str)) {
            return;
        }
        throw new IllegalArgumentException(("keys must match regex [a-z0-9_-]{1,120}: \"" + str + '\"').toString());
    }

    public static /* synthetic */ b c0(d dVar, String str, long j3, int i3, Object obj) {
        if ((i3 & 2) != 0) {
            j3 = f1734B;
        }
        return dVar.a0(str, j3);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final boolean w0() {
        int i3 = this.f1752i;
        return i3 >= 2000 && i3 >= this.f1751h.size();
    }

    private final j x0() {
        return t.c(new P2.e(this.f1762s.e(this.f1746c), new f()));
    }

    private final void y0() {
        this.f1762s.a(this.f1747d);
        Iterator it = this.f1751h.values().iterator();
        while (it.hasNext()) {
            Object next = it.next();
            h.e(next, "i.next()");
            c cVar = (c) next;
            int i3 = 0;
            if (cVar.b() == null) {
                int i4 = this.f1765v;
                while (i3 < i4) {
                    this.f1749f += cVar.e()[i3];
                    i3++;
                }
            } else {
                cVar.l(null);
                int i5 = this.f1765v;
                while (i3 < i5) {
                    this.f1762s.a((File) cVar.a().get(i3));
                    this.f1762s.a((File) cVar.c().get(i3));
                    i3++;
                }
                it.remove();
            }
        }
    }

    private final void z0() throws IOException {
        b3.k kVarD = t.d(this.f1762s.b(this.f1746c));
        try {
            String strG = kVarD.G();
            String strG2 = kVarD.G();
            String strG3 = kVarD.G();
            String strG4 = kVarD.G();
            String strG5 = kVarD.G();
            if (!h.b(f1744z, strG) || !h.b(f1733A, strG2) || !h.b(String.valueOf(this.f1764u), strG3) || !h.b(String.valueOf(this.f1765v), strG4) || strG5.length() > 0) {
                throw new IOException("unexpected journal header: [" + strG + ", " + strG2 + ", " + strG4 + ", " + strG5 + ']');
            }
            int i3 = 0;
            while (true) {
                try {
                    A0(kVarD.G());
                    i3++;
                } catch (EOFException unused) {
                    this.f1752i = i3 - this.f1751h.size();
                    if (kVarD.J()) {
                        this.f1750g = x0();
                    } else {
                        B0();
                    }
                    r rVar = r.f10584a;
                    A2.a.a(kVarD, null);
                    return;
                }
            }
        } finally {
        }
    }

    public final synchronized void B0() {
        try {
            j jVar = this.f1750g;
            if (jVar != null) {
                jVar.close();
            }
            j jVarC = t.c(this.f1762s.c(this.f1747d));
            try {
                jVarC.h0(f1744z).L(10);
                jVarC.h0(f1733A).L(10);
                jVarC.i0(this.f1764u).L(10);
                jVarC.i0(this.f1765v).L(10);
                jVarC.L(10);
                for (c cVar : this.f1751h.values()) {
                    if (cVar.b() != null) {
                        jVarC.h0(f1737E).L(32);
                        jVarC.h0(cVar.d());
                        jVarC.L(10);
                    } else {
                        jVarC.h0(f1736D).L(32);
                        jVarC.h0(cVar.d());
                        cVar.s(jVarC);
                        jVarC.L(10);
                    }
                }
                r rVar = r.f10584a;
                A2.a.a(jVarC, null);
                if (this.f1762s.f(this.f1746c)) {
                    this.f1762s.g(this.f1746c, this.f1748e);
                }
                this.f1762s.g(this.f1747d, this.f1746c);
                this.f1762s.a(this.f1748e);
                this.f1750g = x0();
                this.f1753j = false;
                this.f1758o = false;
            } finally {
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    public final synchronized boolean C0(String str) {
        h.f(str, "key");
        v0();
        D();
        G0(str);
        c cVar = (c) this.f1751h.get(str);
        if (cVar == null) {
            return false;
        }
        h.e(cVar, "lruEntries[key] ?: return false");
        boolean zD0 = D0(cVar);
        if (zD0 && this.f1749f <= this.f1745b) {
            this.f1757n = false;
        }
        return zD0;
    }

    public final boolean D0(c cVar) {
        j jVar;
        h.f(cVar, "entry");
        if (!this.f1754k) {
            if (cVar.f() > 0 && (jVar = this.f1750g) != null) {
                jVar.h0(f1737E);
                jVar.L(32);
                jVar.h0(cVar.d());
                jVar.L(10);
                jVar.flush();
            }
            if (cVar.f() > 0 || cVar.b() != null) {
                cVar.q(true);
                return true;
            }
        }
        b bVarB = cVar.b();
        if (bVarB != null) {
            bVarB.c();
        }
        int i3 = this.f1765v;
        for (int i4 = 0; i4 < i3; i4++) {
            this.f1762s.a((File) cVar.a().get(i4));
            this.f1749f -= cVar.e()[i4];
            cVar.e()[i4] = 0;
        }
        this.f1752i++;
        j jVar2 = this.f1750g;
        if (jVar2 != null) {
            jVar2.h0(f1738F);
            jVar2.L(32);
            jVar2.h0(cVar.d());
            jVar2.L(10);
        }
        this.f1751h.remove(cVar.d());
        if (w0()) {
            Q2.d.j(this.f1760q, this.f1761r, 0L, 2, null);
        }
        return true;
    }

    public final void F0() {
        while (this.f1749f > this.f1745b) {
            if (!E0()) {
                return;
            }
        }
        this.f1757n = false;
    }

    public final synchronized void P(b bVar, boolean z3) {
        h.f(bVar, "editor");
        c cVarD = bVar.d();
        if (!h.b(cVarD.b(), bVar)) {
            throw new IllegalStateException("Check failed.");
        }
        if (z3 && !cVarD.g()) {
            int i3 = this.f1765v;
            for (int i4 = 0; i4 < i3; i4++) {
                boolean[] zArrE = bVar.e();
                h.c(zArrE);
                if (!zArrE[i4]) {
                    bVar.a();
                    throw new IllegalStateException("Newly created entry didn't create value for index " + i4);
                }
                if (!this.f1762s.f((File) cVarD.c().get(i4))) {
                    bVar.a();
                    return;
                }
            }
        }
        int i5 = this.f1765v;
        for (int i6 = 0; i6 < i5; i6++) {
            File file = (File) cVarD.c().get(i6);
            if (!z3 || cVarD.i()) {
                this.f1762s.a(file);
            } else if (this.f1762s.f(file)) {
                File file2 = (File) cVarD.a().get(i6);
                this.f1762s.g(file, file2);
                long j3 = cVarD.e()[i6];
                long jH = this.f1762s.h(file2);
                cVarD.e()[i6] = jH;
                this.f1749f = (this.f1749f - j3) + jH;
            }
        }
        cVarD.l(null);
        if (cVarD.i()) {
            D0(cVarD);
            return;
        }
        this.f1752i++;
        j jVar = this.f1750g;
        h.c(jVar);
        if (cVarD.g() || z3) {
            cVarD.o(true);
            jVar.h0(f1736D).L(32);
            jVar.h0(cVarD.d());
            cVarD.s(jVar);
            jVar.L(10);
            if (z3) {
                long j4 = this.f1759p;
                this.f1759p = 1 + j4;
                cVarD.p(j4);
            }
        } else {
            this.f1751h.remove(cVarD.d());
            jVar.h0(f1738F).L(32);
            jVar.h0(cVarD.d());
            jVar.L(10);
        }
        jVar.flush();
        if (this.f1749f > this.f1745b || w0()) {
            Q2.d.j(this.f1760q, this.f1761r, 0L, 2, null);
        }
    }

    public final void X() {
        close();
        this.f1762s.d(this.f1763t);
    }

    public final synchronized b a0(String str, long j3) {
        h.f(str, "key");
        v0();
        D();
        G0(str);
        c cVar = (c) this.f1751h.get(str);
        if (j3 != f1734B && (cVar == null || cVar.h() != j3)) {
            return null;
        }
        if ((cVar != null ? cVar.b() : null) != null) {
            return null;
        }
        if (cVar != null && cVar.f() != 0) {
            return null;
        }
        if (!this.f1757n && !this.f1758o) {
            j jVar = this.f1750g;
            h.c(jVar);
            jVar.h0(f1737E).L(32).h0(str).L(10);
            jVar.flush();
            if (this.f1753j) {
                return null;
            }
            if (cVar == null) {
                cVar = new c(this, str);
                this.f1751h.put(str, cVar);
            }
            b bVar = new b(this, cVar);
            cVar.l(bVar);
            return bVar;
        }
        Q2.d.j(this.f1760q, this.f1761r, 0L, 2, null);
        return null;
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public synchronized void close() {
        b bVarB;
        try {
            if (this.f1755l && !this.f1756m) {
                Collection collectionValues = this.f1751h.values();
                h.e(collectionValues, "lruEntries.values");
                Object[] array = collectionValues.toArray(new c[0]);
                if (array == null) {
                    throw new NullPointerException("null cannot be cast to non-null type kotlin.Array<T>");
                }
                for (c cVar : (c[]) array) {
                    if (cVar.b() != null && (bVarB = cVar.b()) != null) {
                        bVarB.c();
                    }
                }
                F0();
                j jVar = this.f1750g;
                h.c(jVar);
                jVar.close();
                this.f1750g = null;
                this.f1756m = true;
                return;
            }
            this.f1756m = true;
        } catch (Throwable th) {
            throw th;
        }
    }

    public final synchronized C0026d d0(String str) {
        h.f(str, "key");
        v0();
        D();
        G0(str);
        c cVar = (c) this.f1751h.get(str);
        if (cVar == null) {
            return null;
        }
        h.e(cVar, "lruEntries[key] ?: return null");
        C0026d c0026dR = cVar.r();
        if (c0026dR == null) {
            return null;
        }
        this.f1752i++;
        j jVar = this.f1750g;
        h.c(jVar);
        jVar.h0(f1739G).L(32).h0(str).L(10);
        if (w0()) {
            Q2.d.j(this.f1760q, this.f1761r, 0L, 2, null);
        }
        return c0026dR;
    }

    public final boolean e0() {
        return this.f1756m;
    }

    @Override // java.io.Flushable
    public synchronized void flush() {
        if (this.f1755l) {
            D();
            F0();
            j jVar = this.f1750g;
            h.c(jVar);
            jVar.flush();
        }
    }

    public final File n0() {
        return this.f1763t;
    }

    public final V2.a t0() {
        return this.f1762s;
    }

    public final int u0() {
        return this.f1765v;
    }

    public final synchronized void v0() {
        try {
            if (N2.c.f1409h && !Thread.holdsLock(this)) {
                StringBuilder sb = new StringBuilder();
                sb.append("Thread ");
                Thread threadCurrentThread = Thread.currentThread();
                h.e(threadCurrentThread, "Thread.currentThread()");
                sb.append(threadCurrentThread.getName());
                sb.append(" MUST hold lock on ");
                sb.append(this);
                throw new AssertionError(sb.toString());
            }
            if (this.f1755l) {
                return;
            }
            if (this.f1762s.f(this.f1748e)) {
                if (this.f1762s.f(this.f1746c)) {
                    this.f1762s.a(this.f1748e);
                } else {
                    this.f1762s.g(this.f1748e, this.f1746c);
                }
            }
            this.f1754k = N2.c.C(this.f1762s, this.f1748e);
            if (this.f1762s.f(this.f1746c)) {
                try {
                    z0();
                    y0();
                    this.f1755l = true;
                    return;
                } catch (IOException e4) {
                    W2.j.f2732c.g().k("DiskLruCache " + this.f1763t + " is corrupt: " + e4.getMessage() + ", removing", 5, e4);
                    try {
                        X();
                        this.f1756m = false;
                        B0();
                        this.f1755l = true;
                    } catch (Throwable th) {
                        this.f1756m = false;
                        throw th;
                    }
                }
            }
            B0();
            this.f1755l = true;
        } catch (Throwable th2) {
            throw th2;
        }
    }
}
