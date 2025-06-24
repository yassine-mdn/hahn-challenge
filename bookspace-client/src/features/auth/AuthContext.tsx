import { createContext, useContext, useState, useEffect, type ReactNode } from "react";
import Cookies from "js-cookie";
import { type AuthenticationResponseDTO, AuthenticationResponseDTORoleEnum } from "@/types/authentication-response-dto";

interface AuthContextType {
  user: string | null;
  role: AuthenticationResponseDTORoleEnum | null;
  login: (auth: AuthenticationResponseDTO) => void;
  logout: () => void;
  isAuthenticated: boolean;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export const AuthProvider = ({ children }: { children: ReactNode }) => {
  const [user, setUser] = useState<string | null>(null);
  const [role, setRole] = useState<AuthenticationResponseDTORoleEnum | null>(null);
  const [accessToken, setAccessToken] = useState<string | null>(null);
  const [refreshToken, setRefreshToken] = useState<string | null>(null);

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
  }, []);

  const login = (auth: AuthenticationResponseDTO) => {
    if (auth.username && auth.role && auth.accessToken && auth.refreshToken) {
      setUser(auth.username);
      setRole(auth.role);
      setAccessToken(auth.accessToken);
      setRefreshToken(auth.refreshToken);
      Cookies.set("user", auth.username);
      Cookies.set("role", auth.role);
      Cookies.set("accessToken", auth.accessToken);
      Cookies.set("refreshToken", auth.refreshToken);
    }
  };

  const logout = () => {
    setUser(null);
    setRole(null);
    setAccessToken(null);
    setRefreshToken(null);
    Cookies.remove("user");
    Cookies.remove("role");
    Cookies.remove("accessToken");
    Cookies.remove("refreshToken");
  };

  const isAuthenticated = !!accessToken;

  return (
    <AuthContext.Provider value={{ user, role, login, logout, isAuthenticated }}>
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