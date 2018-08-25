<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>产品项</title>
<#include "/inc/init_easyui_import.ftl"/>
<script type="text/javascript">

	function saveData(){
		var params = jQuery("#iForm").formSerialize();
		jQuery.ajax({
				   type: "POST",
				   url: '${contextPath}/crowd/productItem/saveProductItem',
				   data: params,
				   dataType: 'json',
				   error: function(data){
					   alert('服务器处理错误！');
				   },
				   success: function(data){
					   if(data != null && data.message != null){
						   alert(data.message);
					   } else {
						   alert('操作成功完成！');
					   }
					   if(data.statusCode == 200){
					       window.parent.location.reload();
					   } 
				   }
			 });
	}

	function saveAsData(){
		document.getElementById("id").value="";
		var params = jQuery("#iForm").formSerialize();
		jQuery.ajax({
				   type: "POST",
				   url: '${contextPath}/crowd/productItem/saveProductItem',
				   data: params,
				   dataType:  'json',
				   error: function(data){
					   alert('服务器处理错误！');
				   },
				   success: function(data){
					   if(data != null && data.message != null){
						   alert(data.message);
					   } else {
						   alert('操作成功完成！');
					   }
					   if(data.statusCode == 200){
					       window.parent.location.reload();
					   }
				   }
			 });
	}

</script>
</head>

<body>
<div style="margin:0;"></div>  

<div class="easyui-layout" data-options="fit:true">  
  <div data-options="region:'north',split:true,border:true" style="height:40px"> 
    <div class="toolbar-backgroud"> 
	<span class="x_content_title">&nbsp;编辑产品项</span>&nbsp;
	<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-save'" onclick="javascript:saveData();" >保存</a> 
    </div> 
  </div>

  <div data-options="region:'center',border:false,cache:true">
  <form id="iForm" name="iForm" method="post">
  <input type="hidden" id="id" name="id" value="${productItem.id}"/>
  <table class="easyui-form" style="width:600px;" align="center">
    <tbody>
	<tr>
		<td width="20%" align="left">分类</td>
		<td align="left">
           <select id="category" name="category">
			    <option value="">----请选择----</option>
				<#list categories as category>
				<option value="${category.code}">${category.name}</option>
				</#list>
			</select>
			<script type="text/javascript">
				 document.getElementById("category").value="${productItem.category}";
			</script>     
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">标题</td>
		<td align="left">
              <input id="itemTitle" name="itemTitle" type="text" 
			         class="easyui-validatebox x-text" style="width:350px;"   
				     value="${productItem.itemTitle}"/>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">发起人</td>
		<td align="left">
              <input id="itemName" name="itemName" type="text" 
			         class="easyui-validatebox x-text" style="width:350px;" 
				     value="${productItem.itemName}"/>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">地址</td>
		<td align="left">
              <input id="itemLocation" name="itemLocation" type="text" 
			         class="easyui-validatebox x-text" style="width:350px;"
				     value="${productItem.itemLocation}"/>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">小图片地址</td>
		<td align="left">
              <input id="smallUrl" name="smallUrl" type="text" 
			         class="easyui-validatebox x-text" style="width:350px;"
				     value="${productItem.smallUrl}"/>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">图片地址</td>
		<td align="left">
              <input id="itemUrl" name="itemUrl" type="text" 
			         class="easyui-validatebox x-text" style="width:350px;"
				     value="${productItem.itemUrl}"/>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">内容描述</td>
		<td align="left">
		    <textarea id="itemContent" name="itemContent" rows="6" cols="46" class="x-text" style="height:150px;width:350px;" >${productItem.itemContent}</textarea>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">金额</td>
		<td align="left">
			<input id="itemMoney" name="itemMoney" type="text" style="width:90px;" 
			       class="easyui-numberbox  x-text"  precision="2" value="${productItem.itemMoney}"/>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">有效天数</td>
		<td align="left">
			<input id="itemDay" name="itemDay" type="text" 
			       class="easyui-numberbox x-text" style="width:90px;" 
				   increment="1" value="${productItem.itemDay}"/>
		</td>
	</tr>
   </tbody>
  </table>
  </form>
</div>
</div>
</body>
</html>