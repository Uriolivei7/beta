package c2;

import android.content.Context;
import com.facebook.soloader.E;
import com.facebook.soloader.p;
import com.facebook.soloader.w;
import java.io.File;

/* JADX INFO: renamed from: c2.g, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public class C0335g implements InterfaceC0336h {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final Context f5696a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final C0329a f5697b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final int f5698c;

    public C0335g(Context context, C0329a c0329a) {
        this.f5696a = context;
        this.f5697b = c0329a;
        this.f5698c = c0329a.c();
    }

    private boolean b() {
        String strC = c();
        return new File(strC).exists() && this.f5697b.a(strC);
    }

    private String c() {
        return this.f5696a.getApplicationInfo().sourceDir;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void d(E[] eArr) {
        for (int i3 = 0; i3 < eArr.length; i3++) {
            Object[] objArr = eArr[i3];
            if (objArr instanceof w) {
                eArr[i3] = ((w) objArr).a(this.f5696a);
            }
        }
    }

    @Override // c2.InterfaceC0336h
    public boolean a(UnsatisfiedLinkError unsatisfiedLinkError, E[] eArr) {
        if (b()) {
            d(eArr);
            return true;
        }
        if (this.f5698c == this.f5697b.c()) {
            return false;
        }
        p.g("soloader.recovery.DetectDataAppMove", "Context was updated (perhaps by another thread)");
        return true;
    }
}
