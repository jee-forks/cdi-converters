package com.github.rmannibucau.converter.internals;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;

public final class ParameterizedTypeImpl implements ParameterizedType {
    private final Type[] actualTypeArguments;
    private final Type rawType;
    private final Type ownerType;

    public ParameterizedTypeImpl(Type rawType, Type[] actualTypeArguments, Type ownerType) {
        this.actualTypeArguments = actualTypeArguments;
        this.rawType = rawType;
        this.ownerType = ownerType;
    }

    public Type[] getActualTypeArguments() {
        return Arrays.copyOf(actualTypeArguments, actualTypeArguments.length);
    }

    public Type getOwnerType() {
        return ownerType;
    }

    public Type getRawType() {
        return rawType;
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(actualTypeArguments) ^ (ownerType == null ? 0 :
                ownerType.hashCode()) ^ (rawType == null ? 0 : rawType.hashCode());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj instanceof ParameterizedType) {
            final ParameterizedType that = (ParameterizedType) obj;
            final Type thatOwnerType = that.getOwnerType();
            final Type thatRawType = that.getRawType();
            return (ownerType == null ? thatOwnerType == null : ownerType.equals(thatOwnerType))
                    && (rawType == null ? thatRawType == null : rawType.equals(thatRawType))
                    && Arrays.equals(actualTypeArguments, that.getActualTypeArguments());
        }
        return false;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(rawType);
        if (actualTypeArguments.length > 0) {
            sb.append("<");
            for (Type actualType : actualTypeArguments) {
                sb.append(actualType);
                sb.append(",");
            }
            sb.delete(sb.length() - 1, sb.length());
            sb.append(">");
        }
        return sb.toString();
    }
}

