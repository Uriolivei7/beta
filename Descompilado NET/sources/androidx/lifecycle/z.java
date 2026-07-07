package androidx.lifecycle;

import android.os.Bundle;
import androidx.savedstate.a;
import java.util.Map;
import kotlin.Lazy;
import r2.AbstractC0681d;

/* JADX INFO: loaded from: classes.dex */
public final class z implements a.c {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final androidx.savedstate.a f5372a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private boolean f5373b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private Bundle f5374c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private final Lazy f5375d;

    static final class a extends D2.i implements C2.a {

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        final /* synthetic */ H f5376c;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        a(H h3) {
            super(0);
            this.f5376c = h3;
        }

        @Override // C2.a
        /* JADX INFO: renamed from: e, reason: merged with bridge method [inline-methods] */
        public final A a() {
            return y.e(this.f5376c);
        }
    }

    public z(androidx.savedstate.a aVar, H h3) {
        D2.h.f(aVar, "savedStateRegistry");
        D2.h.f(h3, "viewModelStoreOwner");
        this.f5372a = aVar;
        this.f5375d = AbstractC0681d.a(new a(h3));
    }

    private final A c() {
        return (A) this.f5375d.getValue();
    }

    @Override // androidx.savedstate.a.c
    public Bundle a() {
        Bundle bundle = new Bundle();
        Bundle bundle2 = this.f5374c;
        if (bundle2 != null) {
            bundle.putAll(bundle2);
        }
        for (Map.Entry entry : c().f().entrySet()) {
            String str = (String) entry.getKey();
            Bundle bundleA = ((x) entry.getValue()).c().a();
            if (!D2.h.b(bundleA, Bundle.EMPTY)) {
                bundle.putBundle(str, bundleA);
            }
        }
        this.f5373b = false;
        return bundle;
    }

    public final Bundle b(String str) {
        D2.h.f(str, "key");
        d();
        Bundle bundle = this.f5374c;
        Bundle bundle2 = bundle != null ? bundle.getBundle(str) : null;
        Bundle bundle3 = this.f5374c;
        if (bundle3 != null) {
            bundle3.remove(str);
        }
        Bundle bundle4 = this.f5374c;
        if (bundle4 != null && bundle4.isEmpty()) {
            this.f5374c = null;
        }
        return bundle2;
    }

    public final void d() {
        if (this.f5373b) {
            return;
        }
        Bundle bundleB = this.f5372a.b("androidx.lifecycle.internal.SavedStateHandlesProvider");
        Bundle bundle = new Bundle();
        Bundle bundle2 = this.f5374c;
        if (bundle2 != null) {
            bundle.putAll(bundle2);
        }
        if (bundleB != null) {
            bundle.putAll(bundleB);
        }
        this.f5374c = bundle;
        this.f5373b = true;
        c();
    }
}
