@file:Suppress("UNUSED_PARAMETER", "SpellCheckingInspection")

import java.io.File

fun main(args: Array<String>) {

    if (!checkADB()) {
        println("Error: Cannot continue due to error in ADB")
        return
    }

//    val path = "/sdcard/"
//    val ret = exec(mutableListOf(adbPath, "shell", "ls", "-l", path))
//    println(ret)

//    val pcfile = "c:\\temp\\sysinfo.txt"
//    var success = getSysinfo(pcfile)
//    println("getSysinfo() returned $success")
//    val sysinfomap = sysinfoToMap(pcfile)
//    println("sysinfo map = $sysinfomap")

//    val properties = propertiesToMap()
//    println("properties map = $properties")

    val path = downloadPlatformTools("c:/temp")
    println("downloadPlatformTools returned $path")
    if (path.isNotEmpty()) {
        val zipfile = File(path)
        val targetfolder = unzipPlatformTools(zipfile)
        println("unzipPlatformTools returned $targetfolder")
    }
}

/**
 * Get sysinfo.txt from the device
 * This is for Honeywell devices only
 */
fun getSysinfo(pcfile: String = "c:\\temp\\sysinfo.txt"): Boolean {
    val androidfile = "/sdcard/honeywell/sysinfo/sysinfo.txt"
    return pull(androidfile, pcfile)
}

/**
 * Read the given sysinfo file contents, and return a map of the contents
 * @param fileName: Name of the sysinfo.txt file to process
 * @return: map of the contents
 */
fun sysinfoToMap(fileName: String): Map<String, String> {

    val data = emptyMap<String, String>().toMutableMap()

    File(fileName).readLines().forEach {
        if (':' in it) {
            val parts = it.split(':', limit=2)
            if (parts.size == 2) {
                data[(parts[0].trim())] = parts[1].trim()
            }
        }
    }
    return data
}

/**
 * Read the device properties, and return a map of these properties
 * @return: map of the properties
 */

fun propertiesToMap(): Map<String, String> {

    val data = emptyMap<String, String>().toMutableMap()

    val propString = exec(mutableListOf(adbPath, "shell", "getprop"))

    propString.trim().lines().forEach {
        val parts = it.split("]: [")
        if (parts.size == 2)
            data[parts[0].trimStart('[')] = parts[1].trimEnd(']')
    }

    return data
}



