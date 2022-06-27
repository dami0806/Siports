package com.bor2h.siportstest

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.bor2h.siportstest.databinding.ActivityHomeBinding
import com.bor2h.siportstest.databinding.FragmentHomeBinding
import kotlinx.coroutines.*
import org.jsoup.HttpStatusException
import org.jsoup.Jsoup
import org.jsoup.select.Elements

/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : Fragment() {

    lateinit var weatherAdapter: WeatherAdapter
    // RecyclerView에 삽입할 ItemList
    val datas = mutableListOf<WeatherData>()

    // 뷰 바인딩
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        // 뷰 바인딩
        _binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        val view = binding.root

        CoroutineScope(Dispatchers.Main).launch {
            Log.d("Scheduler", "Dispatchers.Main Start")
            Log.d("Coroutine", "Dispatchers.Main Start")
            val crawlWeatherData = async { crawlWeatherData() }
            crawlWeatherData.await()
            initRecycler()
        }

        return binding.root
    }

    private suspend fun initRecycler() {
        Log.d("Scheduler", "initRecycler Start")
        weatherAdapter = WeatherAdapter(requireActivity())
        binding.rvWeather.adapter = weatherAdapter

        datas.apply {

            weatherAdapter.datas = datas
            weatherAdapter.notifyDataSetChanged()
        }
        Log.d("Scheduler", "initRecycler Finish")
    }

    // 프래그먼트에서 뷰 바인딩으로 인한 메모리 누수 방지
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private suspend fun crawlWeatherData() = withContext(Dispatchers.IO) {
        Log.d("Scheduler", "crawlWeatherData Start")
        Log.d("Crawl", "Start")
        var itemList: ArrayList<WeatherData> = arrayListOf()

        try {
            // 사이트에 접속해서 HTML 문서 가져오기
            val document =
                Jsoup.connect("https://m.search.naver.com/search.naver?sm=mtp_hty.top&where=m&query=%EC%8B%9C%ED%9D%A5+%EB%82%A0%EC%94%A8").get()
            Log.d("CrawlingHTMLDocument", document.text())

            // HTML 파싱해서 데이터 추출하기
            // .week_list 하위 li 태그 추출
            val elements: Elements = document.select("ul.week_list li")
            Log.d("CrawlingHTMLWeekList", document.select("ul.week_list li").text())

            // 각 element 처리
            var NumOfWeatherData = 0;
            for (element in elements) {
                var day = element.select("strong.day").text()
                var date = element.select("span.date").text()
                var rainfallAm = element.select("span.rainfall")[0].text()
                var rainfallPm = element.select("span.rainfall")[1].text()
                var lowTemperature = element.select("span.lowest").text().substring(4)
                var highTemperature = element.select("span.highest").text().substring(4)

                // 로그 확인을 위한 WeatherData 생성
                var weatherData = WeatherData(
                    day,
                    date,
                    rainfallAm,
                    rainfallPm,
                    lowTemperature = lowTemperature,
                    highTemperature = highTemperature
                )
                Log.d("CrawlingWeatherDataOfDay", weatherData.toString())

                // 강수량에 따라 이미지 변경 20 이하 맑음 / 30 이상 50 이하 / 구름 60 이상 비
                var rainfallAmInt = rainfallAm.substring(0, rainfallAm.length - 1).toInt()
                var rainfallPmInt = rainfallPm.substring(0, rainfallPm.length - 1).toInt()
                var imageAm: Int = R.drawable.ic_map
                var imagePm: Int = R.drawable.ic_map

                when (rainfallAmInt) {
                    in 0..20 -> imageAm = R.drawable.ic_sunny
                    in 21..50 -> imageAm = R.drawable.ic_cloudy
                    in 51..100 -> imageAm = R.drawable.ic_rainy
                }

                when (rainfallPmInt) {
                    in 0..20 -> imagePm = R.drawable.ic_sunny
                    in 21..50 -> imagePm = R.drawable.ic_cloudy
                    in 51..100 -> imagePm = R.drawable.ic_rainy
                }

                // Crawling 결과로 생성된 WeatherData 저장
                datas.add(WeatherData(
                    day,
                    date,
                    rainfallAm,
                    rainfallPm,
                    imageAm,
                    imagePm,
                    lowTemperature = lowTemperature,
                    highTemperature = highTemperature
                ))

                // Crawling 결과로 생성된 WeatherData 수
                NumOfWeatherData++;
                Log.d("CrawlingWeatherDataOfDay", "NumOfWeatherData: " + NumOfWeatherData.toString())
            }
        }
        // status 오류
        catch (httpStatusException: HttpStatusException) {
            Log.e("Crawling", httpStatusException.message.toString())
            httpStatusException.printStackTrace()
        }
        // 기타 오류
        catch (exception: Exception) {
            Log.e("Crawling", exception.message.toString())
            exception.printStackTrace()
        }

        Log.d("Coroutine", "Dispatchers.IO End")
        Log.d("Crawl", "End")
        Log.d("Scheduler", "crawlWeatherData Finish")
    }
}