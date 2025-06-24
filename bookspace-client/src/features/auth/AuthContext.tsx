import {createContext, type ReactNode, useContext, useEffect, useState} from "react";
import Cookies from "js-cookie";
import type {AuthenticationResponseDTORoleEnum} from "@/types/authentication-response-dto";
import type {RegisterDTO} from "@/types/register-dto.ts";
import * as authAPI from "@/services/auth.service"
import type {AuthenticationRequestDTO} from "@/types/authentication-request-dto.ts";
import {useNavigate} from "react-router";

interface AuthContextType {
  user: string | null;
  role: AuthenticationResponseDTORoleEnum | null;
  login: (auth: AuthenticationRequestDTO) => void;
  signup: (auth: RegisterDTO) => void;
  logout: () => void;
  updateUser: (username: string) => void;
  isAuthenticated: boolean;
  loading: boolean;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export const AuthProvider = ({ children }: { children: ReactNode }) => {
  const [user, setUser] = useState<string | null>(null);
  const [role, setRole] = useState<AuthenticationResponseDTORoleEnum | null>(null);
  const [accessToken, setAccessToken] = useState<string | null>(null);
  const [refreshToken, setRefreshToken] = useState<string | null>(null);
  const [loading, setLoading] = useState(true);
  const navigate = useNavigate();

  useEffect(() => {
    const storedUser = Cookies.get("user");
    const storedRole = Cookies.get("role") as AuthenticationResponseDTORoleEnum | undefined;
    const storedAccessToken = Cookies.get("accessToken");
    const storedRefreshToken = Cookies.get("refreshToken");
    if (storedUser && storedRole && storedAccessToken && storedRefreshToken) {
      setUser(storedUser);
      setRole(storedRole);
      setAccessToken(storedAccessToken);
      setRefreshToken(storedRefreshToken);
    }
    setLoading(false);
  }, []);

  const login = async (data: AuthenticationRequestDTO) => {
    const res = await authAPI.login(data);
      setUser(res.username);
      setRole(res.role);
      setAccessToken(res.accessToken);
      setRefreshToken(res.refreshToken);
      Cookies.set("user", res.username);
      Cookies.set("role", res.role);
      Cookies.set("accessToken", res.accessToken);
      Cookies.set("refreshToken", res.refreshToken);
      if (res.role == "ADMIN")
        navigate("/admin");
      else
        navigate("/");
  };

  const signup = async(data: RegisterDTO) => {
    const res = await authAPI.register(data);
    Cookies.set("user", res.username);
    Cookies.set("role", res.role);
    Cookies.set("accessToken", res.accessToken);
    Cookies.set("refreshToken", res.refreshToken);
    navigate("/");
  }

  const logout = () => {
    setUser(null);
    setRole(null);
    setAccessToken(null);
    setRefreshToken(null);
    Cookies.remove("user");
    Cookies.remove("role");
    Cookies.remove("accessToken");
    Cookies.remove("refreshToken");
    navigate("/");
  };

  const updateUser = (username: string) => {
    setUser(username);
    Cookies.set("user", username);
  };

  const isAuthenticated = !!accessToken;

  return (
    <AuthContext.Provider value={{ user, role, login, signup, logout, updateUser, isAuthenticated, loading }}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (context === undefined) {
    throw new Error("useAuth must be used within an AuthProvider");
  }
  return context;
}; 