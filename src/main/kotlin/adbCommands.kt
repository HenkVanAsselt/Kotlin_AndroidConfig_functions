/**
 * ADB related commands like push, pull
 */

/**
 * Pull (Get) a file from the connected Android device
 * @param androidfile: File on the connected Android device to copy to this PC
 * @param pcfile: Fill path to the PC file which will become the copy of the Android file
 * @return: true in case of success, false if not
 */
fun pull (androidfile: String, pcfile: String) : Boolean {
    val ret: String = exec(mutableListOf(adb_exe, "pull", androidfile, pcfile))
    println(ret)
    when {
        "1 file pulled" in ret && "0 skipped" in ret -> return true
        "No such file or directory" in ret -> return false
        "error: cannot create" in ret -> return false
    }
    return false
}