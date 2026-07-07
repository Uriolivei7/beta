package q1;

import D2.h;
import android.view.Choreographer;
import com.facebook.react.bridge.UiThreadUtil;
import q1.InterfaceC0651b;

/* JADX INFO: renamed from: q1.a, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public final class C0650a implements InterfaceC0651b {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static final C0650a f10393a = new C0650a();

    /* JADX INFO: renamed from: q1.a$a, reason: collision with other inner class name */
    private static final class C0143a implements InterfaceC0651b.a {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        private final Choreographer f10394a;

        public C0143a() {
            Choreographer choreographer = Choreographer.getInstance();
            h.e(choreographer, "getInstance(...)");
            this.f10394a = choreographer;
        }

        @Override // q1.InterfaceC0651b.a
        public void a(Choreographer.FrameCallback frameCallback) {
            h.f(frameCallback, "callback");
            this.f10394a.postFrameCallback(frameCallback);
        }

        @Override // q1.InterfaceC0651b.a
        public void b(Choreographer.FrameCallback frameCallback) {
            h.f(frameCallback, "callback");
            this.f10394a.removeFrameCallback(frameCallback);
        }
    }

    private C0650a() {
    }

    public static final C0650a b() {
        return f10393a;
    }

    @Override // q1.InterfaceC0651b
    public InterfaceC0651b.a a() {
        UiThreadUtil.assertOnUiThread();
        return new C0143a();
    }
}
