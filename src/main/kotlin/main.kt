@file:Suppress("UNUSED_PARAMETER", "SpellCheckingInspection")

fun main(args: Array<String>) {

    print("Commandline arguments ")
    val argCount = args.size
    println("len = $argCount")

    // Print the arguments. @todo Remove this test output
    if (argCount > 0) {
        for (s in args) {
            println(s)
        }
    }
    else {
        println("No Commandline arguments given")
        return
    }

    // Check if ADB.exe can be found and started
    if (!checkADB()) {
        println("Error: Cannot continue due to error in ADB")
        return
    }

    when {
        args[0] == "ls" || args[0] == "dir" -> {
//            val ret = exec(mutableListOf(adbPath, "shell", "ls", "-l", "/sdcard/"))
            val ret = exec(mutableListOf(adbPath, "shell", *args))
            println(ret)
        }
        else -> {
            println("Unkown argument " + '"' + args[0] + '"')
            return
        }
    }


//    // test ls. @todo Remove this test from here
//    val ret = exec(mutableListOf(adbPath, "shell", "ls", "-l", "/sdcard/"))
//    println(ret)

}










