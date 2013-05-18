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
import java.net.URL;
import java.util.List;

import static com.google.common.collect.FluentIterable.from;
import static com.google.common.collect.Lists.newArrayList;
import static java.io.File.separator;

public class ClassHelper {
    private Logger logger = LoggerFactory.getLogger(ClassHelper.class);

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
                return new FieldConfig(field.getName(), field.getType());
            }
        }).toList();
    }

    private List<File> getClassFiles(String packageName, String packagePath) {
        File dir = new File(packagePath);
        if (dir == null || !dir.isDirectory()) {
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
        URL url = ClassHelper.class.getClassLoader().getResource("");
        try {
            return String.format("%s%s%s", java.net.URLDecoder.decode(url.getPath(), "UTF-8"), packageName.replaceAll("[.]", separator), separator);
        } catch (UnsupportedEncodingException e) {
            logger.error(String.format("Parse model under %s error", packageName));
            throw new MeltOrmException(e);
        }
    }
}
