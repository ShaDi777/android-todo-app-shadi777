package com.shadi777.build_logic

import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import kotlinx.coroutines.runBlocking
import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.TaskAction
import kotlin.math.roundToInt

private const val TOKEN = "6309155729:AAEaaN1n1oShlMI-1gA35f6CHodFD14b0Lw"
private const val CHAT_ID  = "1070910428"

abstract class UploadTask : DefaultTask() {

    @get:InputDirectory
    abstract val apkDir: DirectoryProperty

    @get:Optional
    @get:Input
    abstract val apkSizeInMBytes: Property<Double>

    @TaskAction
    fun upload() {
        val api = TelegramApi(HttpClient(OkHttp))
        val token = System.getenv("TG_TOKEN") ?: TOKEN
        val chatId = System.getenv("TG_CHAT_ID") ?: CHAT_ID

        runBlocking {
            apkDir.get().asFile.listFiles()
                ?.filter { it.name.endsWith(".apk") }
                ?.forEach {
                    println("FILE = ${it.absolutePath}")
                    if (apkSizeInMBytes.isPresent)
                        println("SIZE = ${apkSizeInMBytes.get()}")

                    //api.uploadFile(it, token, chatId)
                    //if (apkSizeInMBytes.isPresent)
                    //    api.sendMessage("Apk size: ${apkSizeInMBytes.get()} MB", token, chatId)
                }
        }
    }
}