function changePwd()
{
	//先做表单的检查
	var oldPassword = document.forms["chpwdForm"]["oldPassword"].value;
	var newPassword = document.forms["chpwdForm"]["newPassword"].value;
	var confirmpwd = document.forms["chpwdForm"]["confirmpwd"].value;
	
	var flag = true;
	if (oldPassword == null || oldPassword == "") {
		//设置边框变红，提示请输入旧密码
		$("#oldPasswordID").css({
				"border" : "2px solid #ff0000"
			});
		$("#oldPasswordID").attr("placeholder", "请输入旧密码");		
		flag = false;
	}
	if (newPassword == null || newPassword == "") {
		//设置边框变红，提示请输入新密码
		$("#newPasswordID").css({
				"border" : "2px solid #ff0000"
			});
		$("#newPasswordID").attr("placeholder", "请输入新密码");
		flag = false;
	}
	if (confirmpwd == null || confirmpwd == "") {
		//设置边框变红，提示请输入新密码
		$("#confirmpwdID").css({
				"border" : "2px solid #ff0000"
			});
		$("#confirmpwdID").attr("placeholder", "请输入确认密码");
		flag = false;
	}
	if (confirmpwd!=newPassword) {
		//设置边框变红，提示请输入新密码
		$("#confirmpwdID").css({
				"border" : "2px solid #ff0000"
			});
		//alert("密码不一致")
		window.wxc.xcConfirm("您输入的新密码和确认密码不一致", window.wxc.xcConfirm.typeEnum.info);
		flag = false;
	}
	
	if(flag==false){
		return false;
	}	
	//ajax post
	//首先查看旧密码是否正确
	var txt=  "您确定修改密码吗？";
		var option = {
				title: "消息",
				btn: parseInt("0011",2),
				onOk: function(){
					console.log("确认修改密码啦");
					//alert("点击了确认");
					$.ajax({
						dataType : "json", //数据类型为json格式
						contentType : "application/x-www-form-urlencoded; charset=utf-8",
						type : "POST",
						url : "../adminchpwdservlet",
						data :{oldPassword:oldPassword,newPassword:newPassword},
						statusCode : {
							404 : function() {
								alert('page not found');
							}
						},
						success : function(data, status) {
							//alert("message:"+data);	
							 if(data.chpwd_result=="oldpwderror"){ //旧密码错误
								 window.wxc.xcConfirm("您输入的旧密码有误", window.wxc.xcConfirm.typeEnum.error);
								 
							 }else if(data.chpwd_result=="success"){ //成功修改密码
								 window.wxc.xcConfirm("您已成功修改密码", window.wxc.xcConfirm.typeEnum.success);
								 
							 }else{
								 window.wxc.xcConfirm("修改失败", window.wxc.xcConfirm.typeEnum.error);
							 }				 
							
							
						}										
						
					}); //$.ajax结束
																
				}
			}	
		window.wxc.xcConfirm(txt, "custom", option);
	

	return false;
}
