@file:Suppress("UNUSED_PARAMETER", "SpellCheckingInspection")

import java.util.*


fun main(args: Array<String>) {

    // Determine the number of commandline arguments. Used for slicing later.
    val argCount = args.size

    // Test if any commandline arguments are given. If there are, print them.
    if (argCount == 0) {
        println("No Commandline arguments given")
        return // 0x00000005   // ERROR_ACCESS_DENIED
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
        otherArgs = args.sliceArray(1 until argCount - 1)
    }

    // Check if ADB.exe can be found and started
    if (!checkADB()) {
        println("Error: Cannot continue due to error in ADB")
        return // 0x00000005   // ERROR_ACCESS_DENIED
    }

    // Process the commandline arguments
    when {
        args[0] == "shell" -> {
            val commands: MutableList<String> = ArrayList()
            commands.add("cmd.exe")
            commands.add("/C")
            commands.add("start")
            commands.add(adbPath)
            commands.add("shell")
            val pb = ProcessBuilder(commands)
            pb.start()
        }
        args[0] == "ls" || args[0] == "dir" -> {
            val ret = exec(mutableListOf(adbPath, "shell", "ls", *otherArgs))
            println(ret)
            return
        }
        args[0] == "reboot" -> {
            // Arguments could be:  reboot, reboot bootloader, reboot recovery
            val ret = exec(mutableListOf(adbPath, *args))
            println(ret)
            return
        }
        args[0] == "restart" -> {
            println("Step 1: killing adb service")
            var ret = exec(mutableListOf(adbPath, "kill-server"))
            println(ret)
            println("Step 2: restarting adb.exe")
            ret = exec(mutableListOf(adbPath, "start-server"))
            println(ret)
            if ("daemon started successfully" in ret) {
                return // 0x00000000  // ERROR_SUCCESS
            }
            return // 0x00000015 // ERROR_NOT_READY
        }
        else -> {
//            println("Unkown argument " + '"' + args[0] + '"')
//            return
            // Just try if this is a valid shell command....
            val ret = exec(mutableListOf(adbPath, "shell", *args))
            println(ret)
            return // 0x00000000  // ERROR_SUCCESS
        }
    }
    // return // 0x00000001   // ERROR_INVALID_FUNCTION
}
