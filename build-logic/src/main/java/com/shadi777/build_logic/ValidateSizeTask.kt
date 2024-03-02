package com.shadi777.build_logic

import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import org.gradle.internal.enterprise.test.FileProperty
import org.gradle.internal.impldep.org.eclipse.jgit.internal.ketch.Proposal
import java.io.File
import kotlin.math.roundToInt

abstract class ValidateSizeTask : DefaultTask() {

    @get:InputDirectory
    abstract val apkDir: DirectoryProperty

    @get:Input
    abstract val maxApkSizeInMBytes: Property<Double>

    @get:OutputFile
    abstract val outputFile: Property<File>

    @TaskAction
    fun validate() {
        val MByte = 1024 * 1024
        apkDir.get().asFile.listFiles()
            ?.filter { it.name.endsWith(".apk") }
            ?.forEach { file ->
                val sizeInMBytes = ((file.length().toDouble() / MByte.toDouble()) * 100).roundToInt() / 100.0
                println("Validation: $sizeInMBytes <= ${maxApkSizeInMBytes.get()}")
                require(sizeInMBytes <= maxApkSizeInMBytes.get())

                val fileWithSizeMBytes = outputFile.get()
                fileWithSizeMBytes.writeText(sizeInMBytes.toString())

                println("OutputFile = " + fileWithSizeMBytes.absolutePath)
            }


    }
}