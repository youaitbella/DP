function showMessage(msg, id) {
    alert(msg);
    setFocus(id);
}

window.onload = init;
var interval;
function init() {
    startTimer();
    setFocus();
}
function startTimer() {
    interval = setInterval(updateSessionTimer, 1000); // every second
}

function updateSessionTimer() {
    remainingElement = document.getElementById("logout:remaining");
    if (remainingElement == null) {
        return;
    }
    time = remainingElement.innerHTML.split(":");
    secondsLeft = parseInt(time[0]) * 60 + parseInt(time[1], 10) - 1;
    if (secondsLeft <= 0) {
        clearInterval(interval);
        document.location = "/DataPortal/common/TimeOutRedirector.xhtml";
    }
    minutes = Math.floor(secondsLeft / 60);
    seconds = secondsLeft % 60;
    secString = "" + seconds;
    if (seconds < 10) {
        secString = "0" + seconds;
    }
    remainingElement.innerHTML = "" + minutes + ":" + (seconds < 10 ? "0" + seconds : seconds);
}


function setFocus(id) {
    var element = document.getElementById(id);
    if (element && element.focus) {
        element.focus();
        return;
    }
    elements = document.getElementsByTagName('input');
    for (i = 0; i < elements.length; i++) {
        if (elements[i].type.toLowerCase() != "hidden") {
            elements[i].focus();
            break;
        }
    }
}

function clickElementById(id) {
    var element = document.getElementById(id);
    if (element) {
        element.click();
        return;
    }
    alert('Could not find element ' + id);
}


function getCaretPosition(element) {
    var CaretPos = 0; // IE Support
    if (document.selection) {
        element.focus();
        var Sel = document.selection.createRange();
        Sel.moveStart('character', -element.value.length);
        CaretPos = Sel.text.length;
    } else if (element.selectionStart || element.selectionStart == '0') {
// Firefox support
        CaretPos = element.selectionStart;
    }
    return (CaretPos);
}

function setCaretPosition(id, pos) {
    var element = document.getElementById(id);
    if (!element) {
        return;
    }
    if (pos < 0) {
        pos = element.value.length;
    }
    element.focus();
    if (element.value.length === 0) {
        return;
    }
    if (element.setSelectionRange) {
        element.focus();
        element.setSelectionRange(pos, pos);
    } else if (element.createTextRange) {
        var range = element.createTextRange();
        range.collapse(true);
        range.moveEnd('character', pos);
        range.moveStart('character', pos);
        range.select();
    }
}

function uploadFile(elementId) {
    var file = document.getElementById(elementId).files[0];
    // check file.name, file.size
    if (file.type !== "text/plain") {
        alert("Unzulässiger Dateityp");
        return false;
    }
    updateProgress(0);
    showProgressBar();
    var fd = new FormData();
    fd.append("uploadFile", file);
    var xhr = new XMLHttpRequest();
    xhr.upload.addEventListener("progress", uploadProgress, false);
    xhr.open("POST", "UploadServlet");
    xhr.send(fd);
}

function uploadProgress(evt) {
    if (evt.lengthComputable) {
        var percentComplete = Math.round(evt.loaded * 100 / evt.total);
        updateProgress(percentComplete);
    }
}

var updateProgress = function (value) {
    document.getElementById("progressBar").value = value;
}

function hideProgressBar() {
    document.getElementById("progressBar").style.display = "none";
}

function showProgressBar() {
    document.getElementById("progressBar").style.display = "inline";
}