package com.vokal.codegen.processor;

import java.io.IOException;
import java.util.*;
import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;

import com.vokal.codegen.tools.*;

@SupportedAnnotationTypes("com.vokal.codegen.Column")
@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class ModelProcessor extends AbstractProcessor {

    public static final String SUFFIX = "Helper";

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        System.out.println("******************* PRE COMPILER ********************");

        for (TypeElement annotation : annotations) {
            write(classesWithFieldsAnnotatedWith(roundEnv.getElementsAnnotatedWith(annotation)));
        }

        return true;
    }

    private void write(Map<EnclosingClass, Collection<AnnotatedField>> fieldsByEnclosingClass) {
        WriterFactory writerFactory = new WriterFactory(filer(), SUFFIX);
        for (EnclosingClass enclosingClass : fieldsByEnclosingClass.keySet()) {
            try {
                writerFactory.writeClass(enclosingClass)
                        .withFields(fieldsByEnclosingClass.get(enclosingClass));
            } catch (IOException e) {
                messager().printMessage(Diagnostic.Kind.ERROR,
                                        "Error generating helper for class " + enclosingClass.getClassName()
                                                + ". Reason: " + e.getMessage());
            }
        }
    }


    private Map<EnclosingClass, Collection<AnnotatedField>> classesWithFieldsAnnotatedWith(
            Set<? extends Element> annotatedElements) {
        return new AnnotationsConverter(messager(), elementUtils())
                .convert(annotatedElements);
    }


    private Messager messager() {
        return processingEnv.getMessager();
    }

    private Elements elementUtils() {
        return processingEnv.getElementUtils();
    }

    private Filer filer() {
        return processingEnv.getFiler();
    }
}
