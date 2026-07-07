package I0;

import kotlin.enums.EnumEntries;
import kotlin.jvm.internal.DefaultConstructorMarker;
import w2.AbstractC0764a;

/* JADX WARN: Failed to restore enum class, 'enum' modifier and super class removed */
/* JADX WARN: Unknown enum class pattern. Please report as an issue! */
/* JADX INFO: loaded from: classes.dex */
public final class f {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public static final a f414b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public static final f f415c = new f("LOW", 0);

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public static final f f416d = new f("MEDIUM", 1);

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    public static final f f417e = new f("HIGH", 2);

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private static final /* synthetic */ f[] f418f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private static final /* synthetic */ EnumEntries f419g;

    public static final class a {
        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final f a(f fVar, f fVar2) {
            D2.h.f(fVar, "priority1");
            D2.h.f(fVar2, "priority2");
            return fVar.ordinal() > fVar2.ordinal() ? fVar : fVar2;
        }

        private a() {
        }
    }

    static {
        f[] fVarArrA = a();
        f418f = fVarArrA;
        f419g = AbstractC0764a.a(fVarArrA);
        f414b = new a(null);
    }

    private f(String str, int i3) {
    }

    private static final /* synthetic */ f[] a() {
        return new f[]{f415c, f416d, f417e};
    }

    public static final f b(f fVar, f fVar2) {
        return f414b.a(fVar, fVar2);
    }

    public static f valueOf(String str) {
        return (f) Enum.valueOf(f.class, str);
    }

    public static f[] values() {
        return (f[]) f418f.clone();
    }
}
