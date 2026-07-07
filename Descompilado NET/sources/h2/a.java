package H2;

import kotlin.jvm.internal.DefaultConstructorMarker;
import s2.AbstractC0695C;
import x2.AbstractC0774c;

/* JADX INFO: loaded from: classes.dex */
public class a implements Iterable, E2.a {

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    public static final C0006a f368e = new C0006a(null);

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final int f369b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final int f370c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private final int f371d;

    /* JADX INFO: renamed from: H2.a$a, reason: collision with other inner class name */
    public static final class C0006a {
        public /* synthetic */ C0006a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final a a(int i3, int i4, int i5) {
            return new a(i3, i4, i5);
        }

        private C0006a() {
        }
    }

    public a(int i3, int i4, int i5) {
        if (i5 == 0) {
            throw new IllegalArgumentException("Step must be non-zero.");
        }
        if (i5 == Integer.MIN_VALUE) {
            throw new IllegalArgumentException("Step must be greater than Int.MIN_VALUE to avoid overflow on negation.");
        }
        this.f369b = i3;
        this.f370c = AbstractC0774c.b(i3, i4, i5);
        this.f371d = i5;
    }

    public final int a() {
        return this.f369b;
    }

    public final int b() {
        return this.f370c;
    }

    public final int c() {
        return this.f371d;
    }

    @Override // java.lang.Iterable
    /* JADX INFO: renamed from: e, reason: merged with bridge method [inline-methods] */
    public AbstractC0695C iterator() {
        return new b(this.f369b, this.f370c, this.f371d);
    }

    public boolean equals(Object obj) {
        if (obj instanceof a) {
            if (!isEmpty() || !((a) obj).isEmpty()) {
                a aVar = (a) obj;
                if (this.f369b != aVar.f369b || this.f370c != aVar.f370c || this.f371d != aVar.f371d) {
                }
            }
            return true;
        }
        return false;
    }

    public int hashCode() {
        if (isEmpty()) {
            return -1;
        }
        return (((this.f369b * 31) + this.f370c) * 31) + this.f371d;
    }

    public boolean isEmpty() {
        if (this.f371d > 0) {
            if (this.f369b <= this.f370c) {
                return false;
            }
        } else if (this.f369b >= this.f370c) {
            return false;
        }
        return true;
    }

    public String toString() {
        StringBuilder sb;
        int i3;
        if (this.f371d > 0) {
            sb = new StringBuilder();
            sb.append(this.f369b);
            sb.append("..");
            sb.append(this.f370c);
            sb.append(" step ");
            i3 = this.f371d;
        } else {
            sb = new StringBuilder();
            sb.append(this.f369b);
            sb.append(" downTo ");
            sb.append(this.f370c);
            sb.append(" step ");
            i3 = -this.f371d;
        }
        sb.append(i3);
        return sb.toString();
    }
}
