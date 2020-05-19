function fillFormUpdate(currentElem) {
    var cellName = document.querySelector("#updName");
    var cellNumber = document.querySelector("#updNumber");
    var number = currentElem.parentNode.parentNode;
    var name = number.previousSibling.textContent;
    var parent = currentElem.parentNode.parentNode;
    var id = parent.id;
    var form = document.querySelector("#update");
    form.style.display = "block";
    cellName.setAttribute("value", name);
    cellNumber.setAttribute("value", number.textContent);
    cellNumber.parentNode.parentNode.id = id;
}

function openForm() {
    var form = document.querySelector("#add");
    form.style.display = "block";
}

function closeForm(currentElem) {
    var form = currentElem.parentNode.parentNode;
    form.style.display = "none";
}

function createNewRows(phone) {
    var name = phone.name;
    var number = phone.phonenumber;
    var id = phone.id;
    var tbody = document.querySelector('#table').getElementsByTagName('tbody')[0];
    var row = document.createElement("tr");
    tbody.appendChild(row);
    var td1 = document.createElement("td");
    var td2 = document.createElement("td");
    td1.className = "newName";
    td2.className = "newNumber";
    td1.innerHTML = name;
    td2.innerHTML = number+
        `<button class="images"><img src="/assets/images/delete.png" id="deleteButton" width="20" height="20" align="middle" onclick="deleteContact(this)"></button>`+' '+
        `<button id="updateButton" class="images"><img src="/assets/images/edit.png" width="20" height="20" align="middle" onclick="fillFormUpdate(this);"></button>`;
    td2.id = id;
    row.appendChild(td1);
    row.appendChild(td2);
}


function searchByPhone() {
    var number = document.querySelector(".searchPhone").value;
    var request = new XMLHttpRequest();
    request.open('GET', 'http://172.18.0.36:9000/phones/searchByPhone?phoneSubstring='+number, true);
    request.onload = function () {
        var data = JSON.parse(this.responseText);
        if (request.status >= 200 && request.status < 400) {
            console.log(data);
            var tbody = document.querySelector('#table').getElementsByTagName('tbody')[0];
            var rowCount = tbody.rows.length;
            while(--rowCount) {
                tbody.deleteRow(rowCount);
            }
            data.forEach (phone => {
                console.log(phone);
                createNewRows(phone);
            })}
        else {
            console.log('error');
        }}
    request.send();
    var button = document.querySelector(".clearSearch");
    button.className = "showButton";
    return false;
}

function searchByName() {
    var name = document.querySelector(".searchName").value;
    var request = new XMLHttpRequest();
    request.open('GET', 'http://172.18.0.36:9000/phones/searchByName?nameSubstring='+name, true);
    request.onload = function () {
        var data = JSON.parse(this.responseText);
        if (request.status >= 200 && request.status < 400) {
            console.log(data);
            var tbody = document.querySelector('#table').getElementsByTagName('tbody')[0];
            var rowCount = tbody.rows.length;
            while(--rowCount) {
                tbody.deleteRow(rowCount);
            }
            data.forEach (phone => {
                console.log(phone);
                createNewRows(phone);
            })}
        else {
            console.log('error');
        }}
    request.send();
    var button = document.querySelector(".clearSearch");
    button.className = "showButton";
    return false;
}



function addContact() {
    var name = document.querySelector("#addName").value;
    var number = document.querySelector("#addNumber").value;
    var contact = {name:name, phonenumber:number};
    var body = JSON.stringify(contact);
    console.log(body);
    if (confirm("Are you sure that you want to add this contact?")) {
        var request = new XMLHttpRequest();
        request.open('POST', 'http://172.18.0.36:9000/phones/add', true);
        request.setRequestHeader("Content-Type","application/json");
        request.onload = function () {
                var data = this.responseText;
                if (request.status >= 200 && request.status < 400) {
                    console.log(data);
                }
                else {
                    if (request.status == 400){
                        alert("Contact with number "+number+" already exists!");
                        return;
                    }
                    else
                        console.log('error');
                }
            }
        request.send(body);
    }
    document.querySelector(".popup-close-add").click();
    window.location.reload(true);
}

function updateContact(currentElem) {
    var parent = currentElem.parentNode.previousElementSibling;
    var id = parent.id;
    var name = document.querySelector("#updName").value;
    var number = document.querySelector("#updNumber").value;
    var contact = {name: name, phonenumber:number};
    var body = JSON.stringify(contact);
    if (confirm("Are you sure that you want to update this contact?")) {
        var request = new XMLHttpRequest();
        request.open('POST', 'http://172.18.0.36:9000/phones/update/'+id, true);
        request.setRequestHeader("Content-Type","application/json");
        request.onload = function () {
            var data = this.responseText;
            if (request.status >= 200 && request.status < 400) {
                console.log(data);
            }
            else {
                console.log('error');
            }}
        request.send(body);
}
    document.querySelector(".popup-close-upd").click();
    window.location.reload(true);
}


function deleteContact(currentElem) {
    var parent = currentElem.parentNode.parentNode;
    var id = parent.id;
    if (confirm("Are you sure that you want to delete this contact?")) {
        var request = new XMLHttpRequest();
        request.open('DELETE', 'http://172.18.0.36:9000/phones/delete/'+id, true)
        request.onload = function () {
            var data = this.response;
            if (request.status >= 200 && request.status < 400) {
                console.log(data);
            }
            else {
                console.log('error');
            }
        }
        request.send();
        document.location.reload(true);
    }}


function listAll() {
    var request = new XMLHttpRequest();
    request.open('GET', 'http://172.18.0.36:9000/phones', true);
    request.onload = function () {
        var data = JSON.parse(this.responseText);
        if (request.status >= 200 && request.status < 400) {
            data.forEach(phone => {
                console.log(phone);
                createNewRows(phone);
            })
        } else {
            console.log('error');
        }
    }
    request.send();
}