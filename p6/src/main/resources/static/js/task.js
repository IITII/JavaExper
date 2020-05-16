function work() {
    let tmp = $('input').val();
    if (tmp === '' || tmp === undefined || tmp === null) {
        tmp = '17204117'
    }
    $.ajax({
        url: "http://localhost:8080/task/" + tmp
    }).then(function (data) {
        $('#title').text(data.title);
        $('#content').text(data.content);
    })
        .catch(function (e) {
            let msg = "数据获取失败！！！"
            console.log(e);
            console.log(msg);
            alert(msg)
        })
}

