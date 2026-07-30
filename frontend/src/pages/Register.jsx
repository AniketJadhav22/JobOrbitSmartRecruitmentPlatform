import React, { useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import { useAuth } from "../context/AuthContext";

export default function Register() {
  const [form, setForm] = useState({
    fullName: "", email: "", password: "", phone: "", role: "CANDIDATE", companyName: "",
  });
  const [error, setError] = useState("");
  const { register } = useAuth();
  const navigate = useNavigate();

  const handleChange = (e) => setForm({ ...form, [e.target.name]: e.target.value });

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");
    try {
      const data = await register(form);
      navigate(data.role === "RECRUITER" ? "/recruiter" : "/candidate");
    } catch (err) {
      setError(err.response?.data?.message || "Registration failed");
    }
  };

  return (
    <div className="container" style={{ maxWidth: 460 }}>
      <div className="card">
        <h2>Create your JobOrbit account</h2>
        {error && <div className="error-text">{error}</div>}
        <form onSubmit={handleSubmit}>
          <label>Full Name</label>
          <input name="fullName" value={form.fullName} onChange={handleChange} required />
          <label>Email</label>
          <input type="email" name="email" value={form.email} onChange={handleChange} required />
          <label>Password</label>
          <input type="password" name="password" value={form.password} onChange={handleChange} required />
          <label>Phone</label>
          <input name="phone" value={form.phone} onChange={handleChange} />
          <label>I am a</label>
          <select name="role" value={form.role} onChange={handleChange}>
            <option value="CANDIDATE">Job Seeker</option>
            <option value="RECRUITER">Recruiter</option>
          </select>
          {form.role === "RECRUITER" && (
            <>
              <label>Company Name</label>
              <input name="companyName" value={form.companyName} onChange={handleChange} />
            </>
          )}
          <button className="btn btn-primary" style={{ width: "100%" }} type="submit">Create Account</button>
        </form>
        <p style={{ fontSize: 13, marginTop: 12 }}>
          Already registered? <Link to="/login">Login</Link>
        </p>
      </div>
    </div>
  );
}
