import java.util.*

//fun startProcess(vararg command: String, redirectErrorStream: Boolean = false): Process =
//    ProcessBuilder(*command).redirectErrorStream(redirectErrorStream).start()

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