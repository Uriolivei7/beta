package d1;

import android.app.Application;
import i2.C0563c;
import j2.C0575e;
import java.util.ArrayList;
import java.util.Arrays;
import m2.C0611e;

/* JADX INFO: renamed from: d1.k, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public class C0503k {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private Application f9219a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private N f9220b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private J1.a f9221c;

    public C0503k(N n3) {
        this(n3, (J1.a) null);
    }

    public ArrayList a() {
        return new ArrayList(Arrays.asList(new J1.t(this.f9221c), new d3.a(), new p2.m(), new com.reactnativecommunity.asyncstorage.i(), new com.reactnativecommunity.blurview.b(), new C0575e(), new com.learnium.RNDeviceInfo.b(), new C0611e(), new org.wonday.orientation.c(), new C0563c(), new com.oblador.vectoricons.c(), new com.reactnativecommunity.webview.q()));
    }

    public C0503k(Application application) {
        this(application, (J1.a) null);
    }

    public C0503k(N n3, J1.a aVar) {
        this.f9220b = n3;
        this.f9221c = aVar;
    }

    public C0503k(Application application, J1.a aVar) {
        this.f9220b = null;
        this.f9219a = application;
        this.f9221c = aVar;
    }
}
