package com.shadi777.build_logic

import com.android.build.api.artifact.SingleArtifact
import com.android.build.api.variant.AndroidComponentsExtension
import kotlinx.coroutines.runBlocking
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import java.io.File

class UploadPlugin : Plugin<Project> {
    override fun apply(project: Project) {

        val buildLogic = project.extensions.create(
            "UploadExtension", UploadExtension::class.java
        ).apply {
            doSizeValidation.set(true)
            maxApkSizeInMBytes.set(18.0)
        }

        val androidComponents =
            project.extensions.findByType(AndroidComponentsExtension::class.java)
                ?: throw GradleException("'com.android.application' plugin required.")

        androidComponents.onVariants { variant ->
            // DONT TRY
            //variant.artifacts.get(SingleArtifact.APK).get().asFile.renameTo(File("todolist-${variant.name}-${project.version}.apk"))
            //project.buildFile.renameTo(File("todolist-${variant.name}-${project.version}.apk"))

            val capVariantName = variant.name.replaceFirstChar { it.uppercaseChar() }
            val apkDirectory = variant.artifacts.get(SingleArtifact.APK)
            val validationFile = File(
                project.buildDir.absolutePath + "\\outputs\\validateOutput.txt"
                        ).apply {
                delete()
                createNewFile()
                        }

            val validateTask = project.tasks.register(
                "validateApkSizeFor$capVariantName",
                ValidateSizeTask::class.java
            ) {
                apkDir.set(apkDirectory)
                maxApkSizeInMBytes.set(buildLogic.maxApkSizeInMBytes.get())
                outputFile.set(validationFile)
            }

            val uploadTask = project.tasks.register(
                "uploadApkFor$capVariantName",
                UploadTask::class.java
            ) {
                if (buildLogic.doSizeValidation.get()) {
                    dependsOn(validateTask) // TODO wait for file
                    apkSizeInMBytes.set(validationFile.readText().toDouble())
                }
                apkDir.set(apkDirectory)
            }

            uploadTask.get().mustRunAfter(validateTask)
            validateTask.get().finalizedBy(uploadTask)
        }
    }
}
