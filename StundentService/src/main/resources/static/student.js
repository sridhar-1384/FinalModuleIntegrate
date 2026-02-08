// -------- STUDENT ID --------
const params = new URLSearchParams(window.location.search);
const STUDENT_ID = params.get("studentId");

let resumePath = null;
const addedSkillIds = new Set();

// -------- LOAD STUDENT --------
fetch(`http://localhost:8082/api/students/${STUDENT_ID}`)
    .then(res => res.json())
    .then(data => {

        document.getElementById("name").innerText = data.name;
        document.getElementById("email").innerText = data.email;
        document.getElementById("dept").innerText = data.department;
        document.getElementById("cgpa").innerText = data.cgpa;
        document.getElementById("navUser").innerText = data.name;

        resumePath = data.resumePath;

        if (resumePath) {
            document.getElementById("resumeStatus").innerText = "Uploaded";
            document.getElementById("resumeStatus").className = "text-success";
            document.getElementById("downloadBtn").style.display = "inline-block";
        }

        renderSkills(data.skills || []);
        loadSkills();
    });

// -------- SIDEBAR SCROLL --------
function goToSection(section, el) {

    document.querySelectorAll(".sidebar-link")
        .forEach(link => link.classList.remove("active"));

    el.classList.add("active");

    document.getElementById(`section-${section}`)
        .scrollIntoView({ behavior: "smooth" });
}

// -------- RENDER SKILLS --------
function renderSkills(skills) {

    const container = document.getElementById("currentSkills");
    container.innerHTML = "";
    addedSkillIds.clear();

    if (skills.length === 0) {
        container.innerHTML = "<span class='text-muted'>No skills added yet</span>";
        return;
    }

    skills.forEach(s => {

        addedSkillIds.add(s.skillId);

        const chip = document.createElement("div");
        chip.className = "skill-chip";

        chip.innerHTML = `
            <span>${s.skillName} (${s.level})</span>
            <span class="skill-remove" onclick="removeSkill(${s.skillId})">✕</span>
        `;

        container.appendChild(chip);
    });
}

// -------- LOAD SKILLS DROPDOWN --------
async function loadSkills() {

    const res = await fetch("http://localhost:8082/api/skills");
    const skills = await res.json();

    const select = document.getElementById("skillSelect");
    select.innerHTML = `<option value="">Select Skill</option>`;

    skills.forEach(skill => {

        const opt = document.createElement("option");
        opt.value = skill.id;
        opt.text = `${skill.name} (${skill.category})`;

        if (addedSkillIds.has(skill.id)) {
            opt.disabled = true;
            opt.text += " ✓";
        }

        select.appendChild(opt);
    });
}

// -------- ADD SKILL --------
function addSkill() {

    const skillId = skillSelect.value;
    const level = levelSelect.value;

    if (!skillId || !level) {
        alert("Select skill and level");
        return;
    }

    fetch(`http://localhost:8082/api/students/${STUDENT_ID}/skills`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ masterSkillId: skillId, level })
    }).then(res => {
        if (res.ok) location.reload();
        else alert("Skill already exists");
    });
}

// -------- REMOVE SKILL --------
function removeSkill(skillId) {

    if (!confirm("Remove this skill?")) return;

    fetch(`http://localhost:8082/api/students/${STUDENT_ID}/skills/${skillId}`, {
        method: "DELETE"
    }).then(() => location.reload());
}

// -------- UPLOAD RESUME --------
function uploadResume() {

    const file = resumeFile.files[0];
    if (!file) return alert("Select resume");

    const form = new FormData();
    form.append("file", file);

    fetch(`http://localhost:8082/api/students/${STUDENT_ID}/resume`, {
        method: "POST",
        body: form
    }).then(() => location.reload());
}


//go to other pages with studentId in query params
function goPage(page) {
    if (!STUDENT_ID) {
        alert("Student not found");
        return;
    }

    window.location.href = `${page}?studentId=${STUDENT_ID}`;
}

// -------- APPLY JOB --------
function applyJob() {
    if (!resumePath) return alert("Upload resume first");
    alert("Showing jobs (demo)");
}

// -------- LOGOUT --------
function logout() {
    window.location.href = "login.html";
}
