package K1;

import D2.h;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.ViewParent;
import kotlin.jvm.internal.DefaultConstructorMarker;

/* JADX INFO: loaded from: classes.dex */
public final class a implements b {

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private static final C0011a f812c = new C0011a(null);

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private volatile int f813a = -1;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private ViewParent f814b;

    /* JADX INFO: renamed from: K1.a$a, reason: collision with other inner class name */
    private static final class C0011a {
        public /* synthetic */ C0011a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private C0011a() {
        }
    }

    private final void c() {
        ViewParent viewParent = this.f814b;
        if (viewParent != null) {
            viewParent.requestDisallowInterceptTouchEvent(false);
        }
        this.f814b = null;
    }

    @Override // K1.b
    public boolean a(ViewGroup viewGroup, MotionEvent motionEvent) {
        h.f(viewGroup, "view");
        h.f(motionEvent, "event");
        int i3 = this.f813a;
        return (i3 == -1 || motionEvent.getAction() == 1 || viewGroup.getId() != i3) ? false : true;
    }

    public final void b() {
        this.f813a = -1;
        c();
    }

    public final void d(int i3, ViewParent viewParent) {
        this.f813a = i3;
        c();
        if (viewParent != null) {
            viewParent.requestDisallowInterceptTouchEvent(true);
            this.f814b = viewParent;
        }
    }
}
