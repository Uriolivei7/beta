package p;

import android.util.Base64;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public final class e {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final String f10227a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final String f10228b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final String f10229c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private final List f10230d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private final int f10231e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private final String f10232f;

    public e(String str, String str2, String str3, List<List<byte[]>> list) {
        this.f10227a = (String) q.g.g(str);
        this.f10228b = (String) q.g.g(str2);
        this.f10229c = (String) q.g.g(str3);
        this.f10230d = (List) q.g.g(list);
        this.f10231e = 0;
        this.f10232f = a(str, str2, str3);
    }

    private String a(String str, String str2, String str3) {
        return str + "-" + str2 + "-" + str3;
    }

    public List b() {
        return this.f10230d;
    }

    public int c() {
        return this.f10231e;
    }

    String d() {
        return this.f10232f;
    }

    public String e() {
        return this.f10227a;
    }

    public String f() {
        return this.f10228b;
    }

    public String g() {
        return this.f10229c;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("FontRequest {mProviderAuthority: " + this.f10227a + ", mProviderPackage: " + this.f10228b + ", mQuery: " + this.f10229c + ", mCertificates:");
        for (int i3 = 0; i3 < this.f10230d.size(); i3++) {
            sb.append(" [");
            List list = (List) this.f10230d.get(i3);
            for (int i4 = 0; i4 < list.size(); i4++) {
                sb.append(" \"");
                sb.append(Base64.encodeToString((byte[]) list.get(i4), 0));
                sb.append("\"");
            }
            sb.append(" ]");
        }
        sb.append("}");
        sb.append("mCertificatesArray: " + this.f10231e);
        return sb.toString();
    }

    public e(String str, String str2, String str3, int i3) {
        this.f10227a = (String) q.g.g(str);
        this.f10228b = (String) q.g.g(str2);
        this.f10229c = (String) q.g.g(str3);
        this.f10230d = null;
        q.g.a(i3 != 0);
        this.f10231e = i3;
        this.f10232f = a(str, str2, str3);
    }
}
