package o2;

import com.facebook.react.bridge.WritableMap;
import com.facebook.react.uimanager.C0429f0;
import n2.C0623b;

/* JADX INFO: renamed from: o2.a, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public final class C0630a extends AbstractC0631b {

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private final float f10161e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private final float f10162f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private final float f10163g;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private final float f10164h;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public C0630a(C0623b c0623b) {
        super(c0623b);
        D2.h.f(c0623b, "handler");
        this.f10161e = c0623b.J();
        this.f10162f = c0623b.K();
        this.f10163g = c0623b.H();
        this.f10164h = c0623b.I();
    }

    @Override // o2.AbstractC0631b
    public void a(WritableMap writableMap) {
        D2.h.f(writableMap, "eventData");
        super.a(writableMap);
        writableMap.putDouble("x", C0429f0.f(this.f10161e));
        writableMap.putDouble("y", C0429f0.f(this.f10162f));
        writableMap.putDouble("absoluteX", C0429f0.f(this.f10163g));
        writableMap.putDouble("absoluteY", C0429f0.f(this.f10164h));
    }
}
