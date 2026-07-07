package O0;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/* JADX INFO: loaded from: classes.dex */
public abstract class a implements d {

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private static final Set f1450d = new HashSet(Arrays.asList("encoded_size", "encoded_width", "encoded_height", "uri_source", "image_format", "bitmap_config", "is_rounded", "non_fatal_decode_error", "original_url", "modified_url", "image_color_space"));

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private Map f1451b = new HashMap();

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private l f1452c;

    @Override // y0.InterfaceC0776a
    public void A(String str, Object obj) {
        if (f1450d.contains(str)) {
            this.f1451b.put(str, obj);
        }
    }

    @Override // O0.k, y0.InterfaceC0776a
    public Map a() {
        return this.f1451b;
    }

    @Override // O0.d
    public o l() {
        return n.f1480d;
    }

    @Override // O0.d
    public boolean l0() {
        return false;
    }

    @Override // y0.InterfaceC0776a
    public void q(Map map) {
        if (map == null) {
            return;
        }
        for (String str : f1450d) {
            Object obj = map.get(str);
            if (obj != null) {
                this.f1451b.put(str, obj);
            }
        }
    }

    @Override // O0.d
    public l r() {
        if (this.f1452c == null) {
            this.f1452c = new m(h(), d(), b0(), l(), a());
        }
        return this.f1452c;
    }
}
