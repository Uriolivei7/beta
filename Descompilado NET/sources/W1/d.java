package W1;

import D2.h;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;
import kotlin.jvm.internal.DefaultConstructorMarker;

/* JADX INFO: loaded from: classes.dex */
public final class d extends P1.d {

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    public static final a f2698i = new a(null);

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private final int f2699h;

    public static final class a {
        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private a() {
        }
    }

    public d(int i3, int i4) {
        this(-1, i3, i4);
    }

    @Override // P1.d
    protected WritableMap j() {
        WritableMap writableMapCreateMap = Arguments.createMap();
        h.e(writableMapCreateMap, "createMap(...)");
        writableMapCreateMap.putInt("drawerState", u());
        return writableMapCreateMap;
    }

    @Override // P1.d
    public String k() {
        return "topDrawerStateChanged";
    }

    public final int u() {
        return this.f2699h;
    }

    public d(int i3, int i4, int i5) {
        super(i3, i4);
        this.f2699h = i5;
    }
}
