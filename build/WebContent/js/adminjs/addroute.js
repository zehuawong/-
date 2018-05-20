//添加新车按钮
$(document).ready(function() {
	$("#add_route_btn_ID").click(function() { // 添加一行input
		var newrow = "<tr>";
		for (var i = 0; i < 4; i++) {
			newrow += "<td><input/></td>";
		}
		newrow += "<td><button class='savenewR_btn save_btn'>保存</button></td>";
		newrow += "<td><button class='deletenewR_btn delete_btn'>删除</button></td>";
		newrow += "</tr>"
		$("#addroute_table_tbody_ID").append(newrow);
		addInputListener(); // 输入框焦点事件
		saveNewRListener(); // 给保存新线路按钮添加事件监听
		deleteNewRListener();// 给删除新线路按钮添加事件监听
	});

});
  

// 处理管理员点击的保存按钮（开通新线路）
function saveNewRListener() {
	$(".savenewR_btn").click(function() {
		// 首先需要获得
		var tr = $(this).parent().parent();
		addRoute(tr);

	});
}
function editNewRListener()
{
	$(".editnewR_btn").click(function() {
		// alert("从保存按钮切换到了编辑按钮")
		var routeID=$(this).val();
		var tr = $(this).parent().parent();
		for (var i = 0; i < 4; i++) {
			var td = tr.children("td").get(i);
			var tdtext = td.innerHTML;
			td.innerHTML = "<input value='" + tdtext + "'/>";
		}
		addInputListener();
		toggleToSaveNewR(tr, routeID);
	});

}

// 给删除新线路按钮添加事件监听
function deleteNewRListener()
{
	$(".deletenewR_btn").click(function() {
		// 首先需要获得线路号
		var tr = $(this).parent().parent();
		var routeID=tr.children("td").children("button").get(0).value;
		if(routeID==null||routeID==""){
			tr.remove();
			return;
		}
		tr.remove();		
		stopR(routeID); //停运 新开通的线路
	});
	
}



function toggleToEditNewR(tr, routeID) {
	tr.children("td").get(4).innerHTML = "<button class='editnewR_btn edit_btn' value='"
			+ routeID + "'>编辑</button>";
	for (var i = 0; i < 4; i++) {
		tr.children("td").get(i).innerHTML = tr.children("td").children()
				.get(0).value;
	}
	editNewRListener();
		
}


function toggleToSaveNewR(tr, routeID)
{
	tr.children("td").get(4).innerHTML="<button class='savenewR_btn save_btn' value='"+routeID+"'>保存</button>";	
	saveNewRListener();
}



//添加新线路
function addRoute(tr) {

	var routeInfo = new Array(4);
	var tdinput;
	var flag = true;
	for (var i = 0; i < 4; i++) {
		tdinput = tr.children("td").children().get(i);
		routeInfo[i] = tdinput.value;
		if (routeInfo[i] == null || routeInfo[i] == "") {
			tdinput.style.border = "2px solid #ff0000";
			flag = false;
		}
	}
	// 验证表单，是否为空
	if (flag == false)
	return false;
	
	var routeID=tr.children("td").children("button").get(0).value;
	
	if(routeID!=null&&routeID!=""){  //如果已经添加成功并且获取了routeID，则跳转到更新
		updateNewRoute(tr,routeInfo,routeID) ;
		return;		
	}
	
	$.ajax({
		dataType : "json", //数据类型为json格式
		contentType : "application/x-www-form-urlencoded; charset=utf-8",
		type : "POST",
		url : "../addrouteservlet",
		data :{startcity:routeInfo[0],startpoint:routeInfo[1],
			endcity:routeInfo[2],destination:routeInfo[3]},
		statusCode : {
			404 : function() {
				alert('page not found');
			}
		},
		success : function(data, status) {	
			if (data.addR_result == "true") { //添加成功			
				tr.children("td").children("button").get(0).value=data.routeID;
				toggleToEditNewR(tr, data.routeID);
				
			}else {
				var txt="添加失败，请检查线路是否重复";
				window.wxc.xcConfirm(txt, window.wxc.xcConfirm.typeEnum.error);
			}
									
		}										
		
	}); //$.ajax结束
	
	
	 
}



//更新线路，如果已经保存添加成功后，再次点击编辑-保存，
function updateNewRoute(tr,routeInfo,routeID)  
{
	
	$.ajax({
		dataType : "json", //数据类型为json格式
		contentType : "application/x-www-form-urlencoded; charset=utf-8",
		type : "POST",
		url : "../updatenewrouteservlet",
		data :{routeID:routeID,startcity:routeInfo[0],startpoint:routeInfo[1],
			endcity:routeInfo[2],destination:routeInfo[3]},
		statusCode : {
			404 : function() {
				alert('page not found');
			}
		},
		success : function(data, status) {	
			if (data.updateNR_result == "true") { //添加成功			
				tr.children("td").children("button").get(0).value=data.routeID; 
				toggleToEditNewR(tr, routeID);
				
			}else {
				var txt="添加失败，请检查线路是否重复";
				window.wxc.xcConfirm(txt, window.wxc.xcConfirm.typeEnum.error);
			}
									
		}										
		
	}); //$.ajax结束
	
	
	
}

