package l1;

import D2.h;
import androidx.activity.result.d;
import com.facebook.react.bridge.UIManager;
import com.facebook.react.bridge.UIManagerListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import m1.InterfaceC0606a;
import m1.b;

/* JADX INFO: renamed from: l1.a, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public final class C0591a implements UIManagerListener {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final List f9698b = new ArrayList();

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final List f9699c = new ArrayList();

    public final synchronized void a(InterfaceC0606a interfaceC0606a) {
        h.f(interfaceC0606a, "block");
        this.f9699c.add(interfaceC0606a);
    }

    public final synchronized void b(InterfaceC0606a interfaceC0606a) {
        h.f(interfaceC0606a, "block");
        this.f9698b.add(interfaceC0606a);
    }

    @Override // com.facebook.react.bridge.UIManagerListener
    public void didDispatchMountItems(UIManager uIManager) {
        h.f(uIManager, "uiManager");
        didMountItems(uIManager);
    }

    @Override // com.facebook.react.bridge.UIManagerListener
    public void didMountItems(UIManager uIManager) {
        h.f(uIManager, "uiManager");
        if (this.f9699c.isEmpty()) {
            return;
        }
        Iterator it = this.f9699c.iterator();
        while (it.hasNext()) {
            d.a(it.next());
            if (uIManager instanceof b) {
                throw null;
            }
        }
        this.f9699c.clear();
    }

    @Override // com.facebook.react.bridge.UIManagerListener
    public void didScheduleMountItems(UIManager uIManager) {
        h.f(uIManager, "uiManager");
    }

    @Override // com.facebook.react.bridge.UIManagerListener
    public void willDispatchViewUpdates(UIManager uIManager) {
        h.f(uIManager, "uiManager");
        willMountItems(uIManager);
    }

    @Override // com.facebook.react.bridge.UIManagerListener
    public void willMountItems(UIManager uIManager) {
        h.f(uIManager, "uiManager");
        if (this.f9698b.isEmpty()) {
            return;
        }
        Iterator it = this.f9698b.iterator();
        while (it.hasNext()) {
            d.a(it.next());
            if (uIManager instanceof b) {
                throw null;
            }
        }
        this.f9698b.clear();
    }
}
