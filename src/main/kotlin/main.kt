@file:Suppress("UNUSED_PARAMETER", "SpellCheckingInspection")

fun main(args: Array<String>) {

    if (!checkADB()) {
        println("Error: Cannot continue due to error in ADB")
        return
    }

    push("c:/temp/test.txt", "/sdcard/test1.txt")
    val ret = exec(mutableListOf(adbPath, "shell", "ls", "-l", "/sdcard/"))
    println(ret)
}









