package U2;

import kotlin.jvm.internal.DefaultConstructorMarker;

/* JADX INFO: loaded from: classes.dex */
public enum b {
    NO_ERROR(0),
    PROTOCOL_ERROR(1),
    INTERNAL_ERROR(2),
    FLOW_CONTROL_ERROR(3),
    SETTINGS_TIMEOUT(4),
    STREAM_CLOSED(5),
    FRAME_SIZE_ERROR(6),
    REFUSED_STREAM(7),
    CANCEL(8),
    COMPRESSION_ERROR(9),
    CONNECT_ERROR(10),
    ENHANCE_YOUR_CALM(11),
    INADEQUATE_SECURITY(12),
    HTTP_1_1_REQUIRED(13);


    /* JADX INFO: renamed from: r, reason: collision with root package name */
    public static final a f2452r = new a(null);

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final int f2453b;

    public static final class a {
        private a() {
        }

        public final b a(int i3) {
            for (b bVar : b.values()) {
                if (bVar.a() == i3) {
                    return bVar;
                }
            }
            return null;
        }

        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }

    b(int i3) {
        this.f2453b = i3;
    }

    public final int a() {
        return this.f2453b;
    }
}
