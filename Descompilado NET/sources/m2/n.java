package M2;

import java.util.List;
import kotlin.jvm.internal.DefaultConstructorMarker;
import s2.AbstractC0717n;

/* JADX INFO: loaded from: classes.dex */
public interface n {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public static final a f1203b = new a(null);

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static final n f1202a = new a.C0019a();

    public static final class a {

        /* JADX INFO: renamed from: M2.n$a$a, reason: collision with other inner class name */
        private static final class C0019a implements n {
            @Override // M2.n
            public void a(u uVar, List list) {
                D2.h.f(uVar, "url");
                D2.h.f(list, "cookies");
            }

            @Override // M2.n
            public List c(u uVar) {
                D2.h.f(uVar, "url");
                return AbstractC0717n.g();
            }
        }

        private a() {
        }

        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }

    void a(u uVar, List list);

    List c(u uVar);
}
