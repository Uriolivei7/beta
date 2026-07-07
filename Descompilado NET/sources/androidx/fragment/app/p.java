package androidx.fragment.app;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import java.io.FileDescriptor;
import java.io.PrintWriter;

/* JADX INFO: loaded from: classes.dex */
public abstract class p extends AbstractC0290l {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final Activity f5168b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final Context f5169c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private final Handler f5170d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private final int f5171e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    final x f5172f;

    public p(Context context, Handler handler, int i3) {
        this(context instanceof Activity ? (Activity) context : null, context, handler, i3);
    }

    @Override // androidx.fragment.app.AbstractC0290l
    public View f(int i3) {
        return null;
    }

    @Override // androidx.fragment.app.AbstractC0290l
    public boolean h() {
        return true;
    }

    Activity j() {
        return this.f5168b;
    }

    Context k() {
        return this.f5169c;
    }

    public Handler m() {
        return this.f5170d;
    }

    public void p(String str, FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
    }

    public abstract Object w();

    public LayoutInflater y() {
        return LayoutInflater.from(this.f5169c);
    }

    public void z() {
    }

    p(ActivityC0288j activityC0288j) {
        this(activityC0288j, activityC0288j, new Handler(), 0);
    }

    p(Activity activity, Context context, Handler handler, int i3) {
        this.f5172f = new y();
        this.f5168b = activity;
        this.f5169c = (Context) q.g.h(context, "context == null");
        this.f5170d = (Handler) q.g.h(handler, "handler == null");
        this.f5171e = i3;
    }
}
