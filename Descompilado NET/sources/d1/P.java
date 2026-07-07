package d1;

import com.facebook.react.bridge.ModuleHolder;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public final class P {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static final P f9152a = new P();

    public static final class a implements Iterable, E2.a {

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        final /* synthetic */ List f9153b;

        public a(List list) {
            this.f9153b = list;
        }

        @Override // java.lang.Iterable
        public Iterator iterator() {
            return new b(this.f9153b);
        }
    }

    public static final class b implements Iterator, E2.a {

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        private int f9154b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        final /* synthetic */ List f9155c;

        b(List list) {
            this.f9155c = list;
        }

        @Override // java.util.Iterator
        /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
        public ModuleHolder next() {
            List list = this.f9155c;
            int i3 = this.f9154b;
            this.f9154b = i3 + 1;
            return new ModuleHolder((NativeModule) list.get(i3));
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.f9154b < this.f9155c.size();
        }

        @Override // java.util.Iterator
        public void remove() {
            throw new UnsupportedOperationException("Operation is not supported for read-only collection");
        }
    }

    private P() {
    }

    public final Iterable a(O o3, ReactApplicationContext reactApplicationContext) {
        D2.h.f(o3, "reactPackage");
        D2.h.f(reactApplicationContext, "reactApplicationContext");
        Y.a.b("ReactNative", o3.getClass().getSimpleName() + " is not a LazyReactPackage, falling back to old version.");
        return new a(o3.e(reactApplicationContext));
    }
}
