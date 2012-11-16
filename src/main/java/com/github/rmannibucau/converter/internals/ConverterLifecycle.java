package com.github.rmannibucau.converter.internals;

import org.apache.deltaspike.core.util.metadata.builder.ContextualLifecycle;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import java.lang.reflect.Method;

public class ConverterLifecycle implements ContextualLifecycle<Object> {
    private final Method delegate;

    public ConverterLifecycle(final Method javaMember) {
        delegate = javaMember;
    }

    @Override
    public Object create(Bean<Object> bean, CreationalContext<Object> creationalContext) {
        return new ObjectConverterImpl<Object, Object>(delegate);
    }

    @Override
    public void destroy(final Bean<Object> bean, final Object instance, final CreationalContext<Object> creationalContext) {
        // no-op
    }
}
