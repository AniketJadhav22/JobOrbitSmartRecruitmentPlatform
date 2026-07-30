import React, { useEffect, useState } from "react";
import api from "../api/axiosConfig";
import JobCard from "../components/JobCard";

export default function JobList() {
  const [jobs, setJobs] = useState([]);
  const [keyword, setKeyword] = useState("");
  const [location, setLocation] = useState("");
  const [loading, setLoading] = useState(true);

  const fetchJobs = async (kw = "", loc = "") => {
    setLoading(true);
    const { data } = await api.get("/jobs/public/search", { params: { keyword: kw, location: loc } });
    setJobs(data);
    setLoading(false);
  };

  useEffect(() => { fetchJobs(); }, []);

  const handleSearch = (e) => {
    e.preventDefault();
    fetchJobs(keyword, location);
  };

  return (
    <div className="container">
      <h2>Browse Open Positions</h2>
      <form onSubmit={handleSearch} className="flex" style={{ gap: 10, marginBottom: 20 }}>
        <input placeholder="Skill or job title..." value={keyword} onChange={(e) => setKeyword(e.target.value)} style={{ marginBottom: 0 }} />
        <input placeholder="Location..." value={location} onChange={(e) => setLocation(e.target.value)} style={{ marginBottom: 0 }} />
        <button className="btn btn-primary" type="submit">Search</button>
      </form>

      {loading ? <p>Loading jobs...</p> : jobs.length === 0 ? <p>No jobs found.</p> :
        jobs.map((job) => <JobCard key={job.id} job={job} />)}
    </div>
  );
}
