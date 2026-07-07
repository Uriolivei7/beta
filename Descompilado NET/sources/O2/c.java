package o2;

import com.facebook.react.bridge.WritableMap;
import com.facebook.react.uimanager.C0429f0;
import n2.m;
import n2.z;

/* JADX INFO: loaded from: classes.dex */
public final class c extends AbstractC0631b {

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private final float f10169e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private final float f10170f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private final float f10171g;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private final float f10172h;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    private final z f10173i;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public c(m mVar) {
        super(mVar);
        D2.h.f(mVar, "handler");
        this.f10169e = mVar.J();
        this.f10170f = mVar.K();
        this.f10171g = mVar.H();
        this.f10172h = mVar.I();
        this.f10173i = mVar.V0();
    }

    @Override // o2.AbstractC0631b
    public void a(WritableMap writableMap) {
        D2.h.f(writableMap, "eventData");
        super.a(writableMap);
        writableMap.putDouble("x", C0429f0.f(this.f10169e));
        writableMap.putDouble("y", C0429f0.f(this.f10170f));
        writableMap.putDouble("absoluteX", C0429f0.f(this.f10171g));
        writableMap.putDouble("absoluteY", C0429f0.f(this.f10172h));
        if (this.f10173i.a() == -1.0d) {
            return;
        }
        writableMap.putMap("stylusData", this.f10173i.b());
    }
}
