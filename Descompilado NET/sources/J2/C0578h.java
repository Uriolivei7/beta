package j2;

import android.net.ConnectivityManager;
import android.net.LinkProperties;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Handler;
import android.os.Looper;
import com.facebook.react.bridge.ReactApplicationContext;

/* JADX INFO: renamed from: j2.h, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public class C0578h extends AbstractC0574d {

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    private final a f9560j;

    /* JADX INFO: renamed from: k, reason: collision with root package name */
    private Network f9561k;

    /* JADX INFO: renamed from: l, reason: collision with root package name */
    private NetworkCapabilities f9562l;

    /* JADX INFO: renamed from: j2.h$a */
    private class a extends ConnectivityManager.NetworkCallback {
        @Override // android.net.ConnectivityManager.NetworkCallback
        public void onAvailable(Network network) {
            C0578h.this.f9561k = network;
            C0578h.this.q(250);
        }

        @Override // android.net.ConnectivityManager.NetworkCallback
        public void onCapabilitiesChanged(Network network, NetworkCapabilities networkCapabilities) {
            C0578h.this.f9561k = network;
            C0578h.this.f9562l = networkCapabilities;
            C0578h.this.s();
        }

        @Override // android.net.ConnectivityManager.NetworkCallback
        public void onLinkPropertiesChanged(Network network, LinkProperties linkProperties) {
            if (C0578h.this.f9561k != null) {
                C0578h.this.f9561k = network;
            }
            C0578h.this.q(250);
        }

        @Override // android.net.ConnectivityManager.NetworkCallback
        public void onLosing(Network network, int i3) {
            C0578h.this.f9561k = network;
            C0578h.this.s();
        }

        @Override // android.net.ConnectivityManager.NetworkCallback
        public void onLost(Network network) {
            C0578h.this.f9561k = null;
            C0578h.this.f9562l = null;
            C0578h.this.s();
        }

        @Override // android.net.ConnectivityManager.NetworkCallback
        public void onUnavailable() {
            C0578h.this.f9561k = null;
            C0578h.this.f9562l = null;
            C0578h.this.s();
        }

        private a() {
        }
    }

    public C0578h(ReactApplicationContext reactApplicationContext) {
        super(reactApplicationContext);
        this.f9561k = null;
        this.f9562l = null;
        this.f9560j = new a();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void q(int i3) {
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() { // from class: j2.g
            @Override // java.lang.Runnable
            public final void run() {
                this.f9559b.r();
            }
        }, i3);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void r() {
        try {
            this.f9562l = c().getNetworkCapabilities(this.f9561k);
            s();
        } catch (SecurityException unused) {
        }
    }

    @Override // j2.AbstractC0574d
    public void g() {
        try {
            this.f9561k = c().getActiveNetwork();
            q(0);
            c().registerDefaultNetworkCallback(this.f9560j);
        } catch (SecurityException unused) {
        }
    }

    @Override // j2.AbstractC0574d
    public void j() {
        try {
            c().unregisterNetworkCallback(this.f9560j);
        } catch (IllegalArgumentException | SecurityException unused) {
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:25:0x004c  */
    /* JADX WARN: Removed duplicated region for block: B:26:0x0054  */
    /* JADX WARN: Removed duplicated region for block: B:38:0x007b  */
    /* JADX WARN: Removed duplicated region for block: B:41:0x0082  */
    /* JADX WARN: Removed duplicated region for block: B:45:0x008c  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    void s() {
        /*
            r10 = this;
            k2.b r0 = k2.EnumC0588b.UNKNOWN
            android.net.Network r1 = r10.f9561k
            android.net.NetworkCapabilities r2 = r10.f9562l
            r3 = 0
            r4 = 0
            if (r2 == 0) goto L9a
            r5 = 2
            boolean r5 = r2.hasTransport(r5)
            r6 = 4
            r7 = 1
            if (r5 == 0) goto L16
            k2.b r0 = k2.EnumC0588b.BLUETOOTH
            goto L3a
        L16:
            boolean r5 = r2.hasTransport(r4)
            if (r5 == 0) goto L1f
            k2.b r0 = k2.EnumC0588b.CELLULAR
            goto L3a
        L1f:
            r5 = 3
            boolean r5 = r2.hasTransport(r5)
            if (r5 == 0) goto L29
            k2.b r0 = k2.EnumC0588b.ETHERNET
            goto L3a
        L29:
            boolean r5 = r2.hasTransport(r7)
            if (r5 == 0) goto L32
            k2.b r0 = k2.EnumC0588b.WIFI
            goto L3a
        L32:
            boolean r5 = r2.hasTransport(r6)
            if (r5 == 0) goto L3a
            k2.b r0 = k2.EnumC0588b.VPN
        L3a:
            if (r1 == 0) goto L45
            android.net.ConnectivityManager r5 = r10.c()     // Catch: java.lang.SecurityException -> L45
            android.net.NetworkInfo r5 = r5.getNetworkInfo(r1)     // Catch: java.lang.SecurityException -> L45
            goto L46
        L45:
            r5 = r3
        L46:
            int r8 = android.os.Build.VERSION.SDK_INT
            r9 = 28
            if (r8 < r9) goto L54
            r8 = 21
            boolean r8 = r2.hasCapability(r8)
            r8 = r8 ^ r7
            goto L67
        L54:
            if (r1 == 0) goto L66
            if (r5 == 0) goto L66
            android.net.NetworkInfo$DetailedState r8 = r5.getDetailedState()
            android.net.NetworkInfo$DetailedState r9 = android.net.NetworkInfo.DetailedState.CONNECTED
            boolean r8 = r8.equals(r9)
            if (r8 != 0) goto L66
            r8 = r7
            goto L67
        L66:
            r8 = r4
        L67:
            r9 = 12
            boolean r9 = r2.hasCapability(r9)
            if (r9 == 0) goto L7b
            r9 = 16
            boolean r9 = r2.hasCapability(r9)
            if (r9 == 0) goto L7b
            if (r8 != 0) goto L7b
            r8 = r7
            goto L7c
        L7b:
            r8 = r4
        L7c:
            boolean r6 = r2.hasTransport(r6)
            if (r6 == 0) goto L8c
            if (r8 == 0) goto L8d
            int r2 = r2.getLinkDownstreamBandwidthKbps()
            if (r2 == 0) goto L8d
            r4 = r7
            goto L8d
        L8c:
            r4 = r8
        L8d:
            if (r1 == 0) goto L9c
            k2.b r1 = k2.EnumC0588b.CELLULAR
            if (r0 != r1) goto L9c
            if (r4 == 0) goto L9c
            k2.a r3 = k2.EnumC0587a.b(r5)
            goto L9c
        L9a:
            k2.b r0 = k2.EnumC0588b.NONE
        L9c:
            r10.k(r0, r3, r4)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: j2.C0578h.s():void");
    }
}
