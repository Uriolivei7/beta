package P1;

import com.facebook.react.bridge.WritableMap;
import com.facebook.react.uimanager.events.RCTEventEmitter;
import com.facebook.react.uimanager.events.RCTModernEventEmitter;

/* JADX INFO: loaded from: classes.dex */
public abstract class d {

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private static int f1604g;

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private boolean f1605a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private int f1606b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private int f1607c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private long f1608d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private int f1609e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private b f1610f;

    class a implements b {
        a() {
        }

        @Override // P1.d.b
        public boolean a(int i3, String str) {
            return i3 == d.this.o() && str.equals(d.this.k());
        }
    }

    public interface b {
        boolean a(int i3, String str);
    }

    protected d() {
        int i3 = f1604g;
        f1604g = i3 + 1;
        this.f1609e = i3;
    }

    public boolean a() {
        return true;
    }

    public d b(d dVar) {
        return m() >= dVar.m() ? this : dVar;
    }

    public void c(RCTEventEmitter rCTEventEmitter) {
        rCTEventEmitter.receiveEvent(o(), k(), j());
    }

    public void d(RCTModernEventEmitter rCTModernEventEmitter) {
        if (l() != -1) {
            rCTModernEventEmitter.receiveEvent(l(), o(), k(), a(), g(), j(), i());
        } else {
            c(rCTModernEventEmitter);
        }
    }

    final void e() {
        this.f1605a = false;
        t();
    }

    protected boolean f() {
        return false;
    }

    public short g() {
        return (short) 0;
    }

    public b h() {
        if (this.f1610f == null) {
            this.f1610f = new a();
        }
        return this.f1610f;
    }

    protected int i() {
        return 2;
    }

    protected WritableMap j() {
        return null;
    }

    public abstract String k();

    public final int l() {
        return this.f1606b;
    }

    public final long m() {
        return this.f1608d;
    }

    public int n() {
        return this.f1609e;
    }

    public final int o() {
        return this.f1607c;
    }

    protected void p(int i3) {
        q(-1, i3);
    }

    protected void q(int i3, int i4) {
        r(i3, i4, e1.l.c());
    }

    protected void r(int i3, int i4, long j3) {
        this.f1606b = i3;
        this.f1607c = i4;
        this.f1608d = j3;
        this.f1605a = true;
    }

    boolean s() {
        return this.f1605a;
    }

    protected d(int i3) {
        int i4 = f1604g;
        f1604g = i4 + 1;
        this.f1609e = i4;
        p(i3);
    }

    protected d(int i3, int i4) {
        int i5 = f1604g;
        f1604g = i5 + 1;
        this.f1609e = i5;
        q(i3, i4);
    }

    public void t() {
    }
}
