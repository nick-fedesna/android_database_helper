package com.vokal.codegen.tools;

import java.io.IOException;
import java.io.Writer;
import java.util.Collection;
import javax.tools.JavaFileObject;

import com.google.common.base.Joiner;

public abstract class AbsWriter {

    protected static final String BASE_KEY = "BASE_KEY";

    private final JavaFileObject mJavaFileObject;
    private final String         mSuffix;
    private final EnclosingClass mEnclosingClass;

    public AbsWriter(JavaFileObject jfo, String suffix, EnclosingClass enclosingClass) {
        this.mJavaFileObject = jfo;
        this.mSuffix = suffix;
        this.mEnclosingClass = enclosingClass;
    }

    public void withFields(Collection<AnnotatedField> annotatedFields) throws IOException {
        Writer writer = mJavaFileObject.openWriter();
        writer.write(brewJava(annotatedFields));
        writer.flush();
        writer.close();
    }

    private String brewJava(Collection<AnnotatedField> annotatedFields) {
        String classPackage = mEnclosingClass.getClassPackage();
        String helperClassName = mEnclosingClass.getClassName() + "Helper" ;//mSuffix;
        String fqHelperClassName = classPackage + "." + helperClassName;

        String type = getWriterType();

        return Joiner.on("\n").join(
                "// Generated code from CodeGen. Do not modify!",
                "package " + classPackage + ";",
                "import android.content.ContentValues;",
                "import com.vokal.db.SQLiteTable;",
                "import com.vokal.db.util.CursorCreator;",
                "import com.vokal.db.util.CursorGetter;",
                "import com.vokal.db.codegen.ModelHelper;",
                "import android.provider.BaseColumns;",
                "public class " + helperClassName + " implements ModelHelper, BaseColumns {",
                "\t" + mEnclosingClass.getClassName() + " m" + mEnclosingClass.getClassName() + ";",
                "",
                emitStaticStrings(annotatedFields),
                emitPopulateContentValue(annotatedFields),
                "",
                emitTableCreator(annotatedFields),
                "",
                emitCursorCreator(annotatedFields),
                "",
                emitSetObject(annotatedFields),
                "",
                "}"
        );
    }

    private String emitCursorCreator(Collection<AnnotatedField> annotatedFields) {
        StringBuilder builder = new StringBuilder();
        builder.append("\tpublic static final CursorCreator<" + mEnclosingClass.getClassName() + "> CURSOR_CREATOR = new CursorCreator<" + mEnclosingClass.getClassName() + ">() {\n" +
                       "\t\tpublic " + mEnclosingClass.getClassName() + " createFromCursorGetter(CursorGetter getter) {\n" +
                       "\t\t\t" + mEnclosingClass.getClassName() + " model = new " + mEnclosingClass.getClassName() + "();\n");
        for (AnnotatedField annotatedField : annotatedFields) {
            builder.append("\t\t\tmodel." + annotatedField.getName() + " = getter.get" + firstLetterToUpper(annotatedField.getSimpleType()) + "(" + annotatedField.getName().toUpperCase() + ");\n");
        }
        builder.append("\t\t\tmodel.setId(getter.getLong(_ID));\n");
        builder.append("\t\t\treturn model;\n\t\t}\n\t};");

        return builder.toString();
    }

    protected String emitTableCreator(Collection<AnnotatedField> annotatedFields) {
        StringBuilder builder = new StringBuilder();
        builder.append("\tpublic static final SQLiteTable.TableCreator TABLE_CREATOR = new SQLiteTable.TableCreator() {\n" +
                       "\t\t@Override\n" +
                       "\t\tpublic SQLiteTable buildTableSchema(SQLiteTable.Builder aBuilder) {\n\t\t\taBuilder\n");
        for (AnnotatedField annotatedField : annotatedFields) {
            if (annotatedField.getSimpleType().equals("boolean") || annotatedField.getSimpleType().equals("int") || annotatedField.getSimpleType().equals("long")) {
                builder.append("\t\t\t.add" + "IntegerColumn(" + annotatedField.getName().toUpperCase() + ")\n");
            } else if (annotatedField.getSimpleType().equals("double") || annotatedField.getSimpleType().equals("float")) {
                builder.append("\t\t\t.add" + "RealColumn(" + annotatedField.getName().toUpperCase() + ")\n");
            } else {
                builder.append("\t\t\t.add" + firstLetterToUpper(
                        annotatedField.getSimpleType()) + "Column(" + annotatedField.getName().toUpperCase() + ")\n");
            }
        }
        builder.append("\t\t\t;\n\t\t\treturn aBuilder.build();\n\t\t}\n\n");
        builder.append("\t\t@Override\n" +
                       "\t\tpublic SQLiteTable updateTableSchema(SQLiteTable.Updater aUpdater, int aOldVersion) {\n" +
                       "\t\t\treturn aUpdater.recreate();\n" +
                       "\t\t}\n\t};");

        return builder.toString();
    }

    private String  emitStaticStrings(Collection<AnnotatedField> annotatedFields) {
        StringBuilder builder = new StringBuilder();
        for (AnnotatedField annotatedField : annotatedFields) {
            builder.append("\tprivate static final String " + annotatedField.getName().toUpperCase() + " = \"" + annotatedField.getName().toLowerCase() + "\";\n");
        }
        return builder.toString();
    }

    private String emitSetObject(Collection<AnnotatedField> annotatedFields) {
        StringBuilder builder = new StringBuilder();
        builder.append("\t@Override public void setObject(Object a) {\n" +
                       "\t\tm" + mEnclosingClass.getClassName() + " = ((" + mEnclosingClass.getClassName() + ") a);\n");
        builder.append("\t}");
        return builder.toString();
    }

    private String emitPopulateContentValue(Collection<AnnotatedField> annotatedFields) {
        StringBuilder builder = new StringBuilder();
        builder.append("\t@Override\n\tpublic void populateContentValues(ContentValues aValues) {\n\t\tif (m"  + mEnclosingClass.getClassName() + ".hasId()) aValues.put(_ID, m"+ mEnclosingClass.getClassName() +".getId());\n");
        for (AnnotatedField annotatedField : annotatedFields) {
            builder.append(emitSetters(annotatedField));
        }
        builder.append("\t}");
        return builder.toString();
    }

    private String emitSetters(AnnotatedField annotatedField) {
        return "\t\taValues.put("+annotatedField.getName().toUpperCase()+ "," + " m" + mEnclosingClass.getClassName() + "." + annotatedField.getName() + ");\n";
    }

    protected String firstLetterToUpper(String word) {
        return word.substring(0, 1).toUpperCase() + word.substring(1);
    }

    protected abstract String getWriterType();


}
