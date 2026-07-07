package f2;

import android.content.ComponentCallbacks;
import android.content.Context;
import android.content.res.Configuration;

/* JADX INFO: loaded from: classes.dex */
final class i implements ComponentCallbacks {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private int f9408b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private a f9409c;

    interface a {
        void a(int i3);
    }

    public i(Configuration configuration) {
        this.f9408b = configuration.orientation;
    }

    void a(Context context) {
        context.getApplicationContext().unregisterComponentCallbacks(this);
        this.f9409c = null;
    }

    @Override // android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration configuration) {
        int i3 = this.f9408b;
        int i4 = configuration.orientation;
        if (i3 == i4) {
            return;
        }
        this.f9408b = i4;
        a aVar = this.f9409c;
        if (aVar == null) {
            return;
        }
        aVar.a(i4);
    }

    @Override // android.content.ComponentCallbacks
    public void onLowMemory() {
    }
}
