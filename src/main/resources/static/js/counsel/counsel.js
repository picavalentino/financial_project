const modal = document.getElementById("myModal");
const openModalBtn = document.getElementById("openModalBtn");
const closeModalBtn = document.getElementById("closeModalBtn");
const userList = document.getElementById("userList");
const pagination = document.getElementById("pagination");

let currentPage = 0;  // 현재 페이지
const pageSize = 5;   // 한 페이지에 표시할 데이터 수

// 모달 열기
openModalBtn.onclick = function() {
    loadPageData(currentPage);  // 페이지 데이터 로드
    modal.style.display = "block";
}

// 모달 닫기
closeModalBtn.onclick = function() {
    modal.style.display = "none";
}

// 모달 외부 클릭 시 닫기
window.onclick = function(event) {
    if (event.target == modal) {
        modal.style.display = "none";
    }
}

// 페이지 데이터 로드
function loadPageData(page) {
    fetch(`/getPagedData?page=${page}&size=${pageSize}`)
        .then(response => response.json())
        .then(data => {
            renderUserList(data.content);
            renderPagination(data.totalPages, data.number);
        })
        .catch(error => console.log('Error:', error));
}

// 사용자 목록 렌더링
function renderUserList(users) {
    userList.innerHTML = '';  // 이전 내용 지우기
    users.forEach(user => {
        const li = document.createElement("li");
        li.textContent = `ID: ${user.id}, 이름: ${user.name}, 이메일: ${user.email}`;
        userList.appendChild(li);
    });
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