package androidx.lifecycle;

import android.os.Bundle;
import androidx.lifecycle.AbstractC0299g;
import androidx.lifecycle.LegacySavedStateHandleController;
import androidx.savedstate.a;
import java.util.Iterator;

/* JADX INFO: loaded from: classes.dex */
public final class LegacySavedStateHandleController {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static final LegacySavedStateHandleController f5282a = new LegacySavedStateHandleController();

    public static final class a implements a.InterfaceC0083a {
        @Override // androidx.savedstate.a.InterfaceC0083a
        public void a(G.d dVar) {
            D2.h.f(dVar, "owner");
            if (!(dVar instanceof H)) {
                throw new IllegalStateException("Internal error: OnRecreation should be registered only on components that implement ViewModelStoreOwner");
            }
            G gS = ((H) dVar).s();
            androidx.savedstate.a aVarB = dVar.b();
            Iterator it = gS.c().iterator();
            while (it.hasNext()) {
                D dB = gS.b((String) it.next());
                D2.h.c(dB);
                LegacySavedStateHandleController.a(dB, aVarB, dVar.t());
            }
            if (gS.c().isEmpty()) {
                return;
            }
            aVarB.i(a.class);
        }
    }

    private LegacySavedStateHandleController() {
    }

    public static final void a(D d4, androidx.savedstate.a aVar, AbstractC0299g abstractC0299g) {
        D2.h.f(d4, "viewModel");
        D2.h.f(aVar, "registry");
        D2.h.f(abstractC0299g, "lifecycle");
        SavedStateHandleController savedStateHandleController = (SavedStateHandleController) d4.c("androidx.lifecycle.savedstate.vm.tag");
        if (savedStateHandleController == null || savedStateHandleController.j()) {
            return;
        }
        savedStateHandleController.h(aVar, abstractC0299g);
        f5282a.c(aVar, abstractC0299g);
    }

    public static final SavedStateHandleController b(androidx.savedstate.a aVar, AbstractC0299g abstractC0299g, String str, Bundle bundle) {
        D2.h.f(aVar, "registry");
        D2.h.f(abstractC0299g, "lifecycle");
        D2.h.c(str);
        SavedStateHandleController savedStateHandleController = new SavedStateHandleController(str, x.f5361f.a(aVar.b(str), bundle));
        savedStateHandleController.h(aVar, abstractC0299g);
        f5282a.c(aVar, abstractC0299g);
        return savedStateHandleController;
    }

    private final void c(final androidx.savedstate.a aVar, final AbstractC0299g abstractC0299g) {
        AbstractC0299g.b bVarB = abstractC0299g.b();
        if (bVarB == AbstractC0299g.b.INITIALIZED || bVarB.b(AbstractC0299g.b.STARTED)) {
            aVar.i(a.class);
        } else {
            abstractC0299g.a(new InterfaceC0302j() { // from class: androidx.lifecycle.LegacySavedStateHandleController$tryToAddRecreator$1
                @Override // androidx.lifecycle.InterfaceC0302j
                public void d(l lVar, AbstractC0299g.a aVar2) {
                    D2.h.f(lVar, "source");
                    D2.h.f(aVar2, "event");
                    if (aVar2 == AbstractC0299g.a.ON_START) {
                        abstractC0299g.c(this);
                        aVar.i(LegacySavedStateHandleController.a.class);
                    }
                }
            });
        }
    }
}
