package com.example.scheduleapp

/* Copyright (C) Cyrus Singer - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Cyrus Singer <japaneserhino@gmail.com>, October 2020
 */

import android.content.Context
import com.google.api.client.auth.oauth2.Credential
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.JsonFactory
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.client.util.store.FileDataStoreFactory
import com.google.api.services.classroom.Classroom
import com.google.api.services.classroom.ClassroomScopes
import java.io.*
import java.security.GeneralSecurityException
import java.util.*

//REDUNDANT
class GoogleClassroomInterface {

    private val APPLICATION_NAME: String  = "Cauch Android";
    private val  JSON_FACTORY: JsonFactory = JacksonFactory.getDefaultInstance();
    private val TOKENS_DIRECTORY_PATH: String = "tokens"

    /**
     * Global instance of the scopes required by this quickstart.
     * If modifying these scopes, delete your previously saved tokens/ folder.
     */
    private val SCOPES: List<String> =
        Collections.singletonList(ClassroomScopes.CLASSROOM_COURSES_READONLY)
    private val CREDENTIALS_FILE_PATH = "/credentials.json"

    /**
     * Creates an authorized Credential object.
     * @param HTTP_TRANSPORT The network HTTP Transport.
     * @return An authorized Credential object.
     * @throws IOException If the credentials.json file cannot be found.
     */
    @Throws(IOException::class)
    private fun getCredentials(HTTP_TRANSPORT: NetHttpTransport,context: Context): Credential? {
        // Load client secrets.

        val `in`: InputStream = context.getResources().openRawResource(R.raw.credentials)
        val clientSecrets =
            GoogleClientSecrets.load(JSON_FACTORY, InputStreamReader(`in`))

        // Build flow and trigger user authorization request.
        val flow:GoogleAuthorizationCodeFlow  = GoogleAuthorizationCodeFlow.Builder(
            HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
            .setDataStoreFactory(FileDataStoreFactory(File(context.filesDir,TOKENS_DIRECTORY_PATH)))
            .setAccessType("offline")
            .build()
        val receiver: LocalServerReceiver = LocalServerReceiver.Builder().setPort(8888).build()
        return AuthorizationCodeInstalledApp(flow, receiver).authorize("user")
    }

    @Throws(IOException::class, GeneralSecurityException::class)
    fun main(context: Context, args: Array<String>) {
        // Build a new authorized API client service.
        val HTTP_TRANSPORT = com.google.api.client.http.javanet.NetHttpTransport()
        val service =
            Classroom.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT,context))
                .setApplicationName(APPLICATION_NAME)
                .build()

        // List the first 10 courses that the user has access to.
        val response = service.courses().list()
            .setPageSize(10)
            .execute()
        val courses = response.courses
        if (courses == null || courses.size == 0) {
            println("No courses found.")
        } else {
            println("Courses:")
            for (course in courses) {
                System.out.printf("%s\n", course.name)
            }
        }
    }
}