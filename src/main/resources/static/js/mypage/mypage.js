$(document).ready(function() {
  // 프로필 이미지 미리보기
  const imageData = /*[[${imageData}]]*/ '';
  if (imageData) {
    $("#profilePreview").attr("src", imageData);
  }

  $("#profileImage").on("change", function() {
    const file = this.files[0];
    if (file) {
      const reader = new FileReader();
      reader.onload = function(e) {
        $("#profilePreview").attr("src", e.target.result);
      }
      reader.readAsDataURL(file);
    }
  });

  // 모달 관련 요소 가져오기
  const $modal = $("#pwModal");
  const $btn = $("#pw_edit");
  const $closeBtn = $(".close");

  // 버튼 클릭 시 모달 열기
  $btn.on("click", function() {
    $modal.css("display", "block"); // 모달 보이기
  });

  // 닫기 버튼 클릭 시 모달 닫기
  $closeBtn.on("click", function() {
    $modal.css("display", "none"); // 모달 숨기기
  });

  // 모달 외부 클릭 시 닫기
  $(window).on("click", function(event) {
    if ($(event.target).is($modal)) {
      $modal.css("display", "none"); // 모달 숨기기
    }
  });
});
