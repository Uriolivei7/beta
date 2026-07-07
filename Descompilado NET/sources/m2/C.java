package M2;

import java.nio.charset.Charset;
import kotlin.jvm.internal.DefaultConstructorMarker;

/* JADX INFO: loaded from: classes.dex */
public abstract class C {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static final a f908a = new a(null);

    public static final class a {

        /* JADX INFO: renamed from: M2.C$a$a, reason: collision with other inner class name */
        public static final class C0014a extends C {

            /* JADX INFO: renamed from: b, reason: collision with root package name */
            final /* synthetic */ b3.l f909b;

            /* JADX INFO: renamed from: c, reason: collision with root package name */
            final /* synthetic */ x f910c;

            C0014a(b3.l lVar, x xVar) {
                this.f909b = lVar;
                this.f910c = xVar;
            }

            @Override // M2.C
            public long a() {
                return this.f909b.v();
            }

            @Override // M2.C
            public x b() {
                return this.f910c;
            }

            @Override // M2.C
            public void h(b3.j jVar) {
                D2.h.f(jVar, "sink");
                jVar.u(this.f909b);
            }
        }

        public static final class b extends C {

            /* JADX INFO: renamed from: b, reason: collision with root package name */
            final /* synthetic */ byte[] f911b;

            /* JADX INFO: renamed from: c, reason: collision with root package name */
            final /* synthetic */ x f912c;

            /* JADX INFO: renamed from: d, reason: collision with root package name */
            final /* synthetic */ int f913d;

            /* JADX INFO: renamed from: e, reason: collision with root package name */
            final /* synthetic */ int f914e;

            b(byte[] bArr, x xVar, int i3, int i4) {
                this.f911b = bArr;
                this.f912c = xVar;
                this.f913d = i3;
                this.f914e = i4;
            }

            @Override // M2.C
            public long a() {
                return this.f913d;
            }

            @Override // M2.C
            public x b() {
                return this.f912c;
            }

            @Override // M2.C
            public void h(b3.j jVar) {
                D2.h.f(jVar, "sink");
                jVar.k(this.f911b, this.f914e, this.f913d);
            }
        }

        private a() {
        }

        public static /* synthetic */ C g(a aVar, x xVar, byte[] bArr, int i3, int i4, int i5, Object obj) {
            if ((i5 & 4) != 0) {
                i3 = 0;
            }
            if ((i5 & 8) != 0) {
                i4 = bArr.length;
            }
            return aVar.c(xVar, bArr, i3, i4);
        }

        public static /* synthetic */ C h(a aVar, byte[] bArr, x xVar, int i3, int i4, int i5, Object obj) {
            if ((i5 & 1) != 0) {
                xVar = null;
            }
            if ((i5 & 2) != 0) {
                i3 = 0;
            }
            if ((i5 & 4) != 0) {
                i4 = bArr.length;
            }
            return aVar.f(bArr, xVar, i3, i4);
        }

        public final C a(x xVar, b3.l lVar) {
            D2.h.f(lVar, "content");
            return d(lVar, xVar);
        }

        public final C b(x xVar, String str) {
            D2.h.f(str, "content");
            return e(str, xVar);
        }

        public final C c(x xVar, byte[] bArr, int i3, int i4) {
            D2.h.f(bArr, "content");
            return f(bArr, xVar, i3, i4);
        }

        public final C d(b3.l lVar, x xVar) {
            D2.h.f(lVar, "$this$toRequestBody");
            return new C0014a(lVar, xVar);
        }

        public final C e(String str, x xVar) {
            D2.h.f(str, "$this$toRequestBody");
            Charset charset = K2.d.f816b;
            if (xVar != null) {
                Charset charsetD = x.d(xVar, null, 1, null);
                if (charsetD == null) {
                    xVar = x.f1251g.c(xVar + "; charset=utf-8");
                } else {
                    charset = charsetD;
                }
            }
            byte[] bytes = str.getBytes(charset);
            D2.h.e(bytes, "(this as java.lang.String).getBytes(charset)");
            return f(bytes, xVar, 0, bytes.length);
        }

        public final C f(byte[] bArr, x xVar, int i3, int i4) {
            D2.h.f(bArr, "$this$toRequestBody");
            N2.c.i(bArr.length, i3, i4);
            return new b(bArr, xVar, i4, i3);
        }

        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }

    public static final C c(x xVar, b3.l lVar) {
        return f908a.a(xVar, lVar);
    }

    public static final C d(x xVar, String str) {
        return f908a.b(xVar, str);
    }

    public static final C e(x xVar, byte[] bArr) {
        return a.g(f908a, xVar, bArr, 0, 0, 12, null);
    }

    public long a() {
        return -1L;
    }

    public abstract x b();

    public boolean f() {
        return false;
    }

    public boolean g() {
        return false;
    }

    public abstract void h(b3.j jVar);
}
