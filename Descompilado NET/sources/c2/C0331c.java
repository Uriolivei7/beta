package c2;

import android.content.Context;
import com.facebook.soloader.C;
import com.facebook.soloader.C0482c;
import com.facebook.soloader.C0485f;
import com.facebook.soloader.E;
import com.facebook.soloader.G;
import com.facebook.soloader.p;
import java.io.File;
import java.util.ArrayList;

/* JADX INFO: renamed from: c2.c, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public class C0331c implements InterfaceC0336h {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final Context f5690a;

    public C0331c(Context context) {
        this.f5690a = context;
    }

    @Override // c2.InterfaceC0336h
    public boolean a(UnsatisfiedLinkError unsatisfiedLinkError, E[] eArr) {
        if (!(unsatisfiedLinkError instanceof C)) {
            return false;
        }
        p.b("SoLoader", "Checking /data/app missing libraries.");
        File file = new File(this.f5690a.getApplicationInfo().nativeLibraryDir);
        if (!file.exists()) {
            p.b("SoLoader", "Native library directory " + file + " does not exist, exiting /data/app recovery.");
            return false;
        }
        ArrayList arrayList = new ArrayList();
        int length = eArr.length;
        int i3 = 0;
        while (true) {
            if (i3 >= length) {
                break;
            }
            E e4 = eArr[i3];
            if (e4 instanceof C0482c) {
                C0482c c0482c = (C0482c) e4;
                try {
                    for (G.c cVar : c0482c.o()) {
                        if (!new File(file, cVar.f8199b).exists()) {
                            arrayList.add(cVar.f8199b);
                        }
                    }
                    if (arrayList.isEmpty()) {
                        p.b("SoLoader", "No libraries missing from " + file);
                        return false;
                    }
                    p.b("SoLoader", "Missing libraries from " + file + ": " + arrayList.toString() + ", will run prepare on tbe backup so source");
                    c0482c.e(0);
                } catch (Exception e5) {
                    p.c("SoLoader", "Encountered an exception while recovering from /data/app failure ", e5);
                    return false;
                }
            } else {
                i3++;
            }
        }
        for (E e6 : eArr) {
            if ((e6 instanceof C0485f) && !(e6 instanceof C0482c)) {
                ((C0485f) e6).h();
            }
        }
        p.b("SoLoader", "Successfully recovered from /data/app disk failure.");
        return true;
    }
}
