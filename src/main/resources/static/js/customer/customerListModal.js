// 모든 모달을 닫는 함수
function closeAllModals() {
    const modals = document.querySelectorAll(".modal-overlay");
    modals.forEach(modal => {
        modal.style.display = "none";
    });
}

// 특정 모달 열기 함수
function openPrintModal() {
    closeAllModals();  // 다른 모달 닫기
    document.getElementById("printModal").style.display = "flex";
}

function openMessageModal() {
    closeAllModals();  // 다른 모달 닫기
    document.getElementById("messageModal").style.display = "flex";
}

function openInsertModal() {
    closeAllModals();  // 다른 모달 닫기
    document.getElementById("insertModal").style.display = "flex";
}

// 특정 모달 닫기 함수
function closeModal(modalId) {
    document.getElementById(modalId).style.display = "none";
}

/* ========================================================================= */
/* 메세지 발송 */

// 태그 지우기
function removeTag(event) {
    event.stopPropagation(); // 클릭 이벤트가 부모 요소로 전달되지 않도록 방지
    const tagButton = event.target.closest(".tag-btn, .hidden-tag-btn"); // 현재 클릭된 "X" 버튼의 부모 태그 버튼 찾기
    if (tagButton) {
        tagButton.remove(); // 태그 버튼 삭제
        updateTagVisibility(); // 태그 삭제 후 태그 상태 업데이트
    }
}
// 초기 상태에서 태그 가시성 업데이트
updateTagVisibility();

/* ========================================================================= */
/* 고객등록 */

/* ========================================================================= */
/* 인쇄 */
function printTable() {
    // 모달 안에서 인쇄할 영역을 선택
    var printContents = document.getElementById('print-table').outerHTML;
    var originalContents = document.body.innerHTML;

    // 페이지의 내용을 인쇄할 내용으로 변경
    document.body.innerHTML = printContents;

    // 인쇄
    window.print();

    // 원래 페이지로 복구
    document.body.innerHTML = originalContents;
}
