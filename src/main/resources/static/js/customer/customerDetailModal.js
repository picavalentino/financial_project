// 모든 모달을 닫는 함수
function closeAllModals() {
    const modals = document.querySelectorAll(".modal-overlay");
    modals.forEach(modal => {
        modal.style.display = "none";
    });
}

// =========================================================================
// 담당자 검색

function openSearchModal() {
    closeAllModals();  // 다른 모달 닫기
    document.getElementById("searchModal").style.display = "flex";
}

function closeModal(modalId) {
    document.getElementById(modalId).style.display = "none";
}

// 담당자 검색 기능 (AJAX로 서버에서 데이터 가져오기)
function searchManager() {
    var searchValue = document.getElementById('searchInput').value.trim();

    // AJAX 요청
    fetch(`/api/managers/search?name=${searchValue}`)
        .then(response => response.json())
        .then(data => {
            var resultListDiv = document.getElementById('resultList');
            resultListDiv.innerHTML = ''; // 기존 검색 결과 초기화

            if (data.length === 0) {
                resultListDiv.innerHTML = '<div>검색 결과가 없습니다.</div>';
            } else {
                data.forEach(function (manager) {
                    var resultButton = document.createElement('button');
                    resultButton.className = 'result-btn';
                    resultButton.innerHTML = `
                        <span class="name">${manager.user_name}</span>
                        <span class="info">${manager.user_id} / ${manager.user_telno}</span>
                    `;
                    resultButton.onclick = function () {
                        selectManager(manager);
                    };
                    resultListDiv.appendChild(resultButton);
                });
            }
        })
        .catch(error => {
            console.error('검색 중 오류 발생:', error);
            var resultListDiv = document.getElementById('resultList');
            resultListDiv.innerHTML = '<div>오류가 발생했습니다. 다시 시도해주세요.</div>';
        });
}

// 담당자 선택 기능
function selectManager(manager) {
    // 부모 페이지의 필드에 선택한 담당자 정보 반영
    document.querySelector('.searchManager').value = manager.user_name;
    document.querySelector('input[name="custUsersId"]').value = manager.user_id;
    document.querySelector('input[name="custUsersTelno"]').value = manager.user_telno;

    // 모달창 닫기
    closeModal('searchModal');
}

// =========================================================================
// 인쇄
function DetailPrint() {
    // 모달 안에서 인쇄할 영역을 선택
    var printContents = document.getElementById('leftSide').outerHTML;
    var originalContents = document.body.innerHTML;

    // 페이지의 내용을 인쇄할 내용으로 변경
    document.body.innerHTML = printContents;

    // 인쇄
    window.print();

    // 원래 페이지로 복구
    document.body.innerHTML = originalContents;
}
// =========================================================================
// 목록
function goList() {
    // Spring Controller의 목록 페이지 URL로 이동
    window.location.href = "/customer/list";
}