$(document).ready(function(){
    // 비밀번호 텍스트로 보이게
    $("#btn-eye").on("click", function(){
        const input = $('#user_pw');
        if(input.attr('type')==='password'){
            $("#btn-hide").css("display", "none");
            $("#btn-show").css("display", "block");
            input.attr('type', 'text');
        }else{
            $("#btn-show").css("display", "none");
            $("#btn-hide").css("display", "block");
            input.attr('type', 'password');
        }
    })
    // 로그인 실패시 알림창
    var errorMessage = $("#hiddenErrorMessage").val();
    if (errorMessage) {
        alert(errorMessage);
        $("#hiddenErrorMessage").val('');
    }
    // 아이디 찾기 모달창

})