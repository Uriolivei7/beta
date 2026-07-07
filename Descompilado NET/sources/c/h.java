package C;

import android.view.ViewGroup;
import androidx.fragment.app.Fragment;

/* JADX INFO: loaded from: classes.dex */
public final class h extends g {

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final ViewGroup f108c;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public h(Fragment fragment, ViewGroup viewGroup) {
        super(fragment, "Attempting to add fragment " + fragment + " to container " + viewGroup + " which is not a FragmentContainerView");
        D2.h.f(fragment, "fragment");
        D2.h.f(viewGroup, "container");
        this.f108c = viewGroup;
    }
}
