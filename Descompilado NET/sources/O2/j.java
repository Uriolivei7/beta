package o2;

import com.facebook.react.bridge.WritableMap;
import com.facebook.react.uimanager.C0429f0;
import n2.C0620B;

/* JADX INFO: loaded from: classes.dex */
public final class j extends AbstractC0631b {

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private final float f10197e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private final float f10198f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private final float f10199g;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private final float f10200h;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public j(C0620B c0620b) {
        super(c0620b);
        D2.h.f(c0620b, "handler");
        this.f10197e = c0620b.J();
        this.f10198f = c0620b.K();
        this.f10199g = c0620b.H();
        this.f10200h = c0620b.I();
    }

    @Override // o2.AbstractC0631b
    public void a(WritableMap writableMap) {
        D2.h.f(writableMap, "eventData");
        super.a(writableMap);
        writableMap.putDouble("x", C0429f0.f(this.f10197e));
        writableMap.putDouble("y", C0429f0.f(this.f10198f));
        writableMap.putDouble("absoluteX", C0429f0.f(this.f10199g));
        writableMap.putDouble("absoluteY", C0429f0.f(this.f10200h));
    }
}
