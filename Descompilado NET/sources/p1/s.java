package P1;

import kotlin.enums.EnumEntries;
import kotlin.jvm.internal.DefaultConstructorMarker;
import w2.AbstractC0764a;

/* JADX WARN: Failed to restore enum class, 'enum' modifier and super class removed */
/* JADX WARN: Unknown enum class pattern. Please report as an issue! */
/* JADX INFO: loaded from: classes.dex */
public final class s {

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public static final a f1701c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public static final s f1702d = new s("START", 0, "topTouchStart");

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    public static final s f1703e = new s("END", 1, "topTouchEnd");

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    public static final s f1704f = new s("MOVE", 2, "topTouchMove");

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    public static final s f1705g = new s("CANCEL", 3, "topTouchCancel");

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private static final /* synthetic */ s[] f1706h;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    private static final /* synthetic */ EnumEntries f1707i;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final String f1708b;

    public static final class a {
        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final String a(s sVar) {
            D2.h.f(sVar, "type");
            return sVar.c();
        }

        private a() {
        }
    }

    static {
        s[] sVarArrA = a();
        f1706h = sVarArrA;
        f1707i = AbstractC0764a.a(sVarArrA);
        f1701c = new a(null);
    }

    private s(String str, int i3, String str2) {
        this.f1708b = str2;
    }

    private static final /* synthetic */ s[] a() {
        return new s[]{f1702d, f1703e, f1704f, f1705g};
    }

    public static final String b(s sVar) {
        return f1701c.a(sVar);
    }

    public static s valueOf(String str) {
        return (s) Enum.valueOf(s.class, str);
    }

    public static s[] values() {
        return (s[]) f1706h.clone();
    }

    public final String c() {
        return this.f1708b;
    }
}
