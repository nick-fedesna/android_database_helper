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
                mEnclosingClass.getClassName() + " m" + mEnclosingClass.getClassName() + ";",
                "",
                "protected transient Long _id = -1L;",
                "",
                emitStaticStrings(annotatedFields),
                "",
                emitPopulateContentValue(annotatedFields),
                "",
                emitTableCreator(annotatedFields),
                "",
                emitCursorCreator(annotatedFields),
                "",
                emitSetObject(annotatedFields),
                "",
                "protected boolean hasId() {\nreturn _id != null && _id != -1;\n}",
                "}"
        );
    }

    private String emitCursorCreator(Collection<AnnotatedField> annotatedFields) {
        StringBuilder builder = new StringBuilder();
        builder.append("    public static final CursorCreator<" + mEnclosingClass.getClassName() + "> CURSOR_CREATOR = new CursorCreator<" + mEnclosingClass.getClassName() + ">() {\n" +
                               "        public " + mEnclosingClass.getClassName() + " createFromCursorGetter(CursorGetter getter) {\n" +
                               "       " + mEnclosingClass.getClassName() + " model = new " + mEnclosingClass.getClassName() + "();\n");
        for (AnnotatedField annotatedField : annotatedFields) {
            builder.append("model." + annotatedField.getName() + " = getter.get" + firstLetterToUpper(annotatedField.getSimpleType()) + "(" + annotatedField.getName().toUpperCase() + ");\n");
        }
        builder.append("model.setId(getter.getLong(_ID));\n");
        builder.append("return model;\n}\n};\n");

        return builder.toString();
    }

    protected String emitTableCreator(Collection<AnnotatedField> annotatedFields) {
        StringBuilder builder = new StringBuilder();
        builder.append("    public static final SQLiteTable.TableCreator TABLE_CREATOR = new SQLiteTable.TableCreator() {\n" +
                               "\n" +
                               "        @Override\n" +
                               "        public SQLiteTable buildTableSchema(SQLiteTable.Builder aBuilder) {\naBuilder");
        for (AnnotatedField annotatedField : annotatedFields) {
            if (annotatedField.getSimpleType().equals("boolean") || annotatedField.getSimpleType().equals("int") || annotatedField.getSimpleType().equals("long")) {
                builder.append(".add" + "IntegerColumn(" + annotatedField.getName().toUpperCase() + ")\n");
            } else if (annotatedField.getSimpleType().equals("double") || annotatedField.getSimpleType().equals("float")) {
                builder.append(".add" + "RealColumn(" + annotatedField.getName().toUpperCase() + ")\n");
            } else {
                builder.append(".add" + firstLetterToUpper(
                        annotatedField.getSimpleType()) + "Column(" + annotatedField.getName().toUpperCase() + ")\n");
            }
        }
        builder.append(";\nreturn aBuilder.build();\n}\n");
        builder.append("        @Override\n" +
                               "        public SQLiteTable updateTableSchema(SQLiteTable.Updater aUpdater, int aOldVersion) {\n" +
                               "            return aUpdater.recreate();\n" +
                               "        }\n};\n");

        return builder.toString();
    }

    private String  emitStaticStrings(Collection<AnnotatedField> annotatedFields) {
        StringBuilder builder = new StringBuilder();
        for (AnnotatedField annotatedField : annotatedFields) {
        builder.append("private static final String " + annotatedField.getName().toUpperCase() + " = \"" + annotatedField.getName().toLowerCase() + "\";\n");
        }
        return builder.toString();
    }

    private String emitSetObject(Collection<AnnotatedField> annotatedFields) {
        StringBuilder builder = new StringBuilder();
        builder.append("@Override public void setObject(Object a) {\n" +
                       "m" + mEnclosingClass.getClassName() + " = ((" + mEnclosingClass.getClassName() + ") a);\n");
        builder.append("}");
        return builder.toString();
    }

    private String emitPopulateContentValue(Collection<AnnotatedField> annotatedFields) {
        StringBuilder builder = new StringBuilder();
        builder.append("@Override\npublic void populateContentValues(ContentValues aValues) {\nif (hasId()) aValues.put(_ID, _id);\n");
        for (AnnotatedField annotatedField : annotatedFields) {
            builder.append(emitSetters(annotatedField));
        }
        builder.append("}");
        return builder.toString();
    }

    private String emitSetters(AnnotatedField annotatedField) {
        return "aValues.put("+annotatedField.getName().toUpperCase()+ "," + " m" + mEnclosingClass.getClassName() + "." + annotatedField.getName() + ");\n";
    }

    protected String firstLetterToUpper(String word) {
        return word.substring(0, 1).toUpperCase() + word.substring(1);
    }

    protected abstract String getWriterType();


}
