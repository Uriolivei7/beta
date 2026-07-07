package W1;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;
import kotlin.jvm.internal.DefaultConstructorMarker;

/* JADX INFO: loaded from: classes.dex */
public final class a extends P1.d {

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    public static final C0041a f2694h = new C0041a(null);

    /* JADX INFO: renamed from: W1.a$a, reason: collision with other inner class name */
    public static final class C0041a {
        public /* synthetic */ C0041a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private C0041a() {
        }
    }

    public a(int i3) {
        this(-1, i3);
    }

    @Override // P1.d
    protected WritableMap j() {
        return Arguments.createMap();
    }

    @Override // P1.d
    public String k() {
        return "topDrawerClose";
    }

    public a(int i3, int i4) {
        super(i3, i4);
    }
}
