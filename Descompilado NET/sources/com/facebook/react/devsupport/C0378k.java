package com.facebook.react.devsupport;

import H1.e;
import M2.B;
import M2.InterfaceC0194e;
import M2.InterfaceC0195f;
import M2.z;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.Settings;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.devsupport.C0369b;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import k1.InterfaceC0584b;

/* JADX INFO: renamed from: com.facebook.react.devsupport.k, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public class C0378k {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final C1.a f6738a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final H1.d f6739b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final M2.z f6740c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private final C0369b f6741d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private final W f6742e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private final Context f6743f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private final String f6744g;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private H1.b f6745h;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    private M f6746i;

    /* JADX INFO: renamed from: com.facebook.react.devsupport.k$a */
    class a extends AsyncTask {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        final /* synthetic */ g f6747a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        final /* synthetic */ String f6748b;

        /* JADX INFO: renamed from: com.facebook.react.devsupport.k$a$a, reason: collision with other inner class name */
        class C0105a extends H1.c {
            C0105a() {
            }

            @Override // H1.f
            public void b(Object obj) {
                a.this.f6747a.e();
            }
        }

        /* JADX INFO: renamed from: com.facebook.react.devsupport.k$a$b */
        class b extends H1.c {
            b() {
            }

            @Override // H1.f
            public void b(Object obj) {
                a.this.f6747a.c();
            }
        }

        /* JADX INFO: renamed from: com.facebook.react.devsupport.k$a$c */
        class c implements e.b {
            c() {
            }

            @Override // H1.e.b
            public void a() {
                a.this.f6747a.a();
            }

            @Override // H1.e.b
            public void b() {
                a.this.f6747a.b();
            }
        }

        a(g gVar, String str) {
            this.f6747a = gVar;
            this.f6748b = str;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
        public Void doInBackground(Void... voidArr) {
            HashMap map = new HashMap();
            map.put("reload", new C0105a());
            map.put("devMenu", new b());
            Map mapD = this.f6747a.d();
            if (mapD != null) {
                map.putAll(mapD);
            }
            map.putAll(new H1.a().d());
            c cVar = new c();
            C0378k.this.f6745h = new H1.b(this.f6748b, C0378k.this.f6739b, map, cVar);
            C0378k.this.f6745h.f();
            return null;
        }
    }

    /* JADX INFO: renamed from: com.facebook.react.devsupport.k$b */
    class b extends AsyncTask {
        b() {
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
        public Void doInBackground(Void... voidArr) {
            if (C0378k.this.f6745h != null) {
                C0378k.this.f6745h.e();
                C0378k.this.f6745h = null;
            }
            return null;
        }
    }

    /* JADX INFO: renamed from: com.facebook.react.devsupport.k$c */
    class c extends AsyncTask {
        c() {
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
        public Void doInBackground(Void... voidArr) {
            Map mapE = com.facebook.react.modules.systeminfo.a.e(C0378k.this.f6743f);
            C0378k.this.f6746i = new CxxInspectorPackagerConnection(C0378k.this.s(), (String) mapE.get("deviceName"), C0378k.this.f6744g);
            C0378k.this.f6746i.connect();
            return null;
        }
    }

    /* JADX INFO: renamed from: com.facebook.react.devsupport.k$d */
    class d extends AsyncTask {
        d() {
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
        public Void doInBackground(Void... voidArr) {
            if (C0378k.this.f6746i != null) {
                C0378k.this.f6746i.closeQuietly();
                C0378k.this.f6746i = null;
            }
            return null;
        }
    }

    /* JADX INFO: renamed from: com.facebook.react.devsupport.k$f */
    private enum f {
        BUNDLE("bundle"),
        MAP("map");


        /* JADX INFO: renamed from: b, reason: collision with root package name */
        private final String f6762b;

        f(String str) {
            this.f6762b = str;
        }

        public String b() {
            return this.f6762b;
        }
    }

    /* JADX INFO: renamed from: com.facebook.react.devsupport.k$g */
    public interface g {
        void a();

        void b();

        void c();

        Map d();

        void e();
    }

    public C0378k(C1.a aVar, Context context, H1.d dVar) {
        this.f6738a = aVar;
        this.f6739b = dVar;
        z.a aVar2 = new z.a();
        TimeUnit timeUnit = TimeUnit.MILLISECONDS;
        M2.z zVarB = aVar2.e(5000L, timeUnit).M(0L, timeUnit).N(0L, timeUnit).b();
        this.f6740c = zVarB;
        this.f6741d = new C0369b(zVarB);
        this.f6742e = new W(zVarB);
        this.f6743f = context;
        this.f6744g = context.getPackageName();
    }

    private String k(String str, f fVar) {
        return l(str, fVar, this.f6739b.b());
    }

    private String l(String str, f fVar, String str2) {
        return m(str, fVar, str2, false, true);
    }

    private String m(String str, f fVar, String str2, boolean z3, boolean z4) {
        boolean zP = p();
        StringBuilder sb = new StringBuilder();
        for (Map.Entry entry : this.f6739b.a().entrySet()) {
            if (((String) entry.getValue()).length() != 0) {
                sb.append("&" + ((String) entry.getKey()) + "=" + Uri.encode((String) entry.getValue()));
            }
        }
        StringBuilder sb2 = new StringBuilder();
        sb2.append(String.format(Locale.US, "http://%s/%s.%s?platform=android&dev=%s&lazy=%s&minify=%s&app=%s&modulesOnly=%s&runModule=%s", str2, str, fVar.b(), Boolean.valueOf(zP), Boolean.valueOf(zP), Boolean.valueOf(t()), this.f6744g, z3 ? "true" : "false", z4 ? "true" : "false"));
        sb2.append(InspectorFlags.getFuseboxEnabled() ? "&excludeSource=true&sourcePaths=url-server" : "");
        sb2.append(sb.toString());
        return sb2.toString();
    }

    private boolean p() {
        return this.f6738a.n();
    }

    private String r() {
        return u(String.format(Locale.US, "android-%s-%s-%s", this.f6744g, Settings.Secure.getString(this.f6743f.getContentResolver(), "android_id"), InspectorFlags.getFuseboxEnabled() ? "fusebox" : "legacy"));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String s() {
        return String.format(Locale.US, "http://%s/inspector/device?name=%s&app=%s&device=%s&profiling=%b", this.f6739b.b(), Uri.encode(com.facebook.react.modules.systeminfo.a.d()), Uri.encode(this.f6744g), Uri.encode(r()), Boolean.valueOf(InspectorFlags.getIsProfilingBuild()));
    }

    private boolean t() {
        return this.f6738a.l();
    }

    private static String u(String str) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.reset();
            try {
                byte[] bArrDigest = messageDigest.digest(str.getBytes("UTF-8"));
                return String.format("%02x%02x%02x%02x%02x%02x%02x%02x%02x%02x%02x%02x%02x%02x%02x%02x%02x%02x%02x%02x", Byte.valueOf(bArrDigest[0]), Byte.valueOf(bArrDigest[1]), Byte.valueOf(bArrDigest[2]), Byte.valueOf(bArrDigest[3]), Byte.valueOf(bArrDigest[4]), Byte.valueOf(bArrDigest[5]), Byte.valueOf(bArrDigest[6]), Byte.valueOf(bArrDigest[7]), Byte.valueOf(bArrDigest[8]), Byte.valueOf(bArrDigest[9]), Byte.valueOf(bArrDigest[10]), Byte.valueOf(bArrDigest[11]), Byte.valueOf(bArrDigest[12]), Byte.valueOf(bArrDigest[13]), Byte.valueOf(bArrDigest[14]), Byte.valueOf(bArrDigest[15]), Byte.valueOf(bArrDigest[16]), Byte.valueOf(bArrDigest[17]), Byte.valueOf(bArrDigest[18]), Byte.valueOf(bArrDigest[19]));
            } catch (UnsupportedEncodingException e4) {
                throw new AssertionError("This environment doesn't support UTF-8 encoding", e4);
            }
        } catch (NoSuchAlgorithmException e5) {
            throw new AssertionError("Could not get standard SHA-256 algorithm", e5);
        }
    }

    public void i() {
        new d().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
    }

    public void j() {
        new b().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
    }

    public void n() {
        M m3 = this.f6746i;
        if (m3 != null) {
            m3.sendEventToAllConnections("{ \"id\":1,\"method\":\"Debugger.disable\" }");
        }
    }

    public void o(InterfaceC0584b interfaceC0584b, File file, String str, C0369b.c cVar) {
        this.f6741d.e(interfaceC0584b, file, str, cVar);
    }

    public String q(String str) {
        return l(str, f.BUNDLE, this.f6739b.b());
    }

    public String v(String str) {
        return k(str, f.BUNDLE);
    }

    public void w(k1.g gVar) {
        String strB = this.f6739b.b();
        if (strB != null) {
            this.f6742e.a(strB, gVar);
        } else {
            Y.a.I("ReactNative", "No packager host configured.");
            gVar.a(false);
        }
    }

    public void x(ReactContext reactContext, String str) {
        this.f6740c.b(new B.a().m(String.format(Locale.US, "http://%s/open-debugger?device=%s", this.f6739b.b(), Uri.encode(r()))).g("POST", M2.C.d(null, "")).b()).o(new e(reactContext, str));
    }

    public void y() {
        if (this.f6746i != null) {
            Y.a.I("ReactNative", "Inspector connection already open, nooping.");
        } else {
            new c().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
        }
    }

    public void z(String str, g gVar) {
        if (this.f6745h != null) {
            Y.a.I("ReactNative", "Packager connection already open, nooping.");
        } else {
            new a(gVar, str).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
        }
    }

    /* JADX INFO: renamed from: com.facebook.react.devsupport.k$e */
    class e implements InterfaceC0195f {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        final /* synthetic */ ReactContext f6756a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        final /* synthetic */ String f6757b;

        e(ReactContext reactContext, String str) {
            this.f6756a = reactContext;
            this.f6757b = str;
        }

        @Override // M2.InterfaceC0195f
        public void a(InterfaceC0194e interfaceC0194e, IOException iOException) {
            T1.c.d(this.f6756a, this.f6757b);
        }

        @Override // M2.InterfaceC0195f
        public void b(InterfaceC0194e interfaceC0194e, M2.D d4) {
        }
    }
}
