<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Create</title>
    <script>
        function send() {
            let object = {
                "code": document.getElementById("code_snippet").value,
                "time": parseInt(document.getElementById("time_restriction").value),
                "views": parseInt(document.getElementById("views_restriction").value),
            };
            if(isNaN(object.time)) {
                alert("Insert valid time!");
                return;
            }
            if(isNaN(object.views)) {
                alert("Insert valid views!");
                return;
            }
            let json = JSON.stringify(object);
            let xhr = new XMLHttpRequest();
            xhr.open("POST", '/api/code/new', false)
            xhr.setRequestHeader('Content-type', 'application/json; charset=utf-8');
            xhr.send(json);
            if (xhr.status === 200) {
                alert("Success!");
            }
        }
    </script>
</head>
<body>
<form>
    <label for="code_snippet">Insert code</label><textarea id="code_snippet"></textarea>
    <label for="time_restriction">Time (seconds) of visibility</label><input id="time_restriction" type="text"/>
    <label for="views_restriction">Max number of views</label><input id="views_restriction" type="text"/>
    <button id="send_snippet" type="submit" onclick="send()">Submit</button>
</form>
</body>
</html>