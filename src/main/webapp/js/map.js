var getRequest = function (){
   var url = location.search; //获取url中"?"符后的字串
   var theRequest = new Object();
   if (url.indexOf("?") != -1) {
      var str = url.substr(1);
      strs = str.split("&");
      for(var i = 0; i < strs.length; i ++) {
         theRequest[strs[i].split("=")[0]]=unescape(strs[i].split("=")[1]);
      }
   }
   return theRequest;
}
var _mapInit = function (arr){
	var arrBus = [];
	for (var i = 0; i < arr.length; i++) {
		var point = arr[i];
		if(!point.hasOwnProperty("longitude")){
			point["longitude"] = point.x;
		}
		if(!point.hasOwnProperty("latitude")){
			point["latitude"] = point.y;
		}
		arrBus.push(new AMap.LngLat(point.longitude,point.latitude));
	};
	var opt = {
		zooms: [3,18], //设置地图缩放级别
		center: arrBus[0], //设置地图中心点
		doubleClickZoom: true, //双击放大地图
		scrollWheel: true //鼠标滚轮缩放地图
	}
	var mapObj = new AMap.Map("iCenter", opt);
	
	//画线
	var polyline = new AMap.Polyline({
		id : "polyline01",
		path : arrBus, //线经纬度数组
		strokeColor : "#E35850", //线颜色
		strokeOpacity : 0.8, //线透明度
		strokeWeight : 3  //线宽
	});
	polyline.setMap(mapObj);
	//画点
	/*
	var marker = null;
	for(var i=0;i<arrBus.length; i++){
		marker = new AMap.Marker({	
			map:mapObj,
			position:arrBus[i],
			content:i+"",
			offset:new AMap.Pixel(0,0) //相对于基点的偏移位置
		});		
	}
	*/
	//
	mapObj.setFitView();
}
var mapInit = function(url){
	$.get(url,function(data){
		var arr = data;
		console.log(typeof(arr));
		if(typeof(arr)=='string'){
			arr = eval('('+arr+')');
		}
		console.log(typeof(arr));
		$("#pointCount").html(arr.length);
		_mapInit(arr);
	});
}

	