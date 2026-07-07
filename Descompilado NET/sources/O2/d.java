package o2;

import com.facebook.react.bridge.WritableMap;
import com.facebook.react.uimanager.C0429f0;
import n2.o;

/* JADX INFO: loaded from: classes.dex */
public final class d extends AbstractC0631b {

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private final float f10174e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private final float f10175f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private final float f10176g;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private final float f10177h;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    private final int f10178i;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public d(o oVar) {
        super(oVar);
        D2.h.f(oVar, "handler");
        this.f10174e = oVar.J();
        this.f10175f = oVar.K();
        this.f10176g = oVar.H();
        this.f10177h = oVar.I();
        this.f10178i = oVar.V0();
    }

    @Override // o2.AbstractC0631b
    public void a(WritableMap writableMap) {
        D2.h.f(writableMap, "eventData");
        super.a(writableMap);
        writableMap.putDouble("x", C0429f0.f(this.f10174e));
        writableMap.putDouble("y", C0429f0.f(this.f10175f));
        writableMap.putDouble("absoluteX", C0429f0.f(this.f10176g));
        writableMap.putDouble("absoluteY", C0429f0.f(this.f10177h));
        writableMap.putInt("duration", this.f10178i);
    }
}
