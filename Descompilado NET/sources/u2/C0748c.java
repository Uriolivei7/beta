package u2;

import D2.h;

/* JADX INFO: Access modifiers changed from: package-private */
/* JADX INFO: renamed from: u2.c, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public class C0748c extends C0747b {
    public static int d(int i3, int... iArr) {
        h.f(iArr, "other");
        for (int i4 : iArr) {
            i3 = Math.max(i3, i4);
        }
        return i3;
    }

    public static int e(int i3, int... iArr) {
        h.f(iArr, "other");
        for (int i4 : iArr) {
            i3 = Math.min(i3, i4);
        }
        return i3;
    }
}
