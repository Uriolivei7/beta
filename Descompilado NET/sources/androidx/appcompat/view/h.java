package androidx.appcompat.view;

import android.view.View;
import android.view.animation.Interpolator;
import androidx.core.view.C0254i0;
import androidx.core.view.C0258k0;
import androidx.core.view.InterfaceC0256j0;
import java.util.ArrayList;
import java.util.Iterator;

/* JADX INFO: loaded from: classes.dex */
public class h {

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private Interpolator f3423c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    InterfaceC0256j0 f3424d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private boolean f3425e;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private long f3422b = -1;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private final C0258k0 f3426f = new a();

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    final ArrayList f3421a = new ArrayList();

    class a extends C0258k0 {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        private boolean f3427a = false;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        private int f3428b = 0;

        a() {
        }

        @Override // androidx.core.view.C0258k0, androidx.core.view.InterfaceC0256j0
        public void b(View view) {
            int i3 = this.f3428b + 1;
            this.f3428b = i3;
            if (i3 == h.this.f3421a.size()) {
                InterfaceC0256j0 interfaceC0256j0 = h.this.f3424d;
                if (interfaceC0256j0 != null) {
                    interfaceC0256j0.b(null);
                }
                d();
            }
        }

        @Override // androidx.core.view.C0258k0, androidx.core.view.InterfaceC0256j0
        public void c(View view) {
            if (this.f3427a) {
                return;
            }
            this.f3427a = true;
            InterfaceC0256j0 interfaceC0256j0 = h.this.f3424d;
            if (interfaceC0256j0 != null) {
                interfaceC0256j0.c(null);
            }
        }

        void d() {
            this.f3428b = 0;
            this.f3427a = false;
            h.this.b();
        }
    }

    public void a() {
        if (this.f3425e) {
            Iterator it = this.f3421a.iterator();
            while (it.hasNext()) {
                ((C0254i0) it.next()).c();
            }
            this.f3425e = false;
        }
    }

    void b() {
        this.f3425e = false;
    }

    public h c(C0254i0 c0254i0) {
        if (!this.f3425e) {
            this.f3421a.add(c0254i0);
        }
        return this;
    }

    public h d(C0254i0 c0254i0, C0254i0 c0254i02) {
        this.f3421a.add(c0254i0);
        c0254i02.j(c0254i0.d());
        this.f3421a.add(c0254i02);
        return this;
    }

    public h e(long j3) {
        if (!this.f3425e) {
            this.f3422b = j3;
        }
        return this;
    }

    public h f(Interpolator interpolator) {
        if (!this.f3425e) {
            this.f3423c = interpolator;
        }
        return this;
    }

    public h g(InterfaceC0256j0 interfaceC0256j0) {
        if (!this.f3425e) {
            this.f3424d = interfaceC0256j0;
        }
        return this;
    }

    public void h() {
        if (this.f3425e) {
            return;
        }
        for (C0254i0 c0254i0 : this.f3421a) {
            long j3 = this.f3422b;
            if (j3 >= 0) {
                c0254i0.f(j3);
            }
            Interpolator interpolator = this.f3423c;
            if (interpolator != null) {
                c0254i0.g(interpolator);
            }
            if (this.f3424d != null) {
                c0254i0.h(this.f3426f);
            }
            c0254i0.l();
        }
        this.f3425e = true;
    }
}
