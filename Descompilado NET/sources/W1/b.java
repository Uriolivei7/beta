package W1;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;
import kotlin.jvm.internal.DefaultConstructorMarker;

/* JADX INFO: loaded from: classes.dex */
public final class b extends P1.d {

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    public static final a f2695h = new a(null);

    public static final class a {
        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private a() {
        }
    }

    public b(int i3) {
        this(-1, i3);
    }

    @Override // P1.d
    protected WritableMap j() {
        return Arguments.createMap();
    }

    @Override // P1.d
    public String k() {
        return "topDrawerOpen";
    }

    public b(int i3, int i4) {
        super(i3, i4);
    }
}
