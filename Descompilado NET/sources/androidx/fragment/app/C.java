package androidx.fragment.app;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import androidx.lifecycle.AbstractC0299g;

/* JADX INFO: loaded from: classes.dex */
final class C implements Parcelable {
    public static final Parcelable.Creator<C> CREATOR = new a();

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    final String f4857a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    final String f4858b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    final boolean f4859c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    final int f4860d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    final int f4861e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    final String f4862f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    final boolean f4863g;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    final boolean f4864h;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    final boolean f4865i;

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    final Bundle f4866j;

    /* JADX INFO: renamed from: k, reason: collision with root package name */
    final boolean f4867k;

    /* JADX INFO: renamed from: l, reason: collision with root package name */
    final int f4868l;

    /* JADX INFO: renamed from: m, reason: collision with root package name */
    Bundle f4869m;

    class a implements Parcelable.Creator {
        a() {
        }

        @Override // android.os.Parcelable.Creator
        /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
        public C createFromParcel(Parcel parcel) {
            return new C(parcel);
        }

        @Override // android.os.Parcelable.Creator
        /* JADX INFO: renamed from: b, reason: merged with bridge method [inline-methods] */
        public C[] newArray(int i3) {
            return new C[i3];
        }
    }

    C(Fragment fragment) {
        this.f4857a = fragment.getClass().getName();
        this.f4858b = fragment.f4944g;
        this.f4859c = fragment.f4953p;
        this.f4860d = fragment.f4962y;
        this.f4861e = fragment.f4963z;
        this.f4862f = fragment.f4911A;
        this.f4863g = fragment.f4914D;
        this.f4864h = fragment.f4951n;
        this.f4865i = fragment.f4913C;
        this.f4866j = fragment.f4945h;
        this.f4867k = fragment.f4912B;
        this.f4868l = fragment.f4929S.ordinal();
    }

    Fragment a(o oVar, ClassLoader classLoader) {
        Fragment fragmentA = oVar.a(classLoader, this.f4857a);
        Bundle bundle = this.f4866j;
        if (bundle != null) {
            bundle.setClassLoader(classLoader);
        }
        fragmentA.s1(this.f4866j);
        fragmentA.f4944g = this.f4858b;
        fragmentA.f4953p = this.f4859c;
        fragmentA.f4955r = true;
        fragmentA.f4962y = this.f4860d;
        fragmentA.f4963z = this.f4861e;
        fragmentA.f4911A = this.f4862f;
        fragmentA.f4914D = this.f4863g;
        fragmentA.f4951n = this.f4864h;
        fragmentA.f4913C = this.f4865i;
        fragmentA.f4912B = this.f4867k;
        fragmentA.f4929S = AbstractC0299g.b.values()[this.f4868l];
        Bundle bundle2 = this.f4869m;
        if (bundle2 != null) {
            fragmentA.f4940c = bundle2;
        } else {
            fragmentA.f4940c = new Bundle();
        }
        return fragmentA;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(128);
        sb.append("FragmentState{");
        sb.append(this.f4857a);
        sb.append(" (");
        sb.append(this.f4858b);
        sb.append(")}:");
        if (this.f4859c) {
            sb.append(" fromLayout");
        }
        if (this.f4861e != 0) {
            sb.append(" id=0x");
            sb.append(Integer.toHexString(this.f4861e));
        }
        String str = this.f4862f;
        if (str != null && !str.isEmpty()) {
            sb.append(" tag=");
            sb.append(this.f4862f);
        }
        if (this.f4863g) {
            sb.append(" retainInstance");
        }
        if (this.f4864h) {
            sb.append(" removing");
        }
        if (this.f4865i) {
            sb.append(" detached");
        }
        if (this.f4867k) {
            sb.append(" hidden");
        }
        return sb.toString();
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i3) {
        parcel.writeString(this.f4857a);
        parcel.writeString(this.f4858b);
        parcel.writeInt(this.f4859c ? 1 : 0);
        parcel.writeInt(this.f4860d);
        parcel.writeInt(this.f4861e);
        parcel.writeString(this.f4862f);
        parcel.writeInt(this.f4863g ? 1 : 0);
        parcel.writeInt(this.f4864h ? 1 : 0);
        parcel.writeInt(this.f4865i ? 1 : 0);
        parcel.writeBundle(this.f4866j);
        parcel.writeInt(this.f4867k ? 1 : 0);
        parcel.writeBundle(this.f4869m);
        parcel.writeInt(this.f4868l);
    }

    C(Parcel parcel) {
        this.f4857a = parcel.readString();
        this.f4858b = parcel.readString();
        this.f4859c = parcel.readInt() != 0;
        this.f4860d = parcel.readInt();
        this.f4861e = parcel.readInt();
        this.f4862f = parcel.readString();
        this.f4863g = parcel.readInt() != 0;
        this.f4864h = parcel.readInt() != 0;
        this.f4865i = parcel.readInt() != 0;
        this.f4866j = parcel.readBundle();
        this.f4867k = parcel.readInt() != 0;
        this.f4869m = parcel.readBundle();
        this.f4868l = parcel.readInt();
    }
}
