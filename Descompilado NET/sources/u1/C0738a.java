package u1;

import com.facebook.react.bridge.WritableMap;
import kotlin.jvm.internal.DefaultConstructorMarker;

/* JADX INFO: renamed from: u1.a, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public final class C0738a {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final String f10847a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final WritableMap f10848b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final long f10849c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private final boolean f10850d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private final InterfaceC0744g f10851e;

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public C0738a(String str, WritableMap writableMap) {
        this(str, writableMap, 0L, false, null, 28, null);
        D2.h.f(str, "taskKey");
        D2.h.f(writableMap, "data");
    }

    public final WritableMap a() {
        return this.f10848b;
    }

    public final InterfaceC0744g b() {
        return this.f10851e;
    }

    public final String c() {
        return this.f10847a;
    }

    public final long d() {
        return this.f10849c;
    }

    public final boolean e() {
        return this.f10850d;
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public C0738a(String str, WritableMap writableMap, long j3) {
        this(str, writableMap, j3, false, null, 24, null);
        D2.h.f(str, "taskKey");
        D2.h.f(writableMap, "data");
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public C0738a(String str, WritableMap writableMap, long j3, boolean z3) {
        this(str, writableMap, j3, z3, null, 16, null);
        D2.h.f(str, "taskKey");
        D2.h.f(writableMap, "data");
    }

    public C0738a(String str, WritableMap writableMap, long j3, boolean z3, InterfaceC0744g interfaceC0744g) {
        D2.h.f(str, "taskKey");
        D2.h.f(writableMap, "data");
        this.f10847a = str;
        this.f10848b = writableMap;
        this.f10849c = j3;
        this.f10850d = z3;
        this.f10851e = interfaceC0744g;
    }

    public /* synthetic */ C0738a(String str, WritableMap writableMap, long j3, boolean z3, InterfaceC0744g interfaceC0744g, int i3, DefaultConstructorMarker defaultConstructorMarker) {
        this(str, writableMap, (i3 & 4) != 0 ? 0L : j3, (i3 & 8) != 0 ? false : z3, (i3 & 16) != 0 ? C0745h.f10868b : interfaceC0744g);
    }

    /* JADX WARN: Illegal instructions before constructor call */
    public C0738a(C0738a c0738a) {
        D2.h.f(c0738a, "source");
        String str = c0738a.f10847a;
        WritableMap writableMapCopy = c0738a.f10848b.copy();
        long j3 = c0738a.f10849c;
        boolean z3 = c0738a.f10850d;
        InterfaceC0744g interfaceC0744g = c0738a.f10851e;
        this(str, writableMapCopy, j3, z3, interfaceC0744g != null ? interfaceC0744g.copy() : null);
    }
}
