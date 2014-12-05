package com.king.gis.test;

import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;

import com.king.gis.douglas.Point;
import com.king.gis.douglas.Rarefy;
import com.king.gis.ext.RoadExt;
import com.king.gis.match.RoadMatch;
import com.king.gis.util.Write2File;
import com.king.gis.util.data.Data;
import com.king.gis.util.data.StaticData;

public class KingTest {
	static Data data = new StaticData();

	public static void main(String[] args) {
		JsonConfig config = new JsonConfig();
		config.setExcludes(new String[] { "z", });
		String path = "D:/Project/myself_proX64/maven_gis/maven.1417677787248/trunk/src/main/webapp/data/%s";
		// 原始道路
		List<Point> source = data.getTrip(null);
		Write2File.write2File(String.format(path, "polyline-source.json"),
				JSONArray.fromObject(source, config).toString());
		// 抽稀
		List<Point> source2rarefy = Rarefy.tripRarefy(source);
		Write2File.write2File(String
				.format(path, "polyline-source2rarefy.json"), JSONArray
				.fromObject(source2rarefy, config).toString());
		// 拐角匹配
		List<Point> rarefy2match = RoadMatch.roadMatch(source2rarefy);
		Write2File.write2File(
				String.format(path, "polyline-rarefy2match.json"), JSONArray
						.fromObject(rarefy2match, config).toString());
		// 匹配后抽稀
		List<Point> match2rarefy = Rarefy.tripRarefy(rarefy2match);
		Write2File.write2File(
				String.format(path, "polyline-match2rarefy.json"), JSONArray
						.fromObject(match2rarefy, config).toString());
		// 道路扩展
		List<com.king.gis.ext.Point> roadExt = null;
		roadExt = RoadExt.ext(Data.convert(source));
		Write2File.write2File(String.format(path, "polyline-road-ext.json"),
				JSONArray.fromObject(roadExt, config).toString());
	}
}
