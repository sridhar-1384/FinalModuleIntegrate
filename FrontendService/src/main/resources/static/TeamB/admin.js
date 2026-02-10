
let selectedStudentId = null;
function openEditStudent(id, dept, cgpa) {
    selectedStudentId = id;

    const deptInput = document.getElementById("adminDept");
    const cgpaInput = document.getElementById("adminCgpa");
    const section = document.getElementById("editStudentSection"); // ✅ THIS LINE

    if (!deptInput || !cgpaInput || !section) {
        console.error("Edit section inputs not found");
        return;
    }

    deptInput.value = dept;
    cgpaInput.value = cgpa;

    section.style.display = "block";
    section.scrollIntoView({ behavior: "smooth" });
}



// -------- NAVIGATION --------
function goPage(page) {
    window.location.href = page;
}

// -------- LOGOUT --------
function logout() {
    window.location.href = "login.html";
}
// Load students ONLY on students page
if (document.getElementById("studentsContainer")) {
    loadStudents();
}

// Load master skills ONLY on master skills page
if (document.getElementById("skillsContainer")) {
    loadMasterSkills();
}


// -------- LOAD STUDENTS --------
//if (document.getElementById("studentsContainer")) {
//    loadStudents();
//}
function loadStudents() {
    const token = sessionStorage.getItem("sessionToken");

    fetch("http://localhost:8082/api/admin/students", {
        headers: {
            "Session-Token": token
        }
    })
        .then(res => {
            if (!res.ok) throw new Error("Failed to load students");
            return res.json();
        })
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
                        <button class="btn btn-primary btn-sm"
                                onclick="openEditStudent(${s.id}, '${s.dept ?? ""}', '${s.cgpa ?? ""}')">
                            Update
                        </button>
                        <button class="btn btn-danger btn-sm"
                                onclick="deleteStudent(${s.id})">
                            Delete
                        </button>
                    </div>
                `;
                container.appendChild(card);
            });
        })
        .catch(err => {
            console.error(err);
            alert("Unable to load students");
        });
}


// -------- UPDATE (placeholder) --------
async function saveAdminStudent() {
    const token = sessionStorage.getItem("sessionToken");

    const payload = {};
    if (adminDept.value.trim()) payload.dept = adminDept.value.trim();
    if (adminCgpa.value.trim()) payload.cgpa = parseFloat(adminCgpa.value);

    await fetch(
        `http://localhost:8082/api/admin/update-student/${selectedStudentId}`,
        {
            method: "PUT",
            headers: {
                "Content-Type": "application/json",
                "Session-Token": token
            },
            body: JSON.stringify(payload)
        }
    );

    alert("Updated ✅");
    closeEdit();
    loadStudents();
}
function closeEdit() {
    document.getElementById("editStudentSection").style.display = "none";
    selectedStudentId = null;
}




// -------- DELETE STUDENT --------
function deleteStudent(id) {

    if (!confirm("Are you sure you want to delete this student?")) return;

    const token = sessionStorage.getItem("sessionToken");

    if (!token) {
        alert("Session expired. Please login again.");
        return;
    }

    fetch(`http://localhost:8082/api/admin/delete-student/${id}`, {
        method: "DELETE",
        headers: {
            "Session-Token": token
        }
    })
    .then(res => {
        if (!res.ok) {
            return res.text().then(err => {
                throw new Error(err);
            });
        }
        alert("Student deleted ✅");
        loadStudents();
    })
    .catch(err => {
        console.error("Delete error:", err);
        alert("Delete failed ❌");
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

async function addMasterSkill() {
    const token = sessionStorage.getItem("sessionToken");
    const skillName = document.getElementById("skillName").value.trim();

    if (!skillName) {
        alert("Skill name is required");
        return;
    }

    try {
        const res = await fetch("http://localhost:8082/api/admin/add-skill", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                "Session-Token": token
            },
            body: JSON.stringify({
                name: skillName   // ✅ MUST be "name"
            })
        });

        if (!res.ok) {
            const err = await res.text();
            throw new Error(err);
        }

        alert("Master skill added successfully ✅");
        document.getElementById("skillName").value = "";
        loadMasterSkills();

    } catch (err) {
        console.error("Add skill error:", err);
        alert("Failed to add skill");
    }
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
async function addMasterSkill() {
    const token = sessionStorage.getItem("sessionToken");

    const nameInput = document.getElementById("skillName");
    const categoryInput = document.getElementById("skillCategory");

    if (!token) {
        alert("Session expired. Please login again.");
        return;
    }

    if (!nameInput || !categoryInput) {
        console.error("Skill name or category input not found");
        return;
    }

    const skillName = nameInput.value.trim();
    const category = categoryInput.value.trim();

    if (!skillName || !category) {
        alert("Both skill name and category are required");
        return;
    }

    console.log("Adding master skill:", {
        name: skillName,
        category: category
    });

    try {
        const res = await fetch("http://localhost:8082/api/admin/add-skill", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                "Session-Token": token
            },
            body: JSON.stringify({
                name: skillName,
                category: category
            })
        });

        if (!res.ok) {
            const errText = await res.text();
            console.error("Backend error:", errText);
            throw new Error(errText);
        }

        alert("Master skill added successfully ✅");

        // Clear inputs
        nameInput.value = "";
        categoryInput.value = "";

        // Reload skills list
        if (typeof loadMasterSkills === "function") {
            loadMasterSkills();
        }

    } catch (err) {
        console.error("Add master skill failed:", err);
        alert("Failed to add master skill");
    }
}



