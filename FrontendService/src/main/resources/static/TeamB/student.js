
// -------- GLOBAL VARIABLES --------
let resumePath = null;
const addedSkillIds = new Set();

// -------- UTILITY: GET SESSION TOKEN --------
function getSessionToken() {
    const token = sessionStorage.getItem("sessionToken");
    if (!token) {
        alert("Please login first");
        window.location.href = "/login.html";
        return null;
    }
    return token;
}

// -------- LOAD STUDENT PROFILE --------
async function loadStudentProfile() {
    const sessionToken = getSessionToken();
    if (!sessionToken) return;

    console.log("Session Token:", sessionToken);
    console.log("Attempting to fetch from: http://localhost:8082/api/students/me");

    try {
        const res = await fetch('http://localhost:8082/api/students/me', {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Session-Token': sessionToken
            }
        });

        console.log("Response received:", res);
        console.log("Response status:", res.status);
        console.log("Response ok:", res.ok);

        if (!res.ok) {
            const errorText = await res.text();
            console.error("Error response body:", errorText);

            if (res.status === 401 || res.status === 403) {
                alert("Session expired. Please login again.");
                sessionStorage.removeItem("sessionToken");
                window.location.href = "/login.html";
                return;
            }
            throw new Error(`Server responded with ${res.status}: ${errorText}`);
        }

        const data = await res.json();
        console.log("Student data received:", data);

        // Update UI
        document.getElementById("name").innerText = data.name || "N/A";
        document.getElementById("email").innerText = data.email || "N/A";
        document.getElementById("dept").innerText = data.dept || "N/A";
        document.getElementById("cgpa").innerText = data.cgpa || "N/A";
        document.getElementById("navUser").innerText = data.name || "User";

        resumePath = data.resumePath;

        if (resumePath) {
            document.getElementById("resumeStatus").innerText = "Uploaded";
            document.getElementById("resumeStatus").className = "text-success";
            document.getElementById("downloadBtn").style.display = "inline-block";
        } else {
            document.getElementById("resumeStatus").innerText = "Not Uploaded";
            document.getElementById("resumeStatus").className = "text-danger";
            document.getElementById("downloadBtn").style.display = "none";
        }

        renderSkills(data.skills || []);
        loadSkills();

    } catch (error) {
        console.error("Fetch failed completely:", error);
        console.error("Error name:", error.name);
        console.error("Error message:", error.message);

        let errorMsg = "Failed to load profile. Please try again.";

        if (error.message === "Failed to fetch") {
            errorMsg += "\n\nPossible causes:\n" +
                       "1. Server is not running on port 8082\n" +
                       "2. CORS is blocking the request\n" +
                       "3. Wrong server URL";
        } else {
            errorMsg += "\n\nError: " + error.message;
        }

        alert(errorMsg);
    }
}

// -------- RENDER CURRENT SKILLS --------
function renderSkills(skills) {
    const container = document.getElementById("currentSkills");
    container.innerHTML = "";

    addedSkillIds.clear();

    if (!skills || skills.length === 0) {
        container.innerHTML = "<p class='text-muted'>No skills added yet</p>";
        return;
    }

    skills.forEach(skill => {

        //  use correct backend fields
        addedSkillIds.add(skill.skillId);

        const badge = document.createElement("span");
        badge.className = "badge bg-primary d-flex align-items-center gap-2 mb-2";

        badge.innerHTML = `
            ${skill.skillName} - ${skill.level}
            <button class="btn-close btn-close-white btn-sm"
                    onclick="removeSkill(${skill.skillId})"
                    style="font-size:0.6rem"></button>
        `;

        container.appendChild(badge);
    });
}


async function loadSkills() {
    try {
        const res = await fetch('http://localhost:8082/api/skills');
        const allSkills = await res.json();

        const select = document.getElementById("skillSelect");
        select.innerHTML = '<option value="">Select Skill</option>';

        allSkills.forEach(skill => {

            const option = document.createElement("option");
            option.value = skill.id;
            option.innerText = skill.name;

            // if skill already added → disable + grey
            if (addedSkillIds.has(skill.id)) {
                option.disabled = true;
                option.innerText += " (Already Added)";
            }

            select.appendChild(option);
        });

    } catch (error) {
        console.error("Error loading skills:", error);
    }
}

// -------- ADD SKILL --------
async function addSkill() {
    const sessionToken = getSessionToken();
    if (!sessionToken) return;

    const skillId = document.getElementById("skillSelect").value;
    const level = document.getElementById("levelSelect").value;

    if (!skillId || !level) {
        alert("Please select both skill and level");
        return;
    }

    const parsedSkillId = parseInt(skillId);
    if (isNaN(parsedSkillId)) {
        alert("Invalid skill selected");
        return;
    }

    try {
        const requestBody = {
            masterSkillId: parsedSkillId,  // Changed from skillId to masterSkillId
            level
        };
        console.log('Sending request:', requestBody);

        const res = await fetch('http://localhost:8082/api/students/add/skills', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Session-Token': sessionToken
            },
            body: JSON.stringify(requestBody)
        });

        const responseData = await res.text();
        console.log('Response status:', res.status);
        console.log('Response data:', responseData);

        if (!res.ok) {
            let errorMessage = "Failed to add skill";
            try {
                const errorJson = JSON.parse(responseData);
                errorMessage = errorJson.message || errorJson.error || errorMessage;
            } catch (e) {
                errorMessage = responseData || errorMessage;
            }
            throw new Error(errorMessage);
        }

        alert("Skill added successfully!");
        loadStudentProfile();

    } catch (error) {
        console.error("Error adding skill:", error);
        alert("Failed to add skill: " + error.message);
    }
}

// -------- REMOVE SKILL --------
async function removeSkill(skillId) {
    const sessionToken = getSessionToken();
    if (!sessionToken) return;

    if (!confirm("Are you sure you want to remove this skill?")) {
        return;
    }

    try {
        const res = await fetch(`http://localhost:8082/api/students/skills/${skillId}`, {
            method: 'DELETE',
            headers: {
                'Session-Token': sessionToken
            }
        });

        if (!res.ok) {
            throw new Error("Failed to remove skill");
        }

        alert("Skill removed successfully!");

        // Reload profile to refresh skills
        loadStudentProfile();

    } catch (error) {
        console.error("Error removing skill:", error);
        alert("Failed to remove skill. Please try again.");
    }
}

// -------- UPLOAD RESUME --------
async function uploadResume() {
    const sessionToken = getSessionToken();
    if (!sessionToken) {
        console.error("No session token found");
        return;
    }

    const fileInput = document.getElementById("resumeFile");
    const file = fileInput.files[0];

    console.log("Selected file:", file);

    if (!file) {
        alert("Please select a file first");
        return;
    }

    // Validate file type (PDF only)
    if (file.type !== 'application/pdf') {
        alert("Please upload a PDF file only");
        console.error("Invalid file type:", file.type);
        return;
    }

    // Validate file size (max 5MB)
    if (file.size > 5 * 1024 * 1024) {
        alert("File size must be less than 5MB");
        console.error("File too large:", file.size, "bytes");
        return;
    }

    const formData = new FormData();
    formData.append("file", file);

    console.log("FormData created, uploading file:", file.name, "Size:", file.size, "bytes");

    try {
        const res = await fetch('http://localhost:8082/api/students/resume', {
            method: 'POST',
            headers: {
                'Session-Token': sessionToken
            },
            body: formData  // Correct variable
        });

        console.log("Response status:", res.status);
        console.log("Response headers:", Object.fromEntries(res.headers.entries()));

        const responseText = await res.text();
        console.log("Response body:", responseText);

        if (!res.ok) {
            let errorMessage = "Failed to upload resume";
            try {
                const errorJson = JSON.parse(responseText);
                errorMessage = errorJson.message || errorJson.error || errorMessage;
                console.error("Parsed error:", errorJson);
            } catch (e) {
                errorMessage = responseText || errorMessage;
                console.error("Raw error response:", responseText);
            }
            throw new Error(errorMessage);
        }

        alert("Resume uploaded successfully!");
        fileInput.value = "";
        loadStudentProfile();

    } catch (error) {
        console.error("Error uploading resume:", error);
        console.error("Error stack:", error.stack);
        alert("Failed to upload resume: " + error.message);
    }
}

// -------- DOWNLOAD RESUME --------
async function downloadResume() {
    const sessionToken = getSessionToken();
    if (!sessionToken) return;

    if (!resumePath) {
        alert("No resume available to download");
        return;
    }

    try {
        const res = await fetch('http://localhost:8082/api/students/resume/download', {
            method: 'GET',
            headers: {
                'Session-Token': sessionToken
            }
        });

        if (!res.ok) {
            throw new Error("Failed to download resume");
        }

        const blob = await res.blob();
        const url = window.URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = `resume_${Date.now()}.pdf`;
        document.body.appendChild(a);
        a.click();
        window.URL.revokeObjectURL(url);
        document.body.removeChild(a);

    } catch (error) {
        console.error("Error downloading resume:", error);
        alert("Failed to download resume. Please try again.");
    }
}

// -------- LOGOUT --------
function logout() {
    sessionStorage.removeItem("sessionToken");
    window.location.href = "/TeamA/login.html";
}

// -------- NAVIGATION --------
function goPage(page) {
    window.location.href = page;
}

// -------- INITIALIZE ON PAGE LOAD --------
document.addEventListener('DOMContentLoaded', () => {
    loadStudentProfile();
});
