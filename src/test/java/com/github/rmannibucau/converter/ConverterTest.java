package com.github.rmannibucau.converter;

import com.github.rmannibucau.converter.api.Converter;
import com.github.rmannibucau.converter.api.ObjectConverter;
import com.github.rmannibucau.converter.internals.ConverterExtension;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.enterprise.inject.spi.Extension;
import javax.inject.Inject;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(Arquillian.class)
public class ConverterTest {
    @Deployment
    public static WebArchive war() {
        return ShrinkWrap.create(WebArchive.class, "cdi-converters.war")
                .addClasses(StringHolder.class, IdentityConverter.class, PrefixConverter.class)
                .addAsWebInfResource(EmptyAsset.INSTANCE, ArchivePaths.create("beans.xml"))
                .addAsLibraries(ShrinkWrap.create(JavaArchive.class)
                        .addPackages(false, Converter.class.getPackage(), ConverterExtension.class.getPackage())
                        .addAsServiceProvider(Extension.class, ConverterExtension.class)
                        .addAsManifestResource(EmptyAsset.INSTANCE, ArchivePaths.create("beans.xml")));
    }

    @Inject
    @Converter(in = StringHolder.class, out = StringHolder.class)
    private ObjectConverter<StringHolder, StringHolder> identity;

    @Inject
    @Converter(in = StringHolder.class, out = String.class)
    private ObjectConverter<StringHolder, String> prefix;

    @Inject
    @Converter(in = In.class, out = Out.class)
    private ObjectConverter<In, Out> len;

    @Test
    public void simple() {
        assertNotNull(identity);
        assertEquals("foo", identity.to(new StringHolder("foo")).value);
        assertEquals("_foo", prefix.to(new StringHolder("foo")));
        assertEquals(3, len.to(new In("foo")).len);
    }

    @Test
    public void collection() {
        final Collection<In> input = Arrays.asList(new In("a"), new In("aa"), new In("aaa"));

        final Collection<Out> output = len.to(input);
        assertNotNull(output);
        assertEquals(3, output.size());

        final Iterator<Out> it = output.iterator();
        for (int i = 1; i <= output.size(); i++) {
            assertEquals(i, it.next().len);
        }
    }

    public static class StringHolder {
        public String value;

        public StringHolder(final String value) {
            this.value = value;
        }
    }

    public static class In {
        public String value;

        public In(final String foo) {
            value = foo;
        }
    }

    public static class Out {
        public int len;

        public Out(final int length) {
            len = length;
        }
    }

    public static class IdentityConverter {
        @Converter(in = StringHolder.class, out = StringHolder.class)
        public static StringHolder convert(final StringHolder in) {
            return new StringHolder(in.value);
        }
    }

    public static class PrefixConverter {
        @Converter(in = StringHolder.class, out = String.class)
        public static String convert(final StringHolder in) {
            return "_" + in.value;
        }
    }

    public static class LenConverter {
        @Converter(in = In.class, out = Out.class)
        public static Out convert(final In in) {
            return new Out(in.value.length());
        }
    }
}
