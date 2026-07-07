package com.facebook.react.devsupport;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.facebook.react.bridge.ReactContext;
import d1.AbstractC0505m;
import d1.AbstractC0507o;
import java.util.Arrays;
import java.util.Locale;
import kotlin.jvm.internal.DefaultConstructorMarker;

/* JADX INFO: loaded from: classes.dex */
public final class L extends FrameLayout {

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    public static final a f6643e = new a(null);

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final TextView f6644b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final com.facebook.react.modules.debug.h f6645c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private final b f6646d;

    public static final class a {
        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private a() {
        }
    }

    private final class b implements Runnable {

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        private boolean f6647b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        private int f6648c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        private int f6649d;

        public b() {
        }

        public final void a() {
            this.f6647b = false;
            L.this.post(this);
        }

        public final void b() {
            this.f6647b = true;
        }

        @Override // java.lang.Runnable
        public void run() {
            if (this.f6647b) {
                return;
            }
            this.f6648c += L.this.f6645c.d() - L.this.f6645c.g();
            this.f6649d += L.this.f6645c.c();
            L l3 = L.this;
            l3.c(l3.f6645c.e(), L.this.f6645c.f(), this.f6648c, this.f6649d);
            L.this.f6645c.j();
            L.this.postDelayed(this, 500L);
        }
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public L(ReactContext reactContext) {
        super(reactContext);
        D2.h.c(reactContext);
        View.inflate(reactContext, AbstractC0507o.f9257c, this);
        View viewFindViewById = findViewById(AbstractC0505m.f9242o);
        D2.h.d(viewFindViewById, "null cannot be cast to non-null type android.widget.TextView");
        this.f6644b = (TextView) viewFindViewById;
        this.f6645c = new com.facebook.react.modules.debug.h(reactContext);
        this.f6646d = new b();
        c(0.0d, 0.0d, 0, 0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void c(double d4, double d5, int i3, int i4) {
        D2.u uVar = D2.u.f192a;
        String str = String.format(Locale.US, "UI: %.1f fps\n%d dropped so far\n%d stutters (4+) so far\nJS: %.1f fps", Arrays.copyOf(new Object[]{Double.valueOf(d4), Integer.valueOf(i3), Integer.valueOf(i4), Double.valueOf(d5)}, 4));
        D2.h.e(str, "format(...)");
        this.f6644b.setText(str);
        Y.a.b("ReactNative", str);
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.f6645c.j();
        com.facebook.react.modules.debug.h.l(this.f6645c, 0.0d, 1, null);
        this.f6646d.a();
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.f6645c.n();
        this.f6646d.b();
    }
}
