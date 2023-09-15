package com.net128.test.entitysort

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.util.*

@Service
class SchemaSpyService(
    @Value("\${spring.datasource.url}")
    private val dbUrl: String,

    @Value("\${spring.datasource.username}")
    private val dbUsername: String,

    @Value("\${spring.datasource.password}")
    private val dbPassword: String,

    @Value("\${spring.datasource.driver-class-name}")
    private val dbDriver: String)  {

    fun generateSchemaDocumentation() : Path {
        val outputDirectory = Files.createTempDirectory("schemaspy-output-")
      //  val currentClasspath = System.getProperty("java.class.path").replace(" ", "%20")
      //  val tempFile = Files.createTempFile("classpath", ".txt").toFile()
        val schemaSpyJar = File("runtime").findMatchingFile("schemaspy-.*-fat.jar".toRegex())?.absolutePath ?:""
        val driverJar = File("runtime").findMatchingFile("h2.*.jar".toRegex())?.absolutePath ?:""

//        var h2Properties = Thread.currentThread().contextClassLoader.getResourceAsStream("h2.properties")?.use {
//            it.reader().readText() } ?: throw IllegalArgumentException("Resource not found!")
//        h2Properties = h2Properties.replace("connectionSpec=.*".toRegex(RegexOption.MULTILINE), "connectionSpec=$dbUrl")
//
//        val h2PropertiesFile = File.createTempFile("h2PropertiesFile", ".properties")
//        h2PropertiesFile.deleteOnExit()
//
//        println(h2Properties)
        //CONNECTIONSPEC
        //   tempFile.writeText("$schemaSpyJar:$currentClasspath")
     //   tempFile.deleteOnExit()
        // Get the class loader for the main class
        // Create the ProcessBuilder object
        val processBuilder = ProcessBuilder("java", "-jar", schemaSpyJar,
            "-t", guessDbTypeFromUrl(dbUrl), "-dp", driverJar, "-c", dbUrl, "-u", dbUsername, "-p", dbPassword, "-o", "html")

        processBuilder.inheritIO()
        // Start the process
        val process = processBuilder.start()

        // Read the output from the input stream
        val output = process.inputStream.bufferedReader().readText()
        val errors = process.errorStream.bufferedReader().readText()
        println(output)
        println(errors)
        return outputDirectory
    }

    fun generateSchemaDocumentation1(): Path {
        val outputDirectory = Files.createTempDirectory("schemaspy-output-")
        val args = arrayOf(
            "-t", "h2",
         //   "-dp", dbDriver,
            "-db", guessDbTypeFromUrl(dbUrl),
            "-u", dbUsername,
            "-p", dbPassword,
            "-s", "S0",
            "-o", outputDirectory.toString()
        )
        //Main.main(*args)
        return outputDirectory
    }


    /*
    fun generateSchemaDocumentation(): Path {
        val dbType = guessDbTypeFromUrl(dbUrl)

        val outputDirectory = Files.createTempDirectory("schemaspy-output-")

        val args = arrayOf(
            "-t", dbType,
            "-dp", dbDriver,
            "-db", extractDbNameFromUrl(dbUrl),
            "-u", dbUsername,
            "-p", dbPassword,
            "-s", "S0",
            "-o", outputDirectory.toString()
        )

        try {
            schemaSpyRunner.run(*args)
        } catch (e: Exception) {
            throw RuntimeException("Failed to generate schema documentation", e)
        }

        return outputDirectory
    }*/

    fun File.findMatchingFile(pattern: Regex): File? {
        return this.listFiles()?.find { it.isFile && pattern.matches(it.name) }
    }

    private fun extractDbNameFromUrl(url: String): String {
        return url.split("/").last().split(";").first()
    }

    private fun guessDbTypeFromUrl(url: String): String {
        return when {
            url.contains("mysql") -> "mysql"
            url.contains("postgresql") -> "pgsql"
            url.contains("sqlserver") -> "mssql"
            url.contains("oracle") -> "ora"
            url.contains("db2") -> "db2"
            url.contains("sqlite") -> "sqlite"
            url.contains("h2") -> "h2"
            else -> throw UnsupportedOperationException("Cannot determine dbType from URL: $url")
        }
    }
}
