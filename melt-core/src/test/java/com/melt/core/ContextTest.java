package com.melt.core;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.nullValue;

public class ContextTest {
    @Test
    public void should_resolve_specific_object() {
        ContextBuilder builder = new ContextBuilder();
        Context context = builder.build();
        String target = context.resolve();
        assertThat(target, is(nullValue()));
    }
}
