

本项目是对apk反编译插入代码的攻击，设想是支持插入任何代码，目前已经支持方法插入代码，还需要完善。

目前已进行和待进行的工作：

``` java
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
```

原理，对apk解压然后反编译其dex，最后打开jar对其内class使用ASM字节码插桩，最后重新打回dex，打包apk并签名，完成固定代码插桩。