package com.miqt.injectapk

import groovyjarjarasm.asm.ClassReader
import groovyjarjarasm.asm.ClassWriter
import groovyjarjarasm.asm.ClassVisitor
import groovyjarjarasm.asm.MethodVisitor
import groovyjarjarasm.asm.Opcodes
import groovyjarjarasm.asm.commons.AdviceAdapter
import jdk.internal.org.objectweb.asm.Type
import org.apache.commons.io.FileUtils

class Leaning {
    static void main(String[] args) {
        def clazzBytes = new File("out/production/loadApkTool/com/miqt/injectapk/TestClass.class").bytes
        byte[] code = InjectClass(clazzBytes)
        FileUtils.writeByteArrayToFile(new File("out/production/loadApkTool/com/miqt/injectapk/TestClassGG.class"), code)
    }

    private static byte[] InjectClass(byte[] clazzBytes) {
        ClassReader cr = new ClassReader(clazzBytes)
        ClassWriter cw = new ClassWriter(cr, ClassWriter.COMPUTE_MAXS)
        ClassVisitor cv = new TryCVisitor(Opcodes.ASM5, cw)
        cr.accept(cv, ClassReader.EXPAND_FRAMES)
        byte[] code = cw.toByteArray()
        code
    }

    private final static class TryCVisitor extends ClassVisitor {

        def className = null;

        TryCVisitor(int i, ClassVisitor classVisitor) {
            super(i, classVisitor)
        }

        @Override
        void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
            super.visit(version, access, name, signature, superName, interfaces)
            className = name.replace("/", ".")
        }

        @Override
        MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
            Type[] types = Type.getArgumentTypes(descriptor)
            Type returnType = Type.getReturnType(descriptor)
            boolean isStatic = (access & Opcodes.ACC_STATIC) != 0

            if (name.endsWith("<init>")) {
                isStatic = true
            }

            def mv = cv.visitMethod(access, name, descriptor, signature, exceptions)
            mv = new AdviceAdapter(Opcodes.ASM5, mv, access, name, descriptor) {
                @Override
                protected void onMethodEnter() {
                    if (isStatic) {
                        mv.visitInsn(Opcodes.ACONST_NULL);//null
                    } else {
                        mv.visitVarInsn(Opcodes.ALOAD, 0);//this
                    }
                    mv.visitLdcInsn(className);//className
                    mv.visitLdcInsn(name);//methodbName
                    mv.visitLdcInsn(returnType.toString());//returntype
                    mv.visitIntInsn(Opcodes.BIPUSH, types.length);
                    mv.visitTypeInsn(Opcodes.ANEWARRAY, "java/lang/Object");

                    for (int i = 0; i < types.length; i++) {
                        mv.visitInsn(Opcodes.DUP);
                        mv.visitInsn(Opcodes.ICONST_0);
                        mv.visitVarInsn(Opcodes.ALOAD, i + 1);
                        mv.visitInsn(Opcodes.AASTORE);
                    }

                    mv.visitMethodInsn(Opcodes.INVOKESTATIC, "com/analysys/plugin/Key", "enter", "(Ljava/lang/Object;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Object;)V", false);

                }

                @Override
                protected void onMethodExit(int opcode) {
                    if (isStatic) {
                        mv.visitInsn(Opcodes.ACONST_NULL);//null
                    } else {
                        mv.visitVarInsn(Opcodes.ALOAD, 0);//this
                    }
                    mv.visitLdcInsn(className);//className
                    mv.visitLdcInsn(name);//methodbName
                    mv.visitLdcInsn(returnType.toString());//returntype
                    mv.visitIntInsn(Opcodes.BIPUSH, types.length);
                    mv.visitTypeInsn(Opcodes.ANEWARRAY, "java/lang/Object");

                    for (int i = 0; i < types.length; i++) {
                        mv.visitInsn(Opcodes.DUP);
                        mv.visitInsn(Opcodes.ICONST_0);
                        mv.visitVarInsn(Opcodes.ALOAD, i + 1);
                        mv.visitInsn(Opcodes.AASTORE);
                    }
                    mv.visitMethodInsn(Opcodes.INVOKESTATIC, "com/analysys/plugin/Key", "exit", "(Ljava/lang/Object;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Object;)V", false);
                }

            }
        }
    }
}
