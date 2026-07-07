package H0;

import java.util.concurrent.TimeUnit;
import kotlin.jvm.internal.DefaultConstructorMarker;

/* JADX INFO: loaded from: classes.dex */
public final class y {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final int f331a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final int f332b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public final int f333c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public final int f334d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    public final int f335e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    public final long f336f;

    public y(int i3, int i4, int i5, int i6, int i7) {
        this(i3, i4, i5, i6, i7, 0L, 32, null);
    }

    public y(int i3, int i4, int i5, int i6, int i7, long j3) {
        this.f331a = i3;
        this.f332b = i4;
        this.f333c = i5;
        this.f334d = i6;
        this.f335e = i7;
        this.f336f = j3;
    }

    public /* synthetic */ y(int i3, int i4, int i5, int i6, int i7, long j3, int i8, DefaultConstructorMarker defaultConstructorMarker) {
        this(i3, i4, i5, i6, i7, (i8 & 32) != 0 ? TimeUnit.MINUTES.toMillis(5L) : j3);
    }
}
