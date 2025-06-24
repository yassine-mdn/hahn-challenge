import axios from "axios";
import instance from "../../../../../Downloads/bookspace-client/bookspace-client/src/api/axios";
import type {AuthenticationRequestDTO} from "@/types/authentication-request-dto.ts";
import type {RegisterDTO} from "@/types/register-dto.ts";
import type {AuthenticationResponseDTO} from "@/types/authentication-response-dto.ts";

const BASE_URL = "http://localhost:1001/api/v1/auth";

export const login = async (data: AuthenticationRequestDTO):Promise<AuthenticationResponseDTO> => {
  const res = await axios.post(`${BASE_URL}/login`, data);
  return res.data;
};

export const register = async (data: RegisterDTO): Promise<AuthenticationResponseDTO> => {
  const res = await axios.post(`${BASE_URL}/sign-in`, data);
  return res.data;
};

export const refreshToken = async (): Promise<AuthenticationResponseDTO> => {
  const res = await instance.post(`/auth/refresh-token`);
  return res.data;
};
