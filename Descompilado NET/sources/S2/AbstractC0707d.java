package s2;

import java.util.AbstractList;
import java.util.List;

/* JADX INFO: renamed from: s2.d, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public abstract class AbstractC0707d extends AbstractList implements List, E2.c {
    protected AbstractC0707d() {
    }

    public abstract int a();

    public abstract Object b(int i3);

    @Override // java.util.AbstractList, java.util.List
    public final /* bridge */ Object remove(int i3) {
        return b(i3);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public final /* bridge */ int size() {
        return a();
    }
}
