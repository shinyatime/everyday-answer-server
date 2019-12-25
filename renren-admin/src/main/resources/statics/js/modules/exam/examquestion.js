$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'exam/examquestion/list',
        datatype: "json",
        colModel: [
			{ label: '题目类型', name: 'type', index: 'type', width: 25 , formatter: function(value, options, row){
			        var html = "";
			        if (value === '1' ) {
                        html = "<span class=\"label label-success\">单选题</span>";
                    } else if (value === '2' ) {
                        html = "<span class=\"label label-success\">多选题</span>";
                    } else if (value === '3' ) {
                        html = "<span class=\"label label-success\">填空题</span>";
                    }
                    return html;
                }},
            { label: '分类', name: 'tag', index: 'tag', width: 25 },
			{ label: '题目', name: 'stem', index: 'stem', width: 150 },
			{ label: '备选答案', name: 'metas', index: 'metas', width: 100, formatter: function(value, options, row){
			    var html = '';
			    if (row.type === '3') {
                    html = '无'
                } else {
                    var arr = value.split("@@");
                    for (var i = 0; i < arr.length; i++) {
                        html += choose[i] + '.' + arr[i] + "\n";
                    }
                }
			    return html;
                }},
			{ label: '分数', name: 'score', index: 'score', width: 25 },
			{ label: '正确答案', name: 'answer', index: 'answer', width: 25 },
			{ label: '解析', name: 'analysis', index: 'analysis', width: 100 },
			{ label: '创建时间', name: 'createTime', index: 'create_time', width: 25 },
			{ label: '创建人', name: 'createUser', index: 'create_user', width: 25 },
			{ label: '状态', name: 'status', index: 'status', width: 20, formatter: function(value, options, row){
                    return value === '0' ?
                        '<span class="label label-danger">禁用</span>' :
                        '<span class="label label-success">正常</span>';
                }
			}
        ],
		viewrecords: true,
        height: 600,
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

    $('#crowd_file').on("change",function(){
        var formData = new FormData();
        formData.append("file",$('#crowd_file')[0].files[0]);
        $.ajax({
            url:baseURL + 'exam/examquestion/import',
            dataType:'json',
            type:'POST',
            async: false,
            data: formData,
            processData : false, // 使数据不做处理
            contentType : false, // 不要设置Content-Type请求头
            success: function(data){
                if (data.code == 0) {
                    alert('上传成功！');
                    $("#jqGrid").trigger("reloadGrid");
                    emptyFileUpload($('#crowd_file'))
                } else {
                    alert(data.msg);
                }

            },
            error:function(response){
                console.log(response);
            }
        });

    });
});

function imp() {
    $('#crowd_file').trigger('click');
}

function catchq() {
    $.ajax({
        type: "GET",
        url: baseURL + "exam/examquestion/catchQuestion",
        success: function(r){
            if(r.code === 0){
                layer.msg("操作成功", {icon: 1});
            }else{
                layer.alert(r.msg);
            }
        }
    });
}

function emptyFileUpload(selector) {
    var fi;
    var sourceParent;

    if (selector) {
        fi = $(selector);
        sourceParent = fi.parent();
    }
    else {
        return;
    }

    $("<form id='tempForm'></form>").appendTo(document.body);

    var tempForm = $("#tempForm");

    tempForm.append(fi);
    tempForm.get(0).reset();

    sourceParent.append(fi);
    tempForm.remove();
}


var choose = ['A','B','C','D','E','F','G','H','I','J','K','L','M','N'];
var vm = new Vue({
	el:'#rrapp',
	data:{
		showList: true,
		title: null,
        choose: choose,
        showChoose: false,
        metalist:[],
		examQuestion: {},
        isup: false,
        q: {
            stem:null,
            metas:null,
            tag:null
        }
	},
	methods: {
		query: function () {
			vm.reload();
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.examQuestion = {};
			vm.isup = false;
		},
		update: function (event) {
			var id = getSelectedRow();
			if(id == null){
				return ;
			}
			vm.showList = false;
            vm.title = "修改";
            
            vm.getInfo(id)
            vm.isup = true
		},
		saveOrUpdate: function (event) {
		    $('#btnSaveOrUpdate').button('loading').delay(1000).queue(function() {
                var url = vm.examQuestion.id == null ? "exam/examquestion/save" : "exam/examquestion/update";
                if (vm.showChoose) {
                    vm.examQuestion.metas = vm.metalist.join("@@");
                } else {
                    vm.examQuestion.metas = "";
                }

                $.ajax({
                    type: "POST",
                    url: baseURL + url,
                    contentType: "application/json",
                    data: JSON.stringify(vm.examQuestion),
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
                        url: baseURL + "exam/examquestion/delete",
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
        restore: function(event) {
            var ids = getSelectedRows();
            if(ids == null){
                return ;
            }
            var lock = false;
            layer.confirm('确定要恢复选中的记录？', {
                btn: ['确定','取消'] //按钮
            }, function(){
                if(!lock) {
                    lock = true;
                    $.ajax({
                        type: "POST",
                        url: baseURL + "exam/examquestion/restore",
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
			$.get(baseURL + "exam/examquestion/info/"+id, function(r){
                vm.examQuestion = r.examQuestion;
                vm.changetype();
                if (vm.examQuestion.metas != '') {
                    vm.metalist = vm.examQuestion.metas.split("@@");
                }
            });
		},
		reload: function (event) {
			vm.showList = true;
			var page = $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{
                postData:{'stem': vm.q.stem,'metas': vm.q.metas,'tag': vm.q.tag},
                page:page
            }).trigger("reloadGrid");
		},
        changetype: function () {
            if (vm.examQuestion.type == '1') {
                vm.showChoose = true;
            } else if (vm.examQuestion.type == '2') {
                vm.showChoose = true;
            } else if (vm.examQuestion.type == '3') {
                vm.showChoose = false;
            }
        },
        addmeta: function () {
            vm.metalist.push('');
        },
        delmeta: function (index) {
            var temmetalist = vm.metalist;
            vm.metalist=[];
            console.log(vm.metalist)
            for(var i=0;i<temmetalist.length;i++){
                if(i!=index){
                    vm.metalist.push(temmetalist[i]);
                }
            }
        }
	}
});