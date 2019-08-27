$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'exam/examintegralcount/list',
        datatype: "json",
        colModel: [
            { label: '', name: 'userId', width: 20, hidden: true,key: true  },
			{ label: '姓名', name: 'userName', index: 'user_name', width: 80 },
			{ label: '总积分', name: 'integralCount', index: 'integral_count', width: 80 },
			{ label: '最后答题时间', name: 'updateTime', index: 'update_time', width: 80, sortable: false }
        ],
		viewrecords: true,
        height: 385,
        rowNum: 10,
		rowList : [10,30,50],
        rownumbers: true, 
        rownumWidth: 25, 
        autowidth:true,
        multiselect: true,
        pager: "#jqGridPager",
        jsonReader : {
            root: "page.list",
            page: "page.currPage",
            total: "page.totalPage",
            records: "page.totalCount"
        },
        prmNames : {
            page:"page", 
            rows:"limit", 
            order: "order"
        },
        gridComplete:function(){
        	//隐藏grid底部滚动条
        	$("#jqGrid").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" }); 
        }
    });

    $("#jqGrid1").jqGrid({
        url: baseURL + 'exam/examintegraldetails/list',
        postData: {"userId":0},
        datatype: "json",
        colModel: [
            { label: '用户姓名', name: 'userName', index: 'user_name', width: 80 },
            { label: '积分描述', name: 'content', index: 'content', width: 80 },
            { label: '正确数', name: 'rightNum', index: 'right_num', width: 80 },
            { label: '总题数', name: 'count', index: 'count', width: 80 },
            { label: '积分', name: 'integral', index: 'integral', width: 80 },
            { label: '创建时间 ', name: 'createTime', index: 'create_time', width: 80 }
        ],
        viewrecords: true,
        height: 385,
        rowNum: 10,
        rowList : [10,30,50],
        rownumbers: true,
        rownumWidth: 25,
        autowidth:true,
        multiselect: true,
        pager: "#jqGridPager1",
        jsonReader : {
            root: "page.list",
            page: "page.currPage",
            total: "page.totalPage",
            records: "page.totalCount"
        },
        prmNames : {
            page:"page",
            rows:"limit",
            order: "order"
        },
        gridComplete:function(){
            //隐藏grid底部滚动条
            $("#jqGrid1").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" });
        }
    });
});

var vm = new Vue({
	el:'#rrapp',
	data:{
		showList: true,
		title: null,
		examIntegralCount: {}
	},
	methods: {
		query: function () {
			vm.reload();
		},
        query1: function (event) {
            var id = getSelectedRow();
            vm.showList = false;
            var page = $("#jqGrid1").jqGrid('getGridParam','page');
            $("#jqGrid1").jqGrid('setGridParam',{
                page:page,
                postData: {'userId':id}
            }).trigger("reloadGrid");
        },
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.examIntegralCount = {};
		},
		update: function (event) {
			var id = getSelectedRow();
			if(id == null){
				return ;
			}
			vm.showList = false;
            vm.title = "修改";
            
            vm.getInfo(id)
		},
		saveOrUpdate: function (event) {
		    $('#btnSaveOrUpdate').button('loading').delay(1000).queue(function() {
                var url = vm.examIntegralCount.id == null ? "exam/examintegralcount/save" : "exam/examintegralcount/update";
                $.ajax({
                    type: "POST",
                    url: baseURL + url,
                    contentType: "application/json",
                    data: JSON.stringify(vm.examIntegralCount),
                    success: function(r){
                        if(r.code === 0){
                             layer.msg("操作成功", {icon: 1});
                             vm.reload();
                             $('#btnSaveOrUpdate').button('reset');
                             $('#btnSaveOrUpdate').dequeue();
                        }else{
                            layer.alert(r.msg);
                            $('#btnSaveOrUpdate').button('reset');
                            $('#btnSaveOrUpdate').dequeue();
                        }
                    }
                });
			});
		},
		del: function (event) {
			var ids = getSelectedRows();
			if(ids == null){
				return ;
			}
			var lock = false;
            layer.confirm('确定要删除选中的记录？', {
                btn: ['确定','取消'] //按钮
            }, function(){
               if(!lock) {
                    lock = true;
		            $.ajax({
                        type: "POST",
                        url: baseURL + "exam/examintegralcount/delete",
                        contentType: "application/json",
                        data: JSON.stringify(ids),
                        success: function(r){
                            if(r.code == 0){
                                layer.msg("操作成功", {icon: 1});
                                $("#jqGrid").trigger("reloadGrid");
                            }else{
                                layer.alert(r.msg);
                            }
                        }
				    });
			    }
             }, function(){
             });
		},
		getInfo: function(id){
			$.get(baseURL + "exam/examintegralcount/info/"+id, function(r){
                vm.examIntegralCount = r.examIntegralCount;
            });
		},
		reload: function (event) {
			vm.showList = true;
			var page = $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{ 
                page:page
            }).trigger("reloadGrid");
		}
	}
});