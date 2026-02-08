// -------- NAVIGATION --------
function goPage(page) {
    window.location.href = page;
}

// -------- LOGOUT --------
function logout() {
    window.location.href = "login.html";
}

// -------- LOAD STUDENTS --------
if (document.getElementById("studentsContainer")) {
    loadStudents();
}

function loadStudents() {

    fetch("http://localhost:8082/api/students")
        .then(res => res.json())
        .then(students => {

            const container = document.getElementById("studentsContainer");
            container.innerHTML = "";

            if (students.length === 0) {
                container.innerHTML = "<p class='text-muted'>No students found</p>";
                return;
            }

            students.forEach(s => {

                const card = document.createElement("div");
                card.className = "card p-3 mb-3";

                card.innerHTML = `
                    <h6>${s.name}</h6>
                    <p class="mb-1"><b>Email:</b> ${s.email}</p>
                    <p class="mb-1"><b>Department:</b> ${s.dept}</p>
                    <p class="mb-2"><b>CGPA:</b> ${s.cgpa}</p>

                    <div class="d-flex gap-2">
                        <button class="btn btn-primary btn-sm" onclick="updateStudent(${s.id})">Update</button>
                        <button class="btn btn-danger btn-sm" onclick="deleteStudent(${s.id})">Delete</button>
                    </div>
                `;

                container.appendChild(card);
            });
        });
}

// -------- UPDATE (placeholder) --------
function updateStudent(id) {
    alert("Update student " + id + " (form can be added later)");
}

// -------- DELETE STUDENT --------
function deleteStudent(id) {

    if (!confirm("Are you sure you want to delete this student?")) return;

    fetch(`http://localhost:8082/api/admin/delete-student/${id}`, {
        method: "DELETE"
    })
    .then(res => {
        if (res.ok) {
            alert("Student deleted");
            loadStudents();
        } else {
            alert("Delete failed");
        }
    });
}

// -------- LOAD MASTER SKILLS --------
if (document.getElementById("skillsContainer")) {
    loadMasterSkills();
}

function loadMasterSkills() {

    fetch("http://localhost:8082/api/skills")
        .then(res => res.json())
        .then(skills => {

            const container = document.getElementById("skillsContainer");
            container.innerHTML = "";

            if (skills.length === 0) {
                container.innerHTML =
                    "<p class='text-muted'>No master skills found</p>";
                return;
            }

            skills.forEach(skill => {

                const card = document.createElement("div");
                card.className = "card p-3 mb-3";

                card.innerHTML = `
                    <h6>${skill.name}</h6>
                    <p class="mb-1"><b>Category:</b> ${skill.category}</p>
                    <p class="mb-0"><b>Status:</b> ${skill.active ? "Active" : "Inactive"}</p>
                `;

                container.appendChild(card);
            });
        });
}
// -------- MODAL HANDLING --------
function openAddSkillModal() {
    new bootstrap.Modal(document.getElementById("addSkillModal")).show();
}

function closeAddSkillModal() {
    bootstrap.Modal.getInstance(
        document.getElementById("addSkillModal")
    ).hide();
}

// -------- ADD MASTER SKILL --------
function addMasterSkill() {

    const name = document.getElementById("skillName").value.trim();
    const category = document.getElementById("skillCategory").value.trim();

    if (!name || !category) {
        alert("Please fill all fields");
        return;
    }

    fetch("http://localhost:8082/api/admin/add-skill", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
            name: name,
            category: category,
            active: true
        })
    })
    .then(res => {
        if (res.ok) {
            alert("Skill added successfully");
            location.reload();
        } else {
            alert("Failed to add skill");
        }
    });
}

