package X;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class f extends ArrayList {
    private f(int i3) {
        super(i3);
    }

    public static f b(List list) {
        return new f(list);
    }

    public static f c(Object... objArr) {
        f fVar = new f(objArr.length);
        Collections.addAll(fVar, objArr);
        return fVar;
    }

    private f(List list) {
        super(list);
    }
}
