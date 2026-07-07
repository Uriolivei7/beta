package com.reactnativecommunity.netinfo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Handler;
import j2.C0576f;

/* JADX INFO: loaded from: classes.dex */
public class a {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final c f8446a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final Context f8447b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final InterfaceC0120a f8448c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private final Runnable f8449d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private Handler f8450e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private boolean f8451f = false;

    /* JADX INFO: renamed from: com.reactnativecommunity.netinfo.a$a, reason: collision with other inner class name */
    public interface InterfaceC0120a {
        void onAmazonFireDeviceConnectivityChanged(boolean z3);
    }

    private class b implements Runnable {
        @Override // java.lang.Runnable
        public void run() {
            if (a.this.f8451f) {
                a.this.f8447b.sendBroadcast(new Intent("com.amazon.tv.networkmonitor.CONNECTIVITY_CHECK"));
                a.this.f8450e.postDelayed(a.this.f8449d, 10000L);
            }
        }

        private b() {
        }
    }

    private class c extends BroadcastReceiver {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        boolean f8453a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        private Boolean f8454b;

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            boolean z3;
            String action = intent == null ? null : intent.getAction();
            if ("com.amazon.tv.networkmonitor.INTERNET_DOWN".equals(action)) {
                z3 = false;
            } else if (!"com.amazon.tv.networkmonitor.INTERNET_UP".equals(action)) {
                return;
            } else {
                z3 = true;
            }
            Boolean bool = this.f8454b;
            if (bool == null || bool.booleanValue() != z3) {
                this.f8454b = Boolean.valueOf(z3);
                a.this.f8448c.onAmazonFireDeviceConnectivityChanged(z3);
            }
        }

        private c() {
            this.f8453a = false;
        }
    }

    a(Context context, InterfaceC0120a interfaceC0120a) {
        this.f8446a = new c();
        this.f8449d = new b();
        this.f8447b = context;
        this.f8448c = interfaceC0120a;
    }

    private boolean f() {
        if (Build.MANUFACTURER.equals("Amazon")) {
            String str = Build.MODEL;
            if (str.startsWith("AF") || str.startsWith("KF")) {
                return true;
            }
        }
        return false;
    }

    private void h() {
        if (this.f8446a.f8453a) {
            return;
        }
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.amazon.tv.networkmonitor.INTERNET_DOWN");
        intentFilter.addAction("com.amazon.tv.networkmonitor.INTERNET_UP");
        C0576f.a(this.f8447b, this.f8446a, intentFilter, false);
        this.f8446a.f8453a = true;
    }

    private void i() {
        if (this.f8451f) {
            return;
        }
        Handler handler = new Handler();
        this.f8450e = handler;
        this.f8451f = true;
        handler.post(this.f8449d);
    }

    private void j() {
        if (this.f8451f) {
            this.f8451f = false;
            this.f8450e.removeCallbacksAndMessages(null);
            this.f8450e = null;
        }
    }

    private void l() {
        c cVar = this.f8446a;
        if (cVar.f8453a) {
            this.f8447b.unregisterReceiver(cVar);
            this.f8446a.f8453a = false;
        }
    }

    public void g() {
        if (f()) {
            h();
            i();
        }
    }

    public void k() {
        if (f()) {
            j();
            l();
        }
    }
}
