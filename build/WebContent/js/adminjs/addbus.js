//添加新车按钮
$(document).ready(function() {
	$("#addbusbtnID").click(function() { // 添加一行input

		var newrow = "<tr>";
		for (var i = 0; i < 5; i++) {
			newrow += "<td><input/></td>";
		}
		newrow += "<td><button class='savenewbus_btn save_btn'>保存</button></td>";
		newrow += "<td><button class='deletenewbus_btn delete_btn'>删除</button></td>";
		newrow += "</tr>"
		$("#addbus_table_tbody_ID").append(newrow);
		addInputListener(); // 输入框焦点事件
		saveNewBusListener(); // 给保存新车按钮添加事件监听
		deleteNewBusListener();// 给删除新车按钮添加事件监听
	});

});

// 处理管理员点击的保存按钮（添加新车辆）
function saveNewBusListener() {
	
	$(".savenewbus_btn").click(function() {
		//alert("savenewbus_btn");
		// 首先需要获得车牌号
		var tr = $(this).parent().parent();
		var busID=tr.children("td").children("button").get(0).value;
		if(busID==null||busID==""){  //如果第一次点击保存，还没有获得车辆busID--是添加新车辆
			addBusByNum(tr);
			return;
		}
		//如果已经获取到了busID,则跳转到更新车辆信息
		else{
			updateNewBus(tr,busID);
			return;
		}
		

	});
}





// 给删除新车按钮添加事件监听
function deleteNewBusListener()
{
	$(".deletenewbus_btn").click(function() {
		// 首先需要获得车牌号
		var tr = $(this).parent().parent();
		var busID=tr.children("td").children("button").get(0).value;
		if(busID==null||busID==""){ //如果没有保存就删除一行
			tr.remove();
			return;
		}

		deleteNewBus(tr,busID);
	});
	
}


// 根据车牌号添加新车
function addBusByNum(tr) {
	//alert("添加新车");
	var busInfo = new Array(5);
	var tdinput;
	var flag = true;
	for (var i = 0; i < 5; i++) {
		tdinput = tr.children("td").children().get(i);
		busInfo[i] = tdinput.value;
		if (busInfo[i] == null || busInfo[i] == "") {
			tdinput.style.border = "2px solid #ff0000";
			flag = false;
		}
	}
	// 验证表单，是否为空
	if (flag == false)
		return false;
	// $.ajax();

	$.ajax({
		dataType : "json", //数据类型为json格式
		contentType : "application/x-www-form-urlencoded; charset=utf-8",
		type : "POST",
		url : "../addbusservlet",
		data :{busnum:busInfo[0],bussize:busInfo[1],bustype:busInfo[2],
			busdriver:busInfo[3],phonenumber:busInfo[4]},
		statusCode : {
			404 : function() {
				alert('page not found');
			}
		},
		success : function(data, status) {	
			if (data.addbus_result == "true") { //添加成功
				//获取busID
				var busID=data.busID;
				//alert(busID);
				toggleToEditNewBus(tr, busID);
				
			}else {
				var txt="添加失败，请检查车辆信息是否有误或者车牌号重复";				
				window.wxc.xcConfirm(txt, window.wxc.xcConfirm.typeEnum.error);
			}									
		}										
		
	}); //$.ajax结束	
	

}

function editnewbus_btn_listener(){
	
	
}

//切换到编辑按钮
function toggleToEditNewBus(tr, busID) {
	tr.children("td").get(5).innerHTML = "<button class='editnewbus_btn edit_btn' value='"
			+ busID + "'>编辑</button>";
	for (var i = 0; i < 5; i++) {
		tr.children("td").get(i).innerHTML = tr.children("td").children()
				.get(0).value;
	}

	$(".editnewbus_btn").click(function() {
		// alert("从保存按钮切换到了编辑按钮")
		var tr = $(this).parent().parent();
		for (var i = 0; i < 5; i++) {
			var td = tr.children("td").get(i);
			var tdtext = td.innerHTML;
			td.innerHTML = "<input value='" + tdtext + "'/>";
		}
		addInputListener();
		toggleToSaveNewBus(tr, busID);
	});

}


function toggleToSaveNewBus(tr, busID)
{
	tr.children("td").get(5).innerHTML="<button class='savenewbus_btn save_btn' value='"+busID+"'>保存</button>";	
	saveNewBusListener();
}




function updateNewBus(tr,busID){
	//验证表单，是否为空
	var busInfo=new Array(5);
	var flag=true;
	for(var i=0;i<5;i++){
		tdinput=tr.children("td").children().get(i);
		busInfo[i]=tdinput.value;
		if(busInfo[i]==null||busInfo[i]==""){
			tdinput.style.border="2px solid #ff0000";
			flag=false;
		}
	}
	if(flag==false){
		return false;
	}
	
	$.ajax({
		dataType : "json", //数据类型为json格式
		contentType : "application/x-www-form-urlencoded; charset=utf-8",
		type : "POST",
		url : "../updatebusservlet",
		data :{busnum:busInfo[0],bussize:busInfo[1],bustype:busInfo[2],
			busdriver:busInfo[3],phonenumber:busInfo[4],busID:busID},
		statusCode : {
			404 : function() {
				alert('page not found');
			}
		},
		success : function(data, status) {	
			if (data.updatebus_result == "true") { //更新成功	
				toggleToEditNewBus(tr, busID);				
			}else {
				var txt="更新失败，请检查车辆信息是否有误或者车牌号重复";			
				window.wxc.xcConfirm(txt, window.wxc.xcConfirm.typeEnum.error);
			}									
		}										
		
	}); //$.ajax结束	
	

}





//删除车辆信息
function deleteNewBus(tr,busID){	

	var txt=  "您确定删除该车辆信息吗？";
	var option = {
		title: "信息",
		btn: parseInt("0011",2),
		onOk: function(){
			console.log("确认删除车辆信息啦");
			if(busID==null||busID=="")//未提交到服务器保存
				return;			
			$.ajax({
				dataType : "json", //数据类型为json格式
				contentType : "application/x-www-form-urlencoded; charset=utf-8",
				type : "POST",
				url : "../deletebusservlet",
				data :{busID:busID},
				statusCode : {
					404 : function() {
						alert('page not found');
					}
				},
				success : function(data, status) {	
					if (data.deletebus_result == "true") { //删除成功						
						tr.remove(); 
		
					}else {
						var txt="删除失败";				
						window.wxc.xcConfirm(txt, window.wxc.xcConfirm.typeEnum.error);
					}									
				}										
				
			}); //$.ajax结束	
						
		}
	}
	
	window.wxc.xcConfirm(txt, "custom", option);
		
}


