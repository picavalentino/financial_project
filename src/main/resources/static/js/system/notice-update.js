$(document).ready(function(){
    // CSRF 토큰 가져오기
    let csrfToken = $('meta[name="_csrf"]').attr('content');
    let csrfHeader = $('meta[name="_csrf_header"]').attr('content');

    // 비밀번호 확인 관련
    const serverInqPwd = $("#serverInqPwd").val(); // 서버에서 제공된 비밀번호
    const inqId = $("#inqId").val(); // 게시글 ID (HTML에 hidden input으로 전달)

    // 파일 추가 삭제 관련
    const maxFiles = 5;
    const deletedFiles = [];

    // 파일 삭제 이벤트 (동적으로 생성된 요소 지원)
    $(document).on("click", ".file-item .close", function () {
        const fileName = $(this).data("file-name"); // 파일 이름 가져오기
        if (confirm("정말로 이 파일을 삭제하시겠습니까?")) {
            $(this).closest(".file-item").remove(); // 화면에서 파일 제거
            if (fileName) {
                deletedFiles.push(fileName); // 삭제된 파일 목록에 추가
            }
        }
    });

    // 새 파일 추가 버튼 클릭 이벤트
    $(".btn-add-file").on("click", function () {
        const fileContainer = $("#file-container");
        const fileInputs = fileContainer.find(".file-input");
        if (fileInputs.length >= maxFiles) {
            alert(`최대 ${maxFiles}개의 파일만 업로드할 수 있습니다.`);
            return;
        }
        // 새로운 파일 필드 추가
        fileContainer.append(`
            <div class="file-input">
                <input type="file" name="files" multiple/>
            </div>
        `);
    });

    // 새 파일 삭제 버튼 클릭 이벤트
    $(".btn-remove-file").on("click", function () {
        $(this).closest(".file-input").remove(); // 현재 버튼에 연결된 파일 입력 필드 삭제
    });

    // 수정 버튼 클릭 이벤트
    $("#btn-register").on("click", function (e) {
        e.preventDefault();

        // 비밀번호 확인
        const inputPwd = $("#inqPwd").val();
        if (inputPwd !== serverInqPwd) {
           alert("비밀번호가 일치하지 않습니다.");
           return; // 함수 실행 중단
        }

        const formData = new FormData();
        formData.append("inqId", $("#inqId").val());
        formData.append("inqTitle", $("input[name='inqTitle']").val());
        formData.append("inqContent", $("textarea[name='inqContent']").val());
        formData.append("inqPwd", $("#inqPwd").val());

        // 삭제 요청된 파일 추가
        deletedFiles.forEach(file => {
            formData.append("deletedFiles", file);
        });

        // 새로 추가된 파일 추가
        $("input[name='files']").each(function () {
            if (this.files.length > 0) {
                Array.from(this.files).forEach(file => {
                    formData.append("files", file);
                });
            }
        });

        // AJAX 요청
        $.ajax({
            url: `/system/inquire/detail/${$("#inqId").val()}/update`,
            type: "POST",
            data: formData,
            processData: false, // 데이터 직렬화하지 않음
            beforeSend: function (xhr) {
                if (csrfHeader && csrfToken) {
                    xhr.setRequestHeader(csrfHeader, csrfToken); // CSRF 토큰 추가
                }
            },
            contentType: false, // Content-Type 설정하지 않음 (브라우저가 자동으로 설정)
            success: function (response) {
                alert("수정이 완료되었습니다.");
                window.location.href = `/system/inquire/detail/${$("#inqId").val()}`;
            },
            error: function (xhr) {
                if (xhr.status === 403) {
                    alert("수정 권한이 없습니다.");
                } else {
                    alert("수정 중 오류가 발생했습니다.");
                }
            }
        });
    });

    // 삭제 버튼 클릭
    $("#btn-delete").on("click", function (e) {
        e.preventDefault(); // 기본 동작 방지
        const inputPwd = $("#inqPwd").val();
        const serverPwd = $("#serverInqPwd").val();

        if (inputPwd === serverPwd) {
            if (confirm("공지사항을 삭제하시겠습니까?")) {
                $.ajax({
                    url: `/system/inquire/detail/${inqId}/delete`,
                    type: "POST",
                    contentType: "application/x-www-form-urlencoded; charset=UTF-8",
                    data: { inqPwd: inputPwd },
                    beforeSend: function (xhr) {
                        if (csrfHeader && csrfToken) {
                            xhr.setRequestHeader(csrfHeader, csrfToken);
                        }
                    },
                    success: function (response) {
                        alert("공지사항이 삭제되었습니다.");
                        window.location.href = "/system/inquire";
                    },
                    error: function (xhr) {
                        if (xhr.status === 403) {
                            alert("비밀번호가 일치하지 않습니다.");
                        } else {
                            alert("삭제 중 오류가 발생했습니다.");
                        }
                    }
                });
            }
        } else {
            alert("비밀번호가 일치하지 않습니다.");
        }
    });

    // 목록 버튼 클릭
    $("#btn-list").on("click", function (e) {
         e.preventDefault();
         window.location.href = "/system/inquire";
    });
});