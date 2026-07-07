package com.facebook.react.modules.network;

import M2.C;
import M2.D;
import M2.E;
import M2.InterfaceC0194e;
import M2.InterfaceC0195f;
import M2.t;
import M2.v;
import M2.w;
import M2.x;
import M2.y;
import M2.z;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import b3.q;
import b3.t;
import com.facebook.fbreact.specs.NativeNetworkingAndroidSpec;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;
import i1.C0560a;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import v1.InterfaceC0756a;

/* JADX INFO: loaded from: classes.dex */
@InterfaceC0756a(name = "Networking")
public final class NetworkingModule extends NativeNetworkingAndroidSpec {
    private static final int CHUNK_TIMEOUT_NS = 100000000;
    private static final String CONTENT_ENCODING_HEADER_NAME = "content-encoding";
    private static final String CONTENT_TYPE_HEADER_NAME = "content-type";
    private static final int MAX_CHUNK_SIZE_BETWEEN_FLUSHES = 8192;
    private static final String REQUEST_BODY_KEY_BASE64 = "base64";
    private static final String REQUEST_BODY_KEY_FORMDATA = "formData";
    private static final String REQUEST_BODY_KEY_STRING = "string";
    private static final String REQUEST_BODY_KEY_URI = "uri";
    private static final String TAG = "Networking";
    private static final String USER_AGENT_HEADER_NAME = "user-agent";
    private static com.facebook.react.modules.network.b customClientBuilder;
    private final z mClient;
    private final com.facebook.react.modules.network.d mCookieHandler;
    private final com.facebook.react.modules.network.a mCookieJarContainer;
    private final String mDefaultUserAgent;
    private final List<d> mRequestBodyHandlers;
    private final Set<Integer> mRequestIds;
    private final List<e> mResponseHandlers;
    private boolean mShuttingDown;
    private final List<f> mUriHandlers;

    class a implements i {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        long f6989a = System.nanoTime();

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        final /* synthetic */ String f6990b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        final /* synthetic */ ReactApplicationContext f6991c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        final /* synthetic */ int f6992d;

        a(String str, ReactApplicationContext reactApplicationContext, int i3) {
            this.f6990b = str;
            this.f6991c = reactApplicationContext;
            this.f6992d = i3;
        }

        @Override // com.facebook.react.modules.network.i
        public void a(long j3, long j4, boolean z3) {
            long jNanoTime = System.nanoTime();
            if ((z3 || NetworkingModule.shouldDispatch(jNanoTime, this.f6989a)) && !this.f6990b.equals("text")) {
                o.c(this.f6991c, this.f6992d, j3, j4);
                this.f6989a = jNanoTime;
            }
        }
    }

    class b implements InterfaceC0195f {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        final /* synthetic */ int f6994a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        final /* synthetic */ ReactApplicationContext f6995b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        final /* synthetic */ String f6996c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        final /* synthetic */ boolean f6997d;

        b(int i3, ReactApplicationContext reactApplicationContext, String str, boolean z3) {
            this.f6994a = i3;
            this.f6995b = reactApplicationContext;
            this.f6996c = str;
            this.f6997d = z3;
        }

        @Override // M2.InterfaceC0195f
        public void a(InterfaceC0194e interfaceC0194e, IOException iOException) {
            String message;
            if (NetworkingModule.this.mShuttingDown) {
                return;
            }
            NetworkingModule.this.removeRequest(this.f6994a);
            if (iOException.getMessage() != null) {
                message = iOException.getMessage();
            } else {
                message = "Error while executing request: " + iOException.getClass().getSimpleName();
            }
            o.f(this.f6995b, this.f6994a, message, iOException);
        }

        @Override // M2.InterfaceC0195f
        public void b(InterfaceC0194e interfaceC0194e, D d4) {
            if (NetworkingModule.this.mShuttingDown) {
                return;
            }
            NetworkingModule.this.removeRequest(this.f6994a);
            o.h(this.f6995b, this.f6994a, d4.A(), NetworkingModule.translateHeaders(d4.d0()), d4.y0().l().toString());
            try {
                E eQ = d4.q();
                if ("gzip".equalsIgnoreCase(d4.X("Content-Encoding")) && eQ != null) {
                    q qVar = new q(eQ.z());
                    String strX = d4.X("Content-Type");
                    eQ = E.y(strX != null ? x.f(strX) : null, -1L, t.d(qVar));
                }
                for (e eVar : NetworkingModule.this.mResponseHandlers) {
                    if (eVar.b(this.f6996c)) {
                        o.a(this.f6995b, this.f6994a, eVar.a(eQ));
                        o.g(this.f6995b, this.f6994a);
                        return;
                    }
                }
                if (this.f6997d && this.f6996c.equals("text")) {
                    NetworkingModule.this.readWithProgress(this.f6994a, eQ);
                    o.g(this.f6995b, this.f6994a);
                    return;
                }
                String strA = "";
                if (this.f6996c.equals("text")) {
                    try {
                        strA = eQ.A();
                    } catch (IOException e4) {
                        if (!d4.y0().h().equalsIgnoreCase("HEAD")) {
                            o.f(this.f6995b, this.f6994a, e4.getMessage(), e4);
                        }
                    }
                } else if (this.f6996c.equals(NetworkingModule.REQUEST_BODY_KEY_BASE64)) {
                    strA = Base64.encodeToString(eQ.i(), 2);
                }
                o.b(this.f6995b, this.f6994a, strA);
                o.g(this.f6995b, this.f6994a);
            } catch (IOException e5) {
                o.f(this.f6995b, this.f6994a, e5.getMessage(), e5);
            }
        }
    }

    class c implements i {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        long f6999a = System.nanoTime();

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        final /* synthetic */ ReactApplicationContext f7000b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        final /* synthetic */ int f7001c;

        c(ReactApplicationContext reactApplicationContext, int i3) {
            this.f7000b = reactApplicationContext;
            this.f7001c = i3;
        }

        @Override // com.facebook.react.modules.network.i
        public void a(long j3, long j4, boolean z3) {
            long jNanoTime = System.nanoTime();
            if (z3 || NetworkingModule.shouldDispatch(jNanoTime, this.f6999a)) {
                o.d(this.f7000b, this.f7001c, j3, j4);
                this.f6999a = jNanoTime;
            }
        }
    }

    public interface d {
        boolean a(ReadableMap readableMap);

        C b(ReadableMap readableMap, String str);
    }

    public interface e {
        WritableMap a(E e4);

        boolean b(String str);
    }

    public interface f {
        WritableMap a(Uri uri);

        boolean b(Uri uri, String str);
    }

    public NetworkingModule(ReactApplicationContext reactApplicationContext, String str, z zVar, List<Object> list) {
        super(reactApplicationContext);
        this.mCookieHandler = new com.facebook.react.modules.network.d();
        this.mRequestIds = new HashSet();
        this.mRequestBodyHandlers = new ArrayList();
        this.mUriHandlers = new ArrayList();
        this.mResponseHandlers = new ArrayList();
        this.mShuttingDown = false;
        if (list != null) {
            z.a aVarC = zVar.C();
            Iterator<Object> it = list.iterator();
            if (it.hasNext()) {
                androidx.activity.result.d.a(it.next());
                throw null;
            }
            zVar = aVarC.b();
        }
        this.mClient = zVar;
        if (zVar.r() instanceof com.facebook.react.modules.network.a) {
            this.mCookieJarContainer = (com.facebook.react.modules.network.a) zVar.r();
        } else {
            this.mCookieJarContainer = null;
        }
        this.mDefaultUserAgent = str;
    }

    private synchronized void addRequest(int i3) {
        this.mRequestIds.add(Integer.valueOf(i3));
    }

    private synchronized void cancelAllRequests() {
        try {
            Iterator<Integer> it = this.mRequestIds.iterator();
            while (it.hasNext()) {
                cancelRequest(it.next().intValue());
            }
            this.mRequestIds.clear();
        } catch (Throwable th) {
            throw th;
        }
    }

    private void cancelRequest(int i3) {
        C0560a.a(this.mClient, Integer.valueOf(i3));
    }

    private y.a constructMultipartBody(ReadableArray readableArray, String str, int i3) {
        x xVarF;
        y.a aVar = new y.a();
        aVar.d(x.f(str));
        ReactApplicationContext reactApplicationContextIfActiveOrWarn = getReactApplicationContextIfActiveOrWarn();
        int size = readableArray.size();
        for (int i4 = 0; i4 < size; i4++) {
            ReadableMap map = readableArray.getMap(i4);
            M2.t tVarExtractHeaders = extractHeaders(map.getArray("headers"), null);
            if (tVarExtractHeaders == null) {
                o.f(reactApplicationContextIfActiveOrWarn, i3, "Missing or invalid header format for FormData part.", null);
                return null;
            }
            String strA = tVarExtractHeaders.a(CONTENT_TYPE_HEADER_NAME);
            if (strA != null) {
                xVarF = x.f(strA);
                tVarExtractHeaders = tVarExtractHeaders.e().h(CONTENT_TYPE_HEADER_NAME).e();
            } else {
                xVarF = null;
            }
            if (map.hasKey(REQUEST_BODY_KEY_STRING)) {
                aVar.a(tVarExtractHeaders, C.d(xVarF, map.getString(REQUEST_BODY_KEY_STRING)));
            } else if (!map.hasKey(REQUEST_BODY_KEY_URI)) {
                o.f(reactApplicationContextIfActiveOrWarn, i3, "Unrecognized FormData part.", null);
            } else {
                if (xVarF == null) {
                    o.f(reactApplicationContextIfActiveOrWarn, i3, "Binary FormData part needs a content-type header.", null);
                    return null;
                }
                String string = map.getString(REQUEST_BODY_KEY_URI);
                InputStream inputStreamH = n.h(getReactApplicationContext(), string);
                if (inputStreamH == null) {
                    o.f(reactApplicationContextIfActiveOrWarn, i3, "Could not retrieve file for uri " + string, null);
                    return null;
                }
                aVar.a(tVarExtractHeaders, n.c(xVarF, inputStreamH));
            }
        }
        return aVar;
    }

    private M2.t extractHeaders(ReadableArray readableArray, ReadableMap readableMap) {
        String str;
        if (readableArray == null) {
            return null;
        }
        t.a aVar = new t.a();
        int size = readableArray.size();
        for (int i3 = 0; i3 < size; i3++) {
            ReadableArray array = readableArray.getArray(i3);
            if (array != null && array.size() == 2) {
                String strA = com.facebook.react.modules.network.e.a(array.getString(0));
                String string = array.getString(1);
                if (strA != null && string != null) {
                    aVar.d(strA, string);
                }
            }
            return null;
        }
        if (aVar.f(USER_AGENT_HEADER_NAME) == null && (str = this.mDefaultUserAgent) != null) {
            aVar.a(USER_AGENT_HEADER_NAME, str);
        }
        if (readableMap == null || !readableMap.hasKey(REQUEST_BODY_KEY_STRING)) {
            aVar.h(CONTENT_ENCODING_HEADER_NAME);
        }
        return aVar.e();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ D lambda$sendRequestInternal$0(String str, ReactApplicationContext reactApplicationContext, int i3, v.a aVar) {
        D dA = aVar.a(aVar.i());
        return dA.u0().b(new k(dA.q(), new a(str, reactApplicationContext, i3))).c();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void readWithProgress(int i3, E e4) throws IOException {
        long jD0;
        long jQ = -1;
        try {
            k kVar = (k) e4;
            jD0 = kVar.d0();
            try {
                jQ = kVar.q();
            } catch (ClassCastException unused) {
            }
        } catch (ClassCastException unused2) {
            jD0 = -1;
        }
        l lVar = new l(e4.v() == null ? StandardCharsets.UTF_8 : e4.v().c(StandardCharsets.UTF_8));
        InputStream inputStreamA = e4.a();
        try {
            byte[] bArr = new byte[MAX_CHUNK_SIZE_BETWEEN_FLUSHES];
            ReactApplicationContext reactApplicationContextIfActiveOrWarn = getReactApplicationContextIfActiveOrWarn();
            while (true) {
                int i4 = inputStreamA.read(bArr);
                if (i4 == -1) {
                    return;
                } else {
                    o.e(reactApplicationContextIfActiveOrWarn, i3, lVar.a(bArr, i4), jD0, jQ);
                }
            }
        } finally {
            inputStreamA.close();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void removeRequest(int i3) {
        this.mRequestIds.remove(Integer.valueOf(i3));
    }

    public static void setCustomClientBuilder(com.facebook.react.modules.network.b bVar) {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean shouldDispatch(long j3, long j4) {
        return j4 + 100000000 < j3;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static WritableMap translateHeaders(M2.t tVar) {
        Bundle bundle = new Bundle();
        for (int i3 = 0; i3 < tVar.size(); i3++) {
            String strB = tVar.b(i3);
            if (bundle.containsKey(strB)) {
                bundle.putString(strB, bundle.getString(strB) + ", " + tVar.h(i3));
            } else {
                bundle.putString(strB, tVar.h(i3));
            }
        }
        return Arguments.fromBundle(bundle);
    }

    private C wrapRequestBodyWithProgressEmitter(C c4, int i3) {
        if (c4 == null) {
            return null;
        }
        return n.e(c4, new c(getReactApplicationContextIfActiveOrWarn(), i3));
    }

    @Override // com.facebook.fbreact.specs.NativeNetworkingAndroidSpec
    public void abortRequest(double d4) {
        int i3 = (int) d4;
        cancelRequest(i3);
        removeRequest(i3);
    }

    @Override // com.facebook.fbreact.specs.NativeNetworkingAndroidSpec
    public void addListener(String str) {
    }

    public void addRequestBodyHandler(d dVar) {
        this.mRequestBodyHandlers.add(dVar);
    }

    public void addResponseHandler(e eVar) {
        this.mResponseHandlers.add(eVar);
    }

    public void addUriHandler(f fVar) {
        this.mUriHandlers.add(fVar);
    }

    @Override // com.facebook.fbreact.specs.NativeNetworkingAndroidSpec
    @ReactMethod
    public void clearCookies(Callback callback) {
        this.mCookieHandler.d(callback);
    }

    @Override // com.facebook.react.bridge.BaseJavaModule, com.facebook.react.bridge.NativeModule, com.facebook.react.turbomodule.core.interfaces.TurboModule
    public void initialize() {
        com.facebook.react.modules.network.a aVar = this.mCookieJarContainer;
        if (aVar != null) {
            aVar.d(new w(this.mCookieHandler));
        }
    }

    @Override // com.facebook.react.bridge.BaseJavaModule, com.facebook.react.bridge.NativeModule, com.facebook.react.turbomodule.core.interfaces.TurboModule
    public void invalidate() {
        this.mShuttingDown = true;
        cancelAllRequests();
        this.mCookieHandler.f();
        com.facebook.react.modules.network.a aVar = this.mCookieJarContainer;
        if (aVar != null) {
            aVar.b();
        }
        this.mRequestBodyHandlers.clear();
        this.mResponseHandlers.clear();
        this.mUriHandlers.clear();
    }

    @Override // com.facebook.fbreact.specs.NativeNetworkingAndroidSpec
    public void removeListeners(double d4) {
    }

    public void removeRequestBodyHandler(d dVar) {
        this.mRequestBodyHandlers.remove(dVar);
    }

    public void removeResponseHandler(e eVar) {
        this.mResponseHandlers.remove(eVar);
    }

    public void removeUriHandler(f fVar) {
        this.mUriHandlers.remove(fVar);
    }

    @Override // com.facebook.fbreact.specs.NativeNetworkingAndroidSpec
    public void sendRequest(String str, String str2, double d4, ReadableArray readableArray, ReadableMap readableMap, String str3, boolean z3, double d5, boolean z4) {
        int i3 = (int) d4;
        try {
            sendRequestInternal(str, str2, i3, readableArray, readableMap, str3, z3, (int) d5, z4);
        } catch (Throwable th) {
            Y.a.n("Networking", "Failed to send url request: " + str2, th);
            o.f(getReactApplicationContextIfActiveOrWarn(), i3, th.getMessage(), th);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:85:0x0183  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void sendRequestInternal(java.lang.String r7, java.lang.String r8, final int r9, com.facebook.react.bridge.ReadableArray r10, com.facebook.react.bridge.ReadableMap r11, final java.lang.String r12, boolean r13, int r14, boolean r15) {
        /*
            Method dump skipped, instruction units count: 440
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.facebook.react.modules.network.NetworkingModule.sendRequestInternal(java.lang.String, java.lang.String, int, com.facebook.react.bridge.ReadableArray, com.facebook.react.bridge.ReadableMap, java.lang.String, boolean, int, boolean):void");
    }

    NetworkingModule(ReactApplicationContext reactApplicationContext, String str, z zVar) {
        this(reactApplicationContext, str, zVar, null);
    }

    public NetworkingModule(ReactApplicationContext reactApplicationContext) {
        this(reactApplicationContext, null, g.b(reactApplicationContext), null);
    }

    public NetworkingModule(ReactApplicationContext reactApplicationContext, List<Object> list) {
        this(reactApplicationContext, null, g.b(reactApplicationContext), list);
    }

    public NetworkingModule(ReactApplicationContext reactApplicationContext, String str) {
        this(reactApplicationContext, str, g.b(reactApplicationContext), null);
    }

    private static void applyCustomBuilder(z.a aVar) {
    }
}
