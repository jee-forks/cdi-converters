package com.github.rmannibucau.converter.api;

import java.util.Collection;
import java.util.Map;

public interface ObjectConverter<FROM, TO> {
    TO to(FROM from);
    Collection<TO> to(Collection<FROM> froms);
    <K> Map<K, TO> to(Collection<FROM> froms, String keyAttribute);
}
