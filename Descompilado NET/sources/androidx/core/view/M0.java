package androidx.core.view;

import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowInsetsController;

/* JADX INFO: loaded from: classes.dex */
public final class M0 {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final e f4554a;

    private static class a extends e {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        protected final Window f4555a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        private final N f4556b;

        a(Window window, N n3) {
            this.f4555a = window;
            this.f4556b = n3;
        }

        private void g(int i3) {
            if (i3 == 1) {
                h(4);
            } else if (i3 == 2) {
                h(2);
            } else {
                if (i3 != 8) {
                    return;
                }
                this.f4556b.a();
            }
        }

        private void j(int i3) {
            if (i3 == 1) {
                k(4);
                l(1024);
            } else if (i3 == 2) {
                k(2);
            } else {
                if (i3 != 8) {
                    return;
                }
                this.f4556b.b();
            }
        }

        @Override // androidx.core.view.M0.e
        void a(int i3) {
            for (int i4 = 1; i4 <= 256; i4 <<= 1) {
                if ((i3 & i4) != 0) {
                    g(i4);
                }
            }
        }

        @Override // androidx.core.view.M0.e
        void e(int i3) {
            if (i3 == 0) {
                k(6144);
                return;
            }
            if (i3 == 1) {
                k(4096);
                h(2048);
            } else {
                if (i3 != 2) {
                    return;
                }
                k(2048);
                h(4096);
            }
        }

        @Override // androidx.core.view.M0.e
        void f(int i3) {
            for (int i4 = 1; i4 <= 256; i4 <<= 1) {
                if ((i3 & i4) != 0) {
                    j(i4);
                }
            }
        }

        protected void h(int i3) {
            View decorView = this.f4555a.getDecorView();
            decorView.setSystemUiVisibility(i3 | decorView.getSystemUiVisibility());
        }

        protected void i(int i3) {
            this.f4555a.addFlags(i3);
        }

        protected void k(int i3) {
            View decorView = this.f4555a.getDecorView();
            decorView.setSystemUiVisibility((~i3) & decorView.getSystemUiVisibility());
        }

        protected void l(int i3) {
            this.f4555a.clearFlags(i3);
        }
    }

    private static class b extends a {
        b(Window window, N n3) {
            super(window, n3);
        }

        @Override // androidx.core.view.M0.e
        public boolean b() {
            return (this.f4555a.getDecorView().getSystemUiVisibility() & 8192) != 0;
        }

        @Override // androidx.core.view.M0.e
        public void d(boolean z3) {
            if (!z3) {
                k(8192);
                return;
            }
            l(67108864);
            i(Integer.MIN_VALUE);
            h(8192);
        }
    }

    private static class c extends b {
        c(Window window, N n3) {
            super(window, n3);
        }

        @Override // androidx.core.view.M0.e
        public void c(boolean z3) {
            if (!z3) {
                k(16);
                return;
            }
            l(134217728);
            i(Integer.MIN_VALUE);
            h(16);
        }
    }

    private static class e {
        e() {
        }

        abstract void a(int i3);

        public abstract boolean b();

        public void c(boolean z3) {
        }

        public abstract void d(boolean z3);

        abstract void e(int i3);

        abstract void f(int i3);
    }

    public M0(Window window, View view) {
        N n3 = new N(view);
        int i3 = Build.VERSION.SDK_INT;
        if (i3 >= 30) {
            this.f4554a = new d(window, this, n3);
        } else if (i3 >= 26) {
            this.f4554a = new c(window, n3);
        } else {
            this.f4554a = new b(window, n3);
        }
    }

    public void a(int i3) {
        this.f4554a.a(i3);
    }

    public boolean b() {
        return this.f4554a.b();
    }

    public void c(boolean z3) {
        this.f4554a.c(z3);
    }

    public void d(boolean z3) {
        this.f4554a.d(z3);
    }

    public void e(int i3) {
        this.f4554a.e(i3);
    }

    public void f(int i3) {
        this.f4554a.f(i3);
    }

    private static class d extends e {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        final M0 f4557a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        final WindowInsetsController f4558b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        final N f4559c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        private final l.g f4560d;

        /* JADX INFO: renamed from: e, reason: collision with root package name */
        protected Window f4561e;

        d(Window window, M0 m02, N n3) {
            this(window.getInsetsController(), m02, n3);
            this.f4561e = window;
        }

        @Override // androidx.core.view.M0.e
        void a(int i3) {
            if ((i3 & 8) != 0) {
                this.f4559c.a();
            }
            this.f4558b.hide(i3 & (-9));
        }

        @Override // androidx.core.view.M0.e
        public boolean b() {
            this.f4558b.setSystemBarsAppearance(0, 0);
            return (this.f4558b.getSystemBarsAppearance() & 8) != 0;
        }

        @Override // androidx.core.view.M0.e
        public void c(boolean z3) {
            if (z3) {
                if (this.f4561e != null) {
                    g(16);
                }
                this.f4558b.setSystemBarsAppearance(16, 16);
            } else {
                if (this.f4561e != null) {
                    h(16);
                }
                this.f4558b.setSystemBarsAppearance(0, 16);
            }
        }

        @Override // androidx.core.view.M0.e
        public void d(boolean z3) {
            if (z3) {
                if (this.f4561e != null) {
                    g(8192);
                }
                this.f4558b.setSystemBarsAppearance(8, 8);
            } else {
                if (this.f4561e != null) {
                    h(8192);
                }
                this.f4558b.setSystemBarsAppearance(0, 8);
            }
        }

        @Override // androidx.core.view.M0.e
        void e(int i3) {
            this.f4558b.setSystemBarsBehavior(i3);
        }

        @Override // androidx.core.view.M0.e
        void f(int i3) {
            if ((i3 & 8) != 0) {
                this.f4559c.b();
            }
            this.f4558b.show(i3 & (-9));
        }

        protected void g(int i3) {
            View decorView = this.f4561e.getDecorView();
            decorView.setSystemUiVisibility(i3 | decorView.getSystemUiVisibility());
        }

        protected void h(int i3) {
            View decorView = this.f4561e.getDecorView();
            decorView.setSystemUiVisibility((~i3) & decorView.getSystemUiVisibility());
        }

        d(WindowInsetsController windowInsetsController, M0 m02, N n3) {
            this.f4560d = new l.g();
            this.f4558b = windowInsetsController;
            this.f4557a = m02;
            this.f4559c = n3;
        }
    }
}
