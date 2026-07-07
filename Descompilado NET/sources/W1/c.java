package W1;

import D2.h;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;
import kotlin.jvm.internal.DefaultConstructorMarker;

/* JADX INFO: loaded from: classes.dex */
public final class c extends P1.d {

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    public static final a f2696i = new a(null);

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private final float f2697h;

    public static final class a {
        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private a() {
        }
    }

    public c(int i3, float f3) {
        this(-1, i3, f3);
    }

    @Override // P1.d
    protected WritableMap j() {
        WritableMap writableMapCreateMap = Arguments.createMap();
        h.e(writableMapCreateMap, "createMap(...)");
        writableMapCreateMap.putDouble("offset", u());
        return writableMapCreateMap;
    }

    @Override // P1.d
    public String k() {
        return "topDrawerSlide";
    }

    public final float u() {
        return this.f2697h;
    }

    public c(int i3, int i4, float f3) {
        super(i3, i4);
        this.f2697h = f3;
    }
}
