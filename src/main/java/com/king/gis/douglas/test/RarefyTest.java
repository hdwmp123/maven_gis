package com.king.gis.douglas.test;

import java.util.List;

import com.king.gis.douglas.Point;
import com.king.gis.douglas.Rarefy;
import com.king.gis.util.ReadTrack;

public class RarefyTest {
	public static void main(String[] args) {
		String sql = "select a.* from sc_mnt_trip a,(select trip_id,max(length(b.track)) from sc_mnt_trip b group by trip_id order by max(length(b.track)) desc limit 2,1) b where a.trip_id = b.trip_id";
		List<List<Point>> trips = ReadTrack.readTrack(sql);
		if (trips == null || trips.size() == 0) {
			return;
		}
		List<Point> trip = trips.get(0);
		List<Point> newTrip = Rarefy.tripRarefy(trip);
		newTrip.clear();
	}

}
