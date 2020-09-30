/**
 * Read the device properties, and return a map of these properties
 * @return: map of the properties
 */
fun propertiesToMap(): Map<String, String> {

    val data = emptyMap<String, String>().toMutableMap()

    val propString = exec(mutableListOf(adbPath, "shell", "getprop"))

    propString.trim().lines().forEach {
        val parts = it.split("]: [")
        if (parts.size == 2)
            data[parts[0].trimStart('[')] = parts[1].trimEnd(']')
    }

    return data
}


private fun main() {
    val properties = propertiesToMap()
    println("properties map = $properties")
}