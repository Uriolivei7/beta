package com.facebook.imagepipeline.producers;

import android.net.Uri;
import com.facebook.common.time.RealtimeSinceBootClock;
import com.facebook.imagepipeline.producers.Y;
import e0.InterfaceC0523b;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/* JADX INFO: loaded from: classes.dex */
public class E extends AbstractC0344d {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private int f5980a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private String f5981b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final Map f5982c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private final ExecutorService f5983d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private final InterfaceC0523b f5984e;

    class a implements Runnable {

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        final /* synthetic */ c f5985b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        final /* synthetic */ Y.a f5986c;

        a(c cVar, Y.a aVar) {
            this.f5985b = cVar;
            this.f5986c = aVar;
        }

        @Override // java.lang.Runnable
        public void run() throws Throwable {
            E.this.j(this.f5985b, this.f5986c);
        }
    }

    class b extends C0346f {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        final /* synthetic */ Future f5988a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        final /* synthetic */ Y.a f5989b;

        b(Future future, Y.a aVar) {
            this.f5988a = future;
            this.f5989b = aVar;
        }

        @Override // com.facebook.imagepipeline.producers.C0346f, com.facebook.imagepipeline.producers.g0
        public void a() {
            if (this.f5988a.cancel(false)) {
                this.f5989b.b();
            }
        }
    }

    public static class c extends D {

        /* JADX INFO: renamed from: f, reason: collision with root package name */
        private long f5991f;

        /* JADX INFO: renamed from: g, reason: collision with root package name */
        private long f5992g;

        /* JADX INFO: renamed from: h, reason: collision with root package name */
        private long f5993h;

        public c(InterfaceC0354n interfaceC0354n, f0 f0Var) {
            super(interfaceC0354n, f0Var);
        }
    }

    public E() {
        this((String) null, (Map) null, RealtimeSinceBootClock.get());
    }

    private HttpURLConnection g(Uri uri, int i3) throws IOException {
        HttpURLConnection httpURLConnectionO = o(uri);
        String str = this.f5981b;
        if (str != null) {
            httpURLConnectionO.setRequestProperty("User-Agent", str);
        }
        Map map = this.f5982c;
        if (map != null) {
            for (Map.Entry entry : map.entrySet()) {
                httpURLConnectionO.setRequestProperty((String) entry.getKey(), (String) entry.getValue());
            }
        }
        httpURLConnectionO.setConnectTimeout(this.f5980a);
        int responseCode = httpURLConnectionO.getResponseCode();
        if (m(responseCode)) {
            return httpURLConnectionO;
        }
        if (!l(responseCode)) {
            httpURLConnectionO.disconnect();
            throw new IOException(String.format("Image URL %s returned HTTP code %d", uri.toString(), Integer.valueOf(responseCode)));
        }
        String headerField = httpURLConnectionO.getHeaderField("Location");
        httpURLConnectionO.disconnect();
        Uri uri2 = headerField == null ? null : Uri.parse(headerField);
        String scheme = uri.getScheme();
        if (i3 <= 0 || uri2 == null || X.i.a(uri2.getScheme(), scheme)) {
            throw new IOException(i3 == 0 ? h("URL %s follows too many redirects", uri.toString()) : h("URL %s returned %d without a valid redirect", uri.toString(), Integer.valueOf(responseCode)));
        }
        return g(uri2, i3 - 1);
    }

    private static String h(String str, Object... objArr) {
        return String.format(Locale.getDefault(), str, objArr);
    }

    private static boolean l(int i3) {
        if (i3 == 307 || i3 == 308) {
            return true;
        }
        switch (i3) {
            case 300:
            case 301:
            case 302:
            case 303:
                return true;
            default:
                return false;
        }
    }

    private static boolean m(int i3) {
        return i3 >= 200 && i3 < 300;
    }

    static HttpURLConnection o(Uri uri) {
        return (HttpURLConnection) f0.f.q(uri).openConnection();
    }

    @Override // com.facebook.imagepipeline.producers.Y
    /* JADX INFO: renamed from: f, reason: merged with bridge method [inline-methods] */
    public c c(InterfaceC0354n interfaceC0354n, f0 f0Var) {
        return new c(interfaceC0354n, f0Var);
    }

    @Override // com.facebook.imagepipeline.producers.Y
    /* JADX INFO: renamed from: i, reason: merged with bridge method [inline-methods] */
    public void b(c cVar, Y.a aVar) {
        cVar.f5991f = this.f5984e.now();
        cVar.b().a0(new b(this.f5983d.submit(new a(cVar, aVar)), aVar));
    }

    /* JADX WARN: Removed duplicated region for block: B:29:0x0045  */
    /* JADX WARN: Removed duplicated region for block: B:38:0x0040 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:47:? A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    void j(com.facebook.imagepipeline.producers.E.c r5, com.facebook.imagepipeline.producers.Y.a r6) throws java.lang.Throwable {
        /*
            r4 = this;
            r0 = 0
            android.net.Uri r1 = r5.g()     // Catch: java.lang.Throwable -> L2d java.io.IOException -> L30
            r2 = 5
            java.net.HttpURLConnection r1 = r4.g(r1, r2)     // Catch: java.lang.Throwable -> L2d java.io.IOException -> L30
            e0.b r2 = r4.f5984e     // Catch: java.lang.Throwable -> L1e java.io.IOException -> L20
            long r2 = r2.now()     // Catch: java.lang.Throwable -> L1e java.io.IOException -> L20
            com.facebook.imagepipeline.producers.E.c.o(r5, r2)     // Catch: java.lang.Throwable -> L1e java.io.IOException -> L20
            if (r1 == 0) goto L22
            java.io.InputStream r0 = r1.getInputStream()     // Catch: java.lang.Throwable -> L1e java.io.IOException -> L20
            r5 = -1
            r6.c(r0, r5)     // Catch: java.lang.Throwable -> L1e java.io.IOException -> L20
            goto L22
        L1e:
            r5 = move-exception
            goto L3e
        L20:
            r5 = move-exception
            goto L32
        L22:
            if (r0 == 0) goto L27
            r0.close()     // Catch: java.io.IOException -> L27
        L27:
            if (r1 == 0) goto L3d
        L29:
            r1.disconnect()
            goto L3d
        L2d:
            r5 = move-exception
            r1 = r0
            goto L3e
        L30:
            r5 = move-exception
            r1 = r0
        L32:
            r6.a(r5)     // Catch: java.lang.Throwable -> L1e
            if (r0 == 0) goto L3a
            r0.close()     // Catch: java.io.IOException -> L3a
        L3a:
            if (r1 == 0) goto L3d
            goto L29
        L3d:
            return
        L3e:
            if (r0 == 0) goto L43
            r0.close()     // Catch: java.io.IOException -> L43
        L43:
            if (r1 == 0) goto L48
            r1.disconnect()
        L48:
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.facebook.imagepipeline.producers.E.j(com.facebook.imagepipeline.producers.E$c, com.facebook.imagepipeline.producers.Y$a):void");
    }

    @Override // com.facebook.imagepipeline.producers.AbstractC0344d, com.facebook.imagepipeline.producers.Y
    /* JADX INFO: renamed from: k, reason: merged with bridge method [inline-methods] */
    public Map e(c cVar, int i3) {
        HashMap map = new HashMap(4);
        map.put("queue_time", Long.toString(cVar.f5992g - cVar.f5991f));
        map.put("fetch_time", Long.toString(cVar.f5993h - cVar.f5992g));
        map.put("total_time", Long.toString(cVar.f5993h - cVar.f5991f));
        map.put("image_size", Integer.toString(i3));
        return map;
    }

    @Override // com.facebook.imagepipeline.producers.AbstractC0344d, com.facebook.imagepipeline.producers.Y
    /* JADX INFO: renamed from: n, reason: merged with bridge method [inline-methods] */
    public void a(c cVar, int i3) {
        cVar.f5993h = this.f5984e.now();
    }

    public E(int i3) {
        this((String) null, (Map) null, RealtimeSinceBootClock.get());
        this.f5980a = i3;
    }

    public E(String str, int i3) {
        this(str, (Map) null, RealtimeSinceBootClock.get());
        this.f5980a = i3;
    }

    public E(String str, Map<String, String> map, int i3) {
        this(str, map, RealtimeSinceBootClock.get());
        this.f5980a = i3;
    }

    E(String str, Map map, InterfaceC0523b interfaceC0523b) {
        this.f5983d = Executors.newFixedThreadPool(3);
        this.f5984e = interfaceC0523b;
        this.f5982c = map;
        this.f5981b = str;
    }
}
