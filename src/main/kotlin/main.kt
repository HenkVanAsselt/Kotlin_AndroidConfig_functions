import java.io.File
import java.util.*

val adb = File("D:\\bin\\platform-tools\\adb.exe")
val adb_exe = adb.absolutePath


fun main(args: Array<String>) {

    if (!checkADB()) {
        println("Error: Cannot continue due to error in ADB")
        return
    }
    val path = "/sdcard/honeywell/"
    var ret = exec(mutableListOf(adb_exe, "shell", "ls", "-l", path))
    println(ret)
}

/**
 * Check if a device is connected over ADB
 * return: true if ADB is active and a connected device was found
 */
fun checkADB(): Boolean {
    if (!adb.isFile) {
        println("Error: Could not find " + adb)
        return false
    }

    // Evaluate and react on the response of "adb devices"
    // There has to be at least one device connected and usable
    val ret = exec(mutableListOf(adb_exe, "devices"))
    when {
        "no devices/emulators found" in ret -> {
            println(ret)
            return false
        }
        "List of devices attached" == ret.trim() -> {
            println("No device(s) are connected")
            return false
        }
        "unauthorized" in ret -> {
            println(ret)
            return false
        }
        "no permission" in ret -> {
            println(ret)
            return false
        }
        "\tdevice" in ret -> {
            // At least one device was found
            return true
        }
    }
    println("Warning: ADB response to validate: " + ret)
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

