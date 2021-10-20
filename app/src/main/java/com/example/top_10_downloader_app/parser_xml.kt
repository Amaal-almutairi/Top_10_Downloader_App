package com.example.top_10_downloader_app


import android.util.Log
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.util.*
import kotlin.collections.ArrayList

class parser_xml {
    private val Tag = "FeedParser"
    private val applications = ArrayList<feedEntry>()

    fun parse(xmlDate:String):Boolean{
        Log.d(Tag,"parse called with $xmlDate")
        var status = true
        var inEntry = false
        var textValue = ""

       try {
           val factory = XmlPullParserFactory.newInstance()
           factory.isNamespaceAware = true
           val xpp = factory.newPullParser()
           xpp.setInput(xmlDate.reader())
           var event_Type = xpp.eventType
           var currentType = feedEntry()
           while (event_Type != XmlPullParser.END_DOCUMENT) {
               val tagName = xpp.name?.toLowerCase()
               Log.d(Tag, "parse: tag for" + tagName)
               when (event_Type) {
                   XmlPullParser.START_TAG -> {
                       Log.d(Tag, "parse: Starting tag for  " + tagName)
                       if (tagName == "entry") {
                           inEntry = true
                       }
                   }
                   XmlPullParser.TEXT -> textValue = xpp.text
                   XmlPullParser.END_TAG -> { Log.d(Tag,"Parse:Ending Tag For "+ tagName)
                       if (inEntry){
                           when(tagName){
                               "entry"->{
                                   applications.add(currentType)
                                   inEntry= false
                                   currentType = feedEntry()
                               }
                               "name" -> currentType.name = textValue
                           }
                       }


                   }

               }
               event_Type = xpp.next()
           }
       } catch (e:Exception){
           e.printStackTrace()
           status = false
       }
        return status
    }
    fun getList():ArrayList<feedEntry>{
        return applications
    }
}

