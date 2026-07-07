package com.facebook.react.modules.image;

import D2.h;
import E1.b;
import J0.C0185t;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.SparseArray;
import b0.AbstractC0306a;
import com.facebook.fbreact.specs.NativeImageLoaderAndroidSpec;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.BaseJavaModule;
import com.facebook.react.bridge.GuardedAsyncTask;
import com.facebook.react.bridge.LifecycleEventListener;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.views.image.f;
import h0.AbstractC0547b;
import h0.InterfaceC0548c;
import kotlin.jvm.internal.DefaultConstructorMarker;
import m0.AbstractC0601d;
import r2.r;
import v1.InterfaceC0756a;

/* JADX INFO: loaded from: classes.dex */
@InterfaceC0756a(name = "ImageLoader")
public final class ImageLoaderModule extends NativeImageLoaderAndroidSpec implements LifecycleEventListener {
    public static final a Companion = new a(null);
    private static final String ERROR_GET_SIZE_FAILURE = "E_GET_SIZE_FAILURE";
    private static final String ERROR_INVALID_URI = "E_INVALID_URI";
    private static final String ERROR_PREFETCH_FAILURE = "E_PREFETCH_FAILURE";
    public static final String NAME = "ImageLoader";
    private C0185t _imagePipeline;
    private final Object callerContext;
    private f callerContextFactory;
    private final Object enqueuedRequestMonitor;
    private final SparseArray<InterfaceC0548c> enqueuedRequests;

    public static final class a {
        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private a() {
        }
    }

    public static final class b extends AbstractC0547b {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        final /* synthetic */ Promise f6979a;

        b(Promise promise) {
            this.f6979a = promise;
        }

        @Override // h0.AbstractC0547b
        protected void e(InterfaceC0548c interfaceC0548c) {
            h.f(interfaceC0548c, "dataSource");
            this.f6979a.reject(ImageLoaderModule.ERROR_GET_SIZE_FAILURE, interfaceC0548c.f());
        }

        @Override // h0.AbstractC0547b
        protected void f(InterfaceC0548c interfaceC0548c) {
            h.f(interfaceC0548c, "dataSource");
            if (interfaceC0548c.e()) {
                AbstractC0306a abstractC0306a = (AbstractC0306a) interfaceC0548c.b();
                try {
                    if (abstractC0306a == null) {
                        this.f6979a.reject(ImageLoaderModule.ERROR_GET_SIZE_FAILURE, "Failed to get the size of the image");
                        return;
                    }
                    try {
                        Object objP = abstractC0306a.P();
                        h.e(objP, "get(...)");
                        O0.d dVar = (O0.d) objP;
                        WritableMap writableMapCreateMap = Arguments.createMap();
                        h.e(writableMapCreateMap, "createMap(...)");
                        writableMapCreateMap.putInt("width", dVar.h());
                        writableMapCreateMap.putInt("height", dVar.d());
                        this.f6979a.resolve(writableMapCreateMap);
                    } catch (Exception e4) {
                        this.f6979a.reject(ImageLoaderModule.ERROR_GET_SIZE_FAILURE, e4);
                    }
                } finally {
                    AbstractC0306a.D(abstractC0306a);
                }
            }
        }
    }

    public static final class c extends AbstractC0547b {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        final /* synthetic */ Promise f6980a;

        c(Promise promise) {
            this.f6980a = promise;
        }

        @Override // h0.AbstractC0547b
        protected void e(InterfaceC0548c interfaceC0548c) {
            h.f(interfaceC0548c, "dataSource");
            this.f6980a.reject(ImageLoaderModule.ERROR_GET_SIZE_FAILURE, interfaceC0548c.f());
        }

        @Override // h0.AbstractC0547b
        protected void f(InterfaceC0548c interfaceC0548c) {
            h.f(interfaceC0548c, "dataSource");
            if (interfaceC0548c.e()) {
                AbstractC0306a abstractC0306a = (AbstractC0306a) interfaceC0548c.b();
                try {
                    if (abstractC0306a == null) {
                        this.f6980a.reject(ImageLoaderModule.ERROR_GET_SIZE_FAILURE, "Failed to get the size of the image");
                        return;
                    }
                    try {
                        Object objP = abstractC0306a.P();
                        h.e(objP, "get(...)");
                        O0.d dVar = (O0.d) objP;
                        WritableMap writableMapCreateMap = Arguments.createMap();
                        h.e(writableMapCreateMap, "createMap(...)");
                        writableMapCreateMap.putInt("width", dVar.h());
                        writableMapCreateMap.putInt("height", dVar.d());
                        this.f6980a.resolve(writableMapCreateMap);
                    } catch (Exception e4) {
                        this.f6980a.reject(ImageLoaderModule.ERROR_GET_SIZE_FAILURE, e4);
                    }
                } finally {
                    AbstractC0306a.D(abstractC0306a);
                }
            }
        }
    }

    public static final class d extends AbstractC0547b {

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        final /* synthetic */ int f6982b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        final /* synthetic */ Promise f6983c;

        d(int i3, Promise promise) {
            this.f6982b = i3;
            this.f6983c = promise;
        }

        @Override // h0.AbstractC0547b
        protected void e(InterfaceC0548c interfaceC0548c) {
            h.f(interfaceC0548c, "dataSource");
            try {
                ImageLoaderModule.this.removeRequest(this.f6982b);
                this.f6983c.reject(ImageLoaderModule.ERROR_PREFETCH_FAILURE, interfaceC0548c.f());
            } finally {
                interfaceC0548c.close();
            }
        }

        @Override // h0.AbstractC0547b
        protected void f(InterfaceC0548c interfaceC0548c) {
            h.f(interfaceC0548c, "dataSource");
            if (interfaceC0548c.e()) {
                try {
                    try {
                        ImageLoaderModule.this.removeRequest(this.f6982b);
                        this.f6983c.resolve(Boolean.TRUE);
                    } catch (Exception e4) {
                        this.f6983c.reject(ImageLoaderModule.ERROR_PREFETCH_FAILURE, e4);
                    }
                } finally {
                    interfaceC0548c.close();
                }
            }
        }
    }

    public static final class e extends GuardedAsyncTask {

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        final /* synthetic */ ReadableArray f6985b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        final /* synthetic */ Promise f6986c;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        e(ReadableArray readableArray, Promise promise, ReactApplicationContext reactApplicationContext) {
            super(reactApplicationContext);
            this.f6985b = readableArray;
            this.f6986c = promise;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.facebook.react.bridge.GuardedAsyncTask
        /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
        public void doInBackgroundGuarded(Void... voidArr) {
            h.f(voidArr, "params");
            WritableMap writableMapCreateMap = Arguments.createMap();
            h.e(writableMapCreateMap, "createMap(...)");
            C0185t imagePipeline = ImageLoaderModule.this.getImagePipeline();
            int size = this.f6985b.size();
            for (int i3 = 0; i3 < size; i3++) {
                String string = this.f6985b.getString(i3);
                if (string != null && string.length() != 0) {
                    Uri uri = Uri.parse(string);
                    if (imagePipeline.r(uri)) {
                        writableMapCreateMap.putString(string, "memory");
                    } else if (imagePipeline.t(uri)) {
                        writableMapCreateMap.putString(string, "disk");
                    }
                }
            }
            this.f6986c.resolve(writableMapCreateMap);
        }
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public ImageLoaderModule(ReactApplicationContext reactApplicationContext) {
        super(reactApplicationContext);
        h.f(reactApplicationContext, "reactContext");
        this.enqueuedRequestMonitor = new Object();
        this.enqueuedRequests = new SparseArray<>();
        this.callerContext = this;
    }

    private final Object getCallerContext() {
        return this.callerContext;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final C0185t getImagePipeline() {
        C0185t c0185t = this._imagePipeline;
        if (c0185t != null) {
            return c0185t;
        }
        C0185t c0185tA = AbstractC0601d.a();
        h.e(c0185tA, "getImagePipeline(...)");
        return c0185tA;
    }

    private final void registerRequest(int i3, InterfaceC0548c interfaceC0548c) {
        synchronized (this.enqueuedRequestMonitor) {
            this.enqueuedRequests.put(i3, interfaceC0548c);
            r rVar = r.f10584a;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final InterfaceC0548c removeRequest(int i3) {
        InterfaceC0548c interfaceC0548c;
        synchronized (this.enqueuedRequestMonitor) {
            interfaceC0548c = this.enqueuedRequests.get(i3);
            this.enqueuedRequests.remove(i3);
        }
        return interfaceC0548c;
    }

    private final void setImagePipeline(C0185t c0185t) {
        this._imagePipeline = c0185t;
    }

    @Override // com.facebook.fbreact.specs.NativeImageLoaderAndroidSpec
    public void abortRequest(double d4) {
        InterfaceC0548c interfaceC0548cRemoveRequest = removeRequest((int) d4);
        if (interfaceC0548cRemoveRequest != null) {
            interfaceC0548cRemoveRequest.close();
        }
    }

    @Override // com.facebook.fbreact.specs.NativeImageLoaderAndroidSpec
    @ReactMethod
    public void getSize(String str, Promise promise) {
        h.f(promise, BaseJavaModule.METHOD_TYPE_PROMISE);
        if (str == null || str.length() == 0) {
            promise.reject(ERROR_INVALID_URI, "Cannot get the size of an image for an empty URI");
            return;
        }
        ReactApplicationContext reactApplicationContext = getReactApplicationContext();
        h.e(reactApplicationContext, "getReactApplicationContext(...)");
        U0.b bVarA = U0.c.x(new X1.a(reactApplicationContext, str, 0.0d, 0.0d, null, 28, null).f()).a();
        h.e(bVarA, "build(...)");
        getImagePipeline().k(bVarA, getCallerContext()).h(new b(promise), V.a.b());
    }

    @Override // com.facebook.fbreact.specs.NativeImageLoaderAndroidSpec
    @ReactMethod
    public void getSizeWithHeaders(String str, ReadableMap readableMap, Promise promise) {
        h.f(promise, BaseJavaModule.METHOD_TYPE_PROMISE);
        if (str == null || str.length() == 0) {
            promise.reject(ERROR_INVALID_URI, "Cannot get the size of an image for an empty URI");
            return;
        }
        ReactApplicationContext reactApplicationContext = getReactApplicationContext();
        h.e(reactApplicationContext, "getReactApplicationContext(...)");
        U0.c cVarX = U0.c.x(new X1.a(reactApplicationContext, str, 0.0d, 0.0d, null, 28, null).f());
        h.e(cVarX, "newBuilderWithSource(...)");
        getImagePipeline().k(b.a.c(E1.b.f202D, cVarX, readableMap, null, 4, null), getCallerContext()).h(new c(promise), V.a.b());
    }

    @Override // com.facebook.react.bridge.LifecycleEventListener
    public void onHostDestroy() {
        synchronized (this.enqueuedRequestMonitor) {
            try {
                int size = this.enqueuedRequests.size();
                for (int i3 = 0; i3 < size; i3++) {
                    InterfaceC0548c interfaceC0548cValueAt = this.enqueuedRequests.valueAt(i3);
                    h.e(interfaceC0548cValueAt, "valueAt(...)");
                    interfaceC0548cValueAt.close();
                }
                this.enqueuedRequests.clear();
                r rVar = r.f10584a;
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    @Override // com.facebook.react.bridge.LifecycleEventListener
    public void onHostPause() {
    }

    @Override // com.facebook.react.bridge.LifecycleEventListener
    public void onHostResume() {
    }

    @Override // com.facebook.fbreact.specs.NativeImageLoaderAndroidSpec
    public void prefetchImage(String str, double d4, Promise promise) {
        h.f(promise, BaseJavaModule.METHOD_TYPE_PROMISE);
        int i3 = (int) d4;
        if (str == null || str.length() == 0) {
            promise.reject(ERROR_INVALID_URI, "Cannot prefetch an image for an empty URI");
            return;
        }
        U0.b bVarA = U0.c.x(Uri.parse(str)).a();
        h.e(bVarA, "build(...)");
        InterfaceC0548c interfaceC0548cY = getImagePipeline().y(bVarA, getCallerContext());
        d dVar = new d(i3, promise);
        registerRequest(i3, interfaceC0548cY);
        interfaceC0548cY.h(dVar, V.a.b());
    }

    @Override // com.facebook.fbreact.specs.NativeImageLoaderAndroidSpec
    @ReactMethod
    public void queryCache(ReadableArray readableArray, Promise promise) {
        h.f(readableArray, "uris");
        h.f(promise, BaseJavaModule.METHOD_TYPE_PROMISE);
        new e(readableArray, promise, getReactApplicationContext()).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public ImageLoaderModule(ReactApplicationContext reactApplicationContext, Object obj) {
        super(reactApplicationContext);
        h.f(reactApplicationContext, "reactContext");
        this.enqueuedRequestMonitor = new Object();
        this.enqueuedRequests = new SparseArray<>();
        this.callerContext = obj;
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public ImageLoaderModule(ReactApplicationContext reactApplicationContext, C0185t c0185t, f fVar) {
        super(reactApplicationContext);
        h.f(reactApplicationContext, "reactContext");
        h.f(c0185t, "imagePipeline");
        h.f(fVar, "callerContextFactory");
        this.enqueuedRequestMonitor = new Object();
        this.enqueuedRequests = new SparseArray<>();
        setImagePipeline(c0185t);
        this.callerContext = null;
    }
}
