[![Build Status](https://secure.travis-ci.org/rmannibucau/cdi-converters.png)](http://travis-ci.org/rmannibucau/cdi-converters)

Goal
====

Define converters (static method or not) then be able to use them for
single object or collections through ObjectConverter interface.

Example
=======


    public static class LenConverter {
        // static method
        
        @Converter(in = In.class, out = Out.class)
        public static Out convert(final In in) {
            return new Out(in.value.length());
        }

        // or use completely CDI

        @Inject
        private Helper helper;

        @Converter(in = String.class, out = String.class)
        public String convert(final String in) {
            return helper.foo(in);
        }
    }

then in another bean:

    @Inject
    @Converter(in = In.class, out = Out.class)
    private ObjectConverter<In, Out> len;

    // ...
    assertEquals(3, len.to(new In("foo")).len);
