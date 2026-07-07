package y0;

import D2.h;
import android.graphics.PointF;
import android.graphics.Rect;
import android.net.Uri;
import java.util.Map;
import z0.InterfaceC0783b;

/* JADX INFO: renamed from: y0.b, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public final class C0777b {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static final C0777b f11017a = new C0777b();

    private C0777b() {
    }

    public static final InterfaceC0783b.a a(Map map, Map map2, Map map3, Map map4, Rect rect, String str, PointF pointF, Map map5, Object obj, boolean z3, Uri uri) {
        h.f(map, "componentAttribution");
        h.f(map2, "shortcutAttribution");
        InterfaceC0783b.a aVar = new InterfaceC0783b.a();
        if (rect != null) {
            aVar.f11037h = rect.width();
            aVar.f11038i = rect.height();
        }
        aVar.f11039j = str;
        if (pointF != null) {
            aVar.f11040k = Float.valueOf(pointF.x);
            aVar.f11041l = Float.valueOf(pointF.y);
        }
        aVar.f11035f = obj;
        aVar.f11042m = z3;
        aVar.f11036g = uri;
        aVar.f11032c = map3;
        aVar.f11033d = map5;
        aVar.f11031b = map2;
        aVar.f11030a = map;
        aVar.f11034e = map4;
        return aVar;
    }
}
