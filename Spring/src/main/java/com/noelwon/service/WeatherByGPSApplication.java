package com.noelwon.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class WeatherByGPSApplication {
	Logger logger = LoggerFactory.getLogger("service.BufferedReader");
	Map<String, String> map = new HashMap<String, String>() {
		{
			put("clear sky", "0");
			put("few clouds", "0");
			put("scattered clouds", "0");
			put("broken clouds", "0");
			put("overcast clouds", "0");
			put("mist", "0");
			
			put("shower rain", "1");
			put("light rain", "1");
			put("moderate rain", "1");
			put("Rain", "1");
			put("Thunderstorm", "1");
			put("snow", "1");
			
		}
	};

	public Map<String, String[]> JSONObjectToMap(JSONObject jsonObj) {

		Map<String, String[]> map = new HashMap<>();
		JSONArray list = (JSONArray) jsonObj.get("list");
		JSONObject obj = null;
		String[] str = new String[2];
		for (int i = 0; i < list.size(); i++) {
			obj = (JSONObject) list.get(i);
			str = obj.get("dt_txt").toString().split(" ");
			if (str[1].equals("12:00:00")) {
				JSONObject ob = null;
				JSONArray oblist = (JSONArray) obj.get("weather");
				ob = (JSONObject) oblist.get(0);
				String img = "http://openweathermap.org/img/wn/"
						+ ob.get("icon").toString().replace("d", "").replace("n", "") + "d.png";

				String[] arr = { this.map.get(ob.get("description")), img };
				map.put(str[0], arr);

			}
		}

		return map;
	}

	public Map<String, String[]> getWeather(String lon, String lat) {
		try {
			// OpenAPI call하는 URL
			String urlstr = "http://api.openweathermap.org/data/2.5/forecast?" + "lat=" + lat + "&lon=" + lon
					+ "&appid=your_OpenWeatherMap_Api_Key";
			URL url = new URL(urlstr);
			BufferedReader bf;
			String line;
			String result = "";
			// 날씨 정보를 받아온다.
			bf = new BufferedReader(new InputStreamReader(url.openStream()));
			// 버퍼에 있는 정보를 문자열로 변환.
			while ((line = bf.readLine()) != null) {
				result = result.concat(line);
			}
			// 문자열을 JSON으로 파싱
			JSONParser jsonParser = new JSONParser();
			JSONObject jsonObj = (JSONObject) jsonParser.parse(result);
			Map<String, String[]> maps = JSONObjectToMap(jsonObj);

			bf.close();
			return maps;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return null;
		}

	}
}
