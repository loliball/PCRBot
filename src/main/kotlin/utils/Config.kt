package utils

import java.io.*
import java.util.*

object Config {
    private var prop: Properties = Properties()
    private val defaultFile = File("config.txt")

    init {
        load(defaultFile)
    }

    fun load(configFile: File = defaultFile) {
        configFile.inputStream().use {
            prop.load(it)
        }
    }

    operator fun get(name: String): String = prop.getProperty(name)

}