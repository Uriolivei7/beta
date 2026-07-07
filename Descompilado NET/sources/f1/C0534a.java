package f1;

import D2.h;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Build;
import android.util.SparseArray;
import java.util.LinkedHashMap;
import java.util.Map;
import kotlin.jvm.internal.DefaultConstructorMarker;

/* JADX INFO: renamed from: f1.a, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public final class C0534a {

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public static final b f9381c = new b(null);

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private static final String[] f9382d = {"", "_bold", "_italic", "_bold_italic"};

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private static final String[] f9383e = {".ttf", ".otf"};

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private static final C0534a f9384f = new C0534a();

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final Map f9385a = new LinkedHashMap();

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final Map f9386b = new LinkedHashMap();

    /* JADX INFO: renamed from: f1.a$a, reason: collision with other inner class name */
    private static final class C0127a {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        private final SparseArray f9387a = new SparseArray(4);

        public final Typeface a(int i3) {
            return (Typeface) this.f9387a.get(i3);
        }

        public final void b(int i3, Typeface typeface) {
            this.f9387a.put(i3, typeface);
        }
    }

    /* JADX INFO: renamed from: f1.a$b */
    public static final class b {
        public /* synthetic */ b(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public final Typeface b(String str, int i3, AssetManager assetManager) {
            if (assetManager != null) {
                String str2 = C0534a.f9382d[i3];
                for (String str3 : C0534a.f9383e) {
                    try {
                        Typeface typefaceCreateFromAsset = Typeface.createFromAsset(assetManager, "fonts/" + str + str2 + str3);
                        h.e(typefaceCreateFromAsset, "createFromAsset(...)");
                        return typefaceCreateFromAsset;
                    } catch (RuntimeException unused) {
                    }
                }
            }
            Typeface typefaceCreate = Typeface.create(str, i3);
            h.e(typefaceCreate, "create(...)");
            return typefaceCreate;
        }

        public final C0534a c() {
            return C0534a.f9384f;
        }

        private b() {
        }
    }

    /* JADX INFO: renamed from: f1.a$c */
    public static final class c {

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        public static final C0128a f9388c = new C0128a(null);

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        private final boolean f9389a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        private final int f9390b;

        /* JADX INFO: renamed from: f1.a$c$a, reason: collision with other inner class name */
        public static final class C0128a {
            public /* synthetic */ C0128a(DefaultConstructorMarker defaultConstructorMarker) {
                this();
            }

            private C0128a() {
            }
        }

        public c(int i3) {
            this(i3, 0, 2, null);
        }

        public final Typeface a(Typeface typeface) {
            if (Build.VERSION.SDK_INT < 28) {
                Typeface typefaceCreate = Typeface.create(typeface, b());
                h.c(typefaceCreate);
                return typefaceCreate;
            }
            Typeface typefaceCreate2 = Typeface.create(typeface, this.f9390b, this.f9389a);
            h.c(typefaceCreate2);
            return typefaceCreate2;
        }

        public final int b() {
            return this.f9390b < 700 ? this.f9389a ? 2 : 0 : this.f9389a ? 3 : 1;
        }

        public c(int i3, boolean z3) {
            this.f9389a = z3;
            this.f9390b = i3 == -1 ? 400 : i3;
        }

        public /* synthetic */ c(int i3, int i4, int i5, DefaultConstructorMarker defaultConstructorMarker) {
            this(i3, (i5 & 2) != 0 ? -1 : i4);
        }

        public c(int i3, int i4) {
            i3 = i3 == -1 ? 0 : i3;
            this.f9389a = (i3 & 2) != 0;
            this.f9390b = i4 == -1 ? (i3 & 1) != 0 ? 700 : 400 : i4;
        }
    }

    public final Typeface d(String str, int i3, AssetManager assetManager) {
        h.f(str, "fontFamilyName");
        return e(str, new c(i3, 0, 2, null), assetManager);
    }

    public final Typeface e(String str, c cVar, AssetManager assetManager) {
        h.f(str, "fontFamilyName");
        h.f(cVar, "typefaceStyle");
        if (this.f9386b.containsKey(str)) {
            return cVar.a((Typeface) this.f9386b.get(str));
        }
        Map map = this.f9385a;
        Object c0127a = map.get(str);
        if (c0127a == null) {
            c0127a = new C0127a();
            map.put(str, c0127a);
        }
        C0127a c0127a2 = (C0127a) c0127a;
        int iB = cVar.b();
        Typeface typefaceA = c0127a2.a(iB);
        if (typefaceA != null) {
            return typefaceA;
        }
        Typeface typefaceB = f9381c.b(str, iB, assetManager);
        c0127a2.b(iB, typefaceB);
        return typefaceB;
    }
}
