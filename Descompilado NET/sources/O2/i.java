package o2;

import com.facebook.react.bridge.WritableMap;
import com.facebook.react.uimanager.C0429f0;
import n2.x;

/* JADX INFO: loaded from: classes.dex */
public final class i extends AbstractC0631b {

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private final double f10193e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private final float f10194f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private final float f10195g;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private final double f10196h;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public i(x xVar) {
        super(xVar);
        D2.h.f(xVar, "handler");
        this.f10193e = xVar.W0();
        this.f10194f = xVar.U0();
        this.f10195g = xVar.V0();
        this.f10196h = xVar.X0();
    }

    @Override // o2.AbstractC0631b
    public void a(WritableMap writableMap) {
        D2.h.f(writableMap, "eventData");
        super.a(writableMap);
        writableMap.putDouble("rotation", this.f10193e);
        writableMap.putDouble("anchorX", C0429f0.f(this.f10194f));
        writableMap.putDouble("anchorY", C0429f0.f(this.f10195g));
        writableMap.putDouble("velocity", this.f10196h);
    }
}
