@file:Suppress("SpellCheckingInspection")

import java.io.File
import java.io.FileOutputStream
import java.net.URL
import java.nio.channels.Channels
import java.util.*
import java.util.zip.ZipFile

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

/**
 * Download a file from the given url
 * @param link: The url of the file to download
 * @param target: The destination file
 */
fun downloadFromUrl(link: String, target: File): Boolean {
    val url = URL(link)
    FileOutputStream(target).channel.transferFrom(
            Channels.newChannel(url.openStream()),
            0,
            Long.MAX_VALUE
    )
    return true
}

/**
 * Download the latest Android platform tools from Google.
 * @param foldername: The destination folder for the download
 * @return Full path of the downloaded file as a string in case of success, empty string in case of an failure.
 */
fun downloadPlatformTools(foldername: String = "C:/temp"): String {

    val file = File(foldername, "platform-tools.zip")
    downloadFromUrl("https://dl.google.com/android/repository/platform-tools-latest-windows.zip", file)

    if (file.isFile) {
        println("$file has been downloaded")
        return file.path
    }
    return ""
}

/**
 * Unzip the downloaded platform-tools.zip file in the given folder.
 * @param zipfile: The zipfile to unzip
 * @param foldername: The destination folder for unzipped files
 * @return Full path of the folder with unzipped files as a string in case of success, empty string in case of an failure.
 */
fun unzipPlatformTools(zipfile: File, foldername: String = "c:/temp"): String {

    if (!zipfile.isFile) {
        println("Could not find $zipfile")
        return ""
    }

    // Create the subfolder 'platform-tools' in the given target folder
    val targetfolder = File(foldername, "platform-tools")
    targetfolder.mkdirs()

    // Now really unzip the given zipfile here
    ZipFile(zipfile).use { zip ->
        zip.stream().forEach { entry ->
//            println(entry)
            if (entry.isDirectory)
                File(foldername, entry.name).mkdirs()
            else zip.getInputStream(entry).use { input ->
                File(foldername, entry.name).apply {
                    outputStream().use { output ->
                        input.copyTo(output)
                    }
                    setExecutable(true, false)
                }
            }
        }
    }
    zipfile.delete()        // @todo: Figure out why the file is NOT deleted here.
    return targetfolder.absolutePath
}