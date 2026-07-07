package M0;

import O0.n;
import O0.o;
import X.k;
import java.util.Collections;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class g implements e {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final b f886a;

    private static class a implements b {
        @Override // M0.g.b
        public int a() {
            return 0;
        }

        @Override // M0.g.b
        public List b() {
            return Collections.EMPTY_LIST;
        }

        private a() {
        }
    }

    public interface b {
        int a();

        List b();
    }

    public g() {
        this(new a());
    }

    @Override // M0.e
    public int a(int i3) {
        List listB = this.f886a.b();
        if (listB == null || listB.isEmpty()) {
            return i3 + 1;
        }
        for (int i4 = 0; i4 < listB.size(); i4++) {
            if (((Integer) listB.get(i4)).intValue() > i3) {
                return ((Integer) listB.get(i4)).intValue();
            }
        }
        return Integer.MAX_VALUE;
    }

    @Override // M0.e
    public o b(int i3) {
        return n.d(i3, i3 >= this.f886a.a(), false);
    }

    @Override // M0.e
    public boolean c() {
        return true;
    }

    public g(b bVar) {
        this.f886a = (b) k.g(bVar);
    }
}
