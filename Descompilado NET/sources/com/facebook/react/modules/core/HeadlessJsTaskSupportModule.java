package com.facebook.react.modules.core;

import D2.h;
import com.facebook.fbreact.specs.NativeHeadlessJsTaskSupportSpec;
import com.facebook.react.bridge.BaseJavaModule;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import u1.C0742e;
import v1.InterfaceC0756a;

/* JADX INFO: loaded from: classes.dex */
@InterfaceC0756a(name = NativeHeadlessJsTaskSupportSpec.NAME)
public class HeadlessJsTaskSupportModule extends NativeHeadlessJsTaskSupportSpec {
    public HeadlessJsTaskSupportModule(ReactApplicationContext reactApplicationContext) {
        super(reactApplicationContext);
    }

    @Override // com.facebook.fbreact.specs.NativeHeadlessJsTaskSupportSpec
    public void notifyTaskFinished(double d4) {
        int i3 = (int) d4;
        C0742e.a aVar = C0742e.f10859g;
        ReactApplicationContext reactApplicationContext = getReactApplicationContext();
        h.e(reactApplicationContext, "getReactApplicationContext(...)");
        C0742e c0742eA = aVar.a(reactApplicationContext);
        if (c0742eA.i(i3)) {
            c0742eA.f(i3);
        } else {
            Y.a.G(HeadlessJsTaskSupportModule.class, "Tried to finish non-active task with id %d. Did it time out?", Integer.valueOf(i3));
        }
    }

    @Override // com.facebook.fbreact.specs.NativeHeadlessJsTaskSupportSpec
    public void notifyTaskRetry(double d4, Promise promise) {
        h.f(promise, BaseJavaModule.METHOD_TYPE_PROMISE);
        int i3 = (int) d4;
        C0742e.a aVar = C0742e.f10859g;
        ReactApplicationContext reactApplicationContext = getReactApplicationContext();
        h.e(reactApplicationContext, "getReactApplicationContext(...)");
        C0742e c0742eA = aVar.a(reactApplicationContext);
        if (c0742eA.i(i3)) {
            promise.resolve(Boolean.valueOf(c0742eA.l(i3)));
        } else {
            Y.a.G(HeadlessJsTaskSupportModule.class, "Tried to retry non-active task with id %d. Did it time out?", Integer.valueOf(i3));
            promise.resolve(Boolean.FALSE);
        }
    }
}
