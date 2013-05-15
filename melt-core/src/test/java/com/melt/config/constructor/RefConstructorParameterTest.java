package com.melt.config.constructor;

import com.melt.core.Container;
import com.melt.core.InitializedBeans;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.contains;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class RefConstructorParameterTest {

    private InitializedBeans beans;
    private Container container;
    private RefConstructorParameter parameter;
    private String refName;

    @Before
    public void setUp() throws Exception {
        refName = "ref";
        beans = mock(InitializedBeans.class);
        container = mock(Container.class);
        parameter = new RefConstructorParameter(0, refName);
    }

    @Test
    public void shouldUpdateValueIfContainerHasParameterBean() {
        Object targetObject = new Object();
        when(beans.getBean(refName)).thenReturn(targetObject);

        parameter.updateValue(container, beans);
        verify(beans).getBean(refName);
        assertThat(parameter.getValue(), is(targetObject));
    }

    @Test
    public void shouldUpdateValueFromParentContainerIfInitializedBeanIsNullAndParentContainerIsNotNull() {
        Object targetObject = new Object();
        when(beans.getBean(refName)).thenReturn(null);
        when(container.resolve(refName)).thenReturn(targetObject);

        parameter.updateValue(container, beans);
        verify(beans).getBean(refName);
        verify(container).resolve(refName);
        assertThat(parameter.getValue(), is(targetObject));
    }
}
