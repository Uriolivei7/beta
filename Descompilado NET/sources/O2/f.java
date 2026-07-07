package o2;

import com.facebook.react.bridge.WritableMap;
import n2.q;

/* JADX INFO: loaded from: classes.dex */
public final class f extends AbstractC0631b {

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private final boolean f10179e;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public f(q qVar) {
        super(qVar);
        D2.h.f(qVar, "handler");
        this.f10179e = qVar.c0();
    }

    @Override // o2.AbstractC0631b
    public void a(WritableMap writableMap) {
        D2.h.f(writableMap, "eventData");
        super.a(writableMap);
        writableMap.putBoolean("pointerInside", this.f10179e);
    }
}
