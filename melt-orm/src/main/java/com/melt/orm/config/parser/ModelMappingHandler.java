package com.melt.orm.config.parser;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.io.Files;
import com.melt.orm.exceptions.MeltOrmException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileFilter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.FluentIterable.from;
import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;
import static java.io.File.separator;

public class ModelMappingHandler {
    private Logger logger = LoggerFactory.getLogger(ModelMappingHandler.class);

    public Map<String, ModelConfig> mappingModelConfigs(String packageName) {
        List<Class> classesUnderPackage = getClassesUnderPackage(packageName);
        Map<String, ModelConfig> modelConfigMaps = newHashMap();
        for (Class modelClass : classesUnderPackage) {
            modelConfigMaps.put(modelClass.getName(), mappingClass2Model(modelClass));
        }
        handleOneToOneMapping(modelConfigMaps);
        handleOneToManyMapping(modelConfigMaps);
        handleManyToOneMapping(modelConfigMaps);
        return modelConfigMaps;
    }

    public void handleOneToOneMapping(Map<String, ModelConfig> modelConfigs) {
        for (ModelConfig modelConfig : modelConfigs.values()) {
            for (FieldConfig fieldConfig : modelConfig.getFields()) {
                String fieldTypeName = fieldConfig.getFieldType().getName();
                if (!modelConfigs.containsKey(fieldTypeName)) {
                    continue;
                }
                ModelConfig referenceModelConfig = modelConfigs.get(fieldTypeName);
                if (referenceModelConfig.hasFieldWithType(modelConfig.getModelClass())) {
                    fieldConfig.setOneToOne(true);
                }
            }
        }
    }

    public void handleOneToManyMapping(Map<String, ModelConfig> modelConfigs) {
        for (ModelConfig modelConfig : modelConfigs.values()) {
            for (FieldConfig fieldConfig : modelConfig.getFields()) {
                if(fieldConfig.isSetType() && modelConfigs.containsKey(fieldConfig.getGenericType().getName())){
                    fieldConfig.setOneToMany(true);
                }
            }
        }
    }

    public void handleManyToOneMapping(Map<String, ModelConfig> modelConfigs) {
        for (ModelConfig modelConfig : modelConfigs.values()) {
            for (FieldConfig fieldConfig : modelConfig.getFields()) {
                String fieldTypeName = fieldConfig.getFieldType().getName();
                if (!modelConfigs.containsKey(fieldTypeName)) {
                    continue;
                }
                ModelConfig referenceModelConfig = modelConfigs.get(fieldTypeName);
                if (referenceModelConfig.hasSetFieldWithType(modelConfig.getModelClass())) {
                    fieldConfig.setManyToOne(true);
                }
            }
        }
    }

    public ModelConfig mappingClass2Model(Class clazz) {
        return new ModelConfig(getFieldConfigs(clazz), clazz);
    }

    public List<Class> getClassesUnderPackage(final String packageName) {
        final String packagePath = getPackagePath(packageName);
        List<File> classFiles = getClassFiles(packageName, packagePath);
        ImmutableList<Class> classes = from(classFiles).transform(new Function<File, Class>() {
            @Override
            public Class apply(File file) {
                String className = Files.getNameWithoutExtension(file.getName());
                Object val = null;
                try {
                    return Class.forName(packageName + "." + className);
                } catch (Exception e) {
                    logger.error(String.format("init model %s has error", className));
                    throw new MeltOrmException(e);
                }
            }
        }).toList();
        if (classes.size() == 0) {
            throw new MeltOrmException(String.format("The package %s hasn't any model", packageName));
        }
        return classes;
    }

    public List<FieldConfig> getFieldConfigs(Class clazz) {
        Field[] declaredFields = clazz.getDeclaredFields();
        return from(newArrayList(declaredFields)).transform(new Function<Field, FieldConfig>() {
            @Override
            public FieldConfig apply(java.lang.reflect.Field field) {
                Class<?> fieldType = field.getType();
                if (fieldType.isAssignableFrom(List.class) || fieldType.isAssignableFrom(Set.class)) {
                    Type genericType = field.getGenericType();
                    if (genericType != null && genericType instanceof ParameterizedType) {
                        ParameterizedType pt = (ParameterizedType) genericType;
                        Class genericClazz = (Class) pt.getActualTypeArguments()[0];
                        return new FieldConfig(field.getName(), fieldType, true, genericClazz);
                    }
                }
                if (fieldType.isArray() && !fieldType.getComponentType().isArray()) {
                    return new FieldConfig(field.getName(), fieldType, true, fieldType.getComponentType());
                }
                return new FieldConfig(field.getName(), fieldType);
            }
        }).toList();
    }

    private List<File> getClassFiles(String packageName, String packagePath) {
        File dir = new File(packagePath);
        if (!dir.isDirectory()) {
            throw new MeltOrmException(String.format("The %s is not right package", packageName));
        }
        return newArrayList(dir.listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                if (file.isDirectory())
                    return false;
                return file.getName().matches(".*\\.class$");
            }
        }));
    }

    private String getPackagePath(String packageName) {
        URL url = ModelMappingHandler.class.getClassLoader().getResource("");
        try {
            checkNotNull(url);
            return String.format("%s%s%s", java.net.URLDecoder.decode(url.getPath(), "UTF-8"), packageName.replaceAll("[.]", separator), separator);
        } catch (UnsupportedEncodingException e) {
            logger.error(String.format("Parse model under %s error", packageName));
            throw new MeltOrmException(e);
        }
    }
}
