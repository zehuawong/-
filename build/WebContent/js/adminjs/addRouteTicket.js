
var newRTID; //新班次ID
var routeID_RT; //新班次对应的线路ID
//增加按钮：清空班次表格

function validateRIDForm()
{
	var routeID=$("#addRT_RID_ID").val();	
	if(routeID==null||routeID==""){
		$("#addRT_RID_ID").css("border","2px solid #ff0000");
		return false;
	}
	routeID=parseInt(routeID);
	$("#addRT_R_table_caption_ID").html(routeID+"号线路");
	getRoute(routeID); //ajax
	
	return false;
}


function getRoute(routeID){
	$.ajax({
		dataType : "json", //数据类型为json格式
		contentType : "application/x-www-form-urlencoded; charset=utf-8",
		type : "POST",
		url : "../selectroutebyidservlet",
		data :{routeID:routeID},
		statusCode : {
			404 : function() {
				alert('page not found');
			}
		},
		success : function(data, status) {	
			if (data.selectR_result == "true") { //查找成功	
				routeID_RT=routeID;
				var R_html="";
				R_html+="<tr>"+"<td>"+data.startcity+"</td>"
						+"<td>"+data.startpoint+"</td>"
						+"<td>"+data.endcity+"</td>"
						+"<td>"+data.destination+"</td>"+"</tr>";
				
				$("#addRT_R_table_tbody_ID").html(R_html);
				
				
			}else {
				routeID_RT=null;
				$("#addRT_R_table_tbody_ID").html("<tr><td colspan='4'>该线路暂未开通</td></tr>");
				
			}
									
		}										
		
	}); //$.ajax结束
	

}



//添加新班次按钮
$(document).ready(function() {
	$("#add_RT_btn_ID").click(function() { // 添加一行input
		if(routeID_RT==null||routeID_RT==""){
			return
		}

		var newrow = "<tr>";
		newrow+="<td><input type='date' style='width:130px'/></td>"
		newrow+="<td><input type='time' style='width:100px'/></td>"
		newrow += "<td><input/></td>";
		newrow+="<td><input type='time' style='width:100px'/></td>"
		for(var i=0;i<2;i++){
			newrow += "<td><input type='number' min='1' style='width:60px'/></td>";
		}
		
		newrow += "<td><button class='savenewRT_btn save_btn'>保存</button></td>";
	 
		newrow += "</tr>"
		$("#addRT_table_tbody_ID").append(newrow);
		
		addInputListener(); // 输入框焦点事件
		saveNewRTListener(); // 给保存新班次按钮添加事件监听
		initDate(); //初始化出发日期为今天
	});

});
  

// 处理管理员点击的保存按钮（开通新线路）
function saveNewRTListener() {
	$(".savenewRT_btn").click(function() {
		// 首先需要获得
		var tr = $(this).parent().parent();
		addRT(tr);

	});
}

    

//添加新线路
function addRT(tr) {

	var RTInfo = new Array(6);
	var tdinput;
	var flag = true;
	for (var i = 0; i < 6; i++) {
		tdinput = tr.children("td").children().get(i);
		RTInfo[i] = tdinput.value;
		if (RTInfo[i] == null || RTInfo[i] == "") {
			tdinput.style.border = "2px solid #ff0000";
			flag = false;
		}
	}
	// 验证表单，是否为空
	if (flag == false)
	return false;
	
	var routeID=routeID_RT;
	
	$.ajax({
		dataType : "json", //数据类型为json格式
		contentType : "application/x-www-form-urlencoded; charset=utf-8",
		type : "POST",
		url : "../addRTservlet",
		data :{ddate:RTInfo[0],dtime:RTInfo[1],busnum:RTInfo[2],dur:RTInfo[3],
				ssum:RTInfo[4],price:RTInfo[5],routeID:routeID},
		statusCode : {
			404 : function() {
				alert('page not found');
			}
		},
		success : function(data, status) {	
			if (data.addRT_result == "true") { //添加成功	
				for (var i = 0; i < 6; i++) {
					tr.children("td").get(i).innerHTML = tr.children("td").children()
							.get(0).value;
				}
				
				tr.children("td").get(6).innerHTML = "<button class='edit_btn' disabled='disabled'>运营中</button>";
			
			}else {
				var txt="添加失败，请检查班次是否重复或者车牌号等信息是否有误";
				window.wxc.xcConfirm(txt, window.wxc.xcConfirm.typeEnum.error);
			}
									
		}										
		
	}); //$.ajax结束
	
	
	 
}
 

