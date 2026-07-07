package D0;

import D2.h;
import java.util.List;
import s2.AbstractC0717n;

/* JADX INFO: loaded from: classes.dex */
public final class b {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static final b f134a = new b();

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public static final c f135b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public static final c f136c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public static final c f137d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    public static final c f138e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    public static final c f139f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    public static final c f140g;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    public static final c f141h;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    public static final c f142i;

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    public static final c f143j;

    /* JADX INFO: renamed from: k, reason: collision with root package name */
    public static final c f144k;

    /* JADX INFO: renamed from: l, reason: collision with root package name */
    public static final c f145l;

    /* JADX INFO: renamed from: m, reason: collision with root package name */
    public static final c f146m;

    /* JADX INFO: renamed from: n, reason: collision with root package name */
    public static final c f147n;

    /* JADX INFO: renamed from: o, reason: collision with root package name */
    public static final c f148o;

    /* JADX INFO: renamed from: p, reason: collision with root package name */
    public static final List f149p;

    static {
        c cVar = new c("JPEG", "jpeg");
        f135b = cVar;
        c cVar2 = new c("PNG", "png");
        f136c = cVar2;
        c cVar3 = new c("GIF", "gif");
        f137d = cVar3;
        c cVar4 = new c("BMP", "bmp");
        f138e = cVar4;
        c cVar5 = new c("ICO", "ico");
        f139f = cVar5;
        c cVar6 = new c("WEBP_SIMPLE", "webp");
        f140g = cVar6;
        c cVar7 = new c("WEBP_LOSSLESS", "webp");
        f141h = cVar7;
        c cVar8 = new c("WEBP_EXTENDED", "webp");
        f142i = cVar8;
        c cVar9 = new c("WEBP_EXTENDED_WITH_ALPHA", "webp");
        f143j = cVar9;
        c cVar10 = new c("WEBP_ANIMATED", "webp");
        f144k = cVar10;
        c cVar11 = new c("HEIF", "heif");
        f145l = cVar11;
        f146m = new c("DNG", "dng");
        c cVar12 = new c("BINARY_XML", "xml");
        f147n = cVar12;
        c cVar13 = new c("AVIF", "avif");
        f148o = cVar13;
        f149p = AbstractC0717n.j(cVar, cVar2, cVar3, cVar4, cVar5, cVar6, cVar7, cVar8, cVar9, cVar10, cVar11, cVar12, cVar13);
    }

    private b() {
    }

    public static final boolean a(c cVar) {
        h.f(cVar, "imageFormat");
        return cVar == f140g || cVar == f141h || cVar == f142i || cVar == f143j;
    }

    public static final boolean b(c cVar) {
        h.f(cVar, "imageFormat");
        return a(cVar) || cVar == f144k;
    }
}
