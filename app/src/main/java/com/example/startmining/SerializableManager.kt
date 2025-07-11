package com.example.startmining

import android.content.Context
import java.io.IOException
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.io.Serializable


object SerializableManager {
    /**
     * Saves a serializable object.
     *
     * @param context The application context.
     * @param objectToSave The object to save.
     * @param fileName The name of the file.
     * @param <T> The type of the object.
    </T> */
    fun <T : Serializable?> saveSerializable(
        context: Context,
        objectToSave: T?,
        fileName: String?
    ) {
        try {
            val fileOutputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE)
            val objectOutputStream = ObjectOutputStream(fileOutputStream)

            objectOutputStream.writeObject(objectToSave)

            objectOutputStream.close()
            fileOutputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    /**
     * Loads a serializable object.
     *
     * @param context The application context.
     * @param fileName The filename.
     * @param <T> The object type.
     *
     * @return the serializable object.
    </T> */
    fun <T : Serializable?> readSerializable(context: Context, fileName: String?): T? {
        var objectToReturn: T? = null

        try {
            val fileInputStream = context.openFileInput(fileName)
            val objectInputStream = ObjectInputStream(fileInputStream)
            objectToReturn = objectInputStream.readObject() as T?

            objectInputStream.close()
            fileInputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        }

        return objectToReturn
    }

    /**
     * Removes a specified file.
     *
     * @param context The application context.
     * @param filename The name of the file.
     */
    fun removeSerializable(context: Context, filename: String?) {
        context.deleteFile(filename)
    }
}