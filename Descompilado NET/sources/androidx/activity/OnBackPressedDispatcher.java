package androidx.activity;

import android.os.Build;
import android.window.OnBackInvokedCallback;
import android.window.OnBackInvokedDispatcher;
import androidx.activity.OnBackPressedDispatcher;
import androidx.lifecycle.AbstractC0299g;
import androidx.lifecycle.InterfaceC0302j;
import java.util.Iterator;
import java.util.ListIterator;
import kotlin.jvm.internal.DefaultConstructorMarker;
import s2.C0710g;

/* JADX INFO: loaded from: classes.dex */
public final class OnBackPressedDispatcher {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final Runnable f3001a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final C0710g f3002b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private C2.a f3003c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private OnBackInvokedCallback f3004d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private OnBackInvokedDispatcher f3005e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private boolean f3006f;

    private final class LifecycleOnBackPressedCancellable implements InterfaceC0302j, androidx.activity.a {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        private final AbstractC0299g f3007a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        private final m f3008b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        private androidx.activity.a f3009c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        final /* synthetic */ OnBackPressedDispatcher f3010d;

        public LifecycleOnBackPressedCancellable(OnBackPressedDispatcher onBackPressedDispatcher, AbstractC0299g abstractC0299g, m mVar) {
            D2.h.f(abstractC0299g, "lifecycle");
            D2.h.f(mVar, "onBackPressedCallback");
            this.f3010d = onBackPressedDispatcher;
            this.f3007a = abstractC0299g;
            this.f3008b = mVar;
            abstractC0299g.a(this);
        }

        @Override // androidx.activity.a
        public void cancel() {
            this.f3007a.c(this);
            this.f3008b.e(this);
            androidx.activity.a aVar = this.f3009c;
            if (aVar != null) {
                aVar.cancel();
            }
            this.f3009c = null;
        }

        @Override // androidx.lifecycle.InterfaceC0302j
        public void d(androidx.lifecycle.l lVar, AbstractC0299g.a aVar) {
            D2.h.f(lVar, "source");
            D2.h.f(aVar, "event");
            if (aVar == AbstractC0299g.a.ON_START) {
                this.f3009c = this.f3010d.c(this.f3008b);
                return;
            }
            if (aVar != AbstractC0299g.a.ON_STOP) {
                if (aVar == AbstractC0299g.a.ON_DESTROY) {
                    cancel();
                }
            } else {
                androidx.activity.a aVar2 = this.f3009c;
                if (aVar2 != null) {
                    aVar2.cancel();
                }
            }
        }
    }

    static final class a extends D2.i implements C2.a {
        a() {
            super(0);
        }

        @Override // C2.a
        public /* bridge */ /* synthetic */ Object a() {
            e();
            return r2.r.f10584a;
        }

        public final void e() {
            OnBackPressedDispatcher.this.g();
        }
    }

    static final class b extends D2.i implements C2.a {
        b() {
            super(0);
        }

        @Override // C2.a
        public /* bridge */ /* synthetic */ Object a() {
            e();
            return r2.r.f10584a;
        }

        public final void e() {
            OnBackPressedDispatcher.this.e();
        }
    }

    public static final class c {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public static final c f3013a = new c();

        private c() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static final void c(C2.a aVar) {
            D2.h.f(aVar, "$onBackInvoked");
            aVar.a();
        }

        public final OnBackInvokedCallback b(final C2.a aVar) {
            D2.h.f(aVar, "onBackInvoked");
            return new OnBackInvokedCallback() { // from class: androidx.activity.n
                @Override // android.window.OnBackInvokedCallback
                public final void onBackInvoked() {
                    OnBackPressedDispatcher.c.c(aVar);
                }
            };
        }

        public final void d(Object obj, int i3, Object obj2) {
            D2.h.f(obj, "dispatcher");
            D2.h.f(obj2, "callback");
            ((OnBackInvokedDispatcher) obj).registerOnBackInvokedCallback(i3, (OnBackInvokedCallback) obj2);
        }

        public final void e(Object obj, Object obj2) {
            D2.h.f(obj, "dispatcher");
            D2.h.f(obj2, "callback");
            ((OnBackInvokedDispatcher) obj).unregisterOnBackInvokedCallback((OnBackInvokedCallback) obj2);
        }
    }

    private final class d implements androidx.activity.a {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        private final m f3014a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        final /* synthetic */ OnBackPressedDispatcher f3015b;

        public d(OnBackPressedDispatcher onBackPressedDispatcher, m mVar) {
            D2.h.f(mVar, "onBackPressedCallback");
            this.f3015b = onBackPressedDispatcher;
            this.f3014a = mVar;
        }

        @Override // androidx.activity.a
        public void cancel() {
            this.f3015b.f3002b.remove(this.f3014a);
            this.f3014a.e(this);
            if (Build.VERSION.SDK_INT >= 33) {
                this.f3014a.g(null);
                this.f3015b.g();
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public OnBackPressedDispatcher() {
        this(null, 1, 0 == true ? 1 : 0);
    }

    public final void b(androidx.lifecycle.l lVar, m mVar) {
        D2.h.f(lVar, "owner");
        D2.h.f(mVar, "onBackPressedCallback");
        AbstractC0299g abstractC0299gT = lVar.t();
        if (abstractC0299gT.b() == AbstractC0299g.b.DESTROYED) {
            return;
        }
        mVar.a(new LifecycleOnBackPressedCancellable(this, abstractC0299gT, mVar));
        if (Build.VERSION.SDK_INT >= 33) {
            g();
            mVar.g(this.f3003c);
        }
    }

    public final androidx.activity.a c(m mVar) {
        D2.h.f(mVar, "onBackPressedCallback");
        this.f3002b.add(mVar);
        d dVar = new d(this, mVar);
        mVar.a(dVar);
        if (Build.VERSION.SDK_INT >= 33) {
            g();
            mVar.g(this.f3003c);
        }
        return dVar;
    }

    public final boolean d() {
        C0710g c0710g = this.f3002b;
        if (c0710g != null && c0710g.isEmpty()) {
            return false;
        }
        Iterator<E> it = c0710g.iterator();
        while (it.hasNext()) {
            if (((m) it.next()).c()) {
                return true;
            }
        }
        return false;
    }

    public final void e() {
        Object objPrevious;
        C0710g c0710g = this.f3002b;
        ListIterator<E> listIterator = c0710g.listIterator(c0710g.size());
        while (true) {
            if (!listIterator.hasPrevious()) {
                objPrevious = null;
                break;
            } else {
                objPrevious = listIterator.previous();
                if (((m) objPrevious).c()) {
                    break;
                }
            }
        }
        m mVar = (m) objPrevious;
        if (mVar != null) {
            mVar.b();
            return;
        }
        Runnable runnable = this.f3001a;
        if (runnable != null) {
            runnable.run();
        }
    }

    public final void f(OnBackInvokedDispatcher onBackInvokedDispatcher) {
        D2.h.f(onBackInvokedDispatcher, "invoker");
        this.f3005e = onBackInvokedDispatcher;
        g();
    }

    public final void g() {
        boolean zD = d();
        OnBackInvokedDispatcher onBackInvokedDispatcher = this.f3005e;
        OnBackInvokedCallback onBackInvokedCallback = this.f3004d;
        if (onBackInvokedDispatcher == null || onBackInvokedCallback == null) {
            return;
        }
        if (zD && !this.f3006f) {
            c.f3013a.d(onBackInvokedDispatcher, 0, onBackInvokedCallback);
            this.f3006f = true;
        } else {
            if (zD || !this.f3006f) {
                return;
            }
            c.f3013a.e(onBackInvokedDispatcher, onBackInvokedCallback);
            this.f3006f = false;
        }
    }

    public OnBackPressedDispatcher(Runnable runnable) {
        this.f3001a = runnable;
        this.f3002b = new C0710g();
        if (Build.VERSION.SDK_INT >= 33) {
            this.f3003c = new a();
            this.f3004d = c.f3013a.b(new b());
        }
    }

    public /* synthetic */ OnBackPressedDispatcher(Runnable runnable, int i3, DefaultConstructorMarker defaultConstructorMarker) {
        this((i3 & 1) != 0 ? null : runnable);
    }
}
