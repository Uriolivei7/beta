package C;

import androidx.fragment.app.Fragment;

/* JADX INFO: loaded from: classes.dex */
public final class a extends g {

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final String f88c;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public a(Fragment fragment, String str) {
        super(fragment, "Attempting to reuse fragment " + fragment + " with previous ID " + str);
        D2.h.f(fragment, "fragment");
        D2.h.f(str, "previousFragmentId");
        this.f88c = str;
    }
}
