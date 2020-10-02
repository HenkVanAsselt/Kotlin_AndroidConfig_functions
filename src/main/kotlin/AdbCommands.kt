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
        else -> println("NOTE to developer: this return text is not processed yet: $ret")
    }
    return false
}

/**
 * Test if a given file path is a file on the connected Android device
 * @param androidfile File to test on
 * @return true if this is a file, false if not
 */
fun isFile(androidfile: String): Boolean {
    val ret = exec(mutableListOf(adbPath, "shell", "ls", "-l", androidfile))
//    println(ret)
    /**
     *  Sample output:
     *  -rw-rw---- 1 root sdcard_rw 2666 2020-09-27 10:50 /sdcard/test1.txt
     */

    when {
        androidfile in ret && ret[0] == '-' -> return true
        "No such file or directory" in ret -> return false
        androidfile !in ret -> return false
        else -> println("NOTE to developer: this return text is not processed yet: $ret")
    }
    return false
}

/**
 * Test if a given file path is a folder on the connected Android device
 * @param androidfolder Possible folder name to test on
 * @return true if this is a folder, false if not
 */
fun isFolder(androidfolder: String): Boolean {
    val ret = exec(mutableListOf(adbPath, "shell", "ls", "-l", androidfolder))
//    println(ret)
    when {
        "No such file or directory" in ret -> return false
        androidfolder in ret && ret[0] == 'l' -> return true   // This is a link to another folder
        // Example: lrw-r--r-- 1 root root 21 2009-01-01 01:00 /sdcard -> /storage/self/primary
        ret.startsWith("total ") -> return true     // There are entries found in here, so it is a folder
        else -> println("NOTE to developer: this return text is not processed yet: $ret")
    }
    return false
}

/**
 * Make a directory/folder on the connected Android device
 * @param androidfolder The full path of the directory to create
 * @return true on success, false on failure
 */
fun mkdir(androidfolder: String): Boolean {
    val ret = exec(mutableListOf(adbPath, "shell", "mkdir", androidfolder))
    println(ret)
    when {
        ret == "" -> return true      // All is ok
        "File exists" in ret -> return false
        else -> println("NOTE to developer: this return text is not processed yet: $ret")
    }
    return false
}

/**
 * Delete a file or folder on the connected Android device
 * @param androidfolder The full path of the directory to remove
 * @return true on success, false on failure*
 */
fun rmdir(androidfolder: String): Boolean{
    val ret = exec(mutableListOf(adbPath, "shell", "rm", "-r", androidfolder))
    println(ret)
    when {
        ret == "" -> return true      // All is ok
        "No such file or directory" in ret -> return false
        else -> println("NOTE to developer: this return text is not processed yet: $ret")
    }
    return false

}

/**
 * Delete a file or folder on the connected Android device
 * @param androidfile The full path of the file to remove
 * @return true on success, false on failure*
 */
fun rm(androidfile: String): Boolean{
    val ret = exec(mutableListOf(adbPath, "shell", "rm", androidfile))
    println(ret)
    when {
        ret == "" -> return true      // All is ok
        else -> println("NOTE to developer: this return text is not processed yet: $ret")
    }
    return false


}

// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
// Test functions
// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

private fun main() {

    if (!checkADB()) {
        println("Error: Cannot continue due to error in ADB")
        return
    }

    val foldername = "/sdcard/testdir"
    var ret = rmdir(foldername)
    println("rmdir($foldername) returned $ret")
    ret = mkdir(foldername)
    println("mkdir($foldername) returned $ret")

    var dirs = exec(mutableListOf(adbPath, "shell", "ls", "-l", "/sdcard/"))
    println(dirs)

    ret = rmdir(foldername)
    println("rmdir($foldername) returned $ret")
    dirs = exec(mutableListOf(adbPath, "shell", "ls", "-l", "/sdcard/"))
    println(dirs)

//    ret = rmdir(foldername)
//    println("rmdir($foldername) returned $ret")

//    push("c:/temp/test.txt", "/sdcard/test1.txt")
//    val ret = exec(mutableListOf(adbPath, "shell", "ls", "-l", "/sdcard/"))
//    println(ret)

//    var ret = isFile("/sdcard/test1.txt")
//    println("isFile returned $ret")
//    ret = isFile("/sdcard/test2.txt")
//    println("isFile returned $ret")
//    ret = isFile("/sdcard/")
//    println("isFile returned $ret")

//    var ret = isFolder("/sdcard/")
//    println("isFolder returned $ret")
//    ret = isFolder("/sdcard")
//    println("isFolder returned $ret")
//    ret = isFolder("/sdcard/test1.txt")
//    println("isFolder returned $ret")
}
