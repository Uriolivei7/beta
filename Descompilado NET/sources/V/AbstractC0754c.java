package v;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/* JADX INFO: renamed from: v.c, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public abstract class AbstractC0754c extends AbstractC0752a {

    /* JADX INFO: renamed from: k, reason: collision with root package name */
    private int f10883k;

    /* JADX INFO: renamed from: l, reason: collision with root package name */
    private int f10884l;

    /* JADX INFO: renamed from: m, reason: collision with root package name */
    private LayoutInflater f10885m;

    @Deprecated
    public AbstractC0754c(Context context, int i3, Cursor cursor) {
        super(context, cursor);
        this.f10884l = i3;
        this.f10883k = i3;
        this.f10885m = (LayoutInflater) context.getSystemService("layout_inflater");
    }

    @Override // v.AbstractC0752a
    public View g(Context context, Cursor cursor, ViewGroup viewGroup) {
        return this.f10885m.inflate(this.f10884l, viewGroup, false);
    }

    @Override // v.AbstractC0752a
    public View h(Context context, Cursor cursor, ViewGroup viewGroup) {
        return this.f10885m.inflate(this.f10883k, viewGroup, false);
    }

    @Deprecated
    public AbstractC0754c(Context context, int i3, Cursor cursor, boolean z3) {
        super(context, cursor, z3);
        this.f10884l = i3;
        this.f10883k = i3;
        this.f10885m = (LayoutInflater) context.getSystemService("layout_inflater");
    }

    public AbstractC0754c(Context context, int i3, Cursor cursor, int i4) {
        super(context, cursor, i4);
        this.f10884l = i3;
        this.f10883k = i3;
        this.f10885m = (LayoutInflater) context.getSystemService("layout_inflater");
    }
}
