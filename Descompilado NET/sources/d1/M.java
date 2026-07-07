package d1;

import a1.C0210a;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import com.facebook.hermes.reactexecutor.HermesExecutor;
import com.facebook.react.bridge.JSBundleLoader;
import com.facebook.react.bridge.JSExceptionHandler;
import com.facebook.react.bridge.JavaScriptExecutorFactory;
import com.facebook.react.bridge.NotThreadSafeBridgeIdleDebugListener;
import com.facebook.react.bridge.UIManagerProvider;
import com.facebook.react.common.LifecycleState;
import com.facebook.react.devsupport.C0376i;
import com.facebook.react.jscexecutor.JSCExecutor;
import d1.V;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import k1.InterfaceC0584b;
import k1.InterfaceC0585c;
import q1.InterfaceC0651b;
import t1.C0728a;

/* JADX INFO: loaded from: classes.dex */
public class M {

    /* JADX INFO: renamed from: B, reason: collision with root package name */
    private static final String f9121B = "M";

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private String f9124b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private JSBundleLoader f9125c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private String f9126d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private NotThreadSafeBridgeIdleDebugListener f9127e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private Application f9128f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private boolean f9129g;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private com.facebook.react.devsupport.H f9130h;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    private boolean f9131i;

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    private boolean f9132j;

    /* JADX INFO: renamed from: k, reason: collision with root package name */
    private LifecycleState f9133k;

    /* JADX INFO: renamed from: l, reason: collision with root package name */
    private JSExceptionHandler f9134l;

    /* JADX INFO: renamed from: m, reason: collision with root package name */
    private Activity f9135m;

    /* JADX INFO: renamed from: n, reason: collision with root package name */
    private B1.a f9136n;

    /* JADX INFO: renamed from: o, reason: collision with root package name */
    private boolean f9137o;

    /* JADX INFO: renamed from: p, reason: collision with root package name */
    private InterfaceC0584b f9138p;

    /* JADX INFO: renamed from: q, reason: collision with root package name */
    private JavaScriptExecutorFactory f9139q;

    /* JADX INFO: renamed from: t, reason: collision with root package name */
    private UIManagerProvider f9142t;

    /* JADX INFO: renamed from: u, reason: collision with root package name */
    private Map f9143u;

    /* JADX INFO: renamed from: v, reason: collision with root package name */
    private V.a f9144v;

    /* JADX INFO: renamed from: w, reason: collision with root package name */
    private e1.k f9145w;

    /* JADX INFO: renamed from: x, reason: collision with root package name */
    private InterfaceC0585c f9146x;

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final List f9123a = new ArrayList();

    /* JADX INFO: renamed from: r, reason: collision with root package name */
    private int f9140r = 1;

    /* JADX INFO: renamed from: s, reason: collision with root package name */
    private int f9141s = -1;

    /* JADX INFO: renamed from: y, reason: collision with root package name */
    private EnumC0498f f9147y = null;

    /* JADX INFO: renamed from: z, reason: collision with root package name */
    private InterfaceC0651b f9148z = null;

    /* JADX INFO: renamed from: A, reason: collision with root package name */
    private k1.h f9122A = null;

    M() {
    }

    private JavaScriptExecutorFactory c(String str, String str2, Context context) {
        J.J(context);
        EnumC0498f enumC0498f = this.f9147y;
        if (enumC0498f != null) {
            if (enumC0498f == EnumC0498f.f9206c) {
                HermesExecutor.e();
                return new C0.a();
            }
            JSCExecutor.b();
            return new C0728a(str, str2);
        }
        try {
            try {
                HermesExecutor.e();
                return new C0.a();
            } catch (UnsatisfiedLinkError unused) {
                JSCExecutor.b();
                return new C0728a(str, str2);
            }
        } catch (UnsatisfiedLinkError e4) {
            Y.a.m(f9121B, "Unable to load neither the Hermes nor the JSC native library. Your application is not built correctly and will fail to execute");
            if (e4.getMessage().contains("__cxa_bad_typeid")) {
                throw e4;
            }
            return null;
        }
    }

    public M a(O o3) {
        this.f9123a.add(o3);
        return this;
    }

    public J b() {
        String str;
        C0210a.d(this.f9128f, "Application property has not been set with this builder");
        if (this.f9133k == LifecycleState.f6518d) {
            C0210a.d(this.f9135m, "Activity needs to be set if initial lifecycle state is resumed");
        }
        boolean z3 = true;
        C0210a.b((!this.f9129g && this.f9124b == null && this.f9125c == null) ? false : true, "JS Bundle File or Asset URL has to be provided when dev support is disabled");
        if (this.f9126d == null && this.f9124b == null && this.f9125c == null) {
            z3 = false;
        }
        C0210a.b(z3, "Either MainModulePath or JS Bundle File needs to be provided");
        String packageName = this.f9128f.getPackageName();
        String strD = com.facebook.react.modules.systeminfo.a.d();
        Application application = this.f9128f;
        Activity activity = this.f9135m;
        B1.a aVar = this.f9136n;
        JavaScriptExecutorFactory javaScriptExecutorFactory = this.f9139q;
        JavaScriptExecutorFactory javaScriptExecutorFactoryC = javaScriptExecutorFactory == null ? c(packageName, strD, application.getApplicationContext()) : javaScriptExecutorFactory;
        JSBundleLoader jSBundleLoaderCreateAssetLoader = this.f9125c;
        if (jSBundleLoaderCreateAssetLoader == null && (str = this.f9124b) != null) {
            jSBundleLoaderCreateAssetLoader = JSBundleLoader.createAssetLoader(this.f9128f, str, false);
        }
        JSBundleLoader jSBundleLoader = jSBundleLoaderCreateAssetLoader;
        String str2 = this.f9126d;
        List list = this.f9123a;
        boolean z4 = this.f9129g;
        com.facebook.react.devsupport.H c0376i = this.f9130h;
        if (c0376i == null) {
            c0376i = new C0376i();
        }
        return new J(application, activity, aVar, javaScriptExecutorFactoryC, jSBundleLoader, str2, list, z4, c0376i, this.f9131i, this.f9132j, this.f9127e, (LifecycleState) C0210a.d(this.f9133k, "Initial lifecycle state was not set"), this.f9134l, null, this.f9137o, this.f9138p, this.f9140r, this.f9141s, this.f9142t, this.f9143u, this.f9144v, this.f9145w, this.f9146x, this.f9148z, this.f9122A);
    }

    public M d(Application application) {
        this.f9128f = application;
        return this;
    }

    public M e(String str) {
        String str2;
        if (str == null) {
            str2 = null;
        } else {
            str2 = "assets://" + str;
        }
        this.f9124b = str2;
        this.f9125c = null;
        return this;
    }

    public M f(InterfaceC0651b interfaceC0651b) {
        this.f9148z = interfaceC0651b;
        return this;
    }

    public M g(InterfaceC0585c interfaceC0585c) {
        this.f9146x = interfaceC0585c;
        return this;
    }

    public M h(com.facebook.react.devsupport.H h3) {
        this.f9130h = h3;
        return this;
    }

    public M i(LifecycleState lifecycleState) {
        this.f9133k = lifecycleState;
        return this;
    }

    public M j(String str) {
        if (!str.startsWith("assets://")) {
            return k(JSBundleLoader.createFileLoader(str));
        }
        this.f9124b = str;
        this.f9125c = null;
        return this;
    }

    public M k(JSBundleLoader jSBundleLoader) {
        this.f9125c = jSBundleLoader;
        this.f9124b = null;
        return this;
    }

    public M l(EnumC0498f enumC0498f) {
        this.f9147y = enumC0498f;
        return this;
    }

    public M m(JSExceptionHandler jSExceptionHandler) {
        this.f9134l = jSExceptionHandler;
        return this;
    }

    public M n(String str) {
        this.f9126d = str;
        return this;
    }

    public M o(JavaScriptExecutorFactory javaScriptExecutorFactory) {
        this.f9139q = javaScriptExecutorFactory;
        return this;
    }

    public M p(boolean z3) {
        this.f9137o = z3;
        return this;
    }

    public M q(k1.h hVar) {
        this.f9122A = hVar;
        return this;
    }

    public M r(V.a aVar) {
        this.f9144v = aVar;
        return this;
    }

    public M t(boolean z3) {
        this.f9131i = z3;
        return this;
    }

    public M u(e1.k kVar) {
        this.f9145w = kVar;
        return this;
    }

    public M v(UIManagerProvider uIManagerProvider) {
        this.f9142t = uIManagerProvider;
        return this;
    }

    public M w(boolean z3) {
        this.f9129g = z3;
        return this;
    }

    public M s(k1.i iVar) {
        return this;
    }
}
