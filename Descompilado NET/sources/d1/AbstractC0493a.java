package d1;

import com.facebook.react.bridge.ModuleHolder;
import com.facebook.react.bridge.ModuleSpec;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.module.model.ReactModuleInfo;
import com.facebook.react.uimanager.ViewManager;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import javax.inject.Provider;
import r1.C0670b;
import s2.AbstractC0717n;
import w1.InterfaceC0763a;

/* JADX INFO: renamed from: d1.a, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public abstract class AbstractC0493a implements O {

    /* JADX INFO: renamed from: d1.a$a, reason: collision with other inner class name */
    private final class C0123a implements Provider {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        private final String f9162a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        private final ReactApplicationContext f9163b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        final /* synthetic */ AbstractC0493a f9164c;

        public C0123a(AbstractC0493a abstractC0493a, String str, ReactApplicationContext reactApplicationContext) {
            D2.h.f(str, "name");
            D2.h.f(reactApplicationContext, "reactContext");
            this.f9164c = abstractC0493a;
            this.f9162a = str;
            this.f9163b = reactApplicationContext;
        }

        @Override // javax.inject.Provider
        /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
        public NativeModule get() {
            return this.f9164c.g(this.f9162a, this.f9163b);
        }
    }

    /* JADX INFO: renamed from: d1.a$b */
    public static final class b implements Iterable, E2.a {

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        final /* synthetic */ Iterator f9165b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        final /* synthetic */ AbstractC0493a f9166c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        final /* synthetic */ ReactApplicationContext f9167d;

        public b(Iterator it, AbstractC0493a abstractC0493a, ReactApplicationContext reactApplicationContext) {
            this.f9165b = it;
            this.f9166c = abstractC0493a;
            this.f9167d = reactApplicationContext;
        }

        @Override // java.lang.Iterable
        public Iterator iterator() {
            return new c(this.f9165b, this.f9166c, this.f9167d);
        }
    }

    /* JADX INFO: renamed from: d1.a$c */
    public static final class c implements Iterator, E2.a {

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        private Map.Entry f9168b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        final /* synthetic */ Iterator f9169c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        final /* synthetic */ AbstractC0493a f9170d;

        /* JADX INFO: renamed from: e, reason: collision with root package name */
        final /* synthetic */ ReactApplicationContext f9171e;

        c(Iterator it, AbstractC0493a abstractC0493a, ReactApplicationContext reactApplicationContext) {
            this.f9169c = it;
            this.f9170d = abstractC0493a;
            this.f9171e = reactApplicationContext;
        }

        private final void a() {
            while (this.f9169c.hasNext()) {
                Map.Entry entry = (Map.Entry) this.f9169c.next();
                ReactModuleInfo reactModuleInfo = (ReactModuleInfo) entry.getValue();
                if (!C0670b.t() || !reactModuleInfo.e()) {
                    this.f9168b = entry;
                    return;
                }
            }
            this.f9168b = null;
        }

        @Override // java.util.Iterator
        /* JADX INFO: renamed from: b, reason: merged with bridge method [inline-methods] */
        public ModuleHolder next() {
            if (this.f9168b == null) {
                a();
            }
            Map.Entry entry = this.f9168b;
            if (entry == null) {
                throw new NoSuchElementException("ModuleHolder not found");
            }
            a();
            return new ModuleHolder((ReactModuleInfo) entry.getValue(), new C0123a(this.f9170d, (String) entry.getKey(), this.f9171e));
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            if (this.f9168b == null) {
                a();
            }
            return this.f9168b != null;
        }

        @Override // java.util.Iterator
        public void remove() {
            throw new UnsupportedOperationException("Operation is not supported for read-only collection");
        }
    }

    @Override // d1.O
    public List e(ReactApplicationContext reactApplicationContext) {
        D2.h.f(reactApplicationContext, "reactContext");
        throw new UnsupportedOperationException("createNativeModules method is not supported. Use getModule() method instead.");
    }

    @Override // d1.O
    public List f(ReactApplicationContext reactApplicationContext) {
        D2.h.f(reactApplicationContext, "reactContext");
        List listJ = j(reactApplicationContext);
        if (listJ == null || listJ.isEmpty()) {
            return AbstractC0717n.g();
        }
        ArrayList arrayList = new ArrayList();
        Iterator it = listJ.iterator();
        while (it.hasNext()) {
            Object obj = ((ModuleSpec) it.next()).getProvider().get();
            D2.h.d(obj, "null cannot be cast to non-null type com.facebook.react.uimanager.ViewManager<*, *>");
            arrayList.add((ViewManager) obj);
        }
        return arrayList;
    }

    public abstract NativeModule g(String str, ReactApplicationContext reactApplicationContext);

    public final Iterable h(ReactApplicationContext reactApplicationContext) {
        D2.h.f(reactApplicationContext, "reactContext");
        return new b(i().a().entrySet().iterator(), this, reactApplicationContext);
    }

    public abstract InterfaceC0763a i();

    protected List j(ReactApplicationContext reactApplicationContext) {
        D2.h.f(reactApplicationContext, "reactContext");
        return AbstractC0717n.g();
    }
}
