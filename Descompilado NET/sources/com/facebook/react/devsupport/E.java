package com.facebook.react.devsupport;

import a1.C0210a;
import android.R;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.hardware.SensorManager;
import android.os.Build;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.facebook.fbreact.specs.NativeRedBoxSpec;
import com.facebook.react.bridge.DefaultJSExceptionHandler;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactMarker;
import com.facebook.react.bridge.ReactMarkerConstants;
import com.facebook.react.bridge.UiThreadUtil;
import com.facebook.react.common.JavascriptException;
import com.facebook.react.devsupport.C0369b;
import com.facebook.react.devsupport.C0378k;
import com.facebook.react.devsupport.SharedPreferencesOnSharedPreferenceChangeListenerC0377j;
import com.facebook.react.modules.core.RCTNativeAppEventEmitter;
import d1.AbstractC0508p;
import e1.C0526c;
import e1.g;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import k1.InterfaceC0583a;
import k1.InterfaceC0584b;
import k1.InterfaceC0585c;
import k1.InterfaceC0586d;
import k1.e;

/* JADX INFO: loaded from: classes.dex */
public abstract class E implements k1.e {

    /* JADX INFO: renamed from: B, reason: collision with root package name */
    private final InterfaceC0584b f6594B;

    /* JADX INFO: renamed from: C, reason: collision with root package name */
    private List f6595C;

    /* JADX INFO: renamed from: D, reason: collision with root package name */
    private final Map f6596D;

    /* JADX INFO: renamed from: E, reason: collision with root package name */
    private final e1.k f6597E;

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final Context f6598a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final e1.g f6599b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final BroadcastReceiver f6600c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private final C0378k f6601d;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    protected final c0 f6603f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private final String f6604g;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private final File f6605h;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    private final File f6606i;

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    private final DefaultJSExceptionHandler f6607j;

    /* JADX INFO: renamed from: k, reason: collision with root package name */
    private final InterfaceC0585c f6608k;

    /* JADX INFO: renamed from: l, reason: collision with root package name */
    private final k1.h f6609l;

    /* JADX INFO: renamed from: m, reason: collision with root package name */
    private e1.j f6610m;

    /* JADX INFO: renamed from: n, reason: collision with root package name */
    private AlertDialog f6611n;

    /* JADX INFO: renamed from: o, reason: collision with root package name */
    private C0371d f6612o;

    /* JADX INFO: renamed from: r, reason: collision with root package name */
    private ReactContext f6615r;

    /* JADX INFO: renamed from: s, reason: collision with root package name */
    private final C1.a f6616s;

    /* JADX INFO: renamed from: w, reason: collision with root package name */
    private boolean f6620w;

    /* JADX INFO: renamed from: x, reason: collision with root package name */
    private String f6621x;

    /* JADX INFO: renamed from: y, reason: collision with root package name */
    private k1.j[] f6622y;

    /* JADX INFO: renamed from: z, reason: collision with root package name */
    private k1.f f6623z;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private final LinkedHashMap f6602e = new LinkedHashMap();

    /* JADX INFO: renamed from: p, reason: collision with root package name */
    private boolean f6613p = false;

    /* JADX INFO: renamed from: q, reason: collision with root package name */
    private int f6614q = 0;

    /* JADX INFO: renamed from: t, reason: collision with root package name */
    private boolean f6617t = false;

    /* JADX INFO: renamed from: u, reason: collision with root package name */
    private boolean f6618u = false;

    /* JADX INFO: renamed from: v, reason: collision with root package name */
    private boolean f6619v = false;

    /* JADX INFO: renamed from: A, reason: collision with root package name */
    private int f6593A = 0;

    class a extends BroadcastReceiver {
        a() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if (E.j0(context).equals(intent.getAction())) {
                E.this.s();
            }
        }
    }

    class b implements InterfaceC0586d {
        b() {
        }

        @Override // k1.InterfaceC0586d
        public void a() {
            if (!E.this.f6616s.n() && E.this.f6616s.o()) {
                Toast.makeText(E.this.f6598a, E.this.f6598a.getString(AbstractC0508p.f9281h), 1).show();
                E.this.f6616s.f(false);
            }
            E.this.s();
        }
    }

    class c implements DialogInterface.OnClickListener {

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        final /* synthetic */ EditText f6626b;

        c(EditText editText) {
            this.f6626b = editText;
        }

        @Override // android.content.DialogInterface.OnClickListener
        public void onClick(DialogInterface dialogInterface, int i3) {
            E.this.f6616s.h().d(this.f6626b.getText().toString());
            E.this.s();
        }
    }

    class d implements InterfaceC0586d {
        d() {
        }

        @Override // k1.InterfaceC0586d
        public void a() {
            E.this.f6616s.i(!E.this.f6616s.g());
            E.this.f6603f.h();
        }
    }

    class e extends ArrayAdapter {

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        final /* synthetic */ Set f6629b;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        e(Context context, int i3, String[] strArr, Set set) {
            super(context, i3, strArr);
            this.f6629b = set;
        }

        @Override // android.widget.BaseAdapter, android.widget.ListAdapter
        public boolean areAllItemsEnabled() {
            return false;
        }

        @Override // android.widget.ArrayAdapter, android.widget.Adapter
        public View getView(int i3, View view, ViewGroup viewGroup) {
            View view2 = super.getView(i3, view, viewGroup);
            view2.setEnabled(isEnabled(i3));
            return view2;
        }

        @Override // android.widget.BaseAdapter, android.widget.ListAdapter
        public boolean isEnabled(int i3) {
            return !this.f6629b.contains(getItem(i3));
        }
    }

    class f implements InterfaceC0584b {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        final /* synthetic */ C0369b.c f6631a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        final /* synthetic */ InterfaceC0583a f6632b;

        f(C0369b.c cVar, InterfaceC0583a interfaceC0583a) {
            this.f6631a = cVar;
            this.f6632b = interfaceC0583a;
        }

        @Override // k1.InterfaceC0584b
        public void a() {
            E.this.l0();
            if (E.this.f6594B != null) {
                E.this.f6594B.a();
            }
            ReactMarker.logMarker(ReactMarkerConstants.DOWNLOAD_END, this.f6631a.c());
            this.f6632b.a();
        }

        @Override // k1.InterfaceC0584b
        public void b(String str, Integer num, Integer num2) {
            E.this.f6608k.b(str, num, num2);
            if (E.this.f6594B != null) {
                E.this.f6594B.b(str, num, num2);
            }
        }

        @Override // k1.InterfaceC0584b
        public void c(Exception exc) {
            E.this.l0();
            if (E.this.f6594B != null) {
                E.this.f6594B.c(exc);
            }
            Y.a.n("ReactNative", "Unable to download JS bundle", exc);
            E.this.F0(exc);
            this.f6632b.b(exc);
        }
    }

    class g implements C0378k.g {
        g() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void h() {
            E.this.x();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void i() {
            E.this.s();
        }

        @Override // com.facebook.react.devsupport.C0378k.g
        public void a() {
            E.this.f6620w = false;
        }

        @Override // com.facebook.react.devsupport.C0378k.g
        public void b() {
            E.this.f6620w = true;
        }

        @Override // com.facebook.react.devsupport.C0378k.g
        public void c() {
            UiThreadUtil.runOnUiThread(new Runnable() { // from class: com.facebook.react.devsupport.F
                @Override // java.lang.Runnable
                public final void run() {
                    this.f6635b.h();
                }
            });
        }

        @Override // com.facebook.react.devsupport.C0378k.g
        public Map d() {
            return E.this.f6596D;
        }

        @Override // com.facebook.react.devsupport.C0378k.g
        public void e() {
            if (!InspectorFlags.getFuseboxEnabled()) {
                E.this.f6601d.n();
            }
            UiThreadUtil.runOnUiThread(new Runnable() { // from class: com.facebook.react.devsupport.G
                @Override // java.lang.Runnable
                public final void run() {
                    this.f6636b.i();
                }
            });
        }
    }

    public E(Context context, c0 c0Var, String str, boolean z3, k1.i iVar, InterfaceC0584b interfaceC0584b, int i3, Map<String, H1.f> map, e1.k kVar, InterfaceC0585c interfaceC0585c, k1.h hVar) {
        this.f6603f = c0Var;
        this.f6598a = context;
        this.f6604g = str;
        SharedPreferencesOnSharedPreferenceChangeListenerC0377j sharedPreferencesOnSharedPreferenceChangeListenerC0377j = new SharedPreferencesOnSharedPreferenceChangeListenerC0377j(context, new SharedPreferencesOnSharedPreferenceChangeListenerC0377j.b() { // from class: com.facebook.react.devsupport.o
            @Override // com.facebook.react.devsupport.SharedPreferencesOnSharedPreferenceChangeListenerC0377j.b
            public final void a() {
                this.f6776a.E0();
            }
        });
        this.f6616s = sharedPreferencesOnSharedPreferenceChangeListenerC0377j;
        this.f6601d = new C0378k(sharedPreferencesOnSharedPreferenceChangeListenerC0377j, context, sharedPreferencesOnSharedPreferenceChangeListenerC0377j.h());
        this.f6594B = interfaceC0584b;
        this.f6599b = new e1.g(new g.a() { // from class: com.facebook.react.devsupport.p
            @Override // e1.g.a
            public final void a() {
                this.f6777a.x();
            }
        }, i3);
        this.f6596D = map;
        this.f6600c = new a();
        String strK0 = k0();
        this.f6605h = new File(context.getFilesDir(), strK0 + "ReactNativeDevBundle.js");
        this.f6606i = context.getDir(strK0.toLowerCase(Locale.ROOT) + "_dev_js_split_bundles", 0);
        this.f6607j = new DefaultJSExceptionHandler();
        A(z3);
        this.f6608k = interfaceC0585c == null ? new C0375h(c0Var) : interfaceC0585c;
        this.f6597E = kVar;
        this.f6609l = hVar == null ? new a0(new q.i() { // from class: com.facebook.react.devsupport.q
            @Override // q.i
            public final Object get() {
                return this.f6778a.o0();
            }
        }) : hVar;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void A0() {
        this.f6616s.i(!r0.g());
        this.f6603f.h();
    }

    private void B0(Exception exc) {
        StringBuilder sb = new StringBuilder(exc.getMessage() == null ? "Exception in native call from JS" : exc.getMessage());
        for (Throwable cause = exc.getCause(); cause != null; cause = cause.getCause()) {
            sb.append("\n\n");
            sb.append(cause.getMessage());
        }
        if (!(exc instanceof JavascriptException)) {
            J0(sb.toString(), exc);
        } else {
            Y.a.n("ReactNative", "Exception in native call from JS", exc);
            I0(exc.getMessage().toString(), new k1.j[0], -1, k1.f.f9588c);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void C0() {
        UiThreadUtil.assertOnUiThread();
        if (!this.f6619v) {
            C0371d c0371d = this.f6612o;
            if (c0371d != null) {
                c0371d.i(false);
            }
            if (this.f6618u) {
                this.f6599b.f();
                this.f6618u = false;
            }
            if (this.f6617t) {
                this.f6598a.unregisterReceiver(this.f6600c);
                this.f6617t = false;
            }
            q();
            m0();
            this.f6608k.c();
            this.f6601d.j();
            return;
        }
        C0371d c0371d2 = this.f6612o;
        if (c0371d2 != null) {
            c0371d2.i(this.f6616s.m());
        }
        if (!this.f6618u) {
            this.f6599b.e((SensorManager) this.f6598a.getSystemService("sensor"));
            this.f6618u = true;
        }
        if (!this.f6617t) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(j0(this.f6598a));
            d0(this.f6598a, this.f6600c, intentFilter, true);
            this.f6617t = true;
        }
        if (this.f6613p) {
            this.f6608k.a("Reloading...");
        }
        this.f6601d.z(getClass().getSimpleName(), new g());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void F0(final Exception exc) {
        UiThreadUtil.runOnUiThread(new Runnable() { // from class: com.facebook.react.devsupport.u
            @Override // java.lang.Runnable
            public final void run() {
                this.f6784b.p0(exc);
            }
        });
    }

    private void G0(ReactContext reactContext) {
        if (this.f6615r == reactContext) {
            return;
        }
        this.f6615r = reactContext;
        C0371d c0371d = this.f6612o;
        if (c0371d != null) {
            c0371d.i(false);
        }
        if (reactContext != null) {
            this.f6612o = new C0371d(reactContext);
        }
        if (this.f6615r != null) {
            try {
                URL url = new URL(E());
                ((HMRClient) this.f6615r.getJSModule(HMRClient.class)).setup("android", url.getPath().substring(1), url.getHost(), url.getPort() != -1 ? url.getPort() : url.getDefaultPort(), this.f6616s.o(), url.getProtocol());
            } catch (MalformedURLException e4) {
                J0(e4.getMessage(), e4);
            }
        }
        E0();
    }

    private void H0(String str) {
        if (this.f6598a == null) {
            return;
        }
        try {
            URL url = new URL(str);
            int port = url.getPort() != -1 ? url.getPort() : url.getDefaultPort();
            this.f6608k.a(this.f6598a.getString(AbstractC0508p.f9285l, url.getHost() + ":" + port));
            this.f6613p = true;
        } catch (MalformedURLException e4) {
            Y.a.m("ReactNative", "Bundle url format is invalid. \n\n" + e4.toString());
        }
    }

    private void I0(final String str, final k1.j[] jVarArr, final int i3, final k1.f fVar) {
        UiThreadUtil.runOnUiThread(new Runnable() { // from class: com.facebook.react.devsupport.w
            @Override // java.lang.Runnable
            public final void run() {
                this.f6787b.z0(str, jVarArr, i3, fVar);
            }
        });
    }

    private void K0(String str, k1.j[] jVarArr, int i3, k1.f fVar) {
        this.f6621x = str;
        this.f6622y = jVarArr;
        this.f6593A = i3;
        this.f6623z = fVar;
    }

    private void d0(Context context, BroadcastReceiver broadcastReceiver, IntentFilter intentFilter, boolean z3) {
        if (Build.VERSION.SDK_INT < 34 || context.getApplicationInfo().targetSdkVersion < 34) {
            context.registerReceiver(broadcastReceiver, intentFilter);
        } else {
            context.registerReceiver(broadcastReceiver, intentFilter, z3 ? 2 : 4);
        }
    }

    private String h0() {
        try {
            return i0().k().toString();
        } catch (IllegalStateException unused) {
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String j0(Context context) {
        return context.getPackageName() + ".RELOAD_APP_ACTION";
    }

    private void m0() {
        AlertDialog alertDialog = this.f6611n;
        if (alertDialog != null) {
            alertDialog.dismiss();
            this.f6611n = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void n0(k1.g gVar) {
        this.f6601d.w(gVar);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ Context o0() {
        Activity activityI = this.f6603f.i();
        if (activityI == null || activityI.isFinishing()) {
            return null;
        }
        return activityI;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void p0(Exception exc) {
        if (exc instanceof C0526c) {
            J0(((C0526c) exc).getMessage(), exc);
        } else {
            J0(this.f6598a.getString(AbstractC0508p.f9290q), exc);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void q0(boolean z3) {
        this.f6616s.c(z3);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void r0(boolean z3) {
        this.f6616s.f(z3);
        s();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void t0() {
        Activity activityI = this.f6603f.i();
        if (activityI == null || activityI.isFinishing()) {
            Y.a.m("ReactNative", "Unable to launch change bundle location because react activity is not available");
            return;
        }
        EditText editText = new EditText(activityI);
        editText.setHint("localhost:8081");
        new AlertDialog.Builder(activityI).setTitle(this.f6598a.getString(AbstractC0508p.f9275b)).setView(editText).setPositiveButton(R.string.ok, new c(editText)).create().show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void u0() {
        boolean zO = this.f6616s.o();
        this.f6616s.f(!zO);
        ReactContext reactContext = this.f6615r;
        if (reactContext != null) {
            if (zO) {
                ((HMRClient) reactContext.getJSModule(HMRClient.class)).disable();
            } else {
                ((HMRClient) reactContext.getJSModule(HMRClient.class)).enable();
            }
        }
        if (zO || this.f6616s.n()) {
            return;
        }
        Context context = this.f6598a;
        Toast.makeText(context, context.getString(AbstractC0508p.f9282i), 1).show();
        this.f6616s.k(true);
        s();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void v0() {
        if (!this.f6616s.m()) {
            Activity activityI = this.f6603f.i();
            if (activityI == null) {
                Y.a.m("ReactNative", "Unable to get reference to react activity");
            } else {
                C0371d.h(activityI);
            }
        }
        this.f6616s.c(!r0.m());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void w0() {
        Intent intent = new Intent(this.f6598a, (Class<?>) C0379l.class);
        intent.setFlags(268435456);
        this.f6598a.startActivity(intent);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void x0(InterfaceC0586d[] interfaceC0586dArr, DialogInterface dialogInterface, int i3) {
        interfaceC0586dArr[i3].a();
        this.f6611n = null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void y0(DialogInterface dialogInterface) {
        this.f6611n = null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void z0(String str, k1.j[] jVarArr, int i3, k1.f fVar) {
        K0(str, jVarArr, i3, fVar);
        if (this.f6610m == null) {
            e1.j jVarG = g(NativeRedBoxSpec.NAME);
            if (jVarG != null) {
                this.f6610m = jVarG;
            } else {
                this.f6610m = new i0(this);
            }
            this.f6610m.f(NativeRedBoxSpec.NAME);
        }
        if (this.f6610m.a()) {
            return;
        }
        this.f6610m.b();
    }

    @Override // k1.e
    public void A(boolean z3) {
        this.f6619v = z3;
        E0();
    }

    @Override // k1.e
    public k1.f B() {
        return this.f6623z;
    }

    @Override // k1.e
    public ReactContext C() {
        return this.f6615r;
    }

    @Override // k1.e
    /* JADX INFO: renamed from: D, reason: merged with bridge method [inline-methods] */
    public void s0() {
        this.f6601d.x(this.f6615r, this.f6598a.getString(AbstractC0508p.f9286m));
    }

    public void D0(String str, InterfaceC0583a interfaceC0583a) {
        ReactMarker.logMarker(ReactMarkerConstants.DOWNLOAD_START);
        H0(str);
        C0369b.c cVar = new C0369b.c();
        this.f6601d.o(new f(cVar, interfaceC0583a), this.f6605h, str, cVar);
    }

    @Override // k1.e
    public String E() {
        String str = this.f6604g;
        return str == null ? "" : this.f6601d.v((String) C0210a.c(str));
    }

    public void E0() {
        if (UiThreadUtil.isOnUiThread()) {
            C0();
        } else {
            UiThreadUtil.runOnUiThread(new Runnable() { // from class: com.facebook.react.devsupport.v
                @Override // java.lang.Runnable
                public final void run() {
                    this.f6786b.C0();
                }
            });
        }
    }

    public void J0(String str, Throwable th) {
        Y.a.n("ReactNative", "Exception in native call", th);
        I0(str, l0.a(th), -1, k1.f.f9589d);
    }

    @Override // k1.e
    public View a(String str) {
        return this.f6603f.a(str);
    }

    @Override // k1.e
    public void b(View view) {
        this.f6603f.b(view);
    }

    @Override // k1.e
    public void c(final boolean z3) {
        if (this.f6619v) {
            UiThreadUtil.runOnUiThread(new Runnable() { // from class: com.facebook.react.devsupport.n
                @Override // java.lang.Runnable
                public final void run() {
                    this.f6772b.q0(z3);
                }
            });
        }
    }

    @Override // k1.e
    public void d(String str, e.a aVar) {
        this.f6609l.d(str, aVar);
    }

    @Override // k1.e
    public void e() {
        this.f6609l.e();
    }

    public InterfaceC0585c e0() {
        return this.f6608k;
    }

    @Override // k1.e
    public void f(final boolean z3) {
        if (this.f6619v) {
            UiThreadUtil.runOnUiThread(new Runnable() { // from class: com.facebook.react.devsupport.s
                @Override // java.lang.Runnable
                public final void run() {
                    this.f6781b.r0(z3);
                }
            });
        }
    }

    public C0378k f0() {
        return this.f6601d;
    }

    @Override // k1.e
    public e1.j g(String str) {
        e1.k kVar = this.f6597E;
        if (kVar == null) {
            return null;
        }
        return kVar.g(str);
    }

    public String g0() {
        return this.f6604g;
    }

    @Override // k1.e
    public void h() {
        if (this.f6619v) {
            UiThreadUtil.runOnUiThread(new Runnable() { // from class: com.facebook.react.devsupport.t
                @Override // java.lang.Runnable
                public final void run() {
                    this.f6783b.A0();
                }
            });
        }
    }

    @Override // com.facebook.react.bridge.JSExceptionHandler
    public void handleException(Exception exc) {
        if (this.f6619v) {
            B0(exc);
        } else {
            this.f6607j.handleException(exc);
        }
    }

    @Override // k1.e
    public Activity i() {
        return this.f6603f.i();
    }

    public c0 i0() {
        return this.f6603f;
    }

    @Override // k1.e
    public String j() {
        return this.f6605h.getAbsolutePath();
    }

    @Override // k1.e
    public void k(final k1.g gVar) {
        new Runnable() { // from class: com.facebook.react.devsupport.r
            @Override // java.lang.Runnable
            public final void run() {
                this.f6779b.n0(gVar);
            }
        }.run();
    }

    protected abstract String k0();

    @Override // k1.e
    public String l() {
        return this.f6621x;
    }

    protected void l0() {
        this.f6608k.c();
        this.f6613p = false;
    }

    @Override // k1.e
    public void m() {
        this.f6601d.i();
    }

    @Override // k1.e
    public boolean n() {
        return this.f6619v;
    }

    @Override // k1.e
    public C1.a o() {
        return this.f6616s;
    }

    @Override // k1.e
    public void p(String str, InterfaceC0586d interfaceC0586d) {
        this.f6602e.put(str, interfaceC0586d);
    }

    @Override // k1.e
    public void q() {
        e1.j jVar = this.f6610m;
        if (jVar == null) {
            return;
        }
        jVar.c();
    }

    @Override // k1.e
    public void r(ReactContext reactContext) {
        G0(reactContext);
    }

    @Override // k1.e
    public k1.i t() {
        return null;
    }

    @Override // k1.e
    public void u() {
        if (this.f6619v) {
            this.f6601d.y();
        }
    }

    @Override // k1.e
    public boolean v() {
        if (this.f6619v && this.f6605h.exists()) {
            try {
                String packageName = this.f6598a.getPackageName();
                if (this.f6605h.lastModified() > this.f6598a.getPackageManager().getPackageInfo(packageName, 0).lastUpdateTime) {
                    File file = new File(String.format(Locale.US, "/data/local/tmp/exopackage/%s//secondary-dex", packageName));
                    if (file.exists()) {
                        return this.f6605h.lastModified() > file.lastModified();
                    }
                    return true;
                }
            } catch (PackageManager.NameNotFoundException unused) {
                Y.a.m("ReactNative", "DevSupport is unable to get current app info");
            }
        }
        return false;
    }

    @Override // k1.e
    public k1.j[] w() {
        return this.f6622y;
    }

    @Override // k1.e
    public void x() {
        if (this.f6611n == null && this.f6619v && !ActivityManager.isUserAMonkey()) {
            LinkedHashMap linkedHashMap = new LinkedHashMap();
            HashSet hashSet = new HashSet();
            linkedHashMap.put(this.f6598a.getString(AbstractC0508p.f9289p), new b());
            if (this.f6616s.j()) {
                boolean z3 = this.f6620w;
                String string = this.f6598a.getString(z3 ? AbstractC0508p.f9276c : AbstractC0508p.f9277d);
                if (!z3) {
                    hashSet.add(string);
                }
                linkedHashMap.put(string, new InterfaceC0586d() { // from class: com.facebook.react.devsupport.x
                    @Override // k1.InterfaceC0586d
                    public final void a() {
                        this.f6792a.s0();
                    }
                });
            }
            linkedHashMap.put(this.f6598a.getString(AbstractC0508p.f9275b), new InterfaceC0586d() { // from class: com.facebook.react.devsupport.y
                @Override // k1.InterfaceC0586d
                public final void a() {
                    this.f6793a.t0();
                }
            });
            linkedHashMap.put(this.f6598a.getString(AbstractC0508p.f9284k), new d());
            linkedHashMap.put(this.f6616s.o() ? this.f6598a.getString(AbstractC0508p.f9283j) : this.f6598a.getString(AbstractC0508p.f9280g), new InterfaceC0586d() { // from class: com.facebook.react.devsupport.z
                @Override // k1.InterfaceC0586d
                public final void a() {
                    this.f6794a.u0();
                }
            });
            linkedHashMap.put(this.f6616s.m() ? this.f6598a.getString(AbstractC0508p.f9288o) : this.f6598a.getString(AbstractC0508p.f9287n), new InterfaceC0586d() { // from class: com.facebook.react.devsupport.A
                @Override // k1.InterfaceC0586d
                public final void a() {
                    this.f6575a.v0();
                }
            });
            linkedHashMap.put(this.f6598a.getString(AbstractC0508p.f9291r), new InterfaceC0586d() { // from class: com.facebook.react.devsupport.B
                @Override // k1.InterfaceC0586d
                public final void a() {
                    this.f6576a.w0();
                }
            });
            if (this.f6602e.size() > 0) {
                linkedHashMap.putAll(this.f6602e);
            }
            final InterfaceC0586d[] interfaceC0586dArr = (InterfaceC0586d[]) linkedHashMap.values().toArray(new InterfaceC0586d[0]);
            Activity activityI = this.f6603f.i();
            if (activityI == null || activityI.isFinishing()) {
                Y.a.m("ReactNative", "Unable to launch dev options menu because react activity isn't available");
                return;
            }
            LinearLayout linearLayout = new LinearLayout(activityI);
            linearLayout.setOrientation(1);
            TextView textView = new TextView(activityI);
            textView.setText(activityI.getString(AbstractC0508p.f9278e, k0()));
            textView.setPadding(0, 50, 0, 0);
            textView.setGravity(17);
            textView.setTextSize(16.0f);
            textView.setTypeface(textView.getTypeface(), 1);
            linearLayout.addView(textView);
            String strH0 = h0();
            if (strH0 != null) {
                TextView textView2 = new TextView(activityI);
                textView2.setText(activityI.getString(AbstractC0508p.f9279f, strH0));
                textView2.setPadding(0, 20, 0, 0);
                textView2.setGravity(17);
                textView2.setTextSize(14.0f);
                linearLayout.addView(textView2);
            }
            AlertDialog alertDialogCreate = new AlertDialog.Builder(activityI).setCustomTitle(linearLayout).setAdapter(new e(activityI, R.layout.simple_list_item_1, (String[]) linkedHashMap.keySet().toArray(new String[0]), hashSet), new DialogInterface.OnClickListener() { // from class: com.facebook.react.devsupport.C
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i3) {
                    this.f6577b.x0(interfaceC0586dArr, dialogInterface, i3);
                }
            }).setOnCancelListener(new DialogInterface.OnCancelListener() { // from class: com.facebook.react.devsupport.D
                @Override // android.content.DialogInterface.OnCancelListener
                public final void onCancel(DialogInterface dialogInterface) {
                    this.f6592b.y0(dialogInterface);
                }
            }).create();
            this.f6611n = alertDialogCreate;
            alertDialogCreate.show();
            ReactContext reactContext = this.f6615r;
            if (reactContext != null) {
                ((RCTNativeAppEventEmitter) reactContext.getJSModule(RCTNativeAppEventEmitter.class)).emit("RCTDevMenuShown", null);
            }
        }
    }

    @Override // k1.e
    public Pair y(Pair pair) {
        List list = this.f6595C;
        if (list != null) {
            Iterator it = list.iterator();
            if (it.hasNext()) {
                androidx.activity.result.d.a(it.next());
                throw null;
            }
        }
        return pair;
    }

    @Override // k1.e
    public void z(ReactContext reactContext) {
        if (reactContext == this.f6615r) {
            G0(null);
        }
        System.gc();
    }
}
