<!DOCTYPE html>
<html>
<head>
<title>数据导入</title>
<#include "/inc/init_easyui_import.ftl"/>
<script language="javascript">

	function submitRequest(form){
		if(document.getElementById("file").value==""){
            alert("请选择您要转换的格式为Excel97-2000，扩展名为xls的文件！");
			return;
		}
        if(confirm("您准备进行转换，确认吗？")){
            document.iForm.submit();
		}
	}

</script>
</head>
<body leftmargin="0" topmargin="0" marginheight="0" marginwidth="0">
<br>
<br>
<br>
<center>
<form action="${request.contextPath}/matrix/tableInputData/upload?tableId=${tableId}" method="post"
	enctype="multipart/form-data" name="iForm" class="x-form">
<div class="content-block"  style=" width: 90%;" >
<br>
<div class="x_content_title"><img
	src="${request.contextPath}/static/images/window.png" alt="数据导入">&nbsp;数据导入
</div>
 
<div align="center">
<br>
<label for="file">请选择您要导入的格式为Excel97-2000，扩展名为xls的文件</label>&nbsp;&nbsp;
<input type="file" id="file" name="file" size="50" class="input-file"> <br>
</div>
<br>

<div align="center"><br />
<input type="button" name="bt01" value="确定" class="btn btnGray"
	   onclick="javascript:submitRequest(this.form);" /> 
<br>
<br>
</div>
</div>
</form>
</center>
</body>
</html>