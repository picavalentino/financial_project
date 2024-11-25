$(document).ready(function(){
    // 쿠키 설정 함수
    function setCookie(name, value, days) {
        const date = new Date();
        date.setTime(date.getTime() + (days * 24 * 60 * 60 * 1000));
        document.cookie = `${name}=${value};expires=${date.toUTCString()};path=/`;
    }

    // 쿠키 삭제 함수
    function deleteCookie(name) {
        document.cookie = `${name}=;expires=Thu, 01 Jan 1970 00:00:00 UTC;path=/;`;
    }

    // 쿠키 가져오기 함수
    function getCookie(name) {
        const value = `; ${document.cookie}`;
        const parts = value.split(`; ${name}=`);
        if (parts.length === 2) return parts.pop().split(";").shift();
        return "";
    }
    // 페이지 로드 시 쿠키에서 사원번호 불러오기
    const savedUserId = getCookie("savedUserId");
    if (savedUserId) {
        $("#user_id").val(savedUserId);
        $("#remember_checkbox").prop("checked", true);
    }

    // 로그인 폼 제출 시 쿠키 저장 처리
    $("#loginForm").on("submit", function () {
        if ($("#remember_checkbox").is(":checked")) {
            setCookie("savedUserId", $("#user_id").val(), 7); // 쿠키를 7일 동안 저장
        } else {
            deleteCookie("savedUserId");
        }
    });
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