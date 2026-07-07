package c2;

import com.facebook.soloader.E;
import com.facebook.soloader.InterfaceC0481b;
import com.facebook.soloader.m;
import com.facebook.soloader.p;

/* JADX INFO: renamed from: c2.l, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public class C0340l implements InterfaceC0336h {
    /* JADX WARN: Multi-variable type inference failed */
    @Override // c2.InterfaceC0336h
    public boolean a(UnsatisfiedLinkError unsatisfiedLinkError, E[] eArr) {
        for (m mVar : eArr) {
            if (mVar instanceof InterfaceC0481b) {
                p.b("SoLoader", "Waiting on SoSource " + mVar.c());
                mVar.b();
            }
        }
        return true;
    }
}
