function showSpeciality(specialityInfos) {
    for (let i = 0;i < specialityInfos.length;i++){
        $("#specialityBody").append(addSpeciality(specialityInfos[i]))
        specialityInfosMap.set(specialityInfos[i].id,specialityInfos[i]);
        initEditDelete(specialityInfos[i].id);
    }
}

function initEditDelete(id) {
    let edit = "#edit" + id;
    let deleteS = "#deleteS" + id;
    $(edit).attr("value",id);
    $(deleteS).attr("value",id);
}

function addSpeciality(speciality) {
    return '<tr id="speciality'+speciality.id+'">\n' +
        '          <td>\n' +
        '            <h2 class="table-avatar">\n' +
        '              <a class="avatar avatar-sm mr-2">\n' +
        '                 <img id="image'+speciality.id+'" class="avatar-img" src="'+ speciality.image_url +'" alt="Speciality">\n' +
        '              </a>\n' +
        '              <a id="speciality_name'+speciality.id+'">'+speciality.speciality+'</a>\n' +
        '            </h2>\n' +
        '          </td>\n' +
        '        \n' +
        '          <td class="text-right">\n' +
        '            <div class="actions">\n' +
        '              <a class="btn btn-sm bg-success-light" onclick="edit(this)" id="edit'+speciality.id+'" data-toggle="modal" href="#edit_specialities_details">\n' +
        '<i class="fe fe-pencil"></i> Edit\n' +
        '              </a>\n' +
        '              <a  data-toggle="modal" onclick="deleteSpeciality(this)" id="deleteS'+speciality.id+'" href="#delete_modal" class="btn btn-sm bg-danger-light">\n' +
        '<i class="fe fe-trash"></i> Delete\n' +
        '              </a>\n' +
        '            </div>\n' +
        '          </td>\n' +
        '        </tr>';
}

function edit(obj) {
    let id = parseInt(obj.getAttribute("value"));
    $("#speciality_edit").val(specialityInfosMap.get(id).speciality);
    $("#submitSpeciality").attr("value",id);
}

function deleteSpeciality(obj) {
    let id = parseInt(obj.getAttribute("value"));
    console.log(id);
    $("#deleteSpeciality").attr("value",id);
}

$('#submitSpeciality').click(function (){
    let id = this.value;
    let formData = new FormData();
    formData.append("specialityInfo",JSON.stringify({"speciality":$("#speciality_edit").val(),"id":id}));
    if ($("input#specialityImgEdit")[0].files.length!==0){
        formData.append("file",$('input#specialityImgEdit')[0].files[0]);
    }
    $.ajax({
        url:"/admin/update-speciality",
        type:"post",
        dataType:"json",
        cache: false,
        processData: false,
        contentType: false,
        data:formData,
        success:function (data) {
            if (data.code){
                Notiflix.Notify.Success("更新成功");
                console.log(data.message);
                let speciality = JSON.parse(data.message);
                let image = "#image" + speciality.id;
                $(image).attr("src",speciality.image_url);
                let speciality_name = "#speciality_name" + speciality.id;
                $(speciality_name).text(speciality.speciality);
            }
            else {
                Notiflix.Notify.Failure(data.message);
            }
        }
    });
});

$("#addSpeciality").click(function (){
    let speciality = $("#specialityName").val();
    let formData = new FormData();
    formData.append("specialityInfo", JSON.stringify({"speciality":speciality}));
    if ($("input#specialityImgAdd")[0].files.length!==0){
        formData.append("file",$('input#specialityImgAdd')[0].files[0]);
    }
    $.ajax({
        url:"/admin/add-speciality",
        type:"post",
        dataType:"json",
        cache: false,
        processData: false,
        contentType: false,
        data:formData,
        success:function (data) {
            if (data.code){
                Notiflix.Notify.Success("添加成功");
                let speciality = JSON.parse(data.message);
                console.log(speciality);
                $("#specialityBody").append(addSpeciality(speciality));
                initEditDelete(speciality.id);
            }
            else {
                Notiflix.Notify.Failure(data.message);
            }
        }
    });
});

$('#deleteSpeciality').click(function (){
    let id = this.value;
    $.ajax({
        url: "/admin/delete-speciality",
        type: "post",
        dataType: "json",
        data: {"id":id},
        success:function (data){
            if (data.code){
                Notiflix.Notify.Success(data.message);
                let deleteSpeciality = "#speciality" + id;
                $(deleteSpeciality).remove();
            }
            else {
                Notiflix.Notify.Failure(data.message);
            }
        }
    });
});