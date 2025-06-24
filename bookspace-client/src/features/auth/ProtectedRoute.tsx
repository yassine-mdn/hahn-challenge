import React, {useEffect} from "react";
import { useAuth } from "./AuthContext";
import type {AuthenticationResponseDTORoleEnum} from "@/types/authentication-response-dto";
import { useNavigate } from "react-router";

interface ProtectedRouteProps {
  children: React.ReactNode;
  requiredRole?: AuthenticationResponseDTORoleEnum;
}

const ProtectedRoute: React.FC<ProtectedRouteProps> = ({ children, requiredRole }) => {
  const { isAuthenticated, role, loading } = useAuth();
  const navigate = useNavigate();

  useEffect(() => {
    if (loading) return;

    if (!isAuthenticated) {
      navigate("/login", { replace: true });
    } else if (requiredRole && role !== requiredRole) {
      navigate("/login", { replace: true });
    }
  }, [isAuthenticated, navigate, requiredRole, role, loading]);

  if (loading) {
    return <div>Loading...</div>;
  }

  return <>{children}</>;
};

export default ProtectedRoute; 