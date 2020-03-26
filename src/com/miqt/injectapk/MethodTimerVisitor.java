package com.miqt.injectapk;

import jdk.internal.org.objectweb.asm.*;
import jdk.internal.org.objectweb.asm.commons.AdviceAdapter;

import java.io.File;
import java.util.regex.Pattern;


public class MethodTimerVisitor extends ClassVisitor {

    String classname;

    public MethodTimerVisitor(ClassVisitor classVisitor) {
        super(Opcodes.ASM5, classVisitor);
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        super.visit(version, access, name, signature, superName, interfaces);
        classname = name.replace("/", ".");
    }

    @Override
    public MethodVisitor visitMethod(int access, final String name, final String desc, String signature,
                                     String[] exceptions) {
        MethodVisitor mv = cv.visitMethod(access, name, desc, signature, exceptions);
        mv = new AdviceAdapter(Opcodes.ASM5, mv, access, name, desc) {

            private boolean inject = false;

            @Override
            public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
                if ("Lcom/miqt/pluginlib/annotation/PrintTime;".equals(desc)) {
                    inject = true;
                }
                return super.visitAnnotation(desc, visible);
            }

            @Override
            protected void onMethodEnter() {
                if (isInject()) {
                    String mn = getName(name);
                    mv.visitLdcInsn(mn);
                    mv.visitMethodInsn(Opcodes.INVOKESTATIC, "com/miqt/pluginlib/tools/TimePrint", "start", "(Ljava/lang/String;)V", false);
                }
            }

            private String getName(String name) {
                Type[] types = Type.getArgumentTypes(desc);
                Type returnType = Type.getReturnType(desc);
                String type = "";
                for (int i = 0; i < types.length; i++) {
                    type = type.concat(types[i].getClassName());
                    if (i != types.length - 1) {
                        type = type.concat(",");
                    }
                }
                name = classname.concat(".").concat(name).concat("(").concat(type).concat(") type:").concat(returnType.getClassName());
                return name;
            }

            private boolean isInject() {

                return true;
            }

            @Override
            protected void onMethodExit(int opcode) {
                if (isInject()) {
                    String mn = getName(name);
                    mv.visitLdcInsn(mn);
                    mv.visitMethodInsn(Opcodes.INVOKESTATIC, "com/miqt/pluginlib/tools/TimePrint", "end", "(Ljava/lang/String;)V", false);
                }
            }
        };
        return mv;
    }
}
