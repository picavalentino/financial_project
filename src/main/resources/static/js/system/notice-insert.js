// 최대 파일 입력 필드 개수
const MAX_FILES = 5;

// 파일 추가 함수
function addFile(button) {
    const allFileRows = document.querySelectorAll(".list-row"); // 모든 파일 선택 행
    if (allFileRows.length >= MAX_FILES) {
        return; // 최대 개수 초과 시 추가하지 않음
    }

    const parentRow = button.closest(".list-row"); // 현재 행 찾기
    const newFileRow = document.createElement("div");
    newFileRow.classList.add("list-row");

    newFileRow.innerHTML = `
        <div class="file-input">
            <input type="file" name="files" onchange="selectFile(this);" />
        </div>
        <div class="file-buttons">
            <button type="button" onclick="removeFile(this);" class="btn-icon">
                <i class="bi bi-file-earmark-minus"></i>
            </button>
            <button type="button" onclick="addFile(this);" class="btn-icon">
                <i class="bi bi-file-earmark-plus"></i>
            </button>
        </div>
    `;

    // 부모 요소 다음에 새 파일 입력 추가
    parentRow.insertAdjacentElement("afterend", newFileRow);

    // 버튼 상태 업데이트
    updateButtonStates();
}

// 파일 삭제 함수
function removeFile(button) {
    const allFileRows = document.querySelectorAll(".list-row"); // 모든 파일 선택 행 찾기
    if (allFileRows.length > 1) {
        const parentRow = button.closest(".list-row"); // 현재 행 찾기
        parentRow.remove(); // 해당 행 삭제
    } else {
        alert("최소 하나의 파일 입력 필드는 있어야 합니다.");
    }

    // 버튼 상태 업데이트
    updateButtonStates();
}

// 버튼 상태 업데이트 함수
function updateButtonStates() {
    const allFileRows = document.querySelectorAll(".list-row"); // 모든 파일 선택 행 찾기

    // "파일 추가" 버튼 비활성화 (최대 개수일 경우)
    const addButtons = document.querySelectorAll(".btn-icon > i.bi-file-earmark-plus");
    addButtons.forEach((btn) => {
        btn.closest("button").disabled = allFileRows.length >= MAX_FILES;
    });

    // "파일 삭제" 버튼 비활성화 (최소 1개일 경우)
    const removeButtons = document.querySelectorAll(".btn-icon > i.bi-file-earmark-minus");
    removeButtons.forEach((btn) => {
        btn.closest("button").disabled = allFileRows.length <= 1;
    });
}

// 파일 선택 함수
function selectFile(input) {
    const file = input.files[0];
    if (file) {
        console.log("선택된 파일: ", file.name);
    } else {
        console.log("파일이 선택되지 않았습니다.");
    }
}

// 초기 상태 업데이트
document.addEventListener("DOMContentLoaded", () => {
    updateButtonStates();
});

// 유효성 검사
function validateForm() {
    let isValid = true;
    let firstInvalidField = null;

    // 파일 입력 필드를 제외하고 나머지 필드 검사
    $(".notice-form")
        .find("input:not([type='file']), textarea")
        .each(function () {
            const value = $(this).val().trim();
            if (!value) {
                isValid = false;
                if (!firstInvalidField) {
                    const label = $(this).closest("tr").find("th").text();
                    firstInvalidField = label;
                }
            }
        });

    if (!isValid) {
        alert(`${firstInvalidField} 필드를 입력해주세요.`);
    }
    return isValid; // 유효성 검사 결과 반환
}

$(document).ready(function(){
    // CSRF 토큰 가져오기
    let csrfToken = $('meta[name="_csrf"]').attr('content');
    let csrfHeader = $('meta[name="_csrf_header"]').attr('content');

    // 목록 버튼 클릭
    $("#btn-list").on("click", function (e) {
         e.preventDefault(); // 기본 동작 방지
         window.location.href = "/system/inquire"; // 목록 페이지로 이동
    });

    // 초기화 버튼 클릭
    $("#btn-reset").on("click", function (e) {
         e.preventDefault(); // 기본 동작 방지
         document.getElementById("notice-form").reset(); // 폼 초기화
    });

    // 등록 버튼 클릭
    $("#btn-register").on("click", function (e) {
        e.preventDefault();

        // 유효성 검사
        if (!validateForm()) return;

        const formElement = document.getElementById("notice-form");
        const formData = new FormData(formElement); // 폼 데이터를 가져옵니다.

        // 체크박스 값을 변환하여 추가
        const inqNoticeValue = $("#inqNotice").is(":checked") ? "1" : "0";
        formData.set("inqNotice", inqNoticeValue);

        // FormData 디버깅
        for (const pair of formData.entries()) {
            console.log(`${pair[0]}: ${pair[1]}`);
        }

        $.ajax({
            url: "/system/inquire/insert",
            type: "POST",
            data: formData,
            processData: false,
            contentType: false,
            success: function (response) {
                alert("등록이 완료되었습니다.");
                window.location.href = "/system/inquire";
            },
            error: function (xhr, status, error) {
                console.error("등록 실패: ", error);
                alert(`등록 실패: ${xhr.responseText}`);
            }
        });
    });
});