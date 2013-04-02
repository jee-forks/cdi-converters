package com.github.rmannibucau.converter.internals;

import com.github.rmannibucau.converter.api.Converter;
import com.github.rmannibucau.converter.api.ObjectConverter;
import org.apache.deltaspike.core.util.bean.BeanBuilder;
import org.apache.deltaspike.core.util.metadata.AnnotationInstanceProvider;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.spi.AfterBeanDiscovery;
import javax.enterprise.inject.spi.AnnotatedMethod;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessManagedBean;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class ConverterExtension implements Extension {
    private static final Logger LOGGER = Logger.getLogger(ConverterExtension.class.getName());

    private final Collection<Bean<Object>> beans = new ArrayList<Bean<Object>>();

    protected <X> void processBean(final @Observes ProcessManagedBean<X> pb, final BeanManager bm) {
        for (final AnnotatedMethod<?> method : pb.getAnnotatedBeanClass().getMethods()) {
            final Converter converter = method.getAnnotation(Converter.class);
            if (converter != null) {
                if (method.getParameters().size() == 1) {
                    final Class<?> in = method.getJavaMember().getParameterTypes()[0];
                    final Class<?> out = method.getJavaMember().getReturnType();

                    final Map<String, Class<?>> params = new HashMap<String, Class<?>>();
                    params.put("in", in);
                    params.put("out", out);

                    beans.add(new BeanBuilder<Object>(bm)
                            .beanClass(ObjectConverter.class)
                            .scope(ApplicationScoped.class)
                            .alternative(true)
                            .types(new ParameterizedTypeImpl(ObjectConverter.class, new Type[] { in, out }, ObjectConverter.class), Object.class)
                            .qualifiers(AnnotationInstanceProvider.of(Converter.class, params), AnnotationInstanceProvider.of(Any.class))
                            .beanLifecycle(new ConverterLifecycle(Bean.class.cast(pb.getBean()), method.getJavaMember()))
                            .passivationCapable(false)
                            .create());
                } else {
                    LOGGER.severe("ObjectConverter method can't get more than one parameter and should be static ("
                                            + method.getJavaMember().toGenericString() + ")");
                }
            }
        }
    }

    protected void addConverters(final @Observes AfterBeanDiscovery abd) {
        for (final Bean<Object> bean : beans) {
            abd.addBean(bean);
        }
        beans.clear();
    }
}
