import React from "react";
import { Link, useNavigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";

export default function Navbar() {
  const { user, logout } = useAuth();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate("/login");
  };

  const dashboardLink =
    user?.role === "ADMIN" ? "/admin" :
    user?.role === "RECRUITER" ? "/recruiter" :
    user?.role === "CANDIDATE" ? "/candidate" : "/";

  return (
    <nav style={{ background: "#111827", color: "#fff" }}>
      <div className="container flex-between" style={{ padding: "14px 16px" }}>
        <Link to="/" style={{ fontWeight: 700, fontSize: 20 }}>
          JobOrbit <span style={{ color: "#60a5fa" }}>●</span>
        </Link>
        <div className="flex" style={{ gap: 20, alignItems: "center" }}>
          <Link to="/jobs">Browse Jobs</Link>
          {user ? (
            <>
              <Link to={dashboardLink}>Dashboard</Link>
              <span style={{ color: "#9ca3af", fontSize: 13 }}>{user.fullName} ({user.role})</span>
              <button className="btn btn-outline" style={{ color: "#fff", borderColor: "#fff" }} onClick={handleLogout}>
                Logout
              </button>
            </>
          ) : (
            <>
              <Link to="/login">Login</Link>
              <Link to="/register" className="btn btn-primary">Sign Up</Link>
            </>
          )}
        </div>
      </div>
    </nav>
  );
}
