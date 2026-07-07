package androidx.activity.result;

import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes.dex */
public final class a implements Parcelable {
    public static final Parcelable.Creator<a> CREATOR = new C0049a();

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final int f3044a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final Intent f3045b;

    /* JADX INFO: renamed from: androidx.activity.result.a$a, reason: collision with other inner class name */
    class C0049a implements Parcelable.Creator {
        C0049a() {
        }

        @Override // android.os.Parcelable.Creator
        /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
        public a createFromParcel(Parcel parcel) {
            return new a(parcel);
        }

        @Override // android.os.Parcelable.Creator
        /* JADX INFO: renamed from: b, reason: merged with bridge method [inline-methods] */
        public a[] newArray(int i3) {
            return new a[i3];
        }
    }

    public a(int i3, Intent intent) {
        this.f3044a = i3;
        this.f3045b = intent;
    }

    public static String c(int i3) {
        return i3 != -1 ? i3 != 0 ? String.valueOf(i3) : "RESULT_CANCELED" : "RESULT_OK";
    }

    public Intent a() {
        return this.f3045b;
    }

    public int b() {
        return this.f3044a;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public String toString() {
        return "ActivityResult{resultCode=" + c(this.f3044a) + ", data=" + this.f3045b + '}';
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i3) {
        parcel.writeInt(this.f3044a);
        parcel.writeInt(this.f3045b == null ? 0 : 1);
        Intent intent = this.f3045b;
        if (intent != null) {
            intent.writeToParcel(parcel, i3);
        }
    }

    a(Parcel parcel) {
        this.f3044a = parcel.readInt();
        this.f3045b = parcel.readInt() == 0 ? null : (Intent) Intent.CREATOR.createFromParcel(parcel);
    }
}
