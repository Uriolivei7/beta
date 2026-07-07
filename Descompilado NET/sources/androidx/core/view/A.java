package androidx.core.view;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

/* JADX INFO: loaded from: classes.dex */
public class A {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final Runnable f4540a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final CopyOnWriteArrayList f4541b = new CopyOnWriteArrayList();

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final Map f4542c = new HashMap();

    public A(Runnable runnable) {
        this.f4540a = runnable;
    }

    public void a(C c4) {
        this.f4541b.add(c4);
        this.f4540a.run();
    }

    public void b(Menu menu, MenuInflater menuInflater) {
        Iterator it = this.f4541b.iterator();
        while (it.hasNext()) {
            ((C) it.next()).c(menu, menuInflater);
        }
    }

    public void c(Menu menu) {
        Iterator it = this.f4541b.iterator();
        while (it.hasNext()) {
            ((C) it.next()).b(menu);
        }
    }

    public boolean d(MenuItem menuItem) {
        Iterator it = this.f4541b.iterator();
        while (it.hasNext()) {
            if (((C) it.next()).a(menuItem)) {
                return true;
            }
        }
        return false;
    }

    public void e(Menu menu) {
        Iterator it = this.f4541b.iterator();
        while (it.hasNext()) {
            ((C) it.next()).d(menu);
        }
    }

    public void f(C c4) {
        this.f4541b.remove(c4);
        androidx.activity.result.d.a(this.f4542c.remove(c4));
        this.f4540a.run();
    }
}
