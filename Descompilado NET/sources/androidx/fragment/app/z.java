package androidx.fragment.app;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.fragment.app.x;
import java.util.ArrayList;

/* JADX INFO: loaded from: classes.dex */
final class z implements Parcelable {
    public static final Parcelable.Creator<z> CREATOR = new a();

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    ArrayList f5244a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    ArrayList f5245b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    C0280b[] f5246c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    int f5247d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    String f5248e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    ArrayList f5249f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    ArrayList f5250g;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    ArrayList f5251h;

    class a implements Parcelable.Creator {
        a() {
        }

        @Override // android.os.Parcelable.Creator
        /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
        public z createFromParcel(Parcel parcel) {
            return new z(parcel);
        }

        @Override // android.os.Parcelable.Creator
        /* JADX INFO: renamed from: b, reason: merged with bridge method [inline-methods] */
        public z[] newArray(int i3) {
            return new z[i3];
        }
    }

    public z() {
        this.f5248e = null;
        this.f5249f = new ArrayList();
        this.f5250g = new ArrayList();
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i3) {
        parcel.writeStringList(this.f5244a);
        parcel.writeStringList(this.f5245b);
        parcel.writeTypedArray(this.f5246c, i3);
        parcel.writeInt(this.f5247d);
        parcel.writeString(this.f5248e);
        parcel.writeStringList(this.f5249f);
        parcel.writeTypedList(this.f5250g);
        parcel.writeTypedList(this.f5251h);
    }

    public z(Parcel parcel) {
        this.f5248e = null;
        this.f5249f = new ArrayList();
        this.f5250g = new ArrayList();
        this.f5244a = parcel.createStringArrayList();
        this.f5245b = parcel.createStringArrayList();
        this.f5246c = (C0280b[]) parcel.createTypedArray(C0280b.CREATOR);
        this.f5247d = parcel.readInt();
        this.f5248e = parcel.readString();
        this.f5249f = parcel.createStringArrayList();
        this.f5250g = parcel.createTypedArrayList(C0281c.CREATOR);
        this.f5251h = parcel.createTypedArrayList(x.k.CREATOR);
    }
}
