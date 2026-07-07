package androidx.lifecycle;

import android.app.Application;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import s2.AbstractC0711h;
import s2.AbstractC0717n;

/* JADX INFO: loaded from: classes.dex */
public abstract class C {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private static final List f5258a = AbstractC0717n.j(Application.class, x.class);

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private static final List f5259b = AbstractC0717n.b(x.class);

    public static final Constructor c(Class cls, List list) {
        D2.h.f(cls, "modelClass");
        D2.h.f(list, "signature");
        Constructor<?>[] constructors = cls.getConstructors();
        D2.h.e(constructors, "modelClass.constructors");
        for (Constructor<?> constructor : constructors) {
            Class<?>[] parameterTypes = constructor.getParameterTypes();
            D2.h.e(parameterTypes, "constructor.parameterTypes");
            List listB = AbstractC0711h.B(parameterTypes);
            if (D2.h.b(list, listB)) {
                D2.h.d(constructor, "null cannot be cast to non-null type java.lang.reflect.Constructor<T of androidx.lifecycle.SavedStateViewModelFactoryKt.findMatchingConstructor>");
                return constructor;
            }
            if (list.size() == listB.size() && listB.containsAll(list)) {
                throw new UnsupportedOperationException("Class " + cls.getSimpleName() + " must have parameters in the proper order: " + list);
            }
        }
        return null;
    }

    public static final D d(Class cls, Constructor constructor, Object... objArr) {
        D2.h.f(cls, "modelClass");
        D2.h.f(constructor, "constructor");
        D2.h.f(objArr, "params");
        try {
            return (D) constructor.newInstance(Arrays.copyOf(objArr, objArr.length));
        } catch (IllegalAccessException e4) {
            throw new RuntimeException("Failed to access " + cls, e4);
        } catch (InstantiationException e5) {
            throw new RuntimeException("A " + cls + " cannot be instantiated.", e5);
        } catch (InvocationTargetException e6) {
            throw new RuntimeException("An exception happened in constructor of " + cls, e6.getCause());
        }
    }
}
