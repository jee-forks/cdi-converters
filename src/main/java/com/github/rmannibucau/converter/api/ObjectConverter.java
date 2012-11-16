package com.github.rmannibucau.converter.api;

import java.util.Collection;

public interface ObjectConverter<FROM, TO> {
    TO to(FROM from);
    Collection<TO> to(Collection<FROM> froms);
}
