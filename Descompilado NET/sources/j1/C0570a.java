package j1;

import D2.h;
import K2.d;
import M2.B;
import M2.D;
import M2.E;
import M2.InterfaceC0194e;
import M2.InterfaceC0195f;
import M2.t;
import M2.z;
import com.facebook.react.devsupport.inspector.InspectorNetworkRequestListener;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import r2.r;

/* JADX INFO: renamed from: j1.a, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public final class C0570a {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static final C0570a f9547a = new C0570a();

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private static z f9548b;

    /* JADX INFO: renamed from: j1.a$a, reason: collision with other inner class name */
    public static final class C0133a implements InterfaceC0195f {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        final /* synthetic */ InspectorNetworkRequestListener f9549a;

        C0133a(InspectorNetworkRequestListener inspectorNetworkRequestListener) {
            this.f9549a = inspectorNetworkRequestListener;
        }

        @Override // M2.InterfaceC0195f
        public void a(InterfaceC0194e interfaceC0194e, IOException iOException) {
            h.f(interfaceC0194e, "call");
            h.f(iOException, "e");
            if (interfaceC0194e.q()) {
                return;
            }
            this.f9549a.onError(iOException.getMessage());
        }

        @Override // M2.InterfaceC0195f
        public void b(InterfaceC0194e interfaceC0194e, D d4) {
            InputStream inputStreamA;
            byte[] bArr;
            h.f(interfaceC0194e, "call");
            h.f(d4, "response");
            t tVarO = d4.o();
            HashMap map = new HashMap();
            for (String str : tVarO.c()) {
                map.put(str, tVarO.a(str));
            }
            this.f9549a.onHeaders(d4.i(), map);
            try {
                E eA = d4.a();
                InspectorNetworkRequestListener inspectorNetworkRequestListener = this.f9549a;
                if (eA != null) {
                    try {
                        inputStreamA = eA.a();
                        bArr = new byte[1024];
                    } finally {
                    }
                    while (true) {
                        try {
                            int i3 = inputStreamA.read(bArr);
                            if (i3 == -1) {
                                break;
                            } else {
                                inspectorNetworkRequestListener.onData(new String(bArr, 0, i3, d.f816b));
                            }
                        } finally {
                        }
                    }
                    r rVar = r.f10584a;
                    A2.a.a(inputStreamA, null);
                }
                inspectorNetworkRequestListener.onCompletion();
                r rVar2 = r.f10584a;
                A2.a.a(eA, null);
            } catch (IOException e4) {
                this.f9549a.onError(e4.getMessage());
            }
        }
    }

    private C0570a() {
    }

    public static final void a(String str, InspectorNetworkRequestListener inspectorNetworkRequestListener) {
        h.f(str, "url");
        h.f(inspectorNetworkRequestListener, "listener");
        if (f9548b == null) {
            z.a aVar = new z.a();
            TimeUnit timeUnit = TimeUnit.SECONDS;
            f9548b = aVar.e(10L, timeUnit).N(10L, timeUnit).M(0L, TimeUnit.MINUTES).b();
        }
        try {
            B b4 = new B.a().m(str).b();
            z zVar = f9548b;
            if (zVar == null) {
                h.s("client");
                zVar = null;
            }
            zVar.b(b4).o(new C0133a(inspectorNetworkRequestListener));
        } catch (IllegalArgumentException unused) {
            inspectorNetworkRequestListener.onError("Not a valid URL: " + str);
        }
    }
}
