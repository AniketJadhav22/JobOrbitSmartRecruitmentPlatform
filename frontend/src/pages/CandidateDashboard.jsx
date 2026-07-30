import React, { useEffect, useState } from "react";
import api from "../api/axiosConfig";
import JobCard from "../components/JobCard";

export default function CandidateDashboard() {
  const [tab, setTab] = useState("recommended");
  const [recommended, setRecommended] = useState([]);
  const [applications, setApplications] = useState([]);
  const [profile, setProfile] = useState({ skills: "", experienceYears: "", education: "", bio: "" });
  const [resumeFile, setResumeFile] = useState(null);
  const [saved, setSaved] = useState("");

  useEffect(() => {
    api.get("/candidate/jobs/recommended").then((r) => setRecommended(r.data));
    api.get("/candidate/applications").then((r) => setApplications(r.data));
    api.get("/candidate/profile").then((r) => r.data && setProfile({
      skills: r.data.skills || "",
      experienceYears: r.data.experienceYears || "",
      education: r.data.education || "",
      bio: r.data.bio || "",
    }));
  }, []);

  const saveProfile = async (e) => {
    e.preventDefault();
    await api.put("/candidate/profile", profile);
    setSaved("Profile saved!");
    setTimeout(() => setSaved(""), 2000);
    api.get("/candidate/jobs/recommended").then((r) => setRecommended(r.data));
  };

  const uploadResume = async (e) => {
    e.preventDefault();
    if (!resumeFile) return;
    const formData = new FormData();
    formData.append("file", resumeFile);
    await api.post("/candidate/profile/resume", formData, { headers: { "Content-Type": "multipart/form-data" } });
    setSaved("Resume uploaded!");
    setTimeout(() => setSaved(""), 2000);
  };

  const statusBadge = (status) => {
    const map = { APPLIED: "badge-blue", SHORTLISTED: "badge-yellow", SELECTED: "badge-green", REJECTED: "badge-red" };
    return <span className={`badge ${map[status]}`}>{status}</span>;
  };

  return (
    <div className="container">
      <h2>Candidate Dashboard</h2>
      <div className="flex" style={{ gap: 10, marginBottom: 20 }}>
        <button className={`btn ${tab === "recommended" ? "btn-primary" : "btn-outline"}`} onClick={() => setTab("recommended")}>Recommended Jobs</button>
        <button className={`btn ${tab === "applications" ? "btn-primary" : "btn-outline"}`} onClick={() => setTab("applications")}>My Applications</button>
        <button className={`btn ${tab === "profile" ? "btn-primary" : "btn-outline"}`} onClick={() => setTab("profile")}>My Profile</button>
      </div>

      {tab === "recommended" && (
        recommended.length === 0 ? <p>No active jobs yet. Add skills to your profile for better matches.</p> :
        recommended.map((job) => <JobCard key={job.id} job={job} />)
      )}

      {tab === "applications" && (
        applications.length === 0 ? <p>You haven't applied to any jobs yet.</p> :
        applications.map((a) => (
          <div key={a.id} className="card flex-between">
            <div>
              <strong>{a.jobTitle}</strong>
              <div style={{ fontSize: 13, color: "#6b7280" }}>Applied {new Date(a.appliedDate).toLocaleDateString()} · Match: {a.matchScore}%</div>
            </div>
            {statusBadge(a.status)}
          </div>
        ))
      )}

      {tab === "profile" && (
        <div className="card">
          {saved && <div style={{ color: "#16a34a", marginBottom: 8 }}>{saved}</div>}
          <form onSubmit={saveProfile}>
            <label>Skills (comma separated)</label>
            <input value={profile.skills} onChange={(e) => setProfile({ ...profile, skills: e.target.value })} placeholder="Java, Spring Boot, React, MySQL" />
            <label>Years of Experience</label>
            <input type="number" value={profile.experienceYears} onChange={(e) => setProfile({ ...profile, experienceYears: e.target.value })} />
            <label>Education</label>
            <input value={profile.education} onChange={(e) => setProfile({ ...profile, education: e.target.value })} />
            <label>Bio</label>
            <textarea rows="3" value={profile.bio} onChange={(e) => setProfile({ ...profile, bio: e.target.value })} />
            <button className="btn btn-primary" type="submit">Save Profile</button>
          </form>
          <hr style={{ margin: "20px 0" }} />
          <form onSubmit={uploadResume}>
            <label>Upload Resume</label>
            <input type="file" onChange={(e) => setResumeFile(e.target.files[0])} />
            <button className="btn btn-outline" type="submit">Upload</button>
          </form>
        </div>
      )}
    </div>
  );
}
