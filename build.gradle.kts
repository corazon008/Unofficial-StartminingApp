buildscript {
    val agp_version by extra("8.11.1")
}
// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.11.1" apply false
    id("org.jetbrains.kotlin.android") version "2.2.0" apply false
    kotlin("jvm") version "2.0.10"
    kotlin("plugin.serialization") version "2.0.10"
}