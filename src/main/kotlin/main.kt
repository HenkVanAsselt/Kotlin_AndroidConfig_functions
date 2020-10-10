@file:Suppress("UNUSED_PARAMETER", "SpellCheckingInspection")

import java.util.*
import kotlin.system.exitProcess


fun main(args: Array<String>) {

    // Determine the number of commandline arguments. Used for slicing later.
    val argCount = args.size

    // Test if any commandline arguments are given. If there are, print them.
    if (argCount == 0) {
        println("No Commandline arguments given")
        exitProcess(22)       // ERROR_BAD_COMMAND
    }
    else {
        for (s in args) print("$s ")
        println()
    }

    // Create a slice of commandline arguments, removing the very first one.
    // This will make it possible later to use for example 'dir' instead of 'ls'
    // So "dir -l /sdcard/" will be processed as "ls -l /sdcard"
    var otherArgs = arrayOf("")
    if (argCount > 1){
        otherArgs = args.sliceArray(1 until argCount)
    }

    // Check if ADB.exe can be found and started
    if (!checkADB()) {
        println("Error: Cannot continue due to error in ADB")
        exitProcess(5)      // ERROR_ACCESS_DENIED
    }

    // Process the commandline arguments
    val command = args[0]
    when {
        command == "version" -> {
            val ret = exec(mutableListOf(adbPath, "version"))
            println(ret.trim())
            println("Kotlin version ${KotlinVersion.CURRENT}")
            exitProcess(0)  // ERROR_SUCCESS
        }
        command == "shell" -> {
            val commands: MutableList<String> = ArrayList()
            commands.add("cmd.exe")
            commands.add("/C")
            commands.add("start")
            commands.add(adbPath)
            commands.add("shell")
            // I realize that this can also be done with
            //      val commands: mutableListOf("cmd.exe", "/C", "start" adbPath, "shell")
            // But I will leave it here just to show the possiblities.
            val pb = ProcessBuilder(commands)
            pb.start()
            exitProcess(0)  // ERROR_SUCCESS
        }
        command == "ls" || command == "dir" -> {
            val ret = exec(mutableListOf(adbPath, "shell", "ls", *otherArgs))
            println(ret)
            exitProcess(0)  // ERROR_SUCCESS
        }
        command == "reboot" -> {
            // Arguments could be:  reboot, reboot bootloader, reboot recovery
            val ret = exec(mutableListOf(adbPath, *args))
            println(ret)
            exitProcess(0)  // ERROR_SUCCESS
        }
        command == "restart" -> {
            println("Step 1: killing adb service")
            var ret = exec(mutableListOf(adbPath, "kill-server"))
            print(ret)
            println("Step 2: restarting adb.exe")
            ret = exec(mutableListOf(adbPath, "start-server"))
            println(ret)
            if ("daemon started successfully" in ret) {
                exitProcess(0)  // ERROR_SUCCESS
            }
            exitProcess(21)     // ERROR_NOT_READY
        }
        command == "isdir" -> {
            val androidfolder = args[1]
            if (isDir(androidfolder)) {
                println("found folder $androidfolder")
                exitProcess(0)  // ERROR_SUCCESS
            }
            println("Could not find folder $androidfolder")
            exitProcess(3)      // ERROR_PATH_NOT_FOUND
        }
        command == "isfile" -> {
            val androidfile = args[1]
            if (isFile(androidfile)) {
                println("found file $androidfile")
                exitProcess(0)  // ERROR_SUCCESS
            }
            println("Could not find file $androidfile")
            exitProcess(2)      // ERROR_FILE_NOT_FOUND
        }
        else -> {
            println("Unkown argument " + '"' + command + '"')
            println("However, just trying if it is a valid shell command")
            val ret = exec(mutableListOf(adbPath, "shell", *args))
            println(ret)
            exitProcess(1)      // ERROR_INVALID_FUNCTION
        }
    }
    exitProcess(1)      // ERROR_INVALID_FUNCTION
}
