$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'exam/examintegraldetails/list',
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

    //console.log(laydate.render)
    //常规用法
    laydate.render({
        elem: '#startTime'
    });
    laydate.render({
        elem: '#endTime'
    });
});



var vm = new Vue({
	el:'#rrapp',
	data:{
		showList: true,
		title: null,
		examIntegralDetails: {},
        q: {
            userName:null,
            startTime:null,
            endTime:null
        }
	},
	methods: {
		query: function () {
			vm.reload();
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.examIntegralDetails = {};
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
                var url = vm.examIntegralDetails.id == null ? "exam/examintegraldetails/save" : "exam/examintegraldetails/update";
                $.ajax({
                    type: "POST",
                    url: baseURL + url,
                    contentType: "application/json",
                    data: JSON.stringify(vm.examIntegralDetails),
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
                        url: baseURL + "exam/examintegraldetails/delete",
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
			$.get(baseURL + "exam/examintegraldetails/info/"+id, function(r){
                vm.examIntegralDetails = r.examIntegralDetails;
            });
		},
		reload: function (event) {
			vm.showList = true;
			var startTime = $("#startTime").val();
            var endTime = $("#endTime").val();
			var page = $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{
                postData:{'userName': vm.q.userName,'startTime': startTime,'endTime': endTime},
                page:page
            }).trigger("reloadGrid");
		}
	}
});