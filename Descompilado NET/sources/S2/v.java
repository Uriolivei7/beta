package s2;

import java.util.List;

/* JADX INFO: Access modifiers changed from: package-private */
/* JADX INFO: loaded from: classes.dex */
public class v extends u {
    public static List B(List list) {
        D2.h.f(list, "<this>");
        return new C0703K(list);
    }

    public static List C(List list) {
        D2.h.f(list, "<this>");
        return new C0702J(list);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final int D(List list, int i3) {
        if (i3 >= 0 && i3 <= AbstractC0717n.i(list)) {
            return AbstractC0717n.i(list) - i3;
        }
        throw new IndexOutOfBoundsException("Element index " + i3 + " must be in range [" + new H2.c(0, AbstractC0717n.i(list)) + "].");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final int E(List list, int i3) {
        return AbstractC0717n.i(list) - i3;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final int F(List list, int i3) {
        if (i3 >= 0 && i3 <= list.size()) {
            return list.size() - i3;
        }
        throw new IndexOutOfBoundsException("Position index " + i3 + " must be in range [" + new H2.c(0, list.size()) + "].");
    }
}
