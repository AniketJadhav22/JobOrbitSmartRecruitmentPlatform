import React, { useEffect, useState } from "react";
import api from "../api/axiosConfig";

export default function AdminDashboard() {
  const [tab, setTab] = useState("stats");
  const [stats, setStats] = useState({});
  const [users, setUsers] = useState([]);
  const [jobs, setJobs] = useState([]);

  useEffect(() => {
    api.get("/admin/stats").then((r) => setStats(r.data));
    api.get("/admin/users").then((r) => setUsers(r.data));
    api.get("/admin/jobs").then((r) => setJobs(r.data));
  }, []);

  const deleteUser = async (id) => {
    if (!window.confirm("Delete this user?")) return;
    await api.delete(`/admin/users/${id}`);
    setUsers(users.filter((u) => u.id !== id));
  };

  const deleteJob = async (id) => {
    if (!window.confirm("Delete this job?")) return;
    await api.delete(`/admin/jobs/${id}`);
    setJobs(jobs.filter((j) => j.id !== id));
  };

  return (
    <div className="container">
      <h2>Admin Dashboard</h2>
      <div className="flex" style={{ gap: 10, marginBottom: 20 }}>
        <button className={`btn ${tab === "stats" ? "btn-primary" : "btn-outline"}`} onClick={() => setTab("stats")}>Overview</button>
        <button className={`btn ${tab === "users" ? "btn-primary" : "btn-outline"}`} onClick={() => setTab("users")}>Users</button>
        <button className={`btn ${tab === "jobs" ? "btn-primary" : "btn-outline"}`} onClick={() => setTab("jobs")}>Jobs</button>
      </div>

      {tab === "stats" && (
        <div className="grid-2">
          <div className="card"><h3>{stats.totalUsers ?? "-"}</h3><span>Total Users</span></div>
          <div className="card"><h3>{stats.totalRecruiters ?? "-"}</h3><span>Recruiters</span></div>
          <div className="card"><h3>{stats.totalCandidates ?? "-"}</h3><span>Candidates</span></div>
          <div className="card"><h3>{stats.totalJobs ?? "-"}</h3><span>Job Postings</span></div>
          <div className="card"><h3>{stats.totalApplications ?? "-"}</h3><span>Applications</span></div>
        </div>
      )}

      {tab === "users" && (
        <table style={{ width: "100%", background: "#fff", borderCollapse: "collapse" }}>
          <thead><tr style={{ textAlign: "left" }}><th>Name</th><th>Email</th><th>Role</th><th></th></tr></thead>
          <tbody>
            {users.map((u) => (
              <tr key={u.id} style={{ borderTop: "1px solid #eee" }}>
                <td style={{ padding: 8 }}>{u.fullName}</td>
                <td>{u.email}</td>
                <td>{u.role}</td>
                <td>{u.role !== "ADMIN" && <button className="btn btn-danger" onClick={() => deleteUser(u.id)}>Delete</button>}</td>
              </tr>
            ))}
          </tbody>
        </table>
      )}

      {tab === "jobs" && (
        <table style={{ width: "100%", background: "#fff", borderCollapse: "collapse" }}>
          <thead><tr style={{ textAlign: "left" }}><th>Title</th><th>Status</th><th></th></tr></thead>
          <tbody>
            {jobs.map((j) => (
              <tr key={j.id} style={{ borderTop: "1px solid #eee" }}>
                <td style={{ padding: 8 }}>{j.title}</td>
                <td>{j.status}</td>
                <td><button className="btn btn-danger" onClick={() => deleteJob(j.id)}>Delete</button></td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
}
