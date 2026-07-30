import React, { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import api from "../api/axiosConfig";
import { useAuth } from "../context/AuthContext";

export default function JobDetails() {
  const { id } = useParams();
  const { user } = useAuth();
  const navigate = useNavigate();
  const [job, setJob] = useState(null);
  const [coverLetter, setCoverLetter] = useState("");
  const [resume, setResume] = useState(null);
  const [message, setMessage] = useState("");
  const [error, setError] = useState("");

  useEffect(() => {
    const endpoint = user?.role === "CANDIDATE" ? `/candidate/jobs/${id}` : `/jobs/public/${id}`;
    api.get(user?.role === "CANDIDATE" ? `/jobs/public/${id}` : `/jobs/public/${id}`).then((res) => setJob(res.data));
  }, [id, user]);

  const handleApply = async (e) => {
    e.preventDefault();
    setError(""); setMessage("");
    if (!user) { navigate("/login"); return; }
    try {
      const formData = new FormData();
      formData.append("jobPostId", id);
      formData.append("coverLetter", coverLetter);
      if (resume) formData.append("resume", resume);
      await api.post("/candidate/applications", formData, {
        headers: { "Content-Type": "multipart/form-data" },
      });
      setMessage("Application submitted successfully!");
    } catch (err) {
      setError(err.response?.data?.message || "Failed to apply");
    }
  };

  if (!job) return <div className="container">Loading...</div>;

  return (
    <div className="container">
      <div className="card">
        <h2>{job.title}</h2>
        <div style={{ color: "#6b7280", marginBottom: 10 }}>
          {job.companyName || "Company"} · {job.location || "Remote"} · {job.jobType?.replace("_", " ")}
        </div>
        {typeof job.matchScore === "number" && (
          <span className="badge badge-blue">Your match: {job.matchScore}%</span>
        )}
        <p style={{ whiteSpace: "pre-wrap", marginTop: 16 }}>{job.description}</p>
        <p><strong>Required Skills:</strong> {job.skillsRequired}</p>
        {job.minExperience != null && <p><strong>Min Experience:</strong> {job.minExperience} yrs</p>}
        {(job.minSalary || job.maxSalary) && <p><strong>Salary:</strong> {job.minSalary} - {job.maxSalary}</p>}
      </div>

      {(!user || user.role === "CANDIDATE") && (
        <div className="card">
          <h3>Apply for this job</h3>
          {message && <div style={{ color: "#16a34a", marginBottom: 8 }}>{message}</div>}
          {error && <div className="error-text">{error}</div>}
          <form onSubmit={handleApply}>
            <label>Cover Letter</label>
            <textarea rows="4" value={coverLetter} onChange={(e) => setCoverLetter(e.target.value)} />
            <label>Resume (optional if already uploaded to your profile)</label>
            <input type="file" onChange={(e) => setResume(e.target.files[0])} />
            <button className="btn btn-primary" type="submit">Submit Application</button>
          </form>
        </div>
      )}
    </div>
  );
}
