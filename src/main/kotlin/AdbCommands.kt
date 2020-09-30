@file:Suppress("SpellCheckingInspection")

/**
 * ADB related commands like push, pull
 */

/**
 * Pull (Get) a file from the connected Android device
 * @param androidfile File on the connected Android device to copy to this PC
 * @param pcfile Fill path to the PC file which will become the copy of the Android file
 * @return true in case of success, false if not
 */
fun pull (androidfile: String, pcfile: String) : Boolean {
    val ret: String = exec(mutableListOf(adbPath, "pull", androidfile, pcfile))
//    println(ret)
    when {
        "1 file pulled" in ret && "0 skipped" in ret -> return true
        "No such file or directory" in ret -> return false
        "error: cannot create" in ret -> return false
        else -> println("NOTE to developer: this return text is not processed yet: $ret")
    }
    return false
}

/**
 * Pull (Get) a file from the connected Android device
 * @param androidfile File on the connected Android device to copy to this PC
 * @param pcfile Fill path to the PC file which will become the copy of the Android file
 * @return true in case of success, false if not
 */
fun push (pcfile: String, androidfile: String) : Boolean {
    val ret: String = exec(mutableListOf(adbPath, "push", pcfile, androidfile))
    println(ret)
    when {
        "1 file pushed" in ret && "0 skipped" in ret -> return true
        "No such file or directory" in ret -> return false
        "failed to copy" in ret -> return false
        "remote secure_mkdirs failed" in ret -> return false
        "Read-only file system" in ret -> return false
        "@todo" in ret -> return false
        else -> println("NOTE to developer: this return text is not processed yet: $ret")
    }
    return false
}

// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
// Test functions
// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

private fun main() {
    push("c:/temp/test.txt", "/sdcard/test1.txt")
    val ret = exec(mutableListOf(adbPath, "shell", "ls", "-l", "/sdcard/"))
    println(ret)
}
