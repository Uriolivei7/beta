package androidx.core.view;

import android.view.View;
import android.view.ViewGroup;
import java.util.Iterator;

/* JADX INFO: renamed from: androidx.core.view.f0, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public abstract class AbstractC0248f0 {

    /* JADX INFO: renamed from: androidx.core.view.f0$a */
    public static final class a implements J2.c {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        final /* synthetic */ ViewGroup f4616a;

        a(ViewGroup viewGroup) {
            this.f4616a = viewGroup;
        }

        @Override // J2.c
        public Iterator iterator() {
            return AbstractC0248f0.b(this.f4616a);
        }
    }

    /* JADX INFO: renamed from: androidx.core.view.f0$b */
    public static final class b implements Iterator, E2.a {

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        private int f4617b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        final /* synthetic */ ViewGroup f4618c;

        b(ViewGroup viewGroup) {
            this.f4618c = viewGroup;
        }

        @Override // java.util.Iterator
        /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
        public View next() {
            ViewGroup viewGroup = this.f4618c;
            int i3 = this.f4617b;
            this.f4617b = i3 + 1;
            View childAt = viewGroup.getChildAt(i3);
            if (childAt != null) {
                return childAt;
            }
            throw new IndexOutOfBoundsException();
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.f4617b < this.f4618c.getChildCount();
        }

        @Override // java.util.Iterator
        public void remove() {
            ViewGroup viewGroup = this.f4618c;
            int i3 = this.f4617b - 1;
            this.f4617b = i3;
            viewGroup.removeViewAt(i3);
        }
    }

    public static final J2.c a(ViewGroup viewGroup) {
        return new a(viewGroup);
    }

    public static final Iterator b(ViewGroup viewGroup) {
        return new b(viewGroup);
    }
}
