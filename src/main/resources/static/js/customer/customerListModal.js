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

function openInsertModal() {
    closeAllModals();  // 다른 모달 닫기
    document.getElementById("insertModal").style.display = "flex";
}

// 특정 모달 닫기 함수
function closeModal(modalId) {
    document.getElementById(modalId).style.display = "none";
}

/* ========================================================================= */

// 선택된 고객 모달 열기
function openSelectedCustomersModal() {
    // 체크된 체크박스 선택
    const selectedCheckboxes = document.querySelectorAll('.customer-checkbox:checked');
    const selectedValues = Array.from(selectedCheckboxes).map(checkbox => {
        return {
            id: checkbox.value,
            name: checkbox.closest('tr').querySelector('td:nth-child(3) a').innerText,
            tel: checkbox.closest('tr').querySelector('td:nth-child(5)').innerText
        };
    });

    if (selectedValues.length === 0) {
        alert('선택된 고객이 없습니다.');
        return;
    }

    // 모달에 선택된 값 전달
    updateModalContent(selectedValues);
}

// 모달 내용 업데이트
function updateModalContent(selectedValues) {
    const buttonsContainer = document.getElementById('buttonsContainer');
    buttonsContainer.innerHTML = ''; // 이전 버튼 초기화

    selectedValues.forEach(value => {
        const button = document.createElement('button');
        button.className = 'tag-btn';
        button.innerHTML = `${value.name} (${value.tel}) <span class="close-tag" onclick="removeTag(event)">X</span>`;

        // 버튼 클릭 시 관련 작업 추가 가능
        button.onclick = function () {
            alert(`고객 ID: ${value.id} - ${value.name} 선택됨`);
        };

        buttonsContainer.appendChild(button);
    });

    // 모달 열기
    openMessageModal();
}

// 태그 컨테이너 업데이트
function updateTagContainer(selectedValues) {
    const tagContainer = document.getElementById('tagContainer');
    const noCustomerMessage = document.getElementById('noCustomerMessage');

    // 선택된 고객이 있으면 안내 메시지 숨기고 태그 버튼 추가
    if (selectedValues.length > 0) {
        noCustomerMessage.style.display = 'none';
        tagContainer.innerHTML = ''; // 기존 태그 초기화

        selectedValues.forEach(value => {
            const tagButton = document.createElement('button');
            tagButton.className = 'tag-btn';
            tagButton.innerHTML = `${value.name} (${value.tel}) <span class="close-tag" onclick="removeTag(event)">X</span>`;

            tagContainer.appendChild(tagButton);
        });
    } else {
        // 선택된 고객이 없으면 안내 메시지 표시
        noCustomerMessage.style.display = 'block';
        tagContainer.innerHTML = ''; // 기존 태그 초기화
    }
}

// 태그 제거
function removeTag(event) {
    event.stopPropagation(); // 클릭 이벤트 전파 방지
    const tagButton = event.target.closest('.tag-btn'); // 부모 태그 버튼 찾기
    if (tagButton) {
        tagButton.remove(); // 태그 버튼 삭제
    }

    // 태그 제거 후 남은 태그가 없으면 안내 메시지 표시
    const tagContainer = document.getElementById('tagContainer');
    if (tagContainer.querySelectorAll('.tag-btn').length === 0) {
        document.getElementById('noCustomerMessage').style.display = 'block';
    }
}

// 모달 열기
function openMessageModal() {
    closeAllModals(); // 다른 모달 닫기
    document.getElementById('messageModal').style.display = 'flex'; // 메시지 모달 열기
}
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

/* =========================================================================== */
/* 고객 목록 */
function toggleAll(source) {
    const checkboxes = document.querySelectorAll('.customer-checkbox');
    checkboxes.forEach((checkbox) => {
        checkbox.checked = source.checked;
    });
}