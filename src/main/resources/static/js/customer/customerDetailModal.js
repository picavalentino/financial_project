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
            var tbody = document.getElementById('managerList');
            tbody.innerHTML = ''; // 기존 테이블 내용 초기화

            if (data.length === 0) {
                tbody.innerHTML = '<tr><td colspan="3">검색 결과가 없습니다.</td></tr>';
            } else {
                data.forEach(function (manager) {
                    // 동적으로 테이블 행 추가
                    var row = document.createElement('tr');
                    row.innerHTML = `
                        <td>${manager.user_name}</td>
                        <td>${manager.user_id}</td>
                        <td>${manager.user_telno}</td>
                    `;
                    row.onclick = function () {
                        selectManager(manager);
                    };
                    tbody.appendChild(row);
                });
            }
        })
        .catch(error => {
            console.error('검색 중 오류 발생:', error);
            var tbody = document.getElementById('managerList');
            tbody.innerHTML = '<tr><td colspan="3">오류가 발생했습니다. 다시 시도해주세요.</td></tr>';
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
    const custId = document.getElementById("custId").value;
    const csrfToken = document.querySelector('meta[name="_csrf"]')?.content;
    const csrfHeader = document.querySelector('meta[name="_csrf_header"]')?.content;

    // 초기 데이터 저장
    const originalData = new FormData(form);

    // 변경 버튼 이벤트 등록
    editButton.addEventListener('click', async (event) => {
        event.preventDefault();

        // 변경된 필드 확인
        const updatedCustomerData = getUpdatedCustomerData(form, custId);
        if (!isFormChanged(originalData, form)) {
            alert("변경된 내용이 없습니다.");
            return;
        }

        // 고객 정보 업데이트
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
                user_name: form.querySelector('input[name="user_name"]').value,
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
                      const result = await response.json(); // 서버에서 반환된 데이터를 받음
                      console.log("수정 내역:", result.updateHistory); // 디버깅용 출력
                      alert('고객 정보가 성공적으로 수정되었습니다!');
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
});

// 서버에서 수정 내역 데이터를 로드하고 textarea에 추가
async function loadRevisionHistory(custId) {
    try {
        const response = await fetch(`/customer/detail/${custId}/history`);
        if (!response.ok) throw new Error('수정 내역 로드 실패');
        const revisions = await response.json();

        // 수정 내역을 textarea에 추가
        const formattedRevisions = revisions.map(rev => {
            return `\n수정 일시: ${new Date(rev.custUpdateAt).toLocaleString()}\n수정 ID: ${rev.userId}\n내용: ${rev.updateDetail}\n==========================================`;
        }).join('\n\n');

        document.getElementById('revisionHistory').value = formattedRevisions;
    } catch (error) {
        console.error('수정 내역 로드 중 오류 발생:', error);
        document.getElementById('revisionHistory').value = '수정 내역을 불러오는 중 오류가 발생했습니다.';
    }
}

if (revisions.length === 0) {
    document.getElementById('revisionHistory').value = '수정 내역이 없습니다.';
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
// =========================================================================
// 상품설계 페이지로
      function goPromotion(button) {
      // 버튼의 data 속성에서 ID와 고객명을 가져옴
        const custId = button.getAttribute('data-cust-id');
        const custNm = button.getAttribute('data-cust-nm');

// URL 생성
    const url = `/promotion/list?custId=${encodeURIComponent(custId)}&custNm=${encodeURIComponent(custNm)}`;


         // 생성된 URL로 페이지 이동
         window.location.href = url;
      }