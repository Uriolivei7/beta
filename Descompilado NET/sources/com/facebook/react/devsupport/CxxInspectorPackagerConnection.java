package com.facebook.react.devsupport;

import M2.B;
import M2.z;
import android.os.Handler;
import android.os.Looper;
import com.facebook.jni.HybridData;
import java.io.Closeable;
import java.util.concurrent.TimeUnit;

/* JADX INFO: loaded from: classes.dex */
class CxxInspectorPackagerConnection implements M {
    private final HybridData mHybridData;

    private static class DelegateImpl {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        private final M2.z f6579a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        private final Handler f6580b;

        class a extends M2.I {

            /* JADX INFO: renamed from: a, reason: collision with root package name */
            final /* synthetic */ WebSocketDelegate f6581a;

            /* JADX INFO: renamed from: com.facebook.react.devsupport.CxxInspectorPackagerConnection$DelegateImpl$a$a, reason: collision with other inner class name */
            class RunnableC0100a implements Runnable {

                /* JADX INFO: renamed from: b, reason: collision with root package name */
                final /* synthetic */ Throwable f6583b;

                RunnableC0100a(Throwable th) {
                    this.f6583b = th;
                }

                @Override // java.lang.Runnable
                public void run() {
                    String message = this.f6583b.getMessage();
                    WebSocketDelegate webSocketDelegate = a.this.f6581a;
                    if (message == null) {
                        message = "<Unknown error>";
                    }
                    webSocketDelegate.didFailWithError(null, message);
                    a.this.f6581a.close();
                }
            }

            class b implements Runnable {

                /* JADX INFO: renamed from: b, reason: collision with root package name */
                final /* synthetic */ String f6585b;

                b(String str) {
                    this.f6585b = str;
                }

                @Override // java.lang.Runnable
                public void run() {
                    a.this.f6581a.didReceiveMessage(this.f6585b);
                }
            }

            class c implements Runnable {
                c() {
                }

                @Override // java.lang.Runnable
                public void run() {
                    a.this.f6581a.didOpen();
                }
            }

            class d implements Runnable {
                d() {
                }

                @Override // java.lang.Runnable
                public void run() {
                    a.this.f6581a.didClose();
                    a.this.f6581a.close();
                }
            }

            a(WebSocketDelegate webSocketDelegate) {
                this.f6581a = webSocketDelegate;
            }

            @Override // M2.I
            public void a(M2.H h3, int i3, String str) {
                DelegateImpl.this.scheduleCallback(new d(), 0L);
            }

            @Override // M2.I
            public void c(M2.H h3, Throwable th, M2.D d4) {
                DelegateImpl.this.scheduleCallback(new RunnableC0100a(th), 0L);
            }

            @Override // M2.I
            public void e(M2.H h3, String str) {
                DelegateImpl.this.scheduleCallback(new b(str), 0L);
            }

            @Override // M2.I
            public void f(M2.H h3, M2.D d4) {
                DelegateImpl.this.scheduleCallback(new c(), 0L);
            }
        }

        class b implements a {

            /* JADX INFO: renamed from: b, reason: collision with root package name */
            final /* synthetic */ M2.H f6589b;

            b(M2.H h3) {
                this.f6589b = h3;
            }

            @Override // java.io.Closeable, java.lang.AutoCloseable
            public void close() {
                this.f6589b.a(1000, "End of session");
            }
        }

        public a connectWebSocket(String str, WebSocketDelegate webSocketDelegate) {
            return new b(this.f6579a.D(new B.a().m(str).b(), new a(webSocketDelegate)));
        }

        public void scheduleCallback(Runnable runnable, long j3) {
            this.f6580b.postDelayed(runnable, j3);
        }

        private DelegateImpl() {
            z.a aVar = new z.a();
            TimeUnit timeUnit = TimeUnit.SECONDS;
            this.f6579a = aVar.e(10L, timeUnit).N(10L, timeUnit).M(0L, TimeUnit.MINUTES).b();
            this.f6580b = new Handler(Looper.getMainLooper());
        }
    }

    private static class WebSocketDelegate implements Closeable {

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        private final HybridData f6591b;

        private WebSocketDelegate(HybridData hybridData) {
            this.f6591b = hybridData;
        }

        @Override // java.io.Closeable, java.lang.AutoCloseable
        public void close() {
            this.f6591b.resetNative();
        }

        public native void didClose();

        public native void didFailWithError(Integer num, String str);

        public native void didOpen();

        public native void didReceiveMessage(String str);
    }

    private interface a extends Closeable {
    }

    static {
        I.a();
    }

    public CxxInspectorPackagerConnection(String str, String str2, String str3) {
        this.mHybridData = initHybrid(str, str2, str3, new DelegateImpl());
    }

    private static native HybridData initHybrid(String str, String str2, String str3, DelegateImpl delegateImpl);

    @Override // com.facebook.react.devsupport.M
    public native void closeQuietly();

    @Override // com.facebook.react.devsupport.M
    public native void connect();

    @Override // com.facebook.react.devsupport.M
    public native void sendEventToAllConnections(String str);
}
