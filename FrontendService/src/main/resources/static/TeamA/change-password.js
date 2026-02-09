document.getElementById("changePwdForm").addEventListener("submit",async function (e) {
    e.preventDefault();

    const token = sessionStorage.getItem("sessionToken");

    if (!token) {
        window.location.href = "/login.html";
        return;
    }

    const currentPassword = document.getElementById("currentPassword").value;
    const newPassword = document.getElementById("newPassword").value;

    let res=await fetch("http://localhost:8081/api/auth/change-password", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "Session-Token": token
        },
        body: JSON.stringify({
            currentPassword: currentPassword,
            newPassword: newPassword
        })
    })
    if(!res.ok){
        if(res.status==500){
            sessionStorage.clear();
            window.location.href = "/login.html";
            return;
        }
        let data=await res.text()
        document.getElementById("message").innerText = data;
        return;
    }
    let data=await res.text()
    console.log(data)
    document.getElementById("message").innerText = data;
});