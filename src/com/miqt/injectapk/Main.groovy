package com.miqt.injectapk

import com.sun.deploy.security.JarSignature
import jdk.internal.org.objectweb.asm.ClassReader
import jdk.internal.org.objectweb.asm.ClassVisitor
import jdk.internal.org.objectweb.asm.ClassWriter
import org.apache.commons.codec.digest.DigestUtils
import org.apache.commons.io.FileUtils
import org.apache.commons.io.IOUtils

import java.util.concurrent.TimeUnit
import java.util.jar.JarEntry
import java.util.jar.JarFile
import java.util.jar.JarOutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipFile
import java.util.zip.ZipOutputStream

class Main {
    static void main(String[] args) {

        if (args == null || args.length == 0) {
            println("[friendly reminder]: ")
            println("\t\tjava -jar ApkInject.jar [xxx.apk] --keystore [path.keystore] [alias] [password] ")
            println("\t\t[more]: https://github.com/miqt/ASMInjectCodeToApk ")
            println("\t\t[contact me]: mailto:miqtdev@163.com")
            //return
        }

        println("[sample]: start")

        def apkPath = "./sample_apk/app-debug_raw.apk"
        def keystore = "./sample_apk/sign.keystore"
        def alias = "1111"
        def paasword = "111111"

        for (int i = 0; i < args.length; i++) {
            if (args[i].contains("--keystore")) {
                keystore = args[i + 1]
                alias = args[i + 2]
                paasword = args[i + 3]
            } else {
                apkPath = args[i]
            }
        }


        def apkFile = new File(apkPath)
        def outPath = apkFile.parent + File.separator + "output";
        def tempPath = apkFile.parent + File.separator + "temp";

        if (!new File(outPath).exists()) {
            new File(outPath).mkdirs()
        }
        if (!new File(tempPath).exists()) {
            new File(tempPath).mkdirs()
        }

        def zipFile = new ZipFile(apkFile)
        def outApkFile = new File(outPath, apkFile.name)
        def outputStream = new FileOutputStream(outApkFile)
        def zipOut = new ZipOutputStream(outputStream)
        //遍历Apk文件
        zipFile.entries().each {
            zipOut.putNextEntry(new ZipEntry(it.name))
            if (it.name.endsWith(".dex") && !it.directory) {
                println("[${it.name}] dex2jar")
                def dexFile = new File(tempPath, it.name)
                FileUtils.writeByteArrayToFile(dexFile, zipFile.getInputStream(it).bytes, false)
                //dex2jar
                def dex2jar_jarPath = dexFile.absolutePath.replace(".dex", "_dex2jar.jar")
                def dex2JarTask = Runtime.getRuntime().exec(new String[]{
                        "d2j-dex2jar.bat",
                        dexFile.absolutePath,
                        "--output",
                        dex2jar_jarPath
                })
                dex2JarTask.waitFor(30, TimeUnit.SECONDS)
                //inject jar file
                println("[${it.name}] inject code")
                def injectedJarFile = injectJar(new File(dex2jar_jarPath), new File(dex2jar_jarPath).parentFile)
                def injectedDexPath = injectedJarFile.absolutePath.replace(".jar", ".dex")
                //jar2dex
                println("[${it.name}] jar2dex code")
                def jar2dexTask = Runtime.getRuntime().exec(new String[]{
                        "dx.bat",
                        "--dex",
                        "--output",
                        injectedDexPath,
                        injectedJarFile.absolutePath
                })
                jar2dexTask.waitFor(30, TimeUnit.SECONDS)
                println("[${it.name}] write to apk")
                zipOut.write(new File(injectedDexPath).bytes)
            } else {
                println("[${it.name}] write to apk")
                zipOut.write(zipFile.getInputStream(it).bytes)
            }
            zipOut.closeEntry()
        }
        zipOut.flush()
        zipOut.finish()
        zipOut.close()
        outputStream.close()
        //签名apk
        println("[${apkFile.name}] signApk")
        signApk(outApkFile.absolutePath, outApkFile.absolutePath.replace(".apk", "_sign.apk"), keystore, paasword, alias)
        println("[SUCCESS] finish !")
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

    static boolean signApk(String sourcePath, String targetPath, String key,
                           String passwd, String alias) {
        if (sourcePath == null || targetPath == null || passwd == null || key == null)
            return false;

        File file = new File(sourcePath);
        if (!file.exists())
            return false;

        file = new File(key);
        if (!file.exists())
            return false;

        String cmd = "jarsigner -verbose -keystore " + key + " -signedjar " + targetPath + " " + sourcePath + " " + alias;
        Process process = null;
        try {
            process = Runtime.getRuntime().exec(cmd);
            OutputStream outputStream = process.getOutputStream();
            outputStream.write(passwd.getBytes());
            outputStream.close();
            InputStream inputStream = process.getInputStream();
            InputStreamReader reader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(reader);
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                if (line.contains("incorrect"))
                    return false;
            }
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


}
