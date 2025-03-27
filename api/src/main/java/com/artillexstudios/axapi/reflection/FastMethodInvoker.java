package com.artillexstudios.axapi.reflection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.CallSite;
import java.lang.invoke.LambdaMetafactory;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class FastMethodInvoker {
    private static final Logger log = LoggerFactory.getLogger(FastMethodInvoker.class);

    public static FastMethodInvoker create(String clazz, String method, Class<?>... parameters) {
        try {
            return create(Class.forName(clazz).getDeclaredMethod(method, parameters));
        } catch (Exception exception) {
            log.error("An unexpected error occurred while creating new FastMethodInvoker for class {}, method {}!", clazz, method, exception);
            throw new RuntimeException(exception);
        }
    }

    public static FastMethodInvoker create(Method method) {
        FastMethodInvoker invoker = null;
        method.setAccessible(true);
        if (method.getParameters().length == 0) {
            java.lang.Class<?> returnType = method.getReturnType();
            if (returnType == void.class) {
                if (Modifier.isStatic(method.getModifiers())) {
                    invoker = new FastStaticRunnableInvoker(method);
                } else {
                    invoker = new FastInstanceRunnableInvoker(method);
                }
            } else {
                if (Modifier.isStatic(method.getModifiers())) {
                    // Returns something but no parameter
                    invoker = new FastStaticSupplierInvoker(method);
                } else {
                    invoker = new FastInstanceSupplierInvoker(method);
                }
            }
        } else if (method.getParameters().length == 1) {
            if (method.getReturnType() != void.class) {
                if (Modifier.isStatic(method.getModifiers())) {
                    // Returns something, 1 parameter
                    invoker = new FastStaticFunctionInvoker(method);
                } else {
                    invoker = new FastInstanceFunctionInvoker(method);
                }
            } else {
                if (Modifier.isStatic(method.getModifiers())) {
                    // Doesn't return, 1 parameter
                    invoker = new FastStaticBiSupplierInvoker(method);
                } else {
                    invoker = new FastInstanceBiSupplierInvoker(method);
                }
            }
        } else if (method.getParameters().length == 2) {
            if (method.getReturnType() != void.class) {
                if (Modifier.isStatic(method.getModifiers())) {
                    // Returns something, 1 parameter
                    invoker = new FastStaticBiFunctionInvoker(method);
                } else {
                    invoker = new FastInstanceBiFunctionInvoker(method);
                }
            }
        }

        // I don't want to do more, this will have to do
        if (invoker == null) {
            invoker = new MethodHandleFallbackInvoker(method);
        }

        return invoker;
    }

    public abstract <T> T invoke(Object instance, Object... a);

    public interface TriFunction<A, B, C, D> {

        A apply(B b, C c, D d);
    }

    private static final class FastStaticRunnableInvoker extends FastMethodInvoker {
        Runnable runnable;

        private FastStaticRunnableInvoker(Method method) {
            try {
                MethodHandle handle = MethodHandles.lookup().unreflect(method);

                CallSite callSite = LambdaMetafactory.metafactory(MethodHandles.lookup(), "run",
                        MethodType.methodType(Runnable.class, MethodHandle.class),
                        MethodType.methodType(void.class),
                        MethodHandles.exactInvoker(handle.type()),
                        MethodType.methodType(void.class));
                runnable = (Runnable) callSite.getTarget().invokeExact(handle);
            } catch (Throwable throwable) {
                throw new RuntimeException(throwable);
            }
        }

        @Override
        public <T> T invoke(Object instance, Object... a) {
            runnable.run();
            return null;
        }
    }

    private static final class FastInstanceRunnableInvoker extends FastMethodInvoker {
        Consumer<Object> consumer;

        private FastInstanceRunnableInvoker(Method method) {
            try {
                MethodHandle handle = MethodHandles.lookup().unreflect(method);

                CallSite callSite = LambdaMetafactory.metafactory(MethodHandles.lookup(),
                        "accept",
                        MethodType.methodType(Consumer.class, MethodHandle.class),
                        MethodType.methodType(void.class, Object.class),
                        MethodHandles.exactInvoker(handle.type()),
                        handle.type());

                consumer = (Consumer<Object>) callSite.getTarget().invokeExact(handle);
            } catch (Throwable throwable) {
                throw new RuntimeException(throwable);
            }
        }

        @Override
        public <T> T invoke(Object instance, Object... a) {
            consumer.accept(instance);
            return null;
        }
    }

    private static final class FastStaticSupplierInvoker extends FastMethodInvoker {
        Supplier<?> supplier;

        private FastStaticSupplierInvoker(Method method) {
            try {
                MethodHandle handle = MethodHandles.lookup().unreflect(method);

                CallSite callSite = LambdaMetafactory.metafactory(MethodHandles.lookup(), "get",
                        MethodType.methodType(Supplier.class, MethodHandle.class),
                        MethodType.methodType(Object.class),
                        MethodHandles.exactInvoker(handle.type()),
                        handle.type());
                supplier = (Supplier<?>) callSite.getTarget().invokeExact(handle);
            } catch (Throwable throwable) {
                throw new RuntimeException(throwable);
            }
        }

        @Override
        public <T> T invoke(Object instance, Object... a) {
            return (T) supplier.get();
        }
    }

    private static final class FastInstanceSupplierInvoker extends FastMethodInvoker {
        Function<Object, Object> supplier;

        private FastInstanceSupplierInvoker(Method method) {
            try {
                MethodHandle handle = MethodHandles.lookup().unreflect(method);

                CallSite callSite = LambdaMetafactory.metafactory(MethodHandles.lookup(), "apply",
                        MethodType.methodType(Function.class, MethodHandle.class),
                        MethodType.methodType(Object.class, Object.class),
                        MethodHandles.exactInvoker(handle.type()),
                        handle.type());
                supplier = (Function<Object, Object>) callSite.getTarget().invokeExact(handle);
            } catch (Throwable throwable) {
                throw new RuntimeException(throwable);
            }
        }

        @Override
        public <T> T invoke(Object instance, Object... a) {
            return (T) supplier.apply(instance);
        }
    }

    private static final class FastStaticBiSupplierInvoker extends FastMethodInvoker {
        Consumer<Object> supplier;

        private FastStaticBiSupplierInvoker(Method method) {
            try {
                MethodHandle handle = MethodHandles.lookup().unreflect(method);

                CallSite callSite = LambdaMetafactory.metafactory(MethodHandles.lookup(), "accept",
                        MethodType.methodType(Consumer.class, MethodHandle.class),
                        MethodType.methodType(void.class, Object.class),
                        MethodHandles.exactInvoker(handle.type()),
                        handle.type());
                supplier = (Consumer<Object>) callSite.getTarget().invokeExact(handle);
            } catch (Throwable throwable) {
                throw new RuntimeException(throwable);
            }
        }

        @Override
        public <T> T invoke(Object instance, Object... a) {
            supplier.accept(a[0]);
            return null;
        }
    }

    private static final class FastInstanceBiSupplierInvoker extends FastMethodInvoker {
        BiConsumer<Object, Object> supplier;

        private FastInstanceBiSupplierInvoker(Method method) {
            try {
                MethodHandle handle = MethodHandles.lookup().unreflect(method);
                CallSite callSite = LambdaMetafactory.metafactory(MethodHandles.lookup(), "accept",
                        MethodType.methodType(BiConsumer.class, MethodHandle.class),
                        MethodType.methodType(void.class, Object.class, Object.class),
                        MethodHandles.exactInvoker(handle.type()),
                        handle.type());
                supplier = (BiConsumer<Object, Object>) callSite.getTarget().invokeExact(handle);
            } catch (Throwable throwable) {
                throw new RuntimeException(throwable);
            }
        }

        @Override
        public <T> T invoke(Object instance, Object... a) {
            supplier.accept(instance, a[0]);
            return null;
        }
    }

    private static final class FastStaticFunctionInvoker extends FastMethodInvoker {
        Function<Object, Object> supplier;

        // 1 parameter, 1 return
        private FastStaticFunctionInvoker(Method method) {
            try {
                MethodHandle handle = MethodHandles.lookup().unreflect(method);

                CallSite callSite = LambdaMetafactory.metafactory(MethodHandles.lookup(), "apply",
                        MethodType.methodType(Function.class, MethodHandle.class),
                        MethodType.methodType(Object.class, Object.class),
                        MethodHandles.exactInvoker(handle.type()),
                        handle.type());
                supplier = (Function<Object, Object>) callSite.getTarget().invokeExact(handle);
            } catch (Throwable throwable) {
                throw new RuntimeException(throwable);
            }
        }

        @Override
        public <T> T invoke(Object instance, Object... a) {
            return (T) supplier.apply(a[0]);
        }
    }

    private static final class FastInstanceFunctionInvoker extends FastMethodInvoker {
        BiFunction<Object, Object, Object> supplier;

        // 1 parameter, 1 return
        private FastInstanceFunctionInvoker(Method method) {
            try {
                MethodHandle handle = MethodHandles.lookup().unreflect(method);

                CallSite callSite = LambdaMetafactory.metafactory(MethodHandles.lookup(), "apply",
                        MethodType.methodType(BiFunction.class, MethodHandle.class),
                        MethodType.methodType(Object.class, Object.class, Object.class),
                        MethodHandles.exactInvoker(handle.type()),
                        handle.type());
                supplier = (BiFunction<Object, Object, Object>) callSite.getTarget().invokeExact(handle);
            } catch (Throwable throwable) {
                throw new RuntimeException(throwable);
            }
        }

        @Override
        public <T> T invoke(Object instance, Object... a) {
            return (T) supplier.apply(instance, a[0]);
        }
    }

    private static final class FastStaticBiFunctionInvoker extends FastMethodInvoker {
        BiFunction<Object, Object, Object> supplier;

        // 2 parameter, 1 return
        private FastStaticBiFunctionInvoker(Method method) {
            try {
                MethodHandle handle = MethodHandles.lookup().unreflect(method);

                CallSite callSite = LambdaMetafactory.metafactory(MethodHandles.lookup(), "apply",
                        MethodType.methodType(BiFunction.class, MethodHandle.class),
                        MethodType.methodType(Object.class, Object.class, Object.class),
                        MethodHandles.exactInvoker(handle.type()),
                        handle.type());
                supplier = (BiFunction<Object, Object, Object>) callSite.getTarget().invokeExact(handle);
            } catch (Throwable throwable) {
                throw new RuntimeException(throwable);
            }
        }

        @Override
        public <T> T invoke(Object instance, Object... a) {
            return (T) supplier.apply(a[0], a[1]);
        }
    }

    private static final class FastInstanceBiFunctionInvoker extends FastMethodInvoker {
        TriFunction<Object, Object, Object, Object> supplier;

        // 2 parameter, 1 return
        private FastInstanceBiFunctionInvoker(Method method) {
            try {
                MethodHandle handle = MethodHandles.lookup().unreflect(method);

                CallSite callSite = LambdaMetafactory.metafactory(MethodHandles.lookup(), "apply",
                        MethodType.methodType(TriFunction.class, MethodHandle.class),
                        MethodType.methodType(Object.class, Object.class, Object.class, Object.class),
                        MethodHandles.exactInvoker(handle.type()),
                        handle.type());
                supplier = (TriFunction<Object, Object, Object, Object>) callSite.getTarget().invokeExact(handle);
            } catch (Throwable throwable) {
                throw new RuntimeException(throwable);
            }
        }

        @Override
        public <T> T invoke(Object instance, Object... a) {
            return (T) supplier.apply(instance, a[0], a[1]);
        }
    }

    private static final class MethodHandleFallbackInvoker extends FastMethodInvoker {
        MethodHandle handle;

        private MethodHandleFallbackInvoker(Method method) {
            try {
                handle = MethodHandles.lookup().unreflect(method);
            } catch (Throwable throwable) {
                throw new RuntimeException(throwable);
            }
        }

        @Override
        public <T> T invoke(Object instance, Object... a) {
            try {
                return (T) handle.invoke(instance, a);
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        }
    }
}
