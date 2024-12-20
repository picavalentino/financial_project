$(document).ready(function(){
    // 인풋 포커스
    $("#user_id").on("focus", function(){
        $("#id-border").css("border-bottom", "2px solid #040a47");
    })
    $("#user_pw").on("focus", function(){
        $("#pw-border").css("border-bottom", "2px solid #040a47");
    })
    $("#user_id").on("blur", function(){
        $("#id-border").css("border-bottom", "2px solid #929292");
    })
    $("#user_pw").on("blur", function(){
        $("#pw-border").css("border-bottom", "2px solid #929292");
    })
    // 사원번호 숫자만
    $("#user_id").on("keypress", function (event) {
        // 숫자 (0-9)만 입력 가능하게 설정
        if (event.which < 48 || event.which > 57) {
            // 백스페이스 (8), 탭 (9), 화살표 키 등은 허용
            if (event.which !== 8 && event.which !== 9) {
                event.preventDefault();
            }
        }
    });
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
    // 사번찾기에서 자동으로 사번 입력창에 삽입
    const id = sessionStorage.getItem("id");
    if(id){
        $("#user_id").val(id);
        $("#remember_checkbox").prop("checked", true);
        sessionStorage.clear();
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
    const errorMessage = $('#error-container').data('error-message');
    if (errorMessage) {
        alert(errorMessage);
    }
    // 아이디 찾기 모달창
    $(".btn-retrieve-id").on("click", function(){
        $("#id-modal").addClass('show');
    })
    // 비밀번호 찾기 모달창
    $(".btn-retrieve-pw").on("click", function(){
        $("#pw-modal").addClass('show');
    })
    // 비밀번호 변경 모달창
    $("#btn-change").on("click", function(){
        sessionStorage.setItem("id",$("#pw-user_id").val());
        $("#pw-modal").removeClass('show');
        $("#change-pw-modal").addClass('show');
    })
})