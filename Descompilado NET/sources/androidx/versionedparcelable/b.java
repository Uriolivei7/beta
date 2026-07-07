package androidx.versionedparcelable;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.SparseIntArray;
import l.C0589a;

/* JADX INFO: loaded from: classes.dex */
class b extends a {

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private final SparseIntArray f5559d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private final Parcel f5560e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private final int f5561f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private final int f5562g;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private final String f5563h;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    private int f5564i;

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    private int f5565j;

    /* JADX INFO: renamed from: k, reason: collision with root package name */
    private int f5566k;

    b(Parcel parcel) {
        this(parcel, parcel.dataPosition(), parcel.dataSize(), "", new C0589a(), new C0589a(), new C0589a());
    }

    @Override // androidx.versionedparcelable.a
    public void A(byte[] bArr) {
        if (bArr == null) {
            this.f5560e.writeInt(-1);
        } else {
            this.f5560e.writeInt(bArr.length);
            this.f5560e.writeByteArray(bArr);
        }
    }

    @Override // androidx.versionedparcelable.a
    protected void C(CharSequence charSequence) {
        TextUtils.writeToParcel(charSequence, this.f5560e, 0);
    }

    @Override // androidx.versionedparcelable.a
    public void E(int i3) {
        this.f5560e.writeInt(i3);
    }

    @Override // androidx.versionedparcelable.a
    public void G(Parcelable parcelable) {
        this.f5560e.writeParcelable(parcelable, 0);
    }

    @Override // androidx.versionedparcelable.a
    public void I(String str) {
        this.f5560e.writeString(str);
    }

    @Override // androidx.versionedparcelable.a
    public void a() {
        int i3 = this.f5564i;
        if (i3 >= 0) {
            int i4 = this.f5559d.get(i3);
            int iDataPosition = this.f5560e.dataPosition();
            this.f5560e.setDataPosition(i4);
            this.f5560e.writeInt(iDataPosition - i4);
            this.f5560e.setDataPosition(iDataPosition);
        }
    }

    @Override // androidx.versionedparcelable.a
    protected a b() {
        Parcel parcel = this.f5560e;
        int iDataPosition = parcel.dataPosition();
        int i3 = this.f5565j;
        if (i3 == this.f5561f) {
            i3 = this.f5562g;
        }
        return new b(parcel, iDataPosition, i3, this.f5563h + "  ", this.f5556a, this.f5557b, this.f5558c);
    }

    @Override // androidx.versionedparcelable.a
    public boolean g() {
        return this.f5560e.readInt() != 0;
    }

    @Override // androidx.versionedparcelable.a
    public byte[] i() {
        int i3 = this.f5560e.readInt();
        if (i3 < 0) {
            return null;
        }
        byte[] bArr = new byte[i3];
        this.f5560e.readByteArray(bArr);
        return bArr;
    }

    @Override // androidx.versionedparcelable.a
    protected CharSequence k() {
        return (CharSequence) TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(this.f5560e);
    }

    @Override // androidx.versionedparcelable.a
    public boolean m(int i3) {
        while (this.f5565j < this.f5562g) {
            int i4 = this.f5566k;
            if (i4 == i3) {
                return true;
            }
            if (String.valueOf(i4).compareTo(String.valueOf(i3)) > 0) {
                return false;
            }
            this.f5560e.setDataPosition(this.f5565j);
            int i5 = this.f5560e.readInt();
            this.f5566k = this.f5560e.readInt();
            this.f5565j += i5;
        }
        return this.f5566k == i3;
    }

    @Override // androidx.versionedparcelable.a
    public int o() {
        return this.f5560e.readInt();
    }

    @Override // androidx.versionedparcelable.a
    public Parcelable q() {
        return this.f5560e.readParcelable(getClass().getClassLoader());
    }

    @Override // androidx.versionedparcelable.a
    public String s() {
        return this.f5560e.readString();
    }

    @Override // androidx.versionedparcelable.a
    public void w(int i3) {
        a();
        this.f5564i = i3;
        this.f5559d.put(i3, this.f5560e.dataPosition());
        E(0);
        E(i3);
    }

    @Override // androidx.versionedparcelable.a
    public void y(boolean z3) {
        this.f5560e.writeInt(z3 ? 1 : 0);
    }

    private b(Parcel parcel, int i3, int i4, String str, C0589a c0589a, C0589a c0589a2, C0589a c0589a3) {
        super(c0589a, c0589a2, c0589a3);
        this.f5559d = new SparseIntArray();
        this.f5564i = -1;
        this.f5566k = -1;
        this.f5560e = parcel;
        this.f5561f = i3;
        this.f5562g = i4;
        this.f5565j = i3;
        this.f5563h = str;
    }
}
