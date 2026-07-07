package w2;

import D2.h;
import java.io.Serializable;
import kotlin.enums.EnumEntries;
import s2.AbstractC0705b;
import s2.AbstractC0711h;

/* JADX INFO: renamed from: w2.b, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
final class C0765b extends AbstractC0705b implements EnumEntries, Serializable {

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final Enum[] f10895c;

    public C0765b(Enum<Object>[] enumArr) {
        h.f(enumArr, "entries");
        this.f10895c = enumArr;
    }

    @Override // s2.AbstractC0704a
    public int a() {
        return this.f10895c.length;
    }

    public boolean b(Enum r3) {
        h.f(r3, "element");
        return ((Enum) AbstractC0711h.s(this.f10895c, r3.ordinal())) == r3;
    }

    @Override // s2.AbstractC0705b, java.util.List
    /* JADX INFO: renamed from: c, reason: merged with bridge method [inline-methods] */
    public Enum get(int i3) {
        AbstractC0705b.f10601b.a(i3, this.f10895c.length);
        return this.f10895c[i3];
    }

    @Override // s2.AbstractC0704a, java.util.Collection
    public final /* bridge */ boolean contains(Object obj) {
        if (obj instanceof Enum) {
            return b((Enum) obj);
        }
        return false;
    }

    public int e(Enum r3) {
        h.f(r3, "element");
        int iOrdinal = r3.ordinal();
        if (((Enum) AbstractC0711h.s(this.f10895c, iOrdinal)) == r3) {
            return iOrdinal;
        }
        return -1;
    }

    public int f(Enum r22) {
        h.f(r22, "element");
        return indexOf(r22);
    }

    @Override // s2.AbstractC0705b, java.util.List
    public final /* bridge */ int indexOf(Object obj) {
        if (obj instanceof Enum) {
            return e((Enum) obj);
        }
        return -1;
    }

    @Override // s2.AbstractC0705b, java.util.List
    public final /* bridge */ int lastIndexOf(Object obj) {
        if (obj instanceof Enum) {
            return f((Enum) obj);
        }
        return -1;
    }
}
