//基本信息
$("#submitBasicInfo").click(function (){
    //一般
    let high = $("#high").val();let weight = $("#weight").val();
    let left_sight = $("#eyesightL").val();let right_sight = $("#eyesightR").val();
    //吸烟
    let smoke_rate = $("#smoke_rate").val();let smokeBegin = $("#smokeBegin").val();
    let smokeEnd = $("#smokeEnd").val();
    //饮酒
    let alcohol_rate = $("#alcohol_rate").val();let alcohol_csp = $("#alcohol_csp").val();
    let alcoholBegin = $("#alcoholBegin").val();let alcoholEnd = $("#alcoholEnd").val();
    //锻炼
    let physical_rate = $("#physical_rate").val();let physical_time = $("#physical_time").val();
    let physical_way = $("#physical_way").val();

    $.ajax({
        url:"/patient/saveHealthBasicInfo",
        type:"post",
        dataType:"json",
        data:{"healthBasicInfo":JSON.stringify({"normal":JSON.stringify({"high":high,"weight":weight,"left_sight":left_sight,"right_sight":right_sight}),
              "smoke":JSON.stringify({"smoke_rate":smoke_rate,"beginTime":smokeBegin,"endTime":smokeEnd}),
              "alcohol":JSON.stringify({"alcohol_rate":alcohol_rate,"alcohol_consumption":alcohol_csp,"begin_time":alcoholBegin,"endTime":alcoholEnd}),
              "physical":JSON.stringify({"physical_rate":physical_rate,"physical_time":physical_time,"physical_way":physical_way})
        })},
        success:function (data) {
            if (data.code===-1){
                Notiflix.Notify.Failure(data.message);
            }
            else {
                Notiflix.Notify.Success(data.message);
            }
        },
        error:function () {
            Notiflix.Notify.Failure("请求失败");
        }

    });
});


//疾病履历
function addDiseaseHtml(record) {
    return '<tr id="disease'+ record.id+'">\n' +
        '         <td><span class="d-block text-info">' + record.time + '</span></td>\n' +
        '         <td><span class="d-block text-info">' + record.status + '</span></td>\n'+
        '         <td class="text-right">\n' +
        '           <div class="table-action">\n' +
        '             <a id="'+ record.id +'" onclick="showDetailDisease(this)" class="btn btn-sm bg-success-light" data-toggle="modal" data-target="#addDivDisease">\n' +
        '               <i class="far fa-eye"></i> View\n' +
        '             </a>\n' +
        '             <a onclick="editDisease(this)" id="edit'+ record.id +'" class="btn btn-sm bg-success-light" data-toggle="modal" data-target="#addDivDisease">\n' +
        '               <i class="fas fa-edit"></i> Edit\n' +
        '             </a>\n' +
        '             <a onclick="deleteDisease(this)" id="delete'+ record.id+'" class="btn btn-sm bg-danger-light">\n' +
        '               <i class="far fa-trash-alt"></i> Delete\n' +
        '             </a>'+
        '           </div>\n' +
        '         </td>\n' +
        '   </tr>';
}

/**
 * 显示所有resume
 * @param recordIdMap resume类型映射该类型的所有resume->id映射resume
 */
function showRecords(recordIdMap) {
    //三种添加表项函数
    let func = ["addDiseaseHtml","addInjuryHtml","addOperationHtml"];
    for (let i = 0;i < types.length;i++){
        for (let id in recordIdMap[types[i]]){
            let body = "#"+types[i]+"Body";
            $(body).append(eval(func[i])(recordIdMap[types[i]][id]));
            //初始化view、delete、edit的value为id
            initEditViewDeleteResume(id);
        }
    }

}

/**
 * 显示resume
 * @param obj
 */
function showDetailDisease(obj) {
    editorDisease.txt.clear();
    $("#confirmDisease").hide();
    $("#cancelDisease").hide();
    let id = parseInt(obj.getAttribute("value"));
    let submitDisease = $("#submitDisease");
    //提交按钮的值为该类resume的类型
    let type = submitDisease.val();
    submitDisease.hide();
    $("#time_disease").val(recordTypeIdMap[type][id].time).prop("disabled",true);
    $("#status").val(recordTypeIdMap[type][id].status).prop("disabled",true);
    editorDisease.txt.html(recordTypeIdMap[type][id].health_describe);
    editorDisease.disable();
}

function initEditViewDeleteResume(id) {
    let edit = "#edit" + id;
    $(edit).attr("value",id);
    let deleteDisease = "#delete" + id;
    $(deleteDisease).attr("value",id);
    let view = "#" + id;
    $(view).attr("value",id);
}

$("#addDisease").click(function (){
    editorDisease.txt.clear();
    editorDisease.enable();
    $("#time_disease").val("").prop("disabled",false);
    $("#status").val("").prop("disabled",false);
    $("#submitDisease").hide();
    $("#confirmDisease").show();
});

$('#confirmDisease').click(function (){
    let time = $("#time_disease").val();let status = $("#status").val();
    let health_type = $("#submitDisease").val();let health_describe = editorDisease.txt.html();
    console.log(time);
    if (time.length!==0){
        $.ajax({
            url: "/patient/addResume",
            type: "post",
            dataType: "json",
            data: {"resumeInfo":JSON.stringify({"time":time,"status":status,
                    "health_type":health_type,"health_describe":health_describe})
            },
            success: function (data) {
                if (parseInt(data.code)===-1){
                    Notiflix.Notify.Failure(data.message);
                }
                else {
                    Notiflix.Notify.Success("添加成功");
                    let resume = JSON.parse(data.message);
                    let new_id = resume.id;
                    let obj = {};
                    obj[new_id] = resume;
                    Object.assign(recordTypeIdMap[$("#submitDisease").val()],obj);
                    $("#diseaseBody").append(addDiseaseHtml(resume));
                    initEditViewDeleteResume(new_id);
                    sendResumeMsg(new_id,1,health_type,resume);
                }
            }
        });
    }
    else {
        Notiflix.Notify.Failure("时间需填写");
    }
});

$('#submitDisease').click(function (){
    let time = $("#time_disease").val();let status = $("#status").val();
    let health_type = $("#submitDisease").val();let health_describe = editorDisease.txt.html();
    if (time.length===0){
        Notiflix.Notify.Failure("时间需填写");
    }
    else {
        $.ajax({
            url: "/patient/updateResume",
            type: "post",
            dataType: "json",
            data: {"id":nowResumeDeal_id,"resumeInfo":JSON.stringify({"time":time,"status":status,
                    "health_type":health_type,"health_describe":health_describe})
            },
            success: function (data) {
                if (parseInt(data.code)===-1){
                    Notiflix.Notify.Failure(data.message);
                }
                else {
                    Notiflix.Notify.Success("修改成功");
                    let resume = JSON.parse(data.message);
                    recordTypeIdMap[$("#submitDisease").val()][parseInt(resume.id)] = resume;
                    let disease = "#disease" + nowResumeDeal_id;
                    updateDisease(disease,resume);
                    sendResumeMsg(resume.id,0,health_type,resume);
                }
            }
        });
    }
});

function editDisease(obj) {
    $("#confirmDisease").hide();
    //id
    let id = parseInt(obj.getAttribute("value"));
    let submitDisease = $("#submitDisease");
    submitDisease.show();
    $("#cancelDisease").show();
    let type = submitDisease.val();
    nowResumeDeal_id = id;
    $("#time_disease").val(recordTypeIdMap[type][id].time).prop("disabled",false);
    $("#status").val(recordTypeIdMap[type][id].status).prop("disabled",false);
    editorDisease.txt.html(recordTypeIdMap[type][id].health_describe);
    editorDisease.enable();
}

function deleteDisease(obj) {
    let id = parseInt(obj.getAttribute("value"));
    let health_type = $("#submitDisease").val();
    //tr
    let disease = "#disease" + id;
    $(disease).remove();
    $.ajax({
        url:"/patient/deleteResume",
        type:"post",
        dataType:"json",
        data:{"id":id},
        success:function (data) {
            if (data.code===1){
                Notiflix.Notify.Success("删除成功");
                sendResumeMsg(id,-1,health_type);
            }
            else {
                Notiflix.Notify.Failure("删除失败");
            }
        }
    });
}
function updateDisease(id,resume) {
    let td = $(id).children("td");
    td.eq(0).children("span").text(resume.time);
    td.eq(1).children("span").text(resume.status);
}


//Injury
function addInjuryHtml(record) {
    return '<tr id="injury'+ record.id+'">\n' +
        '         <td><span class="d-block text-info">' + record.time + '</span></td>\n' +
        '         <td class="text-right">\n' +
        '           <div class="table-action">\n' +
        '             <a id="'+ record.id +'" onclick="showDetailInjury(this)" class="btn btn-sm bg-success-light" data-toggle="modal" data-target="#addDivInjury">\n' +
        '               <i class="far fa-eye"></i> View\n' +
        '             </a>\n' +
        '             <a onclick="editInjury(this)" id="edit'+ record.id +'" class="btn btn-sm bg-success-light" data-toggle="modal" data-target="#addDivInjury">\n' +
        '               <i class="fas fa-edit"></i> Edit\n' +
        '             </a>\n' +
        '             <a onclick="deleteInjury(this)" id="delete'+ record.id+'" class="btn btn-sm bg-danger-light">\n' +
        '               <i class="far fa-trash-alt"></i> Delete\n' +
        '             </a>'+
        '           </div>\n' +
        '         </td>\n' +
        '   </tr>';
}

function showDetailInjury(obj) {
    editorInjury.txt.clear();
    $("#confirmInjury").hide();
    $("#cancelInjury").hide();
    let id = parseInt(obj.getAttribute("value"));
    let submitInjury = $("#submitInjury");
    let type = submitInjury.val();
    submitInjury.hide();
    $("#time_injury").val(recordTypeIdMap[type][id].time).prop("disabled",true);
    editorInjury.txt.html(recordTypeIdMap[type][id].health_describe);
    editorInjury.disable();
}

$("#addInjury").click(function (){
    editorInjury.txt.clear();
    editorInjury.enable();
    $("#time_injury").val("").prop("disabled",false);
    $("#submitInjury").hide();
});

$('#confirmInjury').click(function (){
    let time = $("#time_injury").val();
    let health_type = $("#submitInjury").val();let health_describe = editorInjury.txt.html();
    if (time.length!==0){
        $.ajax({
            url: "/patient/addResume",
            type: "post",
            dataType: "json",
            data: {"resumeInfo":JSON.stringify({"time":time,"status":"",
                    "health_type":health_type,"health_describe":health_describe})
            },
            success: function (data) {
                if (parseInt(data.code)===-1){
                    Notiflix.Notify.Failure(data.message);
                }
                else {
                    Notiflix.Notify.Success("添加成功");
                    let resume = JSON.parse(data.message);
                    let new_id = resume.id;
                    let obj = {};
                    obj[new_id] = resume;
                    Object.assign(recordTypeIdMap[$("#submitInjury").val()],obj);
                    $("#injuryBody").append(addInjuryHtml(resume));
                    console.log(recordTypeIdMap);
                    initEditViewDeleteResume(new_id);
                    sendResumeMsg(new_id,1,health_type,resume);
                }
            }
        });
    }
    else {
        Notiflix.Notify.Failure("时间需填写");
    }
});

$('#submitInjury').click(function (){
    let time = $("#time_injury").val();
    let health_type = $("#submitInjury").val();let health_describe = editorInjury.txt.html();
    if (time.length===0){
        Notiflix.Notify.Failure("时间需填写");
    }
    else {
        $.ajax({
            url: "/patient/updateResume",
            type: "post",
            dataType: "json",
            data: {"id":nowResumeDeal_id,"resumeInfo":JSON.stringify({"time":time,"status":"",
                    "health_type":health_type,"health_describe":health_describe})
            },
            success: function (data) {
                if (parseInt(data.code)===-1){
                    Notiflix.Notify.Failure(data.message);
                }
                else {
                    Notiflix.Notify.Success("修改成功");
                    let resume = JSON.parse(data.message);
                    recordTypeIdMap[$("#submitInjury").val()][parseInt(resume.id)] = resume;
                    let injury = "#injury" + nowResumeDeal_id;
                    updateInjury(injury,resume);
                    sendResumeMsg(resume.id,0,health_type,resume);
                }
            }
        });
    }
});

function editInjury(obj) {
    $("#confirmInjury").hide();
    let id = parseInt(obj.getAttribute("value"));
    let submitInjury = $("#submitInjury");
    submitInjury.show();
    $("#cancelInjury").show();
    let type = submitInjury.val();
    nowResumeDeal_id = id;
    $("#time_injury").val(recordTypeIdMap[type][id].time).prop("disabled",false);
    editorInjury.txt.html(recordTypeIdMap[type][id].health_describe);
    editorInjury.enable();
}

function deleteInjury(obj) {
    let id = parseInt(obj.getAttribute("value"));
    let injury = "#injury" + id;
    $(injury).remove();
    let health_type = $("#submitInjury").val();
    $.ajax({
        url:"/patient/deleteResume",
        type:"post",
        dataType:"json",
        data:{"id":id},
        success:function (data) {
            if (data.code===1){
                Notiflix.Notify.Success("删除成功");
                sendResumeMsg(id,-1,health_type);
            }
            else {
                Notiflix.Notify.Failure("删除失败");
            }
        }
    });
}
function updateInjury(id,resume) {
    let td = $(id).children("td");
    td.eq(0).children("span").text(resume.time);
}



//Operation operation
function addOperationHtml(record) {
    return '<tr id="operation'+ record.id+'">\n' +
        '         <td><span class="d-block text-info">' + record.time + '</span></td>\n' +
        '         <td class="text-right">\n' +
        '           <div class="table-action">\n' +
        '             <a id="'+ record.id +'" onclick="showDetailOperation(this)" class="btn btn-sm bg-success-light" data-toggle="modal" data-target="#addDivOperation">\n' +
        '               <i class="far fa-eye"></i> View\n' +
        '             </a>\n' +
        '             <a onclick="editOperation(this)" id="edit'+ record.id +'" class="btn btn-sm bg-success-light" data-toggle="modal" data-target="#addDivOperation">\n' +
        '               <i class="fas fa-edit"></i> Edit\n' +
        '             </a>\n' +
        '             <a onclick="deleteOperation(this)" id="delete'+ record.id+'" class="btn btn-sm bg-danger-light">\n' +
        '               <i class="far fa-trash-alt"></i> Delete\n' +
        '             </a>'+
        '           </div>\n' +
        '         </td>\n' +
        '   </tr>';
}

function showDetailOperation(obj) {
    editorOperation.txt.clear();
    $("#confirmOperation").hide();
    $("#cancelOperation").hide();
    let id = parseInt(obj.getAttribute("value"));
    let submitOperation = $("#submitOperation");
    let type = submitOperation.val();
    submitOperation.hide();
    $("#time_operation").val(recordTypeIdMap[type][id].time).prop("disabled",true);
    editorOperation.txt.html(recordTypeIdMap[type][id].health_describe);
    editorOperation.disable();
}

$("#addOperation").click(function (){
    editorOperation.txt.clear();
    editorOperation.enable();
    $("#time_operation").val("").prop("disabled",false);
    $("#submitOperation").hide();
});

$('#confirmOperation').click(function (){
    let time = $("#time_operation").val();
    let health_type = $("#submitOperation").val();let health_describe = editorOperation.txt.html();
    if (time.length!==0){
        $.ajax({
            url: "/patient/addResume",
            type: "post",
            dataType: "json",
            data: {"resumeInfo":JSON.stringify({"time":time,"status":"",
                    "health_type":health_type,"health_describe":health_describe})
            },
            success: function (data) {
                if (parseInt(data.code)===-1){
                    Notiflix.Notify.Failure(data.message);
                }
                else {
                    Notiflix.Notify.Success("添加成功");
                    let resume = JSON.parse(data.message);
                    let new_id = resume.id;
                    let obj = {};
                    obj[new_id] = resume;
                    Object.assign(recordTypeIdMap[$("#submitOperation").val()],obj);
                    $("#operationBody").append(addOperationHtml(resume));
                    initEditViewDeleteResume(new_id);
                    sendResumeMsg(new_id,1,health_type,resume);
                }
            }
        });
    }
    else {
        Notiflix.Notify.Failure("时间需填写");
    }
});

$('#submitOperation').click(function (){
    let time = $("#time_operation").val();
    let health_type = $("#submitOperation").val();let health_describe = editorOperation.txt.html();
    if (time.length===0){
        Notiflix.Notify.Failure("时间需填写");
    }
    else {
        $.ajax({
            url: "/patient/updateResume",
            type: "post",
            dataType: "json",
            data: {"id":nowResumeDeal_id,"resumeInfo":JSON.stringify({"time":time,"status":"",
                    "health_type":health_type,"health_describe":health_describe})
            },
            success: function (data) {
                if (parseInt(data.code)===-1){
                    Notiflix.Notify.Failure(data.message);
                }
                else {
                    Notiflix.Notify.Success("修改成功");
                    let resume = JSON.parse(data.message);
                    recordTypeIdMap[$("#submitOperation").val()][parseInt(resume.id)] = resume;
                    let operation = "#operation" + nowResumeDeal_id;
                    updateOperation(operation,resume);
                    sendResumeMsg(resume.id,0,health_type,resume);
                }
            }
        });
    }
});

function editOperation(obj) {
    $("#confirmOperation").hide();
    let id = parseInt(obj.getAttribute("value"));
    let submitOperation = $("#submitOperation");
    submitOperation.show();
    $("#cancelOperation").show();
    let type = submitOperation.val();
    nowResumeDeal_id = id;
    $("#time_operation").val(recordTypeIdMap[type][id].time).prop("disabled",false);
    editorOperation.txt.html(recordTypeIdMap[type][id].health_describe);
    editorOperation.enable();
}

function deleteOperation(obj) {
    let id = parseInt(obj.getAttribute("value"));
    let operation = "#operation" + id;
    $(operation).remove();
    let health_type = $("#submitOperation").val();
    $.ajax({
        url:"/patient/deleteResume",
        type:"post",
        dataType:"json",
        data:{"id":id},
        success:function (data) {
            if (data.code===1){
                Notiflix.Notify.Success("删除成功");
                sendResumeMsg(id,-1,health_type);
            }
            else {
                Notiflix.Notify.Failure("删除失败");
            }
        }
    });
}
function updateOperation(id,resume) {
    let td = $(id).children("td");
    td.eq(0).children("span").text(resume.time);
}


//exam
function addExamHtml(exam) {
    let time = timeDeal(exam.time,8,false);
    return '<tr id="exam'+ exam.id+'">\n' +
        '         <td><span class="d-block text-info">' + exam.option_exam + '</span></td>\n' +
        '         <td><span class="d-block text-info">' + exam.value_exam + '</span></td>\n' +
        '         <td><span class="d-block text-info">' + time + '</span></td>\n' +
        '         <td><span class="d-block text-info">' + exam.hospital + '</span></td>\n' +
        '         <td class="text-right">\n' +
        '           <div class="table-action">\n' +
        '             <a id="viewExam'+ exam.id +'" onclick="showDetailExam(this)" class="btn btn-sm bg-success-light" data-toggle="modal" data-target="#addDivExam">\n' +
        '               <i class="far fa-eye"></i> View\n' +
        '             </a>\n' +
        '             <a onclick="editExam(this)" id="editExam'+ exam.id +'" class="btn btn-sm bg-success-light" data-toggle="modal" data-target="#addDivExam">\n' +
        '               <i class="fas fa-edit"></i> Edit\n' +
        '             </a>\n' +
        '             <a onclick="deleteExam(this)" id="deleteExam'+ exam.id+'" class="btn btn-sm bg-danger-light">\n' +
        '               <i class="far fa-trash-alt"></i> Delete\n' +
        '             </a>'+
        '           </div>\n' +
        '         </td>\n' +
        '   </tr>';
}
function showExams(examInfosIdMap) {
    for (let id in examInfosIdMap){
        $("#examBody").append(addExamHtml(examInfosIdMap[id]));
        initExamViewDelEditVal(id);
    }
}
function initExamViewDelEditVal(id) {
    let viewExam = "#viewExam" + id;
    let editExam = "#editExam" + id;
    let deleteExam = "#deleteExam" + id;
    $(viewExam).attr("value",id);$(editExam).attr("value",id);$(deleteExam).attr("value",id);
}
function showDetailExam(obj) {
    editorExam.txt.clear();
    let submitExam = $("#submitExam");
    submitExam.hide();
    $("#confirmExam").hide();
    $("#cancelExam").hide();
    let id = parseInt(obj.getAttribute("value"));
    $("#time_exam").val(timeDeal(examInfosIdMap[id].time,5,true)).prop("disabled",true);
    $("#hospital_exam").val(examInfosIdMap[id].hospital).prop("disabled",true);
    $("#option_exam").val(examInfosIdMap[id].option_exam).prop("disabled",true);
    $("#value_exam").val(examInfosIdMap[id].value_exam).prop("disabled",true);
    let exam_image = $("#exam_image");let exam_otherFile = $("#exam_otherFile");
    $("#examOtherUpload").prop("disabled",true);$("#examImageUpload").prop("disabled",true);
    $(exam_image).attr("href",examInfosIdMap[id].image_url);
    $(exam_image).text("图片文件链接");
    $(exam_otherFile).attr("href",examInfosIdMap[id].file_url);
    $(exam_otherFile).text("其他文件链接");
    let begin = examInfosIdMap[id].file_url.lastIndexOf("/");
    let length = examInfosIdMap[id].file_url.length;
    $(exam_otherFile).attr("download",examInfosIdMap[id].file_url.substr(begin,length));
    editorExam.txt.html(examInfosIdMap[id].description);
    editorExam.disable();
}

$("#addExam").click(function (){
    editorExam.txt.clear();
    editorExam.enable();
    $("#time_exam").val("").prop("disabled",false);$("#option_exam").val("").prop("disabled",false);
    $("#value_exam").val("").prop("disabled",false);$("#hospital_exam").val("").prop("disabled",false);
    $("#exam_image").val("").prop("disabled",false);$("#exam_otherFile").val("").prop("disabled",false);
    $("#submitExam").hide();$("#cancelExam").show();
    $("#confirmExam").show();
});

function examFormData() {
    let time_exam = $("#time_exam");
    if($(time_exam).val().length===0){
        return false;
    }
    let time = timeDeal($(time_exam).val() + ":00",8,false); let option_exam = $("#option_exam").val();
    let value_exam = $("#value_exam").val(); let hospital = $("#hospital_exam").val();
    let image = $("input#examImageUpload")[0]; let file = $("input#examOtherUpload")[0];
    let image_url;let file_url;
    if (image.files.length===0){image_url = $("#exam_image").attr("href");}
    if (file.files.length===0){file_url = $("#exam_image").attr("href");}
    let description = editorExam.txt.html();
    let formData = new FormData();
    formData.append("examInfo",JSON.stringify({"time":time,"option_exam":option_exam,"value_exam":value_exam,
        "hospital":hospital,"description":description,"image_url":image_url,"file_url":file_url}));
    formData.append("image",image.files[0]);formData.append("otherFile",file.files[0]);
    return formData;
}

$('#confirmExam').click(function (){
    let formData = examFormData();
    if (formData){
        $.ajax({
            url: "/patient/addExam",
            type: "post",
            dataType: "json",
            data: formData,
            cache: false,
            processData: false,
            contentType: false,
            success: function (data) {
                if (parseInt(data.code)===-1){
                    Notiflix.Notify.Failure(data.message);
                }
                else {
                    Notiflix.Notify.Success("添加成功");
                    let exam = JSON.parse(data.message);
                    let new_id = exam.id;
                    let obj = {};
                    obj[new_id] = exam;
                    Object.assign(examInfosIdMap,obj);
                    $("#examBody").append(addExamHtml(exam));
                    initExamViewDelEditVal(new_id);
                    sendExamMsg(new_id,1,JSON.parse(data.message));
                }
            }
        });
    }
    else {
        Notiflix.Notify.Failure("时间须填写");
    }
});

$('#submitExam').click(function (){
    let formData = examFormData();
    if (formData){
        let id = this.getAttribute("value");
        formData.append("id", id);
        $.ajax({
            url: "/patient/updateExam",
            type: "post",
            dataType: "json",
            data: formData,
            cache: false,
            processData: false,
            contentType: false,
            success: function (data) {
                if (parseInt(data.code) === -1) {
                    Notiflix.Notify.Failure(data.message);
                } else {
                    Notiflix.Notify.Success("修改成功");
                    let exam = JSON.parse(data.message);
                    examInfosIdMap[exam.id] = exam;
                    console.log(exam);
                    let tr = "#exam" + id;
                    updateExam(tr, exam);
                    $("#exam_image").attr("href", exam.image_url);
                    $("#exam_otherFile").attr("href", exam.file_url);
                    sendExamMsg(id,0,JSON.parse(data.message));
                }
            }
        });
    }
    else {
        Notiflix.Notify.Failure("时间须填写");
    }

});

function editExam(obj) {
    editorExam.txt.clear();
    $("#confirmExam").hide();
    let submitExam = $("#submitExam");
    submitExam.show();
    $("#cancelExam").show();

    let id = parseInt(obj.getAttribute("value"));
    submitExam.attr("value",id);
    $("#time_exam").val(timeDeal(examInfosIdMap[id].time,5,true)).prop("disabled",false);
    $("#hospital_exam").val(examInfosIdMap[id].hospital).prop("disabled",false);
    $("#option_exam").val(examInfosIdMap[id].option_exam).prop("disabled",false);
    $("#value_exam").val(examInfosIdMap[id].value_exam).prop("disabled",false);
    let exam_image = $("#exam_image");let exam_otherFile = $("#exam_otherFile");
    $("#examOtherUpload").prop("disabled",false);$("#examImageUpload").prop("disabled",false);
    $(exam_image).attr("href",examInfosIdMap[id].image_url);
    $(exam_image).text("图片文件链接");
    $(exam_otherFile).attr("href",examInfosIdMap[id].file_url);
    $(exam_otherFile).text("其他文件链接");
    let begin = examInfosIdMap[id].file_url.lastIndexOf("/");
    console.log(begin);
    let length = examInfosIdMap[id].file_url.length;
    console.log(length);
    console.log(examInfosIdMap[id].file_url.substr(begin,length));
    $(exam_otherFile).attr("download",examInfosIdMap[id].file_url.substr(begin,length));
    editorExam.txt.html(examInfosIdMap[id].description);
    editorExam.enable();
}

function deleteExam(obj) {
    let id = parseInt(obj.getAttribute("value"));
    let exam = "#exam" + id;
    $(exam).remove();
    $.ajax({
        url:"/patient/deleteExam",
        type:"post",
        dataType:"json",
        data:{"id":id},
        success:function (data) {
            if (data.code===1){
                Notiflix.Notify.Success("删除成功");
                sendExamMsg(id,-1);
            }
            else {
                Notiflix.Notify.Failure("删除失败");
            }
        }
    });
}

function updateExam(id,exam) {
    let td = $(id).children("td");
    td.eq(0).children("span").text(exam.option_exam);
    td.eq(1).children("span").text(exam.value_exam);
    td.eq(2).children("span").text(timeDeal(exam.time,8,false));
    td.eq(3).children("span").text(exam.hospital);
}

function timeDeal(time,last=8,T=false) {
    let exam_time;let result;
    if(time.indexOf("T")!==-1){
        exam_time = time.split("T");
        result = exam_time[0] + " " + exam_time[1].substr(0,last);
        if (T){
            //含有T
            return exam_time[0] + "T" + exam_time[1].substr(0,last);
        }
    }
    else{
        exam_time = time.split(" ");
        result = exam_time[0] + " " + exam_time[1].substr(0,last);
        if (T){
            //含有T
            return exam_time[0] + "T" + exam_time[1].substr(0,last);
        }
    }
    return result;
}

function sendExamMsg(id,code,exam="") {
    websocket.send(JSON.stringify({"type":"exam","id":id,
        "code":code,"patient_email":patient_email,"exam":exam}));
}


function sendResumeMsg(id,code,resumeType,resume="") {
    websocket.send(JSON.stringify({"type":"resume","id":id,
        "code":code,"patient_email":patient_email,"resume":resume,"resume_type":resumeType}));
}
