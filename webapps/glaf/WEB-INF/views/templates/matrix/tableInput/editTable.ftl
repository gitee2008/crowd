<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>数据导入定义</title>
<#include "/inc/init_easyui_import.ftl"/>
<script type="text/javascript">

	function saveData(){
		var params = jQuery("#iForm").formSerialize();
		jQuery.ajax({
				   type: "POST",
				   url: '${contextPath}/matrix/tableInput/save',
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
		var params = jQuery("#iForm").formSerialize();
		jQuery.ajax({
				   type: "POST",
				   url: '${contextPath}/matrix/tableInput/saveAs',
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

</script>
</head>
<body>
<div style="margin:0;"></div>  
<div class="easyui-layout" data-options="fit:true">  
  <div data-options="region:'north',split:false,border:true" style="height:42px"  class="toolbar-backgroud"> 
    <div style="margin:4px;"> 
	<img src="${contextPath}/static/images/window.png">&nbsp;<span class="x_content_title">编辑数据导入定义</span>
	<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-save'" onclick="javascript:saveData();" >保存</a> 
	<#if tableId?exists>
	<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-save'" onclick="javascript:saveAsData();" >另存</a> 
	</#if>
    </div> 
  </div>

  <div data-options="region:'center',border:false,cache:true">
  <form id="iForm" name="iForm" method="post">
  <input type="hidden" id="tableId" name="tableId" value="${tableId}"/>
  <table class="easyui-form" style="width:95%;" align="center">
    <tbody>
	<tr>
		<td width="20%" align="left">标题</td>
		<td align="left">
            <input id="title" name="title" type="text" 
			       class="easyui-validatebox  x-text"  style="width:420px;"
				   value="${tableInput.title}" size="60"/>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">描述</td>
		<td align="left">
            <textarea id="description" name="description" type="text" 
			          class="easyui-validatebox  x-text"
					  style="width:420px;height:60px;">${tableInput.description}</textarea>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">目标表</td>
		<td align="left">
            <input id="tableName" name="tableName" type="text" 
			       class="easyui-validatebox x-text" style="width:420px;"
				   value="${tableInput.tableName}" size="30" maxLength="30"/>
			<div style="margin-top:5px;">
			  <span style="color:#ff3300;">
			    （提示：为了保证系统安全，目标表只能以useradd_、etl_、sync_、tree_table_、tmp_开头。）
			  </span>
	       </div>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">聚合主键列</td>
		<td align="left">
            <input id="aggregationKey" name="aggregationKey" type="text" 
			       class="easyui-validatebox x-text" style="width:420px;"
				   value="${tableInput.aggregationKey}" size="60" maxLength="300"/>
			<div style="margin-top:5px;">
			  <span style="color:#0099ff;">
			    （提示：构成唯一记录的列，多个列用半角的逗号","隔开。）
			  </span>
	       </div>
		</td>
	</tr>
    <tr>
		<td width="20%" align="left">开始行号</td>
		<td align="left">
            <input id="startRow" name="startRow" type="text" 
			       class="easyui-numberbox x-text"  style="width:60px;" precision="0"
				   value="${tableInput.startRow}" size="60"/>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">结束行号</td>
		<td align="left">
            <input id="stopRow" name="stopRow" type="text" 
			       class="easyui-numberbox x-text"  style="width:60px;" precision="0"
				   value="${tableInput.stopRow}" size="60"/>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">结束词</td>
		<td align="left">
            <input id="stopWord" name="stopWord" type="text" 
			       class="easyui-validatebox  x-text"  style="width:420px;"
				   value="${tableInput.stopWord}" size="60"/>
		</td>
	</tr>
	<tr>
		<td width="15%" align="left">抓取策略</td>
		<td align="left">
		    <select id="deleteFetch" name="deleteFetch">
			    <option value="">----请选择----</option>
				<option value="Y">每次抓取前删除</option>
			    <option value="N">增量抓取</option>
             </select>
             <script type="text/javascript">
                 document.getElementById("deleteFetch").value="${tableInput.deleteFetch}";
             </script>
			 &nbsp;（提示：增量抓取需要在合成主键列自行设置增量日期字段。）
		</td>
	</tr>
	<tr>
		<td width="15%" align="left">输入数据格式</td>
		<td align="left">
		    <select id="inputFlag" name="inputFlag">
			    <option value="">----请选择----</option>
				<option value="xls">Excel</option>
			    <option value="csv">CSV</option>
				<option value="text">Text</option>
             </select>
             <script type="text/javascript">
                 document.getElementById("inputFlag").value="${tableInput.inputFlag}";
             </script>
		</td>
	</tr>
	<tr>
	  <td colspan="2"><br><br><br><br></td>
	</tr>
    </tbody>
  </table>
  </form>
</div>
</div>

</body>
</html>