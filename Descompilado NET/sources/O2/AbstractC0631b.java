package o2;

import com.facebook.react.bridge.WritableMap;
import n2.C0625d;

/* JADX INFO: renamed from: o2.b, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public abstract class AbstractC0631b {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final int f10165a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final int f10166b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final int f10167c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private final int f10168d;

    public AbstractC0631b(C0625d c0625d) {
        D2.h.f(c0625d, "handler");
        this.f10165a = c0625d.M();
        this.f10166b = c0625d.R();
        this.f10167c = c0625d.Q();
        this.f10168d = c0625d.O();
    }

    public void a(WritableMap writableMap) {
        D2.h.f(writableMap, "eventData");
        writableMap.putInt("numberOfPointers", this.f10165a);
        writableMap.putInt("handlerTag", this.f10166b);
        writableMap.putInt("state", this.f10167c);
        writableMap.putInt("pointerType", this.f10168d);
    }
}
