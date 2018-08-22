<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>人员基本信息</title>
<#include "/inc/init_easyui_import.ftl"/>
<script type="text/javascript">

	function saveData(){
		var params = jQuery("#iForm").formSerialize();
		jQuery.ajax({
				   type: "POST",
				   url: '${contextPath}/hr/staffInfo/saveStaffInfo',
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
				   url: '${contextPath}/hr/staffInfo/saveStaffInfo',
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
	<span class="x_content_title">编辑人员基本信息</span>
	<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-save'" onclick="javascript:saveData();" >保存</a> 
    </div> 
  </div>

  <div data-options="region:'center',border:false,cache:true">
  <form id="iForm" name="iForm" method="post">
  <input type="hidden" id="id" name="id" value="${staffInfo.id}"/>
  <table class="easyui-form" style="width:600px;" align="center">
    <tbody>
	<tr>
		<td width="20%" align="left">工号</td>
		<td align="left">
              <input id="code" name="code" type="text" 
			         class="easyui-validatebox x-text"  
			  
				     value="${staffInfo.code}"/>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">姓名</td>
		<td align="left">
              <input id="name" name="name" type="text" 
			         class="easyui-validatebox x-text"  
			  
				     value="${staffInfo.name}"/>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">部门ID</td>
		<td align="left">
              <input id="deptId" name="deptId" type="text" 
			         class="easyui-validatebox x-text"  
			  
				     value="${staffInfo.deptId}"/>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">部门</td>
		<td align="left">
              <input id="department" name="department" type="text" 
			         class="easyui-validatebox x-text"  
			  
				     value="${staffInfo.department}"/>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">性别</td>
		<td align="left">
              <input id="sex" name="sex" type="text" 
			         class="easyui-validatebox x-text"  
			  
				     value="${staffInfo.sex}"/>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">职务</td>
		<td align="left">
              <input id="post" name="post" type="text" 
			         class="easyui-validatebox x-text"  
			  
				     value="${staffInfo.post}"/>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">入职时间</td>
		<td align="left">
			<input id="entryDate" name="entryDate" type="text" 
			       class="easyui-datebox x-text"
			
			       <#if staffInfo.entryDate?exists>
				   value="${staffInfo.entryDate ? string('yyyy-MM-dd')}"
				   </#if>
				   />
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">关联用户ID</td>
		<td align="left">
              <input id="userId" name="userId" type="text" 
			         class="easyui-validatebox x-text"  
			  
				     value="${staffInfo.userId}"/>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">试用转正</td>
		<td align="left">
              <input id="staffStatus" name="staffStatus" type="text" 
			         class="easyui-validatebox x-text"  
			  
				     value="${staffInfo.staffStatus}"/>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">转正日期</td>
		<td align="left">
			<input id="becomerDate" name="becomerDate" type="text" 
			       class="easyui-datebox x-text"
			
			       <#if staffInfo.becomerDate?exists>
				   value="${staffInfo.becomerDate ? string('yyyy-MM-dd')}"
				   </#if>
				   />
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">在职类别</td>
		<td align="left">
              <input id="professionType" name="professionType" type="text" 
			         class="easyui-validatebox x-text"  
			  
				     value="${staffInfo.professionType}"/>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">用工形式</td>
		<td align="left">
              <input id="employmentForm" name="employmentForm" type="text" 
			         class="easyui-validatebox x-text"  
			  
				     value="${staffInfo.employmentForm}"/>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">身份证号码</td>
		<td align="left">
              <input id="cardNo" name="cardNo" type="text" 
			         class="easyui-validatebox x-text"  
			  
				     value="${staffInfo.cardNo}"/>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">出生日期</td>
		<td align="left">
			<input id="birthday" name="birthday" type="text" 
			       class="easyui-datebox x-text"
			
			       <#if staffInfo.birthday?exists>
				   value="${staffInfo.birthday ? string('yyyy-MM-dd')}"
				   </#if>
				   />
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">年龄</td>
		<td align="left">
			<input id="age" name="age" type="text" 
			       class="easyui-numberbox x-text" 
				   increment="10"  
				   value="${staffInfo.age}"/>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">籍贯</td>
		<td align="left">
              <input id="nativePlace" name="nativePlace" type="text" 
			         class="easyui-validatebox x-text"  
			  
				     value="${staffInfo.nativePlace}"/>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">政治面貌</td>
		<td align="left">
              <input id="politicalStatus" name="politicalStatus" type="text" 
			         class="easyui-validatebox x-text"  
			  
				     value="${staffInfo.politicalStatus}"/>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">民族</td>
		<td align="left">
              <input id="nation" name="nation" type="text" 
			         class="easyui-validatebox x-text"  
			  
				     value="${staffInfo.nation}"/>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">婚姻状况</td>
		<td align="left">
              <input id="maritalStatus" name="maritalStatus" type="text" 
			         class="easyui-validatebox x-text"  
			  
				     value="${staffInfo.maritalStatus}"/>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">户口所在地</td>
		<td align="left">
              <input id="registeredResidence" name="registeredResidence" type="text" 
			         class="easyui-validatebox x-text"  
			  
				     value="${staffInfo.registeredResidence}"/>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">身份证到期</td>
		<td align="left">
              <input id="cardNoExpire" name="cardNoExpire" type="text" 
			         class="easyui-validatebox x-text"  
			  
				     value="${staffInfo.cardNoExpire}"/>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">移动电话</td>
		<td align="left">
              <input id="mobileTelphone" name="mobileTelphone" type="text" 
			         class="easyui-validatebox x-text"  
			  
				     value="${staffInfo.mobileTelphone}"/>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">电子邮箱</td>
		<td align="left">
              <input id="email" name="email" type="text" 
			         class="easyui-validatebox x-text"  
			  
				     value="${staffInfo.email}"/>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">家庭住址</td>
		<td align="left">
              <input id="homeAddress" name="homeAddress" type="text" 
			         class="easyui-validatebox x-text"  
			  
				     value="${staffInfo.homeAddress}"/>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">学历</td>
		<td align="left">
              <input id="educationalBackground" name="educationalBackground" type="text" 
			         class="easyui-validatebox x-text"  
			  
				     value="${staffInfo.educationalBackground}"/>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">学位</td>
		<td align="left">
              <input id="degree" name="degree" type="text" 
			         class="easyui-validatebox x-text"  
			  
				     value="${staffInfo.degree}"/>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">毕业院校</td>
		<td align="left">
              <input id="graduateInstitutions" name="graduateInstitutions" type="text" 
			         class="easyui-validatebox x-text"  
			  
				     value="${staffInfo.graduateInstitutions}"/>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">专业</td>
		<td align="left">
              <input id="profession" name="profession" type="text" 
			         class="easyui-validatebox x-text"  
			  
				     value="${staffInfo.profession}"/>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">毕业时间</td>
		<td align="left">
			<input id="graduationTime" name="graduationTime" type="text" 
			       class="easyui-datebox x-text"
			
			       <#if staffInfo.graduationTime?exists>
				   value="${staffInfo.graduationTime ? string('yyyy-MM-dd')}"
				   </#if>
				   />
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">外语水平</td>
		<td align="left">
              <input id="foreignLanguage" name="foreignLanguage" type="text" 
			         class="easyui-validatebox x-text"  
			  
				     value="${staffInfo.foreignLanguage}"/>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">技术职称</td>
		<td align="left">
              <input id="technicalTitle" name="technicalTitle" type="text" 
			         class="easyui-validatebox x-text"  
			  
				     value="${staffInfo.technicalTitle}"/>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">教育经历</td>
		<td align="left">
              <input id="educationalExperience" name="educationalExperience" type="text" 
			         class="easyui-validatebox x-text"  
			  
				     value="${staffInfo.educationalExperience}"/>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">工作经历</td>
		<td align="left">
              <input id="workExperience" name="workExperience" type="text" 
			         class="easyui-validatebox x-text"  
			  
				     value="${staffInfo.workExperience}"/>
		</td>
	</tr>
 
    </tbody>
  </table>
  </form>
</div>
</div>
</body>
</html>