package pro.yggdra.util

import pro.yggdra.JavaPaper
import java.util.logging.ConsoleHandler
import java.util.logging.Level
import java.util.logging.Logger

class Messenger {

    companion object {
        var allowed = true
        private var logger = Logger.getLogger(JavaPaper::class.java.name)

        init {
            logger.useParentHandlers = false
            logger.level = Level.INFO

            val handler = ConsoleHandler()
            val formatter = LogFormatter()
            handler.formatter = formatter
            logger.addHandler(handler)
        }

        fun info(message: String) {
            if (allowed) logger.info(message)
        }
    }

}