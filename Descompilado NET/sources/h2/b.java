package H2;

import java.util.NoSuchElementException;
import s2.AbstractC0695C;

/* JADX INFO: loaded from: classes.dex */
public final class b extends AbstractC0695C {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final int f372b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final int f373c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private boolean f374d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private int f375e;

    public b(int i3, int i4, int i5) {
        this.f372b = i5;
        this.f373c = i4;
        boolean z3 = false;
        if (i5 <= 0 ? i3 >= i4 : i3 <= i4) {
            z3 = true;
        }
        this.f374d = z3;
        this.f375e = z3 ? i3 : i4;
    }

    @Override // s2.AbstractC0695C
    public int a() {
        int i3 = this.f375e;
        if (i3 != this.f373c) {
            this.f375e = this.f372b + i3;
        } else {
            if (!this.f374d) {
                throw new NoSuchElementException();
            }
            this.f374d = false;
        }
        return i3;
    }

    @Override // java.util.Iterator
    public boolean hasNext() {
        return this.f374d;
    }
}
