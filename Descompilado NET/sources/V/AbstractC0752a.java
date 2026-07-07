package v;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.FilterQueryProvider;
import android.widget.Filterable;
import v.C0753b;

/* JADX INFO: renamed from: v.a, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public abstract class AbstractC0752a extends BaseAdapter implements Filterable, C0753b.a {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    protected boolean f10871b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    protected boolean f10872c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    protected Cursor f10873d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    protected Context f10874e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    protected int f10875f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    protected C0148a f10876g;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    protected DataSetObserver f10877h;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    protected C0753b f10878i;

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    protected FilterQueryProvider f10879j;

    /* JADX INFO: renamed from: v.a$a, reason: collision with other inner class name */
    private class C0148a extends ContentObserver {
        C0148a() {
            super(new Handler());
        }

        @Override // android.database.ContentObserver
        public boolean deliverSelfNotifications() {
            return true;
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean z3) {
            AbstractC0752a.this.i();
        }
    }

    /* JADX INFO: renamed from: v.a$b */
    private class b extends DataSetObserver {
        b() {
        }

        @Override // android.database.DataSetObserver
        public void onChanged() {
            AbstractC0752a abstractC0752a = AbstractC0752a.this;
            abstractC0752a.f10871b = true;
            abstractC0752a.notifyDataSetChanged();
        }

        @Override // android.database.DataSetObserver
        public void onInvalidated() {
            AbstractC0752a abstractC0752a = AbstractC0752a.this;
            abstractC0752a.f10871b = false;
            abstractC0752a.notifyDataSetInvalidated();
        }
    }

    @Deprecated
    public AbstractC0752a(Context context, Cursor cursor) {
        f(context, cursor, 1);
    }

    public void a(Cursor cursor) {
        Cursor cursorJ = j(cursor);
        if (cursorJ != null) {
            cursorJ.close();
        }
    }

    @Override // v.C0753b.a
    public Cursor b() {
        return this.f10873d;
    }

    public CharSequence c(Cursor cursor) {
        return cursor == null ? "" : cursor.toString();
    }

    public Cursor d(CharSequence charSequence) {
        FilterQueryProvider filterQueryProvider = this.f10879j;
        return filterQueryProvider != null ? filterQueryProvider.runQuery(charSequence) : this.f10873d;
    }

    public abstract void e(View view, Context context, Cursor cursor);

    void f(Context context, Cursor cursor, int i3) {
        if ((i3 & 1) == 1) {
            i3 |= 2;
            this.f10872c = true;
        } else {
            this.f10872c = false;
        }
        boolean z3 = cursor != null;
        this.f10873d = cursor;
        this.f10871b = z3;
        this.f10874e = context;
        this.f10875f = z3 ? cursor.getColumnIndexOrThrow("_id") : -1;
        if ((i3 & 2) == 2) {
            this.f10876g = new C0148a();
            this.f10877h = new b();
        } else {
            this.f10876g = null;
            this.f10877h = null;
        }
        if (z3) {
            C0148a c0148a = this.f10876g;
            if (c0148a != null) {
                cursor.registerContentObserver(c0148a);
            }
            DataSetObserver dataSetObserver = this.f10877h;
            if (dataSetObserver != null) {
                cursor.registerDataSetObserver(dataSetObserver);
            }
        }
    }

    public View g(Context context, Cursor cursor, ViewGroup viewGroup) {
        return h(context, cursor, viewGroup);
    }

    @Override // android.widget.Adapter
    public int getCount() {
        Cursor cursor;
        if (!this.f10871b || (cursor = this.f10873d) == null) {
            return 0;
        }
        return cursor.getCount();
    }

    @Override // android.widget.BaseAdapter, android.widget.SpinnerAdapter
    public View getDropDownView(int i3, View view, ViewGroup viewGroup) {
        if (!this.f10871b) {
            return null;
        }
        this.f10873d.moveToPosition(i3);
        if (view == null) {
            view = g(this.f10874e, this.f10873d, viewGroup);
        }
        e(view, this.f10874e, this.f10873d);
        return view;
    }

    @Override // android.widget.Filterable
    public Filter getFilter() {
        if (this.f10878i == null) {
            this.f10878i = new C0753b(this);
        }
        return this.f10878i;
    }

    @Override // android.widget.Adapter
    public Object getItem(int i3) {
        Cursor cursor;
        if (!this.f10871b || (cursor = this.f10873d) == null) {
            return null;
        }
        cursor.moveToPosition(i3);
        return this.f10873d;
    }

    @Override // android.widget.Adapter
    public long getItemId(int i3) {
        Cursor cursor;
        if (this.f10871b && (cursor = this.f10873d) != null && cursor.moveToPosition(i3)) {
            return this.f10873d.getLong(this.f10875f);
        }
        return 0L;
    }

    @Override // android.widget.Adapter
    public View getView(int i3, View view, ViewGroup viewGroup) {
        if (!this.f10871b) {
            throw new IllegalStateException("this should only be called when the cursor is valid");
        }
        if (this.f10873d.moveToPosition(i3)) {
            if (view == null) {
                view = h(this.f10874e, this.f10873d, viewGroup);
            }
            e(view, this.f10874e, this.f10873d);
            return view;
        }
        throw new IllegalStateException("couldn't move cursor to position " + i3);
    }

    public abstract View h(Context context, Cursor cursor, ViewGroup viewGroup);

    @Override // android.widget.BaseAdapter, android.widget.Adapter
    public boolean hasStableIds() {
        return true;
    }

    protected void i() {
        Cursor cursor;
        if (!this.f10872c || (cursor = this.f10873d) == null || cursor.isClosed()) {
            return;
        }
        this.f10871b = this.f10873d.requery();
    }

    public Cursor j(Cursor cursor) {
        Cursor cursor2 = this.f10873d;
        if (cursor == cursor2) {
            return null;
        }
        if (cursor2 != null) {
            C0148a c0148a = this.f10876g;
            if (c0148a != null) {
                cursor2.unregisterContentObserver(c0148a);
            }
            DataSetObserver dataSetObserver = this.f10877h;
            if (dataSetObserver != null) {
                cursor2.unregisterDataSetObserver(dataSetObserver);
            }
        }
        this.f10873d = cursor;
        if (cursor != null) {
            C0148a c0148a2 = this.f10876g;
            if (c0148a2 != null) {
                cursor.registerContentObserver(c0148a2);
            }
            DataSetObserver dataSetObserver2 = this.f10877h;
            if (dataSetObserver2 != null) {
                cursor.registerDataSetObserver(dataSetObserver2);
            }
            this.f10875f = cursor.getColumnIndexOrThrow("_id");
            this.f10871b = true;
            notifyDataSetChanged();
        } else {
            this.f10875f = -1;
            this.f10871b = false;
            notifyDataSetInvalidated();
        }
        return cursor2;
    }

    public AbstractC0752a(Context context, Cursor cursor, boolean z3) {
        f(context, cursor, z3 ? 1 : 2);
    }

    public AbstractC0752a(Context context, Cursor cursor, int i3) {
        f(context, cursor, i3);
    }
}
