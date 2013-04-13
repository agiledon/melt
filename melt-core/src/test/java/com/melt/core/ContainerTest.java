package com.melt.core;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.nullValue;

public class ContainerTest {
    @Test
    public void should_resolve_specific_object() {
        ContainerBuilder builder = new ContainerBuilder();
        Container container = builder.build();
        String target = container.resolve();
        assertThat(target, is(nullValue()));
    }
}
