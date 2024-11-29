// 모든 모달을 닫는 함수
function closeAllModals() {
    const modals = document.querySelectorAll(".modal-overlay");
    modals.forEach(modal => {
        modal.style.display = "none";
    });
}
// =========================================================================
// 담당자 검색 모달 열기
function openSearchModal() {
    const modal = document.getElementById("searchModal");
    if (modal && modal.style.display !== "flex") {
        modal.style.display = "flex"; // 필요한 모달만 열기
        console.log("모달이 열렸습니다.");
    } else if (!modal) {
        console.error("모달 요소를 찾을 수 없습니다.");
    }
}

// 모달 닫기
function closeModal(modalId) {
    const modal = document.getElementById(modalId);
    if (modal) {
        modal.style.display = "none";
        console.log("모달이 닫혔습니다.");
    } else {
        console.error(`모달 ID ${modalId}를 찾을 수 없습니다.`);
    }
}

// 담당자 검색 기능 (AJAX로 서버에서 데이터 가져오기)
function searchManager() {
    var custId = document.getElementById('custId').value;
    var searchValue = document.querySelector('#searchModal .search-bar input').value.trim();

    if (!custId) {
        console.error("고객 ID가 존재하지 않습니다.");
        return;
    }

    console.log(`검색 요청: 고객 ID = ${custId}, 검색어 = ${searchValue}`);

    // AJAX 요청
    fetch(`/customer/detail/${custId}/searchManager?name=${searchValue}`)
        .then(response => {
            if (!response.ok) {
                throw new Error(`서버 오류 발생: ${response.status}`);
            }
            return response.json();
        })
        .then(data => {
            console.log("검색 결과:", data);
            var resultListDiv = document.querySelector('#searchModal .result-list');
            resultListDiv.innerHTML = ''; // 기존 검색 결과 초기화

            if (data.length === 0) {
                resultListDiv.innerHTML = '<div>검색 결과가 없습니다.</div>';
            } else {
                data.forEach(function (manager) {
                    // 동적으로 검색 결과 버튼을 추가
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
            var resultListDiv = document.querySelector('#searchModal .result-list');
            resultListDiv.innerHTML = '<div>오류가 발생했습니다. 다시 시도해주세요.</div>';
        });
}

// 담당자 선택 기능
function selectManager(manager) {
    console.log("선택된 담당자:", manager);

    // 부모 페이지의 필드에 선택한 담당자 정보 반영
    const userNameInput = document.querySelector('.searchManager');
    const userIdInput = document.querySelector('input[name="user_id"]');
    const userTelnoInput = document.querySelector('input[name="user_telno"]');
    const deptPositionInput = document.querySelector('input[name="user_dept_Position"]');

    if (userNameInput) userNameInput.value = manager.user_name;
    if (userIdInput) userIdInput.value = manager.user_id;
    if (userTelnoInput) userTelnoInput.value = manager.user_telno;
    if (deptPositionInput) {
        deptPositionInput.value = `${manager.dept_name} / ${manager.position_name}`;
    }

    // "변경" 버튼 활성화 트리거
    const editButton = document.querySelector('.edit-btn');
    if (editButton) {
        const event = new Event('input'); // 입력 이벤트 트리거
        userNameInput.dispatchEvent(event);
    }

    // 모달창 닫기
    closeModal('searchModal');
}

// ===================================================================================================
document.addEventListener('DOMContentLoaded', () => {
    // 초기화
    const form = document.getElementById('customerUpdateForm');
    const editButton = document.querySelector('.edit-btn');
    const revisionHistory = document.getElementById('revisionHistory');
    const custId = document.getElementById("custId").value;
    const csrfToken = document.querySelector('meta[name="_csrf"]')?.content;
    const csrfHeader = document.querySelector('meta[name="_csrf_header"]')?.content;

    // 초기 데이터 저장
    const originalData = new FormData(form);

    // 수정 내역 로드
    loadRevisionHistory(custId);

    // 변경 버튼 이벤트 등록
    editButton.addEventListener('click', async (event) => {
        event.preventDefault();

        // 변경된 필드 확인
        const updateDetail = getChangedFields(form);
        if (updateDetail.length === 0) {
            alert("변경된 내용이 없습니다.");
            return;
        }

        // 수정 내역 저장
        const updateHistory = createUpdateHistory(custId, updateDetail);
        await saveUpdateHistory(updateHistory);

        // 고객 정보 업데이트
        const updatedCustomerData = getUpdatedCustomerData(form, custId);
        await sendUpdateRequest(updatedCustomerData);
    });

    // 필드 변경 감지
    form.querySelectorAll('input, select, textarea').forEach(input => {
        if (!input.disabled) {
            input.addEventListener('input', () => {
                editButton.disabled = !isFormChanged(originalData, form);
            });
        }
    });

    // 함수: 수정 내역 로드
    async function loadRevisionHistory(custId) {
        try {
            const response = await fetch(`/customer/detail/${custId}/history`);
            if (!response.ok) throw new Error('수정 내역 로드 실패');
            const revisions = await response.json();
            updateTextarea(revisions);
        } catch (error) {
            console.error('수정 내역 로드 중 오류 발생:', error);
        }
    }

    // 함수: 수정 내역 화면에 업데이트
    function updateTextarea(revisions) {
        revisionHistory.value = revisions.map(rev =>
            `수정일자: ${rev.custUpdateAt}\n수정ID: ${rev.userId}\n내용: ${rev.updateDetail}\n======================================`
        ).join('\n\n');
    }

    // 함수: 폼 변경 여부 확인
    function isFormChanged(originalData, form) {
        const currentData = new FormData(form);
        for (let [key, value] of originalData.entries()) {
            if (currentData.get(key) !== value) {
                return true;
            }
        }
        return false;
    }

    // 함수: 수정된 고객 데이터 가져오기
        function getUpdatedCustomerData(form, custId) {
            return {
                custId,
                custTelno: form.querySelector('input[name="custTelno"]').value,
                custEmail: form.querySelector('input[name="custEmail"]').value,
                custOccpTyCd: form.querySelector('select[name="custOccpTyCd"]')?.value || '미정',
                custAddr: form.querySelector('input[name="custAddr"]').value,
                users: {
                    user_id: form.querySelector('input[name="user_id"]').value,
                },
            };
        }

        // 함수: 고객 정보 업데이트 요청
        async function sendUpdateRequest(updatedCustomerData) {
            try {
                const response = await fetch(`/customer/update`, {
                    method: 'PUT',
                    headers: {
                        'Content-Type': 'application/json',
                        [csrfHeader]: csrfToken,
                    },
                    body: JSON.stringify(updatedCustomerData),
                });

                if (response.ok) {
                    alert('고객 정보가 성공적으로 수정되었습니다!!');
                    window.location.href = `/customer/detail/${updatedCustomerData.custId}`;
                } else {
                    const errorMessage = await response.text();
                    throw new Error(errorMessage);
                }
            } catch (error) {
                console.error('수정 요청 중 오류 발생:', error);
                alert('담당자를 입력해주세요');
            }
        }

    // 함수: 변경된 필드 가져오기
    function getChangedFields(form) {
        const fields = ['custTelno', 'custEmail', 'custAddr', 'custOccpTyCd'];
        const changes = [];

        fields.forEach(field => {
            const currentValue = form.querySelector(`input[name="${field}"], select[name="${field}"]`)?.value || '미정';
            const previousValue = form.querySelector(`input[name="previous${field.charAt(0).toUpperCase() + field.slice(1)}"]`)?.value || '미정';
            if (currentValue !== previousValue) {
                changes.push(`${field}: ${previousValue} → ${currentValue}`);
            }
        });

        return changes;
    }

    // 함수: 수정 내역 객체 생성
   function createUpdateHistory(custId, updateDetail) {
       return {
           custId,
           userId: document.querySelector('input[name="staffId"]').value,
           updateDetail: updateDetail.join(", "),
           custUpdateAt: new Date().toISOString(),
       };
   }
    // 수정 내역 포맷팅
    function formatRevisionHistory(updateHistory) {
        return `수정 일시 :  ${new Date(updateHistory.custUpdateAt).toLocaleString()},\n수정 ID : ${updateHistory.userId} , \n ${updateHistory.updateDetail}`;
    }

    // 함수: 수정 내역 저장
   async function saveUpdateHistory(updateHistory) {
       try {
           const response = await fetch('/customer/update', {
               method: 'PUT',
               headers: {
                   'Content-Type': 'application/json',
                   [csrfHeader]: csrfToken,
               },
               body: JSON.stringify(updateHistory),
           });

           if (!response.ok) throw new Error('수정 내역 저장 실패');

           // 포맷된 내역을 추가로 업데이트
           const formattedHistory = formatRevisionHistory(updateHistory);
           appendRevisionHistory(formattedHistory);

           alert('수정 내역이 성공적으로 저장되었습니다.');
       } catch (error) {
           console.error('수정 내역 저장 중 오류 발생:', error);
       }
   }

   // 수정 내역을 화면에 추가
   function appendRevisionHistory(formattedHistory) {
       const revisionHistory = document.getElementById('revisionHistory');
       revisionHistory.value = `${formattedHistory}\n${revisionHistory.value}`; // 최신 내역이 상단에 추가
   }
});

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
// =========================================================================
// 상품설계 페이지로
function goPromotion() {
    // Spring Controller의 목록 페이지 URL로 이동
    window.location.href = "/promotion/list";
}