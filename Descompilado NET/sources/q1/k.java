package Q1;

import android.view.View;
import android.view.animation.Animation;
import kotlin.jvm.internal.DefaultConstructorMarker;

/* JADX INFO: loaded from: classes.dex */
public final class k extends Q1.a {

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private static final a f1840f = new a(null);

    private static final class a {
        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private a() {
        }
    }

    @Override // Q1.a
    /* JADX INFO: renamed from: g, reason: merged with bridge method [inline-methods] */
    public Animation b(View view, int i3, int i4, int i5, int i6) {
        D2.h.f(view, "view");
        boolean z3 = true;
        boolean z4 = (((int) view.getX()) == i3 && ((int) view.getY()) == i4) ? false : true;
        if (view.getWidth() == i5 && view.getHeight() == i6) {
            z3 = false;
        }
        if (z4 || z3) {
            return new m(view, i3, i4, i5, i6);
        }
        return null;
    }

    @Override // Q1.a
    /* JADX INFO: renamed from: h, reason: merged with bridge method [inline-methods] */
    public boolean e() {
        return this.f1803d > 0;
    }
}
