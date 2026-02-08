
// Sidebar active link
document.querySelectorAll(".sidebar a").forEach(link => {
  if (link.href === window.location.href) {
    link.classList.add("active");
  }
});

// SEND NOTIFICATION
const notificationForm = document.getElementById("notificationForm");

if (notificationForm) {
    notificationForm.addEventListener("submit", async (e) => {
        e.preventDefault();

        const data = {
            userId: Number(document.getElementById("userId").value),
            message: document.getElementById("message").value
        };

        try {
            const res = await fetch("http://localhost:8080/api/notifications/notification", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(data)
            });

            if (res.ok) {
                alert("Notification sent successfully");
                notificationForm.reset();
            } else {
                alert("Failed to send notification");
            }
        } catch (err) {
            alert("Server error");
            console.error(err);
        }
    });
}

//send email
const emailForm = document.getElementById("emailForm");

if (emailForm) {
    emailForm.addEventListener("submit", async (e) => {
        e.preventDefault();

        const payload = {
            recipient: document.getElementById("toEmail").value,
            subject: document.getElementById("subject").value,
            body: document.getElementById("emailMessage").value
        };

        try {
            const response = await fetch(
                "http://localhost:8080/api/notifications/email",
                {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json",
                    },
                    body: JSON.stringify(payload)
                }
            );

            if (response.ok) {
                alert("Email sent successfully ✅");
                emailForm.reset();
            } else {
                alert("Email failed ❌");
            }
        } catch (err) {
            console.error(err);
            alert("Server error ❌");
        }
    });
}
