package C;

import androidx.fragment.app.Fragment;
import kotlin.jvm.internal.DefaultConstructorMarker;

/* JADX INFO: loaded from: classes.dex */
public abstract class g extends RuntimeException {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final Fragment f107b;

    public /* synthetic */ g(Fragment fragment, String str, int i3, DefaultConstructorMarker defaultConstructorMarker) {
        this(fragment, (i3 & 2) != 0 ? null : str);
    }

    public final Fragment a() {
        return this.f107b;
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public g(Fragment fragment, String str) {
        super(str);
        D2.h.f(fragment, "fragment");
        this.f107b = fragment;
    }
}
