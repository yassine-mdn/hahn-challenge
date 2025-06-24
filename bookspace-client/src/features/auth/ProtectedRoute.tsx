import React, {useEffect} from "react";
import { useAuth } from "./AuthContext";
import type {AuthenticationResponseDTORoleEnum} from "@/types/authentication-response-dto";
import { useNavigate } from "react-router";

interface ProtectedRouteProps {
  children: React.ReactNode;
  requiredRole?: AuthenticationResponseDTORoleEnum;
}

const ProtectedRoute: React.FC<ProtectedRouteProps> = ({ children, requiredRole }) => {
  const { isAuthenticated, role } = useAuth();
  const navigate = useNavigate();

  useEffect(() => {
    if (!isAuthenticated) {
      navigate("/login", { replace: true });
    }

    if (requiredRole && role != requiredRole) {
      navigate("/login", { replace: true });
    }
  }, [isAuthenticated, navigate, requiredRole, role]);

  return <>{children}</>;
};

export default ProtectedRoute; 