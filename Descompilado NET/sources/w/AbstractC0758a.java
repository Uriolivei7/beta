package w;

import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: renamed from: w.a, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public abstract class AbstractC0758a implements Parcelable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final Parcelable f10894a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public static final AbstractC0758a f10893b = new C0151a();
    public static final Parcelable.Creator<AbstractC0758a> CREATOR = new b();

    /* JADX INFO: renamed from: w.a$a, reason: collision with other inner class name */
    static class C0151a extends AbstractC0758a {
        C0151a() {
            super((C0151a) null);
        }
    }

    /* JADX INFO: renamed from: w.a$b */
    static class b implements Parcelable.ClassLoaderCreator {
        b() {
        }

        @Override // android.os.Parcelable.Creator
        /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
        public AbstractC0758a createFromParcel(Parcel parcel) {
            return createFromParcel(parcel, null);
        }

        @Override // android.os.Parcelable.ClassLoaderCreator
        /* JADX INFO: renamed from: b, reason: merged with bridge method [inline-methods] */
        public AbstractC0758a createFromParcel(Parcel parcel, ClassLoader classLoader) {
            if (parcel.readParcelable(classLoader) == null) {
                return AbstractC0758a.f10893b;
            }
            throw new IllegalStateException("superState must be null");
        }

        @Override // android.os.Parcelable.Creator
        /* JADX INFO: renamed from: c, reason: merged with bridge method [inline-methods] */
        public AbstractC0758a[] newArray(int i3) {
            return new AbstractC0758a[i3];
        }
    }

    /* synthetic */ AbstractC0758a(C0151a c0151a) {
        this();
    }

    public final Parcelable a() {
        return this.f10894a;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i3) {
        parcel.writeParcelable(this.f10894a, i3);
    }

    private AbstractC0758a() {
        this.f10894a = null;
    }

    protected AbstractC0758a(Parcelable parcelable) {
        if (parcelable != null) {
            this.f10894a = parcelable == f10893b ? null : parcelable;
            return;
        }
        throw new IllegalArgumentException("superState must not be null");
    }

    protected AbstractC0758a(Parcel parcel, ClassLoader classLoader) {
        Parcelable parcelable = parcel.readParcelable(classLoader);
        this.f10894a = parcelable == null ? f10893b : parcelable;
    }
}
