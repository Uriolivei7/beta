package D0;

import D0.c;
import D2.h;
import X.p;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import kotlin.Lazy;
import kotlin.jvm.internal.DefaultConstructorMarker;
import r2.AbstractC0681d;
import r2.EnumC0684g;

/* JADX INFO: loaded from: classes.dex */
public final class e {

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    public static final a f154e = new a(null);

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private static final Lazy f155f = AbstractC0681d.b(EnumC0684g.f10565b, new C2.a() { // from class: D0.d
        @Override // C2.a
        public final Object a() {
            return e.f();
        }
    });

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private int f156a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private List f157b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final D0.a f158c = new D0.a();

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private boolean f159d;

    public static final class a {
        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public final int e(int i3, InputStream inputStream, byte[] bArr) throws IOException {
            if (bArr.length < i3) {
                throw new IllegalStateException("Check failed.");
            }
            if (!inputStream.markSupported()) {
                return X.a.b(inputStream, bArr, 0, i3);
            }
            try {
                inputStream.mark(i3);
                return X.a.b(inputStream, bArr, 0, i3);
            } finally {
                inputStream.reset();
            }
        }

        public final c b(InputStream inputStream) {
            h.f(inputStream, "is");
            return d().c(inputStream);
        }

        public final c c(InputStream inputStream) {
            h.f(inputStream, "is");
            try {
                return b(inputStream);
            } catch (IOException e4) {
                throw p.a(e4);
            }
        }

        public final e d() {
            return (e) e.f155f.getValue();
        }

        private a() {
        }
    }

    private e() {
        h();
    }

    public static final c d(InputStream inputStream) {
        return f154e.c(inputStream);
    }

    public static final e e() {
        return f154e.d();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final e f() {
        return new e();
    }

    private final void h() {
        this.f156a = this.f158c.a();
        List list = this.f157b;
        if (list != null) {
            h.c(list);
            Iterator it = list.iterator();
            while (it.hasNext()) {
                this.f156a = Math.max(this.f156a, ((c.b) it.next()).a());
            }
        }
    }

    public final c c(InputStream inputStream) throws IOException {
        h.f(inputStream, "is");
        int i3 = this.f156a;
        byte[] bArr = new byte[i3];
        int iE = f154e.e(i3, inputStream, bArr);
        c cVarB = this.f158c.b(bArr, iE);
        if (h.b(cVarB, b.f147n) && !this.f159d) {
            cVarB = c.f151d;
        }
        if (cVarB != c.f151d) {
            return cVarB;
        }
        List list = this.f157b;
        if (list != null) {
            Iterator it = list.iterator();
            while (it.hasNext()) {
                c cVarB2 = ((c.b) it.next()).b(bArr, iE);
                if (cVarB2 != c.f151d) {
                    return cVarB2;
                }
            }
        }
        return c.f151d;
    }

    public final e g(boolean z3) {
        this.f159d = z3;
        return this;
    }
}
