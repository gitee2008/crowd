
 <#if path?if_exists>
  <img src="${request.contextPath}/${path}">
 <#elseif processDefinition?if_exists>
  <div align="left" style="padding-left: 60px;">
    <br>
    <br>  �����ţ� ${processDefinition.deploymentId}
	<br>  �����ţ� ${processDefinition.id}
	<br>  �������ƣ� ${processDefinition.name}
	<br>  ����Key��  ${processDefinition.key}
	<br>  ���̰汾�� ${processDefinition.version}
  </div>
 <#else>
     ���̷�����ɣ�  
 </#if>