package ru.venidiktov.extension;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestInstancePostProcessor;

import java.util.Arrays;

public class PostProcessingExtension implements TestInstancePostProcessor {
    @Override
    public void postProcessTestInstance(Object testInstance, ExtensionContext extensionContext) throws Exception {
        System.out.println("Post processing extension!");
        Arrays.stream(testInstance.getClass().getDeclaredFields()).forEach(it -> {
            if (it.isAnnotationPresent(SuppressWarnings.class)) {
                System.out.println("Поле помечено аннотацией SuppressWarnings");
            }
        });
    }
}
