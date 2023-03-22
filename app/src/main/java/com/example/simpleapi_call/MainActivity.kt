package com.example.simpleapi_call

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.gson.Gson
import org.json.JSONObject
import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.SocketTimeoutException
import java.net.URL


// Site link
// https://run.mocky.io/v3/1f85386a-6d6c-426a-88ee-8efe310e6c25
// Mocky is used to fake API calls to test whether they work

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // Below is the template for a GET request
        /** CallAPILoginAsyncTask().execute() */
        // Below is the template for a POST request
        CallAPILoginAsyncTask("Eric","Password").execute()
    }

    // The inner keyword prevents the CallAPILoginAsyncTask from having a context
    @SuppressLint("StaticFieldLeak")
    // Below is the template for a GET request....constructor with no parameters
    /** private inner class CallAPILoginAsyncTask(): AsyncTask<Any, Void, String>() { */
    // Below is the template for a POST request
    private inner class CallAPILoginAsyncTask(val username: String,val password: String): AsyncTask<Any, Void, String>() {
        // Can be useful when you want to delay the initialization of a variable to a later point in time
        private lateinit var customProgressDialog: Dialog
        /**
         * This function is for the task which we wants to perform before background execution.
         * Here we have shown the progress dialog to user that UI is not frozen but executing something in background.
         */
        @Deprecated("Deprecated in Java")
        override fun onPreExecute() {
            super.onPreExecute()
            Log.e("JSON RESPONSE RESULT", "Null")
            showProgressDialog()
        }

        @Deprecated("Deprecated in Java")
        override fun doInBackground(vararg p0: Any?): String {
            var result = "Null"
            // Used to establish a connection
            var connection: HttpURLConnection? = null
            try {
                val url = URL("https://run.mocky.io/v3/1f85386a-6d6c-426a-88ee-8efe310e6c25")
                // Establish the connection
                connection = url.openConnection() as HttpURLConnection
                /**
                 * A URL connection can be used for input and/or output.  Set the DoOutput
                 * flag to true if you intend to use the URL connection for output,
                 * false if not.  The default is false.
                 */
                connection.doOutput = true
                connection.doInput = true

                /** Post data */
                // Sets whether HTTP redirects should be automatically followed by this instance. The default is always true
                connection.instanceFollowRedirects = false
                // Set the method for the URL request, one of:
                // GET
                // POST
                // HEAD
                // OPTIONS
                // PUT
                // DELETE
                // TRACE
                // are legal, subject to protocol restrictions.  The default method is GET.
                connection.requestMethod = "POST"

                // Sets the general request property. If a property with the key already
                // exists, overwrite its value with the new value.
                // These options are also visible from the Mocky website
                connection.setRequestProperty("Content-Type", "application/json")
                connection.setRequestProperty("charset", "utf-8")
                connection.setRequestProperty("Accept", "application/json")

                // Some protocols do caching of documents.  Occasionally, it is important to be able to "tunnel through" and ignore the caches (e.g., the "reload" button in a browser).  If the UseCaches flag on a connection
                // is true, the connection is allowed to use whatever caches it can.
                //  If false, caches are to be ignored.
                // The default value comes from DefaultUseCaches, which defaults to
                // true.

                connection.useCaches = false

                // Now we have to write the data
                // Creates a new data output stream to write data to the specified underlying output stream
                val write = DataOutputStream(connection.outputStream)
                // Create JSONObject Request
                val jsonRequest = JSONObject()
                jsonRequest.put("username", username) // Request Parameter 1
                jsonRequest.put("password", password) // Request Parameter 2

                write.writeBytes(jsonRequest.toString())
                write.flush()
                write.close()
                /** Post data */



                /** Receive data */
                val httpResult: Int = connection.responseCode // Gets the status code from an HTTP response message.
                if (httpResult == HttpURLConnection.HTTP_OK) {
                    /**
                     * Returns an input stream that reads from this open connection.
                     */
                    val inputStream = connection.inputStream
                    val reader = BufferedReader(InputStreamReader(inputStream))
                    val sb = StringBuilder()
                    var line: String?
                    // Read every line of the inputStream
                    try {
                        // While the reader still has something to read
                        // .also is equivalent to and
                        // it is the result of the readLine
                        // Within the also block line gets the value of it, if it is null line will also be null
                        while (reader.readLine().also { line = it } != null) {
                            // Append and then ski to the next line
                            sb.append(line + "\n")
                        }
                    } catch (e: IOException) {
                        e.printStackTrace()
                    } finally { // Close the input stream
                        try {
                            inputStream.close()
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                    }
                    result = sb.toString()
                } else { // If there is a problem
                    result = connection.responseMessage
                }
            } catch (e: Exception) {
               result = "Error + ${e.printStackTrace()}"
            } catch (e : SocketTimeoutException) {
                result = "Connection timeout"
            } finally {
                connection?.disconnect()
            }
            /** Receive data */
            return result
        }
        /**
         * This function will be executed after the background execution is completed.
         */
        @Deprecated("Deprecated in Java")
        override fun onPostExecute(result: String) {
            super.onPostExecute(result)
            cancelProgressDialog()
            Log.i("JSON RESPONSE RESULT", result)

            /** Using GSON to retrieve the data */

            // Data class to be used as the model is passed
            val responseData = Gson().fromJson(result, ResponseData::class.java)

            Log.i("GSON message",responseData.message)
            Log.i("GSON user id","${responseData.user_id}")
            Log.i("GSON name",responseData.name)
            Log.i("GSON email",responseData.email)

            Log.i("GSON is profile", "${responseData.profile_details.is_profile_completed}")
            Log.i("GSON rating", "${responseData.profile_details.rating}")

            Log.i("GSON data list size","${responseData.data_list.size}")
            // Iterate using the position int value of the list
            for(item in responseData.data_list.indices) {
                Log.i("GSON value ${item}", "${responseData.data_list[item]}")

                Log.i("GSON ID","${responseData.data_list[item].id}")
                Log.i("GSON value",responseData.data_list[item].value)
            }

            /** Using GSON to retrieve the data */

            /**
             *Creates a new with name/value mappings from the JSON string.
             */
            val jsonObject = JSONObject(result)

            // Returns the value mapped by {name} if it exists.
            val message = jsonObject.optString("message")
            Log.i("Message", message)
            // Returns the value mapped by {name} if it exists.
            val userId = jsonObject.optInt("user_id")
            Log.i("User Id", userId.toString())

            // Returns the value mapped by {name} if it exists.
            val name = jsonObject.optString("name")
            Log.i("Name", name)

            // Returns the value mapped by {name} if it exists.
            val email = jsonObject.optString("email")
            Log.i("Email", email)

            // Returns the value mapped by {name} if it exists.
            val profileDetailsObject = jsonObject.optJSONObject("profile_details")
            val isProfileCompleted = profileDetailsObject?.optBoolean("is_profile_completed")
            Log.i("Is Profile Completed", "$isProfileCompleted")
            val rating = profileDetailsObject?.optDouble("rating")
            Log.i("Rating", "$rating")

            // Returns the value mapped by {name} if it exists.
            // Using the array as a JSON object
            val dataListArray = jsonObject.optJSONArray("data_list")
            Log.i("Data List Size", "${dataListArray.length()}")
            for (item in 0 until dataListArray.length()) {
                Log.i("Value $item", "${dataListArray[item]}")
                // Fetching the values in the array
                val dataItemObject: JSONObject = dataListArray[item] as JSONObject
                val id = dataItemObject.optString("id")
                Log.i("ID", id)

                val value = dataItemObject.optString("value")
                Log.i("Value", value)


            }




        }

        /**
         * Method is used to show the Custom Progress Dialog.
         */
        private fun showProgressDialog() {
            customProgressDialog = Dialog(this@MainActivity)
            /*Set the screen content from a layout resource.
            The resource will be inflated, adding all top-level views to the screen.*/
            customProgressDialog.setContentView(R.layout.dialog_custom_progress)
            //Start the dialog and display it on screen.
            customProgressDialog.show()
        }
        /**
         * This function is used to dismiss the progress dialog if it is visible to user.
         */
        private fun cancelProgressDialog() {
            customProgressDialog.dismiss()
        }
    }
}