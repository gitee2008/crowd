<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>数据导出</title>
<#include "/inc/init_easyui_import.ftl"/>
<script type="text/javascript">
    var contextPath="${request.contextPath}";

	function saveData(){
		var params = jQuery("#iForm").formSerialize();
		jQuery.ajax({
				   type: "POST",
				   url: '${request.contextPath}/matrix/xmlExport/save',
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
					   if(data.statusCode == 200) { 
					       parent.location.reload(); 
					   }
				   }
			 });
	}

	function saveAsData(){
		var params = jQuery("#iForm").formSerialize();
		jQuery.ajax({
				   type: "POST",
				   url: '${request.contextPath}/matrix/xmlExport/saveAs',
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
					   if(data.statusCode == 200) { 
					       parent.location.reload(); 
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
	<span class="x_content_title"><img src="${request.contextPath}/static/images/window.png">&nbsp;编辑导出节点定义</span>
	<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-save'" 
	   onclick="javascript:saveData();" >保存</a> 
	<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-save'" 
	   onclick="javascript:saveAsData();" >另存</a> 
    </div> 
  </div>

  <div data-options="region:'center',border:false,cache:true">
  <form id="iForm" name="iForm" method="post">
  <input type="hidden" id="id" name="id" value="${xmlExport.id}"/>
  <input type="hidden" id="expId" name="expId" value="${xmlExport.id}"/>
  
  <table class="easyui-form" style="width:98%;" align="center">
    <tbody>
	<#if children?exists>
	<tr>
		<td width="12%" align="left">父节点</td>
		<td align="left">
            <select id="nodeParentId" name="nodeParentId">
				<option value="0">&nbsp;&nbsp;/&nbsp;&nbsp;</option>
				<#list children as child>
				<option value="${child.nodeId}">${child.blank}&nbsp;+&lt;${child.xmlTag}&gt;&nbsp;${child.title}[${child.level}]</option>
				</#list>
            </select>
			<script type="text/javascript">
			    document.getElementById("nodeParentId").value="${parent.nodeId}";
			</script>
		</td>
	</tr>
	<#else>
    
	</#if>
	<tr>
		<td width="12%" align="left">标题</td>
		<td align="left">
            <input id="title" name="title" type="text" 
			       class="easyui-validatebox x-text" style="width:625px;" 
				   value="${xmlExport.title}"/>
		</td>
	</tr>
	<tr>
		<td width="12%" align="left">名称</td>
		<td align="left">
            <input id="name" name="name" type="text" 
			       class="easyui-validatebox x-text" style="width:625px;" 
				   value="${xmlExport.name}"/>
			<div style="margin-top:5px;">
		     （提示：可以用于参数名的前缀，以便在子节点中引用。）
	        </div>
		</td>
	</tr>
	<tr>
		<td width="12%" align="left">映射名</td>
		<td align="left">
            <input id="mapping" name="mapping" type="text" 
			       class="easyui-validatebox x-text" style="width:625px;" 
				   value="${xmlExport.mapping}"/>
			<div style="margin-top:5px;">
		     （提示：指向上级节点的唯一字段名。）
	        </div>
		</td>
	</tr>
	<tr>
		<td width="12%" align="left">XML Tag</td>
		<td align="left">
            <input id="xmlTag" name="xmlTag" type="text" 
			       class="easyui-validatebox x-text" style="width:625px;" 
				   value="${xmlExport.xmlTag}"/>
		</td>
	</tr>

	<tr>
		<td width="90" align="left">SQL取数语句</td>
		<td align="left">
		    <textarea id="sql" name="sql" rows="6" cols="46" class="x-textarea" style="width:625px;height:320px;" >${xmlExport.sql}</textarea>
		   <div style="margin-top:5px;">
		     （提示：可以使用union语句组合结果。）
			<br>
			<span>
			 （可以使用动态参数,也可以使用父节点的输出变量当输入参数,例如: column1 = <script>document.write("#");</script>{param1}）
			</span>
			<br>
	      </div>
		</td>
	</tr>

    <tr>
		<td width="12%" align="left">结果类型</td>
		<td align="left">
		    <select id="resultFlag" name="resultFlag">
			    <option value="M">多条记录</option>
			    <option value="S">单一记录</option>
             </select>
             <script type="text/javascript">
                 document.getElementById("resultFlag").value="${xmlExport.resultFlag}";
             </script>
		</td>
    </tr>
  
	<tr>
		<td width="12%" align="left">顺序编号</td>
		<td align="left">
		    <select id="sortNo" name="sortNo">
			    <option value="0">----请选择----</option>
				<#list sortNoList as sortNo>
				<option value="${sortNo}">${sortNo}</option>
				</#list>
             </select>
             <script type="text/javascript">
                 document.getElementById("sortNo").value="${xmlExport.sortNo}";
             </script>
			 &nbsp;（提示：顺序小的先执行。）
		</td>
	</tr>
	<tr>
		<td width="12%" align="left">是否有效</td>
		<td align="left">
		    <select id="active" name="active">
				<option value="Y">是</option>
			    <option value="N">否</option>
             </select>
             <script type="text/javascript">
                 document.getElementById("active").value="${xmlExport.active}";
             </script>
		</td>
	</tr>
	<tr><td><br><br><br><br></td></tr>
    </tbody>
  </table>
 </form>
</div>
</div>
</body>
</html>