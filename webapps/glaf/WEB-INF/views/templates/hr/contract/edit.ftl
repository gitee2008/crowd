<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>劳动合同</title>
<#include "/inc/init_easyui_import.ftl"/>
<script type="text/javascript">

	function saveData(){
		var params = jQuery("#iForm").formSerialize();
		jQuery.ajax({
				   type: "POST",
				   url: '${contextPath}/hr/contract/saveContract',
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

	function saveAsData(){
		document.getElementById("id").value="";
		document.getElementById("id").value="";
		var params = jQuery("#iForm").formSerialize();
		jQuery.ajax({
				   type: "POST",
				   url: '${contextPath}/hr/contract/saveContract',
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
	<span class="x_content_title">编辑劳动合同</span>
	<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-save'" onclick="javascript:saveData();" >保存</a> 
    </div> 
  </div>

  <div data-options="region:'center',border:false,cache:true">
  <form id="iForm" name="iForm" method="post">
  <input type="hidden" id="id" name="id" value="${contract.id}"/>
  <table class="easyui-form" style="width:600px;" align="center">
    <tbody>
	<tr>
		<td width="20%" align="left">工号</td>
		<td align="left">
              <input id="code" name="code" type="text" 
			         class="easyui-validatebox x-text"  
			  
				     value="${contract.code}"/>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">姓名</td>
		<td align="left">
              <input id="name" name="name" type="text" 
			         class="easyui-validatebox x-text"  
			  
				     value="${contract.name}"/>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">性别</td>
		<td align="left">
              <input id="sex" name="sex" type="text" 
			         class="easyui-validatebox x-text"  
			  
				     value="${contract.sex}"/>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">部门</td>
		<td align="left">
              <input id="department" name="department" type="text" 
			         class="easyui-validatebox x-text"  
			  
				     value="${contract.department}"/>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">职务</td>
		<td align="left">
              <input id="post" name="post" type="text" 
			         class="easyui-validatebox x-text"  
			  
				     value="${contract.post}"/>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">内容要素</td>
		<td align="left">
              <input id="content" name="content" type="text" 
			         class="easyui-validatebox x-text"  
			  
				     value="${contract.content}"/>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">单位</td>
		<td align="left">
              <input id="company" name="company" type="text" 
			         class="easyui-validatebox x-text"  
			  
				     value="${contract.company}"/>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">合同编号</td>
		<td align="left">
              <input id="contractCode" name="contractCode" type="text" 
			         class="easyui-validatebox x-text"  
			  
				     value="${contract.contractCode}"/>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">合同期限</td>
		<td align="left">
              <input id="limit" name="limit" type="text" 
			         class="easyui-validatebox x-text"  
			  
				     value="${contract.limit}"/>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">期限类型</td>
		<td align="left">
              <input id="limitType" name="limitType" type="text" 
			         class="easyui-validatebox x-text"  
			  
				     value="${contract.limitType}"/>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">起始时间</td>
		<td align="left">
			<input id="start" name="start" type="text" 
			       class="easyui-datebox x-text"
			
			       <#if contract.start?exists>
				   value="${contract.start ? string('yyyy-MM-dd')}"
				   </#if>
				   />
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">终止日期</td>
		<td align="left">
			<input id="end" name="end" type="text" 
			       class="easyui-datebox x-text"
			
			       <#if contract.end?exists>
				   value="${contract.end ? string('yyyy-MM-dd')}"
				   </#if>
				   />
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">签订日期</td>
		<td align="left">
			<input id="signDate" name="signDate" type="text" 
			       class="easyui-datebox x-text"
			
			       <#if contract.signDate?exists>
				   value="${contract.signDate ? string('yyyy-MM-dd')}"
				   </#if>
				   />
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">签订地点</td>
		<td align="left">
              <input id="signPlace" name="signPlace" type="text" 
			         class="easyui-validatebox x-text"  
			  
				     value="${contract.signPlace}"/>
		</td>
	</tr>
 
    </tbody>
  </table>
  </form>
</div>
</div>
</body>
</html>