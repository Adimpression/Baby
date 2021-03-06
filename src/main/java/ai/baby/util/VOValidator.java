package ai.baby.util;

import ai.scribble.License;
import ai.scribble._class_preamble;
import ai.scribble._method_preamble;
import org.slf4j.LoggerFactory;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import java.lang.reflect.InvocationTargetException;

/**
 * @author Ravindranath Akila
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@_class_preamble(
        authors = {"Ravindranath Akila"},
        version = 1,
        description = {"To be extended by all VO Handler classes"},
        conventions = {"Extend this for all VO classes"},
        extentions = {},
        TODO = {"Under Progress"})
public abstract class VOValidator {

    @_method_preamble(
            authors = {"Ravindranath Akila"},
            version = 1,
            description = {"All value object handlers are supposed to implement this"},
            conventions = {""},
            callBackModules = {AroundInvoke.class})
    @AroundInvoke
    final Object AroundInvoke(final InvocationContext invocationContext) throws Exception {
        validator(invocationContext);
        return invocationContext.proceed();
    }

    @_method_preamble(
            authors = {"Ravindranath Akila"},
            version = 1,
            description = {"Called by AroundInvoke"},
            conventions = {"Internal to Class. Make void empty methods for any additional methods in handler classes"},
            callBackModules = {VOValidator.class})
    private final void validator(final InvocationContext invocationContext) {
        try {
            try {
                this.getClass().getMethod(invocationContext.getMethod().getName(), new Class[]{InvocationContext.class, Object[].class}).invoke(this, invocationContext, invocationContext.getParameters());
            } catch (final IllegalAccessException ex) {
                LoggerFactory.getLogger(VOValidator.class.getName()).error("ERROR IN VALIDATING BEAN FOR METHOD:" + invocationContext.getMethod().toGenericString(), ex);
            } catch (final IllegalArgumentException ex) {
                LoggerFactory.getLogger(VOValidator.class.getName()).error("ERROR IN VALIDATING BEAN FOR METHOD:" + invocationContext.getMethod().toGenericString(), ex);
            } catch (final InvocationTargetException ex) {
                LoggerFactory.getLogger(VOValidator.class.getName()).error("ERROR IN VALIDATING BEAN FOR METHOD:" + invocationContext.getMethod().toGenericString(), ex);
            }
        } catch (final NoSuchMethodException ex) {
            LoggerFactory.getLogger(VOValidator.class.getName()).error("ERROR IN VALIDATING BEAN FOR METHOD:" + invocationContext.getMethod().toGenericString(), ex);
        } catch (final SecurityException ex) {
            LoggerFactory.getLogger(VOValidator.class.getName()).error("ERROR IN VALIDATING BEAN FOR METHOD:" + invocationContext.getMethod().toGenericString(), ex);
        }
    }
}
