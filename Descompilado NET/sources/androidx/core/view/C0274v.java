package androidx.core.view;

import android.graphics.Insets;
import android.graphics.Rect;
import android.os.Build;
import android.view.DisplayCutout;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: renamed from: androidx.core.view.v, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public final class C0274v {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final DisplayCutout f4668a;

    /* JADX INFO: renamed from: androidx.core.view.v$a */
    static class a {
        static DisplayCutout a(Rect rect, List<Rect> list) {
            return new DisplayCutout(rect, list);
        }

        static List<Rect> b(DisplayCutout displayCutout) {
            return displayCutout.getBoundingRects();
        }

        static int c(DisplayCutout displayCutout) {
            return displayCutout.getSafeInsetBottom();
        }

        static int d(DisplayCutout displayCutout) {
            return displayCutout.getSafeInsetLeft();
        }

        static int e(DisplayCutout displayCutout) {
            return displayCutout.getSafeInsetRight();
        }

        static int f(DisplayCutout displayCutout) {
            return displayCutout.getSafeInsetTop();
        }
    }

    /* JADX INFO: renamed from: androidx.core.view.v$b */
    static class b {
        static DisplayCutout a(Insets insets, Rect rect, Rect rect2, Rect rect3, Rect rect4) {
            return new DisplayCutout(insets, rect, rect2, rect3, rect4);
        }
    }

    /* JADX INFO: renamed from: androidx.core.view.v$c */
    static class c {
        static DisplayCutout a(Insets insets, Rect rect, Rect rect2, Rect rect3, Rect rect4, Insets insets2) {
            return new DisplayCutout(insets, rect, rect2, rect3, rect4, insets2);
        }

        static Insets b(DisplayCutout displayCutout) {
            return displayCutout.getWaterfallInsets();
        }
    }

    public C0274v(Rect rect, List<Rect> list) {
        this(Build.VERSION.SDK_INT >= 28 ? a.a(rect, list) : null);
    }

    private static DisplayCutout a(androidx.core.graphics.b bVar, Rect rect, Rect rect2, Rect rect3, Rect rect4, androidx.core.graphics.b bVar2) {
        int i3 = Build.VERSION.SDK_INT;
        if (i3 >= 30) {
            return c.a(bVar.e(), rect, rect2, rect3, rect4, bVar2.e());
        }
        if (i3 >= 29) {
            return b.a(bVar.e(), rect, rect2, rect3, rect4);
        }
        if (i3 < 28) {
            return null;
        }
        Rect rect5 = new Rect(bVar.f4472a, bVar.f4473b, bVar.f4474c, bVar.f4475d);
        ArrayList arrayList = new ArrayList();
        if (rect != null) {
            arrayList.add(rect);
        }
        if (rect2 != null) {
            arrayList.add(rect2);
        }
        if (rect3 != null) {
            arrayList.add(rect3);
        }
        if (rect4 != null) {
            arrayList.add(rect4);
        }
        return a.a(rect5, arrayList);
    }

    static C0274v f(DisplayCutout displayCutout) {
        if (displayCutout == null) {
            return null;
        }
        return new C0274v(displayCutout);
    }

    public int b() {
        if (Build.VERSION.SDK_INT >= 28) {
            return a.c(this.f4668a);
        }
        return 0;
    }

    public int c() {
        if (Build.VERSION.SDK_INT >= 28) {
            return a.d(this.f4668a);
        }
        return 0;
    }

    public int d() {
        if (Build.VERSION.SDK_INT >= 28) {
            return a.e(this.f4668a);
        }
        return 0;
    }

    public int e() {
        if (Build.VERSION.SDK_INT >= 28) {
            return a.f(this.f4668a);
        }
        return 0;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || C0274v.class != obj.getClass()) {
            return false;
        }
        return q.c.a(this.f4668a, ((C0274v) obj).f4668a);
    }

    public int hashCode() {
        DisplayCutout displayCutout = this.f4668a;
        if (displayCutout == null) {
            return 0;
        }
        return displayCutout.hashCode();
    }

    public String toString() {
        return "DisplayCutoutCompat{" + this.f4668a + "}";
    }

    public C0274v(androidx.core.graphics.b bVar, Rect rect, Rect rect2, Rect rect3, Rect rect4, androidx.core.graphics.b bVar2) {
        this(a(bVar, rect, rect2, rect3, rect4, bVar2));
    }

    private C0274v(DisplayCutout displayCutout) {
        this.f4668a = displayCutout;
    }
}
