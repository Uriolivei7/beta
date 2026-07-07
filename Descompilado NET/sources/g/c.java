package G;

import D2.h;
import android.os.Bundle;
import androidx.lifecycle.AbstractC0299g;
import androidx.savedstate.Recreator;
import kotlin.jvm.internal.DefaultConstructorMarker;

/* JADX INFO: loaded from: classes.dex */
public final class c {

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public static final a f249d = new a(null);

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final d f250a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final androidx.savedstate.a f251b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private boolean f252c;

    public static final class a {
        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final c a(d dVar) {
            h.f(dVar, "owner");
            return new c(dVar, null);
        }

        private a() {
        }
    }

    public /* synthetic */ c(d dVar, DefaultConstructorMarker defaultConstructorMarker) {
        this(dVar);
    }

    public static final c a(d dVar) {
        return f249d.a(dVar);
    }

    public final androidx.savedstate.a b() {
        return this.f251b;
    }

    public final void c() {
        AbstractC0299g abstractC0299gT = this.f250a.t();
        if (abstractC0299gT.b() != AbstractC0299g.b.INITIALIZED) {
            throw new IllegalStateException("Restarter must be created only during owner's initialization stage");
        }
        abstractC0299gT.a(new Recreator(this.f250a));
        this.f251b.e(abstractC0299gT);
        this.f252c = true;
    }

    public final void d(Bundle bundle) {
        if (!this.f252c) {
            c();
        }
        AbstractC0299g abstractC0299gT = this.f250a.t();
        if (!abstractC0299gT.b().b(AbstractC0299g.b.STARTED)) {
            this.f251b.f(bundle);
            return;
        }
        throw new IllegalStateException(("performRestore cannot be called when owner is " + abstractC0299gT.b()).toString());
    }

    public final void e(Bundle bundle) {
        h.f(bundle, "outBundle");
        this.f251b.g(bundle);
    }

    private c(d dVar) {
        this.f250a = dVar;
        this.f251b = new androidx.savedstate.a();
    }
}
