package com.miqt.injectapk

import groovyjarjarasm.asm.Opcodes
import groovyjarjarasm.asm.ClassReader
import groovyjarjarasm.asm.ClassVisitor
import groovyjarjarasm.asm.ClassWriter
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
            println("\t[cmd]: java -jar ApkInject.jar [xxx.apk] --keystore [path.keystore] [alias] [password] ")
            println("\t[more]: https://github.com/miqt/ASMInjectCodeToApk ")
            println("\t[contact me]: mailto:miqtdev@163.com")
            //return
        }

        println("[sample]: start")

        def apkPath = "./sample_files/app-release.apk"
        def keystore = "./sample_files/sign.keystore"
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

        FileUtils.deleteDirectory(new File(outPath))
        FileUtils.deleteDirectory(new File(tempPath))
        FileUtils.forceMkdir(outPath as File)
        FileUtils.forceMkdir(tempPath as File)

        def zipFile = new ZipFile(apkFile)
        def outApkFile = new File(outPath, apkFile.name)
        def outputStream = new FileOutputStream(outApkFile)
        def zipOut = new ZipOutputStream(outputStream)

        def dexIndex =1;
        //遍历Apk文件
        zipFile.entries().each {
            zipOut.putNextEntry(new ZipEntry(it.name))
            if (it.name.endsWith(".dex") && !it.directory) {
                dexIndex ++;
                println("[${new Date().format("yyyy-MM-dd HH:mm:ss") + " " + it.name}] dex2jar")
                def dexFile = new File(tempPath, it.name)
                FileUtils.writeByteArrayToFile(dexFile, zipFile.getInputStream(it).bytes, false)
                //dex2jar
                def dex2jar_jarPath = dexFile.absolutePath.replace(".dex", "_dex2jar.jar")
                def dex2JarTask = Runtime.getRuntime().exec(new String[]{
                        "./tools/dex2jar-2.0/d2j-dex2jar.bat",
                        dexFile.absolutePath,
                        "--output",
                        dex2jar_jarPath
                })
                println("[${new Date().format("yyyy-MM-dd HH:mm:ss") + " " + it.name}] result : ${dex2JarTask.inputStream.text}")
                dex2JarTask.waitFor()
                //inject jar file
                println("[${new Date().format("yyyy-MM-dd HH:mm:ss") + " " + it.name}] inject code")
                def injectedJarFile = InjectUtils.injectJar(new File(dex2jar_jarPath), new File(dex2jar_jarPath).parentFile)
                def injectedDexPath = injectedJarFile.absolutePath.replace(".jar", ".dex")
                //jar2dex
                println("[${new Date().format("yyyy-MM-dd HH:mm:ss") + " " + it.name}] jar2dex code")
                def jar2dexTask = Runtime.getRuntime().exec(new String[]{
                        "dx.bat","--dex","--output",
                        injectedDexPath,
                        injectedJarFile.absolutePath
                })
                println("[${new Date().format("yyyy-MM-dd HH:mm:ss") + " " + it.name}] result : ${jar2dexTask.errorStream.text}")
                jar2dexTask.waitFor()
                println("[${new Date().format("yyyy-MM-dd HH:mm:ss") + " " + it.name}] write to apk")
                zipOut.write(new File(injectedDexPath).bytes)
            } else {
                println("[${new Date().format("yyyy-MM-dd HH:mm:ss") + " " + it.name}] write to apk")
                zipOut.write(zipFile.getInputStream(it).bytes)
            }
            zipOut.closeEntry()
        }
        // copy libs
        zipOut.putNextEntry(new ZipEntry("classes${dexIndex}.dex"))
        zipOut.write(new File("./sample_files/libs/lib.dex").bytes)
        zipOut.closeEntry()
        // copy libs end

        zipOut.flush()
        zipOut.finish()
        zipOut.close()
        outputStream.close()
        //签名apk
        println("[${apkFile.name}] signApk")
        signApk(outApkFile.absolutePath, outApkFile.absolutePath.replace(".apk", "_sign.apk"), keystore, paasword, alias)
        println("[SUCCESS] finish !")
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
