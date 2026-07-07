package O0;

import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class m implements l {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final int f1475b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final int f1476c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private final int f1477d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private final o f1478e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private final Map f1479f;

    public m(int i3, int i4, int i5, o oVar, Map<String, Object> map) {
        this.f1475b = i3;
        this.f1476c = i4;
        this.f1477d = i5;
        this.f1478e = oVar;
        this.f1479f = map;
    }

    @Override // O0.k, y0.InterfaceC0776a
    public Map a() {
        return this.f1479f;
    }

    @Override // O0.l
    public int d() {
        return this.f1476c;
    }

    @Override // O0.l
    public int h() {
        return this.f1475b;
    }
}
