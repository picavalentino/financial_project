// 모든 모달을 닫는 함수
function closeAllModals() {
    const modals = document.querySelectorAll(".modal-overlay");
    modals.forEach(modal => {
        modal.style.display = "none";
    });
}

function closeModal(modalId) {
    document.getElementById(modalId).style.display = "none";
}

// 특정 모달 열기 함수
function openSearchModal() {
    closeAllModals();  // 다른 모달 닫기
    document.getElementById("searchModal").style.display = "flex";
}

/* ========================================================================= */
/* 담당자 검색 */

/* ========================================================================= */
/* 인쇄 */
function DetailPrint() {
    // 인쇄
    window.print();

/*    // 모달 안에서 인쇄할 영역을 선택
    var printContents = document.getElementById('customerInfo').outerHTML;
    var originalContents = document.body.innerHTML;

    // 페이지의 내용을 인쇄할 내용으로 변경
    document.body.innerHTML = printContents;

    // 원래 페이지로 복구
    document.body.innerHTML = originalContents;*/
}
/* ========================================================================= */
/* 목록 */
function goList() {
    // Spring Controller의 목록 페이지 URL로 이동
    window.location.href = "/customer/list";
}