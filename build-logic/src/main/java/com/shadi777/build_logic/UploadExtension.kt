package com.shadi777.build_logic

import org.gradle.api.provider.Property

interface UploadExtension {
    abstract val doSizeValidation: Property<Boolean>
    abstract val maxApkSizeInMBytes: Property<Double>
}