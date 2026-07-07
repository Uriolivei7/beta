package c2;

import com.facebook.soloader.E;

/* JADX INFO: renamed from: c2.e, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public class C0333e implements InterfaceC0336h {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final InterfaceC0336h[] f5691a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private int f5692b = 0;

    public C0333e(InterfaceC0336h... interfaceC0336hArr) {
        this.f5691a = interfaceC0336hArr;
    }

    @Override // c2.InterfaceC0336h
    public boolean a(UnsatisfiedLinkError unsatisfiedLinkError, E[] eArr) {
        int i3;
        InterfaceC0336h[] interfaceC0336hArr;
        do {
            i3 = this.f5692b;
            interfaceC0336hArr = this.f5691a;
            if (i3 >= interfaceC0336hArr.length) {
                return false;
            }
            this.f5692b = i3 + 1;
        } while (!interfaceC0336hArr[i3].a(unsatisfiedLinkError, eArr));
        return true;
    }
}
