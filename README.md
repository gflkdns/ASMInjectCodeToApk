

本项目是对apk反编译插入代码的攻击，设想是支持插入任何代码，目前已经支持方法插入代码，还需要完善。

原理，对apk解压然后反编译其dex，最后打开jar对其内class使用ASM字节码插桩，最后重新打回dex，打包apk并签名，完成固定代码插桩。

使用：

```java_holder_method_tree
java -jar ApkInject.jar [xxx.apk] --keystore [path.keystore] [alias] [password] 
```