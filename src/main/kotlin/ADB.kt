/**
 * # Module ADB
 * ADB related variables and functions
 */

import java.io.File

/** Location of File adb.exe */
val adbFile = File("D:\\bin\\platform-tools\\adb.exe")

/** String representation of the full path to adb.exe */
val adbPath: String = adbFile.absolutePath

/**
 * Check if ADB is functional, adn if a device is connected and accessible.
 * @return true if ADB is active and a connected device was found
 */
fun checkADB(): Boolean {

    // Check if the executable adb.exe can be found on this PC
    if (!adbFile.isFile) {
        println("Error: Could not find $adbFile")
        return false
    }

    // Evaluate and react on the response of "adb devices"
    // There has to be at least one device connected and usable
    val ret = exec(mutableListOf(adbPath, "devices"))
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
        else -> println("NOTE to developer: this return text is not processed yet: $ret")

    }
    println("Warning: ADB response to validate: $ret")
    return true
}