package com.miqt.injectapk


class Leaning {
    static void main(String[] args) {
        new File("./sample_files/").eachFileRecurse {
            if (it.isFile() && it.name.endsWith(".jar")) {
                InjectUtils.injectJar(it, new File("./sample_files/"))
            }
        }

    }
}
