package U2;

import java.util.List;
import kotlin.jvm.internal.DefaultConstructorMarker;

/* JADX INFO: loaded from: classes.dex */
public interface l {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public static final a f2663b = new a(null);

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static final l f2662a = new a.C0036a();

    public static final class a {

        /* JADX INFO: renamed from: U2.l$a$a, reason: collision with other inner class name */
        private static final class C0036a implements l {
            @Override // U2.l
            public boolean a(int i3, List list) {
                D2.h.f(list, "requestHeaders");
                return true;
            }

            @Override // U2.l
            public boolean b(int i3, List list, boolean z3) {
                D2.h.f(list, "responseHeaders");
                return true;
            }

            @Override // U2.l
            public boolean c(int i3, b3.k kVar, int i4, boolean z3) {
                D2.h.f(kVar, "source");
                kVar.s(i4);
                return true;
            }

            @Override // U2.l
            public void d(int i3, b bVar) {
                D2.h.f(bVar, "errorCode");
            }
        }

        private a() {
        }

        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }

    boolean a(int i3, List list);

    boolean b(int i3, List list, boolean z3);

    boolean c(int i3, b3.k kVar, int i4, boolean z3);

    void d(int i3, b bVar);
}
