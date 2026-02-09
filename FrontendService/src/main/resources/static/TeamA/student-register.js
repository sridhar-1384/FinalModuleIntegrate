document.getElementById("regForm").addEventListener("submit", async function(e){
    e.preventDefault();

    let body = {
        name: document.getElementById("name").value,
        email: document.getElementById("email").value,
        password: document.getElementById("password").value,
        department: document.getElementById("department").value,
        cgpa: parseFloat(document.getElementById("cgpa").value),
        phone: document.getElementById("phone").value
    };

    let res = await fetch("http://localhost:8081/api/auth/register-student", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(body)
    });

    let data = await res.json();

    if (data.status === "error") {
        alert(data.message);
        return;
    }

    alert(data.message);
    window.location.href = "login.html";
});

