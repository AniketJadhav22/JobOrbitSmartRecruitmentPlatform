import React from "react";
import { Link } from "react-router-dom";
import { useAuth } from "../context/AuthContext";

export default function Home() {
  const { user } = useAuth();
  return (
    <div className="container">
      <div className="card" style={{ textAlign: "center", padding: 60 }}>
        <h1>Welcome to JobOrbit</h1>
        <p style={{ color: "#6b7280", maxWidth: 560, margin: "8px auto 24px" }}>
          A smart recruitment platform that matches candidates to jobs using
          a skill-based scoring engine — connecting the right talent with the right opportunity.
        </p>
        <div className="flex" style={{ justifyContent: "center", gap: 12 }}>
          <Link to="/jobs" className="btn btn-primary">Browse Jobs</Link>
          {!user && <Link to="/register" className="btn btn-outline">Get Started</Link>}
        </div>
      </div>

      <div className="grid-2">
        <div className="card">
          <h3>For Candidates</h3>
          <p>Build your profile, upload your resume, and get jobs ranked by how well your skills match.</p>
        </div>
        <div className="card">
          <h3>For Recruiters</h3>
          <p>Post openings, manage applicants, and track candidates through your hiring pipeline.</p>
        </div>
      </div>
    </div>
  );
}
