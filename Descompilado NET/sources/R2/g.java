package R2;

import java.net.Proxy;

/* JADX INFO: loaded from: classes.dex */
public abstract /* synthetic */ class g {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static final /* synthetic */ int[] f2183a;

    static {
        int[] iArr = new int[Proxy.Type.values().length];
        f2183a = iArr;
        iArr[Proxy.Type.DIRECT.ordinal()] = 1;
        iArr[Proxy.Type.HTTP.ordinal()] = 2;
    }
}
