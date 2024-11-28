$(document).ready(function(){
    // 비밀번호 text->password & password->text
    $("#change-btn-eye").on("click", function(){
        const input = $('#change-user_pw');
        if(input.attr('type')==='password'){
            $("#change-btn-hide").css("display", "none");
            $("#change-btn-show").css("display", "block");
            input.attr('type', 'text');
        }else{
            $("#change-btn-show").css("display", "none");
            $("#change-btn-hide").css("display", "block");
            input.attr('type', 'password');
        }
    })
    // 비밀번호 유효성 검사
    $("#change-user_pw, #change-confirm-user_pw").on("input", function() {
        let pw = $("#change-user_pw").val();
        let confirm_pw = $("#change-confirm-user_pw").val();
        const pwPattern = /^(?=.*[a-zA-Z])(?=.*\d)[a-zA-Z\d]{6,13}$/;
        if (pw === confirm_pw) {
            $("#change-msg-not-match").css("display", "none");
            if (pwPattern.test(pw)) {
                $("#btn-change-pw").prop("disabled", false);
                $("#change-msg-invalid-pw").css("display", "none");
            } else {
                $("#btn-change-pw").prop("disabled", true);
                $("#change-msg-invalid-pw").css("display", "inline");
            }
        }else {
            $("#btn-change-pw").prop("disabled", true);
            $("#change-msg-not-match").css("display", "inline");
            if (pwPattern.test(pw)) {
                $("#change-msg-invalid-pw").css("display", "none");
            } else {
                $("#change-msg-invalid-pw").css("display", "inline");
            }
        }
    })
    // 비밀번호 변경
    $("#btn-change-pw").on("click", function(){
        $.ajax({
            type: 'GET',
            url: '/login/change-pw',
            data: {
                id: sessionStorage.getItem("id"),
                pw: $("#change-user_pw").val()
            },
            success: function(response) {
                if(response){
                    alert("비밀번호가 변경 되었습니다.");
                    sessionStorage.clear();
                    window.location.href = "/login";
                }else{
                    alert("비밀번호 변경에 실패하였습니다. 다시 시도해주세요.");
                }
            },
            error: function() {
                alert("비밀번호 변경에 실패하였습니다. 다시 시도해주세요.");
            }
        });
    })
    // 창닫기
    $("#btn-change-pw-cross").on("click", function(){
        $("#change-pw-modal").removeClass('show');
        sessionStorage.clear();
        window.location.href = "/login";
    })
})