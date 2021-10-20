package com.example.top_10_downloader_app

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.*
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL



class feedEntry{
    var name: String = ""

    override fun toString(): String {
        return """
            name = $name
            """.trimIndent()
    }
}
class MainActivity : AppCompatActivity() {
    private val Tag = "MainActivity"
    lateinit var tvfeed: TextView
    lateinit var feedBtn: Button
    lateinit var rvFeed: RecyclerView
    lateinit var itemList: ArrayList<String>
    val feedURL =
        "http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topfreeapplications/limit=10/xml"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d(Tag, "onCreate called")
        tvfeed = findViewById(R.id.tvmain)
        feedBtn = findViewById(R.id.b1)
        itemList = arrayListOf()
        feedBtn.setOnClickListener {
            requestApi(feedURL)
            initrecyclerview()
        }
           Log.d(Tag,"onCreate: done")
        }



        private fun initrecyclerview() {
            rvFeed = findViewById(R.id.rv)
            rvFeed.layoutManager = LinearLayoutManager(this)
            rvFeed.setHasFixedSize(true)

        }

        private fun downloadXML(urlPath: String?): String {
            val xmlResult = StringBuilder()

            try {
                val url = URL(urlPath)
                val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
                val response = connection.responseCode
                Log.d(Tag, "downloadXML: The response code was $response")
                val reader = BufferedReader(InputStreamReader(connection.inputStream))
                val inputBuffer = CharArray(500)
                var CharsRead = 0
                while (CharsRead >= 0) {
                    CharsRead = reader.read(inputBuffer)
                    if (CharsRead > 0) {
                        xmlResult.append(String(inputBuffer, 0, CharsRead))
                    }
                }
                reader.close()
                Log.d(Tag, "Received ${xmlResult.length} bytes")
                return xmlResult.toString()


            } catch (e: MalformedURLException) {
                Log.e(Tag, "downloadXML: Invalid URL: ${e.message}")

            } catch (e: IOException) {
                Log.e(Tag, "downloadXML: IO Exception reading data: ${e.message}")
            } catch (e: SecurityException) {
                e.printStackTrace()
                Log.e(Tag, "downloadXML: Security exception.  Needs permissions? ${e.message}")
            } catch (e: Exception) {
                Log.e(Tag, "Unknown error: ${e.message}")
            }
            return ""

        }

        private fun requestApi(url:String){

            var listItems = ArrayList<feedEntry>()

            CoroutineScope(Dispatchers.IO).launch {


                val rssFeed = async {

                    downloadXML(url)

                }.await()

                if (rssFeed.isEmpty()) {
                    Log.e(Tag, "request Api fun: Error downloading")
                } else {

                    val parseApplications = async {

                        parser_xml()

                    }.await()

                    parseApplications.parse(rssFeed)
                    listItems = parseApplications.getList()


                    withContext(Dispatchers.Main) {


                        rvFeed.adapter = myAdapter(listItems)


                    }
                }



            }

        }}





