package com.facebook.react.fabric;

import com.facebook.react.bridge.ReactMarker;
import com.facebook.react.bridge.ReactMarkerConstants;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class a implements ReactMarker.FabricMarkerListener {

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    static final g f6814c = new g();

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    static final g f6815d = new g();

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    static final g f6816e = new g();

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    static final g f6817f = new g();

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    static final g f6818g = new g();

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final Map f6819a = new HashMap();

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final List f6820b = new ArrayList();

    /* JADX INFO: renamed from: com.facebook.react.fabric.a$a, reason: collision with other inner class name */
    public interface InterfaceC0106a {
        void a(b bVar);
    }

    public static class b {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        private final long f6821a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        private final Map f6822b;

        /* JADX INFO: Access modifiers changed from: private */
        public void b(ReactMarkerConstants reactMarkerConstants, c cVar) {
            this.f6822b.put(reactMarkerConstants, cVar);
        }

        private long r(ReactMarkerConstants reactMarkerConstants) {
            c cVar = (c) this.f6822b.get(reactMarkerConstants);
            if (cVar != null) {
                return cVar.a();
            }
            return -1L;
        }

        public long c() {
            return d() - e();
        }

        public long d() {
            return r(ReactMarkerConstants.FABRIC_BATCH_EXECUTION_END);
        }

        public long e() {
            return r(ReactMarkerConstants.FABRIC_BATCH_EXECUTION_START);
        }

        public long f() {
            return g() - i();
        }

        public long g() {
            return r(ReactMarkerConstants.FABRIC_COMMIT_END);
        }

        public long h() {
            return this.f6821a;
        }

        public long i() {
            return r(ReactMarkerConstants.FABRIC_COMMIT_START);
        }

        public long j() {
            return k() - l();
        }

        public long k() {
            return r(ReactMarkerConstants.FABRIC_DIFF_END);
        }

        public long l() {
            return r(ReactMarkerConstants.FABRIC_DIFF_START);
        }

        public long m() {
            return r(ReactMarkerConstants.FABRIC_FINISH_TRANSACTION_END);
        }

        public long n() {
            return r(ReactMarkerConstants.FABRIC_FINISH_TRANSACTION_START);
        }

        public long o() {
            return p() - q();
        }

        public long p() {
            return r(ReactMarkerConstants.FABRIC_LAYOUT_END);
        }

        public long q() {
            return r(ReactMarkerConstants.FABRIC_LAYOUT_START);
        }

        public long s() {
            return m() - n();
        }

        public String toString() {
            return "FabricCommitPoint{mCommitNumber=" + this.f6821a + ", mPoints=" + this.f6822b + '}';
        }

        private b(int i3) {
            this.f6822b = new HashMap();
            this.f6821a = i3;
        }
    }

    private static class c {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        private final long f6823a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        private final int f6824b;

        public c(long j3, int i3) {
            this.f6823a = j3;
            this.f6824b = i3;
        }

        public long a() {
            return this.f6823a;
        }
    }

    private static boolean b(ReactMarkerConstants reactMarkerConstants) {
        return reactMarkerConstants == ReactMarkerConstants.FABRIC_COMMIT_START || reactMarkerConstants == ReactMarkerConstants.FABRIC_COMMIT_END || reactMarkerConstants == ReactMarkerConstants.FABRIC_FINISH_TRANSACTION_START || reactMarkerConstants == ReactMarkerConstants.FABRIC_FINISH_TRANSACTION_END || reactMarkerConstants == ReactMarkerConstants.FABRIC_DIFF_START || reactMarkerConstants == ReactMarkerConstants.FABRIC_DIFF_END || reactMarkerConstants == ReactMarkerConstants.FABRIC_LAYOUT_START || reactMarkerConstants == ReactMarkerConstants.FABRIC_LAYOUT_END || reactMarkerConstants == ReactMarkerConstants.FABRIC_BATCH_EXECUTION_START || reactMarkerConstants == ReactMarkerConstants.FABRIC_BATCH_EXECUTION_END || reactMarkerConstants == ReactMarkerConstants.FABRIC_UPDATE_UI_MAIN_THREAD_START || reactMarkerConstants == ReactMarkerConstants.FABRIC_UPDATE_UI_MAIN_THREAD_END || reactMarkerConstants == ReactMarkerConstants.FABRIC_LAYOUT_AFFECTED_NODES;
    }

    private void c(b bVar) {
        Iterator it = this.f6820b.iterator();
        while (it.hasNext()) {
            ((InterfaceC0106a) it.next()).a(bVar);
        }
    }

    public void a(InterfaceC0106a interfaceC0106a) {
        this.f6820b.add(interfaceC0106a);
    }

    public void d(InterfaceC0106a interfaceC0106a) {
        this.f6820b.remove(interfaceC0106a);
    }

    @Override // com.facebook.react.bridge.ReactMarker.FabricMarkerListener
    public void logFabricMarker(ReactMarkerConstants reactMarkerConstants, String str, int i3, long j3) {
        logFabricMarker(reactMarkerConstants, str, i3, j3, 0);
    }

    @Override // com.facebook.react.bridge.ReactMarker.FabricMarkerListener
    public void logFabricMarker(ReactMarkerConstants reactMarkerConstants, String str, int i3, long j3, int i4) {
        if (b(reactMarkerConstants)) {
            b bVar = (b) this.f6819a.get(Integer.valueOf(i3));
            if (bVar == null) {
                bVar = new b(i3);
                this.f6819a.put(Integer.valueOf(i3), bVar);
            }
            bVar.b(reactMarkerConstants, new c(j3, i4));
            if (reactMarkerConstants != ReactMarkerConstants.FABRIC_BATCH_EXECUTION_END || j3 <= 0) {
                return;
            }
            c(bVar);
            this.f6819a.remove(Integer.valueOf(i3));
        }
    }
}
