package fr.icodem.asciidoc.parser.peg.example.asciidoc.listener;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.LinkedList;
import java.util.Queue;

public class DeferredHandler implements InvocationHandler {

    private Queue<Runnable> queue;
    private AsciidocHandler delegate;
    private AsciidocHandler proxy;

    public static DeferredHandler of(AsciidocHandler delegate) {

        DeferredHandler handler = new DeferredHandler(delegate);

        handler.proxy = (AsciidocHandler) Proxy.newProxyInstance(AsciidocHandler.class.getClassLoader(),
                new Class[] {AsciidocHandler.class}, handler);

        return handler;
    }

    private DeferredHandler(AsciidocHandler delegate) {
        this.delegate = delegate;
        this.queue = new LinkedList<>();
    }

    public void add(Runnable runnable) {
        queue.add(runnable);
    }

    public void flush() {
        queue.forEach(Runnable::run);
        queue.clear();
    }

    public AsciidocHandler getProxy() {
        return proxy;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        queue.add(() -> getDeferredInvocation(method, args));

        return null;
    }

    private Object getDeferredInvocation(Method method, Object[] args) {
        Object result;
        try {
            result = method.invoke(delegate, args);
        } catch (Exception e) {
            throw new DeferredHandlerException("Failed invocating : " + method.getName(), e);
        }
        return result;
    }
}
