
//全局变量：
var bussum=0;
var bustotalPages=0;
var busPerPage=8; //每页显示8条记录
var busArray; //线路查询结果数组


 
function validatebusForm(){
	var reqkey=$("#searchbus_key_ID").val();
	var reqvalue=$("#searchbus_value_ID").val();
	var flag=true;
	if(reqkey==null||reqkey==""){
		$("#searchbus_key_ID").css("border","2px solid #ff0000");
		flag=false;
	}
	if(reqkey!="all"&&(reqvalue==null||reqvalue=="")){
		$("#searchbus_value_ID").css("border","2px solid #ff0000");
		flag=false;
	}
	if(flag==false){
		return false;
	}

	
	selectBus(reqkey,reqvalue);
	
	return false;
}

//查询车辆信息
function selectBus(reqkey,reqvalue){
	$.ajax({
		dataType : "json", //数据类型为json格式
		contentType : "application/x-www-form-urlencoded; charset=utf-8",
		type : "POST",
		url : "../selectbusservlet",
		data :{reqkey:reqkey,reqvalue:reqvalue},
		statusCode : {
			404 : function() {
				alert('page not found');
			}
		},
		success : function(data, status) {	
			if (data.selectbus_result == "true") { //查询成功		
				 
				busArray=data.bus;
				bussum=data.bussum;				
				bustotalPages=bussum%busPerPage==0 ?bussum/busPerPage :bussum/busPerPage+1;												
				bustotalPages=parseInt(bustotalPages);	
				
				add_busPage_btn_listener();				
			}else {
				bussum=0;
				bustotalPages=0;	
				$("#bus_table_tbody_ID").html("<tr><td colspan='6'>没有找到车辆信息</td></tr>");			
				add_busPage_btn_listener();
			}									
		}										
		
	}); //$.ajax结束	
	

}

function showBusPageOf(num)
{
	if(num>=bustotalPages)
		return;
	var startIndex=num*busPerPage;	
	//计算每一页实际要显示的记录条数，防止记录不足busPerPage，
	var numPerPage=0; //每一页实际要显示的记录条数
	if(startIndex+busPerPage>bussum){
		numPerPage=bussum-startIndex;		
	}else numPerPage=busPerPage;
	
	//alert("开始索引："+startIndex);
	bus_html="";
	for(var i=startIndex;i<startIndex+numPerPage;i++){	
		bus_html+="<tr>"+"<td>"+busArray[i].busID+"</td>"
				+"<td>"+busArray[i].busnum+"</td>"
				+"<td>"+busArray[i].bussize+"</td>"
				+"<td>"+busArray[i].bustype+"</td>"
				+"<td>"+busArray[i].busdriver+"</td>"
				+"<td>"+busArray[i].phonenumber+"</td>";

		bus_html+="<td><button class='editbus_btn edit_btn' value='"+busArray[i].busID+"'>编辑</button></td>"
		
		bus_html+="<td><button class='deletebus_btn delete_btn' value='"+busArray[i].busID+"'>删除</button><td>"
	
		bus_html+="</tr>";
		
	}
	
	//alert(ticket_html);
	$("#bus_table_tbody_ID").html(bus_html);	
	
	editbus_btn_listener();
	deletebus_btn_listener();

}


//处理管理员点击的编辑按钮
function editbus_btn_listener(){
	$(".editbus_btn").click(function() {
		var busID=$(this).val();
		var tr=$(this).parent().parent();
		for(var i=1;i<=5;i++){
			var td=tr.children("td").get(i); 
			var tdtext=td.innerHTML;
			td.innerHTML="<input value='"+tdtext+"'/>";
		}	
		addInputListener();
		toggleToSaveBus(tr,busID);
		//alert("切换结束");
	
	});
	
}


//处理管理员点击的保存按钮
function savebus_btn_listener(){
	$(".savebus_btn").click(function() {	
		// alert("点击了保存按钮");
		var busID=$(this).val();
		var tr=$(this).parent().parent();
		updateBus(tr,busID)	
	});
	
}


// 点击编辑按钮之后切换到了保存按钮 ,
function toggleToSaveBus(tr,busID){
	tr.children("td").get(6).innerHTML="<button class='savebus_btn save_btn' value='"+busID+"'>保存</button>";	
	savebus_btn_listener();
	
}

function updateBus(tr,busID){
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
				toggleToEditBus(tr,busID);
				
			}else {
				var txt="更新失败，请检查车辆信息是否有误或者车牌号重复";
				
				window.wxc.xcConfirm(txt, window.wxc.xcConfirm.typeEnum.error);
			}									
		}										
		
	}); //$.ajax结束	
	
	
	
}



// 点击了保存按钮之后切换到编辑按钮
function toggleToEditBus(tr,busID){
	tr.children("td").get(6).innerHTML="<button class='editbus_btn edit_btn' value='"+busID+"'>编辑</button>";	
	for(var i=0;i<5;i++){
		tr.children("td").get(i+1).innerHTML=tr.children("td").children().get(0).value;  
	}
	$(".editbus_btn").click(function() {		
		// alert("从保存按钮切换到了编辑按钮")
		var tr=$(this).parent().parent();
		for(var i=1;i<=5;i++){
			var td=tr.children("td").get(i); 
			var tdtext=td.innerHTML;
			td.innerHTML="<input value='"+tdtext+"'/>";
		}	
		addInputListener();
		toggleToSaveBus(tr,busID);
	});

}




//分页按钮添加点击事件监听
function add_busPage_btn_listener()
{
	var setTotalCount =bussum;
	$('#buspages_box').paging({
		initPageNo : 1, // 初始页码
		totalPages : bustotalPages, //总页数
		totalCount : '合计' + setTotalCount + '条数据', // 条目总数
		slideSpeed : 600, // 缓动速度。单位毫秒
		jump : true, //是否支持跳转
		callback : function(page) { // 回调函数
			console.log(page);
			//alert("当前页："+page);
			showBusPageOf(page-1);
			
		}
	})
}


//删除车辆信息按钮
function deletebus_btn_listener()
{
	$(".deletebus_btn").click(function(){
		var tr=$(this).parent().parent();
		//获得汽车编号
		var busID=$(this).val();
		var txt=  "您确定删除该车辆信息吗？";
		var option = {
			title: "信息",
			btn: parseInt("0011",2),
			onOk: function(){
				console.log("确认删除车辆信息啦");
				// $.ajax();
				deleteBus(tr,busID);
			}
		}
		window.wxc.xcConfirm(txt, "custom", option);
	});
	
	
}

function deleteBus(tr,busID){
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
				
				validatebusForm(); //刷新一遍表格
				
			}else {
				var txt="删除失败";				
				window.wxc.xcConfirm(txt, window.wxc.xcConfirm.typeEnum.error);
			}									
		}										
		
	}); //$.ajax结束	
	
}

