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
    if (remainingElement === null) {
        return;
    }
    time = remainingElement.innerHTML.split(":");
    secondsLeft = parseInt(time[0]) * 60 + parseInt(time[1], 10) - 1;
    if (secondsLeft <= 0) {
        clearInterval(interval);
        document.location = "/DataPortal/Common/TimeOut.xhtml";
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
        if (elements[i].type.toLowerCase() !== "hidden") {
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
    } else if (element.selectionStart || element.selectionStart === '0') {
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
    //alert(doesSupportXMLHttpRequest());
    var file = document.getElementById(elementId).files[0];
    // check file.name, file.size
//    if (file.type !== "text/plain") {
//        alert("Unzulässiger Dateityp");
//        return false;
//    }
    if (!file.name.endsWith(".zip")) {
        alert("Unzulässiger Dateityp");
        return false;
    }

    var fd = new FormData();
    fd.append("fileToUpload", file);
    updateProgress(0);
    showProgressBar();

    var xhr = new XMLHttpRequest();
    xhr.upload.addEventListener("progress", uploadProgress, false);
    //xhr.upload.onprogress = uploadProgress;
    xhr.addEventListener("load", uploadComplete, false);
    xhr.open("POST", "upload/CertUploadServlet", true);
    xhr.setRequestHeader("X-Requested-With", "XMLHttpRequest");
    xhr.setRequestHeader("X-File-Name", encodeURIComponent(file.name));
    xhr.setRequestHeader("Content-Type", "application/octet-stream");
    xhr.send(file);


}

doesSupportXMLHttpRequest = function () {
    var input = document.createElement('input');
    input.type = 'file';

    return (
            'multiple' in input &&
            typeof File !== "undefined" &&
            typeof (new XMLHttpRequest()).upload !== "undefined");
};

function uploadComplete(evt) {
    //jsf.ajax.request(this, event, {render: 'form :logout:remaining'});
}

function uploadProgress(evt) {
    if (evt.lengthComputable) {
        var percentComplete = Math.round(evt.loaded * 100 / evt.total);
        updateProgress(percentComplete);
    }
}

var updateProgress = function (value) {
    var pBar = document.getElementById("progressBar");
    pBar.value = value;
};

function hideProgressBar() {
    document.getElementById("progressBar").style.visibility = "hidden";
}

function showProgressBar() {
    document.getElementById("progressBar").style.visibility = "visible";
}

function autoGrow(oField) {
    if (oField.scrollHeight > oField.clientHeight) {
        oField.style.height = oField.scrollHeight + "px";
    }
}

function onRemoved(cookie) {
    console.log(`Removed: ${cookie}`);
}

function onError(error) {
    console.log(`Error removing cookie: ${error}`);
}

function removeCookie(url) {
    var browser = new Browser();
    var removing = browser.cookies.remove({
        url: url,
        name: "JSESSIONID"
    });
    //removing.then(onRemoved, onError);
    return removing;
}

function deleteCookie(module) {
    document.cookie = "JSESSIONID=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/" + module + ";";
}

function removeCookies() {
    var modules = [
        "DataPortalAdmin",
        "DataPortal",
        "DataPortalCalc",
        "DataPortalCert",
        "DataPortalDrg",
        "DataPortalInsurance",
        "DataPortalPsy"
    ];
    var ctxt = window.location.pathname.split('/')[1];
    var basePath = window.location.protocol + "//" + window.location.host + "/";
    var removings = [];
    modules.forEach(function (module) {
        if (module !== ctxt) {
           removings.push(removeCookie(basePath + module));
        }
    });
    removings.all.then(onRemoved, onError);
            
}

function addOnLoadFunction(func) {
    var functionChain = window.onload;
    if (typeof window.onload !== "function")
        window.onload = func;
    else
        window.onload = function () {
            functionChain();
            func();
        };
}

/*
 * usage in page:
 *         <script>
 *           jsf.ajax.addOnEvent(showProgressCursor);
 *       </script>
 */
function showProgressCursor(data) {
    var status = data.status;
    var element = document.getElementsByTagName('HTML')[0];

    switch (status) {
        case "begin":
            element.className = 'progress';
            break;

        case "complete":
        case "success":
            element.className = '';
            break;
    }
}

//Change the Headercolor of Dialogs
function changeDialogColor(action) {
    if (action === 'save') {
        $('.ui-dialog.ui-widget-content .ui-dialog-titlebar').css({"background-color": "#5bf770"});
    } else if (action === 'delete') {
        $('.ui-dialog.ui-widget-content .ui-dialog-titlebar').css({"background-color": "#ea4848"});
    } else if (action === 'info') {
        $('.ui-dialog.ui-widget-content .ui-dialog-titlebar').css({"background-color": "#a8cce6"});
    } else {
        console.log('Unknown action');
    }
}
