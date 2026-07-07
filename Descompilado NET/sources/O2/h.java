package o2;

import com.facebook.react.bridge.WritableMap;
import com.facebook.react.uimanager.C0429f0;
import n2.u;

/* JADX INFO: loaded from: classes.dex */
public final class h extends AbstractC0631b {

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private final double f10189e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private final float f10190f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private final float f10191g;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private final double f10192h;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public h(u uVar) {
        super(uVar);
        D2.h.f(uVar, "handler");
        this.f10189e = uVar.Z0();
        this.f10190f = uVar.X0();
        this.f10191g = uVar.Y0();
        this.f10192h = uVar.a1();
    }

    @Override // o2.AbstractC0631b
    public void a(WritableMap writableMap) {
        D2.h.f(writableMap, "eventData");
        super.a(writableMap);
        writableMap.putDouble("scale", this.f10189e);
        writableMap.putDouble("focalX", C0429f0.f(this.f10190f));
        writableMap.putDouble("focalY", C0429f0.f(this.f10191g));
        writableMap.putDouble("velocity", this.f10192h);
    }
}
