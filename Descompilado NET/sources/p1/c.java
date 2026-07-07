package P1;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.uimanager.C0429f0;

/* JADX INFO: loaded from: classes.dex */
public final class c extends d {

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private final int f1602h;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    private final int f1603i;

    public c(int i3, int i4, int i5, int i6) {
        super(i3, i4);
        this.f1602h = i5;
        this.f1603i = i6;
    }

    @Override // P1.d
    protected WritableMap j() {
        WritableMap writableMapCreateMap = Arguments.createMap();
        writableMapCreateMap.putDouble("width", C0429f0.f(this.f1602h));
        writableMapCreateMap.putDouble("height", C0429f0.f(this.f1603i));
        D2.h.c(writableMapCreateMap);
        return writableMapCreateMap;
    }

    @Override // P1.d
    public String k() {
        return "topContentSizeChange";
    }

    public c(int i3, int i4, int i5) {
        this(-1, i3, i4, i5);
    }
}
