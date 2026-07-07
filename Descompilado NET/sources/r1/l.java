package R1;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.Shader;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import kotlin.enums.EnumEntries;
import r2.C0685h;
import w2.AbstractC0764a;

/* JADX INFO: loaded from: classes.dex */
public final class l {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final a f2054a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final m f2055b;

    /* JADX WARN: Failed to restore enum class, 'enum' modifier and super class removed */
    /* JADX WARN: Unknown enum class pattern. Please report as an issue! */
    private static final class a {

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        public static final a f2056b = new a("LINEAR_GRADIENT", 0);

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        private static final /* synthetic */ a[] f2057c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        private static final /* synthetic */ EnumEntries f2058d;

        static {
            a[] aVarArrA = a();
            f2057c = aVarArrA;
            f2058d = AbstractC0764a.a(aVarArrA);
        }

        private a(String str, int i3) {
        }

        private static final /* synthetic */ a[] a() {
            return new a[]{f2056b};
        }

        public static a valueOf(String str) {
            return (a) Enum.valueOf(a.class, str);
        }

        public static a[] values() {
            return (a[]) f2057c.clone();
        }
    }

    public /* synthetic */ class b {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public static final /* synthetic */ int[] f2059a;

        static {
            int[] iArr = new int[a.values().length];
            try {
                iArr[a.f2056b.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            f2059a = iArr;
        }
    }

    public l(ReadableMap readableMap, Context context) {
        D2.h.f(context, "context");
        if (readableMap == null) {
            throw new IllegalArgumentException("Gradient cannot be null");
        }
        String string = readableMap.getString("type");
        if (!D2.h.b(string, "linearGradient")) {
            throw new IllegalArgumentException("Unsupported gradient type: " + string);
        }
        this.f2054a = a.f2056b;
        ReadableMap map = readableMap.getMap("direction");
        if (map == null) {
            throw new IllegalArgumentException("Gradient must have direction");
        }
        ReadableArray array = readableMap.getArray("colorStops");
        if (array == null) {
            throw new IllegalArgumentException("Invalid colorStops array");
        }
        this.f2055b = new m(map, array, context);
    }

    public final Shader a(Rect rect) {
        D2.h.f(rect, "bounds");
        if (b.f2059a[this.f2054a.ordinal()] == 1) {
            return this.f2055b.d(rect.width(), rect.height());
        }
        throw new C0685h();
    }
}
