package com.miqt.injectapk

import org.apache.commons.codec.digest.DigestUtils
import org.apache.commons.io.FileUtils

class Leaning {
    static void main(String[] args) {
        new File("./out/production/loadApkTool/com").eachFileRecurse {
            if (it.isFile() && it.name.contains("Test") && !it.name.contains("Inj_")) {
                def clazzBytes = it.bytes
                byte[] code = InjectUtils.injectClass(clazzBytes)
                FileUtils.writeByteArrayToFile(new File(it.getParent(), "Inj_" + it.name), code)
            }
        }
        new File("./sample_files/").eachFileRecurse {
            if (it.isFile() && it.name.endsWith(".jar")) {
                InjectUtils.injectJar(it, new File("./sample_files/", DigestUtils.md5Hex(it.name) + ".jar"))
            }
        }

    }
}
