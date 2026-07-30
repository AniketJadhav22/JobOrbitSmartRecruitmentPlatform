import React, { useEffect, useState } from "react";
import api from "../api/axiosConfig";

const emptyJob = {
  title: "", description: "", skillsRequired: "", location: "",
  jobType: "FULL_TIME", minExperience: "", minSalary: "", maxSalary: "", applicationDeadline: "",
};

export default function RecruiterDashboard() {
  const [tab, setTab] = useState("jobs");
  const [jobs, setJobs] = useState([]);
  const [applications, setApplications] = useState([]);
  const [form, setForm] = useState(emptyJob);
  const [editingId, setEditingId] = useState(null);
  const [message, setMessage] = useState("");

  const loadJobs = () => api.get("/recruiter/jobs").then((r) => setJobs(r.data));
  const loadApplications = () => api.get("/recruiter/applications").then((r) => setApplications(r.data));

  useEffect(() => { loadJobs(); loadApplications(); }, []);

  const handleChange = (e) => setForm({ ...form, [e.target.name]: e.target.value });

  const handleSubmit = async (e) => {
    e.preventDefault();
    const payload = { ...form, applicationDeadline: form.applicationDeadline || null };
    if (editingId) {
      await api.put(`/recruiter/jobs/${editingId}`, payload);
      setMessage("Job updated!");
    } else {
      await api.post("/recruiter/jobs", payload);
      setMessage("Job posted!");
    }
    setForm(emptyJob);
    setEditingId(null);
    loadJobs();
    setTimeout(() => setMessage(""), 2000);
  };

  const editJob = (job) => {
    setForm({
      title: job.title, description: job.description, skillsRequired: job.skillsRequired,
      location: job.location || "", jobType: job.jobType || "FULL_TIME",
      minExperience: job.minExperience || "", minSalary: job.minSalary || "",
      maxSalary: job.maxSalary || "", applicationDeadline: job.applicationDeadline || "",
    });
    setEditingId(job.id);
    setTab("post");
  };

  const closeJob = async (id) => { await api.patch(`/recruiter/jobs/${id}/close`); loadJobs(); };
  const deleteJob = async (id) => { if (window.confirm("Delete this job posting?")) { await api.delete(`/recruiter/jobs/${id}`); loadJobs(); } };

  const updateAppStatus = async (id, status) => {
    await api.patch(`/recruiter/applications/${id}/status`, { status });
    loadApplications();
  };

  const statusBadge = (status) => {
    const map = { APPLIED: "badge-blue", SHORTLISTED: "badge-yellow", SELECTED: "badge-green", REJECTED: "badge-red" };
    return <span className={`badge ${map[status]}`}>{status}</span>;
  };

  return (
    <div className="container">
      <h2>Recruiter Dashboard</h2>
      <div className="flex" style={{ gap: 10, marginBottom: 20 }}>
        <button className={`btn ${tab === "jobs" ? "btn-primary" : "btn-outline"}`} onClick={() => setTab("jobs")}>My Job Postings</button>
        <button className={`btn ${tab === "post" ? "btn-primary" : "btn-outline"}`} onClick={() => { setTab("post"); setEditingId(null); setForm(emptyJob); }}>Post a Job</button>
        <button className={`btn ${tab === "applications" ? "btn-primary" : "btn-outline"}`} onClick={() => setTab("applications")}>Applications</button>
      </div>

      {tab === "jobs" && (
        jobs.length === 0 ? <p>You haven't posted any jobs yet.</p> :
        jobs.map((job) => (
          <div key={job.id} className="card flex-between">
            <div>
              <strong>{job.title}</strong>{" "}
              <span className={`badge ${job.status === "ACTIVE" ? "badge-green" : "badge-red"}`}>{job.status}</span>
              <div style={{ fontSize: 13, color: "#6b7280" }}>{job.location} · {job.jobType}</div>
            </div>
            <div className="flex" style={{ gap: 8 }}>
              <button className="btn btn-outline" onClick={() => editJob(job)}>Edit</button>
              {job.status === "ACTIVE" && <button className="btn btn-outline" onClick={() => closeJob(job.id)}>Close</button>}
              <button className="btn btn-danger" onClick={() => deleteJob(job.id)}>Delete</button>
            </div>
          </div>
        ))
      )}

      {tab === "post" && (
        <div className="card">
          {message && <div style={{ color: "#16a34a", marginBottom: 8 }}>{message}</div>}
          <form onSubmit={handleSubmit}>
            <label>Job Title</label>
            <input name="title" value={form.title} onChange={handleChange} required />
            <label>Description</label>
            <textarea rows="4" name="description" value={form.description} onChange={handleChange} required />
            <label>Required Skills (comma separated)</label>
            <input name="skillsRequired" value={form.skillsRequired} onChange={handleChange} placeholder="Java, Spring Boot, React, MySQL" required />
            <div className="grid-2">
              <div>
                <label>Location</label>
                <input name="location" value={form.location} onChange={handleChange} />
              </div>
              <div>
                <label>Job Type</label>
                <select name="jobType" value={form.jobType} onChange={handleChange}>
                  <option value="FULL_TIME">Full Time</option>
                  <option value="PART_TIME">Part Time</option>
                  <option value="INTERNSHIP">Internship</option>
                  <option value="CONTRACT">Contract</option>
                </select>
              </div>
              <div>
                <label>Min Experience (yrs)</label>
                <input type="number" name="minExperience" value={form.minExperience} onChange={handleChange} />
              </div>
              <div>
                <label>Application Deadline</label>
                <input type="date" name="applicationDeadline" value={form.applicationDeadline} onChange={handleChange} />
              </div>
              <div>
                <label>Min Salary</label>
                <input type="number" name="minSalary" value={form.minSalary} onChange={handleChange} />
              </div>
              <div>
                <label>Max Salary</label>
                <input type="number" name="maxSalary" value={form.maxSalary} onChange={handleChange} />
              </div>
            </div>
            <button className="btn btn-primary" type="submit">{editingId ? "Update Job" : "Post Job"}</button>
          </form>
        </div>
      )}

      {tab === "applications" && (
        applications.length === 0 ? <p>No applications received yet.</p> :
        applications.map((a) => (
          <div key={a.id} className="card flex-between">
            <div>
              <strong>{a.candidateName}</strong> applied for <strong>{a.jobTitle}</strong>
              <div style={{ fontSize: 13, color: "#6b7280" }}>Match: {a.matchScore}% · {new Date(a.appliedDate).toLocaleDateString()}</div>
              {a.resumeFilePath && <a href={a.resumeFilePath} target="_blank" rel="noreferrer">View Resume</a>}
            </div>
            <div className="flex" style={{ gap: 8, alignItems: "center" }}>
              {statusBadge(a.status)}
              <select value={a.status} onChange={(e) => updateAppStatus(a.id, e.target.value)}>
                <option value="APPLIED">Applied</option>
                <option value="SHORTLISTED">Shortlisted</option>
                <option value="SELECTED">Selected</option>
                <option value="REJECTED">Rejected</option>
              </select>
            </div>
          </div>
        ))
      )}
    </div>
  );
}
