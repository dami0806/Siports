package com.bor2h.siportstest

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import net.daum.mf.map.api.CalloutBalloonAdapter
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView
import kotlin.system.exitProcess


class MapActivity : AppCompatActivity() {
    private val PERMISSIONS_REQUEST_CODE = 100
    private var REQUIRED_PERMISSIONS = arrayOf<String>(Manifest.permission.ACCESS_FINE_LOCATION)
    private val eventListener = MarkerEventListener(this) // 마커 클릭 이벤트 리스너

    private var isMlMarkerExist: Boolean = false // 현재위치 마커가 있는지 확인하기 위한 변수

    private lateinit var mapPageLocationBtn: ImageButton // 현재위치 추적 버튼
    private lateinit var soccerBtn: Button
    private lateinit var baseballBtn: Button
    private lateinit var badmintonBtn: Button
    private lateinit var lifesportsBtn: Button
    private lateinit var futsalBtn: Button
    private lateinit var resetBtn: Button


    // 각 마커의 시설 이름 배열
    private val arrayMarkerName = arrayOf(
        "포동클레이구장",
        "천연잔디구장 - 희망공원",
        "MTV인조잔디구장",
        "정왕육상경기장",
        "신천동인조잔디축구장",
        "희망공원인조잔디축구장",
        "옥구인조잔디축구장",
        "포동인조잔디축구장",
        "월곶에코피아야구장",
        "전용배드민턴장",
        "시흥어울림국민체육센터",
        "시흥시체육관(공공스포츠클럽)",
        "다목적 운동공간(아름다운요가&짐)",
        "축구클럽(배곧 주니어 축구클럽)",
        "ABC행복학습타운(한마음관)",
        "월곶동생활체육시설",
        "스포츠박스",
        "함송 풋살장",
        "달월 풋살장",
        "은계 풋살장",
        "신천 B동풋살장",
        "매화동 풋살장",
        "배곧풋살장",
        "MTV 풋살장",
        "중앙공원 풋살장",
        "거모동 풋살장",
        "산들공원 풋살장",
        "세미공원 풋살장",
        "조남동 풋살장",
        "과림동 풋살장",
        "신천 A동풋살장",
        "에코피아 풋살장",
        "신현풋살장"
    )

    //각 마커의 주소 배열
    private val arrayMarkerAddress = arrayOf(
        "경기도 시흥시 신현로 55",
        "경기도 시흥시 군자천로131번길 64",
        "경기도 시흥시 엠티브이북로 253",
        "경기도 시흥시 역전로 2",
        "경기도 시흥시 신천동 525",
        "경기도 시흥시 군자천로131번길 64",
        "경기도 시흥시 서해안로 277",
        "경기도 시흥시 신현로 55",
        "경기도 시흥시 서해안로 900",
        "경기도 시흥시 역전로 50",
        "경기도 시흥시 정왕대로233번길 33",
        "경기도 시흥시 서해안로 1589",
        "경기도 시흥시 군자로481번길 22",
        "경기도 시흥시 배곧4로 22",
        "경기도 시흥시 소래산길 11",
        "경기도 시흥시 월곶중앙로14번길 64",
        "경기도 시흥시 역전로 228",
        "경기도 시흥시 함송로13번길 17",
        "경기도 시흥시 월곶동 942-30",
        "경기도 시흥시 은행고길 30",
        "경기도 시흥시 신천동518-5",
        "경기도 시흥시 매화동 205-3",
        "경기도 시흥시 배곧4로 32-5",
        "경기도 시흥시 정왕동 2609-6",
        "경기도 시흥시 정왕대로 191",
        "경기도 시흥시 군자로 534번길19",
        "경기도 시흥시 거모동 1351",
        "경기도 시흥시 조남동 705",
        "경기도 시흥시 동서로 989-67",
        "경기도 시흥시 금오로 419-25",
        "경기도 시흥시 신천동518-5",
        "경기도 시흥시 서해안로 900",
        "경기도 시흥시 신현로 55"
    )

    // 각 마커의 예약사이트 연결 링크 배열
    private val arrayMarkerUrl = arrayOf(
        "https://share.siheung.go.kr/space/view.do?searchCategory=3&searchDetailCategory=35&searchCondition=title&pageIndex=1&key=201000&use_date=&space_no=340&searchPositonDong=&searchReserve=&searchStartTime=&searchEndTime=&searchStartDate=&searchEndDate=&searchKeyword=",
        "https://share.siheung.go.kr/space/view.do?searchCategory=3&searchDetailCategory=35&searchCondition=title&pageIndex=1&key=201000&use_date=&space_no=291&searchPositonDong=&searchReserve=&searchStartTime=&searchEndTime=&searchStartDate=&searchEndDate=&searchKeyword=",
        "https://share.siheung.go.kr/space/view.do?searchCategory=3&searchDetailCategory=35&searchCondition=title&pageIndex=1&key=201000&use_date=&space_no=286&searchPositonDong=&searchReserve=&searchStartTime=&searchEndTime=&searchStartDate=&searchEndDate=&searchKeyword=",
        "https://share.siheung.go.kr/space/view.do?searchCategory=3&searchDetailCategory=35&searchCondition=title&pageIndex=1&key=201000&use_date=&space_no=285&searchPositonDong=&searchReserve=&searchStartTime=&searchEndTime=&searchStartDate=&searchEndDate=&searchKeyword=",
        "https://share.siheung.go.kr/space/view.do?searchCategory=3&searchDetailCategory=35&searchCondition=title&pageIndex=1&key=201000&use_date=&space_no=284&searchPositonDong=&searchReserve=&searchStartTime=&searchEndTime=&searchStartDate=&searchEndDate=&searchKeyword=",
        "https://share.siheung.go.kr/space/view.do?searchCategory=3&searchDetailCategory=35&searchCondition=title&pageIndex=1&key=201000&use_date=&space_no=283&searchPositonDong=&searchReserve=&searchStartTime=&searchEndTime=&searchStartDate=&searchEndDate=&searchKeyword=",
        "https://share.siheung.go.kr/space/view.do?searchCategory=3&searchDetailCategory=35&searchCondition=title&pageIndex=2&key=201000&use_date=&space_no=282&searchPositonDong=&searchReserve=&searchStartTime=&searchEndTime=&searchStartDate=&searchEndDate=&searchKeyword=",
        "https://share.siheung.go.kr/space/view.do?searchCategory=3&searchDetailCategory=35&searchCondition=title&pageIndex=2&key=201000&use_date=&space_no=281&searchPositonDong=&searchReserve=&searchStartTime=&searchEndTime=&searchStartDate=&searchEndDate=&searchKeyword=",
        "https://share.siheung.go.kr/space/view.do?searchCategory=3&searchDetailCategory=18&searchCondition=title&pageIndex=1&key=202000&use_date=&space_no=289&searchPositonDong=&searchReserve=&searchStartTime=&searchEndTime=&searchStartDate=&searchEndDate=&searchKeyword=",
        "https://share.siheung.go.kr/space/view.do?searchCategory=3&searchDetailCategory=19&searchCondition=title&pageIndex=1&key=203000&use_date=&space_no=62&searchPositonDong=&searchReserve=&searchStartTime=&searchEndTime=&searchStartDate=&searchEndDate=&searchKeyword=",
        "https://share.siheung.go.kr/space/view.do?searchCategory=3&searchDetailCategory=22&searchCondition=title&pageIndex=1&key=204000&use_date=&space_no=1335&searchPositonDong=&searchReserve=&searchStartTime=&searchEndTime=&searchStartDate=&searchEndDate=&searchKeyword=",
        "https://share.siheung.go.kr/space/view.do?searchCategory=3&searchDetailCategory=22&searchCondition=title&pageIndex=1&key=204000&use_date=&space_no=1243&searchPositonDong=&searchReserve=&searchStartTime=&searchEndTime=&searchStartDate=&searchEndDate=&searchKeyword=",
        "https://share.siheung.go.kr/space/view.do?searchCategory=3&searchDetailCategory=22&searchCondition=title&pageIndex=1&key=204000&use_date=&space_no=314&searchPositonDong=&searchReserve=&searchStartTime=&searchEndTime=&searchStartDate=&searchEndDate=&searchKeyword=",
        "https://share.siheung.go.kr/space/view.do?searchCategory=3&searchDetailCategory=22&searchCondition=title&pageIndex=1&key=204000&use_date=&space_no=312&searchPositonDong=&searchReserve=&searchStartTime=&searchEndTime=&searchStartDate=&searchEndDate=&searchKeyword=",
        "https://share.siheung.go.kr/space/view.do?searchCategory=3&searchDetailCategory=22&searchCondition=title&pageIndex=1&key=204000&use_date=&space_no=294&searchPositonDong=&searchReserve=&searchStartTime=&searchEndTime=&searchStartDate=&searchEndDate=&searchKeyword=",
        "https://share.siheung.go.kr/space/view.do?searchCategory=3&searchDetailCategory=22&searchCondition=title&pageIndex=1&key=204000&use_date=&space_no=63&searchPositonDong=&searchReserve=&searchStartTime=&searchEndTime=&searchStartDate=&searchEndDate=&searchKeyword=",
        "https://share.siheung.go.kr/space/view.do?searchCategory=3&searchDetailCategory=22&searchCondition=title&pageIndex=2&key=204000&use_date=&space_no=58&searchPositonDong=&searchReserve=&searchStartTime=&searchEndTime=&searchStartDate=&searchEndDate=&searchKeyword=",
        "https://share.siheung.go.kr/space/view.do?searchCategory=3&searchDetailCategory=38&searchCondition=title&pageIndex=1&key=206000&use_date=&space_no=1248&searchPositonDong=&searchReserve=&searchStartTime=&searchEndTime=&searchStartDate=&searchEndDate=&searchKeyword=",
        "https://share.siheung.go.kr/space/view.do?searchCategory=3&searchDetailCategory=38&searchCondition=title&pageIndex=1&key=206000&use_date=&space_no=1227&searchPositonDong=&searchReserve=&searchStartTime=&searchEndTime=&searchStartDate=&searchEndDate=&searchKeyword=",
        "https://share.siheung.go.kr/space/view.do?searchCategory=3&searchDetailCategory=38&searchCondition=title&pageIndex=1&key=206000&use_date=&space_no=1226&searchPositonDong=&searchReserve=&searchStartTime=&searchEndTime=&searchStartDate=&searchEndDate=&searchKeyword=",
        "https://share.siheung.go.kr/space/view.do?searchCategory=3&searchDetailCategory=38&searchCondition=title&pageIndex=1&key=206000&use_date=&space_no=345&searchPositonDong=&searchReserve=&searchStartTime=&searchEndTime=&searchStartDate=&searchEndDate=&searchKeyword=",
        "https://share.siheung.go.kr/space/view.do?searchCategory=3&searchDetailCategory=38&searchCondition=title&pageIndex=1&key=206000&use_date=&space_no=338&searchPositonDong=&searchReserve=&searchStartTime=&searchEndTime=&searchStartDate=&searchEndDate=&searchKeyword=",
        "https://share.siheung.go.kr/space/view.do?searchCategory=3&searchDetailCategory=38&searchCondition=title&pageIndex=1&key=206000&use_date=&space_no=337&searchPositonDong=&searchReserve=&searchStartTime=&searchEndTime=&searchStartDate=&searchEndDate=&searchKeyword=",
        "https://share.siheung.go.kr/space/view.do?searchCategory=3&searchDetailCategory=38&searchCondition=title&pageIndex=2&key=206000&use_date=&space_no=336&searchPositonDong=&searchReserve=&searchStartTime=&searchEndTime=&searchStartDate=&searchEndDate=&searchKeyword=",
        "https://share.siheung.go.kr/space/view.do?searchCategory=3&searchDetailCategory=38&searchCondition=title&pageIndex=2&key=206000&use_date=&space_no=335&searchPositonDong=&searchReserve=&searchStartTime=&searchEndTime=&searchStartDate=&searchEndDate=&searchKeyword=",
        "https://share.siheung.go.kr/space/view.do?searchCategory=3&searchDetailCategory=38&searchCondition=title&pageIndex=2&key=206000&use_date=&space_no=334&searchPositonDong=&searchReserve=&searchStartTime=&searchEndTime=&searchStartDate=&searchEndDate=&searchKeyword=",
        "https://share.siheung.go.kr/space/view.do?searchCategory=3&searchDetailCategory=38&searchCondition=title&pageIndex=2&key=206000&use_date=&space_no=333&searchPositonDong=&searchReserve=&searchStartTime=&searchEndTime=&searchStartDate=&searchEndDate=&searchKeyword=",
        "https://share.siheung.go.kr/space/view.do?searchCategory=3&searchDetailCategory=38&searchCondition=title&pageIndex=2&key=206000&use_date=&space_no=332&searchPositonDong=&searchReserve=&searchStartTime=&searchEndTime=&searchStartDate=&searchEndDate=&searchKeyword=",
        "https://share.siheung.go.kr/space/view.do?searchCategory=3&searchDetailCategory=38&searchCondition=title&pageIndex=2&key=206000&use_date=&space_no=331&searchPositonDong=&searchReserve=&searchStartTime=&searchEndTime=&searchStartDate=&searchEndDate=&searchKeyword=",
        "https://share.siheung.go.kr/space/view.do?searchCategory=3&searchDetailCategory=38&searchCondition=title&pageIndex=3&key=206000&use_date=&space_no=330&searchPositonDong=&searchReserve=&searchStartTime=&searchEndTime=&searchStartDate=&searchEndDate=&searchKeyword=",
        "https://share.siheung.go.kr/space/view.do?searchCategory=3&searchDetailCategory=38&searchCondition=title&pageIndex=3&key=206000&use_date=&space_no=311&searchPositonDong=&searchReserve=&searchStartTime=&searchEndTime=&searchStartDate=&searchEndDate=&searchKeyword=",
        "https://share.siheung.go.kr/space/view.do?searchCategory=3&searchDetailCategory=38&searchCondition=title&pageIndex=3&key=206000&use_date=&space_no=288&searchPositonDong=&searchReserve=&searchStartTime=&searchEndTime=&searchStartDate=&searchEndDate=&searchKeyword=",
        "https://share.siheung.go.kr/space/view.do?searchCategory=3&searchDetailCategory=38&searchCondition=title&pageIndex=3&key=206000&use_date=&space_no=287&searchPositonDong=&searchReserve=&searchStartTime=&searchEndTime=&searchStartDate=&searchEndDate=&searchKeyword="
    )

    // 각 마커의 위도 배열
    private val arrayMarkerLatitude = arrayOf(
        37.404177910922506,
        37.33706050066034,
        37.3181968420809,
        37.33843130351412,
        37.43543878494933,
        37.33754492215164,
        37.35684123901512,
        37.40392284903578,
        37.39533094800151,
        37.339615892709055,
        37.34836440559527,
        37.44771789787663,
        37.346939758548146,
        37.37756231995029,
        37.449318649595554,
        37.39240178965839,
        37.34933057512744,
        37.365121533161236,
        37.383140478612475,
        37.44089374595078,
        37.435245459417594,
        37.417017739179606,
        37.376494310400126,
        37.32077524390766,
        37.34867648916175,
        37.34139565996338,
        37.3467761143123,
        37.37743786002161,
        37.38458911010815,
        37.439408621887154,
        37.4351193396478,
        37.39607231697744,
        37.40349207962988
    )

    // 각 마커의 경도 배열
    private val arrayMarkerLongitude = arrayOf(
        126.78448044891276,
        126.71424605945997,
        126.70572652803462,
        126.7560561752189,
        126.77724408688442,
        126.71336403153892,
        126.7155763693579,
        126.78419317006582,
        126.75278320340425,
        126.7570228403812,
        126.74088084984024,
        126.78483872883922,
        126.78413064594854,
        126.7269580151469,
        126.78563936236719,
        126.74184732627155,
        126.74599022036767,
        126.730928730418,
        126.76172929114547,
        126.7995115982832,
        126.77507239072463,
        126.81383986064002,
        126.72780584102932,
        126.70904748950866,
        126.7363399055828,
        126.78632454297698,
        126.78088641253312,
        126.85738083530909,
        126.85138556866617,
        126.83074544420266,
        126.77508406731603,
        126.75077056092645,
        126.78513748963861
    )

    // 각 마커의 운동종목
    private val arrayMarkerSportsType = arrayOf(
        "축구",
        "축구",
        "축구",
        "축구",
        "축구",
        "축구",
        "축구",
        "축구",
        "야구",
        "배드민턴",
        "생활체육시설",
        "생활체육시설",
        "생활체육시설",
        "생활체육시설",
        "생활체육시설",
        "생활체육시설",
        "생활체육시설",
        "풋살",
        "풋살",
        "풋살",
        "풋살",
        "풋살",
        "풋살",
        "풋살",
        "풋살",
        "풋살",
        "풋살",
        "풋살",
        "풋살",
        "풋살",
        "풋살",
        "풋살",
        "풋살"
    )

    // 각 마커와 내 위치와의 거리(미터)
    private val distBtwMakerMl = Array(arrayMarkerAddress.size) { 0.0 }

    // 지도에 찍히는 마커 객체 배열
    private val marker = Array<MapPOIItem?>(arrayMarkerAddress.size) { null }

    // 내위치 위도 경도
    var mlLatitude: Double = 0.0
    var mlLongitude: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_map)

        val mapView = MapView(this)

        val mapViewContainer = findViewById<View>(R.id.map_view) as ViewGroup
        mapViewContainer.addView(mapView) // 맵 띄우기

        mapPageLocationBtn = findViewById<ImageButton>(R.id.mapPageLocationBtn)
        soccerBtn = findViewById<Button>(R.id.soccerBtn)
        baseballBtn = findViewById<Button>(R.id.baseballBtn)
        badmintonBtn = findViewById<Button>(R.id.badmintonBtn)
        lifesportsBtn = findViewById<Button>(R.id.lifesportsBtn)
        futsalBtn = findViewById<Button>(R.id.futsalBtn)
        resetBtn = findViewById<Button>(R.id.resetBtn)

        mapView.setCalloutBalloonAdapter(CustomBalloonAdapter(layoutInflater)) // 커스텀 말풍선 등록
        mapView.setPOIItemEventListener(eventListener)  // 마커 클릭 이벤트 리스너 등록

        // 중심점 설정(시흥시청) 및 시흥시가 한 화면안에 들어올 정도의 초기 줌레벨 설정
        mapView.setMapCenterPointAndZoomLevel(
            MapPoint.mapPointWithGeoCoord(37.38011461827379, 126.80297610398351), 7, true
        )

        // 마커 추가 및 마커 정보 추가 후 지도에 표시
        val mlMarker = MapPOIItem() // 현재 위치 마커
        for (i in arrayMarkerAddress.indices) {
            marker[i] = MapPOIItem()
            marker[i]?.itemName = arrayMarkerName[i]
            marker[i]?.tag = i
            marker[i]?.mapPoint = MapPoint.mapPointWithGeoCoord(
                arrayMarkerLatitude[marker[i]?.tag!!],
                arrayMarkerLongitude[marker[i]?.tag!!]
            )
            marker[i]?.markerType = MapPOIItem.MarkerType.BluePin // 기본으로 제공하는 BluePin 마커 모양.
            marker[i]?.selectedMarkerType =
                MapPOIItem.MarkerType.RedPin // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.
            mapView.addPOIItem(marker[i])
        }

        // 버튼 눌렀을때 현재 위치 추적
        mapPageLocationBtn.setOnClickListener {
            val permissionCheck =
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                val lm: LocationManager =
                    getSystemService(Context.LOCATION_SERVICE) as LocationManager
                try {
                    val userNowLocation: Location =
                        lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)!!
                    val uLatitude = userNowLocation.latitude
                    val uLongitude = userNowLocation.longitude
                    mlLatitude = uLatitude
                    mlLongitude = uLongitude
                    val uNowPosition = MapPoint.mapPointWithGeoCoord(uLatitude, uLongitude)
                    // 현재위치에 마커 찍기
                    mapView.setMapCenterPoint(uNowPosition, true)
                    if (isMlMarkerExist) {
                        mapView.removePOIItem(mlMarker)
                    }
                    mlMarker.itemName = "현재 위치"
                    mlMarker.tag = -1
                    mlMarker.mapPoint = MapPoint.mapPointWithGeoCoord(uLatitude, uLongitude)
                    mlMarker.markerType =
                        MapPOIItem.MarkerType.YellowPin // 기본으로 제공하는 BluePin 마커 모양.
                    mlMarker.selectedMarkerType =
                        MapPOIItem.MarkerType.RedPin // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.
                    mapView.addPOIItem(mlMarker)
                    isMlMarkerExist = true

                    // 현재 위치와 각 마커와의 거리 전부 저장
                    for (i in distBtwMakerMl.indices) {
                        distBtwMakerMl[i] = distanceByDegreeAndroid(
                            mlLatitude,
                            mlLongitude,
                            arrayMarkerLatitude[i],
                            arrayMarkerLongitude[i]
                        )
                    }

                } catch (e: NullPointerException) {
                    Log.e("LOCATION_ERROR", e.toString())
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        ActivityCompat.finishAffinity(this)
                    } else {
                        ActivityCompat.finishAffinity(this)
                    }

                    val intent = Intent(this, MapActivity::class.java)
                    startActivity(intent)
                    exitProcess(0)
                }
            } else {
                Toast.makeText(this, "위치 권한이 없습니다.", Toast.LENGTH_SHORT).show()
                ActivityCompat.requestPermissions(
                    this,
                    REQUIRED_PERMISSIONS,
                    PERMISSIONS_REQUEST_CODE
                )
            }
        }

        soccerBtn.setOnClickListener {
            for (i in arrayMarkerAddress.indices) {
                mapView.removePOIItem(marker[i])
                if (arrayMarkerSportsType[i] == "축구") mapView.addPOIItem(marker[i])
            }
        }

        // 각 운동 종목의 마커만 표시하게 하는 버튼

        baseballBtn.setOnClickListener {
            for (i in arrayMarkerAddress.indices) {
                mapView.removePOIItem(marker[i])
                if (arrayMarkerSportsType[i] == "야구") mapView.addPOIItem(marker[i])
            }
        }

        badmintonBtn.setOnClickListener {
            for (i in arrayMarkerAddress.indices) {
                mapView.removePOIItem(marker[i])
                if (arrayMarkerSportsType[i] == "배드민턴") mapView.addPOIItem(marker[i])
            }
        }

        lifesportsBtn.setOnClickListener {
            for (i in arrayMarkerAddress.indices) {
                mapView.removePOIItem(marker[i])
                if (arrayMarkerSportsType[i] == "생활체육시설") mapView.addPOIItem(marker[i])
            }
        }

        futsalBtn.setOnClickListener {
            for (i in arrayMarkerAddress.indices) {
                mapView.removePOIItem(marker[i])
                if (arrayMarkerSportsType[i] == "풋살") mapView.addPOIItem(marker[i])
            }
        }

        resetBtn.setOnClickListener {
            for (i in arrayMarkerAddress.indices) {
                mapView.removePOIItem(marker[i])
                mapView.addPOIItem(marker[i])
            }
        }
    }

    // 거리 구하는 함수
    private fun distanceByDegreeAndroid(
        _latitude1: Double?,
        _longitude1: Double?,
        _latitude2: Double?,
        _longitude2: Double?
    ): Double {
        val startPos = Location("PointA")
        val endPos = Location("PointB")

        startPos.latitude = _latitude1!!
        startPos.longitude = _longitude1!!
        endPos.latitude = _latitude2!!
        endPos.longitude = _longitude2!!

        return startPos.distanceTo(endPos).toDouble()
    }

    // 커스텀 말풍선 클래스
    inner class CustomBalloonAdapter(inflater: LayoutInflater) : CalloutBalloonAdapter {
        private val mCalloutBalloon: View = inflater.inflate(R.layout.balloon_layout, null)
        private val name: TextView = mCalloutBalloon.findViewById(R.id.ball_tv_name)
        private val dist: TextView = mCalloutBalloon.findViewById(R.id.ball_tv_dist)
        private val address: TextView = mCalloutBalloon.findViewById(R.id.ball_tv_address)
        private var meter: Double = 0.0

        override fun getCalloutBalloon(poiItem: MapPOIItem?): View {
            // 마커 클릭 시 나오는 말풍선
            name.text = poiItem?.itemName   // 해당 마커의 정보 이용 가능
            if (poiItem?.tag == -1) {
                address.text = ""
                dist.text = ""
            } else {
                address.text = arrayMarkerAddress[poiItem?.tag!!]
                meter = distBtwMakerMl[poiItem.tag]
                if (meter >= 1000) {
                    meter = meter / 1000 + (((meter % 1000.0) / 1000))
                    dist.text = "현위치와의 거리: " + String.format("%.1f", meter) + "km"
                } else {
                    dist.text = "현위치와의 거리: " + String.format("%.1f", meter) + "m"
                }
            }
            return mCalloutBalloon
        }

        override fun getPressedCalloutBalloon(poiItem: MapPOIItem?): View {
            // 말풍선 누르고 있을때 나오는 내용
            if (poiItem?.tag == -1) {
                address.text = ""
                dist.text = ""
            } else {
                address.text = arrayMarkerAddress[poiItem?.tag!!]
                meter = distBtwMakerMl[poiItem.tag]
                if (meter >= 1000) {
                    meter = meter / 1000 + (((meter % 1000.0) / 1000))
                    dist.text = "현위치와의 거리: " + String.format("%.1f", meter) + "km"
                } else {
                    dist.text = "현위치와의 거리: " + String.format("%.1f", meter) + "m"
                }
            }
            return mCalloutBalloon
        }
    }

    // 마커 클릭 이벤트 리스너
    inner class MarkerEventListener(val context: Context) : MapView.POIItemEventListener {
        override fun onPOIItemSelected(mapView: MapView?, poiItem: MapPOIItem?) {
            // 마커 클릭 시
        }

        override fun onCalloutBalloonOfPOIItemTouched(mapView: MapView?, poiItem: MapPOIItem?) {
            // 말풍선 클릭 시 (Deprecated)
            // 이 함수도 작동하지만 그냥 아래 있는 함수에 작성하자
        }

        override fun onCalloutBalloonOfPOIItemTouched(
            mapView: MapView?,
            poiItem: MapPOIItem?,
            buttonType: MapPOIItem.CalloutBalloonButtonType?
        ) {
            // 말풍선 클릭 시
            val builder = AlertDialog.Builder(context)
            val itemList = arrayOf("예약사이트로 연결", "취소")
            builder.setTitle("${poiItem?.itemName}")
            builder.setItems(itemList) { dialog, which ->
                when (which) {
                    0 -> {
                        if (poiItem?.tag == -1) { // 현재 위치에서는 예약 사이트 없음
                            Toast.makeText(context, "연결 가능한 예약 사이트가 없습니다", Toast.LENGTH_SHORT)
                                .show()
                        } else { // 선택된 마커의 예약 사이트 연결
                            val intent = Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse(arrayMarkerUrl[poiItem?.tag!!])
                            )
                            startActivity(intent, null)
                        }
                    }
                    1 -> dialog.dismiss()   // 대화상자 닫기
                }
            }
            builder.show()
        }

        override fun onDraggablePOIItemMoved(
            mapView: MapView?,
            poiItem: MapPOIItem?,
            mapPoint: MapPoint?
        ) {
            // 마커의 속성 중 isDraggable = true 일 때 마커를 이동시켰을 경우
        }
    }
}