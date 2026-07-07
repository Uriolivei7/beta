package com.facebook.react.devsupport;

import M2.B;
import M2.InterfaceC0194e;
import M2.InterfaceC0195f;
import M2.z;
import java.io.IOException;
import java.util.Arrays;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import kotlin.jvm.internal.DefaultConstructorMarker;

/* JADX INFO: loaded from: classes.dex */
public final class W {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private static final a f6663b = new a(null);

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final M2.z f6664a;

    private static final class a {
        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public final String b(String str) {
            D2.u uVar = D2.u.f192a;
            String str2 = String.format(Locale.US, "http://%s/status", Arrays.copyOf(new Object[]{str}, 1));
            D2.h.e(str2, "format(...)");
            return str2;
        }

        private a() {
        }
    }

    public static final class b implements InterfaceC0195f {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        final /* synthetic */ k1.g f6665a;

        b(k1.g gVar) {
            this.f6665a = gVar;
        }

        @Override // M2.InterfaceC0195f
        public void a(InterfaceC0194e interfaceC0194e, IOException iOException) {
            D2.h.f(interfaceC0194e, "call");
            D2.h.f(iOException, "e");
            Y.a.I("ReactNative", "The packager does not seem to be running as we got an IOException requesting its status: " + iOException.getMessage());
            this.f6665a.a(false);
        }

        @Override // M2.InterfaceC0195f
        public void b(InterfaceC0194e interfaceC0194e, M2.D d4) throws IOException {
            D2.h.f(interfaceC0194e, "call");
            D2.h.f(d4, "response");
            if (!d4.e0()) {
                Y.a.m("ReactNative", "Got non-success http code from packager when requesting status: " + d4.i());
                this.f6665a.a(false);
                return;
            }
            M2.E eA = d4.a();
            if (eA == null) {
                Y.a.m("ReactNative", "Got null body response from packager when requesting status");
                this.f6665a.a(false);
                return;
            }
            String strA = eA.A();
            if (D2.h.b("packager-status:running", strA)) {
                this.f6665a.a(true);
                return;
            }
            Y.a.m("ReactNative", "Got unexpected response from packager when requesting status: " + strA);
            this.f6665a.a(false);
        }
    }

    public W() {
        z.a aVar = new z.a();
        TimeUnit timeUnit = TimeUnit.MILLISECONDS;
        this.f6664a = aVar.e(5000L, timeUnit).M(0L, timeUnit).N(0L, timeUnit).b();
    }

    public final void a(String str, k1.g gVar) {
        D2.h.f(str, "host");
        D2.h.f(gVar, "callback");
        this.f6664a.b(new B.a().m(f6663b.b(str)).b()).o(new b(gVar));
    }

    public W(M2.z zVar) {
        D2.h.f(zVar, "client");
        this.f6664a = zVar;
    }
}
