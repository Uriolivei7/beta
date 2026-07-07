package c2;

import android.content.Context;
import com.facebook.soloader.E;
import com.facebook.soloader.p;
import com.facebook.soloader.v;
import java.io.File;

/* JADX INFO: renamed from: c2.b, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public class C0330b implements InterfaceC0336h {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final Context f5688a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final C0329a f5689b;

    public C0330b(Context context, C0329a c0329a) {
        this.f5688a = context;
        this.f5689b = c0329a;
    }

    @Override // c2.InterfaceC0336h
    public boolean a(UnsatisfiedLinkError unsatisfiedLinkError, E[] eArr) {
        String str = this.f5688a.getApplicationInfo().sourceDir;
        if (new File(str).exists()) {
            p.g("soloader.recovery.CheckBaseApkExists", "Base apk exists: " + str);
            return false;
        }
        StringBuilder sb = new StringBuilder("Base apk does not exist: ");
        sb.append(str);
        sb.append(". ");
        this.f5689b.b(sb);
        throw new v(sb.toString(), unsatisfiedLinkError);
    }
}
