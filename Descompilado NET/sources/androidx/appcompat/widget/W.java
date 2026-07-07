package androidx.appcompat.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.transition.Transition;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.HeaderViewListAdapter;
import android.widget.ListAdapter;
import android.widget.PopupWindow;
import androidx.appcompat.view.menu.ListMenuItemView;
import java.lang.reflect.Method;

/* JADX INFO: loaded from: classes.dex */
public class W extends U implements V {

    /* JADX INFO: renamed from: K, reason: collision with root package name */
    private static Method f4030K;

    /* JADX INFO: renamed from: J, reason: collision with root package name */
    private V f4031J;

    static class a {
        static void a(PopupWindow popupWindow, Transition transition) {
            popupWindow.setEnterTransition(transition);
        }

        static void b(PopupWindow popupWindow, Transition transition) {
            popupWindow.setExitTransition(transition);
        }
    }

    static class b {
        static void a(PopupWindow popupWindow, boolean z3) {
            popupWindow.setTouchModal(z3);
        }
    }

    public static class c extends P {

        /* JADX INFO: renamed from: o, reason: collision with root package name */
        final int f4032o;

        /* JADX INFO: renamed from: p, reason: collision with root package name */
        final int f4033p;

        /* JADX INFO: renamed from: q, reason: collision with root package name */
        private V f4034q;

        /* JADX INFO: renamed from: r, reason: collision with root package name */
        private MenuItem f4035r;

        public c(Context context, boolean z3) {
            super(context, z3);
            if (1 == context.getResources().getConfiguration().getLayoutDirection()) {
                this.f4032o = 21;
                this.f4033p = 22;
            } else {
                this.f4032o = 22;
                this.f4033p = 21;
            }
        }

        @Override // androidx.appcompat.widget.P
        public /* bridge */ /* synthetic */ int d(int i3, int i4, int i5, int i6, int i7) {
            return super.d(i3, i4, i5, i6, i7);
        }

        @Override // androidx.appcompat.widget.P
        public /* bridge */ /* synthetic */ boolean e(MotionEvent motionEvent, int i3) {
            return super.e(motionEvent, i3);
        }

        @Override // androidx.appcompat.widget.P, android.view.ViewGroup, android.view.View
        public /* bridge */ /* synthetic */ boolean hasFocus() {
            return super.hasFocus();
        }

        @Override // androidx.appcompat.widget.P, android.view.View
        public /* bridge */ /* synthetic */ boolean hasWindowFocus() {
            return super.hasWindowFocus();
        }

        @Override // androidx.appcompat.widget.P, android.view.View
        public /* bridge */ /* synthetic */ boolean isFocused() {
            return super.isFocused();
        }

        @Override // androidx.appcompat.widget.P, android.view.View
        public /* bridge */ /* synthetic */ boolean isInTouchMode() {
            return super.isInTouchMode();
        }

        @Override // androidx.appcompat.widget.P, android.view.View
        public boolean onHoverEvent(MotionEvent motionEvent) {
            androidx.appcompat.view.menu.d dVar;
            int headersCount;
            int iPointToPosition;
            int i3;
            if (this.f4034q != null) {
                ListAdapter adapter = getAdapter();
                if (adapter instanceof HeaderViewListAdapter) {
                    HeaderViewListAdapter headerViewListAdapter = (HeaderViewListAdapter) adapter;
                    headersCount = headerViewListAdapter.getHeadersCount();
                    dVar = (androidx.appcompat.view.menu.d) headerViewListAdapter.getWrappedAdapter();
                } else {
                    dVar = (androidx.appcompat.view.menu.d) adapter;
                    headersCount = 0;
                }
                androidx.appcompat.view.menu.g item = (motionEvent.getAction() == 10 || (iPointToPosition = pointToPosition((int) motionEvent.getX(), (int) motionEvent.getY())) == -1 || (i3 = iPointToPosition - headersCount) < 0 || i3 >= dVar.getCount()) ? null : dVar.getItem(i3);
                MenuItem menuItem = this.f4035r;
                if (menuItem != item) {
                    androidx.appcompat.view.menu.e eVarB = dVar.b();
                    if (menuItem != null) {
                        this.f4034q.e(eVarB, menuItem);
                    }
                    this.f4035r = item;
                    if (item != null) {
                        this.f4034q.d(eVarB, item);
                    }
                }
            }
            return super.onHoverEvent(motionEvent);
        }

        @Override // android.widget.ListView, android.widget.AbsListView, android.view.View, android.view.KeyEvent.Callback
        public boolean onKeyDown(int i3, KeyEvent keyEvent) {
            ListMenuItemView listMenuItemView = (ListMenuItemView) getSelectedView();
            if (listMenuItemView != null && i3 == this.f4032o) {
                if (listMenuItemView.isEnabled() && listMenuItemView.getItemData().hasSubMenu()) {
                    performItemClick(listMenuItemView, getSelectedItemPosition(), getSelectedItemId());
                }
                return true;
            }
            if (listMenuItemView == null || i3 != this.f4033p) {
                return super.onKeyDown(i3, keyEvent);
            }
            setSelection(-1);
            ListAdapter adapter = getAdapter();
            (adapter instanceof HeaderViewListAdapter ? (androidx.appcompat.view.menu.d) ((HeaderViewListAdapter) adapter).getWrappedAdapter() : (androidx.appcompat.view.menu.d) adapter).b().e(false);
            return true;
        }

        @Override // androidx.appcompat.widget.P, android.widget.AbsListView, android.view.View
        public /* bridge */ /* synthetic */ boolean onTouchEvent(MotionEvent motionEvent) {
            return super.onTouchEvent(motionEvent);
        }

        public void setHoverListener(V v3) {
            this.f4034q = v3;
        }

        @Override // androidx.appcompat.widget.P, android.widget.AbsListView
        public /* bridge */ /* synthetic */ void setSelector(Drawable drawable) {
            super.setSelector(drawable);
        }
    }

    static {
        try {
            if (Build.VERSION.SDK_INT <= 28) {
                f4030K = PopupWindow.class.getDeclaredMethod("setTouchModal", Boolean.TYPE);
            }
        } catch (NoSuchMethodException unused) {
            Log.i("MenuPopupWindow", "Could not find method setTouchModal() on PopupWindow. Oh well.");
        }
    }

    public W(Context context, AttributeSet attributeSet, int i3, int i4) {
        super(context, attributeSet, i3, i4);
    }

    public void N(Object obj) {
        a.a(this.f3993G, (Transition) obj);
    }

    public void O(Object obj) {
        a.b(this.f3993G, (Transition) obj);
    }

    public void P(V v3) {
        this.f4031J = v3;
    }

    public void Q(boolean z3) {
        if (Build.VERSION.SDK_INT > 28) {
            b.a(this.f3993G, z3);
            return;
        }
        Method method = f4030K;
        if (method != null) {
            try {
                method.invoke(this.f3993G, Boolean.valueOf(z3));
            } catch (Exception unused) {
                Log.i("MenuPopupWindow", "Could not invoke setTouchModal() on PopupWindow. Oh well.");
            }
        }
    }

    @Override // androidx.appcompat.widget.V
    public void d(androidx.appcompat.view.menu.e eVar, MenuItem menuItem) {
        V v3 = this.f4031J;
        if (v3 != null) {
            v3.d(eVar, menuItem);
        }
    }

    @Override // androidx.appcompat.widget.V
    public void e(androidx.appcompat.view.menu.e eVar, MenuItem menuItem) {
        V v3 = this.f4031J;
        if (v3 != null) {
            v3.e(eVar, menuItem);
        }
    }

    @Override // androidx.appcompat.widget.U
    P s(Context context, boolean z3) {
        c cVar = new c(context, z3);
        cVar.setHoverListener(this);
        return cVar;
    }
}
