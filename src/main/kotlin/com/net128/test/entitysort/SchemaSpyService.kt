package com.net128.test.entitysort

import org.schemaspy.Main
import org.schemaspy.cli.*
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.util.*
import java.util.concurrent.ConcurrentHashMap

@Service
class SchemaSpyService(
    private val schemaSpyRunner: SchemaSpyRunner,

    @Value("\${spring.datasource.url}")
    private val dbUrl: String,

    @Value("\${spring.datasource.username}")
    private val dbUsername: String,

    @Value("\${spring.datasource.password}")
    private val dbPassword: String,

    @Value("\${spring.datasource.driver-class-name}")
    private val dbDriver: String)  {

    fun callSchemaSpy() {
        // Get the class loader for the main class
        // Create the ProcessBuilder object
        val processBuilder = ProcessBuilder("java", "-jar", "schemaspy-<version>.jar", "-c", dbUrl, "-u", dbUsername, "-p", dbPassword, "-o", "html")

        // Start the process
        val process = processBuilder.start()

        // Get the input stream from the process
        val inputStream = process.inputStream

        // Get the output stream from the process
        val outputStream = process.outputStream

        // Write the connection string, username, password, and output format to the output stream
        outputStream.write("c=$dbUrl\n".toByteArray())
        outputStream.write("u=$dbUsername\n".toByteArray())
        outputStream.write("p=$dbPassword\n".toByteArray())
        outputStream.write("o=html\n".toByteArray())

        // Close the output stream
        outputStream.close()

        // Read the output from the input stream
        val output = inputStream.bufferedReader().readText()

        // Close the input stream
        inputStream.close()

        // Print the output
        println(output)
    }

    fun generateSchemaDocumentation(): Path {
        val outputDirectory = Files.createTempDirectory("schemaspy-output-")
        val args = arrayOf(
            "-t", "h2",
         //   "-dp", dbDriver,
            "-db", extractDbNameFromUrl(dbUrl),
            "-u", dbUsername,
            "-p", dbPassword,
            "-s", "S0",
            "-o", outputDirectory.toString()
        )
        Main.main(*args)
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

    private fun extractDbNameFromUrl(url: String): String {
        val parts = url.split("/")
        return parts.last()
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
