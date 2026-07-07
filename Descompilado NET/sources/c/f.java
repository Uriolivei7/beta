package C;

import androidx.fragment.app.Fragment;
import kotlin.jvm.internal.DefaultConstructorMarker;

/* JADX INFO: loaded from: classes.dex */
public abstract class f extends g {
    public /* synthetic */ f(Fragment fragment, String str, int i3, DefaultConstructorMarker defaultConstructorMarker) {
        this(fragment, (i3 & 2) != 0 ? null : str);
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public f(Fragment fragment, String str) {
        super(fragment, str);
        D2.h.f(fragment, "fragment");
    }
}
