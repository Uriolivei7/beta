package K2;

import java.util.Iterator;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import s2.AbstractC0704a;
import s2.AbstractC0717n;

/* JADX INFO: loaded from: classes.dex */
final class j implements i {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final Matcher f836a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final CharSequence f837b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final h f838c;

    public static final class a extends AbstractC0704a implements h {

        /* JADX INFO: renamed from: K2.j$a$a, reason: collision with other inner class name */
        static final class C0012a extends D2.i implements C2.l {
            C0012a() {
                super(1);
            }

            @Override // C2.l
            public /* bridge */ /* synthetic */ Object d(Object obj) {
                return e(((Number) obj).intValue());
            }

            public final g e(int i3) {
                return a.this.get(i3);
            }
        }

        a() {
        }

        @Override // s2.AbstractC0704a
        public int a() {
            return j.this.c().groupCount() + 1;
        }

        public /* bridge */ boolean b(g gVar) {
            return super.contains(gVar);
        }

        @Override // s2.AbstractC0704a, java.util.Collection
        public final /* bridge */ boolean contains(Object obj) {
            if (obj == null ? true : obj instanceof g) {
                return b((g) obj);
            }
            return false;
        }

        @Override // K2.h
        public g get(int i3) {
            H2.c cVarE = l.e(j.this.c(), i3);
            if (cVarE.i().intValue() < 0) {
                return null;
            }
            String strGroup = j.this.c().group(i3);
            D2.h.e(strGroup, "group(...)");
            return new g(strGroup, cVarE);
        }

        @Override // s2.AbstractC0704a, java.util.Collection
        public boolean isEmpty() {
            return false;
        }

        @Override // java.util.Collection, java.lang.Iterable
        public Iterator iterator() {
            return J2.d.f(AbstractC0717n.H(AbstractC0717n.h(this)), new C0012a()).iterator();
        }
    }

    public j(Matcher matcher, CharSequence charSequence) {
        D2.h.f(matcher, "matcher");
        D2.h.f(charSequence, "input");
        this.f836a = matcher;
        this.f837b = charSequence;
        this.f838c = new a();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final MatchResult c() {
        return this.f836a;
    }

    @Override // K2.i
    public h a() {
        return this.f838c;
    }
}
