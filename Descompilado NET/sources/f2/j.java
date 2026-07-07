package f2;

import android.view.View;
import f2.h;

/* JADX INFO: loaded from: classes.dex */
final class j implements View.OnClickListener {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final h f9410b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final h.a f9411c;

    j(h hVar, h.a aVar) {
        this.f9410b = hVar;
        this.f9411c = aVar;
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        h.a aVar = this.f9411c;
        if (aVar == null) {
            return;
        }
        aVar.a(this.f9410b, view);
    }
}
