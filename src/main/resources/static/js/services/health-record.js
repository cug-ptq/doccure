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
        '           </div>\n' +
        '         </td>\n' +
        '   </tr>';
}

function showRecords(recordIdMap) {
    let func = ["addDiseaseHtml","addInjuryHtml","addOperationHtml"];
    for (let i = 0;i < types.length;i++){
        for (let id in recordIdMap[types[i]]){
            let body = "#"+types[i]+"Body";
            $(body).append(eval(func[i])(recordIdMap[types[i]][id]));
            initViewResume(id);
        }
    }

}

function showDetailDisease(obj) {
    editorDisease.txt.clear();
    let id = parseInt(obj.getAttribute("value"));
    let submitDisease = $("#submitDisease");
    let type = submitDisease.val();
    submitDisease.hide();
    $("#time_disease").val(recordTypeIdMap[type][id].time).prop("disabled",true);
    $("#status").val(recordTypeIdMap[type][id].status).prop("disabled",true);
    editorDisease.txt.html(recordTypeIdMap[type][id].health_describe);
    editorDisease.disable();
}

function initViewResume(id) {
    let view = "#" + id;
    $(view).attr("value",id);
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
        '           </div>\n' +
        '         </td>\n' +
        '   </tr>';
}

function showDetailInjury(obj) {
    editorInjury.txt.clear();
    let id = parseInt(obj.getAttribute("value"));
    let submitInjury = $("#submitInjury");
    let type = submitInjury.val();
    submitInjury.hide();
    $("#time_injury").val(recordTypeIdMap[type][id].time).prop("disabled",true);
    editorInjury.txt.html(recordTypeIdMap[type][id].health_describe);
    editorInjury.disable();
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
        '           </div>\n' +
        '         </td>\n' +
        '   </tr>';
}

function showDetailOperation(obj) {
    editorOperation.txt.clear();
    let id = parseInt(obj.getAttribute("value"));
    let submitOperation = $("#submitOperation");
    let type = submitOperation.val();
    submitOperation.hide();
    $("#time_operation").val(recordTypeIdMap[type][id].time).prop("disabled",true);
    editorOperation.txt.html(recordTypeIdMap[type][id].health_describe);
    editorOperation.disable();
}


//exam
function addExamHtml(exam) {
    let time = timeDeal(exam.time,8,false);
    return '<tr id="exam'+ exam.id+'">\n' +
        '         <td><span class="d-block text-info" id="option">' + exam.option_exam + '</span></td>\n' +
        '         <td><span class="d-block text-info">' + exam.value_exam + '</span></td>\n' +
        '         <td><span class="d-block text-info">' + time + '</span></td>\n' +
        '         <td><span class="d-block text-info">' + exam.hospital + '</span></td>\n' +
        '         <td class="text-right">\n' +
        '           <div class="table-action">\n' +
        '             <a id="viewExam'+ exam.id +'" onclick="showDetailExam(this)" class="btn btn-sm bg-success-light" data-toggle="modal" data-target="#addDivExam">\n' +
        '               <i class="far fa-eye"></i> View\n' +
        '             </a>\n' +
        '           </div>\n' +
        '         </td>\n' +
        '   </tr>';
}

function showExams(examInfosIdMap) {
    for (let id in examInfosIdMap){
        $("#examBody").append(addExamHtml(examInfosIdMap[id]));
        initExamViewVal(id);
    }
}
function initExamViewVal(id) {
    let viewExam = "#viewExam" + id;
    $(viewExam).attr("value",id);
}
function showDetailExam(obj) {
    editorExam.txt.clear();
    let submitExam = $("#submitExam");
    submitExam.hide();
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
    editorExam.txt.html(examInfosIdMap[id].description);
    editorExam.disable();
}

function timeDeal(time,last,T) {
    let exam_time;let result;
    if(time.indexOf("T")!==-1){
        exam_time = time.split("T");
        result = exam_time[0] + " " + exam_time[1].substr(0,last);
        if (T){
            return exam_time[0] + "T" + exam_time[1].substr(0,last);
        }
    }
    else{
        result = time;
    }
    return result;
}

$(function (){
   $("#high").prop("disabled",true);
   $("#weight").prop("disabled",true);
   $("#eyesightL").prop("disabled",true);
   $("#eyesightR").prop("disabled",true);
   $("#smoke_rate").prop("disabled",true);
   $("#smokeBegin").prop("disabled",true);
   $("#alcohol_rate").prop("disabled",true);
   $("#alcohol_csp").prop("disabled",true);
   $("#alcoholBegin").prop("disabled",true);
   $("#alcoholEnd").prop("disabled",true);
   $("#physical_rate").prop("disabled",true);
   $("#physical_time").prop("disabled",true);
   $("#physical_way").prop("disabled",true);
});



function receiveExamMsg(message) {
    message = JSON.parse(message);
    if (message.patient_email === patient_email){
        Notiflix.Notify.Info("有体检信息哦!");

        let id = message.id;
        if (message.code===-1){//删除
            let exam = "#exam" + id;
            $(exam).remove();
        }
        else if (message.code===1){//添加
            $("#examBody").prepend(addExamHtml(message.exam));
            let obj = {};obj[message.exam.id] = message.exam;
            Object.assign(examInfosIdMap,obj);
        }
        else if (message.code===0){//更新
            examInfosIdMap[message.exam.id] = message.exam;
            console.log(message.exam)
            let exam = "#exam" + message.exam.id;
            updateExam(exam,message.exam);
        }
    }
}

function receiveResume(message) {
    message = JSON.parse(message);
    if (message.patient_email === patient_email){
        if (message.resume_type === "disease"){
            Notiflix.Notify.Info("有疾病履历信息哦!");
            let body = "#diseaseBody";
            dealResumeMsg(message,body,"disease");
        }
        else if (message.resume_type === "injury"){
            Notiflix.Notify.Info("有外伤履历信息哦!");
            let body = "#injuryBody";
            dealResumeMsg(message,body,"disease");
        }
        else if (message.resume_type === "operation"){
            Notiflix.Notify.Info("有手术履历信息哦!");
            let body = "#operationBody";
            dealResumeMsg(message,body,"disease");
        }
    }
}

function dealResumeMsg(message,body,resumeType) {
    let id = message.id;
    let resume = "#" + resumeType + id;
    if (message.code===-1){//删除
        $(resume).remove();
    }
    else if (message.code===1){//添加
        if (resumeType==="disease"){
            $(body).prepend(addDiseaseHtml(message.resume));
        }
        else if (resumeType==="injury"){
            $(body).prepend(addInjuryHtml(message.resume));
        }
        else if (resumeType==="operation"){
            $(body).prepend(addOperationHtml(message.resume));
        }
        let obj = {};obj[message.resume.id] = message.resume;
        Object.assign(recordTypeIdMap[resumeType],obj);
    }
    else if (message.code===0){//更新
        recordTypeIdMap[resumeType][message.resume.id] = message.resume;
        console.log(message.resume)
        if (resumeType==="disease"){
            updateDisease(resume,message.resume);
        }
        else if (resumeType==="injury"){
            updateInjury(resume,message.resume);
        }
        else if (resumeType==="operation"){
            updateOperation(resume,message.resume);
        }
    }
}

function updateExam(id,exam) {
    let td = $(id).children("td");
    td.eq(0).children("span").text(exam.option_exam);
    td.eq(1).children("span").text(exam.value_exam);
    td.eq(2).children("span").text(timeDeal(exam.time,8,false));
    td.eq(3).children("span").text(exam.hospital);
}

function updateDisease(id,resume) {
    let td = $(id).children("td");
    td.eq(0).children("span").text(resume.time);
    td.eq(1).children("span").text(resume.status);
}

function updateInjury(id,resume) {
    let td = $(id).children("td");
    td.eq(0).children("span").text(resume.time);
}
function updateOperation(id,resume) {
    let td = $(id).children("td");
    td.eq(0).children("span").text(resume.time);
}