function showApplication(applications) {
    for (let i = 0;i < applications.length;i++){
        $("#applyBody").append(addApplication(applications[i]));
        let accept = "#accept" + applications[i].id;
        $(accept).attr("value",applications[i].id);
        let cancel = "#cancel" + applications[i].id;
        $(cancel).attr("value",applications[i].id);
    }
}

function addApplication(application) {
    let time = timeDeal(application.time,5).split(" ");
    return '<tr id="application'+ application.id +'">\n' +
        '      <td>\n' +
        '        <h2 class="table-avatar">\n' +
        '          <a href="'+application.image_url+'" class="avatar avatar-sm mr-2"><img class="avatar-img rounded-circle" src="'+application.image_url+'" alt="User Image"></a>\n' +
        '          <a>'+application.username+'</a>\n' +
        '        </h2>\n' +
        '      </td>\n' +
        '      <td>'+application.speciality+'</td>\n' +
        '      <td>\n' +
        '        <h2 class="table-avatar">\n' +
        '          <a href="'+application.file_url+'" class="avatar avatar-sm mr-2"><img class="avatar-img rounded-circle" src="'+staticFileUrl+'" alt="认证文件"></a>\n' +
        '          <a>认证文件</a>\n' +
        '        </h2>\n' +
        '      </td>\n' +
        '      <td>'+time[0]+'<span class="text-primary d-block">'+time[1]+'</span></td>\n' +
        '      <td>\n' +
        '        <div class="appointment-action">\n' +
        '          <a class="btn btn-sm bg-success-light" onclick="accept(this)" id="accept'+application.id+'">\n' +
        '            <i class="fas fa-check"></i> Accept' +
        '          </a>\n' +
        '          <a class="btn btn-sm bg-danger-light" onclick="cancel(this)" id="cancel'+application.id+'" class="btn btn-sm bg-info-light" data-toggle="modal" data-target="#addResult">\n' +
        '            <i class="fas fa-times"></i> Cancel' +
        '          </a>\n' +
        '        </div>\n'+
        '      </td>\n' +
        '    </tr>'
}



function timeDeal(time,last=8,T=false) {
    let apply_time;let result;
    if(time.indexOf("T")!==-1){
        apply_time = time.split("T");
        result = apply_time[0] + " " + apply_time[1].substr(0,last);
    }
    else{
        result = time;
    }

    if (T){
        return apply_time[0] + "T" + apply_time[1].substr(0,last);
    }
    return result;
}

function accept(obj) {
    let id = parseInt(obj.getAttribute("value"));
    changeResult(id,1);
    let application = "#application" + id;
    $(application).remove();
}

function cancel(obj) {
    let id = parseInt(obj.getAttribute("value"));
    $("#submitCancel").attr("value",id);
}
function changeResult(id,status,result=" ") {
    $.ajax({
        url:"/admin/doctor/deal-register",
        type:"post",
        dataType:"json",
        data:{"applyInfo":JSON.stringify({"id":id,"status":status,"result":result})},
        success:function (data) {
            if (data.code){
                Notiflix.Notify.Success(data.message);
            }
            else {
                Notiflix.Notify.Failure("操作失败");
            }
        }
    });
}

function submitCancel() {
    let id = $("#submitCancel").attr("value");
    changeResult(id,-1,editor.txt.html());
    editor.txt.clear();
    let application = "#application" + id;
    $(application).remove();
}