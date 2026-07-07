package r;

import android.view.View;
import android.view.accessibility.AccessibilityRecord;

/* JADX INFO: loaded from: classes.dex */
public class x {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final AccessibilityRecord f10489a;

    @Deprecated
    public x(Object obj) {
        this.f10489a = (AccessibilityRecord) obj;
    }

    public static void a(AccessibilityRecord accessibilityRecord, int i3) {
        accessibilityRecord.setMaxScrollX(i3);
    }

    public static void b(AccessibilityRecord accessibilityRecord, int i3) {
        accessibilityRecord.setMaxScrollY(i3);
    }

    public static void c(AccessibilityRecord accessibilityRecord, View view, int i3) {
        accessibilityRecord.setSource(view, i3);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof x)) {
            return false;
        }
        x xVar = (x) obj;
        AccessibilityRecord accessibilityRecord = this.f10489a;
        return accessibilityRecord == null ? xVar.f10489a == null : accessibilityRecord.equals(xVar.f10489a);
    }

    public int hashCode() {
        AccessibilityRecord accessibilityRecord = this.f10489a;
        if (accessibilityRecord == null) {
            return 0;
        }
        return accessibilityRecord.hashCode();
    }
}
