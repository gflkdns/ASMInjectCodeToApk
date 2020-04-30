package com.miqt.injectapk

import groovyjarjarasm.asm.ClassReader
import groovyjarjarasm.asm.ClassVisitor
import groovyjarjarasm.asm.ClassWriter
import groovyjarjarasm.asm.Opcodes
import org.apache.commons.codec.digest.DigestUtils
import org.apache.commons.io.IOUtils

import java.util.jar.JarEntry
import java.util.jar.JarFile
import java.util.jar.JarOutputStream
import java.util.zip.ZipEntry

class InjectUtils {
    static File injectJar(File jarFile, File outFile) {
        def file = new JarFile(jarFile)
        def hexName = DigestUtils.md5Hex(jarFile.absolutePath).substring(0, 8)
        def outputJar = new File(outFile, hexName + jarFile.name)
        def jarOutputStream = new JarOutputStream(new FileOutputStream(outputJar))
        def enumeration = file.entries()
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

     static byte[] injectClass(byte[] clazzBytes) {
        def cr = new ClassReader(clazzBytes)
        def cw = new ClassWriter(cr, ClassWriter.COMPUTE_MAXS)
        def cv = new TryCVisitor(Opcodes.ASM7, cw)
        cr.accept(cv as ClassVisitor, ClassReader.EXPAND_FRAMES)
        byte[] code = cw.toByteArray()
        code
    }
}
