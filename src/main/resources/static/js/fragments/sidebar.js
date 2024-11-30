$(document).ready(function(){
    alert("동작 확인");
    // auth_menu 리스트 불러오기

})
// 사용자 프로필 정보 로드
function loadUserProfileInfo() {
    const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');
    const csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');

    fetch('/sidebar/info', {
        method: 'GET',
        headers: {
            [csrfHeader]: csrfToken, // CSRF 토큰 추가
        },
    })
    .then(response => {
        if (response.ok) {
            return response.json();
        } else {
            console.error('Failed to fetch user info');
            return null;
        }
    })
    .then(data => {
        if (data) {
            // 프로필 이미지 업데이트
            const profileImage = document.getElementById('userProfileImage');
            profileImage.src = data.user_imgpath || '/images/common/profile.png';

            // 이름과 직위 업데이트
            const profileName = document.getElementById('userProfileName');
            profileName.textContent = `${data.user_name} ${data.code_nm} 님`;
        }
    })
    .catch(error => {
        console.error('Error fetching user profile info:', error);
    });
}

// 북마크 상태를 서버에서 가져와 적용
function loadBookmarks() {
    const csrfToken = $('meta[name="_csrf"]').attr('content');
    const csrfHeader = $('meta[name="_csrf_header"]').attr('content');
    fetch('/bookmark/list', {
        method: 'GET',
    })
    .then(response => response.json())
    .then(bookmarkedMenuIds => {
        // 북마크된 menu_id 목록을 순회하며 UI 업데이트
        document.querySelectorAll('.bookmark-btn').forEach(btn => {
            const menuId = btn.getAttribute('data-menu-id');
            if (bookmarkedMenuIds.includes(Number(menuId))) {
                // 북마크된 상태의 이미지로 변경
                btn.querySelector('img').src = '/images/common/pin_ok.png';
            }
        });
    });
}

// 북마크 버튼 클릭 이벤트
document.querySelectorAll('.bookmark-btn').forEach(btn => {
    btn.addEventListener('click', function () {
        const menuId = this.getAttribute('data-menu-id');
        const csrfToken = $('meta[name="_csrf"]').attr('content');
        const csrfHeader = $('meta[name="_csrf_header"]').attr('content');

        fetch('/bookmark/add', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
                [csrfHeader]: csrfToken,
            },
            body: `menuId=${menuId}`
        })
        .then(response => {
            if (response.ok) {
                alert('북마크에 추가되었습니다!');
                this.querySelector('img').src = '/images/common/pin_ok.png';
                window.location.reload();
            } else {
                response.text().then(msg => alert(msg));
            }
        });
    });
});

// DOMContentLoaded 이벤트로 모든 기능 초기화
document.addEventListener('DOMContentLoaded', () => {
    loadUserProfileInfo(); // 사용자 프로필 정보 로드
    loadBookmarks(); // 북마크 상태 로드
    setupBookmarkButtons(); // 북마크 버튼 클릭 이벤트 설정
});