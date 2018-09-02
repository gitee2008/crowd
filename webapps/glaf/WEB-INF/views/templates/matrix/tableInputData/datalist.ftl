<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>数据列表</title>
<#include "/inc/init_easyui_layer3_import.ftl"/>
<script type="text/javascript">
 
    var _height = jQuery(window).height();
	var _width = jQuery(window).width();
 
    var x_height = Math.floor(_height * 0.82);
	var x_width = Math.floor(_width);

	//alert(jQuery(window).height());

   <#if table.treeFlag == "Y">
   var setting = {
			async: {
				enable: true,
				url:"${contextPath}/matrix/tableInputData/treeJson?tableId=${tableId}",
				dataFilter: filter
			},
			callback: {
				onClick: zTreeOnClick
			}
		};
  
  	function filter(treeId, parentNode, childNodes) {
		if (!childNodes) return null;
		for (var i=0, l=childNodes.length; i<l; i++) {
			childNodes[i].name = childNodes[i].name.replace(/\.n/g, '.');
			childNodes[i].icon="${contextPath}/static/images/basic.gif";
			if(childNodes[i].level == 2){
               //childNodes[i].icon="${contextPath}/static/images/bricks.png";
			}
		}
		return childNodes;
	}


    function zTreeOnClick(event, treeId, treeNode, clickFlag) {
		jQuery("#nodeId").val(treeNode.id);
		//var p = jQuery('#mydatagrid').datagrid('getPager');
		//alert(p.pageSize);
		//var link = "${contextPath}/matrix/tableInputData/data?tableId=${tableId}&parentId="+treeNode.id;
		//loadGridData(link);
		jQuery('#mydatagrid').datagrid({
			queryParams: {
				tableId: '${tableId}',
				parentId: treeNode.id
			}
		});

 	}

    jQuery(document).ready(function(){
		jQuery.fn.zTree.init(jQuery("#myTree"), setting);
	});

	</#if>

   var link_ = "${contextPath}/matrix/tableInputData/json?tableId=${tableId}&topId=${topId}";
 
   jQuery(function(){
		jQuery('#mydatagrid').datagrid({
				width:x_width,
				height:x_height,
				fit: false,
				fitColumns: true,
				nowrap: false,
				striped: true,
				collapsible: true,
				url: link_,
				remoteSort: false,
				singleSelect: true,
				idField: 'id',
				columns:[[
				        {title:'序号', field:'startIndex', width:60, sortable:false},
                        <#list  columns as field>
                         <#if field.locked == 0 && field.displayType == 4 && field.columnName?exists>
						   {title:'${field.title}', field:'${field.lowerColumnName}', width:${field.listWidth}, sortable:true},
					     </#if>
                        </#list>
					    {field:'functionKey', title:'功能键', width:60, formatter:formatterKeys }
				]],
				rownumbers: false,
				pagination: true,
				pageSize: 20,
				pageList: [10,15,20,25,30,40,50,100,500,1000],
				pagePosition: 'both'
			});

			var p = jQuery('#mydatagrid').datagrid('getPager');
			jQuery(p).pagination({
				onBeforeRefresh:function(){
                    alert('before refresh');
 				}
		    });

			$('#mydatagrid').datagrid({
				onDblClickRow: function(index, row){
					 editRow(row.uuid);
				}
			});

	});

		 
	function addNew(){
		link="${contextPath}/matrix/tableInputData/edit?tableId=${tableId}&topId=${topId}";
		layer.open({
			  type: 2,
			  maxmin: true,
			  shadeClose: true,
			  title: "新增记录",
			  area: ['980px', (jQuery(window).height() - 30) +'px'],
			  shade: 0.85,
			  fixed: false, //不固定
			  shadeClose: true,
			  content: [link, 'no']
			});
	}


	function formatterStatus(val, row){
		if(val == 9){
			return "<font color='green'>通过</font>";
		}else if(val == -1){
			return "<font color='red'>不通过</font>";
		} else {
            return "<font color='blue'>待审核</font>";
		}
	}


	function formatterKeys(val, row){
		var str = ""; 
	    return str;
	}
 
	function reloadGrid(){
		jQuery('#mydatagrid').datagrid({
			queryParams: {
				tableId: '${tableId}',
				topId: '${topId}',
				parentId: jQuery("#nodeId").val()
			}
		});
	}


	function resize(){
		jQuery('#mydatagrid').datagrid('resize', {
			width:800,
			height:400
		});
	}


	function getSelected(){
	    var selected = jQuery('#mydatagrid').datagrid('getSelected');
	    if (selected){
		    alert(selected.code+":"+selected.name+":"+selected.addr+":"+selected.col4);
	    }
	}

	function getSelections(){
	    var ids = [];
	    var rows = jQuery('#mydatagrid').datagrid('getSelections');
	    for(var i=0;i<rows.length;i++){
		    ids.push(rows[i].code);
	    }
	    alert(ids.join(':'));
	}

	function clearSelections(){
	    jQuery('#mydatagrid').datagrid('clearSelections');
	}

	function loadGridData(url){
	    jQuery.ajax({
			type: "POST",
			url:  url,
			dataType:  'json',
			error: function(data){
				alert('服务器处理错误！');
			},
			success: function(data){
				jQuery('#mydatagrid').datagrid('loadData', data);
			}
		});
	}

 
    function exportXls(){
        window.open("${contextPath}/matrix/tableInputData/exportXls?tableId=${tableId}&topId=${topId}");
    }

	function searchData(){
       document.iForm.submit();
	}

</script>
</head>
<body>
<input type="hidden" id="tableId" name="tableId" value="${tableId}" >
<input type="hidden" id="parentId" name="parentId" value="${parentId}" >
<input type="hidden" id="nodeId" name="nodeId" value="${nodeId}" > 
<input type="hidden" id="topId" name="topId" value="${topId}" >
<div class="easyui-layout" data-options="fit:true"> 
   <#if table.treeFlag == "Y">
    <div data-options="region:'west',split:true" style="width:240px;">
	  <div class="easyui-layout" data-options="fit:true">  
			 <div data-options="region:'center',border:false">
			    <ul id="myTree" class="ztree"></ul>  
			 </div> 
        </div>  
	</div> 
   </#if>
    <div data-options="region:'center'"> 
	 <div class="easyui-layout" data-options="fit:true">  
	  <div data-options="region:'north', split:false, border:true" style="height:42px" class="toolbar-backgroud"> 
	    <div> 
		  <form id="iForm" name="iForm" method="post" action="">
			<table>
			 <tr>
			   <td colspan="10">
				<div style="margin-top:2px;height:30px"> 
				&nbsp;<img src="${contextPath}/static/images/window.png">
				&nbsp;<span class="x_content_title">${table.title}</span>
				</div> 
			   </td>
			   </tr> 
			 </table>
			</form>
		  </div> 
	    </div> 
        <div data-options="region:'center',border:true">
		  <table id="mydatagrid"></table>
	  </div>  
	</div>
  </div>
</div>
</body>
</html>
