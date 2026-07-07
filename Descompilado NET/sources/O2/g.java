package o2;

import com.facebook.react.bridge.WritableMap;
import com.facebook.react.uimanager.C0429f0;
import n2.t;
import n2.z;

/* JADX INFO: loaded from: classes.dex */
public final class g extends AbstractC0631b {

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private final float f10180e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private final float f10181f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private final float f10182g;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private final float f10183h;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    private final float f10184i;

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    private final float f10185j;

    /* JADX INFO: renamed from: k, reason: collision with root package name */
    private final float f10186k;

    /* JADX INFO: renamed from: l, reason: collision with root package name */
    private final float f10187l;

    /* JADX INFO: renamed from: m, reason: collision with root package name */
    private final z f10188m;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public g(t tVar) {
        super(tVar);
        D2.h.f(tVar, "handler");
        this.f10180e = tVar.J();
        this.f10181f = tVar.K();
        this.f10182g = tVar.H();
        this.f10183h = tVar.I();
        this.f10184i = tVar.V0();
        this.f10185j = tVar.W0();
        this.f10186k = tVar.X0();
        this.f10187l = tVar.Y0();
        this.f10188m = tVar.U0();
    }

    @Override // o2.AbstractC0631b
    public void a(WritableMap writableMap) {
        D2.h.f(writableMap, "eventData");
        super.a(writableMap);
        writableMap.putDouble("x", C0429f0.f(this.f10180e));
        writableMap.putDouble("y", C0429f0.f(this.f10181f));
        writableMap.putDouble("absoluteX", C0429f0.f(this.f10182g));
        writableMap.putDouble("absoluteY", C0429f0.f(this.f10183h));
        writableMap.putDouble("translationX", C0429f0.f(this.f10184i));
        writableMap.putDouble("translationY", C0429f0.f(this.f10185j));
        writableMap.putDouble("velocityX", C0429f0.f(this.f10186k));
        writableMap.putDouble("velocityY", C0429f0.f(this.f10187l));
        if (this.f10188m.a() == -1.0d) {
            return;
        }
        writableMap.putMap("stylusData", this.f10188m.b());
    }
}
