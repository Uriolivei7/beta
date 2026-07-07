package androidx.fragment.app;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;
import androidx.fragment.app.F;
import androidx.lifecycle.AbstractC0299g;
import java.util.ArrayList;

/* JADX INFO: renamed from: androidx.fragment.app.b, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
final class C0280b implements Parcelable {
    public static final Parcelable.Creator<C0280b> CREATOR = new a();

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    final int[] f5060a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    final ArrayList f5061b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    final int[] f5062c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    final int[] f5063d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    final int f5064e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    final String f5065f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    final int f5066g;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    final int f5067h;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    final CharSequence f5068i;

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    final int f5069j;

    /* JADX INFO: renamed from: k, reason: collision with root package name */
    final CharSequence f5070k;

    /* JADX INFO: renamed from: l, reason: collision with root package name */
    final ArrayList f5071l;

    /* JADX INFO: renamed from: m, reason: collision with root package name */
    final ArrayList f5072m;

    /* JADX INFO: renamed from: n, reason: collision with root package name */
    final boolean f5073n;

    /* JADX INFO: renamed from: androidx.fragment.app.b$a */
    class a implements Parcelable.Creator {
        a() {
        }

        @Override // android.os.Parcelable.Creator
        /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
        public C0280b createFromParcel(Parcel parcel) {
            return new C0280b(parcel);
        }

        @Override // android.os.Parcelable.Creator
        /* JADX INFO: renamed from: b, reason: merged with bridge method [inline-methods] */
        public C0280b[] newArray(int i3) {
            return new C0280b[i3];
        }
    }

    C0280b(C0279a c0279a) {
        int size = c0279a.f4884c.size();
        this.f5060a = new int[size * 6];
        if (!c0279a.f4890i) {
            throw new IllegalStateException("Not on back stack");
        }
        this.f5061b = new ArrayList(size);
        this.f5062c = new int[size];
        this.f5063d = new int[size];
        int i3 = 0;
        for (int i4 = 0; i4 < size; i4++) {
            F.a aVar = (F.a) c0279a.f4884c.get(i4);
            int i5 = i3 + 1;
            this.f5060a[i3] = aVar.f4901a;
            ArrayList arrayList = this.f5061b;
            Fragment fragment = aVar.f4902b;
            arrayList.add(fragment != null ? fragment.f4944g : null);
            int[] iArr = this.f5060a;
            iArr[i5] = aVar.f4903c ? 1 : 0;
            iArr[i3 + 2] = aVar.f4904d;
            iArr[i3 + 3] = aVar.f4905e;
            int i6 = i3 + 5;
            iArr[i3 + 4] = aVar.f4906f;
            i3 += 6;
            iArr[i6] = aVar.f4907g;
            this.f5062c[i4] = aVar.f4908h.ordinal();
            this.f5063d[i4] = aVar.f4909i.ordinal();
        }
        this.f5064e = c0279a.f4889h;
        this.f5065f = c0279a.f4892k;
        this.f5066g = c0279a.f5058v;
        this.f5067h = c0279a.f4893l;
        this.f5068i = c0279a.f4894m;
        this.f5069j = c0279a.f4895n;
        this.f5070k = c0279a.f4896o;
        this.f5071l = c0279a.f4897p;
        this.f5072m = c0279a.f4898q;
        this.f5073n = c0279a.f4899r;
    }

    private void a(C0279a c0279a) {
        int i3 = 0;
        int i4 = 0;
        while (true) {
            boolean z3 = true;
            if (i3 >= this.f5060a.length) {
                c0279a.f4889h = this.f5064e;
                c0279a.f4892k = this.f5065f;
                c0279a.f4890i = true;
                c0279a.f4893l = this.f5067h;
                c0279a.f4894m = this.f5068i;
                c0279a.f4895n = this.f5069j;
                c0279a.f4896o = this.f5070k;
                c0279a.f4897p = this.f5071l;
                c0279a.f4898q = this.f5072m;
                c0279a.f4899r = this.f5073n;
                return;
            }
            F.a aVar = new F.a();
            int i5 = i3 + 1;
            aVar.f4901a = this.f5060a[i3];
            if (x.G0(2)) {
                Log.v("FragmentManager", "Instantiate " + c0279a + " op #" + i4 + " base fragment #" + this.f5060a[i5]);
            }
            aVar.f4908h = AbstractC0299g.b.values()[this.f5062c[i4]];
            aVar.f4909i = AbstractC0299g.b.values()[this.f5063d[i4]];
            int[] iArr = this.f5060a;
            int i6 = i3 + 2;
            if (iArr[i5] == 0) {
                z3 = false;
            }
            aVar.f4903c = z3;
            int i7 = iArr[i6];
            aVar.f4904d = i7;
            int i8 = iArr[i3 + 3];
            aVar.f4905e = i8;
            int i9 = i3 + 5;
            int i10 = iArr[i3 + 4];
            aVar.f4906f = i10;
            i3 += 6;
            int i11 = iArr[i9];
            aVar.f4907g = i11;
            c0279a.f4885d = i7;
            c0279a.f4886e = i8;
            c0279a.f4887f = i10;
            c0279a.f4888g = i11;
            c0279a.e(aVar);
            i4++;
        }
    }

    public C0279a b(x xVar) {
        C0279a c0279a = new C0279a(xVar);
        a(c0279a);
        c0279a.f5058v = this.f5066g;
        for (int i3 = 0; i3 < this.f5061b.size(); i3++) {
            String str = (String) this.f5061b.get(i3);
            if (str != null) {
                ((F.a) c0279a.f4884c.get(i3)).f4902b = xVar.e0(str);
            }
        }
        c0279a.n(1);
        return c0279a;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i3) {
        parcel.writeIntArray(this.f5060a);
        parcel.writeStringList(this.f5061b);
        parcel.writeIntArray(this.f5062c);
        parcel.writeIntArray(this.f5063d);
        parcel.writeInt(this.f5064e);
        parcel.writeString(this.f5065f);
        parcel.writeInt(this.f5066g);
        parcel.writeInt(this.f5067h);
        TextUtils.writeToParcel(this.f5068i, parcel, 0);
        parcel.writeInt(this.f5069j);
        TextUtils.writeToParcel(this.f5070k, parcel, 0);
        parcel.writeStringList(this.f5071l);
        parcel.writeStringList(this.f5072m);
        parcel.writeInt(this.f5073n ? 1 : 0);
    }

    C0280b(Parcel parcel) {
        this.f5060a = parcel.createIntArray();
        this.f5061b = parcel.createStringArrayList();
        this.f5062c = parcel.createIntArray();
        this.f5063d = parcel.createIntArray();
        this.f5064e = parcel.readInt();
        this.f5065f = parcel.readString();
        this.f5066g = parcel.readInt();
        this.f5067h = parcel.readInt();
        Parcelable.Creator creator = TextUtils.CHAR_SEQUENCE_CREATOR;
        this.f5068i = (CharSequence) creator.createFromParcel(parcel);
        this.f5069j = parcel.readInt();
        this.f5070k = (CharSequence) creator.createFromParcel(parcel);
        this.f5071l = parcel.createStringArrayList();
        this.f5072m = parcel.createStringArrayList();
        this.f5073n = parcel.readInt() != 0;
    }
}
