package com.example.webreport.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.json.JSONArray
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponents
import org.springframework.web.util.UriComponentsBuilder
import org.thymeleaf.util.StringUtils
import org.w3c.dom.Document
import javax.xml.parsers.DocumentBuilderFactory

@Controller
@RequestMapping("/")
class IndexController{

    @GetMapping(path = ["index"])
    fun index(model: Model): String {
        model.addAttribute("greeting", "Hello Thymeleaf!")
        println("index")
        val factory = HttpComponentsClientHttpRequestFactory();

        factory.setConnectTimeout(5000)
        factory.setReadTimeout(5000)

        val restTemplate = RestTemplate(factory)
        val header = HttpHeaders()
        val entity = HttpEntity<Map<String, Any>>(header)
        val url ="http://www.chungnam.go.kr/stats/cnHumanStatsToJson.do"
        val uri : UriComponents
                = UriComponentsBuilder.fromHttpUrl(url).build()

        val resultMap : ResponseEntity<String>
                = restTemplate.exchange(uri.toString(),HttpMethod.GET, entity, String::class.java)
        println(resultMap.body)
        val mapper = ObjectMapper()
        var jsonInString = mapper.writeValueAsString(resultMap.getBody());
        var tmp1 = jsonInString.toString().replace("\"{\\\"resultList\\\":", "")
        var tmp2 = tmp1.substring(0, tmp1.length-2)
        println(tmp2.replace("\\", ""))
        val jarray = JSONArray(tmp2.replace("\\", ""))
        println(jarray.getJSONObject(0))
        model.addAttribute("jsonInString", jarray)
        return "index"
    }
}