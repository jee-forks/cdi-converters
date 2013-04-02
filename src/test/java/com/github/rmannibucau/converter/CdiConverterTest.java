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

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.spi.Extension;
import javax.inject.Inject;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(Arquillian.class)
public class CdiConverterTest {
    @Deployment
    public static WebArchive war() {
        return ShrinkWrap.create(WebArchive.class, "cdi-converters.war")
                .addClasses(Helper.class, ReverseConverter.class)
                .addAsWebInfResource(EmptyAsset.INSTANCE, ArchivePaths.create("beans.xml"))
                .addAsLibraries(ShrinkWrap.create(JavaArchive.class)
                        .addPackages(false, Converter.class.getPackage(), ConverterExtension.class.getPackage())
                        .addAsServiceProvider(Extension.class, ConverterExtension.class)
                        .addAsManifestResource(EmptyAsset.INSTANCE, ArchivePaths.create("beans.xml")));
    }

    @Inject
    @Converter(in = String.class, out = String.class)
    private ObjectConverter<String, String> reverse;

    @Test
    public void simple() {
        assertNotNull(reverse);
        assertEquals("cba", reverse.to("abc"));
    }

    public static class ReverseConverter {
        @Inject
        private Helper helper;

        @Converter(in = String.class, out = String.class)
        public String convert(final String in) {
            return helper.foo(in);
        }
    }

    @ApplicationScoped
    public static class Helper {
        public String foo(final String in) {
            return new StringBuilder(in).reverse().toString();
        }
    }
}
