package androidx.loader.app;

import androidx.activity.result.d;
import androidx.lifecycle.D;
import androidx.lifecycle.E;
import androidx.lifecycle.G;
import androidx.lifecycle.l;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import l.h;
import q.AbstractC0644b;

/* JADX INFO: loaded from: classes.dex */
class b extends androidx.loader.app.a {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final l f5377a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final a f5378b;

    static class a extends D {

        /* JADX INFO: renamed from: f, reason: collision with root package name */
        private static final E.b f5379f = new C0081a();

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        private h f5380d = new h();

        /* JADX INFO: renamed from: e, reason: collision with root package name */
        private boolean f5381e = false;

        /* JADX INFO: renamed from: androidx.loader.app.b$a$a, reason: collision with other inner class name */
        static class C0081a implements E.b {
            C0081a() {
            }

            @Override // androidx.lifecycle.E.b
            public D a(Class cls) {
                return new a();
            }
        }

        a() {
        }

        static a g(G g3) {
            return (a) new E(g3, f5379f).a(a.class);
        }

        @Override // androidx.lifecycle.D
        protected void d() {
            super.d();
            if (this.f5380d.n() <= 0) {
                this.f5380d.c();
            } else {
                d.a(this.f5380d.p(0));
                throw null;
            }
        }

        public void f(String str, FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
            if (this.f5380d.n() > 0) {
                printWriter.print(str);
                printWriter.println("Loaders:");
                StringBuilder sb = new StringBuilder();
                sb.append(str);
                sb.append("    ");
                if (this.f5380d.n() <= 0) {
                    return;
                }
                d.a(this.f5380d.p(0));
                printWriter.print(str);
                printWriter.print("  #");
                printWriter.print(this.f5380d.l(0));
                printWriter.print(": ");
                throw null;
            }
        }

        void h() {
            if (this.f5380d.n() <= 0) {
                return;
            }
            d.a(this.f5380d.p(0));
            throw null;
        }
    }

    b(l lVar, G g3) {
        this.f5377a = lVar;
        this.f5378b = a.g(g3);
    }

    @Override // androidx.loader.app.a
    public void a(String str, FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        this.f5378b.f(str, fileDescriptor, printWriter, strArr);
    }

    @Override // androidx.loader.app.a
    public void c() {
        this.f5378b.h();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(128);
        sb.append("LoaderManager{");
        sb.append(Integer.toHexString(System.identityHashCode(this)));
        sb.append(" in ");
        AbstractC0644b.a(this.f5377a, sb);
        sb.append("}}");
        return sb.toString();
    }
}
