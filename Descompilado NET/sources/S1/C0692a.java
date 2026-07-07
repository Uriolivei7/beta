package s1;

import D2.h;
import P1.d;
import com.facebook.react.bridge.WritableMap;

/* JADX INFO: renamed from: s1.a, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public final class C0692a extends d {

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private final String f10590h;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    private final WritableMap f10591i;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public C0692a(String str, WritableMap writableMap, int i3, int i4) {
        super(i3, i4);
        h.f(str, "eventName");
        this.f10590h = str;
        this.f10591i = writableMap;
    }

    @Override // P1.d
    protected WritableMap j() {
        return this.f10591i;
    }

    @Override // P1.d
    public String k() {
        return this.f10590h;
    }
}
