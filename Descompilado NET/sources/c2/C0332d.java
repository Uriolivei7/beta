package c2;

import com.facebook.soloader.C;
import com.facebook.soloader.C0482c;
import com.facebook.soloader.E;
import com.facebook.soloader.G;
import com.facebook.soloader.p;

/* JADX INFO: renamed from: c2.d, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public class C0332d implements InterfaceC0336h {
    @Override // c2.InterfaceC0336h
    public boolean a(UnsatisfiedLinkError unsatisfiedLinkError, E[] eArr) {
        if (!(unsatisfiedLinkError instanceof C)) {
            return false;
        }
        p.b("SoLoader", "Checking /data/data missing libraries.");
        boolean z3 = false;
        for (E e4 : eArr) {
            if ((e4 instanceof G) && !(e4 instanceof C0482c)) {
                G g3 = (G) e4;
                try {
                    G.c[] cVarArrO = g3.o();
                    int length = cVarArrO.length;
                    int i3 = 0;
                    while (true) {
                        if (i3 < length) {
                            G.c cVar = cVarArrO[i3];
                            if (g3.f(cVar.f8199b) == null) {
                                p.b("SoLoader", "Missing " + cVar.f8199b + " from " + g3.c() + ", will force prepare.");
                                g3.e(2);
                                z3 = true;
                                break;
                            }
                            i3++;
                        }
                    }
                } catch (Exception e5) {
                    p.c("SoLoader", "Encountered an exception while recovering from /data/data failure ", e5);
                    return false;
                }
            }
        }
        if (z3) {
            p.b("SoLoader", "Successfully recovered from /data/data disk failure.");
            return true;
        }
        p.b("SoLoader", "No libraries missing from unpacking so paths while recovering /data/data failure");
        return false;
    }
}
