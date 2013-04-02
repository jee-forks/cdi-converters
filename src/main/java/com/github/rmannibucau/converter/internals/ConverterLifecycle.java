package com.github.rmannibucau.converter.internals;

import org.apache.deltaspike.core.util.metadata.builder.ContextualLifecycle;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class ConverterLifecycle implements ContextualLifecycle<Object> {
    private final Bean<Object> bean;
    private final Method delegate;

    public ConverterLifecycle(final Bean<Object> bean, final Method javaMember) {
        this.delegate = javaMember;

        if (Modifier.isStatic(javaMember.getModifiers())) {
            this.bean = null;
        } else {
            this.bean = bean;
        }
    }

    @Override
    public Object create(final Bean<Object> beanIn, final CreationalContext<Object> creationalContext) {
        return new ObjectConverterImpl<Object, Object>(bean, delegate);
    }

    @Override
    public void destroy(final Bean<Object> bean, final Object instance, final CreationalContext<Object> creationalContext) {
        // no-op
    }
}
