package Y0;

import D2.h;
import K2.o;
import M0.c;
import O0.d;
import O0.i;
import O0.j;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import f0.f;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import kotlin.jvm.internal.DefaultConstructorMarker;
import s2.AbstractC0717n;

/* JADX INFO: loaded from: classes.dex */
public final class b implements c {

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public static final a f2792c = new a(null);

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final Resources f2793a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final Map f2794b;

    public static final class a {
        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private a() {
        }
    }

    public b(Resources resources) {
        h.f(resources, "resources");
        this.f2793a = resources;
        this.f2794b = new ConcurrentHashMap();
    }

    private final int b(String str) {
        Map map = this.f2794b;
        Object objValueOf = map.get(str);
        if (objValueOf == null) {
            Uri uri = Uri.parse(str);
            h.e(uri, "parse(...)");
            objValueOf = Integer.valueOf(c(uri));
            map.put(str, objValueOf);
        }
        return ((Number) objValueOf).intValue();
    }

    private final int c(Uri uri) {
        Integer numJ;
        if (!f.n(uri) && !f.p(uri)) {
            throw new IllegalStateException(("Unsupported uri " + uri).toString());
        }
        List<String> pathSegments = uri.getPathSegments();
        h.e(pathSegments, "getPathSegments(...)");
        String str = (String) AbstractC0717n.U(pathSegments);
        if (str != null && (numJ = o.j(str)) != null) {
            return numJ.intValue();
        }
        throw new IllegalStateException(("Unable to read resource ID from " + uri.getPath()).toString());
    }

    @Override // M0.c
    public d a(j jVar, int i3, O0.o oVar, I0.d dVar) {
        h.f(jVar, "encodedImage");
        h.f(oVar, "qualityInfo");
        h.f(dVar, "options");
        try {
            String strD0 = jVar.d0();
            if (strD0 == null) {
                throw new IllegalStateException("No source in encoded image");
            }
            Drawable drawableE = androidx.core.content.res.f.e(this.f2793a, b(strD0), null);
            if (drawableE != null) {
                return new i(drawableE);
            }
            return null;
        } catch (Throwable th) {
            Y.a.n("XmlFormatDecoder", "Cannot decode xml", th);
            return null;
        }
    }
}
