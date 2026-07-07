package s2;

import java.util.AbstractCollection;
import java.util.Collection;

/* JADX INFO: renamed from: s2.c, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public abstract class AbstractC0706c extends AbstractCollection implements Collection, E2.b {
    protected AbstractC0706c() {
    }

    public abstract int a();

    @Override // java.util.AbstractCollection, java.util.Collection
    public final /* bridge */ int size() {
        return a();
    }
}
