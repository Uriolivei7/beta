package J0;

import kotlin.enums.EnumEntries;
import w2.AbstractC0764a;

/* JADX WARN: Failed to restore enum class, 'enum' modifier and super class removed */
/* JADX WARN: Unknown enum class pattern. Please report as an issue! */
/* JADX INFO: renamed from: J0.n, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public final class EnumC0180n {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public static final EnumC0180n f598b = new EnumC0180n("ALWAYS", 0);

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public static final EnumC0180n f599c = new EnumC0180n("AUTO", 1);

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public static final EnumC0180n f600d = new EnumC0180n("NEVER", 2);

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private static final /* synthetic */ EnumC0180n[] f601e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private static final /* synthetic */ EnumEntries f602f;

    static {
        EnumC0180n[] enumC0180nArrA = a();
        f601e = enumC0180nArrA;
        f602f = AbstractC0764a.a(enumC0180nArrA);
    }

    private EnumC0180n(String str, int i3) {
    }

    private static final /* synthetic */ EnumC0180n[] a() {
        return new EnumC0180n[]{f598b, f599c, f600d};
    }

    public static EnumC0180n valueOf(String str) {
        return (EnumC0180n) Enum.valueOf(EnumC0180n.class, str);
    }

    public static EnumC0180n[] values() {
        return (EnumC0180n[]) f601e.clone();
    }
}
