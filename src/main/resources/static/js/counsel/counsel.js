// 상담 목록 관련 변수
const listModal = document.getElementById("myModal");
const openModalBtn = document.getElementById("openModalBtn");
const closeModalBtn = document.getElementById("closeModalBtn");
const counselList = document.getElementById("counselList");
const pagination = document.getElementById("pagination");

// 상담 상세 관련 변수
const detailModal = document.getElementById("detailModal");
const backBtn = document.getElementById("backBtn");
const closeDetailModalBtn = document.getElementById("closeDetailModalBtn");

// 상담 작성 관련 변수
const writeModal = document.getElementById("writeModal");
const counselWriteBtn = document.getElementById("counselWriteBtn");
const closeWriteModalBtn = document.getElementById("closeWriteModalBtn");

let currentPage = 0;  // 현재 페이지
const pageSize = 5;   // 한 페이지에 표시할 데이터 수

// 모달 열기
openModalBtn.onclick = function() {
    loadPageData(currentPage);  // 페이지 데이터 로드
    listModal.style.display = "block";
}

// 상담 목록 모달 닫기
closeModalBtn.onclick = function() {
    listModal.style.display = "none";
}

// 상담 상세 모달 닫기
closeDetailModalBtn.onclick = function() {
    detailModal.style.display = "none";
}

// 상담 작성 모달 닫기
closeWriteModalBtn.onclick = function() {
    writeModal.style.display = "none";
}

// 모달 외부 클릭 시 닫기
window.onclick = function(event) {
    if (event.target == listModal) {
        listModal.style.display = "none";
    }
}

// 페이지 데이터 로드
function loadPageData(page) {
    fetch(`/customer/counsel/getPagedData?page=${page}&size=${pageSize}`)
        .then(response => response.json())
        .then(data => {
            renderUserList(data.content);
            renderPagination(data.totalPages, data.number);
        })
        .catch(error => console.log('Error:', error));
}

// 사용자 목록 렌더링
function renderUserList(counsels) {
    counselList.innerHTML = '';  // 이전 내용 지우기
    // 각 필드 이름을 배열로 정의
    const counselFields = [
        'counselId',
        'userName',
        'userDeptCd',
        'counselCategory',
        'counselCreateAt',
        'counselUpdateAt'
    ];

    counsels.forEach((counsel, i) => {
        const tr = document.createElement("tr");

        // counselFields 배열을 통해 각 필드를 순회
        counselFields.forEach(field => {
            const td = document.createElement("td");
            td.textContent = counsel[field];  // 동적으로 필드 값 삽입
            tr.appendChild(td);  // tr에 td 추가
        });

        // tr 클릭 시 모달 열기
        tr.addEventListener('click', () => {
            // 기존에 열려있는 모달을 닫기
            listModal.style.display = "none";

//            // 새로운 모달 창에 내용 삽입
//            document.getElementById("modalCounselId").textContent = `상담 ID: ${counsel.counselId}`;
//            document.getElementById("modalUserName").textContent = `고객명: ${counsel.userName}`;
//            document.getElementById("modalUserDeptCd").textContent = `부서: ${counsel.userDeptCd}`;
//            document.getElementById("modalCounselCategory").textContent = `상담 카테고리: ${counsel.counselCategory}`;
//            document.getElementById("modalCounselCreateAt").textContent = `상담 시작일: ${counsel.counselCreateAt}`;
//            document.getElementById("modalCounselUpdateAt").textContent = `상담 수정일: ${counsel.counselUpdateAt}`;
//
            // 새 모달을 열기
            detailModal.style.display = "block";
        });

        // 생성한 tr을 테이블에 추가
        counselList.appendChild(tr);
    });
}

// 뒤로가기 버튼 클릭
backBtn.onclick = function() {
    detailModal.style.display = "none";
    listModal.style.display = "block";
}

// 페이징 버튼 렌더링
function renderPagination(totalPages, currentPage) {
    pagination.innerHTML = '';  // 이전 내용 지우기
    for (let i = 0; i < totalPages; i++) {
        const button = document.createElement("button");
        button.textContent = i + 1;
        button.onclick = function() {
            loadPageData(i);
        };
        if (i === currentPage) {
            button.disabled = true;  // 현재 페이지는 비활성화
        }
        pagination.appendChild(button);
    }
}

// 상담작성 버튼 클릭
counselWriteBtn.onclick = function() {
    listModal.style.display = "none";
    writeModal.style.display = "block";
}
