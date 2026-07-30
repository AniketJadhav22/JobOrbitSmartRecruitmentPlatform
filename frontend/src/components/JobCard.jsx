import React from "react";
import { Link } from "react-router-dom";

export default function JobCard({ job }) {
  return (
    <div className="card">
      <div className="flex-between">
        <div>
          <h3 style={{ margin: "0 0 4px" }}>
            <Link to={`/jobs/${job.id}`}>{job.title}</Link>
          </h3>
          <div style={{ color: "#6b7280", fontSize: 14 }}>
            {job.companyName || "Company"} · {job.location || "Remote"} · {job.jobType?.replace("_", " ")}
          </div>
        </div>
        {typeof job.matchScore === "number" && (
          <span className={`badge ${job.matchScore >= 70 ? "badge-green" : job.matchScore >= 40 ? "badge-yellow" : "badge-red"}`}>
            {job.matchScore}% match
          </span>
        )}
      </div>
      <p style={{ color: "#374151", fontSize: 14 }}>
        {job.description?.slice(0, 160)}{job.description?.length > 160 ? "..." : ""}
      </p>
      <div style={{ fontSize: 13, color: "#6b7280" }}>
        Skills: {job.skillsRequired}
      </div>
    </div>
  );
}
