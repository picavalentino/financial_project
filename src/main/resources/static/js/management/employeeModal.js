document.addEventListener("DOMContentLoaded", function() {
  function openRegistrationModal() {
    var registrationModal = new bootstrap.Modal(document.getElementById('registrationModal'));
    registrationModal.show();
  }

  window.openRegistrationModal = openRegistrationModal; // HTML에서 함수 호출을 위해 전역으로 설정
});
