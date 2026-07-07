package app.netmirror.netmirrornew;

import D2.h;
import android.app.Application;
import android.content.Context;
import com.facebook.react.defaults.d;
import com.facebook.react.defaults.g;
import com.facebook.react.soloader.OpenSourceMergedSoMapping;
import com.facebook.soloader.SoLoader;
import d1.C0503k;
import d1.InterfaceC0491A;
import d1.InterfaceC0516y;
import d1.N;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public final class MainApplication extends Application implements InterfaceC0516y {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final N f5567b = new a(this);

    public static final class a extends g {

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        private final boolean f5568c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        private final boolean f5569d;

        a(MainApplication mainApplication) {
            super(mainApplication);
            this.f5568c = true;
            this.f5569d = true;
        }

        @Override // d1.N
        protected String j() {
            return "index";
        }

        @Override // d1.N
        protected List m() {
            ArrayList arrayListA = new C0503k(this).a();
            h.e(arrayListA, "apply(...)");
            return arrayListA;
        }

        @Override // d1.N
        public boolean u() {
            return false;
        }

        @Override // com.facebook.react.defaults.g
        protected Boolean y() {
            return Boolean.valueOf(this.f5569d);
        }

        @Override // com.facebook.react.defaults.g
        protected boolean z() {
            return this.f5568c;
        }
    }

    @Override // d1.InterfaceC0516y
    public N a() {
        return this.f5567b;
    }

    @Override // d1.InterfaceC0516y
    public InterfaceC0491A b() {
        Context applicationContext = getApplicationContext();
        h.e(applicationContext, "getApplicationContext(...)");
        return d.e(applicationContext, a(), null, 4, null);
    }

    @Override // android.app.Application
    public void onCreate() {
        super.onCreate();
        SoLoader.l(this, OpenSourceMergedSoMapping.f7227a);
        com.facebook.react.defaults.a.c(false, false, false, 7, null);
    }
}
