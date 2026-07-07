package u1;

import kotlin.jvm.internal.DefaultConstructorMarker;

/* JADX INFO: renamed from: u1.h, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public final class C0745h implements InterfaceC0744g {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static final a f10867a = new a(null);

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public static final C0745h f10868b = new C0745h();

    /* JADX INFO: renamed from: u1.h$a */
    public static final class a {
        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private a() {
        }
    }

    private C0745h() {
    }

    @Override // u1.InterfaceC0744g
    public boolean a() {
        return false;
    }

    @Override // u1.InterfaceC0744g
    public int b() {
        throw new IllegalStateException("Should not retrieve delay as canRetry is: " + a());
    }

    @Override // u1.InterfaceC0744g
    public InterfaceC0744g c() {
        throw new IllegalStateException("Should not update as canRetry is: " + a());
    }

    @Override // u1.InterfaceC0744g
    public InterfaceC0744g copy() {
        return this;
    }
}
