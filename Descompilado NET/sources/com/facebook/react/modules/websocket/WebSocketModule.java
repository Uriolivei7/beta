package com.facebook.react.modules.websocket;

import D2.h;
import K2.o;
import M2.B;
import M2.D;
import M2.H;
import M2.I;
import M2.z;
import b3.l;
import com.facebook.fbreact.specs.NativeWebSocketModuleSpec;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableMapKeySetIterator;
import com.facebook.react.bridge.ReadableType;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.network.d;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import kotlin.jvm.internal.DefaultConstructorMarker;
import v1.InterfaceC0756a;

/* JADX INFO: loaded from: classes.dex */
@InterfaceC0756a(name = "WebSocketModule")
public final class WebSocketModule extends NativeWebSocketModuleSpec {
    public static final a Companion = new a(null);
    public static final String NAME = "WebSocketModule";
    private static com.facebook.react.modules.network.b customClientBuilder;
    private final Map<Integer, b> contentHandlers;
    private final d cookieHandler;
    private final Map<Integer, H> webSocketConnections;

    public static final class a {
        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public final void c(z.a aVar) {
            WebSocketModule.access$getCustomClientBuilder$cp();
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* JADX WARN: Removed duplicated region for block: B:27:0x004d  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public final java.lang.String d(java.lang.String r7) {
            /*
                r6 = this;
                java.net.URI r0 = new java.net.URI     // Catch: java.net.URISyntaxException -> L92
                r0.<init>(r7)     // Catch: java.net.URISyntaxException -> L92
                java.lang.String r1 = r0.getScheme()     // Catch: java.net.URISyntaxException -> L92
                if (r1 == 0) goto L4d
                int r2 = r1.hashCode()     // Catch: java.net.URISyntaxException -> L92
                r3 = 3804(0xedc, float:5.33E-42)
                java.lang.String r4 = "http"
                if (r2 == r3) goto L45
                r3 = 118039(0x1cd17, float:1.65408E-40)
                java.lang.String r5 = "https"
                if (r2 == r3) goto L3a
                r3 = 3213448(0x310888, float:4.503E-39)
                if (r2 == r3) goto L2e
                r3 = 99617003(0x5f008eb, float:2.2572767E-35)
                if (r2 == r3) goto L27
                goto L4d
            L27:
                boolean r1 = r1.equals(r5)     // Catch: java.net.URISyntaxException -> L92
                if (r1 != 0) goto L35
                goto L4d
            L2e:
                boolean r1 = r1.equals(r4)     // Catch: java.net.URISyntaxException -> L92
                if (r1 != 0) goto L35
                goto L4d
            L35:
                java.lang.String r4 = r0.getScheme()     // Catch: java.net.URISyntaxException -> L92
                goto L4f
            L3a:
                java.lang.String r2 = "wss"
                boolean r1 = r1.equals(r2)     // Catch: java.net.URISyntaxException -> L92
                if (r1 != 0) goto L43
                goto L4d
            L43:
                r4 = r5
                goto L4f
            L45:
                java.lang.String r2 = "ws"
                boolean r1 = r1.equals(r2)     // Catch: java.net.URISyntaxException -> L92
                if (r1 != 0) goto L4f
            L4d:
                java.lang.String r4 = ""
            L4f:
                int r1 = r0.getPort()     // Catch: java.net.URISyntaxException -> L92
                r2 = -1
                java.lang.String r3 = "format(...)"
                if (r1 == r2) goto L79
                D2.u r1 = D2.u.f192a     // Catch: java.net.URISyntaxException -> L92
                java.lang.String r1 = "%s://%s:%s"
                java.lang.String r2 = r0.getHost()     // Catch: java.net.URISyntaxException -> L92
                int r0 = r0.getPort()     // Catch: java.net.URISyntaxException -> L92
                java.lang.Integer r0 = java.lang.Integer.valueOf(r0)     // Catch: java.net.URISyntaxException -> L92
                java.lang.Object[] r0 = new java.lang.Object[]{r4, r2, r0}     // Catch: java.net.URISyntaxException -> L92
                r2 = 3
                java.lang.Object[] r0 = java.util.Arrays.copyOf(r0, r2)     // Catch: java.net.URISyntaxException -> L92
                java.lang.String r0 = java.lang.String.format(r1, r0)     // Catch: java.net.URISyntaxException -> L92
                D2.h.e(r0, r3)     // Catch: java.net.URISyntaxException -> L92
                goto L91
            L79:
                D2.u r1 = D2.u.f192a     // Catch: java.net.URISyntaxException -> L92
                java.lang.String r1 = "%s://%s"
                java.lang.String r0 = r0.getHost()     // Catch: java.net.URISyntaxException -> L92
                java.lang.Object[] r0 = new java.lang.Object[]{r4, r0}     // Catch: java.net.URISyntaxException -> L92
                r2 = 2
                java.lang.Object[] r0 = java.util.Arrays.copyOf(r0, r2)     // Catch: java.net.URISyntaxException -> L92
                java.lang.String r0 = java.lang.String.format(r1, r0)     // Catch: java.net.URISyntaxException -> L92
                D2.h.e(r0, r3)     // Catch: java.net.URISyntaxException -> L92
            L91:
                return r0
            L92:
                java.lang.IllegalArgumentException r0 = new java.lang.IllegalArgumentException
                java.lang.StringBuilder r1 = new java.lang.StringBuilder
                r1.<init>()
                java.lang.String r2 = "Unable to set "
                r1.append(r2)
                r1.append(r7)
                java.lang.String r7 = " as default origin header"
                r1.append(r7)
                java.lang.String r7 = r1.toString()
                r0.<init>(r7)
                throw r0
            */
            throw new UnsupportedOperationException("Method not decompiled: com.facebook.react.modules.websocket.WebSocketModule.a.d(java.lang.String):java.lang.String");
        }

        public final void e(com.facebook.react.modules.network.b bVar) {
            WebSocketModule.access$setCustomClientBuilder$cp(bVar);
        }

        private a() {
        }
    }

    public interface b {
        void a(l lVar, WritableMap writableMap);

        void b(String str, WritableMap writableMap);
    }

    public static final class c extends I {

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        final /* synthetic */ int f7052b;

        c(int i3) {
            this.f7052b = i3;
        }

        @Override // M2.I
        public void a(H h3, int i3, String str) {
            h.f(h3, "webSocket");
            h.f(str, "reason");
            WritableMap writableMapCreateMap = Arguments.createMap();
            writableMapCreateMap.putInt("id", this.f7052b);
            writableMapCreateMap.putInt("code", i3);
            writableMapCreateMap.putString("reason", str);
            WebSocketModule webSocketModule = WebSocketModule.this;
            h.c(writableMapCreateMap);
            webSocketModule.sendEvent("websocketClosed", writableMapCreateMap);
        }

        @Override // M2.I
        public void b(H h3, int i3, String str) {
            h.f(h3, "websocket");
            h.f(str, "reason");
            h3.a(i3, str);
        }

        @Override // M2.I
        public void c(H h3, Throwable th, D d4) {
            h.f(h3, "webSocket");
            h.f(th, "t");
            WebSocketModule.this.notifyWebSocketFailed(this.f7052b, th.getMessage());
        }

        @Override // M2.I
        public void d(H h3, l lVar) {
            h.f(h3, "webSocket");
            h.f(lVar, "bytes");
            WritableMap writableMapCreateMap = Arguments.createMap();
            writableMapCreateMap.putInt("id", this.f7052b);
            writableMapCreateMap.putString("type", "binary");
            b bVar = (b) WebSocketModule.this.contentHandlers.get(Integer.valueOf(this.f7052b));
            if (bVar != null) {
                h.c(writableMapCreateMap);
                bVar.a(lVar, writableMapCreateMap);
            } else {
                writableMapCreateMap.putString("data", lVar.a());
            }
            WebSocketModule webSocketModule = WebSocketModule.this;
            h.c(writableMapCreateMap);
            webSocketModule.sendEvent("websocketMessage", writableMapCreateMap);
        }

        @Override // M2.I
        public void e(H h3, String str) {
            h.f(h3, "webSocket");
            h.f(str, "text");
            WritableMap writableMapCreateMap = Arguments.createMap();
            writableMapCreateMap.putInt("id", this.f7052b);
            writableMapCreateMap.putString("type", "text");
            b bVar = (b) WebSocketModule.this.contentHandlers.get(Integer.valueOf(this.f7052b));
            if (bVar != null) {
                h.c(writableMapCreateMap);
                bVar.b(str, writableMapCreateMap);
            } else {
                writableMapCreateMap.putString("data", str);
            }
            WebSocketModule webSocketModule = WebSocketModule.this;
            h.c(writableMapCreateMap);
            webSocketModule.sendEvent("websocketMessage", writableMapCreateMap);
        }

        @Override // M2.I
        public void f(H h3, D d4) {
            h.f(h3, "webSocket");
            h.f(d4, "response");
            WebSocketModule.this.webSocketConnections.put(Integer.valueOf(this.f7052b), h3);
            WritableMap writableMapCreateMap = Arguments.createMap();
            writableMapCreateMap.putInt("id", this.f7052b);
            writableMapCreateMap.putString("protocol", d4.a0("Sec-WebSocket-Protocol", ""));
            WebSocketModule webSocketModule = WebSocketModule.this;
            h.c(writableMapCreateMap);
            webSocketModule.sendEvent("websocketOpen", writableMapCreateMap);
        }
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public WebSocketModule(ReactApplicationContext reactApplicationContext) {
        super(reactApplicationContext);
        h.f(reactApplicationContext, "context");
        this.webSocketConnections = new ConcurrentHashMap();
        this.contentHandlers = new ConcurrentHashMap();
        this.cookieHandler = new d();
    }

    public static final /* synthetic */ com.facebook.react.modules.network.b access$getCustomClientBuilder$cp() {
        return null;
    }

    public static final /* synthetic */ void access$setCustomClientBuilder$cp(com.facebook.react.modules.network.b bVar) {
    }

    private final String getCookie(String str) {
        try {
            List list = (List) this.cookieHandler.get(new URI(Companion.d(str)), new HashMap()).get("Cookie");
            if (list != null && !list.isEmpty()) {
                return (String) list.get(0);
            }
            return null;
        } catch (IOException unused) {
            throw new IllegalArgumentException("Unable to get cookie from " + str);
        } catch (URISyntaxException unused2) {
            throw new IllegalArgumentException("Unable to get cookie from " + str);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void notifyWebSocketFailed(int i3, String str) {
        WritableMap writableMapCreateMap = Arguments.createMap();
        writableMapCreateMap.putInt("id", i3);
        writableMapCreateMap.putString("message", str);
        h.c(writableMapCreateMap);
        sendEvent("websocketFailed", writableMapCreateMap);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void sendEvent(String str, WritableMap writableMap) {
        ReactApplicationContext reactApplicationContext = getReactApplicationContext();
        if (reactApplicationContext.hasActiveReactInstance()) {
            reactApplicationContext.emitDeviceEvent(str, writableMap);
        }
    }

    public static final void setCustomClientBuilder(com.facebook.react.modules.network.b bVar) {
        Companion.e(bVar);
    }

    @Override // com.facebook.fbreact.specs.NativeWebSocketModuleSpec
    public void addListener(String str) {
        h.f(str, "eventName");
    }

    @Override // com.facebook.fbreact.specs.NativeWebSocketModuleSpec
    public void close(double d4, String str, double d5) {
        int i3 = (int) d5;
        H h3 = this.webSocketConnections.get(Integer.valueOf(i3));
        if (h3 == null) {
            return;
        }
        try {
            h3.a((int) d4, str);
            this.webSocketConnections.remove(Integer.valueOf(i3));
            this.contentHandlers.remove(Integer.valueOf(i3));
        } catch (Exception e4) {
            Y.a.n("ReactNative", "Could not close WebSocket connection for id " + i3, e4);
        }
    }

    @Override // com.facebook.fbreact.specs.NativeWebSocketModuleSpec
    public void connect(String str, ReadableArray readableArray, ReadableMap readableMap, double d4) {
        boolean z3;
        h.f(str, "url");
        int i3 = (int) d4;
        z.a aVar = new z.a();
        TimeUnit timeUnit = TimeUnit.SECONDS;
        z.a aVarM = aVar.e(10L, timeUnit).N(10L, timeUnit).M(0L, TimeUnit.MINUTES);
        Companion.c(aVarM);
        z zVarB = aVarM.b();
        B.a aVarM2 = new B.a().k(Integer.valueOf(i3)).m(str);
        String cookie = getCookie(str);
        if (cookie != null) {
            aVarM2.a("Cookie", cookie);
        }
        if (readableMap != null && readableMap.hasKey("headers") && readableMap.getType("headers") == ReadableType.Map) {
            ReadableMap map = readableMap.getMap("headers");
            if (map == null) {
                throw new IllegalStateException("Required value was null.");
            }
            ReadableMapKeySetIterator readableMapKeySetIteratorKeySetIterator = map.keySetIterator();
            z3 = false;
            while (readableMapKeySetIteratorKeySetIterator.hasNextKey()) {
                String strNextKey = readableMapKeySetIteratorKeySetIterator.nextKey();
                if (ReadableType.String == map.getType(strNextKey)) {
                    if (o.n(strNextKey, "origin", true)) {
                        z3 = true;
                    }
                    String string = map.getString(strNextKey);
                    if (string == null) {
                        throw new IllegalStateException(("value for name " + strNextKey + " == null").toString());
                    }
                    aVarM2.a(strNextKey, string);
                } else {
                    Y.a.I("ReactNative", "Ignoring: requested " + strNextKey + ", value not a string");
                }
            }
        } else {
            z3 = false;
        }
        if (!z3) {
            aVarM2.a("origin", Companion.d(str));
        }
        if (readableArray != null && readableArray.size() > 0) {
            StringBuilder sb = new StringBuilder("");
            int size = readableArray.size();
            for (int i4 = 0; i4 < size; i4++) {
                String string2 = readableArray.getString(i4);
                String string3 = string2 != null ? o.w0(string2).toString() : null;
                if (!(string3 == null || string3.length() == 0) && !o.E(string3, ",", false, 2, null)) {
                    sb.append(string3);
                    sb.append(",");
                }
            }
            if (sb.length() > 0) {
                sb.replace(sb.length() - 1, sb.length(), "");
                String string4 = sb.toString();
                h.e(string4, "toString(...)");
                aVarM2.a("Sec-WebSocket-Protocol", string4);
            }
        }
        zVarB.D(aVarM2.b(), new c(i3));
        zVarB.c().a().shutdown();
    }

    @Override // com.facebook.react.bridge.BaseJavaModule, com.facebook.react.bridge.NativeModule, com.facebook.react.turbomodule.core.interfaces.TurboModule
    public void invalidate() {
        Iterator<H> it = this.webSocketConnections.values().iterator();
        while (it.hasNext()) {
            it.next().a(1001, null);
        }
        this.webSocketConnections.clear();
        this.contentHandlers.clear();
    }

    @Override // com.facebook.fbreact.specs.NativeWebSocketModuleSpec
    public void ping(double d4) {
        int i3 = (int) d4;
        H h3 = this.webSocketConnections.get(Integer.valueOf(i3));
        if (h3 != null) {
            try {
                h3.f(l.f5638e);
                return;
            } catch (Exception e4) {
                notifyWebSocketFailed(i3, e4.getMessage());
                return;
            }
        }
        WritableMap writableMapCreateMap = Arguments.createMap();
        h.e(writableMapCreateMap, "createMap(...)");
        writableMapCreateMap.putInt("id", i3);
        writableMapCreateMap.putString("message", "client is null");
        sendEvent("websocketFailed", writableMapCreateMap);
        WritableMap writableMapCreateMap2 = Arguments.createMap();
        writableMapCreateMap2.putInt("id", i3);
        writableMapCreateMap2.putInt("code", 0);
        writableMapCreateMap2.putString("reason", "client is null");
        sendEvent("websocketClosed", writableMapCreateMap2);
        this.webSocketConnections.remove(Integer.valueOf(i3));
        this.contentHandlers.remove(Integer.valueOf(i3));
    }

    @Override // com.facebook.fbreact.specs.NativeWebSocketModuleSpec
    public void removeListeners(double d4) {
    }

    @Override // com.facebook.fbreact.specs.NativeWebSocketModuleSpec
    public void send(String str, double d4) {
        h.f(str, "message");
        int i3 = (int) d4;
        H h3 = this.webSocketConnections.get(Integer.valueOf(i3));
        if (h3 != null) {
            try {
                h3.b(str);
                return;
            } catch (Exception e4) {
                notifyWebSocketFailed(i3, e4.getMessage());
                return;
            }
        }
        WritableMap writableMapCreateMap = Arguments.createMap();
        h.e(writableMapCreateMap, "createMap(...)");
        writableMapCreateMap.putInt("id", i3);
        writableMapCreateMap.putString("message", "client is null");
        sendEvent("websocketFailed", writableMapCreateMap);
        WritableMap writableMapCreateMap2 = Arguments.createMap();
        writableMapCreateMap2.putInt("id", i3);
        writableMapCreateMap2.putInt("code", 0);
        writableMapCreateMap2.putString("reason", "client is null");
        sendEvent("websocketClosed", writableMapCreateMap2);
        this.webSocketConnections.remove(Integer.valueOf(i3));
        this.contentHandlers.remove(Integer.valueOf(i3));
    }

    @Override // com.facebook.fbreact.specs.NativeWebSocketModuleSpec
    public void sendBinary(String str, double d4) {
        h.f(str, "base64String");
        int i3 = (int) d4;
        H h3 = this.webSocketConnections.get(Integer.valueOf(i3));
        if (h3 != null) {
            try {
                l lVarA = l.f5639f.a(str);
                if (lVarA == null) {
                    throw new IllegalStateException("bytes == null");
                }
                h3.f(lVarA);
                return;
            } catch (Exception e4) {
                notifyWebSocketFailed(i3, e4.getMessage());
                return;
            }
        }
        WritableMap writableMapCreateMap = Arguments.createMap();
        h.e(writableMapCreateMap, "createMap(...)");
        writableMapCreateMap.putInt("id", i3);
        writableMapCreateMap.putString("message", "client is null");
        sendEvent("websocketFailed", writableMapCreateMap);
        WritableMap writableMapCreateMap2 = Arguments.createMap();
        writableMapCreateMap2.putInt("id", i3);
        writableMapCreateMap2.putInt("code", 0);
        writableMapCreateMap2.putString("reason", "client is null");
        sendEvent("websocketClosed", writableMapCreateMap2);
        this.webSocketConnections.remove(Integer.valueOf(i3));
        this.contentHandlers.remove(Integer.valueOf(i3));
    }

    public final void setContentHandler(int i3, b bVar) {
        if (bVar == null) {
            this.contentHandlers.remove(Integer.valueOf(i3));
        } else {
            this.contentHandlers.put(Integer.valueOf(i3), bVar);
        }
    }

    public final void sendBinary(l lVar, int i3) {
        h.f(lVar, "byteString");
        H h3 = this.webSocketConnections.get(Integer.valueOf(i3));
        if (h3 == null) {
            WritableMap writableMapCreateMap = Arguments.createMap();
            h.e(writableMapCreateMap, "createMap(...)");
            writableMapCreateMap.putInt("id", i3);
            writableMapCreateMap.putString("message", "client is null");
            sendEvent("websocketFailed", writableMapCreateMap);
            WritableMap writableMapCreateMap2 = Arguments.createMap();
            writableMapCreateMap2.putInt("id", i3);
            writableMapCreateMap2.putInt("code", 0);
            writableMapCreateMap2.putString("reason", "client is null");
            sendEvent("websocketClosed", writableMapCreateMap2);
            this.webSocketConnections.remove(Integer.valueOf(i3));
            this.contentHandlers.remove(Integer.valueOf(i3));
            return;
        }
        try {
            h3.f(lVar);
        } catch (Exception e4) {
            notifyWebSocketFailed(i3, e4.getMessage());
        }
    }
}
