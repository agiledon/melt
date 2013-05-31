package com.melt.orm.session;

import com.melt.orm.config.parser.FieldConfig;
import com.melt.orm.config.parser.ModelConfig;
import com.melt.orm.util.GlobalConsent;

public class CommandExecutor {
    protected final Session session;

    public CommandExecutor(Session session) {
        this.session = session;
    }

    protected Integer getId(Object fieldValue) {
        ModelConfig subModelConfig = session.getModelConfig(fieldValue.getClass());
        if (subModelConfig != null) {
            for (FieldConfig subFieldConfig : subModelConfig.getFields()) {
                if ("id".equalsIgnoreCase(subFieldConfig.getFieldName())) {
                    return (Integer) subFieldConfig.getFieldValue(fieldValue);
                }
            }
        }
        return GlobalConsent.DEFAULT_ID;
    }
}
