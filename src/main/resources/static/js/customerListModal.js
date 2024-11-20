function openPrintModal() {
    document.getElementById("printModal").style.display = "flex";
}

function openMessageModal() {
    document.getElementById("messageModal").style.display = "flex";
}

function openInsertModal() {
    document.getElementById("insertModal").style.display = "flex";
}

function closeModal(modalId) {
    document.getElementById(modalId).style.display = "none";
}