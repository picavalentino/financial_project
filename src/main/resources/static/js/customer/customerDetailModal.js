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