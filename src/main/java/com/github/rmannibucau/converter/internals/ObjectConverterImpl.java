package com.github.rmannibucau.converter.internals;

import com.github.rmannibucau.converter.api.ObjectConverter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;

public class ObjectConverterImpl<FROM, TO> implements ObjectConverter<FROM, TO> {
    private final Method convertMethod;

    public ObjectConverterImpl(Method convertMethod) {
        this.convertMethod = convertMethod;
    }

    @Override
    public TO to(final FROM from) {
        try {
            return (TO) convertMethod.invoke(null, from);
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException(e);
        } catch (InvocationTargetException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public Collection<TO> to(final Collection<FROM> froms) {
        if (froms == null) {
            return null;
        }

        final Collection<TO> out = new ArrayList<TO>();
        for (FROM from : froms) {
            out.add(to(from));
        }
        return out;
    }
}
