@file:Suppress("SpellCheckingInspection")

import java.io.File

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

// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
// Test functions
// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

@Suppress("FunctionName")
private fun main() {
    val pcfile = "c:\\temp\\sysinfo.txt"
    val success = getSysinfo(pcfile)
    println("getSysinfo() returned $success")
    val sysinfomap = sysinfoToMap(pcfile)
    println("sysinfo map = $sysinfomap")
}
