import java.io.File
import java.util.*

val adb = File("D:\\bin\\platform-tools\\adb.exe")
val adb_exe = adb.absolutePath


fun main(args: Array<String>) {


//    println(checkADB())
    val path = "/sdcard/honeywell/"
    var ret = exec(mutableListOf(adb_exe, "shell", "ls", "-l", "-R", path))
    println(ret)
}

/**
 * Check if a device is connected over ADB
 * return: true if a device was found, false if not
 */
fun checkADB(): Boolean {
    val ret = exec(mutableListOf(adb_exe, "devices"))
    when {
        "no devices/emulators found" in ret -> return false
    }
    return true
}



fun startProcess(vararg command: String, redirectErrorStream: Boolean = false): Process =
    ProcessBuilder(*command).redirectErrorStream(redirectErrorStream).start()

fun startProcess(command: List<String?>, redirectErrorStream: Boolean = false): Process =
    ProcessBuilder(command).redirectErrorStream(redirectErrorStream).start()

fun exec(vararg args: MutableList<String>, redirectErrorStream: Boolean = true): String {
    val sb = StringBuilder()
    args.forEach {
        Scanner(startProcess(it, redirectErrorStream).inputStream, "UTF-8").useDelimiter("").use { scanner ->
            while (scanner.hasNextLine())
                sb.append(scanner.nextLine() + '\n')
        }
    }
    return sb.toString()
}

