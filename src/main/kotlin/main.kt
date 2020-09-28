@file:Suppress("UNUSED_PARAMETER", "SpellCheckingInspection")

import java.io.File
import java.util.*

fun main(args: Array<String>) {

    if (!checkADB()) {
        println("Error: Cannot continue due to error in ADB")
        return
    }

//    val path = "/sdcard/honeywell/"
//    val ret = exec(mutableListOf(adb_exe, "shell", "ls", "-l", path))
//    println(ret)

    val androidfile = "/sdcard/honeywell/sysinfo/sysinfo.txt"
    val pcfile = "c:\\temp\\sysinfo.txt"
    val ret = pull(androidfile, pcfile)
    println(ret)
    val x = sysinfoToMap(pcfile)
    println(x)
}


/**
 * Read the given sysinfo file contents, and return a map of the contents
 * @param fileName: Name of the sysinfo.txt file to process
 * @return: map of the contents
 */
fun sysinfoToMap(fileName: String): Map<String, String> {

    var data = emptyMap<String, String>().toMutableMap()

    File(fileName).readLines().forEach {
        if (':' in it) {
            println(it)
            val parts = it.split(':', limit=2)
            println(parts)
            data[parts[0]] = parts[1]
        }
        println(data)
    }
    return data
}




