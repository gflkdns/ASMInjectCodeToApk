import jdk.internal.org.objectweb.asm.AnnotationVisitor
import jdk.internal.org.objectweb.asm.ClassReader
import jdk.internal.org.objectweb.asm.ClassVisitor
import jdk.internal.org.objectweb.asm.ClassWriter
import jdk.internal.org.objectweb.asm.FieldVisitor
import jdk.internal.org.objectweb.asm.MethodVisitor
import jdk.internal.org.objectweb.asm.Opcodes
import org.apache.commons.codec.digest.DigestUtils
import org.apache.commons.io.IOUtils

import java.util.jar.JarEntry
import java.util.jar.JarFile
import java.util.jar.JarOutputStream
import java.util.zip.ZipEntry

class Main {
    static void main(String[] args) {
        println("start")
        //todo 1. 解压APK
        println("1. 解压APK")
        //todo 2. 反编译DEX
        println("2. 反编译DEX")
        //todo 3. 对jar插桩
        println("3. 对jar插桩")
        injectJar(new File("C:\\Users\\miqt\\Desktop\\tmpdir\\classes-dex2jar.jar"),
                new File("C:\\Users\\miqt\\Desktop\\tmpdir\\"))
        //todo 4. jar2dex
        println("4. jar2dex")
        //todo 5. 打包APK
        println("5. 打包APK")
        //todo 5. 重新签名
        println("6. 重新签名")
        println(" end ")
    }

    static byte[] injectClass(byte[] clazzBytes) {
        ClassReader cr = new ClassReader(clazzBytes)
        ClassWriter cw = new ClassWriter(cr, ClassWriter.COMPUTE_MAXS)
        ClassVisitor cv = new MethodTimerVisitor(cw)
        cr.accept(cv, ClassReader.EXPAND_FRAMES)
        byte[] code = cw.toByteArray()
        return code
    }

    static File injectJar(File jarFile, File tempDir) {
        def file = new JarFile(jarFile)
        def hexName = DigestUtils.md5Hex(jarFile.absolutePath).substring(0, 8)
        def outputJar = new File(tempDir, hexName + jarFile.name)
        JarOutputStream jarOutputStream = new JarOutputStream(new FileOutputStream(outputJar))
        Enumeration enumeration = file.entries()
        while (enumeration.hasMoreElements()) {
            JarEntry jarEntry = (JarEntry) enumeration.nextElement()
            InputStream inputStream = file.getInputStream(jarEntry)
            String entryName = jarEntry.getName()
            ZipEntry zipEntry = new ZipEntry(entryName)
            jarOutputStream.putNextEntry(zipEntry)
            byte[] modifiedClassBytes = null
            byte[] sourceClassBytes = IOUtils.toByteArray(inputStream)
            if (entryName.endsWith(".class") && !entryName.contains("Time") /*&& !entryName.contains("\$")*/) {
                modifiedClassBytes = injectClass(sourceClassBytes)
            }
            if (modifiedClassBytes == null) {
                jarOutputStream.write(sourceClassBytes)
            } else {
                jarOutputStream.write(modifiedClassBytes)
            }
            jarOutputStream.closeEntry()
        }
        jarOutputStream.close()
        file.close()
        return outputJar
    }

}
